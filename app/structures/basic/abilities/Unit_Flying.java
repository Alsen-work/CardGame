package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_Flying implements Ability{
    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private ActivateMoment activateMoment;
    EffectAnimation eAnimation;

    public Unit_Flying(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;

        this.activateMoment = ActivateMoment.construction;
    }

    public boolean execute(Monster targetMonster, GameState gameState) {

        int boardWidth = gameState.getBoard().getWidth();
        int boardLength = gameState.getBoard().getHeight();

        targetMonster.setMovesMax(boardWidth*boardLength);

        return true;
    }

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