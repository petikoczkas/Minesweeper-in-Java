package game;

import java.util.Date;


public class Stopper{
	private int time; //a kezd�s �ta eltelt id�
	
	//kisz�m�tja a kezd�s �ta eltelt id�t
	public void timer(Date date, Date startDate) {
		time=(int) ((date.getTime()-startDate.getTime())/1000);
	}

	public int getTime() {
		return time;
	}
	
}
