package game;

import java.awt.Color;
import java.util.Date;

import javax.swing.JLabel;

public class Runner{
	static JLabel timer=new JLabel(); //az idõ megjelenítéséhez szükséges
	
	public static void main(String[] args) throws java.lang.NullPointerException {
		
		//létrehozza a játék futtatásához szükséges osztályokat, változókat
		Menu menu=new Menu();
		boolean firstrun=true;
		Stopper stopper=new Stopper();
		boolean firststart=true;
		Date startDate=new Date();
		while(true) {
			timer.setForeground(Color.BLACK); //ha a while-ban csak az if lenne nem futna le és csak így tudtam megoldani a problémát
			
			//elsõ kattintás után elkezdi számolni az eltelt idõt
			if(Menu.isClick() && !Menu.isGameover() && !Menu.isWin()) {
				//az elsõ kattintásnál létrehozza az adott dátumot
				if(firststart) {
					startDate=new Date();
					firststart=false;
				}
				timer.setText("Time: "+menu.getTime());
				stopper.timer(new Date(), startDate);
				menu.setTime(stopper.getTime());
			}
			//ha újrakezdõdik a játék akkor visszaállítja a változók értékét alaphelyzetbe
			if(Menu.retry.getModel().isArmed()) {
				firststart=true;
				firstrun=true;
			}
			//nyerés esetén elmenti az adott nevet és az elért idõt, ha jobb mint a legrosszabb idõ a ranglistán
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
