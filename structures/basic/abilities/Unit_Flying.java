package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_Flying implements Ability{
    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private Call_IDs callID;
    EffectAnimation eAnimation;

    public Unit_Flying(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;

        this.callID = Call_IDs.construction;
    }

    public boolean execute(Monster targetMonster, GameState gameState) {

        int boardWidth = gameState.getBoard().getBoardWidth();
        int boardLength = gameState.getBoard().getBoardLength();

        System.out.print("In windshrike ability setup");
        targetMonster.setMovesMax(boardWidth*boardLength);

        return true;
    }
}
