package structures;

import structures.basic.Board;
import structures.basic.Player;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	private Board board;
	private Player 	player1;
	private Player 	player2;
	private Unit humanAvatar;
	private Unit aiAvatar;

	public boolean gameInitalised = false;
	
	public boolean something = false;
public GameState() {

	//set board
	board = new Board();

	// set players
	player1 = new Player();
	player2 = new Player();

	//set mana
	giveMana();

}

	public Board getBoard() {

		return board;
	}

	public Unit getHumanAvatar() {
		humanAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, -1, Unit.class);
		return humanAvatar;
	}

	public Unit getAiAvatar() {
		aiAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, -2, Unit.class);
		return aiAvatar;
	}




	//set mana
	public void giveMana() {

			player1.setMana(1);
			System.out.println("player1 mana is " + player1.getMana());


			player2.setMana(1);
			System.out.println("player2 mana is " + player2.getMana());
	}

	public Player getPlayer1() {
		return  player1;
	}

	public Player getPlayer2() {
		return  player2;
	}

}



