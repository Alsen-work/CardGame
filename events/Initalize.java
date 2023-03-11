package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 *
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor {



	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		// hello this is a change

		gameState.gameInitalised = true;

		gameState.something = true;

		// User 1 makes a change
		//CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
		//CheckMoveLogic.executeDemo(out);




		//set board by Luo
		initBoard(out,gameState,message);
		// set player avatar by Luo
		initPlayer(out,gameState,message);




	}

	private static void initBoard(ActorRef out, GameState gameState, JsonNode message) {
		BasicCommands.addPlayer1Notification(out, "initBoard", 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Board gameBoard = gameState.getBoard();//gamestate里创建board并赋值给gameboard

		// Draw the board
		for (int i = 0; i<gameBoard.getBoard().length; i++) {
			for (int j = 0; j<gameBoard.getBoard()[0].length; j++) {
				BasicCommands.drawTile(out, gameBoard.getBoard()[i][j], 0);
			}
		}

	}
	private static void initPlayer(ActorRef out, GameState gameState, JsonNode message) {

		// drawUnit humanAvatar
		BasicCommands.addPlayer1Notification(out, "drawUnit humanAvatar", 2);
		Unit humanAvatar = gameState.getHumanAvatar();
		Tile tilehuman = gameState.getBoard().getTile(1, 2);
		humanAvatar.setPositionByTile(tilehuman);
		BasicCommands.drawUnit(out,humanAvatar, tilehuman);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BasicCommands.addPlayer1Notification(out, "drawUnit humanAvatar attack", 2);
		BasicCommands.setUnitAttack(out, humanAvatar, 2);
		BasicCommands.setUnitHealth(out,humanAvatar, 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// drawUnit aiAvatar
		BasicCommands.addPlayer1Notification(out, "drawUnit aiAvatar", 2);
		Unit aiAvatar = gameState.getAiAvatar();
		Tile tileAi = gameState.getBoard().getTile(7, 2);
		aiAvatar.setPositionByTile(tileAi);
		BasicCommands.drawUnit(out,aiAvatar, tileAi);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BasicCommands.addPlayer1Notification(out, "drawUnit aiAvatar attack", 2);
		BasicCommands.setUnitAttack(out, aiAvatar, 2);
		BasicCommands.setUnitHealth(out, aiAvatar, 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		// Set player stats
		BasicCommands.addPlayer1Notification(out, "Set player1 health", 2);
		BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		BasicCommands.addPlayer1Notification(out, "Set the initial mana of player 1", 2);
		BasicCommands.setPlayer1Mana(out, gameState.getRoundPlayer());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BasicCommands.addPlayer1Notification(out, "Set player2 health", 2);
		BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
		BasicCommands.addPlayer1Notification(out, "Set the initial mana of player 2", 2);
		BasicCommands.setPlayer2Mana(out, gameState.getRoundPlayer());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//draw Player's hand
		int i = 0;

		for (Card card : gameState.getRoundPlayer().getHand().getHandList()) {
			BasicCommands.drawCard(out, card, i + 1, 0);
			BasicCommands.addPlayer1Notification(out, "Draw hand" + i+1, 2);
			i++;
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}









}
