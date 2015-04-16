package PokerRules.TexasHoldEm;

import CardGameExceptions.CardGameActionException;
import Money.Pot;
import Person.Dealer;
import Person.Person;
import PokerRules.BlackJack.BlackJackAction;

public class TexasDealer extends Dealer
{
    public TexasDealer(final Pot pot) {
	super(pot);
    }

    @Override public int getCardSpace() {
	return 3;
    }

    @Override public void turn() {
	try {
	    game.getActions().makeMove(BlackJackAction.HIT);
	} catch (CardGameActionException e) {
	    e.printStackTrace();
	}
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
