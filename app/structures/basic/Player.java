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
	public void setMana(int addMana) {
		this.mana = addMana;
		int newMana = mana + addMana;

		if(newMana > 9) {
			this.mana = 9;
		}	else {
			this.mana = newMana;
		}
	}
	
	
}
