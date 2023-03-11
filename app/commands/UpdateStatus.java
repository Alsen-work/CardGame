package commands;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;

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
            BasicCommands.deleteCard(out, i);
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

    public static void threadSleep() {
        try {Thread.sleep(threadSleepTime);} catch (InterruptedException e) {e.printStackTrace();}
    }

}
