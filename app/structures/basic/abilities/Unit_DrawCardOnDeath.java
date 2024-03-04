
package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_DrawCardOnDeath implements Ability {
	
		// Ability attributes 
		private boolean enemyTarget; 
		private Class<? extends Monster> targetType; 
		private ActivateMoment activateMoment;
		EffectAnimation eAnimation; 
		
		// Constructors
		public Unit_DrawCardOnDeath(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
			this.enemyTarget = enemyTarget;
			this.targetType = targetType; 
			this.eAnimation = eAnimation;
			this.activateMoment = ActivateMoment.Death;
		}
		

		/* Class methods */
		
		// ABILITY IMPLEMENTATION
		// ================================================================================
		// Player draws card when this unit is defeated. 
		// Ignore the Monster input since this ability doesnt affect Monsters
		public boolean execute(Monster target, GameState gameState) {
			
			target.getOwner().getHand().drawCard(gameState.getTurnOwner().getDeck());
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

	

