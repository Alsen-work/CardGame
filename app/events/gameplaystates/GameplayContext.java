package events.gameplaystates;

import akka.actor.ActorRef;
import events.gameplaystates.tileplaystates.ITilePlayStates;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;


/*
 * GameplayContext holds the current state of the game and the behaviour of which varies depending on the current state
 * " The Context class doesn't implement state-specific behavior directly. Instead, Context refers to the State interface 
 *   for performing state-specific behavior (state.operation()), which makes Context independent of how state-specific behavior is implemented"
 *
 * GameplayContext持有游戏的当前状态，其行为根据当前状态的不同而变化
 * " Context类并不直接实现特定状态的行为。相反，Context引用了State接口
 * 用于执行特定状态的行为（state.operation()），这使得Context独立于特定状态行为的实现方式"
 */
public class GameplayContext {

	// Attributes
	private ITilePlayStates				currentStates;		// 当前执行的瓦片状态参考// Current Tile state reference for executing

	private Card 						loadedCard; 		// 任何处于选择模式的卡片都是由以前的动作产生的。// Any Card in select mode from a previous action
	private Class<?> 					cardClasstype; 		// 卡片的类别类型// Class type of the Card
	private Unit						loadedUnit; 		// 从以前的行动中目前处于选择模式的任何单位// Any Unit that is currently in selected mode from previous action
	private GameState					gameStateRef; 		// 游戏状态参考，以便在处理游戏控制流时使用游戏变量。// GameState reference to use game variables when dealing with gameplay control flow
	private String						tileFlag; 			// 用于详细说明当前点击的瓷砖状态的标志。// Flag for detailing the status of the current tile clicked
	private Tile						clickedTile;		// 当前被点击的瓷砖参考// Current tile clicked reference
	private boolean						combinedActive;		// 表示多个单元状态将被处理的变量。// Variable to indicate multiple Unit states will be processed
	public ActorRef 					out; 				// 前端参考			// Front end reference


	// Constructor
	public GameplayContext(GameState gameState, ActorRef out) {

		// Setting attributes 
		this.currentStates = null; 
		this.gameStateRef = gameState;
		this.out = out; 
	}
	
	
	// State method (called from within Context) 
	public void executeAndCreateUnitStates() {
		
		
		/* -----------------------------------------------------------------------------------------------
		 * Check if a (friendly/enemy) Unit has been clicked/is on the tile clicked (or if its empty)
		 * ----------------------------------------------------------------------------------------------- */

		/* -----------------------------------------------------------------------------------------------
		 * 检查是否有（友方/敌方）单位被点击/在被点击的瓦片上（或其是否为空）。
		 * ----------------------------------------------------------------------------------------------- */

		
		// Flag needed to determine what what Unit state is required (e.g. SummonMonster or CastSpell)
		if (checkUnitPresentOnTile() && (isUnitFriendly())) {
			this.setTileFlag("friendly unit");
		}
		else if (checkUnitPresentOnTile() && (!isUnitFriendly())) {
			this.setTileFlag("enemy unit");
		}
		else {
			this.setTileFlag("empty"); 
		}
		
		System.out.println(this.tileFlag);
		
		/*
		 * Combination of different user inputs to substates 
		 * 
		 *   Card selected 	+ 	Unit target 	-> Cast spell (if spell card)
		 *   None selected	+	Unit target		-> Display unit actions 
		 *   Card selected 	+ 	Empty target	-> SummonMonster (if Monster card) 
		 *   Unit selected 	+ 	Empty target 	-> Move unit
		 *   Unit selected 	+ 	Unit target		-> Attack Unit (if enemy) or Move and Attack Unit (if enemy)
		 */
		System.out.println("In GameplayContext.");

		// Execute state created from previous user input (specified in TileClicked)
		currentStates.execute(this);
	}
	
	
	
	
	// Getters and setters
	public ITilePlayStates getCurrentStates() {
		return currentStates;
	}

	public void addCurrentState(ITilePlayStates state) {
		this.currentStates = state;
	}
	
	public Card getLoadedCard() {
		return loadedCard;
	}

	public void setLoadedCard(Card loadedCard) {
		this.loadedCard = loadedCard;
	}

	public GameState getGameStateRef() {
		return gameStateRef;
	}

	public void setGameStateRef(GameState gameStateRef) {
		this.gameStateRef = gameStateRef;
	}

	public Class<?> getCardClasstype() {
		return cardClasstype;
	}

	public void setCardClasstype(Class<?> classtype) {
		this.cardClasstype = classtype;
	}

	public Unit getLoadedUnit() {
		return loadedUnit;
	}

	public void setLoadedUnit(Unit loadedUnit) {
		this.loadedUnit = loadedUnit;
	}

	public String getTileFlag() {
		return tileFlag;
	}

	public void setTileFlag(String tileFlag) {
		this.tileFlag = tileFlag;
	}
	
	public Tile getClickedTile() {
		return clickedTile; 
	}
	
	public void setClickedTile(Tile newClicked) {
		this.clickedTile = newClicked; 
	}
	public boolean getCombinedActive() {
		return combinedActive;
	}
	
	public void setCombinedActive(boolean c) {
		combinedActive = c;
	}
	
	/* Helper methods */
	private boolean checkUnitPresentOnTile() {	
		return (clickedTile.getUnitOnTile() != null);
	}
//
//	public void deselectAllAfterActionPerformed() {
//
//		gameStateRef.deselectAllEntities();
//		this.loadedUnit = null;
//		this.loadedCard = null;
//
//	}
	
	
	private boolean isUnitFriendly() {
		return clickedTile.getUnitOnTile().getOwner() == gameStateRef.getRoundPlayer();
	}
	
	// Debug
	public void debugPrint() {
		
		// TileFlag
		System.out.println("TileFlag: " + tileFlag); 
		
		// Card
		if (loadedCard != null) {
			System.out.println("Card info:\n" + loadedCard.getCardname() + "\nHP: " + loadedCard.getBigCard().getHealth() + "\nAttack: " + loadedCard.getBigCard().getAttack());
			System.out.println("Card type: " + cardClasstype); 
		}
		
		// Add unit print
		
		// Tile print
		System.out.println("Tile (x,y) : (" + clickedTile.getTilex() + "," + clickedTile.getTiley() + ")");

	}
	
}
