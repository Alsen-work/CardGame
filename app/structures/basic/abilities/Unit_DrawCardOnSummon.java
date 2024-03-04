
package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_DrawCardOnSummon implements Ability {
	
		// Ability attributes 
		private boolean enemyTarget; 
		private Class<? extends Monster> targetType; 
		private ActivateMoment activateMoment;
		EffectAnimation eAnimation; 
		
		// Constructors
		public Unit_DrawCardOnSummon(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
			this.enemyTarget = enemyTarget;
			this.targetType = targetType; 
			this.eAnimation = eAnimation;
			this.activateMoment = ActivateMoment.Summon;
		}
		
		
		/* Class methods */
		
		// ABILITY IMPLEMENTATION
		// ================================================================================
		// Both players draw a card when this Unit is summoned. 
		// Ignore the Monster input since this ability doesnt affect Monsters
		public boolean execute(Monster target, GameState gameState) {
			return execute(gameState); 
		}
		
		// Can also just call this method, monsters shouldnt be inputed anyway
		public boolean execute(GameState gameState) {
		
			gameState.getPlayer().getHand().drawCard(gameState.getPlayer().getDeck());
			gameState.getComputer().getHand().drawCard(gameState.getComputer().getDeck());

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

	

