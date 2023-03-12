package events.gameplaystates.unitplaystates;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.UpdateStatus;
import events.gameplaystates.GameplayContext;
import structures.basic.*;
import structures.basic.abilities.Ability;
import structures.basic.abilities.ActivateMoment;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

public class SummonMonsterState implements IUnitPlayStates {

	/*** State attributes ***/
	private Tile 				targetTile;
	private ArrayList <Tile> 	summonRange;
	
	
	/*** State constructor ***/
	/* 
	 * Changed constructor to input current and target tiles to decouple Unit states from TileClicked
	 * Previously Unit states had tilex, tiley be used from context which were variables recieved from TileClicked. 
	 * Decoupling required to use unit States from the ComputerPlayer.
	 * 	/*
	 * 改变了构造函数以输入当前和目标瓷砖，使单元状态与TileClicked脱钩。
	 * 以前的单元状态有tilex，可以从上下文中使用，而上下文是由TileClicked接收的变量。
	 * 为了使用ComputerPlayer的单元状态，需要进行解耦。*/
	
	public SummonMonsterState(Tile targetTile) {
		this.targetTile = targetTile;
		this.summonRange = null;
	}
	
	
	/*** State method ***/
	
	public void execute(GameplayContext context) {
		
		System.out.println("In SummonMonsterSubState.");
		

		/**===========================================**/
		context.getGameStateRef().userinteractionLock();
		/**===========================================**/

		
		
		/** Build summon range **/
		// Use adjusted range is monster has special skill on summoe
		//在怪物有特殊技能的情况下，使用调整范围。
		if (context.getGameStateRef().useAdjustedMonsterActRange()) {

			// Abilities with adjusted act range populate this container with adjusted range
			summonRange = context.getGameStateRef().getTileAdjustedRangeContainer();
		}
		else {
			summonRange = context.getGameStateRef().getBoard().allSummonableTiles(context.getGameStateRef().getRoundPlayer());
		}
		
		
		// Target tile within summon range && sufficient player mana check
		//召唤范围内的目标瓦片&&足够的玩家法力检查
		if(tileInSummonRange() && sufficientMana(context.getGameStateRef().getRoundPlayer(), context.getLoadedCard())) {
			
			// Verbose output
			//冗长的输出
			BasicCommands.addPlayer1Notification(context.out, "Monster summoned!", 2);
			
			// Execute summon method
			// 执行召唤方法
			summonMonster(context, context.out, context.getLoadedCard().getConfigFile(), context.getLoadedCard(), this.targetTile);
			

			// Update board counter for num Monsters
			//更新Num Monsters的棋盘计数器
			//context.getGameStateRef().getBoard().updateUnitCount(1);

			
			/** Delete card from Hand + update visual **/
			/**从手中删除卡片+更新视觉 **/
			
			// Index variables
			int cardIndexInHand = context.getGameStateRef().getRoundPlayer().getHand().getSelectCardPos()-1;
			int oldHandSize =  context.getGameStateRef().getRoundPlayer().getHand().getHandList().size(); 	// How many UI cards to delete


			// Remove card
			System.out.println("Removing card: " + context.getGameStateRef().getRoundPlayer().getHand().getCardFromHand(cardIndexInHand).getCardname());
			context.getGameStateRef().getRoundPlayer().getHand().removeCard(cardIndexInHand);
			for(Card c : context.getGameStateRef().getRoundPlayer().getHand().getHandList()) {
				System.out.println("now Hand card id: " + c.getId() + " name " + c.getCardname());
			}


			// Only update Hand for Human player
			//if (context.getGameStateRef().getRoundPlayer() instanceof HumanPlayer) {
				UpdateStatus.updateHandCards(context.out, context.getGameStateRef(), oldHandSize, context.getGameStateRef().getRoundPlayer().getHand().getHandList());
			//}
		
			// Reset board visual (highlighted tiles)
			UpdateStatus.updateBoard(context.out, context.getGameStateRef());
		
			
			/** Reset entity selection and board **/  
			// Deselect after action finished
			//context.deselectAllAfterActionPerformed();
		}
		
		else {
		// Verbose console messages for debugging, simplify for submission
		//	用于调试的粗略的控制台信息，简化提交
			if(!tileInSummonRange()) {
				System.out.println("Tile is not in summon range.");
			} else if(!(sufficientMana(context.getGameStateRef().getRoundPlayer(), context.getLoadedCard()))) {
				System.out.println("Insufficient mana to summon this monster.");
				BasicCommands.addPlayer1Notification(context.out, "Need more mana.", 1);
				UpdateStatus.updateBoard(context.out, context.getGameStateRef());
			} else {
				System.out.println("Can't summon Monster, please try again.");
			}
			
		}
		
		
		/**===========================================**/
		context.getGameStateRef().userinteractionUnlock();
		/**===========================================**/
	}

	
	public void summonMonster(GameplayContext context, ActorRef out, String u_configFile, Card statsRef, Tile summonTile) {
		
		// Mana cost application due to summon monster
		context.getGameStateRef().getRoundPlayer().loseMana(statsRef.getManacost());
		UpdateStatus.updatePlayerHealth(out, context.getGameStateRef());
		UpdateStatus.updatePlayerMana(out, context.getGameStateRef());

		// Need some code about retrieving StaticConfFiles matching card from Deck here
		Monster summonedMonster = (Monster) BasicObjectBuilders.loadMonsterUnit(u_configFile,statsRef,context.getGameStateRef().getRoundPlayer(),Monster.class);
		summonedMonster.setPositionByTile(context.getGameStateRef().getBoard().getTile(summonTile.getTilex(),summonTile.getTiley()));
		summonedMonster.setOwner(context.getGameStateRef().getRoundPlayer());
		UpdateStatus.threadSleep();
		
		// Add unit to tile on board
		summonTile.addUnit(summonedMonster);
		UpdateStatus.threadSleep();
		
		// Summon animation
		EffectAnimation summonEf = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.playEffectAnimation(out, summonEf, summonTile);
		
		// Drawing summoned monster with stats on the board
		UpdateStatus.drawUnitWithStats(out, summonedMonster, summonTile);
		UpdateStatus.threadSleep();
		
		// Check for on-summon triggers: permanent changes to Monster statistics, abilities activated by summoning
		checkForSummonTriggers(summonedMonster, context);
		
	}
	
	
	
	// Checking for triggered abilities: construction-related, summon-method related
	private void checkForSummonTriggers(Monster summonedMonster, GameplayContext context) {
		
		if(summonedMonster.hasAbility()) {	
			checkForConstructionAbilityActivation(summonedMonster, context);
			checkForSummonAbilityActivation(summonedMonster, context);	
		}	
		
	}
	
	// Abilities activated directly after object construction to permanently modify the Unit
	private void checkForConstructionAbilityActivation(Monster summonedMonster, GameplayContext context) {
		
		if (summonedMonster.getMonsterAbility() != null ) {
			for(Ability a : summonedMonster.getMonsterAbility()) {
				if(a.getActivateMoment() == ActivateMoment.construction) {
					a.execute(summonedMonster, context.getGameStateRef());
				}
			}
		}
	}
	
	// Abilities activated by the act of summoning in the game logic
	private void checkForSummonAbilityActivation(Monster summonedMonster, GameplayContext context) {

		if (summonedMonster.getMonsterAbility() != null ) {

			for(Ability a : summonedMonster.getMonsterAbility()) {
				if(a.getActivateMoment() == ActivateMoment.Summon) {
					System.out.println("Ability:" + a);

					// Target logic
					if (a.getTargetType() == Avatar.class) {

						// Avatar reference
						Avatar targetAvatar; 

						// Set avatar target based on ability taget
						if (a.targetEnemy() == false) {						
							targetAvatar = context.getGameStateRef().getHumanAvatar(); 
						}
						else {
							targetAvatar = context.getGameStateRef().getAiAvatar();
						}

						// Execute and play animations
						BasicCommands.playUnitAnimation(context.out, summonedMonster, UnitAnimationType.channel);
						UpdateStatus.threadSleep();
						if (a.getEffectAnimation() != null) {
							BasicCommands.playEffectAnimation(context.out, a.getEffectAnimation(), context.getGameStateRef().getHumanAvatar().getPosition().getTile(context.getGameStateRef().getBoard()));
							UpdateStatus.threadSleep();
						}
						BasicCommands.playUnitAnimation(context.out, summonedMonster, UnitAnimationType.idle);

						// Execute ability
						a.execute(targetAvatar, context.getGameStateRef());
					}
					
					else if (a.getTargetType() == Monster.class) {

						// Monster reference
						Monster targetMonster; 

						if (a.targetEnemy() == false) {
							// Assume can only target itself atm but allies can be implemented when abilities of that kind are added
							targetMonster = summonedMonster;
						} else {
							// Not acting on enemy monsters atm, would need to obtain ref from board
							targetMonster = summonedMonster; // Placeholder
						}		
						a.execute(targetMonster, context.getGameStateRef());

					} else {

						//Other abilities: Draw cards on summon currently present in the game
						a.execute(null, context.getGameStateRef());

						// Draw card reprint happens soon after 
					}
				}
			}
		}	
	}


	
	/*	Helper methods	*/
	// Returns true if the clicked targetTile is within a Player's summon range
	private boolean tileInSummonRange() {
		System.out.println("ileInSummonRange:"+summonRange);
		if(summonRange.contains(targetTile)) {	return true;	}
		return false;
	}
	
	// Returns true if Player has sufficient mana to cover the card's playing cost
	private boolean sufficientMana(Player p, Card mon) {
		System.out.println("Turn owner has: " + p.getMana() + " mana");
		if(p.getMana() - mon.getManacost() >= 0) {	return true;	}
		return false;
	}
	
}


