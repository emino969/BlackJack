package PokerRules.BlackJack;

import CardGameExceptions.NoSuchCardException;
import Cards.Card;
import Money.Pot;
import Person.Person;
import Person.PersonState;
import PokerRules.CardGameMethods;
import PokerRules.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlackJack extends Game implements CardGameMethods
{
    private BlackJackMoves moves;
    private Map<Person, BlackJackHand> personMap;
    private Map<Person, Integer> personBets;
    private static final int FAST_DELAY = 1000;
    private ActionListener move =  new AbstractAction() {
    	    @Override public void actionPerformed(ActionEvent e)	{
    		runGameForward();
    	    }
    	};

    public BlackJack() {
	setDealer(new BlackJackDealer(new Pot(1000)));
	dealer.setGame(this); //Dealer is created in table
	this.moves = new BlackJackMoves()	{
	    @Override public String getHandValue(Person person)	{
		if	(isState(person, BlackJackHand.BLACKJACK)) {
		    return "  BLACKJACK  ";
		}	else {
		    return "  Hand: " + getLegalHandSum(person) + "  ";
		}
	    }

	    @Override public ArrayList<String> getOptions(Person person)	{
		ArrayList<String> names = new ArrayList<String>();
		if	(person.getHand().isEmpty()) {
		    names.add("Bet 25$");
		    names.add("Bet 50$");
		    names.add("Bet 75$");
		    names.add("Bet 100$");
		}	else if (!person.isPersonState(PersonState.INACTIVE))	{
		    names.add("Stand");
		    names.add("Hit");

		    if	(person.getHand().getSize() == 2) {
			names.add("Double");
			names.add("Surrender");
		    }

		    if	(isSplittable(person))	{
			//names.add("Split");
		    }
		}
		return names;
	    }

	    @Override public void makeMove(String name)	{
		if(name.equals("Stand"))	{
		    stand();
		}	else if(name.equals("Hit"))	{
		    hit();
		}	else if(name.equals("Bet 50$"))	{
		    buyCards(getCurrentPlayer(), 50);
		}	else if(name.equals("Bet 100$"))	{
		    buyCards(getCurrentPlayer(), 100);
		}	else if(name.equals("Bet 75$"))	{
		    buyCards(getCurrentPlayer(), 75);
		}	else if(name.equals("Bet 25$"))	{
		    buyCards(getCurrentPlayer(), 25);
		}	else if(name.equals("Double"))	{
		    doubleDown();
		}	else if(name.equals("Surrender"))	{
		    surrender();
		}	else if(name.equals("Split"))	{
		    hit();
		}	else if(name.equals("Double_hit")) {
		    doubleDown();
		}	else if(name.equals("Double_surrender")) {
		    doubleDown();
		}	else if(name.equals("Double_stand")){
		    doubleDown();
		}	else if (name.equals("Surrender_hit")){
		    surrender();
		}
	    }

	    @Override public void split()	{
		//Not completely added yet, WONT DO ANYTHING IF YOU PRESS SPLIT
		int currentBet = personBets.get(currentPlayer);
		makeBet(currentPlayer, currentBet);
		currentPlayer.addHand();
		currentPlayer.getHandByIndex(1).addCard(currentPlayer.popCard()); //Gets one of the cards from the first hand
		currentPlayer.getHandByIndex(0).addCard(dealer.popCard());
		currentPlayer.getHandByIndex(1).addCard(dealer.popCard());
	    }

	    @Override public void surrender()	{
		//Fold and lose half your bet
		int bet = personBets.get(currentPlayer);
		int loss = bet / 2;
		dealer.getTablePot().subtractAmount(bet);
		dealer.addToPot(loss);
		currentPlayer.addToPot(loss);
		personBets.put(currentPlayer, 0);
		currentPlayer.changePersonState(PersonState.LOSER);
	    }

	    @Override public void stand()	{
		/** Do nothing */
		currentPlayer.changePersonState(PersonState.WAITING);
	    }

	    @Override public void hit()	{
		/** Add one card to player */
		getCurrentPlayer().addCard(dealer.popCard());
		currentPlayer.changePersonState(PersonState.WAITING);
	    }

	    @Override public void doubleDown()	{
		/** Double the bet and stand */
		int bet = personBets.get(getCurrentPlayer());
		makeBet(getCurrentPlayer(), bet);
		hit();
	    }
	};

	setOptions(moves);
	this.personMap = new HashMap<Person, BlackJackHand>();
	this.personBets = new HashMap<Person, Integer>(); //Keeping track of every persons bet
    }

    private void buyCards(Person person, int amount)	{
	makeBet(person, amount);
 	dealer.giveNCardsToPlayer(person, 2);
	currentPlayer.changePersonState(PersonState.WAITING);
    }

    @Override public void makeBet(Person person, int amount)	{
	person.bet(amount);
	if	(personBets.containsKey(person)) {
	    int currentBettedPot = personBets.get(person);
	    personBets.put(person, currentBettedPot + amount);
	}	else	{
	    personBets.put(person, amount);
	}
    }

    public boolean isSplittable(Person person)	{
	if	(person.getHand().getSize() == 2) {
	    try {
		int firstCard = person.getHand().getCardByIndex(0).getCardIntValue();
		int secondCard = person.getHand().getCardByIndex(1).getCardIntValue();
		return firstCard == secondCard;
	    }	catch(NoSuchCardException e)	{
		System.out.println("There was no such card");
		return false;
	    }
	}	else	{
	    return false;
	}
    }

    private boolean checkVictoryOverDealer(Person person)	{
	if	(isState(dealer, BlackJackHand.BUSTED)) {
	    return true;
	}	else	{
	    return (getLegalHandSum(dealer) <= getLegalHandSum(person)) && !isState(person, BlackJackHand.BUSTED) ;
	}
    }

    @Override public void getNextPlayer()	{
	setCurrentPlayer(nextPerson());
	notifyListeners();
    }

    @Override public Person nextPerson()	{
	if	(getActivePlayers().size() != 0) {
	    if	(getActivePlayers().contains(currentPlayer)) {
		return getPersonByIndex((currentPlayerIndex + 1) % getActivePlayers().size());
	    }	else	{
		return getPersonByIndex(currentPlayerIndex % getActivePlayers().size());
	    }
	}	else	{
	    return null;
	}
    }

    @Override public void runGameForward()	{
	/** Give next player the turn */
	System.out.println(dealerIsInactive());
	currentPlayer.turn();
	updatePlayerState(currentPlayer);
	if	(isOver())	{
	    if	(isOverState)	{
		clockTimer.stop();
		getWinner();
		//Extremely important this comes before getNextPlayer()
		notifyListeners();
		//Otherwhise when the game is finished the getNextPlayer method will be stuck in a loop searching for players
	    }	else if	(dealerIsInactive())	{
		activateDealer();
		clockTimer.setDelay(FAST_DELAY); //Make the dealer finish faster
		clockTimer.restart();
		getNextPlayer();
	    }
	}	else	{
	    getNextPlayer();
	}

	//If current player is YOU, then stop the clock
	if	(currentPlayer.equals(getPlayer()))	{
	    clockTimer.stop();
	}
    }

    private boolean isEveryOneInactive()	{
	for	(Person person : getActivePlayers()) {
	    if (!person.isPersonState(PersonState.INACTIVE)) {
		return false;
	    }
	}
	return true;
    }

    @Override public boolean isOver()	{
	return (isEveryOneInactive() || isOverState);
    }

    private boolean dealerIsInactive()	{
	return dealer.isPersonState(PersonState.INACTIVE);
    }

    private void updatePlayerState(Person player) {
	int handSum = getLegalHandSum(player);
	if (handSum > 21) {
	    personMap.put(player, BlackJackHand.BUSTED);
	    player.changePersonState(PersonState.LOSER); //Also counts as inactive
	} else if (handSum == 21 && player.getHand().getSize() == 2) {
	    personMap.put(player, BlackJackHand.BLACKJACK);
	} else if (handSum < 21) {
	    personMap.put(player, BlackJackHand.THIN);
	}
    }

    public int getLegalHandSum(Person person)	{
	int minSum = person.getHand().getSumAceOnBottom();
	int maxSum = person.getHand().getSumAceOnTop();
	if	(maxSum <= 21)	{
	    return maxSum;
	}	else	{
	    return minSum;
	}
    }
    public Card getVisableDealerCard(){
	for (Card card : dealer.getHand().getCardList()) {
	    if(card.isVisible()) return card;
	}
	throw new RuntimeException("Dealer has no cards");
    }

    public boolean isState(Person person, BlackJackHand st)	{
	return personMap.get(person).equals(st);
    }

    @Override public void getWinner()	{
	isOverState = true;
	for	(Person person : getOnlyPlayers())	{
	    if	(checkVictoryOverDealer(person) && !person.isPersonState(PersonState.LOSER))	{
		makeToWinner(person);
	    }	else	{
		makeToLoser(person);
	    }
	}
    }

    private boolean isATie(Person person)	{
	if	(getLegalHandSum(dealer) == getLegalHandSum(person))	{
	    return isState(dealer, BlackJackHand.BLACKJACK) && isState(person, BlackJackHand.BLACKJACK);
	}	else	{
	    return false;
	}
    }

    @Override public void makeToWinner(Person person)	{
	int victoryAmount;
	int bet = personBets.get(person);

	if	(isATie(person)) {
	    victoryAmount = 0;
	}	else if(isState(person, BlackJackHand.BLACKJACK))	{
	    victoryAmount = bet / 2;
	}	else	{
	    victoryAmount = bet;
	}

	dealer.getTablePot().subtractAmount(bet);
	dealer.getPot().subtractAmount(victoryAmount); //Dealer pays all profits from his own pocket!
	person.addToPot(victoryAmount + bet);
	person.changePersonState(PersonState.WINNER);
    }

    @Override public void makeToLoser(Person person)	{
	int bet = personBets.get(person);
	person.changePersonState(PersonState.LOSER);
	dealer.giveAmountToPerson(dealer, bet);
    }

    @Override public void setStartingStates()	{
	for	(Person person : getOnlyPlayers())	{
	    person.changePersonState(PersonState.WAITING);
	    personMap.put(person, BlackJackHand.THIN);
	}
    }

    @Override public void startGame()	{
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	clockTimer.addActionListener(move);
	//clockTimer.start();
    }

    @Override public void addPlayer(Person player)	{
	player.setGame(this);
	players.add(player);
    }

    @Override public void nextMove()	{
	clockTimer.restart();
	runGameForward();
    }

    @Override public boolean gameFinished()	{
	return isOver();
    }

    @Override public void restartGame()	{
	isOverState = false;
	clockTimer.setDelay(DELAY);
	personBets.clear();
	personMap.clear();
	setStartingStates();
	collectCards();
	deactivateDealer();
	dealer.startNewGame();
	personMap.put(dealer, BlackJackHand.THIN);
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	notifyListeners();
    }
}
