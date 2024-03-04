package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Spell_Truestrike implements Ability {

	private boolean enemyTarget;
	private Class<? extends Monster> targetType;
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation;
	
	
	// Constructor
	public Spell_Truestrike(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation;
		this.activateMoment = ActivateMoment.noTimeConstraint;
	}

	// Deal 2 damage to  an enemy Unit
	public boolean execute(Monster target, GameState gameState) {

		target.getDamage(2);

		return true; 

	}
	
	// Getters to pass the ability attributes
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
