package structures.basic;

import java.util.ArrayList;
import java.util.Random;

public class Hand {

    private int handNum;//手牌数（可用handList.size（）代替？）
    private ArrayList<Card> handList;
    private Card selectCard;
    private Card randomCard;
    //是否选中
    boolean isCardSelected ;
    private int selectCardPos;


    public Hand() {

        this.handNum = 0;
        this.handList = new ArrayList<Card>();
        this.selectCard = null;
        this.selectCardPos = -1;

    }


    /**手牌规则：
     * 初始摸三张，回合结束摸一张，上限6，超出上限丢弃，若牌堆没有卡则输
     *
     * 手牌逻辑：
     * 判断deck.size是否为0
     * 否——得到随机数randomIndex,随机数与deck列表排序数对应（索引） —> 得到对应卡牌randomCard ->
     *      判断手牌是否超出上限6
     *      是——加入手牌队列handList -> 从牌堆删除卡牌索引 ->判断是否是人类玩家(endTurnClick.java())
     *          是——前端显示手牌(endTurnClick.java())
     *          否——前端不显示手牌(endTurnClick.java())
     *      否——从牌堆删除卡牌索引
     * 是——游戏结束

     */
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
                System.out.println("random card id: " +randomCard.getId());

                //手牌上限为6
                if(handNum < 6 && handNum > -1){
                    handList.add(randomCard);//将从牌堆抽到的牌放入手牌队列
                    this.handNum++;//手牌数加一
                    System.out.println("HandNum is " + this.handNum + " & HandList is "+handList.size());
                    p.getDeck().remove(randomIndex);//将一抽到的卡的索引从牌堆中删除
                    System.out.println("Remaining deck size :" + p.getDeck().size()+ " "  + p );
                    System.out.println();

                }else {
                    //When your hand is full, discard the new card you have drawn
                    p.getDeck().remove(randomIndex);//removes card from deck
                    System.out.println("Current hand reaches maximum!! is" + this.handNum);
                    System.out.println("discard card id: " +randomCard.getId());
                    System.out.println("Remaining deck size :" + p.getDeck().size()+ " "  + p );
                }
                }
            }else { //牌堆为0
            System.out.println("Gameover!! " + p +" loses");
        }
            System.out.println();
    }
    //用于clickedCard表示点击的卡牌
    public Card getCardFromHand(int pos) {
        return getHandList().get(pos);
    }

    public void removeCard(int i) {
        if (i>=0) {
            handList.remove(i);
            setHandNum(handNum--);
            System.out.println("remove card position is " + i );
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

    public int getSelectCardPos() {
        return selectCardPos;
    }

    public void setSelectCardPos(int selectCardPos) {
        this.selectCardPos = selectCardPos;

    }

    public Card getRandomCard() {
        return randomCard;
    }

    public void setRandomCard(Card randomCard) {
        this.randomCard = randomCard;
    }

    public boolean isCardSelected() {
        return isCardSelected;
    }

    public void setCardSelected(boolean cardSelected) {
          this.isCardSelected = cardSelected;
    }





}


