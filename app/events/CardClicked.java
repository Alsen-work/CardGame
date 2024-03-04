package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.UpdateState;
import structures.GameState;
import structures.basic.*;

import java.util.ArrayList;
import structures.basic.Tile;
import structures.basic.abilities.Ability;
import structures.basic.abilities.AbilityToUnitLinkage;
import structures.basic.abilities.ActivateMoment;


public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		// Check if locked, dont not execute anything if so
		if (gameState.playerinteractionLocked()) {
			return;
		}

				// Lock user interaction during action
		gameState.playerinteractionLock();

		// Reset entity selection, cards in hand and board
		UpdateState.updateHandCard(out, gameState, gameState.getTurnOwner().getHand().getHundNum(), gameState.getTurnOwner().getHand().getHandList());
		UpdateState.updateBoard(out, gameState);
		gameState.deselectAllEntities();

		// Hand position the user has clicked 
		int handPosition = message.get("position").asInt();
		
		// Stop the user from going out of bounds
		if (handPosition >= gameState.getTurnOwner().getHand().getHundNum()) {
		handPosition = gameState.getTurnOwner().getHand().getHundNum() - 1;
		}
		
		//creates a placeholder for the clicked card
		Card clickedCard = gameState.getTurnOwner().getHand().getCardFromHand(handPosition);

		//tells the game state that a card in hand is to be played
		gameState.getTurnOwner().getHand().setSelectedCard(gameState.getTurnOwner().getHand().getCardFromHand(handPosition));
		gameState.getTurnOwner().getHand().setSelectedCardPos(handPosition);
		BasicCommands.drawCard(out, gameState.getTurnOwner().getHand().getSelectedCard(), gameState.getTurnOwner().getHand().getSelectedCardPos(), 1);


		// Check if the card has an ability that affects before summoning
		// Boolean switch to check if the ability is applicable 
		boolean outputted = false; 
		
		if(gameState.getTurnOwner().getMana() - clickedCard.getManacost() >= 0) {	//checks card playable with present mana
			if(clickedCard.getCardType()==Monster.class){					//checks if card is related to a monster
				if(clickedCard.hasAbility()) {										// Check if has an ability..
					for(Ability a: clickedCard.getAbilityList()) {
						if (a.getActivateMoment() == ActivateMoment.CardClicked) {
							
							// Execute it (null for no target monster)
							a.execute(null, gameState); 

							// Draw the respective tiles (any ability like this will only affect tiles really unless its like, "if you have this card in your had then get 2 HP per turn but that would be weird"/
							UpdateState.updateBoardTiles(out, gameState.getTileAdjustedRangeContainer(), 1);
							outputted = true; 
							break; 
						}
					}

				}
				if (!outputted) {
					// Else, draw the summonable tiles as normal
					ArrayList<Tile> display= gameState.getBoard().allSummonableTiles(gameState.getTurnOwner());	
					UpdateState.updateBoardTiles(out, display, 1);
				}
			}
			
			//a loop which checks that a card is a spell, then displays playable tiles depending on spell target
			else if (clickedCard.getCardType()==Spell.class) {

				//for spell targeting enemy units
				if(AbilityToUnitLinkage.UnitAbility.get(""+clickedCard.getCardname()).get(0).getTargetType()==Monster.class && clickedCard.targetEnemy()==true){
					
					ArrayList<Tile> display= gameState.getBoard().enemyTile(gameState.getTurnOwner());
					UpdateState.updateBoardTiles(out, display, 2);
					
				}	//for spell which targets enemy avatar	
				else if (AbilityToUnitLinkage.UnitAbility.get(""+clickedCard.getCardname()).get(0).getTargetType()==Avatar.class && clickedCard.targetEnemy()==true){
					
					Tile display= gameState.getBoard().avatarTile(gameState.getTurnOwner(), gameState);
					BasicCommands.drawTile(out,display,2);
				}	
				else if (AbilityToUnitLinkage.UnitAbility.get(""+clickedCard.getCardname()).get(0).getTargetType()==null && clickedCard.targetEnemy()==true) {
					ArrayList<Tile> display= gameState.getBoard().enemyTile(gameState.getTurnOwner());
					display.add(gameState.getBoard().avatarTile(gameState.getTurnOwner(), gameState));
					UpdateState.updateBoardTiles(out, display, 2);
				}
				//for spell targeting friendly unit
				else if (AbilityToUnitLinkage.UnitAbility.get(""+clickedCard.getCardname()).get(0).getTargetType()==Monster.class && clickedCard.targetEnemy()==false){
					
					ArrayList<Tile> display= gameState.getBoard().friendlyTile(gameState.getTurnOwner());
					UpdateState.updateBoardTiles(out, display, 1);

				}	//for spell targeting friendly avatar
				else if (AbilityToUnitLinkage.UnitAbility.get(""+clickedCard.getCardname()).get(0).getTargetType()==Avatar.class && clickedCard.targetEnemy()==false){
					
					Tile display= gameState.getBoard().humanAvatarTile(gameState.getTurnOwner());
					BasicCommands.drawTile(out,display,1);						
				}
			}
		}

		gameState.playerinteractionUnlock();
	}
}