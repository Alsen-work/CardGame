package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

import java.util.List;
import java.util.Random;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 *
 *  表示浏览器中的核心游戏循环正在启动，意味着
 *  * 它已经准备好接收来自后端的命令。
 *
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor {

	public static boolean endaTurn;
	boolean endTurn = false;
	static Player humanPlayer;
	static Player aiPlayer;
	static Tile tile00,tile01,tile02,tile03,tile04,tile10,tile11,tile12,tile13,tile14,tile20,tile21,tile22,tile23,tile24;
	static Tile tile30,tile31,tile32,tile33,tile34,tile40,tile41,tile42,tile43,tile44,tile50,tile51,tile52,tile53,tile54;
	static Tile tile60,tile61,tile62,tile63,tile64,tile70,tile71,tile72,tile73,tile74,tile80,tile81,tile82,tile83,tile84;
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		// hello this is a change

		gameState.gameInitalised = true;

		gameState.something = true;

		// User 1 makes a change
		//CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
		//CheckMoveLogic.executeDemo(out);


//		initAllTile(out);//by Luo
//		initPlayer(out);//by Luo
//		drawCard(out);//by Luo//第一回合从牌堆抽取三张卡
		initBoard(out,gameState,message);//by Luo


	}

	private static void initBoard(ActorRef out, GameState gameState, JsonNode message) {
		Board gameBoard = gameState.getBoard();
		BasicCommands.addPlayer1Notification(out, "initBoard", 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Draw the board
		for (int i = 0; i<gameBoard.getBoard().length; i++) {
			for (int k = 0; k<gameBoard.getBoard()[0].length; k++) {
				BasicCommands.drawTile(out, gameBoard.getBoard()[i][k], 0);
			}

		}

	}

	// init drawTile by Luo
	public static void initAllTile(ActorRef out) {

		// addPlayer1Notification
		BasicCommands.addPlayer1Notification(out, "addPlayer1Notification", 2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		BasicCommands.addPlayer1Notification(out, "initDrawAllTile", 2);// 发出消息持续两秒
		 tile00 = BasicObjectBuilders.loadTile(0, 0);
		 tile01 = BasicObjectBuilders.loadTile(0, 1);
		 tile02 = BasicObjectBuilders.loadTile(0, 2);
		 tile03 = BasicObjectBuilders.loadTile(0, 3);
		 tile04 = BasicObjectBuilders.loadTile(0, 4);

		 tile10 = BasicObjectBuilders.loadTile(1, 0);
		 tile11 = BasicObjectBuilders.loadTile(1, 1);
		 tile12 = BasicObjectBuilders.loadTile(1, 2);
		 tile13 = BasicObjectBuilders.loadTile(1, 3);
		 tile14 = BasicObjectBuilders.loadTile(1, 4);

		 tile20 = BasicObjectBuilders.loadTile(2, 0);
		 tile21 = BasicObjectBuilders.loadTile(2, 1);
		 tile22 = BasicObjectBuilders.loadTile(2, 2);
		 tile23 = BasicObjectBuilders.loadTile(2, 3);
		 tile24 = BasicObjectBuilders.loadTile(2, 4);

		 tile30 = BasicObjectBuilders.loadTile(3, 0);
		 tile31 = BasicObjectBuilders.loadTile(3, 1);
		 tile32 = BasicObjectBuilders.loadTile(3, 2);
		 tile33 = BasicObjectBuilders.loadTile(3, 3);
		 tile34 = BasicObjectBuilders.loadTile(3, 4);

		 tile40 = BasicObjectBuilders.loadTile(4, 0);
		 tile41 = BasicObjectBuilders.loadTile(4, 1);
		 tile42 = BasicObjectBuilders.loadTile(4, 2);
		 tile43 = BasicObjectBuilders.loadTile(4, 3);
		 tile44 = BasicObjectBuilders.loadTile(4, 4);

		 tile50 = BasicObjectBuilders.loadTile(5, 0);
		 tile51 = BasicObjectBuilders.loadTile(5, 1);
		 tile52 = BasicObjectBuilders.loadTile(5, 2);
		 tile53 = BasicObjectBuilders.loadTile(5, 3);
		 tile54 = BasicObjectBuilders.loadTile(5, 4);

		 tile60 = BasicObjectBuilders.loadTile(6, 0);
		 tile61 = BasicObjectBuilders.loadTile(6, 1);
		 tile62 = BasicObjectBuilders.loadTile(6, 2);
		 tile63 = BasicObjectBuilders.loadTile(6, 3);
		 tile64 = BasicObjectBuilders.loadTile(6, 4);

		 tile70 = BasicObjectBuilders.loadTile(7, 0);
		 tile71 = BasicObjectBuilders.loadTile(7, 1);
		 tile72 = BasicObjectBuilders.loadTile(7, 2);
		 tile73 = BasicObjectBuilders.loadTile(7, 3);
		 tile74 = BasicObjectBuilders.loadTile(7, 4);

		 tile80 = BasicObjectBuilders.loadTile(8, 0);
		 tile81 = BasicObjectBuilders.loadTile(8, 1);
		 tile82 = BasicObjectBuilders.loadTile(8, 2);
		 tile83 = BasicObjectBuilders.loadTile(8, 3);
		 tile84 = BasicObjectBuilders.loadTile(8, 4);


		BasicCommands.drawTile(out, tile00, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile01, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile02, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile03, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile04, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile10, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile11, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile12, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile13, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile14, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile20, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile21, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile22, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile23, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile24, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile30, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile31, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile32, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile33, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile34, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile40, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile41, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile42, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile43, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile44, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile50, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile51, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile52, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile53, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile54, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile60, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile61, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile62, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile63, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile64, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile70, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile71, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile72, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile73, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile74, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile80, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile81, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile82, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile83, 0);//0默认，1高亮，2red modified by Luo
		BasicCommands.drawTile(out, tile84, 0);//0默认，1高亮，2red modified by Luo


		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	//init playerhealth by Luo
	public static void initPlayer(ActorRef out) {
		// setPlayer1Health
		BasicCommands.addPlayer1Notification(out, "setPlayer1Health", 1);
		humanPlayer = new Player(20, 0);
		BasicCommands.setPlayer1Health(out, humanPlayer);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// setPlayer2Health
		BasicCommands.addPlayer1Notification(out, "setPlayer2Health", 1);
		aiPlayer = new Player(20, 0);
		BasicCommands.setPlayer2Health(out, aiPlayer);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		// drawUnit humanAvatar
		//BasicCommands.addPlayer1Notification(out, "drawUnit", 2);
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, -1, Unit.class);
		unit.setPositionByTile(tile12);
		BasicCommands.drawUnit(out, unit, tile12);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		// drawUnit humanAvatar
		//BasicCommands.addPlayer1Notification(out, "drawUnit2", 2);
		Unit unit2 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, -2, Unit.class);
		unit2.setPositionByTile(tile72);
		BasicCommands.drawUnit(out, unit2, tile72);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		// setUnitAttack
		//BasicCommands.addPlayer1Notification(out, "setUnitAttack", 2);
		BasicCommands.setUnitAttack(out, unit, 2);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		// setUni2tAttack
		//BasicCommands.addPlayer1Notification(out, "setUnitAttack", 2);
		BasicCommands.setUnitAttack(out, unit2, 2);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		// setUnitHealth
		//BasicCommands.addPlayer1Notification(out, "setUnit2Health", 2);
		BasicCommands.setUnitHealth(out, unit, 20);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		// setUnit2Health
		//BasicCommands.addPlayer1Notification(out, "setUnit2Health", 2);
		BasicCommands.setUnitHealth(out, unit2, 20);
		//try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}




//		// Player Hand Test
//		BasicCommands.addPlayer1Notification(out, "Player Hand Test", 2);
//		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}


//		// drawCard [1]
//		BasicCommands.addPlayer1Notification(out, "drawCard [1u]", 2);
//		Card hailstone_golem = BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 0, Card.class);
//		BasicCommands.drawCard(out, hailstone_golem, 1, 0);
//		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
//
//		// drawCard [1] Highlight
//		BasicCommands.addPlayer1Notification(out, "drawCard [1u] Highlight", 2);
//		BasicCommands.drawCard(out, hailstone_golem, 1, 1);
//		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

	}

	//第一回合从牌堆抽取三张卡by Luo
	public static void drawCard(ActorRef out) {
		//初始化牌堆
		BasicCommands.addPlayer1Notification(out, "初始化牌堆", 2);
		List<Card>  cardsInDeck1 = OrderedCardLoader.getPlayer1Cards();


		//当点击endtrun时--未实现
			//判断排队剩余卡的数量
			if (cardsInDeck1.size() > 0) {
				//随机抽取三张
				Random rand = new Random();
				int numberOfElements = 3;

				for (int i = 0; i < numberOfElements; i++) {
					int randomIndex = rand.nextInt(cardsInDeck1.size());
					Card randomCard = cardsInDeck1.get(randomIndex);
					BasicCommands.drawCard(out, randomCard, i + 1, 0);
					cardsInDeck1.remove(randomIndex);//将一抽到的卡的索引从牌堆中删除

					System.out.println("Remaining deck size" + cardsInDeck1.size());
				}
			}


	}



}
