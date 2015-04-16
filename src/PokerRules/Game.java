package PokerRules;
import GameListeners.GameListener;
import Person.Person;
import Person.PersonState;
import Table.Table;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Game  extends Table
{
    private Table table;
    protected Person currentPlayer;
    public Timer clockTimer;
    static private List<GameListener> ListenerArray;
    private AbstractCardGameAction actions = null; //Get's assigned in subclass
    protected static final int DELAY = 1000; //8 sekunder
    protected boolean isOverState;
    protected int currentPlayerIndex;

    public Game()	{
	this.currentPlayer = null; //Get's assign in the subclass
	this.currentPlayerIndex = -1; //Get's assigned and is very important
	this.ListenerArray = new ArrayList<GameListener>();
	this.clockTimer = new Timer(DELAY, null); //The clockTimer that should be used in the subclasses also
	this.isOverState = false;
	clockTimer.setCoalesce(true);
    }

    public void setIsOver()	{
	isOverState = true;
    }

    static public void addGameListener(GameListener gl)	{
	ListenerArray.add(gl);
    }

    public void notifyListeners()	{
	for(GameListener gl : ListenerArray)	{
	    gl.gameChanged();
	}
    }

    public void setCurrentPlayer(Person player)	{
	this.currentPlayer = player;
	this.currentPlayerIndex = getIndexByPerson(player);
        this.currentPlayer.changePersonState(PersonState.TURN);
    }

    public Person getCurrentPlayer()	{
	return currentPlayer;
    }

    protected void collectCards()	{
	/** Throw all the players cards to thrownCards  */
	for	(Person player : getPlayers())	{
	    player.throwCards();
	}
    }

    public void dealOutNCards(int N)	{
	int currentRound = 0;
	while	(N > currentRound)	{
	    for	(Person player : getActivePlayers())	{
		player.addCard(dealer.popCard());
	    }
	    currentRound++;
	}
    }

    public AbstractCardGameAction getActions()	{
	return actions;
    }

    public void stopClock()	{
	clockTimer.stop();
    }

    public void startClock()	{
	clockTimer.start();
    }

    public void setOptions(AbstractCardGameAction actions)	{
	this.actions = actions;
    }

    /** Allt the methods below is methods that should be overrided in subclasses */

    public void nextMove()	{}

    public boolean gameFinished()	{return false;}

    public void restartGame()	{}

    public void addPlayer(Person player)	{}

    public void startGame()	{}


}
