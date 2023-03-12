package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import events.gameplaystates.GameplayContext;
import events.gameplaystates.tileplaystates.CardPreviouslySelectedState;
import events.gameplaystates.tileplaystates.SingleSelectedState;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 *
 * {
 *
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{


	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


		// Check if locked, dont not execute anything if so
		// 检查是否锁定，如果锁定则不执行任何操作
		if (gameState.isUserInteractionEnabled()) {
			return;
		}

		// Lock user interaction during action
		/**===========================**/
		gameState.userinteractionLock();
		/**===========================**/


		// Selected Tile coordinates
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();

		System.out.println("In TileClicked.");

		// Start the GameplayState (State Pattern for TileClicked control flow)
		//启动GameplayState（TileClicked控制流的状态模式）。
		GameplayContext gameplayContext = new GameplayContext(gameState, out);
		gameplayContext.setClickedTile(gameState.getBoard().getTile(tilex, tiley));

		/* --------------------------------------------------------------------------------
		 * Check previous User inputs (will be either Card Selected State or Unit Selected)
		 * -------------------------------------------------------------------------------- */
		/* --------------------------------------------------------------------------------
		 * 检查以前的用户输入（将是卡选择状态或单位选择）。
		 * -------------------------------------------------------------------------------- */

		if (checkCardClicked(gameState)) {

			gameplayContext.addCurrentState(new CardPreviouslySelectedState());
		}
		else if (checkUnitSelected(gameState)) {

			//gameplayContext.addCurrentState(new UnitPreviouslySelectedState());

			// Set this now to false?
		}
		else {
			// If nothing is selected previously
			gameplayContext.addCurrentState(new SingleSelectedState());
		}





		/*
		 * Execute State. Each state holds the game logic required to execute the desired functionality
		 * Note, some States here create sub-states.
		 * E.g. CardSelectedState deals with the previous user input of a Card click and generates a new Unit state
		 * based on what the user has currently clicked (a unit or empty tile)
		 */
		gameplayContext.executeAndCreateUnitStates();

		/**===========================**/
		gameState.userinteractionUnlock();
		/**===========================**/
	}



	/* Helper methods */

	private boolean checkCardClicked(GameState gameState) {
		return (gameState.getRoundPlayer().getHand().getSelectCard() != null);

	}

	private boolean checkUnitSelected(GameState gameState) {
		return (gameState.getBoard().getUnitSelected() != null);
	}



}