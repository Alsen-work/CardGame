package structures.basic.abilities;

import java.util.ArrayList;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;
import structures.basic.Tile;

public class Unit_Provoke implements Ability{
	// Ability attributes 
	private boolean enemyTarget; 
	private Class<? extends Monster> targetType;
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation;

	// Constructor
	public Unit_Provoke(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation;
		this.activateMoment = ActivateMoment.UnitSelection;
	}

	public boolean execute(Monster target, GameState gameState) {

		// Clear container before calculating new display tiles
		gameState.getTileAdjustedRangeContainer().clear();

		int selectedUnitX = target.getPosition().getTilex();
		int selectedUnitY = target.getPosition().getTiley();
		
		System.out.println("Selected monster: " + selectedUnitX + "," + selectedUnitY);
 
		// All actionable tiles
		ArrayList<Tile> actionableTiles = gameState.getBoard().getUnitAllActionableTiles(selectedUnitX, selectedUnitY, target.getMovesLeft(), target.getAttackRange());
				
		// All moveable tiles for reference (reachAble tiles returns all tiles with no tiles removed due to Units) 
		ArrayList<Tile> moveableTiles 	= gameState.getBoard().unitMovableTiles(selectedUnitX, selectedUnitY, target.getMovesLeft());
		moveableTiles.add(target.getPosition().getTile(gameState.getBoard()));
		
		printTiles("Moveablee tiles", moveableTiles);
		
		// All attackable tiles for reference 
		ArrayList<Tile> attackableTiles = new ArrayList<Tile>(10); 
		for (int i = 0; i < actionableTiles.size(); i++) {
			
			if (!moveableTiles.contains(actionableTiles.get(i))) {
				attackableTiles.add(actionableTiles.get(i)); 
			}
		}
		
		printTiles("AttackableTiles", attackableTiles);
		
		// Provoke monster threat range 
		ArrayList<Tile> threatenedTiles = new ArrayList<Tile>(10); 
		ArrayList<Tile> provokingMonsterTiles = new ArrayList<Tile>(2);
		
		// Check if there is a provoking monster in action range
		for (Tile t : actionableTiles) {
			
			// If there is a Unit and is enemy
			if (t.getUnitOnTile() != null) {	
				
				// If it has an ability
				if (t.getUnitOnTile().getMonsterAbility() != null && (t.getUnitOnTile().getOwner() == gameState.getEnemyPlayer())) { 
					for (Ability ability : t.getUnitOnTile().getMonsterAbility() ) {
						
						if (ability instanceof Unit_Provoke) {
							
							// Generate threatened tiles (all adjacent tiles) (needs to be like this incase more than 1 provoke monster)
							ArrayList<Tile> temp = gameState.getBoard().adjacentTiles(t);
							temp.add(t); 
							
							for (Tile tempT : temp) {
								threatenedTiles.add(tempT);
							}
							
							// Keep track of the provoking monster for reference
							provokingMonsterTiles.add(t);
						}
					}
				}
			}
		}
		
		printTiles("Threatened tiles", threatenedTiles);
		printTiles("Provoke Monster tiles", provokingMonsterTiles);
		
		
		// Terminate ability of no condition to apply
		if (threatenedTiles.isEmpty()) {
			return false;
		}
		

		// Classified tile list (tiles with arbitrary type attached)
		ArrayList<ClassifiedTile> classifiedActionableTiles = new ArrayList<ClassifiedTile>(actionableTiles.size()); 
		
		// Iterate over actionablity tiles and generate a classified actionable tiles lit 
		for (Tile t : actionableTiles) {
			
			// Create tile with type and set the type using class inner method 
			ClassifiedTile tileWithType = new ClassifiedTile(t); 
			
			// Sets ClassifiedTile with type M,A,T,P (these are concatenated to form other characteristic tiles such as TM for threatened and in movement range)
			tileWithType.setType(gameState, threatenedTiles, moveableTiles, attackableTiles, provokingMonsterTiles);
			
			// Add the tile to the array
			classifiedActionableTiles.add(tileWithType);
		}
		
		// debug
		for (ClassifiedTile ct : classifiedActionableTiles) {
			System.out.println("Tile: " + ct.getTile().getTilex() + "," + ct.getTile().getTiley() + "Type: " + ct.getType());
		}

		
		// Return array
		ArrayList<Tile> displayTiles = new ArrayList<Tile>(10);

		// Apply conditions to each characteristic tile type 
		for (ClassifiedTile ct : classifiedActionableTiles) {
			
			// Add all movement tiles. Tile that selected unit is on is an M tile
			if (ct.getType().equals("M")) {
				displayTiles.add(ct.getTile());
			}
			
			// Can only add a TA and A tiles if adjacent to an M tile
			if (ct.getType().equals("A") || ct.getType().equals("TA")) {
								
				// First check if there is a Enemy Unit on the tile (no point displaying if not) 
				if (ct.getTile().getUnitOnTile() != null) {
					if (ct.getTile().getUnitOnTile().getOwner() == gameState.getEnemyPlayer()) {
					
						// Generate adjacent tiles to overlay
						ArrayList<Tile> adjacentTiles = gameState.getBoard().adjacentTiles(ct.getTile());

						// Relate all adjacent tiles with the classified tiles (find overlap)
						for (ClassifiedTile ct2 : classifiedActionableTiles) {
							if (adjacentTiles.contains(ct2.getTile())) {

								// If any of the adjacent tiles are an M tile
								if (ct2.getType().equals("M")) {
									displayTiles.add(ct.getTile());
								}
							}
						}
					}
				}
			}
			
			//  Can only add TM tiles if they are CARDINALLY adjacent to a movement tile
			if (ct.getType().equals("TM")) {
				
				// If selected unit is on a threatened tile (add threatening monster tile and return)
				if (ct.getTile().getUnitOnTile() == target) {
					
					// Force clear all added tiles (better to do this check at the start but inconvenient with how its set up) 
					displayTiles.clear();
					
					// Check closest provoking monster
					displayTiles.add(provokingMonsterTiles.get(0));			// Hard coding first one in cause lazy
					gameState.setTileAdjustedRangeContainer(displayTiles);
					return true;
				}
				
				// Calculate all cardinallyAdjacentTiles
				ArrayList<Tile> cardinallyAdjacentTiles = gameState.getBoard().cardinallyAdjTiles(ct.getTile()); 		
				
				// Relate all adjacent tiles with the classified tiles (find overlap)
				for (ClassifiedTile ct2 : classifiedActionableTiles) {
					if (cardinallyAdjacentTiles.contains(ct2.getTile())) {

						if (ct2.getType().equals("M")) {
							displayTiles.add(ct.getTile());
						}
					}
				}
			}
			
			// If tile is the provoking monster which is in attack range
			if (ct.getType().equals("TAP")) {
				displayTiles.add(ct.getTile());
			}
		}

		displayTiles.remove(target.getPosition().getTile(gameState.getBoard()));
		
		System.out.println("MovesLeft provke: " + target.getMovesLeft());
		
		// Set output
		gameState.setTileAdjustedRangeContainer(displayTiles);
		return true; 
		
	}

	public boolean targetEnemy() {
		return enemyTarget; 
	}
	
	public Class<? extends Monster> getTargetType() {
		return targetType; 
	}
	
	public ActivateMoment getActivateMoment() {
		return activateMoment;
	}
	
	public EffectAnimation getEffectAnimation() {
		return eAnimation;
	}

	private class ClassifiedTile {
		
		// Attributes
		private String 	type; 
		private Tile 	tile; 
		
		// Constructor
		private ClassifiedTile(Tile t) {
			this.tile = t; 
			type = ""; 
		}
		
		public Tile getTile() {
			return tile; 
		}
		
		public String getType() {
			return type;
		}
		
		// Set type of tile (M (movement) ,A (attackable) ,T (threatened by provoking monster) ,P (provoking monster) )
		public void setType(GameState gameState, ArrayList<Tile> threatenedTiles, ArrayList<Tile> moveableTiles, ArrayList<Tile> attackableTiles, ArrayList<Tile> provokingMonsterTiles) {
			
			// Check each list and append characteristic character 
		
			if (threatenedTiles.contains(tile)) {
				type += "T";
			}
			
			if (moveableTiles.contains(tile)) {
				
				// Check if a unit is on the tile, if so, if its an enemy convert to attack tile
				if (tile.getUnitOnTile() != null ) {
					if (tile.getUnitOnTile().getOwner() == gameState.getEnemyPlayer()) {
						type += "A";
					}
					else { 
						type += "M"; 
					}
				}
				else { 
					type += "M"; 
				}
			}
			
			if (attackableTiles.contains(tile)) {
				type += "A";
			}
			
			if (provokingMonsterTiles.contains(tile)) {
				type += "P";
			}
		}
	}

	public void printTiles(String desc, ArrayList<Tile> tiles) {
		
		System.out.println(desc + "\n");
		
		for (Tile t : tiles) {
			System.out.println(t.getTilex() + "," + t.getTiley());
		}
	}
}