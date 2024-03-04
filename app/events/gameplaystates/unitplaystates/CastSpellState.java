package events.gameplaystates.unitplaystates;

import commands.BasicCommands;
import commands.UpdateState;
import events.gameplaystates.GameplayContext;
import structures.basic.*;
import structures.basic.abilities.Ability;
import structures.basic.abilities.AbilityToUnitLinkage;
import structures.basic.abilities.ActivateMoment;
import utils.BasicObjectBuilders;

import java.util.ArrayList;

public class CastSpellState implements IUnitPlayStates {

	private Tile targetTile; 

	
	public CastSpellState(Tile targetTile) {
		this.targetTile = targetTile; 
	}

	public void execute(GameplayContext context) {

		context.getGameStateRef().playerinteractionLock();
		
		// Create spell from the Card in context		
		Spell spellToCast = (Spell) BasicObjectBuilders.loadCard( context.getLoadedCard().getConfigFile(), context.getLoadedCard().getId(), Spell.class); 

		// Set the ability of the spell
		spellToCast.setAbility(context.getLoadedCard().getCardname() ,AbilityToUnitLinkage.UnitAbility.get(context.getLoadedCard().getCardname()).get(0));
		
		/* Cast the Spell on the Unit on tile selected */
		boolean successFlag = false;
		
		// If mana enough
		if (context.getGameStateRef().getTurnOwner().getMana() - context.getLoadedCard().getManacost() >= 0) {
			
			// eturn true to indicate the successfully cast of the spell
			if (targetTile.getUnitOnTile() != null) {
				successFlag = spellToCast.getAbility().execute(targetTile.getUnitOnTile() , context.getGameStateRef());
			}
			else {
				successFlag = false;
			}
		}
		
		// if success, apply the change
		if (successFlag) {
			
			// Verbose output
			BasicCommands.addPlayer1Notification(context.out, "Spell executing!", 2);
			
			System.out.println("Successfully cast spell.");
						
			// Play effect animation associated with ability (if present)
			if (spellToCast.getAbility().getEffectAnimation() != null) {

				BasicCommands.playEffectAnimation(context.out, spellToCast.getAbility().getEffectAnimation(), targetTile);
				UpdateState.threadSleep();
			}
			
			// Possible activations: buff if enemy spell cast, buff if Avatar takes damage etc
			checkForAbilityActivations(targetTile.getUnitOnTile(), context);

			// Index variables
			int cardIndexInHand = context.getGameStateRef().getTurnOwner().getHand().getSelectedCardPos();
			int oldHandSize =  context.getGameStateRef().getTurnOwner().getHand().getHandList().size(); 	// How many UI cards to delete

			// Since the spell is successfully cast, the card need to be removed
			context.getGameStateRef().getTurnOwner().getHand().removeCard(cardIndexInHand);
			UpdateState.updateAllUnitStats(context.out, context.getGameStateRef());


			// Only update Hand for Human player
			if (context.getGameStateRef().getTurnOwner() instanceof HumanPlayer) {
				UpdateState.updateHandCard(context.out, context.getGameStateRef(), oldHandSize, context.getGameStateRef().getTurnOwner().getHand().getHandList());
			}
		
			// Reset board visual (highlighted tiles)
			UpdateState.updateBoard(context.out, context.getGameStateRef());

			// Deselect after action finished
			context.deselectAllAfterActionPerformed();

			// Check if the target has been killed, need to remove it
			if (targetTile.getUnitOnTile().getHP() <= 0) {

				// Check for Avatar death/game end
				if(checkForAvatarDeath(targetTile.getUnitOnTile(), context)) {
					return;
				}
				// Unit dies
				else {
					unitDeath(targetTile, context);
					UpdateState.threadSleep();
				}	
			}
		}
		else {
			System.out.println("Spell cast unsucessful, please select another Unit"); 
		}
		

		context.getGameStateRef().playerinteractionUnlock();
	}

	// Simple helper to check if a Monster is an Avatar
	private boolean isAvatar(Monster m) {
		if(m.getClass() == Avatar.class) {	return true;	}
		return false;
	}
	

	// Check for buffs due to: damage, spell cast
	private void checkForAbilityActivations(Monster m, GameplayContext context) {
		
		// Friendly Avatar damaged buff
		checkAvatarDamaged(m, context);
		
		// Spell cast buff
		checkEnemySpellCast(m, context);	
	}
	
	
	private void checkEnemySpellCast(Monster target, GameplayContext context) {
		
		// Get turnOwner's opposite
		Player spellCheck = context.getGameStateRef().getEnemyPlayer();
		ArrayList <Monster> friendlies = context.getGameStateRef().getBoard().friendlyUnitList(spellCheck);
		
		// Loop over friendly monsters and check if they have an ability
		for(Monster f : friendlies) {
			
			if(!(f.hasAbility())) {	
				continue;	
			}
			// Loop over abilities
			for(Ability a : f.getMonsterAbility()) {
				if(a.getActivateMoment() == ActivateMoment.EnemySpellCast) {
					
					a.execute(f,context.getGameStateRef());
					
					System.out.println("After casting a spell my HP is: " + f.getHP() + " and attack is " + f.getAttackValue());
					
					// Play animation + update stats
					if (a.getEffectAnimation() != null) {
						BasicCommands.playEffectAnimation(context.out, a.getEffectAnimation(), f.getPosition().getTile(context.getGameStateRef().getBoard()));
					}
					BasicCommands.playUnitAnimation(context.out, f, UnitAnimationType.channel);
					UpdateState.threadSleep();
					UpdateState.updateAllUnitStats(context.out, context.getGameStateRef());
				}
			}
		}	
	}
	
	
	// Check if Avatar has taken damage
	private void checkAvatarDamaged(Monster a, GameplayContext context) {
		
		// If Avatar condition is not satisfied
		if(!(isAvatar(a))) {	
			return;		
		}
		// Check for friendly units with ability
		else {
			
			// Get friendly list of monsters
			ArrayList <Monster> friendlies = context.getGameStateRef().getBoard().friendlyUnitList(a.getOwner());
			
			// For each ally of Avatar a
			for(Monster m : friendlies) {
				
				if(!(m.hasAbility())) {
					continue; 
				}
				
				// For each ability
				for(Ability abi : m.getMonsterAbility()) {

					// If ability is triggered by friendly Avatar damage
					if(abi.getActivateMoment() == ActivateMoment.AvatarDealDamage) {
						
						// Apply ability
						abi.execute(m, context.getGameStateRef());

						System.out.println("After Avatar is damaged, my attack is: " + m.getAttackValue() + " and my health is " + m.getHP());

						// Play animation + update stats
						BasicCommands.playUnitAnimation(context.out, m, UnitAnimationType.channel);
						if (abi.getEffectAnimation() != null) {
							BasicCommands.playEffectAnimation(context.out, abi.getEffectAnimation(), m.getPosition().getTile(context.getGameStateRef().getBoard()));
						}
						UpdateState.threadSleep();
						UpdateState.updateAllUnitStats(context.out,context.getGameStateRef());
					}
				}
			}
		}
	}
			

	// Avatar death check --- method checks that the death of a unit is not an Avatar, calls gameOver if so
	private boolean checkForAvatarDeath(Monster deadUnit, GameplayContext context) {
		
		if(isAvatar(deadUnit)) {
			
			// Player notification
			String endgameMessage = "";
			if(context.getGameStateRef().getPlayer() == deadUnit.getOwner()) {
				endgameMessage += "You lose!";
			} else {
				endgameMessage += "You win!";
			}
			BasicCommands.addPlayer1Notification(context.out,endgameMessage, 2);
			
			// Game ends
			context.getGameStateRef().gameOver();
			return true;	
		}
		return false;
	}
	
	// Unit death method to update location data and delete a Unit from Board
	private void unitDeath(Tile grave, GameplayContext context) {
		
		BasicCommands.playUnitAnimation(context.out, targetTile.getUnitOnTile(), UnitAnimationType.death);				
		try {Thread.sleep(1300);} catch (InterruptedException e) {e.printStackTrace();}	
		BasicCommands.deleteUnit(context.out, targetTile.getUnitOnTile());

		Monster deadUnit = grave.getUnitOnTile();
		
		// Check for onDeath ability
		if(deadUnit.hasAbility()) {
			for(Ability a : deadUnit.getMonsterAbility()) {
				if(a.getActivateMoment() == ActivateMoment.Death) {
					a.execute(deadUnit,context.getGameStateRef()); 
					break;
				}
			}
		}
		
		// Update internal Tile values
		grave.removeUnit();
		deadUnit.setPosition(new Position(-1,-1,-1,-1));
		
		context.getGameStateRef().getBoard().updateUnitCount(-1);
		
	}
}
