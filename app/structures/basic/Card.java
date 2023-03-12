package structures.basic;


import structures.basic.abilities.Ability;
import structures.basic.abilities.Unit_SummonAnywhere;

import java.util.ArrayList;

/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Card implements Comparable<Card>{
	
	int id;
	
	String cardname;
	int manacost;
	MiniCard miniCard;
	BigCard bigCard;
	private String 		configFile;
	private Class<?> associatedClass;	//Determining the type of card
	private ArrayList<Ability> abilityList;
	
	public Card() {};
	
	public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super();
		this.id = id;
		this.cardname = cardname;
		this.manacost = manacost;
		this.miniCard = miniCard;
		this.bigCard = bigCard;
		this.configFile="";
		this.abilityList=new ArrayList<Ability>();
		this.associatedClass = Card.class;
	}
	public boolean targetEnemy() {
		boolean result=false;
		for (Ability a: this.abilityList) {
			if (a.targetEnemy()==true){
				result=true;
			}
			else {
				result=false;
			}
		}
		return result;
	}
	//helper method to show where card is playable
	public boolean playableAnywhere() {
		if(hasAbility()) {
			for(Ability a: this.abilityList) {
				if(a.getClass()== Unit_SummonAnywhere.class) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasAbility(){
		if(this.abilityList!=null) {
			return true;
		}else {
			return false;
		}
	}
	@Override
	public int compareTo(Card o) {
		if(this.manacost>o.getManacost()) {
			return 1;
		}else if(this.manacost<o.getManacost()){
			return -1;
		}else {
			return 0;
		}
	}
	//special getter methods to aid with ai logic decisions getting card(if monster) health and attack
	public int getCardHP(){
		return this.getBigCard().getHealth();
	}
	public int getCardAttack() {
		return this.getBigCard().getAttack();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardname() {
		return cardname;
	}
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}
	public int getManacost() {
		return manacost;
	}
	public void setManacost(int manacost) {
		this.manacost = manacost;
	}
	public MiniCard getMiniCard() {
		return miniCard;
	}
	public void setMiniCard(MiniCard miniCard) {
		this.miniCard = miniCard;
	}
	public BigCard getBigCard() {
		return bigCard;
	}
	public void setBigCard(BigCard bigCard) {
		this.bigCard = bigCard;
	}

	public Class<?> getAssociatedClass() {
		return associatedClass;
	}
	public void setAssociatedClass(Class<?> associatedUnitClass) {
		this.associatedClass = associatedUnitClass;
	}


	public ArrayList<Ability> getAbilityList() {
		return abilityList;
	}

	public void setAbilityList(ArrayList<Ability> abilityList) {
		this.abilityList = abilityList;
	}


	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public String getConfigFile() {
		return this.configFile;
	}
}
