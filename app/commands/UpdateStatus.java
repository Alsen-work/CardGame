package commands;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;

public class UpdateStatus  {

    // Update player stats
    public static void updatePlayerMana(ActorRef out, GameState gameState) {
        BasicCommands.addPlayer1Notification(out, "Update mana of player 1", 2);
        BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
        try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
        BasicCommands.addPlayer1Notification(out, "Update mana of player 2", 2);
        BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
        try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

    }
    // drawCard in hand
    public static void drawCardsInHand(ActorRef out, GameState gameState) {

        int i = 0;
        for(Card card : gameState.getRoundPlayer().getHand().getHandList()) { // get list of cards from Hand from Player
            BasicCommands.drawCard(out, card, i+1 , 0);
            BasicCommands.addPlayer1Notification(out, "Draw hand " + (i+1), 2);
            try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
            i++;

        }


    }
}
