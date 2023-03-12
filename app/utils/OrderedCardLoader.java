package utils;

import structures.basic.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a utility class that provides methods for loading the decks for each
 * player, as the deck ordering is fixed. 
 * @author Richard
 *
 */
public class OrderedCardLoader {

	/**
	 * Returns all of the cards in the human player's deck in order
	 * @return
	 */
	//private static ArrayList<Card> cardList;// array of card objects that comprise the deck

	private static ArrayList<Card> deck1;
	private static ArrayList<Card> deck2;
//	public  OrderedCardLoader() { //constructor for deck
//		this.cardList = new ArrayList<Card>();
//	}

//	public ArrayList<Card> getDeck1() {
//
//		List<Card> cardsInDeck = new ArrayList<Card>(20);
//
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger, 0, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pureblade_enforcer, 1, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter, 2, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_silverguard_knight, 3, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald, 4, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian, 5, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion, 6, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 7, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_silverguard_knight, 8, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter, 9, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger, 10, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pureblade_enforcer, 11, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald, 12, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian, 13, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion, 14, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 15, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_truestrike, 16, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_sundrop_elixir, 17, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_truestrike, 18, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_sundrop_elixir, 19, Card.class));
//
//		// Set the associated class of the cards based on their ID
//		for (int i = 0; i < cardsInDeck.size(); i++) {
//			Card card = cardsInDeck.get(i);
//			// Set ability data to be held in card for reference from AI etc
//			if(AbilityToUnitLinkage.UnitAbility.containsKey(card.getCardname())) {
//				card.setAbilityList(AbilityToUnitLinkage.UnitAbility.get(card.getCardname()));
//			}
//			if (i <= 15) {
//				card.setAssociatedClass(Monster.class);
//			} else {
//				card.setAssociatedClass(Spell.class);
//			}
//		}
//
//		return cardsInDeck;


	public static List<Card> getPlayer1Cards() {

			List<Card> cardsInDeck = new ArrayList<Card>(20);



		String[] cardConfigNames = {// list of cards in player deck
				StaticConfFiles.c_azure_herald,
				StaticConfFiles.c_azurite_lion,
				StaticConfFiles.c_comodo_charger,
				StaticConfFiles.c_fire_spitter,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_ironcliff_guardian,
				StaticConfFiles.c_pureblade_enforcer,
				StaticConfFiles.c_silverguard_knight,
				StaticConfFiles.c_azure_herald,
				StaticConfFiles.c_azurite_lion,
				StaticConfFiles.c_comodo_charger,
				StaticConfFiles.c_fire_spitter,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_ironcliff_guardian,
				StaticConfFiles.c_pureblade_enforcer,
				StaticConfFiles.c_silverguard_knight,
				StaticConfFiles.c_sundrop_elixir,
				StaticConfFiles.c_truestrike,
				StaticConfFiles.c_sundrop_elixir,
				StaticConfFiles.c_truestrike};
		String[] unitConfigNames = {// list of units in player deck
				StaticConfFiles.u_azure_herald,
				StaticConfFiles.u_azurite_lion,
				StaticConfFiles.u_comodo_charger,
				StaticConfFiles.u_fire_spitter,
				StaticConfFiles.u_hailstone_golem,
				StaticConfFiles.u_ironcliff_guardian,
				StaticConfFiles.u_pureblade_enforcer,
				StaticConfFiles.u_silverguard_knight,
				StaticConfFiles.u_azure_herald,
				StaticConfFiles.u_azurite_lion,
				StaticConfFiles.u_comodo_charger,
				StaticConfFiles.u_fire_spitter,
				StaticConfFiles.u_hailstone_golem,
				StaticConfFiles.u_ironcliff_guardian,
				StaticConfFiles.u_pureblade_enforcer,
				StaticConfFiles.u_silverguard_knight,};


		// create two instances of each card
		for (int i = 0; i < unitConfigNames.length; i++) {
			Card card = BasicObjectBuilders.loadCard(cardConfigNames[i], unitConfigNames[i], i, Card.class);
			System.out.println("1 cardid: " + i+ " is "+card.getAssociatedClass());
			cardsInDeck.add(card);
		}
		for (int i = unitConfigNames.length; i < cardConfigNames.length; i++) {

			Card card = BasicObjectBuilders.loadCard(cardConfigNames[i], i   , Card.class);
			System.out.println("1 cardid: "+ i+ " is "+card.getAssociatedClass());
			cardsInDeck.add(card);


		}

		return cardsInDeck;


	}
	
	
	/**
	 * Returns all of the cards in the human player's deck in order
	 * @return
	 */
//	public void deck2() {
	
//		List<Card> cardsInDeck = new ArrayList<Card>(20);
//
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_rock_pulveriser, 20, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_bloodshard_golem, 21, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 22, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_windshrike, 23, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pyromancer, 24, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_serpenti, 25, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_planar_scout, 26, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 27, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_rock_pulveriser, 28, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_bloodshard_golem, 29, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 30, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_windshrike, 31, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pyromancer, 32, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_serpenti, 33, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_planar_scout, 34, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 35, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_staff_of_ykir, 36, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_entropic_decay, 37, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_staff_of_ykir, 38, Card.class));
//		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_entropic_decay, 39, Card.class));

	public static List<Card> getPlayer2Cards() {

		List<Card> cardsInDeck = new ArrayList<Card>(20);



		String[] cardConfigNames= {//list of cards in AI player deck
				StaticConfFiles.c_blaze_hound,
				StaticConfFiles.c_bloodshard_golem,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_planar_scout,
				StaticConfFiles.c_pyromancer,
				StaticConfFiles.c_windshrike,
				StaticConfFiles.c_serpenti,
				StaticConfFiles.c_rock_pulveriser,
				StaticConfFiles.c_entropic_decay,
				StaticConfFiles.c_staff_of_ykir,
				StaticConfFiles.c_blaze_hound,
				StaticConfFiles.c_bloodshard_golem,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_planar_scout,
				StaticConfFiles.c_pyromancer,
				StaticConfFiles.c_windshrike,
				StaticConfFiles.c_serpenti,
				StaticConfFiles.c_rock_pulveriser,
				StaticConfFiles.c_entropic_decay,
				StaticConfFiles.c_staff_of_ykir};
		String[] unitConfigNames= {//list of units in AI player deck
				StaticConfFiles.u_blaze_hound,
				StaticConfFiles.u_bloodshard_golem,
				StaticConfFiles.u_hailstone_golemR,
				StaticConfFiles.u_planar_scout,
				StaticConfFiles.u_pyromancer,
				StaticConfFiles.u_windshrike,
				StaticConfFiles.u_serpenti,
				StaticConfFiles.u_rock_pulveriser,
				StaticConfFiles.u_blaze_hound,
				StaticConfFiles.u_bloodshard_golem,
				StaticConfFiles.u_hailstone_golemR,
				StaticConfFiles.u_planar_scout,
				StaticConfFiles.u_pyromancer,
				StaticConfFiles.u_windshrike,
				StaticConfFiles.u_serpenti,
				StaticConfFiles.u_rock_pulveriser};
		// create two instances of each card
		for (int i = 0; i < unitConfigNames.length; i++) {
			Card card = BasicObjectBuilders.loadCard(cardConfigNames[i], unitConfigNames[i], (i+20), Card.class);
			System.out.println("2 cardid: " +(i+20) + " is "+card.getAssociatedClass());
			cardsInDeck.add(card);
		}
		for (int i = unitConfigNames.length; i < cardConfigNames.length; i++) {

			Card card = BasicObjectBuilders.loadCard(cardConfigNames[i], (i+20)  , Card.class);
			System.out.println("2 cardid: "+ (i+20)+ " is "+card.getAssociatedClass());
			cardsInDeck.add(card);


		}

		return cardsInDeck;

	}




}
