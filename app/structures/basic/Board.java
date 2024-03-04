package structures.basic;

import structures.GameState;
import utils.BasicObjectBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Board {

	private Tile [][] board;
	private final int height= 5;
	private final int width = 9;
	private int boardUnitNum;
	private Monster unitSelected;

	private final int[] rangeH = {0,0,1,-1,1,-1,1,-1};
	private final int[] rangeW = {1,-1,0,0,-1,1,1,-1};

	public Board() {

		board = new Tile[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				board[y][x] = BasicObjectBuilders.loadTile(x, y);
				// Mark this tile as available
				board[y][x].free = true;
				// Set the unit on this tile to null
				board[y][x].unitOnTile = null;
			}
		}
	}
	
	public int getBoardWidth() {
		return this.width;
	}
	
	public int getBoardLength() {
		return this.height;
	}

	//getter method to access the 2D array of Tiles
	public Tile[][] getGameBoard() {
		return board;
	}
	
	public void updateUnitCount(int delta) {
		this.boardUnitNum += delta;
	}


	//Get a list of all tiles
	public ArrayList<Tile> getAllTilesList(){
		ArrayList<Tile> allTileList = new ArrayList<Tile>();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				allTileList.add(board[y][x]);
//                System.out.print("getAllTilesList() board[" +y+"]["+x+"]");
//                System.out.println();
			}
		}
		return allTileList;
	}

	public void setGameBoard(Tile[][] gameBoard) {
		this.board = gameBoard;
	}

	public void setUnitSelected(Monster m){
		this.unitSelected = m;
	}

	public Monster getUnitSelected(){
		return this.unitSelected;
	}

	public Tile getTile(int x, int y) {
		return board[y][x];
	}



	// Get a 3x3 matrix centred on this Tile object
	public ArrayList<Tile> cardinallyAdjTiles(Tile centerTile) {

		//Central object coordinates
		ArrayList<Tile> adjacentTiles = new ArrayList<Tile>(4);
		int centerTileX = centerTile.getTilex(); // 获取中心Tile对象的x坐标
		int centerTileY = centerTile.getTiley(); // 获取中心Tile对象的y坐标

		for (int i = centerTileX - 1; i <= (centerTileX + 1); i++) {
			for (int j = centerTileY - 1; j <= (centerTileY + 1); j++) {

				//Determining whether a boundary is out of bounds
				if ((i <= (this.width - 1) && i >= 0) && (j <= (this.height - 1) && j >= 0)) {

					if (i != centerTileX && j == centerTileY) {
						adjacentTiles.add(this.getTile(i, j));
					}
					if (i == centerTileX && j != centerTileY) {
						adjacentTiles.add(this.getTile(i, j));
					}
				}
			}
		}

		return adjacentTiles;
	}

	/**Place the monster on the board*/

	//Get the enemy's tile
	public ArrayList<Tile> enemyTile(Player p){
		ArrayList<Tile> tileRange = new ArrayList<Tile>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() != null && board[y][x].getUnitOnTile().getClass() != Avatar.class  && board[y][x].getUnitOnTile().getOwner()!=p) {
					tileRange.add(board[y][x]);
				}
			}
		}
		return tileRange;
	}

	public ArrayList<Tile> friendlyTile(Player p) {
		ArrayList<Tile> tileRange = new ArrayList<Tile>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() != null && board[y][x].getUnitOnTile().getClass() != Avatar.class && board[y][x].getUnitOnTile().getOwner()==p) {
					tileRange.add(board[y][x]);
				}
			}
		}
		return tileRange;
	}

	// Calculate Tile adjacent to friendly units (8 directions)
	public ArrayList<Tile> adjacentTiles(Tile tile){
		ArrayList<Tile> adjTiles = new ArrayList<Tile>();
		int x = tile.getTilex();
		int y = tile.getTiley();
//		System.out.print("tile.getTilex(); " +x);
//		System.out.println("tile.getTiley(); " +y);

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue; //不考虑当前位置
				if (x + i < 0 || x + i >= width || y + j < 0 || y + j >= height) continue; //不考虑越界的位置
				adjTiles.add(getTile(x + i, y + j));
//				System.out.print("adjacentTiles [" +(x + i)+"]["+ (y + i)+"]");
//				System.out.println();
			}
		}
		return adjTiles;
	}

	// Remove an occupied adjacent Tile from a friendly unit
	ArrayList<Tile> adjacentFreeTiles(Tile t){
		ArrayList<Tile> tileRange = this.adjacentTiles(t);
		tileRange.removeIf(tile -> !(tile.isFree()));
		return tileRange;

	}

	// return a list of Tiles where Monster can be placed
	public ArrayList<Tile> allSummonableTiles(Player p){
		ArrayList <Tile> tileList = new ArrayList<Tile>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((board[y][x].getUnitOnTile() != null) && board[y][x].getUnitOnTile().getOwner() == p) {
					tileList.addAll(this.adjacentFreeTiles(board[y][x]));
//                    System.out.print("allSummonableTiles(): "+ tileList);
//                    System.out.println();
				}
			}
		}
		return tileList;
	}



	//Get a human avatar
	public Tile humanAvatarTile(Player p) {
		Tile avatarTile = null;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() != null && (board[y][x].getUnitOnTile() instanceof Avatar) && board[y][x].getUnitOnTile().getOwner()==p) {
					avatarTile = board[y][x];
				}
			}	
		}
		
		return avatarTile;
	}

	//Get a enemy avatar
	public Tile avatarTile(Player p) {
		Tile avatarTile = null;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() != null && (board[y][x].getUnitOnTile() instanceof Avatar) && board[y][x].getUnitOnTile().getOwner()!=p) {
					avatarTile = board[y][x];
				}
			}	
		}
		
		return avatarTile;
	}

	//Get avatar tile position
	public Tile avatarTile(Player p, GameState g) {
		if (p instanceof HumanPlayer) { 
			int x = g.getComputerAvatar().getPosition().getTilex();
			int y = g.getComputerAvatar().getPosition().getTiley();
			return this.getTile(x, y);
		} else {
			int x = g.getPlayerAvatar().getPosition().getTilex();
			int y = g.getPlayerAvatar().getPosition().getTiley();
			return this.getTile(x, y);
		}
	}

	public ArrayList<Monster> friendlyUnitsWithAvatar(Player p) {	
		ArrayList<Monster> tileRange = new ArrayList<Monster>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() != null && board[y][x].getUnitOnTile().getOwner()==p) {
					tileRange.add(board[y][x].getUnitOnTile());
				}
			}	
		}
		return tileRange;
	}



	/**Moving unit*/
	public ArrayList<Tile> moves(int xpos, int ypos, int moves, Player p){
		HashSet <Tile> tileList = new HashSet<Tile>();
		boolean[][][] visited = new boolean[this.height][this.width][1];
		//get start position
		Tile startTile = this.getTile(xpos, ypos);
		//get start state
		State startState = new State(startTile, startTile.getUnitOnTile().getMovesLeft());
		Queue<State> queue = new LinkedList<State>();
		queue.add(startState);
		while(! queue.isEmpty()) {
			State current = queue.poll();
			if (current.moves == 0) {
				continue;
			}
			else {
				ArrayList<Tile> reachTiles = this.adjacentTiles(current.tile);
				reachTiles.removeIf(tile ->(tile.isFree()==false && tile.getUnitOnTile().getOwner()!=p));
				tileList.add(current.tile);
				System.out.println("added tile: " + current.tile + "to reach tiles, moves left " + current.moves);
				visited[current.tile.getTiley()][current.tile.getTilex()][0] = true;
				for (Tile t : reachTiles) {
					if (visited[t.getTiley()][t.getTilex()][0] != true) {
		
						State nextState = new State(t, current.moves-1);
						queue.add(nextState);
						
					}	
				}
			}
		}
		
		ArrayList <Tile> list = new ArrayList<Tile>(tileList);
		return list;
	}

	class State {
		int xpos, ypos, moves;
		Tile tile;
		
		public State(Tile tile, int moves) {
			this.moves = moves;
			this.tile = tile;
			this.xpos = tile.getTilex();
			this.ypos = tile.getTiley();
		}
		
		public String toString() {
			return "now on tile: "+ tile +" with " + moves + " left";
		}
	}

	
	public ArrayList<Tile> unitMovableTiles (int xpos, int ypos, int moveRange ){
		Player p = this.getTile(xpos, ypos).getUnitOnTile().getOwner();
		ArrayList <Tile> tileList = this.getReachableTiles(xpos, ypos, moveRange);
		tileList.removeIf(t -> !(t.isFree()));

		return tileList;
	}

	// Get all the reachable squares of a unit within a given movement range
	public ArrayList<Tile> getReachableTiles(int xpos, int ypos, int moveRange){
		ArrayList<Tile> reachableTiles = new ArrayList<Tile>();

		for (int i = xpos - moveRange; i <= xpos + moveRange; i++) {
			for (int j = ypos - moveRange; j <= ypos + moveRange; j++) {

				// Check that the coordinates are within the game map
				if (i >= 0 && i < this.width && j >= 0 && j < this.height) {

					// Check that the grid is within moving distance
					if (Math.abs(i - xpos) + Math.abs(j - ypos) <= moveRange) {
						reachableTiles.add(this.getTile(i, j));
					}
				}
			}
		}
		return reachableTiles;
	}

	
	public ArrayList<Tile> unitAttackableTiles (int xpos, int ypos, int attackRange, int moveRange ){
		Player p = this.getTile(xpos, ypos).getUnitOnTile().getOwner();
		
		ArrayList<Tile> reachTiles;
		HashSet <Tile> tileList = new HashSet<Tile>();
		
		if (moveRange == 0) {
			ArrayList<Tile> list = new ArrayList<Tile>(this.calAttackRange(xpos, ypos, attackRange, p));
			return list;
		}

		reachTiles = this.getReachableTiles(xpos, ypos, moveRange);

		for (Tile t : reachTiles) {
	
			if (!(t.isFree()) && t.getUnitOnTile().getOwner()!=p) tileList.add(t);
		}
		reachTiles.removeIf(t -> !(t.isFree()));

		for(Tile t : reachTiles) {
			
			HashSet <Tile> attRange = calAttackRange(t.getTilex(), t.getTiley(), attackRange, p);
			tileList.addAll(attRange);
		}	

		return new ArrayList<>(tileList);

	}

	// Calculates all the squares a unit can attack within a given attack range
	public HashSet<Tile> calAttackRange(int xpos, int ypos, int attackRange, Player p){
		HashSet<Tile> attackableTiles = new HashSet<Tile>();

		for (int i = xpos - attackRange; i <= (xpos + attackRange); i++) {
			for (int j = ypos - attackRange; j <= (ypos + attackRange); j++) {


				// Check that the coordinates are within the game map
				if (i >= 0 && i < this.width && j >= 0 && j < this.height) {

					// // Check if there are enemy units on the grid
					if(this.getTile(i, j).getUnitOnTile() != null && this.getTile(i, j).getUnitOnTile().getOwner() != p) {
						 attackableTiles.add(this.getTile(i, j));
					}
					
				}
			}
		}
		return attackableTiles;
	}

	public ArrayList<Monster> coolDownCheck (Player p){
		ArrayList<Monster> monsterList = new ArrayList<Monster>();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if ((board[y][x].getUnitOnTile() != null)&& board[y][x].getUnitOnTile().getOnCooldown() && board[y][x].getUnitOnTile().getOwner()==p) {
					monsterList.add(this.board[y][x].getUnitOnTile());
				}
				 
			}
		}
		return monsterList;

	}
		

	public ArrayList<Monster> friendlyUnitList (Player p){
		ArrayList<Monster> monsterList = new ArrayList<Monster>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() != null && board[y][x].getUnitOnTile().getOwner()==p) 							{
					monsterList.add(board[y][x].getUnitOnTile());
				}
			}	
		}
		return monsterList;
	}
	
	public ArrayList<Tile> allFreeTiles(){
		ArrayList<Tile> freeTilesList = new ArrayList<Tile>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x].getUnitOnTile() == null) 							{
					freeTilesList.add(board[y][x]);
				}
			}	
		}
		return freeTilesList;
	}

	public ArrayList<Tile> getUnitAllActionableTiles(int xpos, int ypos, int attackRange, int moveRange ){

		// Use Set to avoid duplicate grid additions
		HashSet<Tile> tileSet = new HashSet<>();

		// Get all reachable squares
		ArrayList<Tile> reachableTiles = this.getReachableTiles(xpos, ypos, moveRange);

		for (Tile t : reachableTiles) {
			// Add the reachable grid to the set
			tileSet.add(t);

			// Get the attackable squares
			HashSet<Tile> attackableTiles = new HashSet<>();
			for (int i = t.getTilex() - attackRange; i <= t.getTilex() + attackRange; i++) {
				for (int j = t.getTiley() - attackRange; j <= t.getTiley() + attackRange; j++) {
					// Check that the coordinates are within the game board
					if (i >= 0 && i < this.width && j >= 0 && j < this.height) {
						attackableTiles.add(this.getTile(i, j));
					}
				}
			}


			tileSet.addAll(attackableTiles);
		}

		return new ArrayList<>(tileSet);
	}


	
	
}


	
	