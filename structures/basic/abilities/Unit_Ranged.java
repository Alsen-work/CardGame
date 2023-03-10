package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_Ranged implements Ability{

    private boolean enemyTarget;
    private Class<? extends Monster> targetType;
    private Call_IDs callID;
    EffectAnimation eAnimation;

    public Unit_Ranged(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
        this.enemyTarget = enemyTarget;
        this.targetType = targetType;
        this.eAnimation = eAnimation;

        this.callID = Call_IDs.construction;
    }


    public boolean execute(Monster targetMonster, GameState gameState) {

        int boardWidth = gameState.getBoard().getBoardWidth();
        int boardLength = gameState.getBoard().getBoardLength();

        targetMonster.setAttackRange(boardWidth * boardLength);
        return true;
    }

    public boolean targetEnemy() {
        return enemyTarget;
    }

    public Class<? extends Monster> getTargetType() {
        return targetType;
    }

    public Call_IDs getCallID() {
        return callID;
    }

    public EffectAnimation getEffectAnimation() {
        return eAnimation;
    }

}
