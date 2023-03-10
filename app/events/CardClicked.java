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
			int handPosition = message.get("position").asInt();//1-6

			//现在手牌大小
			int handListSize = gameState.getRoundPlayer().getHand().getHandList().size();

			//执行仅选中的牌高亮逻辑
			if (handPosition >= 1 && handPosition <= handListSize) {
				//clickedCard表示点击的卡牌
				Card clickedCard = gameState.getRoundPlayer().getHand().getCardFromHand(handPosition - 1);
				System.out.println("clickedCard cardId: " + clickedCard.getId() + ", handPosition is: " + handPosition);

				//SelectedCard表示选中的卡牌
				Card selectedCard = gameState.getRoundPlayer().getHand().getSelectCard();

				//判断是否选中
				if (selectedCard != null && selectedCard != clickedCard) {
					//未选中的手牌取消高亮
					BasicCommands.drawCard(out, selectedCard, gameState.getRoundPlayer().getHand().getSelectCardPos(), 0);
					System.out.println("selected cardId: " + selectedCard.getId() + ", handPosition is: " + handPosition + " highlight false!!");
				}

				//选中的手牌名字
				gameState.getRoundPlayer().getHand().setSelectCard(clickedCard);
				//选中的手牌位置
				gameState.getRoundPlayer().getHand().setSelectCardPos(handPosition);
				//选中的手牌高亮显示
				BasicCommands.drawCard(out, clickedCard, handPosition, 1);
				System.out.println(" new clickedCard cardId: " + clickedCard.getId() + ", handPosition is: " + handPosition);
				selectedCard = clickedCard;
				System.out.println(" new selected cardId: " + selectedCard.getId() + ", handPosition is: " + handPosition);

			}



	}









}
