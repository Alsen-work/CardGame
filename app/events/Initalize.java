package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.UpdateState;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Tile;


public class Initalize implements EventProcessor{

	@Override
public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		//Before the construction of the avatar and card, player cannot do anything
		gameState.playerinteractionLock();

		initBoard(out,gameState,message);
		initPlayer(out, gameState, message);

		gameState.playerinteractionUnlock();

	}
	
	private static void initBoard(ActorRef out, GameState gameState, JsonNode message) {

		// Create the board in gamestate and assign it to board
		Board board = gameState.getBoard();
		
		// Draw the board
		for (int i = 0; i<board.getGameBoard().length; i++) {
			for (int j = 0; j<board.getGameBoard()[0].length; j++) {
				BasicCommands.drawTile(out, board.getGameBoard()[i][j], 0);
			}
			UpdateState.threadSleep();
		}
		UpdateState.threadSleep();
		
		// Set up avatar references
		Avatar humanAvatar = gameState.getPlayerAvatar();
		Avatar computerAvatar = gameState.getComputerAvatar();
		UpdateState.threadSleep();
		
	
		// Setting avatars' starting position
		Tile tOne = gameState.getBoard().getTile(1, 2);
		Tile tTwo = gameState.getBoard().getTile(7, 2);
		humanAvatar.setPositionByTile(tOne);
		computerAvatar.setPositionByTile(tTwo);

		// Drawing avatarts on the board
		BasicCommands.drawUnit(out, humanAvatar, tOne);
		tOne.addUnit(humanAvatar);
		UpdateState.threadSleep();
		BasicCommands.setUnitAttack(out, humanAvatar, humanAvatar.getAttackValue());
		BasicCommands.setUnitHealth(out, humanAvatar, humanAvatar.getHP());
		UpdateState.threadSleep();
				
		BasicCommands.drawUnit(out, computerAvatar, tTwo);	
		tTwo.addUnit(computerAvatar);
		UpdateState.threadSleep();
		BasicCommands.setUnitAttack(out, computerAvatar, computerAvatar.getAttackValue());
		BasicCommands.setUnitHealth(out, computerAvatar, computerAvatar.getHP());
		UpdateState.threadSleep();
	}
	
	private static void initPlayer(ActorRef out, GameState gameState, JsonNode message) {
	
		// Set mana for first turn
		gameState.giveMana();
		
		// Set player stats
		BasicCommands.setPlayer1Health(out, gameState.getPlayer());
		UpdateState.threadSleep();
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer());
		UpdateState.threadSleep();
		
		BasicCommands.setPlayer2Health(out, gameState.getComputer());
		UpdateState.threadSleep();
		BasicCommands.setPlayer2Mana(out, gameState.getComputer());
		UpdateState.threadSleep();
		
		int i = 0;
		//showing human player's hand
		for (Card c : gameState.getTurnOwner().getHand().getHandList()) {
			BasicCommands.drawCard(out, c, i, 0);
			i++;
		}
	}
}


