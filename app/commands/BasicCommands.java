package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import play.libs.Json;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;


/**
 * This is a utility class that simply provides short-cut methods for
 * running the basic command set for the game.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class BasicCommands {

	private static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to turn java objects to Strings
	
	// An alternative class with a 'tell' implementation can be given if writing unit tests
	// and need to have a null ActorRef. This should be null during normal operation.
	public static DummyTell altTell = null;
	
	
	/**
	 * You can consider the contents of the user’s browser window a canvas that can be drawn upon. drawTile will draw 
	 * the image of a board tile on the board. This command takes as input a Tile object and a visualisation mode (an 
	 * integer) that specifies which version of the tile to render (each tile has multiple versions, e.g. normal vs. 
	 * highlighted). This command can be used multiple times to change the visualisation mode for a tile.
	 * @param out
	 * @param tile
	 * @param mode
	 * 你可以把用户的浏览器窗口的内容看作是可以被绘制的画布。 drawTile将绘制
	 * 	 * 棋盘上的一块棋子的图像。这个命令的输入是一个瓦片对象和一个可视化模式（一个
	 * 	 * 整数），指定要渲染的瓦片的版本（每个瓦片都有多个版本，例如：正常与高亮）。
	 * 	 * 每个瓦片都有多个版本，例如正常与高亮）。这个命令可以多次使用来改变一个瓦片的可视化模式。
	 */
	@SuppressWarnings({"deprecation"})
	public static void drawTile(ActorRef out, Tile tile, int mode) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawTile");
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			returnMessage.put("mode", mode);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * drawUnit will draw the sprite for a unit (a picture of that unit with its attack and health values) on the board. 
	 * This command takes as input a target Tile (a ‘square’ of the main game grid) to place the unit’s sprite upon, 
	 * and the instance of the Unit (which holds the needed information about how to draw that unit).
	 * @param out
	 * @param unit
	 * @param tile
	 * * drawUnit将在棋盘上绘制一个单位的精灵（该单位的图片及其攻击和健康值）。
	 * 	 * 这个命令需要输入一个目标瓦片（主游戏网格的一个 "方块"）来放置单位的精灵。
	 * 	 * 和单位的实例（它持有关于如何绘制该单位的必要信息）。
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void drawUnit(ActorRef out, Unit unit, Tile tile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawUnit");
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised attack value just under a unit’s sprite to a value between 0 
	 * and 20. The command takes in a unit instance. The associated values are read from the unit object.
	 * @param out
	 * @param unit
	 * @param attack
	 *  这条命令将一个单位的精灵下面的可视化攻击值改为0-20之间的数值。
	 * 	 *和20之间。该命令接收了一个单元实例。相关的数值会从单位对象中读取。
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void setUnitAttack(ActorRef out, Unit unit, int attack) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setUnitAttack");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("attack", attack);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised health value just under a unit’s sprite to a value between 0 
	 * and 20. The command takes in a unit instance. The associated values are read from the unit object.
	 * @param out
	 * @param unit
	 * @param health
	 * 这条命令将一个单位的精灵下面的可视化健康值改为0-20之间的值。
	 * 	 *和20之间。该命令接收一个单元实例。相关的值从单元对象中读取。
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void setUnitHealth(ActorRef out, Unit unit, int health) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setUnitHealth");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("health", health);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command moves a unit sprite from one tile to another. It takes in the unit’s object and the target Tile. 
	 * Note that this command will start the movement, it may take multiple seconds for the movement to complete.
	 * @param out
	 * @param unit
	 * @param tile
	 *  这个命令将一个单元精灵从一个瓦片移动到另一个瓦片。它接收该单元的对象和目标瓦片。
	 * 	 * 注意，这个命令将开始移动，可能需要多秒才能完成移动。
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void moveUnitToTile(ActorRef out, Unit unit, Tile tile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "moveUnitToTile");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command moves a unit sprite from one tile to another. It takes in the unit’s object and the target Tile. 
	 * Note that this command will start the movement, it may take multiple seconds for the movement to complete.
	 * yfirst sets whether the move should move the unit vertically first before moving horizontally
	 * @param out
	 * @param yfirst
	 * @param unit
	 * @param tile
	 *  这个命令将一个单元精灵从一个瓦片移动到另一个瓦片。它接收该单元的对象和目标瓦片。
	 * 	 * 注意，这个命令将开始移动，可能需要多秒才能完成移动。
	 * 	 * yfirst设置移动是否应该先垂直移动单元，然后再水平移动。
	 * 	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void moveUnitToTile(ActorRef out, Unit unit, Tile tile, boolean yfirst) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "moveUnitToTile");
			returnMessage.put("yfirst", yfirst);
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command makes a unit play a specified animation. It takes in the unit object which
	 * contains all of the data needed to play the animations, and a UnitAnimation that specifies
	 * which animation to switch to.
	 * @param out
	 * @param unit
	 * @param animation
	 * * 这个命令让一个单位播放指定的动画。它接收了单元对象，该对象
	 * 	 * 包含所有播放动画所需的数据，以及一个UnitAnimation，它指定了
	 * 	 * 切换到哪个动画。
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void playUnitAnimation(ActorRef out, Unit unit, UnitAnimationType animationToPlay) {
		try {
			
			unit.setAnimation(animationToPlay);
			
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "playUnitAnimation");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("animation", animationToPlay.toString());
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This will delete a unit instance from the board. It takes as input the unit object of the unit.
	 * @param out
	 * @param unit
	 * 这将从棋盘上删除一个单元实例。它接受该单元的单元对象作为输入。
	 * 	 * @param out
	 * 	 * @param unit
	 */
	@SuppressWarnings({"deprecation"})
	public static void deleteUnit(ActorRef out, Unit unit) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "deleteUnit");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised health value in the player’s information card to a value between 0 
	 * and 20. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 * * 该命令将玩家信息卡中的可视化健康值改为0到20之间的数值。
	 * 	 * 和20之间。该命令接收了一个基本玩家实例。相关的数值从基本玩家中读取
	 * 	 *对象。
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer1Health(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer1Health");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised health value in the player’s information card to a value between 0 
	 * and 20. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 *  这条命令将玩家信息卡中的可视化健康值改为0-20之间的数值。
	 * 	 *和20之间。该命令接收了一个基本玩家实例。相关的值从基本玩家中读取
	 * 	 *对象。
	 * 	 * @param out
	 * 	 * @param player
	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer2Health(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer2Health");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised mana value in the player’s information card to a value between 0 
	 * and 9. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 * * 该命令将玩家信息卡中的可视化法力值改为0-9之间的数值。
	 * 	 * 和9之间。该命令接收了一个基本玩家实例。相关的值从基本玩家中读取
	 * 	 * 对象。
	 * 	 * @param out
	 * 	 * @param player
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer1Mana(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer1Mana");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised mana value in the player’s information card to a value between 0 
	 * and 9. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer2Mana(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer2Mana");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command renders a card in the player’s hand. It takes as input a hand position (a value between 1-6), a 
	 * Card (which is an object containing basic information needed to visualise that card) and a visualisation mode 
	 * (similarly to a tile). This command can be issued multiple times to change the visualisation mode of a card.
	 * @param out
	 * @param card
	 * @param position
	 * @param mode
	 * 该命令在玩家的手牌中显示一张牌。它需要输入一个手牌位置（1-6之间的数值），一个
	 * 	 * 卡片（它是一个包含可视化卡片所需基本信息的对象）和一个可视化模式
	 * 	 *（类似于瓦片）。这个命令可以多次发出，以改变一张牌的可视化模式。
	 * 	 *
	 */
	@SuppressWarnings({"deprecation"})
	public static void drawCard(ActorRef out, Card card, int position, int mode) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawCard");
			returnMessage.put("card", mapper.readTree(mapper.writeValueAsString(card)));
			returnMessage.put("position", position);
			returnMessage.put("mode", mode);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command deletes a card in the player’s hand. It takes as input a hand position (a value between 1-6).
	 * @param out
	 * @param position
	 *  该命令删除玩家手中的一张牌。它需要输入一个手牌位置（1-6之间的数值）。
	 * 	 * @param out
	 * 	 * @param position
	 */
	public static void deleteCard(ActorRef out, int position) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "deleteCard");
			returnMessage.put("position", position);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays a specified EffectAnimation (such as an explosion) centred on a particular Tile. It takes as input an 
	 * EffectAnimation (an object with information about rendering the effect) and a target Tile.
	 * @param out
	 * @param effect
	 * @param tile
	 * 播放一个指定的效果动画（如爆炸），以一个特定的瓷砖为中心。它需要输入一个
	 * 	 * EffectAnimation（一个包含渲染效果信息的对象）和一个目标瓦片。
	 * 	 * @param out
	 * 	 * @param effect
	 * 	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static void playEffectAnimation(ActorRef out, EffectAnimation effect, Tile tile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "playEffectAnimation");
			returnMessage.put("effect", mapper.readTree(mapper.writeValueAsString(effect)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command creates a notification box next to the portrait for the player 1 which contains
	 * the specified text. It will be displayed for a number of seconds before being removed.
	 * object.
	 * @param out
	 * @param text
	 * @param displayTimeSeconds
	 */
	public static void addPlayer1Notification(ActorRef out, String text, int displayTimeSeconds) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "addPlayer1Notification");
			returnMessage.put("text", text);
			returnMessage.put("seconds", displayTimeSeconds);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays a projectile fire animation between two tiles
	 *  *在两块瓷砖之间播放射出的火焰动画
	 *
	 * @param out
	 * @param effect
	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static void playProjectileAnimation(ActorRef out, EffectAnimation effect, int mode, Tile startTile, Tile targetTile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawProjectile");
			returnMessage.put("effect", mapper.readTree(mapper.writeValueAsString(effect)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(startTile)));
			returnMessage.put("targetTile", mapper.readTree(mapper.writeValueAsString(targetTile)));
			returnMessage.put("mode", mapper.readTree(mapper.writeValueAsString(mode)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
