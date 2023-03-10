package structures;

import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

import java.util.List;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	private Board board;
	private Player player1;
	private Player player2;
	private Unit humanAvatar;
	private Unit aiAvatar;
	private int roundNumber;
	private Player roundPlayer;
	private Hand humanHand;
	private Hand aiHand;
	private List<Card> deck1;
	private List<Card> deck2;

	public boolean gameInitalised = false;

	public boolean something = false;

	public GameState() {

		//set board
		board = new Board();

		// set players
		player1 = new Player();
		player2 = new Player();

		//set deck
		//手牌逻辑,玩家1从牌堆1抽，2从2抽

		deck1 = OrderedCardLoader.getPlayer1Cards();
		System.out.println("SET deck1 " + deck1.size());
		deck2 = OrderedCardLoader.getPlayer2Cards();
		System.out.println("SET deck2 " + deck2.size());
		player1.setDeck(deck1);
		player2.setDeck(deck2);

		//set start from player1
		//(因为目前逻辑试点击endturn开始游戏，且点击后先执行exchangePlayer(),所以这里为player2)
		this.setRoundPlayer(player2);
		//set 0, start form round 1
		roundNumber = 0;

		//手牌逻辑,玩家1从牌堆1抽，2从2抽
		// Set hands
		humanHand = new Hand();
		aiHand = new Hand();

		//humanHand.giveHand(player1,roundNumber+1);
		player1.setHand(humanHand);
		//aiHand.giveHand(player2,roundNumber+1);
		player2.setHand(aiHand);


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


		if (getRoundPlayer() == player1) {
			player1.addMana(roundNumber + 1);//+1 because the initial roundNumber is 0
			System.out.println("player1 mana is " + player1.getMana());
		} else {
			player2.addMana(roundNumber + 1);
			System.out.println("player2 mana is " + player2.getMana());
		}

	}

	public void clearMana() {
		System.out.println("clear mana");
		getRoundPlayer().setMana(0);
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

	public Hand getHumanHand() {
		return humanHand;
	}

	public void setHumanHand(Hand humanHand) {
		this.humanHand = humanHand;
	}

	public Hand getAiHand() {
		return aiHand;
	}

	public void setAiHand(Hand aiHand) {
		this.aiHand = aiHand;
	}

	public void changePlayer() {

		if (getRoundPlayer() == player1) {
			setRoundPlayer(player2);
		} else {
			setRoundPlayer(player1);
		}
	}

	public void takeTurn() {
		System.out.println("----------------------------");
		// Clears mana at the end of each turn
		clearMana();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Turn on the player's turn and set the player's mana for that turn
		//turn + 1 on player 1's turn
		if (getRoundPlayer() == player1) {
			roundNumber++;
			System.out.println("Round : " + this.roundNumber);
			System.out.println("now is player 1 round");
			giveMana();
			humanHand.giveHand(player1, roundNumber);

		} else {
			System.out.println("now is player 2 round");
			giveMana();
			aiHand.giveHand(player2, roundNumber);

		}
	}









}



