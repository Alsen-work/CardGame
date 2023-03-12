package structures.basic.abilities;

import structures.basic.Avatar;
import structures.basic.Monster;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds the information about the relationship between Units (Monsters) and Spells and what abilities they possess. 
 * Containing data here as don't want to edit the JSON files. 
 */

public class AbilityToUnitLinkage {

	// HashMap contains a String for a key (Unit name, list of abilities the unit has) 
	public static HashMap<String, ArrayList<Ability>> UnitAbility = new HashMap<String, ArrayList<Ability>>(); 

	
	// Initialise the linkage data between Unit names and their abilities 
	public static void initialiseUnitAbilityLinkageData() {
		

		/* Initialising abilities with String name keys*/
		// Can maybe grab this info from a file instead if don't want to hard code it in. 
		
		// For Abilities constructor (execution targets enemies, Class type of target, EffectAnimation)

		UnitAbility.put("Truestrike", 			constructArrayListAbility(	new Spell_TrueStrike(true,null, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation)))));
		UnitAbility.put("Sundrop Elixir", 		constructArrayListAbility(	new Spell_SundropElixir(false, Monster.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff)))));
		UnitAbility.put("Staff of Y'Kir'", 		constructArrayListAbility(	new Spell_StaffOfYkir(false, Avatar.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff)))));
		UnitAbility.put("Entropic Decay", 		constructArrayListAbility(	new Spell_EntropicDecay(true, Monster.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom)))));
		System.out.println("Linked spells");

		UnitAbility.put("Comodo Charger", 		constructArrayListAbility());
		UnitAbility.put("Hailstone Golem", 		constructArrayListAbility());
		UnitAbility.put("Pureblade Enforcer", 	constructArrayListAbility(	new Unit_BuffWhenEnemySpellCast(false, Monster.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff)))));
		UnitAbility.put("Azure Herald", 		constructArrayListAbility(	new Unit_HealAvatarOnSummon(false, Avatar.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff)))));
		UnitAbility.put("Silverguard Knight", 	constructArrayListAbility((Ability) new Unit_Provoked(true, Monster.class, null),
																			new Unit_BuffAttackWhenAvatarDealDamage(false, Monster.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff)))));
		UnitAbility.put("Azurite Lion", 		constructArrayListAbility(	new Unit_DoubleAttack(false, Monster.class, null)));
		UnitAbility.put("Fire Spitter", 		constructArrayListAbility(	new Unit_Ranged(false, Monster.class, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles)))));
		UnitAbility.put("Ironcliff Guardian", 	constructArrayListAbility(	new Unit_SummonAnywhere(false, null, null),
																			new Unit_Provoked(true, Monster.class, null)));
		/***	Deck 2		***/
		UnitAbility.put("Planar Scout", 		constructArrayListAbility(	new Unit_SummonAnywhere(false, null, null)));
		UnitAbility.put("Rock Pulveriser",		constructArrayListAbility(	new Unit_Provoked(true, Monster.class, null)));
		UnitAbility.put("Pyromancer", 			constructArrayListAbility(	new Unit_Ranged(false, null, (BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles)))));
		UnitAbility.put("Bloodshard Golem", 	constructArrayListAbility());
		UnitAbility.put("Blaze Hound", 			constructArrayListAbility(	new Unit_DrawCardOnSummon(false, null, null)));
		UnitAbility.put("WindShrike", 			constructArrayListAbility(	new Unit_Flying(false, null, null),
																			new Unit_DrawCardOnDeath(false, null, null)));
		UnitAbility.put("Serpenti", 			constructArrayListAbility(	new Unit_DoubleAttack(false, null, null)));
	
	
	}
	private static ArrayList<Ability> constructArrayListAbility(Ability ... abs){
		ArrayList<Ability> abilityContainer = new ArrayList<Ability>();
		for(Ability a : abs) {
			abilityContainer.add(a);
		}
		return abilityContainer; 
	}	
}
