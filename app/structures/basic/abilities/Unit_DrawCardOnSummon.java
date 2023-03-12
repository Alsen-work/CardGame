package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_DrawCardOnSummon implements Ability{
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


    // Both players draw a card when this Unit is summoned.
    // Ignore the Monster input since this ability doesnt affect Monsters
    public boolean execute(Monster targetMonster, GameState gameState) {
        return execute(gameState);
    }

    public boolean execute(GameState gameState) {

        gameState.getPlayer1().getHand().giveHand(gameState.getPlayer1(), gameState.getRoundNumber());
        gameState.getPlayer2().getHand().giveHand(gameState.getPlayer2(), gameState.getRoundNumber());

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
