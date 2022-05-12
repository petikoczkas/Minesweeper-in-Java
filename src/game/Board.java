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
	
	private int flags=10; //zászlók száma, amennyit használhat a játékos
	private JLabel mine=new JLabel(); //a bombaszám megjelenítéséhez szükséges JLabel
	
	private int ROW=9; //sorok száma
	private int COL=9; //oszlopok száma
	private int MINE=10; //bombák száma
	
	private ArrayList<ArrayList<Tile>> board = new ArrayList<ArrayList<Tile>>(ROW); //Lista, ami a mezõket tárolja
	
	public ArrayList<ArrayList<Tile>> getList(){
		return board;
	}

	public int getMINE() {
		return MINE;
	}

	public int getFlags() {
		return flags;
	}

	//véletlenszerûen legenerálja a bombák helyét a táblán
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
	
	//segédfüggvény a countMines-hoz ami végigmegy a szomszédos mezõkön és megnézi hogy bomba-e az adott mezõ
	public void updateCount(int r, int c) {
		if(!board.get(r).get(c).isMine()) return;
		
		for(int row=r-1; row<=r+1; row++) {
			for(int col=c-1; col<=c+1; col++) {
				try {
					board.get(row).get(col).increaseCount();
				}
				catch(Exception e) {
					//nem csinál semmit, ekkor kimegy a pályáról
				}
			}
		}
	}
	
	//megszámolja, hogy egy mezõ körül hány bomba található
	public void countMines() {
		for(int row=0; row<ROW; row++) {
			for(int col=0; col<COL; col++) {
				updateCount(row,col);		
			}
		}
	}
	
	//felfedi az összes mezõt, ha a játékos bombára kattint
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
	
	//konstruktor, ami létrehozza a felületet
	public Board(JFrame frame, JPanel header, int R, int C, int minenum) {
		ROW=R;
		COL=C;
	
		//a Menu-tõl kapott frémet letisztítja
		frame.getContentPane().removeAll();
		header.removeAll();
		
		MINE=minenum;
		flags=MINE;
		
		//a mezõk elrendezéséhez szükséges layout
		this.setLayout(new GridLayout(ROW,COL));
		
		//feltölti a két dimenziós lista második szintjeit
		for(int row = 0; row < ROW; row++)  {
	        board.add(new ArrayList<Tile>(COL));
	    }
		
		//létrehozza a mezõket
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
		
		//szükséges megjelenítési lépések
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
	
	//rekurzív függvény, felfedi azokat a mezõket és szomszédjait, amiknek a szomszédjában nincs bomba
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
		fill(r-1,c-1); //bal fenti átló
		fill(r-1,c+1); //jobb fenti átló
		fill(r+1,c-1); //bal lenti átló
		fill(r+1,c+1); //jobb lenti átló
	}

	//ha a játékos használja a zászlókat, akkor csökkenti/növeli a számukat és megjeleníti
	public void flagnum(Tile t) {
		if(!t.isFlagged()) flags++;
		else flags--;
		mine.setText(" Mines: "+flags);
	}
	
	//megvizsgálja, hogy a játékos nyert-e
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
		//csak akkor nézi a kattintásokat ha még fut a játék
		if(!Menu.isGameover() && !Menu.isWin()) {
			Tile t=(Tile)(e.getComponent());
			
			//a bal kattintás lehetséges kimenetelei
			//csak akkor nézzük a kattintást ha az adott mezõ nincs megjelölve
			if(e.getButton()==1 && !t.isFlagged()) {
				int db=0;
				for(int row=0; row<ROW; row++) {
					for(int col=0; col<COL; col++) {
						if(board.get(row).get(col).isEnabled()) db++;
					}
				}
				//elsõ kattintás
				if(db==ROW*COL) {
					Menu.setClick(true);
				}
				//ha bombára kattint a játék véget ér
				if(t.isMine()) {
					gameOver();
				}
				//különben fut tovább a játék
				else{
					fill(t.getRow(), t.getCol());
				}
				//vizsgálja, hogy nyert-e a játékos
				win();
			}
			
			//a jobb kattintás lehetséges kimenetelei
			else if(e.getButton()==3 && !Menu.isWin()) {
				
				//ha megjelöl egy fedett mezõt akkor azt megjeleníti
				if(!t.isFlagged() && flags>0 && t.isEnabled()) {
					t.setIcon(new ImageIcon("flag.png"));
					t.setFlagged(true);
					flagnum(t);
				}
				//ha megszüntet egy jelölést, akkor eltávolítja
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