package game;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Leaderboard{

	private static String currdiff=null; //aktuális nehézségi szint
	private String[] header = { "Position", "Name", "Time" }; //a tábla fejléce

	private static String[][] data=new String[10][3];; //a tábla adatai
	static JTable table; //maga a tábla
	
	
	public static String getData(int i, int j) {
		return data[i][j];
	}
	
	public static void setData(int i, int j, String data) {
		Leaderboard.data[i][j] = data;
	}
	
	public static void setCurrdiff(String currdiff) {
		Leaderboard.currdiff = currdiff;
	}

	//beolvassa a megfelelõ fájlból az adatokat
	public static void filein(String diff) throws NullPointerException, IOException {
		FileReader fr=null;
		BufferedReader br=null;
		int pos=1;
		String line;
		if(diff=="Easy") {
			fr=new FileReader("easy.txt");
		}
		else if(diff=="Medium") {
			fr=new FileReader("medium.txt");
		}
		else if(diff=="Hard"){
			fr=new FileReader("hard.txt");
		}
		br=new BufferedReader(fr);
		for(int i=0; i<10; i++) {
			for(int j=1; j<3; j++) {
				if((line=br.readLine())!=null) {
						data[i][j]=line;
				}
				else{
					data[i][j]=null;
				}
			}
			data[i][0]=String.valueOf(pos++);
		}
		fr.close();
		br.close();
	}
	
	//kiírja a megfelelõ fájlba az adatokat
	public static void fileout() throws IOException {
		if(currdiff!=null) {
			FileWriter fw=null;
			PrintWriter pw=null;
			if(currdiff=="Easy") {
				fw=new FileWriter("easy.txt");

			}
			else if(currdiff=="Medium") {
				fw=new FileWriter("medium.txt");
			}
			else {
				fw=new FileWriter("hard.txt");
			}
			pw=new PrintWriter(fw);
			for(int i=0; i<10; i++) {
				for(int j=1; j<3; j++) {
					if(data[i][j]!=null) pw.println(data[i][j]);
				}
			}
			fw.close();
			pw.close();
		}
	}
	
	//konstruktor
	public Leaderboard(JFrame frame, JPanel board, String diff){
		try {
			filein(diff);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		//létrehozza és megjeleníti a táblát
		table=new JTable(data, header);
		table.setEnabled(false);
		table.setBackground(Menu.bezs);
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.setGridColor(Color.BLACK);
		table.setMaximumSize(new Dimension(300,300));
		table.setSize(200,200);
		
		table.getTableHeader().setBorder(null);
		table.getTableHeader().setBackground(Menu.bezs);
		table.getTableHeader().setEnabled(false);
		
		board.setMaximumSize(new Dimension(450,450));
		board.add(table.getTableHeader());
		board.add(table);
	}
}
