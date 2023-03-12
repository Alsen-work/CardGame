package structures.basic;

import utils.BasicObjectBuilders;

import java.util.ArrayList;
import java.util.HashSet;

public class Board {
    Tile[][] board ;
    private  int width = 9;
    private  int height = 5;
    private Monster unitSelected;



    public Board() {


        board = new Tile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = BasicObjectBuilders.loadTile(x, y);
                // 将该瓷砖标记为可用
                board[y][x].free = true;
                // 将该瓷砖上的单位设置为 null
               // board[y][x].unitOnTile = null;

            }
        }
    }

    public ArrayList<Tile> getAllTilesList(){
        ArrayList<Tile> allTileList = new ArrayList<Tile>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                allTileList.add(board[y][x]);
            }
        }
        return allTileList;
    }

    //返回可以放置Monster的Tile列表
    public ArrayList<Tile> allSummonableTiles(Player p){
        ArrayList <Tile> tileList = new ArrayList<Tile>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((board[y][x].getUnitOnTile() != null)&& board[y][x].getUnitOnTile().getOwner() == p) {
                    tileList.addAll(this.adjacentFreeTiles(board[y][x]));
                }
            }
        }
        return tileList;
    }
    //计算友方单位相邻Tile(8个方位)
    public ArrayList<Tile> adjacentTiles(Tile tile){
        ArrayList<Tile> adjTiles = new ArrayList<Tile>();
        int x = tile.getTilex();
        int y = tile.getTiley();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; //不考虑当前位置
                if (x+i < 0 || x+i >= width || y+j < 0 || y+j >= height) continue; //不考虑越界的位置
                adjTiles.add(getTile(x+i, y+j));
            }
        }

        return adjTiles;
    }


    //从友方单位相邻Tile移除已被占用的
     ArrayList<Tile> adjacentFreeTiles(Tile t){
        ArrayList<Tile> adjFreeTiles = this.adjacentTiles(t);
        adjFreeTiles.removeIf(tile -> !(tile.isFree()));
        return adjFreeTiles;

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

    public ArrayList<Tile> unitAllActionableTiles (int xpos, int ypos, int attackRange, int moveRange ){


        HashSet<Tile> tileList = new HashSet<Tile>();

        // Get a list of all tiles that the unit can reach given their position and move range
        ArrayList<Tile> reachTiles = this.reachableTiles(xpos, ypos, moveRange);

        //the reachable tile list now only contains unoccupied tiles
        //for each of these unoccupied tiles (that the unit could move to)
        //the attack range (with that tile as origin) is calculated as a set
        //the set is added to the set to returned (no duplicated values)
        for(Tile t : reachTiles) {

            // Add movement tile
            tileList.add(t);

            HashSet <Tile> attRange = new HashSet<Tile>();

            // Find tiles around tile t respective of the units attack range
            for (int i = t.getTilex() - attackRange; i <= (t.getTilex()+ attackRange); i++) {
                for (int j = t.getTiley() - attackRange; j <= (t.getTiley() + attackRange); j++) {

                    // Check if indices are within limits of the board
                    if ( (i <= (this.height - 1) && i >= 0) && (j <= (this.width - 1) && j >= 0)) {
                        tileList.add(this.getTile(i, j));
                    }
                }
            }

            tileList.addAll(attRange);
        }

        ArrayList<Tile> list = new ArrayList<Tile>(tileList);
        return list;
    }

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
                if (board[y][x].getUnitOnTile() != null && board[y][x].getUnitOnTile().getClass() != Avatar.class  && board[y][x].getUnitOnTile().getOwner()!=p) {
                    tileRange.add(board[y][x]);
                }
            }
        }
        return tileRange;
    }
    public ArrayList<Tile> unitMovableTiles (int xpos, int ypos, int moveRange ){
        Player p = this.getTile(xpos, ypos).getUnitOnTile().getOwner();
        ArrayList <Tile> tileList = this.reachableTiles(xpos, ypos, moveRange);
        tileList.removeIf(t -> !(t.isFree()));

        return tileList;
    }
    public ArrayList<Tile> reachableTiles (int xpos, int ypos, int moveRange){
        ArrayList<Tile> reachTile = new ArrayList<Tile>();

        for (int i = xpos - moveRange; i <= (xpos + moveRange); i++) {

            for (int j = ypos - moveRange; j <= (ypos + moveRange); j++) {

                // Check if indices are within limits of the board
                if ( (i <= (this.height - 1) && i >= 0) && (j <= (this.width - 1) && j >= 0)) {

                    // Check each tile index combination is adds up to the range
                    // (abs(i -x) is the distance the current index is away from the monster position)
                    if ( (Math.abs(i - xpos) + Math.abs(j - ypos)) <=moveRange) {
                        reachTile.add(this.getTile(i, j));
                    }
                }
            }
        }
        return reachTile;
    }

    public ArrayList<Tile> cardinallyAdjTiles(Tile t) {

        // Set up
        ArrayList<Tile> returnArr = new ArrayList<Tile>(4);
        int xpos = t.getTilex();
        int ypos = t.getTiley();

        // Find tiles around tile t
        for (int i = xpos - 1; i <= (xpos + 1); i++) {
            for (int j = ypos - 1; j <= (ypos + 1); j++) {

                // Check if indices are within limits of the board
                if ( (i <= (this.height - 1) && i >= 0) && (j <= (this.width - 1) && j >= 0)) {

                    // Only add cardinally adjacent
                    if (i != xpos && j == ypos) {
                        returnArr.add(this.getTile(i, j));
                    }

                    if (i == xpos && j != ypos) {
                        returnArr.add(this.getTile(i, j));
                    }
                }
            }
        }

        return returnArr;
    }


    public Tile[][] getBoard() {
        return board;
    }

    public Tile getTile(int x, int y) {
        return board[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setUnitSelected(Monster m){
        this.unitSelected = m;
    }

    public Monster getUnitSelected(){
        return this.unitSelected;
    }



}
