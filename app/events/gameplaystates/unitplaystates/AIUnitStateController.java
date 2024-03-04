package events.gameplaystates.unitplaystates;

import events.gameplaystates.GameplayContext;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Monster;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.abilities.*;
import akka.actor.ActorRef;

public class AIUnitStateController {

	GameplayContext context; 

	public AIUnitStateController(ActorRef out, GameState gameState) {

		context = new GameplayContext(gameState, out);
	}
	

	public void unitAttack(Tile currentTile, Tile targetTile) {

		context.setLoadedUnit(currentTile.getUnitOnTile());
		
		IUnitPlayStates unitState = null;


		if((Math.abs(currentTile.getTilex() - targetTile.getTilex()) <=1 && (Math.abs(currentTile.getTiley() - targetTile.getTiley()) <= 1))
				|| checkForRangedAttacker(currentTile.getUnitOnTile())) {

			unitState = new UnitAttackActionState(currentTile, targetTile);
		}
		else {

			unitState = new UnitCombinedActionState(currentTile, targetTile);	
		}

		if (unitState != null) {
			unitState.execute(context);
		}
	}
	

	public void unitMove(Tile currentTile, Tile targetTile) {

		context.setLoadedUnit(currentTile.getUnitOnTile());

		IUnitPlayStates unitState = new UnitMoveActionState(currentTile, targetTile);

		unitState.execute(context);
	}
	

	public void summonMonster(Card monsterToSummon, Tile targetTile) {

		for(int i = 0; i < context.getGameStateRef().getTurnOwner().getHand().getHandList().size(); i++) {
			if(context.getGameStateRef().getTurnOwner().getHand().getCardFromHand(i).getId() == monsterToSummon.getId()) {
				context.getGameStateRef().getTurnOwner().getHand().setSelectedCard(monsterToSummon);
				context.getGameStateRef().getTurnOwner().getHand().setSelectedCardPos(i);
			}
			if(i == 6 && context.getGameStateRef().getTurnOwner().getHand().getSelectedCard() == null) {	
				System.out.println("Selected card not set in AI Controller");	
			}
		}

		context.setLoadedCard(monsterToSummon);

		if (context.getLoadedCard().getBigCard().getAttack() < 0) {	
			context.setCardClasstype(Spell.class);
		}

		else {
			context.setCardClasstype(Monster.class);
		}

		IUnitPlayStates unitState = new SummonMonsterState(targetTile);

		unitState.execute(context);
	}

	public void spellCast(Card spellToCast, Tile targetTile) {

		for(int i = 0; i < context.getGameStateRef().getTurnOwner().getHand().getHandList().size(); i++) {
			if(context.getGameStateRef().getTurnOwner().getHand().getCardFromHand(i).getId() == spellToCast.getId()) {
				context.getGameStateRef().getTurnOwner().getHand().setSelectedCard(spellToCast);
				context.getGameStateRef().getTurnOwner().getHand().setSelectedCardPos(i);
			}
			if(i == 6 && context.getGameStateRef().getTurnOwner().getHand().getSelectedCard() == null) {	System.out.println("Selected card not set in AI Controller");	}
		}

		context.setLoadedCard(spellToCast);

		IUnitPlayStates unitState = new CastSpellState(targetTile);
		
		// Execute state
		unitState.execute(context);
	}
	
	
	// Helper to check whether input Unit is a Ranged attacker (i.e. does not need to move to
	// attack)
	private boolean checkForRangedAttacker(Monster m) {
		if(m.hasAbility()) {
			for(Ability a : m.getMonsterAbility()) {
				if(a instanceof Unit_Ranged) {	return true;	}
			}
		}
		return false;
	}
	
}
