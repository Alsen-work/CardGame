package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.UpdateState;
import structures.GameState;

public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// Check if locked, dont not execute anything if so
		if (gameState.playerinteractionLocked()) {
			return;
		}
		
		// Lock user interaction during action
		gameState.playerinteractionLock();


		/* Entity deselection and board reset */
		gameState.deselectAllEntities();
		UpdateState.updateBoard(out, gameState);
		if(gameState.getTurnOwner() == gameState.getPlayer()) {
			UpdateState.updateHandCard(out, gameState, gameState.getTurnOwner().getHand().getHundNum(), gameState.getTurnOwner().getHand().getHandList());
		}

		gameState.playerinteractionUnlock();
		
	}

}


