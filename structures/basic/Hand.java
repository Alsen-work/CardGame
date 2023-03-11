package structures.basic;

import java.util.ArrayList;
import java.util.Random;

public class Hand {

    private int handNum;//手牌数（可用handList.size（）代替？）
    private ArrayList<Card> handList;
    private Card selectCard;
    private Card randomCard;
    private int selectCarPos;


    public Hand() {

        this.handNum = 0;
        this.handList = new ArrayList<Card>();
        this.selectCard = null;
        this.selectCarPos = -1;

    }

    //第一回合从牌堆抽取三张卡by Luo
    public void giveHand(Player p,int roundNum) {
        Random rand = new Random();
        int randomIndex;
        int numberOfElements;//抽牌数量（第一回合为3，其余为1）

        if (p.getDeck().size() > 0) {
            System.out.println("init Hand : " + p);
            if (roundNum == 1) {
                numberOfElements = 3;//第一回合抽3张卡
            }else {
                numberOfElements = 1;//其余回合抽1张
            }
            for (int i = 0; i < numberOfElements; i++) {
                randomIndex = rand.nextInt(p.getDeck().size());
                randomCard = p.getDeck().get(randomIndex);//随机抽牌

                //手牌上限不超过6
                if(handNum < 6 && handNum > -1){
                    handList.add(randomCard);//将从牌堆抽到的牌放入手牌队列
                    this.handNum++;//手牌数加一
                    System.out.println("HandNum is " + this.handNum + " & HandList is "+handList.size());
                    p.getDeck().remove(randomIndex);//将一抽到的卡的索引从牌堆中删除
                    System.out.println("Remaining deck size :" + p.getDeck().size()+ " "  + p );
                    System.out.println();

                }else {
                    System.out.println("Current hand reaches maximum!! is" + this.handNum);
                }

            }
            System.out.println();
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


