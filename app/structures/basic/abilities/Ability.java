package structures.basic.abilities;

import structures.GameState;
import structures.basic.Monster;
public interface Ability {

    public boolean execute(Monster monsterEntity, GameState gameState);
    public Class<? extends Monster> getTargetType();
    public boolean targetEnemy();

    public ActivateMoment getCurrentState();
}
