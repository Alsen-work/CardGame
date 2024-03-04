package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;


public class UnitMoving implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int unitid = message.get("id").asInt();

		// Lock UI and set user moving flag to true
		gameState.playerinteractionLock();

		gameState.setUnitMovingFlag(true);
	}

}
