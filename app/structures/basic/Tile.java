package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Comparable<Tile>{

	@JsonIgnore
	private static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	List<String> tileTextures;
	int xpos;
	int ypos;
	int width;
	int height;
	int tilex;
	int tiley;
	
	//attributes added//
	boolean free;
	Monster unitOnTile; 	
	int score;

	public Tile() {}
	
	public Tile(String tileTexture, int xpos, int ypos, int width, int height, int tilex, int tiley) {
		super();
		tileTextures = new ArrayList<String>(1);
		tileTextures.add(tileTexture);
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.tilex = tilex;
		this.tiley = tiley;
		
		this.free = true;
		this.unitOnTile = null;
		this.score = 0;
	}
	
	public Tile(List<String> tileTextures, int xpos, int ypos, int width, int height, int tilex, int tiley) {
		super();
		this.tileTextures = tileTextures;
		this.xpos = xpos;
		this.ypos = ypos;
		this.width = width;
		this.height = height;
		this.tilex = tilex;
		this.tiley = tiley;
		
		this.free = true;
		this.unitOnTile = null;
		this.score = 0;
	}
	public List<String> getTileTextures() {
		return tileTextures;
	}
	public void setTileTextures(List<String> tileTextures) {
		this.tileTextures = tileTextures;
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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
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
	public void setTiley(int tiley) {
		this.tiley = tiley;
	}
	public boolean isFree() {
		return free; 
	}
	public Monster getUnitOnTile() {
		return unitOnTile; 
	}
	
	public void setScore(int d) {
		this.score = d;
	}


	/**
	 * The following method accepts a parameter of type Monster,
	 * which could refer to either a Monster or an Avatar object.
	 * Both methods first verify that the target tile is available
	 * before attempting to add a monster to it. Similarly,
	 * when attempting to remove a monster,
	 * they check if there is actually a monster present on the tile
	 * before proceeding with the removal process.
	 * */


	public boolean addUnit (Monster m) {
		if (!(this.free) || !(this.unitOnTile==null)) {
			return false;
		}
		else {
			this.unitOnTile = m;
			this.free = false;

			m.setPositionByTile(this);
			
			return true;			
		}
	}

	public boolean removeUnit () {
		if (this.free || this.unitOnTile==null) return false;
		else {
			this.unitOnTile.setPosition(null);
			this.free = true;
			this.unitOnTile = null;
			return true;
		}
	}
	
	@Override
	public int compareTo(Tile o) {
		if (this.getScore() > o.getScore()) return 1;
		else if (this.getScore() < o.getScore()) return -1;
		return 0;
	}

	public int getScore() {
		return score;
	}
	
	
	public String toString() {
		return "tile: " + this.tilex + " - " + this.tiley;
	}



	/**
	 * Loads a tile from a configuration file
	 * parameters.
	 * @param configFile
	 * @return
	 */
	public static Tile constructTile(String configFile) {
		
		try {
			Tile tile = mapper.readValue(new File(configFile), Tile.class);
			return tile;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
		
	}


}
