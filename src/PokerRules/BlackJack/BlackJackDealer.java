package PokerRules.BlackJack;

import CardGameExceptions.CardGameActionException;
import Money.Pot;
import Person.Dealer;

public class BlackJackDealer extends Dealer
{
    public BlackJackDealer(final Pot pot) {
	super(pot);
    }

    @Override public void giveStartingCards()	{
	hand.addCard(popCard());
 	hand.addHiddenCard(popCard());
    }

    @Override public int getCardSpace() {
	return 1;
    }

    @Override public void turn() {
	System.out.println("hej");
	if (hand.isAllCardsVisible()) {
	    if (hand.getSumAceOnTop() < 17) {
		try {
		    game.getActions().makeMove(BlackJackAction.HIT);
		} catch (CardGameActionException e) {
		    e.printStackTrace();
		}
	    } else {
		try {
		    game.getActions().makeMove(BlackJackAction.STAND);
		} catch (CardGameActionException e) {
		    e.printStackTrace();
		}
		game.setIsOver();
	    }
	}else if(hand.getSize() == 2)	{
	    hand.getCardByIndex(1).setVisible();
	}
    }
}
