package Person;

import CardGameExceptions.CardGameActionException;
import Money.Pot;
import PokerRules.AbstractCardGameAction;
import PokerRules.CardGameMove;
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
	CardGameMove nextMove = aca.getOptions(this).get(rand.nextInt(aca.getOptions(this).size()));
        try {
            aca.makeMove(nextMove);
        } catch (CardGameActionException e) {
            e.printStackTrace();
        }
    }

    private int betAmount() {
        return 100;
    }
}
