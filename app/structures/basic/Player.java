package structures.basic;

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
	public void setMana(int roundNumber) {
		this.mana += roundNumber +1;
		if(this.mana > 9) {
			this.mana = 9;
		}
	}
	
	
}
