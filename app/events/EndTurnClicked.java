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

		//点击“结束回合”按钮触发事件：
		//更换玩家-清空mana，计算回合数，给mana，给手牌
		//更新玩家生命，更新玩家mana，取消选择手卡，
		System.out.println("clicked EndTurnButton");
		gameState.changePlayer();
		gameState.takeTurn();
		//update player mana
		System.out.println("try update mana");
		UpdateStatus.updatePlayerMana(out, gameState);
		//Show human player hand, ai player not
		if(gameState.getRoundPlayer() == gameState.getPlayer1()) {
			System.out.println("try draw card");
			UpdateStatus.drawCardsInHand(out, gameState);
		}else {
			System.out.println(" it's aiPlayer");
		}



	}

}
