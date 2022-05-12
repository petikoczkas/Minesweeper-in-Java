package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Menu implements MouseListener,KeyListener {
	
	private JFrame frame; //az ablak amiben l�trej�n az eg�sz program
	private JButton button1; 
	private JButton button2; //gombok melyek a men�kezel�shez sz�ks�gesek
	private JButton button3;
	private int time=0; //az adott id�, ha a j�t�kos nyert
	private static boolean click=false; //az els� kattint�s megt�rt�nt-e
	private static boolean gameover=false; //j�t�k v�ge
	private static boolean win=false; //nyert-e
	static JButton retry; // �j j�t�kot ind�t� gomb
	private int row=9, col=9, mine=10; //a p�lya l�trehoz�s�hoz sz�ks�ges adatok
	
	private JPanel header=new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 5)); //fejl�c a j�t�k fut�sa alatt
	private JTextField nametext;
	private String name; //j�t�kos neve
	private JPanel board;
	private JLabel namelabel;
	
	static Color bezs=new Color(218, 203, 179); //h�tt�r sz�ne
	private Color lightblue=new Color(51,153,255); //gombok sz�ne
	
	
	public JFrame getFrame() {
		return frame;
	}

	public JPanel getHeader() {
		return header;
	}

	
	
	
	public String getName() {
		return name;
	}

	public static boolean isWin() {
		return win;
	}

	public static void setWin(boolean win) {
		Menu.win = win;
	}

	public static boolean isClick() {
		return click;
	}

	public static void setClick(boolean click) {
		Menu.click = click;
	}

	public static boolean isGameover() {
		return gameover;
	}

	public static void setGameover(boolean gameover) {
		Menu.gameover = gameover;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	//be�ll�tja az adott gombot
	public void addButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, 30));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setForeground(Color.BLACK);
		button.setBackground(lightblue);
		button.addMouseListener(this);
	}
	
	//be�ll�tja az adott panelt
	public void addPanel(JPanel panel) {
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.setPreferredSize(new Dimension(450, 100));
	    panel.setMaximumSize(new Dimension(450, 100));
	    panel.setBackground(bezs);
	}

	//konstruktor
	public Menu(){
		//l�trehozza, be�ll�tja az ablakot
		frame=new JFrame("Minesweeper");
		frame.setSize(450, 450);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		//hozz�adja a frame-hez a gombokat
		JPanel newgame=new JPanel();
		addPanel(newgame);
		button1=new JButton("New Game");
		addButton(button1);
		
		board=new JPanel();
		addPanel(board);
		button2=new JButton("Leaderboard");
		addButton(button2);
		
		JPanel exit=new JPanel();
		addPanel(exit);
		button3=new JButton("Exit");
		addButton(button3);
		
		JPanel head=new JPanel();
		head.setMaximumSize(new Dimension(450, 150));
		head.setBackground(bezs);
		JLabel label=new JLabel("Minesweeper");
		label.setBackground(bezs);
		label.setBorder(null);
		label.setFont(new Font("Arial", Font.BOLD, 50));
		label.setForeground(Color.BLACK);
		
		newgame.add(button1);
		board.add(button2);
		exit.add(button3);
		
		head.add(label);
		frame.add(head);
		frame.add(newgame);
		frame.add(board);
		frame.add(exit);
		frame.setVisible(true);
		
		retry=new JButton("Retry");
		retry.setFont(new Font("Arial", Font.BOLD, 13));
		retry.addMouseListener(this);
		
	}
	
	//a ranglist�t az el�rt id� szerint rendezi n�vekv� sorrendbe
	public void arrange() {
		int size=10;
		String temp;
		if(Leaderboard.getData(9,2)==null) {
			int i=9;
			while(Leaderboard.getData(i,2)==null) {
				i--;
			}
			size=i+1;
		}
		for(int i=0; i<size; i++) {
			for(int j=i+1; j<size; j++) {
				if(Integer.parseInt(Leaderboard.getData(i,2))>Integer.parseInt(Leaderboard.getData(j,2))) {
					temp=Leaderboard.getData(i,2);
					Leaderboard.setData(i,2, Leaderboard.getData(j,2));
					Leaderboard.setData(j,2, temp);
					
					temp=Leaderboard.getData(i,1);
					Leaderboard.setData(i,1, Leaderboard.getData(j,1));
					Leaderboard.setData(j,1, temp);
				}
			}
		}
		try {
			Leaderboard.fileout();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	//header panel be�ll�t�sa
	public void setheader(int x) {	
		header.setMaximumSize(new Dimension(x, 300));
		header.setPreferredSize(new Dimension(x, 30));
		header.setBackground(bezs);
	}
	
	//�j j�t�kn�l alaphelyzetbe �ll�t�s
	public void reset() {
		click=false;
		gameover=false;
		win=false;
		time=0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b=(JButton)(e.getComponent());
		//a bal kattint�s lehets�ges kimenetelei
		if(e.getButton()==1) {
			//Exit eset�n kil�p�s, adatok elment�se
			if(b.getText()=="Exit") {
				try {
					Leaderboard.fileout();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
				System.exit(0);
			}
			//�j j�t�k eset�n �tnevezi a gombokat, hogy ki lehessen v�lasztani a neh�zs�gi szintet
			else if(b.getText()=="New Game") {
				button1.setText("Easy");
				button2.setText("Medium");
				button3.setText("Hard");
				button1.setVisible(false);
				button2.setVisible(false);
				button3.setVisible(false);
				
				//n�v bek�r�se a neh�zs�gi szint kiv�laszt�sa elott
				nametext=new JTextField(10);
				nametext.setFont(new Font("Arial", Font.PLAIN, 20));
				namelabel=new JLabel("Name: ");
				namelabel.setFont(new Font("Arial", Font.BOLD, 20));
				board.add(namelabel);
				board.add(nametext);
				nametext.addKeyListener(this);
				
			}
			//A ranglista megn�z�s�hez el�sz�r ki kell v�lasztani a megtekinteni k�v�nt ranglist�t
			else if(b.getText()=="Leaderboard") {
				button1.setText("Easy Leaderboard");
				button2.setText("Medium Leaderboard");
				button3.setText("Hard Leaderboard");
			}
			//Megjelen�ti a megfelel� ranglist�t
			else if(b.getText()=="Easy Leaderboard") {
				button1.setVisible(false);
				button2.setVisible(false);
				button1.getParent().setVisible(false);
				button3.setText("Back");
				new Leaderboard(frame, board, "Easy");
			}
			else if(b.getText()=="Medium Leaderboard") {
				button1.setVisible(false);
				button2.setVisible(false);
				button1.getParent().setVisible(false);
				button3.setText("Back");
				new Leaderboard(frame, board, "Medium");
			}
			else if(b.getText()=="Hard Leaderboard") {
				button1.setVisible(false);
				button2.setVisible(false);
				button1.getParent().setVisible(false);
				button3.setText("Back");
				new Leaderboard(frame, board, "Hard");
			}
			//Visszal�p a f�men�be
			else if(b.getText()=="Back") {
				button1.setVisible(true);
				button2.setVisible(true);
				button1.getParent().setVisible(true);
				board.remove(Leaderboard.table);
				board.remove(Leaderboard.table.getTableHeader());
				addPanel(board);
				button1.setText("New Game");
				button2.setText("Leaderboard");
				button3.setText("Exit");
			}
			//Megjelen�ti a megfelel� neh�zs�g� t�bl�t �s elkezd�dik a j�t�k
			else if(b.getText()=="Easy") {
				Leaderboard.setCurrdiff(b.getText());
				try {
					Leaderboard.filein("Easy");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
				setheader(450);
				frame.setSize(450, 480);
				new Board(frame, header, 9, 9, 10);
				row=9;
				col=9;
				mine=10;
				
			}
			else if(b.getText()=="Medium") {
				Leaderboard.setCurrdiff(b.getText());
				try {
					Leaderboard.filein("Medium");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
				setheader(800);
				frame.setSize(800, 830);
				new Board(frame, header, 16, 16, 40);
				row=16;
				col=16;
				mine=40;

			}
			else if(b.getText()=="Hard") {
				Leaderboard.setCurrdiff(b.getText());
				try {
					Leaderboard.filein("Hard");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
				setheader(1500);
				frame.setSize(1500, 830);
				new Board(frame, header, 16, 30, 99);
				row=16;
				col=30;
				mine=99;
				
			}
			//�jrakezdi az adott neh�zs�g� j�t�kot
			else if(b.getText()=="Retry") {
				reset();
				new Board(frame, header, row, col, mine);
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			//az Enter megnyom�s�val lehet elmenteni a nevet, ilyenkor el�rhet�v� v�lik a neh�zs�gi szint kiv�laszt�sa
			if(!nametext.getText().isEmpty()) {
				name=nametext.getText();
				nametext.setVisible(false);
				namelabel.setVisible(false);
				button1.setVisible(true);
				button2.setVisible(true);
				button3.setVisible(true);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}