package events.gameplaystates.unitplaystates;

import events.gameplaystates.GameplayContext;


/*
 * Interface class for all types of unit gameplay states.
 * States should be independent from each other and also reflect different unit actions such as move, attack
 * Implementation of these states should be independent from UI to allow the AI to use them too.

 * 用于所有类型的单位游戏状态的接口类。
 * 这些状态应该是相互独立的，同时也反映了不同的单位行动，如移动、攻击等。
 * 这些状态的实现应该是独立于用户界面的，以便让人工智能也能使用它们。
 */
public interface IUnitPlayStates {

	// All State classes must implement an execute(..) method to play any gameplay logic related to that type of state.
	// 所有状态类都必须实现execute(...)方法，以发挥与该类型状态相关的任何游戏逻辑。
	public abstract void execute(GameplayContext context); 
}
