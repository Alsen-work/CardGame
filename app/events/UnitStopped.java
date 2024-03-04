package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;

public class UnitStopped implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int unitid = message.get("id").asInt();
		
		// Unlock UI when unit stops moving
		gameState.playerinteractionUnlock();
		
		// Set unit moving to false (this signals UnitCombined state)
		gameState.setUnitMovingFlag(false);
		System.out.println("User moving flag set to false");
	}

}
