package events;


import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import commands.UpdateStatus;
import structures.GameState;
import structures.basic.*;
import structures.basic.abilities.Ability;
import structures.basic.abilities.AbilityToUnitLinkage;
import structures.basic.abilities.ActivateMoment;

import java.util.ArrayList;

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


		/**Determining if the user is ready to interact*/
		if (gameState.isUserInteractionEnabled()) {
			return;
		}
		/**Lock**/
		gameState.userinteractionLock();

		//Executed first when the user clicks on a hand Reset state (including hand, board)
		int handNum = gameState.getRoundPlayer().getHand().getHandNum();
		ArrayList<Card> handList = gameState.getRoundPlayer().getHand().getHandList();

		UpdateStatus.updateHandCards(out, gameState, handNum, handList);
		UpdateStatus.updateBoard(out, gameState);
		gameState.deselectHand();
		gameState.deselectUnit();

		//获取卡牌位置（1-6）减一，为handList的索引（0-5）
		int handPosition = message.get("position").asInt();//1-6

		//现在手牌大小
		int handListSize = gameState.getRoundPlayer().getHand().getHandList().size();


		//执行仅选中的牌高亮逻辑
		//clickedCard表示点击的卡牌
		Card clickedCard = gameState.getRoundPlayer().getHand().getCardFromHand(handPosition - 1);
		System.out.println("clickedCard cardId: " + clickedCard.getId() + ", handPosition is: " + handPosition);

		if (handPosition >= 1 && handPosition <= handListSize) {

			//选中的手牌名字
			gameState.getRoundPlayer().getHand().setSelectCard(clickedCard);
			//选中的手牌位置
			gameState.getRoundPlayer().getHand().setSelectCardPos(handPosition);
			//SelectedCard表示选中的卡牌
			Card selectedCard = gameState.getRoundPlayer().getHand().getSelectCard();
			BasicCommands.drawCard(out, selectedCard, handPosition, 1);

//			//判断是否选中
//			if (selectedCard != null && selectedCard != clickedCard) {
//				//未选中的手牌取消高亮
//				BasicCommands.drawCard(out, selectedCard, gameState.getRoundPlayer().getHand().getSelectCardPos(), 0);
//				System.out.println("selected cardId: " + selectedCard.getId() + ", handPosition is: " + handPosition + " highlight false!!");
//			}
//
//			//选中的手牌名字
//			gameState.getRoundPlayer().getHand().setSelectCard(clickedCard);
//			//选中的手牌位置
//			gameState.getRoundPlayer().getHand().setSelectCardPos(handPosition);
//			//选中的手牌高亮显示
//			BasicCommands.drawCard(out, clickedCard, handPosition, 1);
//			System.out.println(" new clickedCard cardId: " + clickedCard.getId() + ", handPosition is: " + handPosition);
//			selectedCard = clickedCard;
//			System.out.println(" new selected cardId: " + selectedCard.getId() + ", handPosition is: " + handPosition);
//
		}


		/**
		 *Determine if mana is sufficient, determine card type, determine card ability
		 */

		boolean hasAbility = clickedCard.hasAbility();
		boolean cardOut = false;

		if (gameState.getRoundPlayer().enoughPay(clickedCard.getManacost())) {
			System.out.println("enoughPayMana");
			//if monster
			if (clickedCard.getAssociatedClass() == Monster.class) {
				System.out.println("it's a Monster Card!");
				if (hasAbility) {
					System.out.println("hasAbility");
					for (Ability ability : clickedCard.getAbilityList()) {
						if (ability.getActivateMoment() == ActivateMoment.CardClicked) {
							// Execute ability
							ability.execute(null, gameState);
							// Draw affected tiles -highlight
							UpdateStatus.updateTiles(out, gameState.getTileAdjustedRangeContainer(), 1);
							cardOut = true;
							System.out.println("CardOut: " + cardOut);
							break;
						}
					}
				}
				System.out.println("Not hasAbility");

				if (!cardOut) {
					System.out.println("!CardOut: " + cardOut);
					// Draw summonable tiles
					ArrayList<Tile> summonableTiles = gameState.getBoard().allSummonableTiles(gameState.getRoundPlayer());
					UpdateStatus.updateTiles(out, summonableTiles, 1);
				}

				//a loop which checks that a card is a spell, then displays playable tiles depending on spell target
				else if (clickedCard.getAssociatedClass() == Spell.class) {
					System.out.println("it's a Spell Card!");
					//for spell targeting enemy units
					if (AbilityToUnitLinkage.UnitAbility.get("" + clickedCard.getCardname()).get(0).getTargetType() == Monster.class && clickedCard.targetEnemy() == true) {

						ArrayList<Tile> display = gameState.getBoard().enemyTile(gameState.getRoundPlayer());
						UpdateStatus.updateTiles(out, display, 2);

					}    //for spell which targets enemy avatar
					else if (AbilityToUnitLinkage.UnitAbility.get("" + clickedCard.getCardname()).get(0).getTargetType() == Avatar.class && clickedCard.targetEnemy() == true) {

						Tile display = gameState.getBoard().enemyAvatarTile(gameState.getRoundPlayer(), gameState);
						BasicCommands.drawTile(out, display, 2);
					} else if (AbilityToUnitLinkage.UnitAbility.get("" + clickedCard.getCardname()).get(0).getTargetType() == null && clickedCard.targetEnemy() == true) {
						ArrayList<Tile> display = gameState.getBoard().enemyTile(gameState.getRoundPlayer());
						display.add(gameState.getBoard().enemyAvatarTile(gameState.getRoundPlayer(), gameState));
						UpdateStatus.updateTiles(out, display, 2);
					}
					//for spell targeting friendly unit
					else if (AbilityToUnitLinkage.UnitAbility.get("" + clickedCard.getCardname()).get(0).getTargetType() == Monster.class && clickedCard.targetEnemy() == false) {

						ArrayList<Tile> display = gameState.getBoard().friendlyTile(gameState.getRoundPlayer());
						UpdateStatus.updateTiles(out, display, 1);

					}    //for spell targeting friendly avatar
					else if (AbilityToUnitLinkage.UnitAbility.get("" + clickedCard.getCardname()).get(0).getTargetType() == Avatar.class && clickedCard.targetEnemy() == false) {

						Tile display = gameState.getBoard().ownAvatarTile(gameState.getRoundPlayer());
						BasicCommands.drawTile(out, display, 1);
					}
				} else {
					System.out.println("it's not a Monster Card!");
				}
			} else {
				System.out.println("Not enoughPayMana");
			}


		}


		/**UnLock**/
		gameState.userinteractionUnlock();
	}


}
