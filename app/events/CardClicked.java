package events;


import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		//获取卡牌位置（1-6）减一，为handList的索引（0-5）
		int handPosition = message.get("position").asInt() ;//1-6

		//clickedCard表示点击的卡牌
		Card clickedCard = gameState.getRoundPlayer().getHand().getCardFromHand(handPosition-1);

		gameState.getRoundPlayer().getHand().setSelectCard(clickedCard);//选中的手牌名字
		gameState.getRoundPlayer().getHand().setSelectCardPos(handPosition);//选中的手牌位置
		BasicCommands.addPlayer1Notification(out, "clicked cardId: "+clickedCard.getId()+", handPosition is: "+handPosition, 2);
		//选中的手牌高亮
		BasicCommands.drawCard(out, gameState.getRoundPlayer().getHand().getSelectCard(), gameState.getRoundPlayer().getHand().getSelectCardPos(), 1);


//		//手牌高亮事件（选中一张时，其他卡牌及先前选中的牌不高亮）---未完成
//		//判断是否高亮
//		if(gameState.getRoundPlayer().getHand().isCardSelected()){
//			//如果该牌已经高亮,取消高亮
//			gameState.getRoundPlayer().getHand().setCardSelected(false);//取消选中状态
//			System.out.println("already highlight , selected card status changes:"+gameState.getRoundPlayer().getHand().isCardSelected());
//			BasicCommands.drawCard(out, gameState.getRoundPlayer().getHand().getSelectCard(), gameState.getRoundPlayer().getHand().getSelectCardPos(), 0);
//		}else {
//			//如果非高亮
//			gameState.getRoundPlayer().getHand().setCardSelected(true);//选中状态
//			System.out.println("selected card status :"+gameState.getRoundPlayer().getHand().isCardSelected());
//			BasicCommands.drawCard(out, gameState.getRoundPlayer().getHand().getSelectCard(), gameState.getRoundPlayer().getHand().getSelectCardPos(), 1);
//
//		}





	}

}
