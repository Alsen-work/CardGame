package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic representation of a tile on the game board. Tiles have both a pixel position
 * and a grid position. Tiles also have a width and height in pixels and a series of urls
 * that point to the different renderable textures that a tile might have.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Tile {

	@JsonIgnore
	private static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	List<String> tileTextures;
	int xpos;
	int ypos;
	int width;
	int height;
	int tilex;
	int tiley;
	boolean free;
	Monster unitOnTile;


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



	//在Tile上放置一个Monster
	public boolean addUnit (Monster m) {
		//判断该区域是否空闲，是否有unit要放置
		if (!(isFree()) || !(getUnitOnTile()==null)) {
			return false;
		}
		else {
			this.setUnitOnTile(m);
			m.setPositionByTile(this);
			this.setFree(false);
			return true;
		}
	}
	//从Tile上移除一个Monster
	public boolean removeUnit () {
		if (isFree() || getUnitOnTile()==null) {
			return false;
		}
		else {
			this.setUnitOnTile(null);
			this.unitOnTile.setPosition(null);
			this.setFree(true);
			return true;
		}
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	public Monster getUnitOnTile() {
		return unitOnTile;
	}
	public void setUnitOnTile(Monster unitOnTile) {
		this.unitOnTile = unitOnTile;
	}
}