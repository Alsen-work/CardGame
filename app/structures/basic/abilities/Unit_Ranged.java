
package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_Ranged implements Ability {
	
		// Ability attributes 
		private boolean enemyTarget; 
		private Class<? extends Monster> targetType; 
		private ActivateMoment activateMoment;
		EffectAnimation eAnimation; 
		
		// Constructors
		public Unit_Ranged(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
			this.enemyTarget = enemyTarget;
			this.targetType = targetType; 
			this.eAnimation = eAnimation;
			this.activateMoment = ActivateMoment.construction;
		}
		

		/* Class methods */
		
		// ABILITY IMPLEMENTATION
		// ================================================================================
		// Monster can attack from anywhere on the Board
		// Ignore GameState input since Ability deals with Unit internal values
		public boolean execute(Monster target, GameState gameState) {

			// Set to board maximum dimensions
			int boardWidth = gameState.getBoard().getBoardWidth(); 
			int boardLength = gameState.getBoard().getBoardLength(); 
			
			// Set attack range to this (so can hit from corner to opposite corner)
			target.setAttackRange(boardWidth * boardLength);
			return true; 
		}
		// ================================================================================
		
		
		// Getters to communicate target and call chronology information
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

}

	

