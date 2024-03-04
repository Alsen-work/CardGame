package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import events.gameplaystates.GameplayContext;
import events.gameplaystates.tileplaystates.CardSelectedBeforeState;
import events.gameplaystates.tileplaystates.SingleSelectedState;
import events.gameplaystates.tileplaystates.UnitSelectedBeforeState;

import structures.GameState;

public class TileClicked implements EventProcessor{

	
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


		// Check if locked, dont not execute anything if so
		if (gameState.playerinteractionLocked()) {
			return;
		}
		
		// Lock user interaction during action
		gameState.playerinteractionLock();

		
		// Selected Tile coordinates
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		
		System.out.println("In TileClicked.");

		GameplayContext gameplayContext = new GameplayContext(gameState, out);
		gameplayContext.setClickedTile(gameState.getBoard().getTile(tilex, tiley));

		if (checkCardClicked(gameState)) {
			
			gameplayContext.addCurrentState(new CardSelectedBeforeState());
		} 
		else if (checkUnitSelected(gameState)) {
			
			gameplayContext.addCurrentState(new UnitSelectedBeforeState());

		}
		else {
			gameplayContext.addCurrentState(new SingleSelectedState());
		}
		

		gameplayContext.executeAndCreateUnitStates();	

		gameState.playerinteractionUnlock();
	}


	
	private boolean checkCardClicked(GameState gameState) {
		return (gameState.getTurnOwner().getHand().getSelectedCard() != null);

	}
	
	private boolean checkUnitSelected(GameState gameState) {
		return (gameState.getBoard().getUnitSelected() != null);
	}



}