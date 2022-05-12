package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener{
	
	private int flags=10; //z�szl�k sz�ma, amennyit haszn�lhat a j�t�kos
	private JLabel mine=new JLabel(); //a bombasz�m megjelen�t�s�hez sz�ks�ges JLabel
	
	private int ROW=9; //sorok sz�ma
	private int COL=9; //oszlopok sz�ma
	private int MINE=10; //bomb�k sz�ma
	
	private ArrayList<ArrayList<Tile>> board = new ArrayList<ArrayList<Tile>>(ROW); //Lista, ami a mez�ket t�rolja
	
	public ArrayList<ArrayList<Tile>> getList(){
		return board;
	}

	public int getMINE() {
		return MINE;
	}

	public int getFlags() {
		return flags;
	}

	//v�letlenszer�en legener�lja a bomb�k hely�t a t�bl�n
	public void generateMines() {
		int bombs=0;
		while(bombs<MINE) {
			int row= (int)(Math.random()*board.size());
			int col= (int)(Math.random()*board.get(0).size());
			
			while(board.get(row).get(col).isMine()) {
				row= (int)(Math.random()*board.size());
				col= (int)(Math.random()*board.get(0).size());
			}
			board.get(row).get(col).setMine(true);
			bombs++;
		}
	}
	
	//seg�df�ggv�ny a countMines-hoz ami v�gigmegy a szomsz�dos mez�k�n �s megn�zi hogy bomba-e az adott mez�
	public void updateCount(int r, int c) {
		if(!board.get(r).get(c).isMine()) return;
		
		for(int row=r-1; row<=r+1; row++) {
			for(int col=c-1; col<=c+1; col++) {
				try {
					board.get(row).get(col).increaseCount();
				}
				catch(Exception e) {
					//nem csin�l semmit, ekkor kimegy a p�ly�r�l
				}
			}
		}
	}
	
	//megsz�molja, hogy egy mez� k�r�l h�ny bomba tal�lhat�
	public void countMines() {
		for(int row=0; row<ROW; row++) {
			for(int col=0; col<COL; col++) {
				updateCount(row,col);		
			}
		}
	}
	
	//felfedi az �sszes mez�t, ha a j�t�kos bomb�ra kattint
	public void gameOver() {
		Menu.setGameover(true);
		for(int row=0; row<ROW; row++) {
			for(int col=0; col<COL; col++) {
				board.get(row).get(col).setIcon(null);
				if(board.get(row).get(col).isMine()) {
					board.get(row).get(col).setIcon(new ImageIcon("mine.png"));
					board.get(row).get(col).setBackground(Color.WHITE);
				}
				else {
					if(board.get(row).get(col).getCount()!=0) board.get(row).get(col).setText(board.get(row).get(col).getCount()+"");
					board.get(row).get(col).setEnabled(false);
				}
			}
		}
	}
	
	//konstruktor, ami l�trehozza a fel�letet
	public Board(JFrame frame, JPanel header, int R, int C, int minenum) {
		ROW=R;
		COL=C;
	
		//a Menu-t�l kapott fr�met letiszt�tja
		frame.getContentPane().removeAll();
		header.removeAll();
		
		MINE=minenum;
		flags=MINE;
		
		//a mez�k elrendez�s�hez sz�ks�ges layout
		this.setLayout(new GridLayout(ROW,COL));
		
		//felt�lti a k�t dimenzi�s lista m�sodik szintjeit
		for(int row = 0; row < ROW; row++)  {
	        board.add(new ArrayList<Tile>(COL));
	    }
		
		//l�trehozza a mez�ket
		for(int row=0; row<ROW; row++) {
			for(int col=0; col<COL; col++) {
				Tile t=new Tile(row,col);
				t.addMouseListener(this);
				this.add(t);
				board.get(row).add(t);
			}
		}
		this.generateMines();
		this.countMines();
		
		//sz�ks�ges megjelen�t�si l�p�sek
		mine.setBackground(Menu.bezs);
		mine.setFont(new Font("Arial", Font.BOLD, 18));
		mine.setForeground(Color.BLACK);
		
		this.setBackground(Menu.bezs);
		
		mine.setText("Mines: "+MINE);
		
		Runner.timer.setBackground(Menu.bezs);
		Runner.timer.setFont(new Font("Arial", Font.BOLD, 18));
		Runner.timer.setText("Time: "+0);

		header.add(mine, FlowLayout.LEFT);
		header.add(Menu.retry, FlowLayout.CENTER);
		header.add(Runner.timer, FlowLayout.RIGHT);
		
		frame.add(header, BorderLayout.NORTH);
		frame.add(this);
		frame.setVisible(true);
	}
	
	//rekurz�v f�ggv�ny, felfedi azokat a mez�ket �s szomsz�djait, amiknek a szomsz�dj�ban nincs bomba
	public void fill(int r, int c) {
		if(r<0 || r>=ROW || c<0 || c>=COL) return;
		if(!board.get(r).get(c).isEnabled()) return;
		if(board.get(r).get(c).isFlagged()==true) return;
		if(board.get(r).get(c).isMine()) return;
		if(board.get(r).get(c).getCount()!=0) {
			board.get(r).get(c).setText(board.get(r).get(c).getCount()+"");
			board.get(r).get(c).setEnabled(false);
			return;
		}
		board.get(r).get(c).setEnabled(false);
		fill(r-1,c); //fel
		fill(r+1,c); //le
		fill(r,c-1); //balra
		fill(r,c+1); //jobbra
		fill(r-1,c-1); //bal fenti �tl�
		fill(r-1,c+1); //jobb fenti �tl�
		fill(r+1,c-1); //bal lenti �tl�
		fill(r+1,c+1); //jobb lenti �tl�
	}

	//ha a j�t�kos haszn�lja a z�szl�kat, akkor cs�kkenti/n�veli a sz�mukat �s megjelen�ti
	public void flagnum(Tile t) {
		if(!t.isFlagged()) flags++;
		else flags--;
		mine.setText(" Mines: "+flags);
	}
	
	//megvizsg�lja, hogy a j�t�kos nyert-e
	public void win() {
		int db=0;
		for(int row=0; row<ROW; row++) {
			for(int col=0; col<COL; col++) {
				if(board.get(row).get(col).isEnabled()) db++;
			}
		}
		if(!Menu.isGameover() && MINE==db) {
			Menu.setWin(true);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//csak akkor n�zi a kattint�sokat ha m�g fut a j�t�k
		if(!Menu.isGameover() && !Menu.isWin()) {
			Tile t=(Tile)(e.getComponent());
			
			//a bal kattint�s lehets�ges kimenetelei
			//csak akkor n�zz�k a kattint�st ha az adott mez� nincs megjel�lve
			if(e.getButton()==1 && !t.isFlagged()) {
				int db=0;
				for(int row=0; row<ROW; row++) {
					for(int col=0; col<COL; col++) {
						if(board.get(row).get(col).isEnabled()) db++;
					}
				}
				//els� kattint�s
				if(db==ROW*COL) {
					Menu.setClick(true);
				}
				//ha bomb�ra kattint a j�t�k v�get �r
				if(t.isMine()) {
					gameOver();
				}
				//k�l�nben fut tov�bb a j�t�k
				else{
					fill(t.getRow(), t.getCol());
				}
				//vizsg�lja, hogy nyert-e a j�t�kos
				win();
			}
			
			//a jobb kattint�s lehets�ges kimenetelei
			else if(e.getButton()==3 && !Menu.isWin()) {
				
				//ha megjel�l egy fedett mez�t akkor azt megjelen�ti
				if(!t.isFlagged() && flags>0 && t.isEnabled()) {
					t.setIcon(new ImageIcon("flag.png"));
					t.setFlagged(true);
					flagnum(t);
				}
				//ha megsz�ntet egy jel�l�st, akkor elt�vol�tja
				else if(t.isFlagged()) {
					t.setIcon(null);
					t.setFlagged(false);
					flagnum(t);
				}
				win();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}