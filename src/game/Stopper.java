package game;

import java.util.Date;


public class Stopper{
	private int time; //a kezdés óta eltelt idõ
	
	//kiszámítja a kezdés óta eltelt idõt
	public void timer(Date date, Date startDate) {
		time=(int) ((date.getTime()-startDate.getTime())/1000);
	}

	public int getTime() {
		return time;
	}
	
}
