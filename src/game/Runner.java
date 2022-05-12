package game;

import java.awt.Color;
import java.util.Date;

import javax.swing.JLabel;

public class Runner{
	static JLabel timer=new JLabel(); //az id� megjelen�t�s�hez sz�ks�ges
	
	public static void main(String[] args) throws java.lang.NullPointerException {
		
		//l�trehozza a j�t�k futtat�s�hoz sz�ks�ges oszt�lyokat, v�ltoz�kat
		Menu menu=new Menu();
		boolean firstrun=true;
		Stopper stopper=new Stopper();
		boolean firststart=true;
		Date startDate=new Date();
		while(true) {
			timer.setForeground(Color.BLACK); //ha a while-ban csak az if lenne nem futna le �s csak �gy tudtam megoldani a probl�m�t
			
			//els� kattint�s ut�n elkezdi sz�molni az eltelt id�t
			if(Menu.isClick() && !Menu.isGameover() && !Menu.isWin()) {
				//az els� kattint�sn�l l�trehozza az adott d�tumot
				if(firststart) {
					startDate=new Date();
					firststart=false;
				}
				timer.setText("Time: "+menu.getTime());
				stopper.timer(new Date(), startDate);
				menu.setTime(stopper.getTime());
			}
			//ha �jrakezd�dik a j�t�k akkor vissza�ll�tja a v�ltoz�k �rt�k�t alaphelyzetbe
			if(Menu.retry.getModel().isArmed()) {
				firststart=true;
				firstrun=true;
			}
			//nyer�s eset�n elmenti az adott nevet �s az el�rt id�t, ha jobb mint a legrosszabb id� a ranglist�n
			if(Menu.isWin() && firstrun) {
				firstrun=false;
				String lasttime=Leaderboard.getData(9,2);
				//ha a ranglista tele van
				if(lasttime!=null) {
					System.out.println("."+lasttime+".");
					if(menu.getTime()<Integer.parseInt(lasttime)) {
						
						Leaderboard.setData(9,1, menu.getName());
						Leaderboard.setData(9,2, String.valueOf(menu.getTime()));
						menu.arrange();
					}
				}
				//ha a ranglista nincs tele
				else {
					int n=0;
					while(Leaderboard.getData(n,2)!=null) {
						n++;
					}
					Leaderboard.setData(n,1, menu.getName());
					Leaderboard.setData(n,2, String.valueOf(menu.getTime()));
					menu.arrange();
				}
			}
		}
	}

}
