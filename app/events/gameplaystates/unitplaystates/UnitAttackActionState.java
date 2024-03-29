package events.gameplaystates.unitplaystates;

import java.util.ArrayList;

import commands.BasicCommands;
import commands.UpdateState;
import events.gameplaystates.GameplayContext;
import structures.basic.Avatar;
import structures.basic.EffectAnimation;
import structures.basic.Monster;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.UnitAnimationType;
import structures.basic.abilities.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class UnitAttackActionState implements IUnitPlayStates {

	private Tile 				currentTile; 
	private Tile 				targetTile; 
	
	private Monster				attacker;
	private Monster				defender;
	
	private ArrayList <Tile>	attackerAttackRange;
	private ArrayList <Tile>	defenderCounterRange;
	
	
	/*** State constructor ***/
	/* 
	 * Changed constructor to input current and target tiles to decouple Unit states from TileClicked
	 * Previously Unit states had tilex, tiley be used from context which were variables received from TileClicked. 
	 * Decoupling required to use unit States from the ComputerPlayer. */
	
	public UnitAttackActionState(Tile currentTile, Tile targetTile) {
		this.currentTile = currentTile;
		this.targetTile = targetTile; 
		this.attacker = null;
		this.defender = null;
		this.attackerAttackRange = null;
		this.defenderCounterRange = null;
	}
	
	
	/** State method **/
	
	public void execute(GameplayContext context) {
		
		/**===========================================**/
		context.getGameStateRef().playerinteractionLock();
		/**===========================================**/
		
		
		System.out.println("In UnitAttackActionSubState.");

		// Gather attacker and defender
		attacker = (Monster) context.getLoadedUnit();
		defender = targetTile.getUnitOnTile();
		
		// Build attacker/defender attackRanges for checks (omitting movement range which has been completed previously)
		attackerAttackRange  = context.getGameStateRef().getBoard().unitAttackableTiles(attacker.getPosition().getTilex(), attacker.getPosition().getTiley(), attacker.getAttackRange(), 0);
		defenderCounterRange = context.getGameStateRef().getBoard().unitAttackableTiles(defender.getPosition().getTilex(), defender.getPosition().getTiley(), defender.getAttackRange(), 0);
		
		
		// Check target is in attack range for attacker
		if(!tileInRange(attacker)){
			System.out.println("Enemy is not in attack range.");
			return;
		}
					
		// Use adjust action range based on movement impaired effects
		if (attacker.hasActionRangeImpaired()) {
			attackerAttackRange = context.getGameStateRef().getTileAdjustedRangeContainer();
		}

		// Execute the attack
		unitAttack(context);

		/***	Condition here for combined substate executing, which requires selection is maintained	***/
		if(!(context.getCombinedActive())) {

			// Update stats after any action 
			UpdateState.updateAllUnitStats(context.out, context.getGameStateRef());

			/** Reset entity selection and board **/  
			// Deselect after action finished *if* not in the middle of move-attack action
			context.deselectAllAfterActionPerformed();

			//  Reset board visual (highlighted tiles)
			UpdateState.updateBoard(context.out, context.getGameStateRef());

			// Only unlock if not in combined state
			/**===========================================**/
			context.getGameStateRef().playerinteractionUnlock();
			/**===========================================**/
		}		

		
	}
	

	
	private void unitAttack(GameplayContext context) {
				

		// Stores interaction outcomes
		boolean survived;		
		
		/*** Attack ***/
		
		System.out.println(attacker.getName() + " has " + attacker.getAttacksLeft() + " attacks left");
		
		// Check if attack is successful, if so update internal values
		if(!attacker.attack()) {
			System.out.println("Unit cannot attack.");
			return;
		}
		
		// Verbose output
		BasicCommands.addPlayer1Notification(context.out, "Attack!", 2);

		System.out.println("Attack successful. " + attacker.getName() + " has " + attacker.getAttacksLeft() + " attacks left");

		// Update defender
		survived = defender.getDamage(attacker.getAttackValue());
		System.out.println("Defender has " + defender.getHP() + " HP");

		// Update UI
		UpdateState.drawUnitDeselect(context.out, context.getGameStateRef(), attacker);
		

		/***	Play animations and set visuals		***/
		playAttackAnimations(attacker, defender, context);

		// If friendly Avatar damaged ability check
		checkAvatarDamaged(defender, context);

		// Re-idle alive units
		BasicCommands.playUnitAnimation(context.out,attacker,UnitAnimationType.idle);
		if(survived) {	
			BasicCommands.playUnitAnimation(context.out,defender,UnitAnimationType.idle);	
		}
		UpdateState.threadSleep();

		
		/***	Death check	***/
		if(!survived) {
			// De-highlight attacker for game action clarity to user
			BasicCommands.drawTile(context.out, currentTile, 0);	

			// Check for Avatar death/game end
			if(checkForAvatarDeath(defender, context)) {
				return;
			}
			// Unit dies
			else {
				unitDeath(context, targetTile);
				UpdateState.threadSleep();
			}	
		}

		/*** Counter-attack	***/
		else {

			// Check for attacker destination and reachable by defender (ranged/adjacent)
			if((checkRangedAttacker(defender) == null) && !(tileInRange(defender))) {	
				System.out.println("Defender cannot counter attack.");
				return;	
			}

			System.out.println("Counter-attack incoming...");

			// Counter attack
			survived = attacker.getDamage(defender.counter());

			/***	Switch attacker/defender highlights for user clarity	***/
			// Initial attacker
			BasicCommands.drawTile(context.out, currentTile, 2);
			UpdateState.threadSleep();
			// Initial defender
			BasicCommands.drawTile(context.out, targetTile, 1);
			UpdateState.threadSleep();
			

			/***	Play animations and set visuals		***/
			playAttackAnimations(defender, attacker, context);

			// If friendly Avatar damaged ability check
			checkAvatarDamaged(attacker, context);

			
			/***	Death check	***/
			if(!survived) {
				// De-highlight attacker tile for game action clarity to user
				BasicCommands.drawTile(context.out, targetTile, 0);

				// Check for Avatar death/game end
				if(checkForAvatarDeath(attacker, context)) {
					return;
				}
				// Unit dies
				else {
					unitDeath(context, currentTile);
					UpdateState.threadSleep();
				}	
			}	
			// Re-idle alive units
			if(survived) {	
				BasicCommands.playUnitAnimation(context.out,attacker,UnitAnimationType.idle);	
			}
			BasicCommands.playUnitAnimation(context.out,defender,UnitAnimationType.idle);
			UpdateState.threadSleep();
		}
	

	}
	
	/*	Helper methods	*/
	
	// Checks the user's selected Tile is within the attack range of the selected unit
	private boolean tileInRange(Monster m) {
		if(m == attacker) {
			if(attackerAttackRange.contains(targetTile)) {	return true;	}
			return false;
		} else {
			if(defenderCounterRange.contains(currentTile)) {	return true;	}
			return false;
		}

	}
	
	
	
	/***			Methods used by attack stages			***/
	// Playing attack animations/effects for attacker and receiver
	// --- note order of input arguments is key to output
	private void playAttackAnimations(Monster attacker, Monster receiver, GameplayContext context) {
			
		// Ranged
		if(attacker.getAbAnimation() != null /*Could need an && for near attacks exempt*/) {
			BasicCommands.playProjectileAnimation(context.out, attacker.getAbAnimation(), 0, attacker.getPosition().getTile(context.getGameStateRef().getBoard()), receiver.getPosition().getTile(context.getGameStateRef().getBoard()));
		}
		
		// Executes for both ranged and non-ranged attacks
		BasicCommands.playUnitAnimation(context.out,attacker,UnitAnimationType.attack);
		
		BasicCommands.playUnitAnimation(context.out, receiver, UnitAnimationType.hit);
		BasicCommands.setUnitHealth(context.out, receiver, receiver.getHP());
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	// Unit death --- method to update location data and delete a Unit from Board
	private void unitDeath(GameplayContext context, Tile grave) {
		
		Monster deadUnit = grave.getUnitOnTile();
		
		// Visuals
		BasicCommands.playUnitAnimation(context.out, deadUnit, UnitAnimationType.death);				
		try {Thread.sleep(1300);} catch (InterruptedException e) {e.printStackTrace();}	
		BasicCommands.deleteUnit(context.out, deadUnit);
			
		// Check for onDeath ability
		if(deadUnit.hasAbility()) {
			for(Ability a : deadUnit.getMonsterAbility()) {
				if(a.getActivateMoment() == ActivateMoment.Death) {
					a.execute(deadUnit,context.getGameStateRef()); 
					break;
				}
			}
		}
		
		// Update internal location values
		grave.removeUnit();
		deadUnit.setPosition(new Position(-1,-1,-1,-1));	// might not need
		
		context.getGameStateRef().getBoard().updateUnitCount(-1);
	}
	
	
	
	/***			Small checks			***/
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
	
	
	// Simple helper to check if a Monster is an Avatar
	private boolean isAvatar(Monster m) {
		if(m.getClass() == Avatar.class) {	return true;	}
		return false;
	}
	
	// Return EffectAnimation if true
	private EffectAnimation checkRangedAttacker(Monster attacker) {
		if(attacker.getAbAnimation() != null) {		return attacker.getAbAnimation();	}
		return null;
	}
	
	// Check if a Monster that has received damage:
	// 1) is a friendly Avatar
	// 2) will trigger any present friendly Unit with a related ability
	private void checkAvatarDamaged(Monster a, GameplayContext context) {
		
		// If Avatar condition is not satisfied
		if(!(isAvatar(a))) {	return;		}
		
		// Check for friendly units with ability
		else {
			
			ArrayList <Monster> friendlies = context.getGameStateRef().getBoard().friendlyUnitList(a.getOwner());
			
			// For each ally of Avatar a
			for(Monster m : friendlies) {
				if(m.hasAbility()) {
					// For each ability
					for(Ability abi : m.getMonsterAbility()) {
						
						// If ability is triggered by friendly Avatar damage
						if(abi.getActivateMoment() == ActivateMoment.AvatarDealDamage) {
							// Change stats
							abi.execute(m, context.getGameStateRef());
							
							System.out.println("After Avatar is damaged, my attack is: " + m.getAttackValue() + " and my health is " + m.getHP());
							
							// Play animation + update stats
							BasicCommands.playUnitAnimation(context.out, m, UnitAnimationType.channel);
							BasicCommands.playEffectAnimation(context.out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), m.getPosition().getTile(context.getGameStateRef().getBoard()));
							UpdateState.threadSleep();
							UpdateState.updateAllUnitStats(context.out, context.getGameStateRef());
						}
						
					}
				}
			}
			
		}
		
	}
}
