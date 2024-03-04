package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_HealAvatarOnSummon implements Ability {

	
	// Ability attributes 
	private boolean enemyTarget; 
	private Class<? extends Monster> targetType; 
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation; 
	
	// Constructors
	public Unit_HealAvatarOnSummon(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation; 
	
		this.activateMoment = ActivateMoment.Summon;
	}
	
	
	/* Class methods */
	
	// ABILITY IMPLEMENTATION
	// ================================================================================
	// Heal avatar 3 hp if this unit is summoned
	public boolean execute(Monster target, GameState gameState) {
		
		// Heal the Avatar 3 HP
		if (target.getClass() == targetType) {
			System.out.println("Avatar HP:" + target.getHP());
			target.heal(3);
			System.out.println("Avatar HP:" + target.getHP());
			return true; 
		}
		System.out.println("did not go in for avatar");

		return false; 
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


