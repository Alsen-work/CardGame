package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import structures.basic.abilities.*;

public class Spell extends Card {

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file

	private String spellName;
	private Ability spellAbility; 						// Spell uses an ability


	public Spell(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {

		super(id, cardname, manacost, miniCard, bigCard); 
	}

	public Spell() {
		super(); 
	}


	public Ability getAbility() {
		return spellAbility; 
	}
	
	public void setAbility(String name, Ability ability) {
		this.spellName 			= name; 
		this.spellAbility 		= ability;
	}
}
