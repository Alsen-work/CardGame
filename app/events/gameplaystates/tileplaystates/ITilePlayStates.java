package events.gameplaystates.tileplaystates;
import events.gameplaystates.GameplayContext;

/**
 * Interface class for Tile states. 
 * Ensures tile states have an execute method and can be polymorphically references where required.
 * 瓦片状态的接口类。
 * 确保瓦片状态有一个执行方法，并且在需要时可以多态地引用。
 *
 * CardPreviouslySelectedState
 * 这个瓦片状态是为之前已经选择了一张牌，而用户要在用户界面上选择另一个东西时使用的。
 * 要在用户界面中选择另一个东西，例如，一个用于召唤的空牌或另一个用于攻击的单位。
 *
 * SingleSelectedState这个状态是在没有任何有效的输入的情况下选择一个单元，例如
 * 另一个先前选定的单位或卡片。它主要用于显示单位行动。
 *
 * UnitPreviouslySelectedState:
 * 这个状态是当一个单位以前被选中并且用户
 * 要求在做下一个动作时使用这个信息，例如点击来
 * 攻击另一个单位
 */
public interface ITilePlayStates {

	// All State classes must implement an execute(..) method to play any gameplay logic related to that type of state.
	// 所有的状态类都必须实现execute(..)方法，以播放与该类型状态相关的任何游戏逻辑。

	public void execute(GameplayContext context); 
}
