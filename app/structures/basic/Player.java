package structures.basic;

import java.util.List;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	int health;
	int mana;
	protected Hand hand;
	private List<Card> deck;


	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;

	}
	public int getMana() {
		return mana;
	}


	// Add Mana and make maximum value by Luo
	//addMana =roundNumber+1
	public void addMana(int addMana) {
		int newMana = mana + addMana;
		if(newMana > 9) {
			this.mana = 9;
		}	else {
			this.mana = newMana;
		}
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand h) {
		this.hand = h;
	}

	public List<Card> getDeck() {
		return deck;
	}
	public void setDeck(List<Card> deck) {
		this.deck = deck;
	}

	public boolean enoughPay(int manacost) {
		if (getMana() - manacost >= 0) {
			return true;
		} else {
			return false;
		}
	}

	//delete Mana , when use cards
	public void loseMana(int loseMana) {
		this.mana = mana - loseMana;
	}

}
