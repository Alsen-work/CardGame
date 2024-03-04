package structures.basic;

import structures.basic.abilities.Ability;
import structures.basic.abilities.Unit_SummonAnywhere;

import java.util.ArrayList;


public class Card implements Comparable<Card> {

	private int 		id;		//card id
	private String 		cardname;
	private int 		manacost;	//mana cost of a card
	private MiniCard 	miniCard;
	private String 		configFile;	// Config file
	private BigCard 	bigCard;
	private ArrayList<Ability> abilityList;	// A collection of cards' abilities
	private Class<?> cardType;	// Types of cards

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
		this.cardType = Card.class;
	}





	/// Check the target for card use
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

	public boolean hasAbility(){
		boolean result= false;
		if(this.abilityList!=null) {
			result=true;
		}
		return result;
	}

	//Shows where cards can be used
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
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public String getConfigFile() {
		return this.configFile;
	}
	public ArrayList<Ability> getAbilityList() {
		return abilityList;
	}
	public void setAbilityList(ArrayList<Ability> abilityList) {
		this.abilityList = abilityList;
	}
	public Class<?> getCardType() {
		return cardType;
	}
	public void setCardType(Class<?> associatedUnitClass) {
		this.cardType = associatedUnitClass;
	}
}