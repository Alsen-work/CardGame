package utils;

import java.io.File;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import structures.basic.Avatar;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.HumanPlayer;
import structures.basic.Monster;
import structures.basic.Player;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.abilities.Unit_Ranged;
import structures.basic.abilities.Ability;
import structures.basic.abilities.AbilityToUnitLinkage;


public class BasicObjectBuilders {

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file

	
	// ObjectBuilder for Monster Unit cards; mapper uses the cardConfig file to make the Card,
	// and the resulting Card object stores the unitConfig file for later use in summoning
	public static Card loadCard(String cardConfigFile, String unitConfigFile, int id, Class<? extends Card> classtype) {
		try {
			Card card = mapper.readValue(new File(cardConfigFile), classtype);
			card.setId(id);
			card.setConfigFile(unitConfigFile);
			
			// Set ability data to be held in card for reference from AI etc
			if(AbilityToUnitLinkage.UnitAbility.containsKey(card.getCardname())) {
				card.setAbilityList(AbilityToUnitLinkage.UnitAbility.get(card.getCardname()));
			}
			
			// Set associated class type -- Monster only for this Builder
			card.setCardType(Monster.class);

			return card;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	public static Card loadCard(String cardConfigFile, int id, Class<? extends Card> classtype) {
		try {
			Card card = mapper.readValue(new File(cardConfigFile), classtype);
			card.setId(id);
			card.setConfigFile(cardConfigFile);

			if(AbilityToUnitLinkage.UnitAbility.containsKey(card.getCardname())) {
				card.setAbilityList(AbilityToUnitLinkage.UnitAbility.get(card.getCardname()));
			}

			card.setCardType(Spell.class);
			
			return card;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	public static EffectAnimation loadEffect(String configurationFile) {
		try {
			EffectAnimation effect = mapper.readValue(new File(configurationFile), EffectAnimation.class);
			return effect;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	public static Unit loadUnit(String configFile, int id,  Class<? extends Unit> classType) {
		
		try {
			Unit unit = mapper.readValue(new File(configFile), classType);
			unit.setId(id);
			return unit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Monster loadMonsterUnit(String u_configFile, Card statsRef, Player p, Class<? extends Monster> classType) {

		try {
			System.out.println("configFile name in objectbuilder is: "+ u_configFile);
			Monster mUnit = mapper.readValue(new File(u_configFile), classType);
			
			// Set monster attributes from reference Card info
			mUnit.setId(statsRef.getId());
			mUnit.setName(statsRef.getCardname());				
			mUnit.setHP(statsRef.getBigCard().getHealth());
			mUnit.setMaxHP(statsRef.getBigCard().getHealth());
			mUnit.setAttackValue(statsRef.getBigCard().getAttack());

			// Set Player owner
			mUnit.setOwner(p);
			
			System.out.println("mUnit has name " + mUnit.getName());
			System.out.println("mUnit has ID " + mUnit.getId());
			
			// Ability setting
			if(mUnit.getMonsterAbility() == null) {	mUnit.setAbility(new ArrayList <Ability> ());	}
			mUnit.setAbility(statsRef.getAbilityList());
			
			// Check for abilities requiring EffectAnimation to be stored
			if(mUnit.hasAbility()) {
				for(Ability a : mUnit.getMonsterAbility()) {
					if(a.getClass() == Unit_Ranged.class) {
						mUnit.setAbAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles));
					}
				}
			}

			return mUnit; 
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
		return null;
	}
	
	// Adjusted Avatar ObjectBuilder to set ID/Name/Owner immediately after constructor/during construction
	public static Avatar loadAvatar(String configFile, int id, Player p, Class<? extends Avatar> classType) {
		
		try {
			Avatar unit = mapper.readValue(new File(configFile), classType);
			unit.setId(id);
			unit.setOwner(p);

			if(p instanceof HumanPlayer) {
				unit.setName("Human Avatar");
			} else {
				unit.setName("Bob");	// AI Avatar
				unit.toggleCooldown();
			}
			
			return unit;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
		
	}

	/**
	 * Generates a tile object with x and y indices
	 * @param x
	 * @param y
	 * @return
	 */
	public static Tile loadTile(int x, int y) {
		int gridmargin = 5;
		int gridTopLeftx = 410;
		int gridTopLefty = 280;
		
		Tile tile = Tile.constructTile(StaticConfFiles.tileConf);
		tile.setXpos((tile.getWidth()*x)+(gridmargin*x)+gridTopLeftx);
		tile.setYpos((tile.getHeight()*y)+(gridmargin*y)+gridTopLefty);
		tile.setTilex(x);
		tile.setTiley(y);
		
		return tile;
		
	}
	
}
