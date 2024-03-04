package structures;

import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.ComputerPlayer;
import structures.basic.Deck;
import structures.basic.Hand;
import structures.basic.HumanPlayer;
import structures.basic.Monster;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.abilities.Ability;
import structures.basic.abilities.AbilityToUnitLinkage;
import structures.basic.abilities.ActivateMoment;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;


import commands.*;


public class GameState {

	private Board 			gameBoard;			// Board object which holds all Unit positions aswell as contains operations to find specific tiles sets. 
	private Player          player;             // human player which holds all data for the player such as Hand and Deck for holding current cards. Also holds the control flow for drawing Cards from a Deck etc
	private Player          computer;           // computer player which holds the same as the above + AI logic for ranking combinations of instructions and actioning them.
	private Avatar          playerAvatar;
	private Avatar 			computerAvatar;
	private int			 	turnCount;			// accumulative int representing the current turn
	private static boolean 	playerDead;			// Boolean variable which is set when either avatar is defeated
	private Player 			turnOwner;			// The current turn owner of the game, refered to for certain checks such as having permission to click (the human player should not be able to select anything during the AI turn) 

	private ArrayList<Tile> tileAdjustedRangeContainer;		// Container array of tiles which store tiles to be highlight due to Abilities or anything else that requires distinct highlighting

	private boolean 		locked;				// User interface lock flag to control how the UI is interacted with
	private boolean			unitMovingFlag; 	// Unit moving flag to stop unit attacking while moving

	public GameState() {

		//Initialise the attributes
		turnCount = 1;											// Turn count set to 1, player turn
		playerDead = false;										// player alive
		tileAdjustedRangeContainer = new ArrayList<Tile>(); 	// Initialise the AdjustedRange container for some monster with special range

		locked = false; 				 
		unitMovingFlag = false; 

		// Initialising the linkage between monster, spell and  abilities
		AbilityToUnitLinkage.initialiseUnitAbilityLinkageData();

		// Deck instantiations
		Deck deckPlayer = new Deck();
		deckPlayer.deck1();

		Deck deckComputer = new Deck();
		deckComputer.deck2();

		// Instantiate players
		player = new HumanPlayer();
		player.setDeck(deckPlayer);
		deckPlayer.shuffleDeck();
		computer = new ComputerPlayer();
		computer.setDeck(deckComputer);
		deckComputer.shuffleDeck();

		// Set hands
		Hand handPlayerOne = new Hand();
		player.setHand(handPlayerOne);
		handPlayerOne.initHand(deckPlayer);

		Hand handPlayerTwo = new Hand();
		computer.setHand(handPlayerTwo);
		handPlayerTwo.initHand(deckComputer);


		// Set turn owner
		this.setTurnOwner(player);

		// Board instantiation 
		gameBoard = new Board();

		// Avatar instantiation
		playerAvatar = BasicObjectBuilders.loadAvatar(StaticConfFiles.humanAvatar, 0, player, Avatar.class);
		computerAvatar = BasicObjectBuilders.loadAvatar(StaticConfFiles.aiAvatar, 1, computer, Avatar.class);

		System.out.println("board: " + this.getBoard());
		System.out.println();
		System.out.println("human avatar owner : " + this.playerAvatar.getOwner());
		System.out.println();
		System.out.println("Computer avatar owner : " + this.computerAvatar.getOwner() );
	}


	public void playerinteractionLock() {
		System.out.println("Player Interaction locked.");
		locked = true;
	}

	public void playerinteractionUnlock() {
		System.out.println("Player Interaction unlocked.");
		locked = false; 
	}

	public boolean playerinteractionLocked() {
		if (locked) {
			System.out.println("Player Interaction is currently locked during action.");
		}
		return locked;
	}

	

	public boolean getUnitMovingFlag() {
		return unitMovingFlag;
	}

	public void setUnitMovingFlag(boolean flag) {
		unitMovingFlag = flag; 
	}



	// Cancel all selection of any Card and Unit
	public void deselectAllEntities() { 

		if(this.getBoard().getUnitSelected() != null) {
			this.getBoard().getUnitSelected().setProvoked(false);
			this.getBoard().setUnitSelected(null);

		}

		if(this.getTurnOwner().getHand().getSelectedCard() != null) {
			this.getTurnOwner().getHand().setSelectedCard(null);
		}

		tileAdjustedRangeContainer.clear(); 
	}


	//give turnCount mana to the player just in the beginning of new turn	
	public void giveMana() {  

		if(getTurnOwner() == player) {	//turncount +1 only when Human player start the new round of game
			this.turnCount +=1;
			player.addMana(this.turnCount);
			System.out.println("player 1 mana is " + player.getMana());
		}
		else {
			computer.addMana(this.turnCount);
			System.out.println("player 2 mana is " + computer.getMana());
		}  
	}

	//clear the mana when the turn ends
	public void emptyMana() {
		getTurnOwner().setMana(0);
	}


	// check if players decks are are empty 
	public boolean isDeckEmpty() {
		ArrayList<Card> turnOwnerDeck = getTurnOwner().getDeck().getCardList();
		int deckCardLeft = turnOwnerDeck.size();
		if(deckCardLeft < 1) {
			return true;
		}
		return false;
	}

	//After attacking or settling, turn the state of unit to cooldown state
	public void setMonsterCooldown(boolean value) {
		ArrayList<Monster> toCool = getBoard().friendlyUnitList(this.getTurnOwner());

		// Add avatars 
		if (this.getTurnOwner() == player) {
			toCool.add(this.getPlayerAvatar());
		}
		else {
			toCool.add(this.getComputerAvatar());
		}

		// Set cooldowns
		for(Monster m : toCool){
			if(m.getOnCooldown() != value) {
				m.toggleCooldown();	
			}
		}
	}

	//When the computer turn end, draw a card to the computer
	public void computerEnd() {

		if (isDeckEmpty()) { 
			gameOver(); 
		}

		if(!isHumanCard()) {
			getTurnOwner().getHand().drawCard(getTurnOwner().getDeck());//if it is human player getting a new card, re-display all card in hand after drawing 
			endTurnStaticChange();
		}	
	}

	public void endTurnStaticChange() {

		emptyMana(); 				// Empty mana for player who ends the turn
		UpdateState.threadSleep();
		deselectAllEntities();		// Deselect all entities
		UpdateState.threadSleep();
		setMonsterCooldown(true);	// Hard set all monsters on turn enders turn to cooldown
		UpdateState.threadSleep();
		turnChange(); 				// change the owner of the turn
		UpdateState.threadSleep();
		giveMana();
		UpdateState.threadSleep();
		setMonsterCooldown(false);
		UpdateState.threadSleep();

	}


	public boolean isHumanCard() {
		if(getTurnOwner() == player) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean checkMonsterAbilityActivation(ActivateMoment callID, Monster targetMonster) {

		boolean abilityFound = false; 

		for (Tile tile : this.getBoard().getAllTilesList()) {

			// Container for containing all executable abilities
			ArrayList<Ability> abilityContainer = new ArrayList<Ability>(2); 

			//Check if a unit is on the tile
			if (tile.getUnitOnTile() != null) {

				// Check if the unit has abilities
				if (tile.getUnitOnTile().getMonsterAbility() != null) {

					// Loop over abilities and get executing ones
					for (Ability ability : tile.getUnitOnTile().getMonsterAbility()) {

						if (ability.getActivateMoment() == callID) {
							abilityContainer.add(ability);
							abilityFound = true;
						}
					}

					for (Ability ability : abilityContainer) {

						if (ability.getActivateMoment() == ActivateMoment.construction) {
							ability.execute(targetMonster, this); 
							abilityContainer.remove(ability);		// Remove this ability to not execute twice
							System.out.println("Executing ability:" + ability);
						}
					}

					for (Ability ability : abilityContainer) {
						ability.execute(targetMonster, this);
						System.out.println("Executing ability:" + ability);
					}
				}

			}

		}
		return abilityFound; 
	}

	// Check if an Ability/external factor was used to alter unit move/attack range
	public boolean useAdjustedMonsterActRange() { 
		return !this.getTileAdjustedRangeContainer().isEmpty(); 
	}

	


	public int getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}


	public boolean isPlayerDead() {
		return playerDead;
	}

	public void setPlayerDead(boolean playerDead) {
		this.playerDead = playerDead;
	}


	public Player getPlayer() {
		return player;
	}

	public Player getComputer() {
		return computer;
	}

	public Player getTurnOwner() {
		return turnOwner;
	}

	public void setTurnOwner(Player turnOwner) {
		this.turnOwner = turnOwner;
	}

	public void turnChange() {
		if (turnOwner == player) {
			turnOwner = computer;
		}
		else {
			turnOwner = player;
		}

	}

	public Player getEnemyPlayer() {

		// Check if the turn owner is instance of human player, if so return the computer player
		if (this.getTurnOwner() == this.player) {
			return this.getComputer();
		}
		else {
			return this.getPlayer();
		}
	}

	// Potentially remove
	public Avatar getPlayerAvatar() {
		return playerAvatar;
	}

	public Avatar getComputerAvatar() {
		return computerAvatar;
	}

	// Is this necessary? 
	public void setPlayers(HumanPlayer h, ComputerPlayer c) {
		player = h;
		computer = c;
	}

	public static void gameOver() {
		playerDead = true;		
	}

	public Board getBoard() {
		return gameBoard; 
	}

	public ArrayList<Tile> getTileAdjustedRangeContainer() {
		return tileAdjustedRangeContainer; 
	}

	public void setTileAdjustedRangeContainer(ArrayList<Tile> tilesToHighlight) {
		tileAdjustedRangeContainer = tilesToHighlight;
	}



}
