package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Spell_TrueStrike implements Ability{
    // Ability attributes
    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private ActivateMoment activateMoment;
    EffectAnimation eAnimation;


    // Constructor
    public Spell_TrueStrike(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;
        this.activateMoment = ActivateMoment.noTimeConstraint;
    }


    /* Class methods */

    // ABILITY IMPLEMENTATION
    // ================================================================================
    // Deal 2 damage to  an enemy Unit
    public boolean execute(Monster targetMonster, GameState gameState) {

        // Reduce Monster HP by 2
        targetMonster.getDamage(2);

        return true;


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
