package structures.basic.ComputerLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.ComputerPlayer;
import structures.basic.Monster;
import structures.basic.Tile;


public class ComputerMoveMonsterLogic {
	private ComputerPlayer player;
	
	public ComputerMoveMonsterLogic(ComputerPlayer p) {
		this.player = p;
	}
	

			public ArrayList<structures.basic.ComputerLogic.ComputerInstruction> movesUnits(Board gameBoard){
				ArrayList<Monster> movableMonsters = this.allMovableMonsters(gameBoard);
				if(movableMonsters.isEmpty()) return new ArrayList<structures.basic.ComputerLogic.ComputerInstruction>();
				
				
				ArrayList<MonsterTileOption> listofMTO = this.getMonstersOptions(movableMonsters, gameBoard);
				
				
				return this.matchMonsterAndTile(listofMTO);
			}

			private ArrayList <Monster> allMovableMonsters(Board gameBoard){
				ArrayList <Monster> myMonsters = gameBoard.friendlyUnitsWithAvatar(player);
				//System.out.println("num mosters I can move bf check: " + myMonsters.size());
				myMonsters.removeIf(m -> (m.getMovesLeft()<=0 || m.getOnCooldown()));
				//System.out.println("after check: " + myMonsters.size());
				
				return myMonsters;
			}

			private ArrayList<MonsterTileOption> getMonstersOptions(ArrayList<Monster> list, Board gameBoard){

				ArrayList<MonsterTileOption> optionList = new ArrayList<MonsterTileOption>();
				
				for (Monster m : list) {
					//System.out.println("calculating tile options for monster: " + m.getName());
					optionList.add(new MonsterTileOption (m, gameBoard));   
				}
				
				return optionList;
			}

			private ArrayList<ComputerInstruction> matchMonsterAndTile (ArrayList<MonsterTileOption> optionList){
				//sorting array based on value of top tile 
				Collections.sort(optionList);
				
				
				//method returns an array list of computer instruction objs
				//each of those objs contains a monster to be moved and a target tiles
				ArrayList <ComputerInstruction> compMoves = new ArrayList<ComputerInstruction>();
				
				//this set keep track of tiles that have already been used as a target tile 
				//so no other monster should be added to it
				HashSet <Tile> tileUsed = new HashSet<Tile>();
				
				int k = 0;
				
				//for loop iterates over the list of (monster - list of tiles) objs (MLT) passed to the method
				for (MonsterTileOption mto: optionList) {
					//for each MLT the top tile is retrieved (k=0)
					//this is the tile with the highest score
					if (mto.getList().isEmpty() || mto.getList() == null || mto.getList().get(k).getScore() < 0) continue;
					
					Tile t = mto.getList().get(k);
				
					//boolean variable for testing purposes
					boolean tileFound = false;
					
					//creating a CI reference
					ComputerInstruction inst = null;
					
					//this checks if the top tile for the given monster (within the MLT obj) has already been used
					if (!(tileUsed.contains(t))){
						//if the best tile has not been used already, a new instruction is created passing it the monster within the MLT obj at optionList[i]
						//and the target tile t
						inst = new ComputerInstruction(mto.getM(), t);
						//adding the new instruction object to the list to be returned 
						compMoves.add(inst);
						//adding the target tile to the tile used set
						tileUsed.add(inst.getTargetTile());
						tileFound = true;
						
						continue;
					}
					else {
						
						if (mto.getList().size() <= k) continue;
						//this part of the code is executed if the tileUsed set already contains target tile t
						do {
							//incrementing k
							k++;
							//to retrieve the new best tile within the MLT obj
							t = mto.getList().get(k);
							
						//checking if the new tile t is in the set already and if there are still tiles to be tested within the MLT obj	
						} while(tileUsed.contains(t)&& k<mto.getList().size()+1);
						
						//once the above loop terminates
						//this condition checks that the do-while loop terminated because a tile was found
						//if tile was found current target tile t is not in set
						if (!tileUsed.contains(t)) {
							tileFound = true;
							//creating a computer instruction with monster with MTL obj and current target tile
							inst = new ComputerInstruction(mto.getM() , t);
							//adding tile to used tile set
							tileUsed.add(t);
							//adding new computer instruction to list to be returned
							compMoves.add(inst);
						}
					}
					//resetting value of k for next loop iteration
					k=0;
				}	
				
				return compMoves;	
			}

			static class MonsterTileOption implements Comparable<MonsterTileOption> {
				Monster m; 
				ArrayList<Tile> list;
				
				double score;
				//private int inRangeScore = -1;
				//private int bringsEnemyInRange = 2; 
				MonsterTileOption(Monster m, Board b){
					this.m = m;
					this.list = b.unitMovableTiles(m.getPosition().getTilex(), m.getPosition().getTiley(), m.getMovesLeft());
					//System.out.println("number of movabale tiles (line 161) : " + list.size());
					if(list != null && !(list.isEmpty())) {
						
						for (Tile t : list) {
							this.calcTileMoveScore(m,b,t);
							//System.out.println(" tile ( "+t.getTilex() + " - " + t.getTiley() + " ) score: " + t.getScore());
						}
						Collections.sort(list);
						this.score = this.list.get(0).getScore();
					}
					else if (list.isEmpty()) this.score = -1.0;
					
				}
				
				public Monster getM() {
					return m;
				}
				
				public ArrayList<Tile> getList(){
					return this.list;
				}
				
				public double getScore() {
					return this.score;
				}
				

				@Override
				public int compareTo(MonsterTileOption o) {
				
					if (this.score > o.getScore()) return -1;
					else if (this.score < o.getScore()) return 1;
					else return 0;
				}
				
				
				//logic for scoring tiles from movement perspective 
				private void calcTileMoveScore(Monster m, Board b, Tile targetTile) {
					//tile where monster is currently located
					Tile currTile = b.getTile(m.getPosition().getTilex(), m.getPosition().getTiley());

					//calculate which enemy tiles are in range from the would be (WB) tile
					HashSet <Tile> wBAttackable = b.calAttackRange(targetTile.getTilex(), targetTile.getTiley(), m.getAttackRange(), m.getOwner());
				
					//get all tiles that this monster could attack from its current tile (with enemies on them)
					HashSet<Tile> currAttackable = b.calAttackRange(currTile.getTilex(), currTile.getTiley(), m.getAttackRange(), m.getOwner());
				
					int deltaOne =  wBAttackable.size() - currAttackable.size();
					//if deltaOne is pos means that new tile would increase num of enemies attackable
					
					//all tiles on the board with an enemy unit on it
					ArrayList <Tile> enemyTilesOnBoard = b.enemyTile(m.getOwner());
				
					int currAttackableByEnemy = 0;
					int wBAttackableByEnemy = 0;
				
					for (Tile t : enemyTilesOnBoard) {
						Monster mnstr = t.getUnitOnTile();
						int x = t.getTilex();
						int y = t.getTiley();

						ArrayList<Tile> tilesEnemyCanAttack = b.unitAttackableTiles(x, y, mnstr.getAttackRange(), mnstr.getMovesLeft());
					
						if (tilesEnemyCanAttack.contains(targetTile)) wBAttackableByEnemy++;
						if (tilesEnemyCanAttack.contains(currTile)) currAttackableByEnemy ++;
					}
					
					int deltaTwo =   currAttackableByEnemy - wBAttackableByEnemy;
					//if delta two is positive it means that the new tile would make the unit attackable by less enemies 
					
					
					
					if (m.getOwner().getHealth() <= ((ComputerPlayer) m.getOwner()).getHPBenchMark()) {
						deltaTwo *= 2;
					} else {
						deltaOne *= 2;
					}
					int score = deltaOne + deltaTwo;
					
					if (currTile.getTilex() > targetTile.getTilex()){ 
						if (m instanceof Avatar) score--;
						else score++;
					}
					
					targetTile.setScore(score);
				}
				
				
				
			}
			
			
			
}
