package structures.basic.abilities;

import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Monster;

public class Unit_Flying implements Ability {

	// Ability attributes 
	private boolean enemyTarget; 
	private Class<? extends Monster> targetType; 
	private ActivateMoment activateMoment;
	EffectAnimation eAnimation; 
	
	// Constructors
	public Unit_Flying(boolean enemyTarget, Class<? extends Monster> targetType, EffectAnimation eAnimation) {
		this.enemyTarget = enemyTarget;
		this.targetType = targetType; 
		this.eAnimation = eAnimation;
		this.activateMoment = ActivateMoment.construction;
	}
	
	// Flying units can move through enemy units
	
	/* Class methods */
	
	// ABILITY IMPLEMENTATION
	// ================================================================================
	// Monster can move anywhere on the Board
	public boolean execute(Monster target, GameState gameState) {
		
		// Set to board maximum dimensions
		int boardWidth = gameState.getBoard().getBoardWidth(); 
		int boardLength = gameState.getBoard().getBoardLength(); 
		
		// Set max moves to board length and width (gameState.getBoard().get....)
		System.out.print("In windshrike ability setup");
		target.setMovesMax(boardWidth*boardLength);

		return true;
	}

	// ================================================================================
	
	
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