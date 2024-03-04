package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_BuffAttackWhenAvatarDealDamage implements Ability {

	
	// Ability attributes 
	private boolean enemyTarget; 
	private Class<? extends Monster> targetType; 
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation; 
	
	// Constructors
	public Unit_BuffAttackWhenAvatarDealDamage(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation;
		this.activateMoment = ActivateMoment.AvatarDealDamage;
	}
	
	
	/* Class methods */
	
	// ABILITY IMPLEMENTATION
	// ================================================================================
	// If your avatar is dealt damage, this unit gains +2 attack 
	public boolean execute(Monster target, GameState gameState) {
		
		// Buff the target monster's attack by 2
		target.buffAttack(2);
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