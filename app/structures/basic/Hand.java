package structures.basic;

import java.util.ArrayList;
import java.util.Random;

public class Hand {

    private int handNum;
    private ArrayList<Card> handList;
    private Card selectCard;
    private Card randomCard;
    private int selectCarPos;


    public Hand() {
        super();
        this.handNum = 1;
        this.handList = new ArrayList<Card>();
        this.selectCard = null;
        this.selectCarPos = -1;

    }

    //第一回合从牌堆抽取三张卡by Luo
    public void giveHand(Player p,int roundNum) {
        Random rand = new Random();
        int randomIndex;
       if (roundNum == 1){//第一回合抽3张卡
           // 初始化牌堆
           System.out.println("init Hand : " + p);
           // 判断牌堆剩余卡的数量
           if (p.getDeck().size() == 20) {
            //随机抽取三张

            int numberOfElements = 3;

            for (int i = 0; i < numberOfElements; i++, handNum++) {
                randomIndex = rand.nextInt(p.getDeck().size());
                randomCard = p.getDeck().get(randomIndex);//随机抽牌
                handList.add(randomCard);//将从排队抽到的牌放入手牌队列

                p.getDeck().remove(randomIndex);//将一抽到的卡的索引从牌堆中删除
                System.out.println("Remaining " + p + " deck size :" + p.getDeck().size());
            }
           }

        }else if(roundNum > 1){
               //其余回合抽一张
               System.out.println("otherRound Hand : " + p);
               randomIndex = rand.nextInt(p.getDeck().size());
               randomCard = p.getDeck().get(randomIndex);//随机抽牌
               handList.add(randomCard);//将从排队抽到的牌放入手牌队列

               p.getDeck().remove(randomIndex);//将一抽到的卡的索引从牌堆中删除
               System.out.println("Remaining " + p + " deck size :" + p.getDeck().size());
           }
       }




    public int getHandNum() {
        return handNum;
    }

    public void setHandNum(int handNum) {
        this.handNum = handNum;
    }

    public ArrayList<Card> getHandList() {
        return handList;
    }

    public void setHandList(ArrayList<Card> handList) {
        this.handList = handList;
    }

    public Card getSelectCard() {
        return selectCard;
    }

    public void setSelectCard(Card selectCard) {
        this.selectCard = selectCard;
    }

    public int getSelectCarPos() {
        return selectCarPos;
    }

    public void setSelectCarPos(int selectCarPos) {
        this.selectCarPos = selectCarPos;
    }

    public Card getRandomCard() {
        return randomCard;
    }

    public void setRandomCard(Card randomCard) {
        this.randomCard = randomCard;
    }


}


