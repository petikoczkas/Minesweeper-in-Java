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
	
	private JFrame frame; //az ablak amiben létrejön az egész program
	private JButton button1; 
	private JButton button2; //gombok melyek a menükezeléshez szükségesek
	private JButton button3;
	private int time=0; //az adott idõ, ha a játékos nyert
	private static boolean click=false; //az elsõ kattintás megtörtént-e
	private static boolean gameover=false; //játék vége
	private static boolean win=false; //nyert-e
	static JButton retry; // új játékot indító gomb
	private int row=9, col=9, mine=10; //a pálya létrehozásához szükséges adatok
	
	private JPanel header=new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 5)); //fejléc a játék futása alatt
	private JTextField nametext;
	private String name; //játékos neve
	private JPanel board;
	private JLabel namelabel;
	
	static Color bezs=new Color(218, 203, 179); //háttér színe
	private Color lightblue=new Color(51,153,255); //gombok színe
	
	
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

	//beállítja az adott gombot
	public void addButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, 30));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setForeground(Color.BLACK);
		button.setBackground(lightblue);
		button.addMouseListener(this);
	}
	
	//beállítja az adott panelt
	public void addPanel(JPanel panel) {
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.setPreferredSize(new Dimension(450, 100));
	    panel.setMaximumSize(new Dimension(450, 100));
	    panel.setBackground(bezs);
	}

	//konstruktor
	public Menu(){
		//létrehozza, beállítja az ablakot
		frame=new JFrame("Minesweeper");
		frame.setSize(450, 450);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		//hozzáadja a frame-hez a gombokat
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
	
	//a ranglistát az elért idõ szerint rendezi növekvõ sorrendbe
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
	
	//header panel beállítása
	public void setheader(int x) {	
		header.setMaximumSize(new Dimension(x, 300));
		header.setPreferredSize(new Dimension(x, 30));
		header.setBackground(bezs);
	}
	
	//új játéknál alaphelyzetbe állítás
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
		//a bal kattintás lehetséges kimenetelei
		if(e.getButton()==1) {
			//Exit esetén kilépés, adatok elmentése
			if(b.getText()=="Exit") {
				try {
					Leaderboard.fileout();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
				System.exit(0);
			}
			//Új játék esetén átnevezi a gombokat, hogy ki lehessen választani a nehézségi szintet
			else if(b.getText()=="New Game") {
				button1.setText("Easy");
				button2.setText("Medium");
				button3.setText("Hard");
				button1.setVisible(false);
				button2.setVisible(false);
				button3.setVisible(false);
				
				//név bekérése a nehézségi szint kiválasztása elott
				nametext=new JTextField(10);
				nametext.setFont(new Font("Arial", Font.PLAIN, 20));
				namelabel=new JLabel("Name: ");
				namelabel.setFont(new Font("Arial", Font.BOLD, 20));
				board.add(namelabel);
				board.add(nametext);
				nametext.addKeyListener(this);
				
			}
			//A ranglista megnézéséhez elõször ki kell választani a megtekinteni kívánt ranglistát
			else if(b.getText()=="Leaderboard") {
				button1.setText("Easy Leaderboard");
				button2.setText("Medium Leaderboard");
				button3.setText("Hard Leaderboard");
			}
			//Megjeleníti a megfelelõ ranglistát
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
			//Visszalép a fõmenübe
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
			//Megjeleníti a megfelelõ nehézségû táblát és elkezdõdik a játék
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
			//Újrakezdi az adott nehézségû játékot
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
			//az Enter megnyomásával lehet elmenteni a nevet, ilyenkor elérhetõvé válik a nehézségi szint kiválasztása
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