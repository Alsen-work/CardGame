package structures.basic;

import java.util.ArrayList;

public class Hand {

	private int hundNum;	// Number of hands
	private ArrayList<Card> handList;
	private Card selectedCard;	// Selected hand
	private int selectedCardPos;	// The positon of the selected hand

	public Hand() {
		super();
		this.hundNum = 1;
		this.handList = new ArrayList<Card>() ;
		this.selectedCard=null;
		this.selectedCardPos =-1;
	}

	public void initHand(Deck deck) {
		
		ArrayList<Card> drawDeck= deck.getCardList();

		Card card1= drawDeck.get(0);
		Card card2= drawDeck.get(1);
		Card card3= drawDeck.get(2);


		handList.add(card1);
		handList.add(card2);
		handList.add(card3);

		deck.delCard(0);
		deck.delCard(1);
		deck.delCard(2);

		setHundNum(3);
	}


	/**Hand rules.
	 * Initially draw three cards, end of turn draw one card, cap 6, discard if over cap, lose if no cards in pile
	 *If there are no cards in the pile, you lose.
	 * Hand logic.
	 * Determine if deck.size is 0
	 * No -- get randomIndex, random number corresponds to deck list sorting number  -> get corresponding card randomCard ->
	 * Determine if the hand is over the limit of 6
	 * Yes - add to hand queue handList -> remove card index from pile (discard) -> determine if it is a human player (endTurnClick.java())
	 * Yes - front end shows the hand
	 * No - no hand is shown on the front end
	 * No - remove the card index from the pile
	 * Yes - game over

	 */

	public void drawCard( Deck deck) {
		

		hundNum = getHundNum();
		
		//Create a temporary deck and find the top card
		ArrayList<Card> drawDeck= deck.getCardList();
		Card drawn= drawDeck.get(0);
		
		//Check that the hand is at its maximum
		if (hundNum <6) {
			handList.add(drawn);
			// Increases the number of cards and removes them from the deck.
			deck.delCard(0);
			hundNum++;
			setHundNum(hundNum);
		}
		else {
			//When your hand is full, discard the new card you have drawn
			deck.delCard(0);
		}

	}

	public Card getCardFromHand(int i) {
		return getHandList().get(i);
	}

	public void removeCard(int i) {
		if (i>=0) {
			handList.remove(i);
			setHundNum(hundNum --);
//			System.out.println("remove handList position is " + i );
		}
	}

	public int getHundNum() {
		return hundNum;
	}
	public void setHundNum(int hundNum) {
		this.hundNum = hundNum;
	}
	public ArrayList<Card> getHandList() {
		return handList;
	}
	public void setHandList(ArrayList<Card> hand) {
		this.handList = hand;
	}
	public Card getSelectedCard() {
		return selectedCard;
	}
	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}
	public int getSelectedCardPos() {
		return selectedCardPos;
	}
	public void setSelectedCardPos(int selectedCardPos) {
		this.selectedCardPos = selectedCardPos;
	}

}