package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_BuffWhenEnemySpellCast implements Ability {

	
	// Ability attributes 
	private boolean enemyTarget; 
	private Class<? extends Monster> targetType; 
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation; 
	
	// Constructors
	public Unit_BuffWhenEnemySpellCast(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation; 
	
		this.activateMoment = ActivateMoment.EnemySpellCast;
	}
	
	
	/* Class methods */
	
	// ABILITY IMPLEMENTATION
	// ================================================================================
	// If the enemy player casts a spell, this minion gains +1 attack and +1 health 
	public boolean execute(Monster target, GameState gameState) {
		
		target.heal(1);
		target.buffAttack(1);
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