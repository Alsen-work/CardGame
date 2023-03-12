package events.gameplaystates.tileplaystates;

import events.gameplaystates.GameplayContext;
import events.gameplaystates.unitplaystates.CastSpellState;
import events.gameplaystates.unitplaystates.IUnitPlayStates;
import events.gameplaystates.unitplaystates.SummonMonsterState;
import structures.basic.Monster;
import structures.basic.Spell;
import structures.basic.Tile;


/**
 * This tile state is for when a card has been previously selected and the user is
 * about to select another thing in the UI, e.g. an empty tile for summoning or another unit for attakcin
**/

/**
 * 这个瓦片状态是为之前已经选择了一张牌，而用户要在用户界面上选择另一个东西时使用的。
 * 要在用户界面中选择另一个东西，例如，一个用于召唤的空牌或另一个用于攻击的单位。
 **/
public class CardPreviouslySelectedState implements ITilePlayStates {

	/** State attributes **/
	IUnitPlayStates unitState; 		// 要执行的单位状态参考（召唤、施法等）。 // Unit state reference to execute (Summon, Castspell etc)
	Tile targetTile; 				// 该状态的目标瓦片// The tile which the state is to target
	
	
	/** Constructor **/
	public CardPreviouslySelectedState() {
		unitState = null; 
		targetTile = null; 
	}
	
	
	/*** State method ***/
	
	public void execute(GameplayContext context) {
	
		// Debug section 
		System.out.println("In CardPreviouslySelectedState.");
		context.debugPrint();
		
		/***	Set reference info for navigating state		***/		
		// Get selected card for use in the Sub state (Monster or Spell use)
		// 在子状态下获得所选的卡牌以使用（怪物或法术使用）。

		context.setLoadedCard( context.getGameStateRef().getRoundPlayer().getHand().getSelectCard() );
		
		// Determine class type of the loaded card
		// 确定加载的卡的类别类型

		context.setCardClasstype(context.getLoadedCard().getAssociatedClass());

		// Set targetTile for future reference
		// 设置目标瓦片，供将来参考

		targetTile = context.getClickedTile();
		
	
		// Determine the unit state (SummonMonster or Cast Spell) 
		// (to lower case just so case isnt a problem)
		// 确定单位状态（召唤怪物或施放法术）。
		// (小写，这样就不会有问题了)
		switch (context.getTileFlag().toLowerCase()) {
		
		case("friendly unit"): {
			
			// Add check for card type
			// 增加对卡片类型的检查
			if (context.getCardClasstype() == Spell.class) {
				unitState = new CastSpellState(targetTile);
				break; 
			}
			else {
				System.out.println("Can't summon Monster on occupied tile.");
				break;
			}
		}
		case("enemy unit"): {

			// Add check for card type
			// 增加对卡片类型的检查
			if (context.getCardClasstype() == Spell.class) {
				unitState = new CastSpellState(targetTile);
				break;
			}
			else {
				System.out.println("Can't summon Monster on occupied tile.");
				break;
			}
		}
		case("empty"): {
			if (context.getCardClasstype() == Monster.class) {
				unitState = new SummonMonsterState(targetTile);
				break;
			}
			else {
				System.out.println("Can't play Spell on empty tile.");
				break;
			}

		}
		}
		
		
		/** Execute Unit state **/
		
		if (unitState != null ) {
			unitState.execute(context);
		}
	}
	
	
}
