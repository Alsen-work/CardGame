package structures.basic;

import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.Collections;

public class Deck{

	private ArrayList<Card> cardList;

	public Deck() { //constructor for deck
		this.cardList = new ArrayList<Card>();
	}

	public void deck1() {

		Card card;

		int start=2;


		String[] cardConfigNames = {
				StaticConfFiles.c_azure_herald,
				StaticConfFiles.c_azurite_lion,
				StaticConfFiles.c_comodo_charger,
				StaticConfFiles.c_fire_spitter,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_ironcliff_guardian,
				StaticConfFiles.c_pureblade_enforcer,
				StaticConfFiles.c_silverguard_knight,
				StaticConfFiles.c_sundrop_elixir,
				StaticConfFiles.c_truestrike};

		String[] unitConfigNames = {
				StaticConfFiles.u_azure_herald,
				StaticConfFiles.u_azurite_lion,
				StaticConfFiles.u_comodo_charger,
				StaticConfFiles.u_fire_spitter,
				StaticConfFiles.u_hailstone_golem,
				StaticConfFiles.u_ironcliff_guardian,
				StaticConfFiles.u_pureblade_enforcer,
				StaticConfFiles.u_silverguard_knight};

		// Loop through the card config names and create two instances of each card
		for (int i = 0; i < cardConfigNames.length; i++) {
			card = BasicObjectBuilders.loadCard(cardConfigNames[i], unitConfigNames[i], i + start, Card.class);
			cardList.add(card);
			card = BasicObjectBuilders.loadCard(cardConfigNames[i], unitConfigNames[i], i + unitConfigNames.length + start, Card.class);
			cardList.add(card);
		}
	}

	public void deck2() {

		Card card;

		int start=2;

		String[] cardConfigNames= {
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

		String[] unitConfigNames= {
				StaticConfFiles.u_blaze_hound,
				StaticConfFiles.u_bloodshard_golem,
				StaticConfFiles.u_hailstone_golemR,
				StaticConfFiles.u_planar_scout,
				StaticConfFiles.u_pyromancer,
				StaticConfFiles.u_windshrike,
				StaticConfFiles.u_serpenti,
				StaticConfFiles.u_rock_pulveriser};

		// Loop through the card config names and create two instances of each card
		for (int i = 0; i < cardConfigNames.length; i++) {
			card = BasicObjectBuilders.loadCard(cardConfigNames[i], unitConfigNames[i % unitConfigNames.length], i + cardConfigNames.length + start, Card.class);
			cardList.add(card);
			card = BasicObjectBuilders.loadCard(cardConfigNames[i], unitConfigNames[i % unitConfigNames.length], i + (2 * cardConfigNames.length) + start, Card.class);
			cardList.add(card);
		}

	}

	public void delCard(int i){
		cardList.remove(i);
	}

	public void shuffleDeck() {
		Collections.shuffle(cardList);
	}

	public void setCardList(ArrayList<Card> deck) {
		this.cardList = deck;
	}

	public ArrayList<Card> getCardList() {
		return cardList;
	}
}
