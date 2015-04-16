package Person;

import Money.Pot;
import PokerRules.AbstractCardGameAction;
import PokerRules.Game;

import java.util.Random;

public class Bot extends Person
{
    public Bot(String name, Pot pot, Game game)	{
	super(name, pot);
    }

    @Override public void turn()	{
	AbstractCardGameAction aca = game.getActions();
	Random rand = new Random();
	String nextMove = aca.getOptions(this).get(rand.nextInt(aca.getOptions(this).size()));
	aca.makeMove(nextMove);
    }

    private int betAmount() {
        return 100;
    }
}
