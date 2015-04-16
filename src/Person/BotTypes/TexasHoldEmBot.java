package Person.BotTypes;

import Money.Pot;
import Person.Person;
import PokerRules.TexasHoldEm.TexasHoldEm;

public class TexasHoldEmBot extends Person
{
    private TexasHoldEm texasHoldEm;
    public TexasHoldEmBot(final String name, final Pot pot, TexasHoldEm texasHoldEm) {
	super(name, pot);
	this.texasHoldEm = texasHoldEm;
    }

    @Override public void turn() {
	super.turn();

    }

}
