package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Spell_EntropicDecay implements Ability{
    // Ability attributes
    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private ActivateMoment activateMoment;
    EffectAnimation eAnimation;

    public Spell_EntropicDecay(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;

        this.activateMoment = ActivateMoment.noTimeConstraint;
    }

    public boolean execute(Monster targetMonster, GameState gameState) {

        if (targetMonster.getClass() == targetType) {
            targetMonster.getDamage(targetMonster.getMaxHP());
            return true;
        }
        else {
            return false;
        }
    }

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
