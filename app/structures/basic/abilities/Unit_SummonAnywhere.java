package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_SummonAnywhere implements Ability {
    // Ability attributes
    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private ActivateMoment activateMoment;
    EffectAnimation eAnimation;


    // Constructor
    public A_U_SummonAnywhere(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;

        this.activateMoment = ActivateMoment.CardClicked;
    }


    public boolean execute(Monster targetMonster, GameState gameState) {

        gameState.getTileAdjustedRangeContainer().clear();

        // This ability is called when in the SummonMonsterState
        // Set the highlight tile container (used for ability specifics) in GameState to the tiles to be highlighted
        gameState.setTileAdjustedRangeContainer(gameState.getBoard().allFreeTiles());

        // Display in CardClicked as has connection to front end

        return true;
    }

    public boolean targetEnemy() {
        return enemyTarget;
    }

    public Class<? extends Monster> getTargetType() {
        return targetType;
    }

    public ActivateMoment getCallID() {
        return activateMoment;
    }

    public EffectAnimation getEffectAnimation() {
        return eAnimation;
    }
}
