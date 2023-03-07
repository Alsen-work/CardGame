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
	private int	roundNumber;
	private Player roundPlayer;

	public boolean gameInitalised = false;
	
	public boolean something = false;

public GameState() {

	//set board
	board = new Board();

	// set players
	player1 = new Player();
	player2 = new Player();

	//set start from player1
	this.setRoundPlayer(player1);
	//set 0, start form round 1
	roundNumber = 0;
	//set mana
	giveMana();

	//测试回合机制是否成立
	System.out.println("-------------1-");
	takeTurn();
	takeTurn();
	System.out.println("-------------2-");
	takeTurn();
	takeTurn();
	System.out.println("-------------3-");
	takeTurn();
	takeTurn();
	System.out.println("-------------4-");
	takeTurn();
	takeTurn();
	System.out.println("-------------5-");

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

	//初始化（回合1）时不会执行else
		if (getRoundPlayer() == player1) {
			player1.setMana(roundNumber);
			System.out.println("player1 mana is " + player1.getMana());
		} else{
			player2.setMana(roundNumber);
			System.out.println("player2 mana is " + player2.getMana());
		}

	}



	public Player getPlayer1() {
		return  player1;
	}

	public Player getPlayer2() {
		return  player2;
	}
	public Player getRoundPlayer() {
		return roundPlayer;
	}
	public void setRoundPlayer(Player roundPlayer) {
		this.roundPlayer = roundPlayer;
	}
	public void takeTurn(){
//		Player[] players = {player1, player2};
//		for (int i = 0; i < players.length ; i ++){
//			System.out.println("now is player"+ i+1 + " round");
//		}
		if(getRoundPlayer() == player1){
			roundNumber++;
			System.out.println("now is round : " +this.roundNumber);
			setRoundPlayer(player2);
		}else {
			setRoundPlayer(player1);
		}
		System.out.println("now is player "+ getRoundPlayer() + " round");
	}

}



