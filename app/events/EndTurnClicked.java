package events;
import structures.basic.*;
import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.ActorRef;
import structures.GameState;
import commands.*;


public class EndTurnClicked implements EventProcessor{
	
	@Override
	public void processEvent (ActorRef out, GameState gameState, JsonNode message){
		
		// If the interaction is locked , don't do anything
		if (gameState.playerinteractionLocked()) {
			return;
		}
		
		// Lock user interaction during action first
		gameState.playerinteractionLock();

		
		// Turn into endturn state
		endTurnStateChange(out, gameState);
		
		// End turn for computer player. Executes all determined moves
		if (gameState.getTurnOwner() == gameState.getComputer()) {
			
			ComputerPlayerTurn compTurn = new ComputerPlayerTurn(out, gameState);
			compTurn.processComputerActions();
			
			if(gameState.getTurnOwner().getDeck().getCardList().isEmpty() && gameState.getTurnOwner() == gameState.getComputer()) {
				System.out.println("Computer lose");
				BasicCommands.addPlayer1Notification(out, "You win!", 2);
				gameState.gameOver();
				return;
			}
			
			BasicCommands.addPlayer1Notification(out, "Your move!", 2);
			UpdateState.threadSleep();

			// Verbose output
			BasicCommands.addPlayer1Notification(out,gameState.getTurnOwner().toString() + "'s turn!", 2);
		}

		gameState.playerinteractionUnlock();
	}
		

	private void endTurnStateChange(ActorRef out, GameState gameState) {  


		/** End turn procedure **/
		gameState.emptyMana(); 										// Empty mana for player who ends the turn
		UpdateState.threadSleep();
		UpdateState.updatePlayerHealth(out, gameState);		// Update player health
		UpdateState.updatePlayerMana(out, gameState);		// Update player mana
		UpdateState.threadSleep();
		gameState.deselectAllEntities();							// Deselect all entities
		UpdateState.updateBoard(out, gameState);  		// Visual rest of the board

		// Check if the deck is empty, if so then gameover
		if (gameState.isDeckEmpty()) {  							//check if current player has enough card in deck left to be added into hand
			gameState.gameOver(); 
		}
		else {

			// If there are cards left in deck, get a card from deck (back end)
			gameState.getTurnOwner().getHand().drawCard(gameState.getTurnOwner().getDeck());  
			
			//if it is human player getting a new card, re-display all card in hand after drawing 
			if(gameState.getTurnOwner() == gameState.getPlayer()) {
				Card card = gameState.getTurnOwner().getDeck().getCardList().get(0);
				int oldCardSize = (gameState.getTurnOwner().getHand().getHandList().size()) -1; 									//after get new one, get current handsize -1 for old size 
				UpdateState.updateHandCard(out, gameState, oldCardSize, gameState.getTurnOwner().getHand().getHandList()); 	//refresh hand ,show with one card added
			}	
		}

		gameState.setMonsterCooldown(true);						// Hard set all monsters on turn enders turn to cooldown
		UpdateState.threadSleep();
		gameState.turnChange(); 								// turnOwner exchanged	
		UpdateState.threadSleep();
		gameState.giveMana();			 						// Give turnCount mana to the player in the beginning of new turn
		UpdateState.threadSleep();
		UpdateState.updatePlayerHealth(out, gameState);	// Update player health
		UpdateState.updatePlayerMana(out, gameState);	// Update player mana
		UpdateState.threadSleep();
		gameState.setMonsterCooldown(false);					// Set all monster cooldowns to false

		UpdateState.threadSleep();


	}

}


