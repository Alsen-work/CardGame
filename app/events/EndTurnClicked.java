package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.UpdateStatus;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


		// End turn state change procedure
		endTurnStatusChange(out, gameState);
	}

	private void endTurnStatusChange(ActorRef out, GameState gameState) {

		System.out.println("clicked EndTurnButton");
		gameState.changePlayer();
		gameState.takeTurn();
		System.out.println("try update mana");
		UpdateStatus.updatePlayerMana(out, gameState);//update player mana
		System.out.println("try draw card");
		UpdateStatus.drawCardsInHand(out, gameState);

		// 判断牌堆是否还有牌
//		if (gameState.getRoundPlayer().getDeck().size() <= 0) {
//			System.out.println("gameover!!");
//			//gameState.gameOver();
//		}
//		else {

		//	if(gameState.getRoundPlayer() == gameState.getPlayer1()) {
				//Card card = gameState.getRoundPlayer().getHand().getCardList().get(0);


		//	}
//		}

	}

}
