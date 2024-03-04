package structures.basic;

import structures.basic.ComputerLogic.ComputerAttackMonsterLogic;
import structures.basic.ComputerLogic.ComputerMoveMonsterLogic;
import structures.basic.ComputerLogic.ComputerPlayCardsLogic;

import java.util.ArrayList;


/**
 * ComputerPlayer is a subclass of Player class that
 * represents the computer player in the game.
 * It contains methods for playing cards,
 * moving monsters and performing attacks by the computer player.
 */
public class ComputerPlayer extends Player {

	private int hPBenchMark;
	

	public ComputerPlayer() {
		super(); 
	}
	
	public String toString() {
		return "Player 2";
	}

	public int getHPBenchMark() {
		return this.hPBenchMark;
	}
	
	public void setHPBenchMark(int hp) {
		this.hPBenchMark = hp;
	}





	//Returns the list of computer instructions for playing cards on the game board.
	public ArrayList<structures.basic.ComputerLogic.ComputerInstruction> playCards(Board gameBoard){
		ComputerPlayCardsLogic play = new ComputerPlayCardsLogic(this);
		
		return play.playCards(gameBoard);
	}


	//	Returns the list of computer instructions for moving monsters on the game board.
	public ArrayList<structures.basic.ComputerLogic.ComputerInstruction> moveMonsters(Board gameBoard){
		ComputerMoveMonsterLogic move = new ComputerMoveMonsterLogic(this);
		return move.movesUnits(gameBoard);
	}
	
	// Returns the list of computer instructions for performing attacks on enemy monsters on the game board.
	public ArrayList <structures.basic.ComputerLogic.ComputerInstruction> performAttacks(Board gameBoard){
		ComputerAttackMonsterLogic attack = new ComputerAttackMonsterLogic (this);
		return attack.computerAttacks(gameBoard);
	}
		
	
}


