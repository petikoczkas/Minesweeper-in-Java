package game;

import javax.swing.JButton;

public class Tile extends JButton{
	
	private int row, col; //az adott sor, oszlop
	private int count; //h�ny bomba van az adott mez� k�r�l
	private boolean isMine; //bomba-e az adott mez�
	private boolean flagged; //meg van-e jel�lve az adott mz�
	
	public Tile(int r, int c) {
		row=r;
		col=c;
		count=0;
		flagged=false;
		isMine=false;
	}
	
	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	//n�veli azt, hogy h�ny bomba van a mez� k�r�l
	public void increaseCount() {
		count += 1;
	}
	
	public int getCount() {
		return count;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
}