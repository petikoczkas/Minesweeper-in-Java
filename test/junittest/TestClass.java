package junittest;

import org.junit.After;
import org.junit.Assert;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;

import game.Board;
import game.Leaderboard;
import game.Menu;
import game.Runner;
import game.Stopper;

public class TestClass {

	//sz�ks�ges adatok a tesztel�shez
	JFrame frame=new JFrame();
	JPanel panel=new JPanel();
	Leaderboard l;
	Stopper s;
	Board b;
	Menu m;
	String data; //vissza�ll�t�shoz sz�ks�ges
	
	//leteszteli, hogy a stopper t�nyleg m�sodpercenk�nt v�lt-e
	@Test
	public void testStopperTimer() {
		s=new Stopper();
		Date d=new Date();
		d.setTime(d.getTime()-1000);
		s.timer(new Date(), d);
		Assert.assertEquals(1, s.getTime());
	}
	
	//ha a Leaderboardnak hib�s .txt f�jlnevet adn�nk meg, akkor megfelel� errort kapn�nk-e
	@Test (expected= NullPointerException.class)
	public void testLeaderboardExeption() throws NullPointerException {
		l=new Leaderboard(frame, panel, "Error");
	}
	
	//teszteli, hogy t�nyleg megt�rt�nt-e az adatok f�jlb�l val� beolvas�sa
	@Test
	public void testLeaderboardFilein() {
		l=new Leaderboard(frame, panel, "Easy");
		Assert.assertTrue(l.getData(0, 0)!=null);
	}
	
	//teszteli, hogy t�nyleg megt�rt�nt-e az adatok f�jlba val� ki�r�sa
	@Test
	public void testLeaderboardFileout() throws IOException {
		l=new Leaderboard(frame, panel, "Hard");
		l.setCurrdiff("Hard");
		data=l.getData(0, 1);
		l.setData(0, 1, "Test");
		l.fileout();
		Leaderboard l2=new Leaderboard(frame, panel, "Hard");
		Assert.assertEquals("Test", l2.getData(0, 1));
	}
	
	//a konstruktor megfelel�en adta-e �t a megadott adatokat, jelen esetben bombasz�m
	@Test
	public void testBoardMinenum() {
		m=new Menu();
		b=new Board(frame, panel, 9, 9, 10);
		Assert.assertEquals(10, b.getMINE());
	}
	
	//a program a bombasz�mb�l �t tudta-e kalkul�lni, hogy mennyi a haszn�lhat� z�szl�k sz�ma
	@Test
	public void testFlagnum() {
		m=new Menu();
		b=new Board(frame, panel, 16, 16, 40);
		Assert.assertEquals(40, b.getFlags());
	}
	
	//sikeres volt-e a mez�ket megjel�lni, hogy azokon a helyeken helyezkednek el a bomb�k
	@Test
	public void testBoardMinecount() {
		m=new Menu();
		b=new Board(frame, panel, 16, 16, 40);
		int mine=0;
		for(int i=0; i<16; i++) {
			for(int j=0; j<16; j++) {
				if(b.getList().get(i).get(j).isMine()) mine++;
			}
		}
		Assert.assertEquals(40, mine);
	}
	
	
	//megfelel�en m�k�dik-e annak a kisz�m�t�sa, hogy h�ny bomba van az adott mez� k�r�l
	@Test
	public void testBoardCountMines() {
		m=new Menu();
		b=new Board(frame, panel, 16, 16, 40);
		for(int i=0; i<16; i++) {
			for(int j=0; j<16; j++) {
				Assert.assertFalse(b.getList().get(i).get(j).getCount()>8 || b.getList().get(i).get(j).getCount()<0);
			}
		}
	}
	
	//veszt�s eset�n vizsg�lja, hogy val�ban felfed�sre ker�lt-e az �sszes mez�
	@Test
	public void testBoardaftergameover() {
		m=new Menu();
		m.setGameover(true);
		m.setWin(false);
		b=new Board(frame, panel, 16, 16, 40);
		b.gameOver();
		for(int i=0; i<16; i++) {
			for(int j=0; j<16; j++) {
				Assert.assertFalse(b.getList().get(i).get(j).isEnabled() && !(b.getList().get(i).get(j).isMine()));
			}
		}
	}
	
	//megn�zi, hogy val�ban j�l m�k�dik-e a ranglista n�vekv� sorrendbe helyez�se id�eredm�ny alapj�n
	@Test
	public void testMenuArrange() {
		l=new Leaderboard(frame, panel, "Hard");
		m=new Menu();
		l.setData(0, 2, "10");
		l.setData(1, 2, "5");
		m.arrange();
		Assert.assertEquals("5" ,l.getData(0, 2));
	}
	//m�dos�t�sok visszavon�sa
	@After
	public void undo() throws IOException {
		l=new Leaderboard(frame, panel, "Hard");
		l.setData(0, 1, data);
		l.fileout();
	}

}
