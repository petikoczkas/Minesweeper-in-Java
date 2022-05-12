package game;

import javax.swing.JButton;

public class Tile extends JButton{
	
	private int row, col; //az adott sor, oszlop
	private int count; //hány bomba van az adott mezõ körül
	private boolean isMine; //bomba-e az adott mezõ
	private boolean flagged; //meg van-e jelölve az adott mzõ
	
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

	//növeli azt, hogy hány bomba van a mezõ körül
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