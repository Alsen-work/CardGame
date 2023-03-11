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


		/**Determining if the user is ready to interact*/
		if (gameState.isUserInteractionEnabled()) {
			return;
		}
		/**Lock*/
		gameState.userinteractionLock();


		// End turn state change procedure
		endTurnStatusChange(out, gameState);


		/**UnLock**/
		gameState.userinteractionUnlock();
	}

	private void endTurnStatusChange(ActorRef out, GameState gameState) {

		//点击“结束回合”按钮触发事件：
		//更换玩家-清空mana，计算回合数，给mana，给手牌
		//更新玩家生命，更新玩家mana，取消选择手卡，
		System.out.println("clicked EndTurnButton");
		gameState.changePlayer();
		UpdateStatus.threadSleep();
		gameState.takeTurn();
		UpdateStatus.threadSleep();
		//update player health
		System.out.println("try update health");
		UpdateStatus.updatePlayerHealth(out, gameState);
		UpdateStatus.threadSleep();
		//update player mana
		System.out.println("try update mana");
		UpdateStatus.updatePlayerMana(out, gameState);
		UpdateStatus.threadSleep();
		// deselect all unit and hand
		System.out.println("deselect all unit and hand");
		gameState.deselectUnit();
		gameState.deselectHand();
		UpdateStatus.threadSleep();

		//回合结束抽牌
		//gameState.getRoundPlayer().getHand().giveHand(gameState.getRoundPlayer(), gameState.getRoundNumber());

		//Show human player hand, ai player not
		if(gameState.getRoundPlayer() == gameState.getPlayer1()) {
			System.out.println("try draw card");
			int preHandNum = (gameState.getRoundPlayer().getHand().getHandList().size()) -1; 									//after get new one, get current handsize -1 for old size
			UpdateStatus.updateHandCards(out, gameState, preHandNum, gameState.getRoundPlayer().getHand().getHandList()); 	//refresh hand ,show with one card added
			UpdateStatus.threadSleep();
		}else {
			System.out.println(" it's aiPlayer");

		}



	}

}
