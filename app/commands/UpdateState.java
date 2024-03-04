package commands;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.*;

import java.util.ArrayList;
import java.util.List;


public class UpdateState {

	//Thread sleep time to ensure program consistency
	private static final int threadSleepTime = 50;
	private static final int threadSleepTimeLong = 100;

	public static void threadSleep() {
		try {Thread.sleep(threadSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
	}

	public static void threadSleepLong() {
		try {Thread.sleep(threadSleepTimeLong);} catch (InterruptedException e) {e.printStackTrace();}
	}



	//update Board and Tiles
	public static void updateBoard(ActorRef out, GameState gameState) {

		updateBoardTiles(out, gameState.getBoard().getAllTilesList(), 0);
	}
	public static void updateBoardTiles(ActorRef out, ArrayList<Tile> tileList, int tileMode) {

		int bufferSize =15;//Set buffer size
		int batchSize = (tileList.size() + bufferSize - 1) / bufferSize; // Calculate how many batches are needed (make sure all tiles are painted)
		for (int bNum = 0; bNum < batchSize; bNum++) {
			int start = bNum * bufferSize; // Calculation of starting position
			int end = Math.min(start + bufferSize, tileList.size()); // Calculate the end position
			List<Tile> batch = tileList.subList(start, end); // Take out the current batch of tiles
			for (Tile tile : batch) {
				BasicCommands.drawTile(out, tile, tileMode);
			}
			threadSleep();
		}

	}


	/**
	 * This method draws a unit and its corresponding statistics on the specified tile.
	 * If the unit is a monster or avatar, its health and attack values will also be displayed.
	 *
	 * @param out The ActorRef that sends the command to the game engine.
	 * @param unit The unit to be drawn
	 * @param onTile The tile on which the unit will be drawn.
	 */
	public static void updateUnitStats(ActorRef out, Unit unit, Tile onTile) {

		// Draw the unit on the  tile
		BasicCommands.drawUnit(out, unit, onTile);
		threadSleep();

		// Set the unit's animation
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.idle);
		threadSleep();

		// If the unit is a monster or avatar, set its health and attack values
		if (unit instanceof Monster || unit instanceof Avatar) {

			Monster mUnit = (Monster) unit;
			// Set the unit's health
			BasicCommands.setUnitHealth(out, mUnit, mUnit.getHP());
			threadSleep();

			// Set the unit's attack value
			BasicCommands.setUnitAttack(out, mUnit, mUnit.getAttackValue());
			threadSleep();
		}
	}

	/**
	 * This method redraws all unit statistics on the game board for units.
	 * It also updates the statistics for the player's and computer's avatars.
	 *
	 * @param out the ActorRef that sends commands to the game engine
	 * @param gameState the current state of the game
	 */
	public static void updateAllUnitStats(ActorRef out, GameState gameState) {

		System.out.println("In redrawAllUnitStats");

		// Loop over all friendly and enemy tiles and update stats
		for (Tile friendlyTile : gameState.getBoard().friendlyTile(gameState.getPlayer())) {

			BasicCommands.setUnitAttack(out, friendlyTile.getUnitOnTile(), friendlyTile.getUnitOnTile().getAttackValue());
			threadSleep();
			BasicCommands.setUnitHealth(out, friendlyTile.getUnitOnTile(), friendlyTile.getUnitOnTile().getHP());
			threadSleep();
		}
		for (Tile enemyTile : gameState.getBoard().enemyTile(gameState.getPlayer())) {

			BasicCommands.setUnitAttack(out, enemyTile.getUnitOnTile(), enemyTile.getUnitOnTile().getAttackValue());
			threadSleep();
			BasicCommands.setUnitHealth(out, enemyTile.getUnitOnTile(), enemyTile.getUnitOnTile().getHP());
			threadSleep();
		}

		// Update the statistics for the player's and computer's avatars
		BasicCommands.setUnitAttack(out, gameState.getPlayerAvatar(), gameState.getPlayerAvatar().getAttackValue());
		threadSleep();
		BasicCommands.setUnitHealth(out, gameState.getPlayerAvatar(), gameState.getPlayerAvatar().getHP());
		threadSleep();

		BasicCommands.setUnitAttack(out, gameState.getComputerAvatar(), gameState.getComputerAvatar().getAttackValue());
		threadSleep();
		BasicCommands.setUnitHealth(out, gameState.getComputerAvatar(), gameState.getComputerAvatar().getHP());
		threadSleep();
	}


	/**
	 * Deselects the tiles covering a given unit's range by dehighlighting them.
	 *
	 * @param out ActorRef object used for sending commands to the front-end
	 * @param gameState current state of the game
	 * @param unit the unit whose range tiles are being deselected
	 */
	public static void drawUnitDeselect(ActorRef out, GameState gameState, Unit unit) {

		// Check if the unit is a Monster or Avatar
		if(unit.getClass() == Monster.class || unit.getClass() == Avatar.class) {

			// Cast unit to Monster for Monster methods/values
			Monster mUnit = (Monster) unit;

			// Get the tile occupied by the unit and draw it
			Tile unitTile = gameState.getBoard().getTile(mUnit.getPosition().getTilex(), mUnit.getPosition().getTiley());
			BasicCommands.drawTile(out, unitTile, 0);

			// Get the tiles within the unit's attack and move range
			ArrayList<Tile> rangeTiles = gameState.getBoard().unitAttackableTiles(mUnit.getPosition().getTilex(), mUnit.getPosition().getTiley(), mUnit.getAttackRange(), mUnit.getMovesLeft());
			rangeTiles.addAll(gameState.getBoard().unitMovableTiles(mUnit.getPosition().getTilex(), mUnit.getPosition().getTiley(), mUnit.getMovesLeft()));

			// Dehighlight tiles 
			updateBoardTiles(out, rangeTiles, 0);
		}
	}
	
	
	// Update player health and mana
	public static void updatePlayerHealth(ActorRef out, GameState gameState) {
		BasicCommands.setPlayer1Health(out, gameState.getPlayer());
		threadSleep();
		BasicCommands.setPlayer2Health(out, gameState.getComputer());
		threadSleep();

	}
	public static void updatePlayerMana(ActorRef out, GameState gameState) {
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer());
		threadSleep();
		BasicCommands.setPlayer2Mana(out, gameState.getComputer());
		threadSleep();
	}

	/**
	 * Updates the display of the player's hand of cards.
	 * @param out the ActorRef object used to send commands to the frontend
	 * @param gameState the current state of the game
	 * @param oldHandSize the previous size of the player's hand of cards
	 * @param handCards the updated list of cards in the player's hand
	 */
	public static void updateHandCard(ActorRef out, GameState gameState, int oldHandSize, ArrayList<Card> handCards) {

		// Delete all cards
		for (int i = 0; i < oldHandSize; i++) {
			BasicCommands.deleteCard(out, i);
			threadSleep();
		}
		 
		// Update hand position
		int i = 0;	
		for(Card c : handCards) { // get list of cards from Hand from Player
			BasicCommands.drawCard(out, c, i, 0);
			i++;
			threadSleep();
		}
	}

	
}
