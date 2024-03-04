package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Spell_SundropElixir implements Ability {

	// Ability attributes 
	private boolean enemyTarget; 
	private Class<? extends Monster> targetType; 
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation; 
	
	// Constructor
	public Spell_SundropElixir(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation;
		this.activateMoment = ActivateMoment.noTimeConstraint;
	}

	
	
	/* Class methods */
	
	// ABILITY IMPLEMENTATION
	// ================================================================================
	// Add 5 health to a Unit. This Cannot take a unit over its starting health.
	public boolean execute(Monster target, GameState gameState) {
		
		// Verbose variable/easy to change
		int healthIncreaseValue = 5;
		
		// Check if the +5 HP is greater than max hp, if so make monster HP = max (done in .heal() method)
		// If Monster is full health .heal() returns false and execution fails (return false)
		if(target.heal(healthIncreaseValue)) {
			return true; 
		}
		return false;
	}
	// ================================================================================
	
	// Getters to communicate target information
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
