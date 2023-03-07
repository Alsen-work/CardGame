package structures.basic;

import utils.BasicObjectBuilders;

public class Board {
    Tile[][] board ;
    private int bRows;
    private int bCols;



    public  Board(){
        this.bRows = 9;
        this.bCols = 5;

        //初始化板
        board = new Tile[this.bCols][this.bRows];
        for (int i = 0; i<bCols; i++) {
            for (int j = 0; j<bRows; j++) {
                board [i][j] = BasicObjectBuilders.loadTile(j, i);

            }
        }
    }

    public Tile[][] getBoard() {
        return board;
    }

    public Tile getTile(int x, int y) {
        return board[y][x];
    }

}
