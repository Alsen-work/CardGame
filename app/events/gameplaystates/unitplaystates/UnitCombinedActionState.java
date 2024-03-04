package events.gameplaystates.unitplaystates;

import java.util.ArrayList;

import commands.UpdateState;
import events.gameplaystates.GameplayContext;
import structures.basic.*;
import structures.basic.abilities.*;

public class UnitCombinedActionState implements IUnitPlayStates {

	private Tile currentTile; 
	private Tile destination;
	private Tile enemyTarget;
	

	public UnitCombinedActionState(Tile currentTile, Tile targetTile) {
		this.currentTile = currentTile; 
		this.destination = null;
		this.enemyTarget = targetTile; 
	}

	
	public void execute(GameplayContext context) {
	
		System.out.println("In UnitCombinedActionSubState.");
				
		// To avoid conflict, we need to lock the interaction before the end of state activity
		context.getGameStateRef().playerinteractionLock();

		// Check for unit abilities that do not require adjacency to attack
		if(currentTile.getUnitOnTile().getMonsterAbility() != null) {
			for(Ability a : currentTile.getUnitOnTile().getMonsterAbility()) {
				
				// If it has "ranged" ability
				if(a instanceof Unit_Ranged) {
					// Attack from current tile
					destination = currentTile; 
					
					// Execute Attack state
					IUnitPlayStates UnitAttackState = new UnitAttackActionState(destination, enemyTarget);
					System.out.println("Ranged Attacker, calling AttackAction from header of CombinedAction...");
					System.out.println("Destination " + destination);
					UnitAttackState.execute(context);
					break;
				}
			}
		}
			
		// CombinedAction state controls state flow when active
		context.setCombinedActive(true);
		
		// Build state reference variables
		if(destination == null && (enemyTarget != null)) {		
			System.out.println("enemyTarget is tile x: " + enemyTarget.getTilex() + ", y: " + enemyTarget.getTiley());	
		}
		
		// Select a destination for moving consistently with method
		unitDestinationSet(context); 
	
		// Executing unit states on a different thread as the time between them is reliant on UnitStopped (which runs on the same thread as the back end) 
		// Can't ask main thread to wait since it will block front end signals and need UnitStopped message
		Thread thread = new Thread(new ExecuteUnitStatesOnDifferentThread(context));
		thread.start();
		
	}


	public class ExecuteUnitStatesOnDifferentThread implements Runnable{
		
		// Class attributes
		GameplayContext context;

		public ExecuteUnitStatesOnDifferentThread(GameplayContext context) {
			this.context = context; 
		}
		
		public void run() {
			
			// Execute move state
			IUnitPlayStates unitMoveState = new UnitMoveActionState(currentTile, destination);	
			System.out.println("Calling MoveAction from CombinedAction...");
			System.out.println("Destination " + destination);
			unitMoveState.execute(context);
			
			// Wait for the Front end to finish (UnitStopped) before continuing
			while (context.getGameStateRef().getUnitMovingFlag()) {
				UpdateState.threadSleep();
			} 

			// Execute attack state
			System.out.println("Calling AttackAction from CombinedAction...");
			System.out.println("Destination " + destination);
			// Execute attack between units
			IUnitPlayStates UnitAttackState = new UnitAttackActionState(destination, enemyTarget);
			UnitAttackState.execute(context);
			
			// Finish combined State execution
			context.setCombinedActive(false);
			
			/** Reset entity selection and board **/  
			// Deselect after combined action
			context.deselectAllAfterActionPerformed();
			
			// Reset board visual (highlighted tiles)
			UpdateState.updateBoard(context.out, context.getGameStateRef());
			
			// Unlock after state activity complete
			/**===========================================**/
			context.getGameStateRef().playerinteractionUnlock();
			/**===========================================**/
		}
	}
	
	/* Method for selecting a destination tile for attacking unit to move to.
	 * If no destination found, return false (boolean return for developer clarity).	*/
	private boolean unitDestinationSet(GameplayContext context) {
		
		/***	Establish unit's available actions		***/
		
		// Retrieve frequently used data
		Tile currentLocation = currentTile;
		
		// Selected unit's movement and collective action ranges
		ArrayList <Tile> actRange; 
		ArrayList <Tile> moveRange = new ArrayList<Tile>();
		
		// Account for movement impairing debuffs (i.e. Provoke)
		if (context.getGameStateRef().useAdjustedMonsterActRange()) {
			
			actRange = context.getGameStateRef().getTileAdjustedRangeContainer();
			
			for (Tile t : context.getGameStateRef().getTileAdjustedRangeContainer()) {
				if (t.getUnitOnTile() == null) {	moveRange.add(t);	}
			}
		}
		else {
			moveRange = context.getGameStateRef().getBoard().unitMovableTiles(currentLocation.getTilex(), currentLocation.getTiley(), currentLocation.getUnitOnTile().getMovesLeft());
			actRange = context.getGameStateRef().getBoard().unitAttackableTiles(currentLocation.getTilex(), currentLocation.getTiley(), currentLocation.getUnitOnTile().getAttackRange(), currentLocation.getUnitOnTile().getMovesLeft());
			actRange.addAll(moveRange);
		}

		// Check enemy is in attack range (all action tiles are attackable)
		if(!(actRange.contains(enemyTarget))) {	
			System.out.println("Enemy is not in range.");
			return false;
		}

		
		// Get potential destination tiles adjacent to enemy && within movement range
		ArrayList <Tile> temp = context.getGameStateRef().getBoard().adjacentTiles(enemyTarget);
		ArrayList <Tile> options = new ArrayList<Tile>(10); 
		for(Tile t : temp) {
			if(moveRange.contains(t)) {
				options.add(t);
			}
		}
		
		// Select destination from options | first preference = cardinal direction from enemy (NESW)
		// Check for cardinal and remove redundant tiles
		for(Tile t : options) {
			
			// Remove if tile not available 
			if (t.getUnitOnTile() != null) {
				options.remove(t); 
			}
			else {
				// If available tile is cardinal, designate it
				if((Math.abs(enemyTarget.getTilex() - t.getTilex()) + (Math.abs(enemyTarget.getTiley() - t.getTiley()))) == 1) {
					destination = t;
					break;
				}
			}
		}
		
		// If no cardinals available, choose any first option available
		if(options.size() > 0) {
			destination = options.get(0);
			return true;
		}
		else {
			System.out.println("No possible destination during unit combined state.");
			return false;
		}
	}
}




