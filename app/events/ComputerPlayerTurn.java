package events;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.UpdateState;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import structures.GameState;
import events.gameplaystates.unitplaystates.AIUnitStateController;
import structures.basic.ComputerLogic.*;
import structures.basic.ComputerPlayer;
import structures.basic.Spell;
import structures.basic.Tile;

public class ComputerPlayerTurn {

	// Attribute
	Thread AIthread = new Thread(); 

	
	// Constructor
	public ComputerPlayerTurn(ActorRef out, GameState g) {

		this.AIthread = new Thread(new RunComputerTurnOnThread(out, g)); 
	}

	
	public void processComputerActions() {

		AIthread.start();
	}

	public static class RunComputerTurnOnThread implements Runnable {
		
		// Attributes
		GameState g; 
		ActorRef out;

		// Constructor 
		public RunComputerTurnOnThread(ActorRef out, GameState gameState) {
			this.g = gameState;
			this.out = out;
		}

		// Thread run
		public void run() {

			ReentrantLock counterLock = new ReentrantLock();
			counterLock.lock();
			
			ComputerPlayer pl2 = (ComputerPlayer) g.getComputer();
			ComputerPlayer compPlayer = pl2;
			AIUnitStateController controller = new AIUnitStateController(out, g);
			compPlayer.setHPBenchMark(10);


			ArrayList<structures.basic.ComputerLogic.ComputerInstruction> cardsToPlay, monstersToMove, attacksToPerform;

			
			cardsToPlay = compPlayer.playCards(g.getBoard());

			if (!cardsToPlay.isEmpty() && cardsToPlay != null) {

				for (ComputerInstruction cI : cardsToPlay) {
					System.out.println(cI);

					if (cI.getCard() == null || cI.getTargetTile() == null) continue; 
					else { 
						System.out.println("get class: " + cI.getCard().getClass().getName());
						System.out.println("get associated class: " + cI.getCard().getCardType().getName());
						if  (cI.getCard().getCardType() == Spell.class) controller.spellCast(cI.getCard(), cI.getTargetTile());
						else { 
							controller.summonMonster(cI.getCard(), cI.getTargetTile());

							// Wait between action types
							waitForActionsToComplete();
						}
					}
				}

				// Wait between action types
				waitForActionsToComplete();

				try {Thread.sleep(15000);} catch (InterruptedException e) {e.printStackTrace();}

				
				attacksToPerform = compPlayer.performAttacks(g.getBoard());

				if (attacksToPerform != null && !attacksToPerform.isEmpty()) {
					System.out.println("Attacks: ");
					for (ComputerInstruction cI : attacksToPerform) {
						System.out.println(cI);
						if (cI.getActor() == null || cI.getTargetTile() == null) continue;

						Tile currTile = g.getBoard().getTile(cI.getActor().getPosition().getTilex(), cI.getActor().getPosition().getTiley());
						controller.unitAttack(currTile, cI.getTargetTile());

						// Wait between action types
						waitForActionsToComplete();
					}
				}

				else {
					System.out.println("no attacks to perform");
				}


				// Wait between action types
				waitForActionsToComplete();
				
				try {Thread.sleep(15000);} catch (InterruptedException e) {e.printStackTrace();}

				monstersToMove = compPlayer.moveMonsters(g.getBoard());

				//check if empty

				if (!monstersToMove.isEmpty() && monstersToMove != null) {
					for (ComputerInstruction cI : monstersToMove) {
						
						if (cI.getActor() == null || cI.getTargetTile() == null) continue;

						Tile currTile = cI.getActor().getPosition().getTile(g.getBoard());
						controller.unitMove(currTile, cI.getTargetTile());

						// Wait between action types
						waitForActionsToComplete();
					}
				}
				else System.out.println("no moves to make");

				// Wait between action types
				waitForActionsToComplete();
				
				try {Thread.sleep(15000);} catch (InterruptedException e) {e.printStackTrace();}

			}
			
			// End turn
			g.computerEnd();
			BasicCommands.addPlayer1Notification(out,g.getTurnOwner().toString() + "'s turn!", 2);

			counterLock.unlock();
		}

		public void waitForActionsToComplete() {

			// Wait between action types
			while (g.getUnitMovingFlag() || g.playerinteractionLocked()) {
				UpdateState.threadSleepLong();
			}
		}

	}
}
