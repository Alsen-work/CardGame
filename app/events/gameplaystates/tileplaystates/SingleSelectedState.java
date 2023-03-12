package events.gameplaystates.tileplaystates;
import events.gameplaystates.GameplayContext;
import events.gameplaystates.unitplaystates.IUnitPlayStates;
import structures.basic.Tile;

/**
 * This state is for when a unit has been selected without any valid previous input such as 
 * another previously selected unit OR card. It is used for displaying Unit actions primarily.
**/
/**
 * 这个状态是在没有任何有效的输入的情况下选择一个单元，例如
 * 另一个先前选定的单位或卡片。它主要用于显示单位行动。
 **/
public class SingleSelectedState implements ITilePlayStates{

	/** State attributes **/
	IUnitPlayStates unitState; // 单位状态类参考// Unit state class reference
	Tile currentTile; 		// 所选单元当前所在的瓦片// Current tile the unit selected is on

	/** State constructor **/ 
	public SingleSelectedState() {	
		unitState = null; 
		currentTile = null; 
	}
	
	
	/*** State method ***/

	public void execute(GameplayContext context) {
			
		System.out.println("In SingleSelectedState");
		context.debugPrint();

		// Set currentTile
		// 设置当前瓷砖
		currentTile = context.getClickedTile(); 

		// Determine the substate (UnitDisplayActionsState or nothing for now)
		// 确定子状态（UnitDisplayActionsState或暂时没有）。
		switch (context.getTileFlag().toLowerCase()) {
		
			case("friendly unit"): {
				System.out.println("friendly unit");
			//	unitState = new UnitDisplayActionsState(currentTile);
				break;
			}
			
			case("enemy unit"): {
				System.out.println("You don't own this Unit");
				break;
			}
			
			case("empty"): {
				System.out.println("Nice empty tile click buddy.");
				break; 
			}
			
			case("default"):{
				System.out.println("Hit default case in SingleSelectedState.");
				break;
			}
			
		}
			
		/** Execute sub-state **/
		
		if (unitState != null) {
			unitState.execute(context);
		}
	}
	
}
