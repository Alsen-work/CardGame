package structures;

import structures.basic.Board;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {



	public boolean gameInitalised = false;
	
	public boolean something = false;

	public Board getBoard() {
		Board board = new Board();
		return board;
	}
	
}



