package PokerRules.HighestCard;

import Cards.Card;
import Money.Pot;
import Person.*;
import PokerRules.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

public class HighestCard extends Game
{
    private int rounds;
    private HighestCardmoves moves;

    public HighestCard() {
	this.rounds = 0;
	this.dealer = new Dealer(new Pot(1000)); //The dealer's personal play account
 	this.moves = new HighestCardmoves()	{
	    @Override public String getHandValue(Person person)	{
		return "";
	    }

 	    @Override public ArrayList<String> getOptions(Person person)	{
 		ArrayList<String> methods = new ArrayList<String>();
		for (HighestCardAction method : HighestCardAction.values()) {
		    methods.add(method.toString());
		}
 	    	return methods;
 	    }

 	    @Override public void makeMove(String name)	{
 		if	(name.equals("Stand"))	{
 	    	    stand();
 	    	}	else if	(name.equals("Change card"))	{
 	    	    changeCard();
 	    	}
 	    }

	    @Override public void stand()	{
		currentPlayer.changePersonState(PersonState.WAITING);
 	    }

 	    @Override public void changeCard()	{
 		Card card = getCurrentPlayer().popCard();
 	      	dealer.addCardToThrownCards(card);
 	      	getCurrentPlayer().addCard(dealer.popCard());
		currentPlayer.changePersonState(PersonState.WAITING);
 	    }
 	};
 	setOptions(moves);
     }


    @Override public void addPlayer(Person player)	{
	player.setGame(this);
	players.add(player);
    }

    @Override public boolean gameFinished() {
	if (rounds == 3){
	    return true;
	} else {
	    return false;
	}
    }


    public void runGameForward()	{
	if (!gameFinished()) {
	    getNextPlayer();
	    currentPlayer.turn();
	}else{
	    clockTimer.stop();
	    getWinner();
    	}
	notifyListeners();
    }
	// handcomparator 
    private void checkRoundWinner() {
	//Need to clone because shuffles around the players in the table
	ArrayList<Person> players = new ArrayList<Person>(getActivePlayers());
	players.sort(new HandComparator());
	dealer.givePot(players.get(getPlayersSize()-1));
	notifyListeners();
    }

    public void startGame()	{
    	dealOutNCards(1);
    	this.currentPlayer = getPersonByIndex(0);
    	ActionListener move =  new AbstractAction() {
    	    @Override public void actionPerformed(ActionEvent e)	{
    		runGameForward();
    	    }
    	};

    	clockTimer.addActionListener(move);
    	clockTimer.start();
        }

    public void getWinner()	{
	//Need to clone because shuffles around the players in the table
	ArrayList<Person> players = new ArrayList<Person>(getActivePlayers());
	players.sort(new HandComparator());
	for (int i = 0; i < players.size(); i++) {
	    if(i == getPlayersSize() -1) dealer.givePot(players.get(i));
	    System.out.println(players.size() - i + ". " + players.get(i).getName() + " " + players.get(i).getPot());
	}
    }

    private boolean finishedRound()	{
	if (getIndexByPerson(currentPlayer) == getPlayersSize() - 1) {
	    checkRoundWinner();
	    rounds++;
	    return true;
    	} else	{
	    return false;
	}
    }

    private void getNextPlayer()	{
	if (!finishedRound()) {
	    currentPlayer.changePersonState(PersonState.WAITING);
	    setCurrentPlayer(getPersonByIndex(getIndexByPerson(currentPlayer) + 1));
	    currentPlayer.changePersonState(PersonState.TURN);
	}else	{
	    currentPlayer.changePersonState(PersonState.WAITING);
	    setCurrentPlayer(getPersonByIndex(0));
	    currentPlayer.changePersonState(PersonState.TURN);

	}
    }

    @Override public void nextMove()	{
	clockTimer.restart();
	runGameForward();
    }

    @Override public void restartGame()	{
	collectCards();
	dealer.startNewGame();
	rounds = 0;
	currentPlayer = getPersonByIndex(0);
	dealOutNCards(1);
	clockTimer.start();
	notifyListeners();
    }

    class HandComparator implements Comparator<Person>
    {
	@Override public int compare(Person p1, Person p2){
	    int truthValue = compareCards(p1.getHand().getCardByIndex(0).getValue(), p2.getHand().getCardByIndex(0).getValue());
	    return truthValue;
	}
	private int compareCards(Comparable c1, Comparable c2){
	    int a = c1.compareTo(c2);
	    if(a == 0) return 0;
	    if(a > 0) return 1;
	    return -1;
	}
    }
}
