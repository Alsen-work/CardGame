package structures.basic;


public class Position {

	int xpos;
	int ypos;
	int tilex;
	int tiley;
	
	public Position() {}
	
	public Position(int xpos, int ypos, int tilex, int tilexy) {
		super();
		this.xpos = xpos;
		this.ypos = ypos;
		this.tilex = tilex;
		this.tiley = tilexy;
	}
	public int getXpos() {
		return xpos;
	}
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}
	public int getYpos() {
		return ypos;
	}
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}
	public int getTilex() {
		return tilex;
	}
	public void setTilex(int tilex) {
		this.tilex = tilex;
	}
	public int getTiley() {
		return tiley;
	}
	public void setTiley(int tilexy) {
		this.tiley = tilexy;
	}
	
	public Tile getTile(Board board) {
		return board.getTile(tilex, tiley);
	}
	
	
	
	
	
}
