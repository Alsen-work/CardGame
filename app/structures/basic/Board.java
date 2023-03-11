package structures.basic;

import utils.BasicObjectBuilders;

import java.util.ArrayList;

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

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                allTileList.add(board[y][x]);
            }
        }
        return allTileList;
    }

    //返回可以放置Monster的Tile列表
    public ArrayList<Tile> allSummonableTiles(Player p){
        ArrayList <Tile> tileList = new ArrayList<Tile>();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
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
