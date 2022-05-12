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

	//szükséges adatok a teszteléshez
	JFrame frame=new JFrame();
	JPanel panel=new JPanel();
	Leaderboard l;
	Stopper s;
	Board b;
	Menu m;
	String data; //visszaállításhoz szükséges
	
	//leteszteli, hogy a stopper tényleg másodpercenként vált-e
	@Test
	public void testStopperTimer() {
		s=new Stopper();
		Date d=new Date();
		d.setTime(d.getTime()-1000);
		s.timer(new Date(), d);
		Assert.assertEquals(1, s.getTime());
	}
	
	//ha a Leaderboardnak hibás .txt fájlnevet adnánk meg, akkor megfelelõ errort kapnánk-e
	@Test (expected= NullPointerException.class)
	public void testLeaderboardExeption() throws NullPointerException {
		l=new Leaderboard(frame, panel, "Error");
	}
	
	//teszteli, hogy tényleg megtörtént-e az adatok fájlból való beolvasása
	@Test
	public void testLeaderboardFilein() {
		l=new Leaderboard(frame, panel, "Easy");
		Assert.assertTrue(l.getData(0, 0)!=null);
	}
	
	//teszteli, hogy tényleg megtörtént-e az adatok fájlba való kiírása
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
	
	//a konstruktor megfelelõen adta-e át a megadott adatokat, jelen esetben bombaszám
	@Test
	public void testBoardMinenum() {
		m=new Menu();
		b=new Board(frame, panel, 9, 9, 10);
		Assert.assertEquals(10, b.getMINE());
	}
	
	//a program a bombaszámból át tudta-e kalkulálni, hogy mennyi a használható zászlók száma
	@Test
	public void testFlagnum() {
		m=new Menu();
		b=new Board(frame, panel, 16, 16, 40);
		Assert.assertEquals(40, b.getFlags());
	}
	
	//sikeres volt-e a mezõket megjelölni, hogy azokon a helyeken helyezkednek el a bombák
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
	
	
	//megfelelõen mûködik-e annak a kiszámítása, hogy hány bomba van az adott mezõ körül
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
	
	//vesztés esetén vizsgálja, hogy valóban felfedésre került-e az összes mezõ
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
	
	//megnézi, hogy valóban jól mûködik-e a ranglista növekvõ sorrendbe helyezése idõeredmény alapján
	@Test
	public void testMenuArrange() {
		l=new Leaderboard(frame, panel, "Hard");
		m=new Menu();
		l.setData(0, 2, "10");
		l.setData(1, 2, "5");
		m.arrange();
		Assert.assertEquals("5" ,l.getData(0, 2));
	}
	//módosítások visszavonása
	@After
	public void undo() throws IOException {
		l=new Leaderboard(frame, panel, "Hard");
		l.setData(0, 1, data);
		l.fileout();
	}

}
