package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.UpdateState;
import structures.GameState;


public class Heartbeat implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// Constantly update
		UpdateState.updateAllUnitStats(out, gameState);
		UpdateState.updatePlayerHealth(out, gameState);
		UpdateState.updatePlayerMana(out, gameState);
	}

}
