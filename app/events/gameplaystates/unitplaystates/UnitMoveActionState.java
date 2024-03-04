package events.gameplaystates.unitplaystates;

import java.util.ArrayList;

import commands.*;
import structures.basic.*;
import events.gameplaystates.GameplayContext;


public class UnitMoveActionState implements IUnitPlayStates {

	private Tile currentTile; 
	private Tile targetTile;
	
	public UnitMoveActionState(Tile currentTile, Tile targetTile) {
		this.currentTile = currentTile;
		this.targetTile = targetTile; 
	}

	
	public void execute(GameplayContext context) {
		
		System.out.println("In UnitMoveActionSubState.");

		context.getGameStateRef().playerinteractionLock();

		context.setLoadedUnit(currentTile.getUnitOnTile());
		if(context.getLoadedUnit() == null) {	System.out.println("Error, current tile has no unit.");	}

		System.out.println("Target tile is:" + targetTile);
		unitMove(context); 

		if(!(context.getCombinedActive())) {

			context.deselectAllAfterActionPerformed();
			//  Reset board visual (highlighted tiles)
			UpdateState.updateBoard(context.out, context.getGameStateRef());

			context.getGameStateRef().playerinteractionUnlock();
		}
	}

	private void unitMove(GameplayContext context) {

		Monster mSelected = (Monster) context.getLoadedUnit();
		ArrayList <Tile> actRange; 
		ArrayList <Tile> moveRange = new ArrayList<Tile>();

		if (context.getGameStateRef().useAdjustedMonsterActRange()) {
			
			// Act range calculate by abilities etc (external factors)
			actRange = context.getGameStateRef().getTileAdjustedRangeContainer();
			
			for (Tile t : context.getGameStateRef().getTileAdjustedRangeContainer()) {
				
				System.out.println(t.getTilex() + "," + t.getTiley());
				if (t.getUnitOnTile() == null) {
					if (t != currentTile) {
						moveRange.add(t);
					}
				}
			}
		}
		else {
			moveRange = context.getGameStateRef().getBoard().unitMovableTiles(currentTile.getTilex(), currentTile.getTiley(), currentTile.getUnitOnTile().getMovesLeft());
			actRange = context.getGameStateRef().getBoard().unitAttackableTiles(currentTile.getTilex(), currentTile.getTiley(), currentTile.getUnitOnTile().getAttackRange(), currentTile.getUnitOnTile().getMovesLeft());
			actRange.addAll(moveRange);
		}

		if((!moveRange.isEmpty()) && moveRange.contains(targetTile)) {

			BasicCommands.addPlayer1Notification(context.out, "Monster moving!", 2);

			if (mSelected.move(targetTile)) {
				System.out.println("MovesLeft: " + mSelected.getMovesLeft());
				System.out.println("Monster on cooldown: " + mSelected.getOnCooldown());

				UpdateState.updateBoardTiles(context.out, actRange, 0);
				UpdateState.threadSleep();

				BasicCommands.drawTile(context.out, currentTile, 0);
				UpdateState.threadSleep();
                UpdateState.threadSleep();


				currentTile.removeUnit();
				targetTile.addUnit(mSelected);
				mSelected.setPositionByTile(targetTile);
				

				context.getGameStateRef().setUnitMovingFlag(true);

				BasicCommands.moveUnitToTile(context.out, mSelected, targetTile);
				UpdateState.threadSleep();

				BasicCommands.playUnitAnimation(context.out, mSelected, UnitAnimationType.move);
				UpdateState.threadSleep();
			}
		}

		else {	
			System.out.println("Can't complete move.");		
		}
	}
	
}



