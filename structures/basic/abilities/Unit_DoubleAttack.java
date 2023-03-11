package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_DoubleAttack implements Ability{
    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private ActivateMoment activateMoment;
    EffectAnimation eAnimation;

    public Unit_DoubleAttack(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;

        this.activateMoment = ActivateMoment.construction;
    }

    public boolean execute(Monster targetMonster, GameState gameState) {
        return execute(targetMonster);
    }

    public boolean execute(Monster targetMonster) {

        targetMonster.setAttacksMax(2);

        if(targetMonster.getAttacksLeft() > 0) {	targetMonster.setAttacksLeft(targetMonster.getAttacksMax());	}
        return true;
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
