package structures.basic.abilities;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public interface Ability {

	//Different monster/spell has different ability, the execute is to make sure the ability take effect at the right time and to the right target

	public boolean execute(Monster monsterEntity, GameState gameState); 
	

	public Class<? extends Monster> getTargetType();


	public boolean targetEnemy();
	
    //get the enum representing the activating moment of the ability
	public ActivateMoment getActivateMoment();
	
    //get the effect animation to displayed in the front end
	public EffectAnimation getEffectAnimation();
}
