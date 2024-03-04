
package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_DoubleAttack implements Ability {

		private boolean enemyTarget; 
		private Class<? extends Monster> targetType; 
		private ActivateMoment activateMoment;
		EffectAnimation eAnimation; 
		
		// Constructors
		public Unit_DoubleAttack(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
			this.enemyTarget = enemyTarget;
			this.targetType = targetType; 
			this.eAnimation = eAnimation;
			this.activateMoment = ActivateMoment.construction;
		}
		
		
		/* Class methods */
		
		// ABILITY IMPLEMENTATION
		// ================================================================================
		// Monster has the ability to attack twice
		// Ignore GameState input since Ability deals with Unit internal values
		public boolean execute(Monster target, GameState gameState) {
			return execute(target);
		}
		
		// Alternative method signature that reflects actual behaviour
		public boolean execute(Monster target) {

			target.setAttacksMax(2);
			
			// For unusual cases where a Monster is summoned off cooldown (has attacksLeft after summoning):
			if(target.getAttacksLeft() > 0) {	target.setAttacksLeft(target.getAttacksMax());	}
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

	

