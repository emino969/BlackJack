package PokerRules.TexasHoldEm;

import Money.Pot;
import Person.Dealer;
import Person.Person;

public class TexasDealer extends Dealer
{
    public TexasDealer(final Pot pot) {
	super(pot);
    }

    @Override public int getCardSpace() {
	return 3;
    }

    @Override public void turn() {
	game.getActions().makeMove("Call");
    }

    @Override public void giveStartingCards() {
	for (Person person : game.getOnlyPlayers()) {
	    giveNCardsToPlayer(person, 2, true);
	}
	hand.addHiddenCard(popCard());

	hand.addCard(popCard());
	hand.addCard(popCard());
	hand.addCard(popCard());
    }

}
