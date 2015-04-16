package Person;

import Money.Pot;
import PokerRules.AbstractCardGameAction;
import GameListeners.GameListener;

public class Player extends Person
{
    public String nextMove;
    private AbstractCardGameAction moves = null;
    private GameListener gl = null;

    public Player(String name, Pot pot) {
	super(name, pot);
	this.nextMove = null;
    }

    public void turn()	{
	/** Välj ett drag */
    }


    public AbstractCardGameAction getOptions()	{
	return moves;
    }

    public void setNextMove(String name)	{
	this.nextMove = name;
    }

    public String getNextMove()	{
	return nextMove;
    }

    public boolean isMoveDecided()	{
	return nextMove != null;
    }

}
