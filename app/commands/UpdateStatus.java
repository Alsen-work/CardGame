package commands;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.*;

import java.util.ArrayList;
import java.util.List;

public class UpdateStatus  {

    private static final int threadSleepTime = 50;
    // Update player health
    public static void updatePlayerHealth(ActorRef out, GameState gameState) {
        System.out.println("updatePlayerHealth()");
        BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
        BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
    }

    // Update player stats
    public static void updatePlayerMana(ActorRef out, GameState gameState) {
        BasicCommands.addPlayer1Notification(out, "Update mana of player 1", 1);
        BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
        threadSleep();
        BasicCommands.addPlayer1Notification(out, "Update mana of player 2", 1);
        BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
        threadSleep();
    }
    // update/redraw Card in hand
    public static void updateHandCards(ActorRef out, GameState gameState, int preHandNum, ArrayList<Card> handCardList) {
        System.out.println("updateHandCards()");
        //Delete first and redraw the hand
        for (int i = 0; i < preHandNum; i++) {
            BasicCommands.deleteCard(out, (i+1));
            threadSleep();
        }

        int i = 0;
        // get handList from Player
        for(Card card : handCardList) {
            BasicCommands.drawCard(out, card, (i+1), 0);
            // BasicCommands.addPlayer1Notification(out, "Draw hand " + (i+1), 1);
            // try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
            i++;
        }
        threadSleep();
    }

    //update Board
    public static void updateBoard(ActorRef out, GameState gameState) {
        updateTiles(out, gameState.getBoard().getAllTilesList(), 0);
    }

    public static void updateTiles(ActorRef out, ArrayList<Tile> tileList, int tileMode) {

        int bufferSize =15;//缓冲区大小
        int batchSize = (tileList.size() + bufferSize - 1) / bufferSize; //计算需要多少个批次（确保所有的瓷砖都被绘制）
        for (int bNum = 0; bNum < batchSize; bNum++) {
            int start = bNum * bufferSize; // 计算起始位置
            int end = Math.min(start + bufferSize, tileList.size()); // 计算结束位置
            List<Tile> batch = tileList.subList(start, end); // 取出当前批次的瓷砖
            for (Tile tile : batch) {
                BasicCommands.drawTile(out, tile, tileMode);
            }
            threadSleep();
        }
        System.out.println("updateTiles()");

    }
    // Draw a unit with stats
    // 绘制一个带有统计信息的单位
    public static void drawUnitWithStats(ActorRef out, Unit unit, Tile onTile) {

        // Draw the unit on the tiles
        // 在瓦片上画出这个单位

        BasicCommands.drawUnit(out, unit, onTile);
        threadSleep();

        // Set animation to idle
        // 设置动画为空闲状态
        BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.idle);
     threadSleep();

        // If unit is a monster or avatar
        // 如果单元是一个怪物或化身

        if (unit.getClass() == Monster.class || unit.getClass() == Avatar.class) {

            // Cast reference to Monster (Avatar extends Monster so works for this class too)
            //对怪物进行引用（阿凡达扩展了怪物，所以也适用于这个类）。

            Monster mUnit = (Monster) unit;

            // Set Unit statistics
            // 设置单元统计

            BasicCommands.setUnitHealth(out, mUnit, mUnit.getHP());
            threadSleep();
            BasicCommands.setUnitAttack(out, mUnit, mUnit.getAttackValue());
            threadSleep();
        }
    }


    // Redraw all Unit stats general command
    // 重绘所有单位统计资料的一般命令

    public static void redrawAllUnitStats(ActorRef out, GameState gameState) {

        System.out.println("In redrawAllUnitStats");

        // Loop over all friendly and enemy tiles and update
        //在所有友方和敌方的瓷砖上循环，并更新

        for (Tile t : gameState.getBoard().friendlyTile(gameState.getPlayer1())) {

            // Redraw stats
            BasicCommands.setUnitAttack(out, t.getUnitOnTile(), t.getUnitOnTile().getAttackValue());
            threadSleep();
            BasicCommands.setUnitHealth(out, t.getUnitOnTile(), t.getUnitOnTile().getHP());
            threadSleep();
        }

        // Loop over enemies
        // 在敌人身上进行循环

        for (Tile t : gameState.getBoard().enemyTile(gameState.getPlayer1())) {

            // Redraw stats
            BasicCommands.setUnitAttack(out, t.getUnitOnTile(), t.getUnitOnTile().getAttackValue());
            threadSleep();
            BasicCommands.setUnitHealth(out, t.getUnitOnTile(), t.getUnitOnTile().getHP());
            threadSleep();
        }

        // Avatars
        BasicCommands.setUnitAttack(out, gameState.getHumanAvatar(), gameState.getHumanAvatar().getAttackValue());
        threadSleep();
        BasicCommands.setUnitHealth(out, gameState.getHumanAvatar(), gameState.getHumanAvatar().getHP());
        threadSleep();

        BasicCommands.setUnitAttack(out, gameState.getAiAvatar(), gameState.getAiAvatar().getAttackValue());
        threadSleep();
        BasicCommands.setUnitHealth(out, gameState.getAiAvatar(), gameState.getAiAvatar().getHP());
        threadSleep();
    }


    // Reset tiles covering a given unit's range
    // 重置覆盖给定单位范围的瓦片

    public static void drawUnitDeselect(ActorRef out, GameState gameState, Unit unit) {
        if(unit.getClass() == Monster.class || unit.getClass() == Avatar.class) {

            // Cast for Monster methods/values
            // 对怪物方法/值的投射

            Monster mUnit = (Monster) unit;

            // Update Monster's occupied tile
            //更新怪物的被占瓦片

            Tile location = gameState.getBoard().getTile(mUnit.getPosition().getTilex(), mUnit.getPosition().getTiley());
            BasicCommands.drawTile(out, location, 0);

            // Get Monster range
            // 获得怪物范围

            ArrayList <Tile> actRange = gameState.getBoard().unitAttackableTiles(mUnit.getPosition().getTilex(), mUnit.getPosition().getTiley(), mUnit.getAttackRange(), mUnit.getMovesLeft());
            actRange.addAll(gameState.getBoard().unitMovableTiles(mUnit.getPosition().getTilex(), mUnit.getPosition().getTiley(), mUnit.getMovesLeft()));

            // Dehighlight tiles
            // 去掉瓷砖的高光部分

            updateTiles(out, actRange, 0);
        }
    }

    public static void threadSleep() {
        try {Thread.sleep(threadSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
    }

}
