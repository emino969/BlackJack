package PokerRules.BlackJack;

import CardGameExceptions.CardGameActionException;
import CardGameExceptions.NoSuchCardException;
import Cards.Card;
import Money.Pot;
import Person.Person;
import Person.PersonState;
import PokerRules.CardGameMethods;
import PokerRules.CardGameMove;
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

	    @Override public ArrayList<CardGameMove> getOptions(Person person)	{
		ArrayList<CardGameMove> actions = new ArrayList<>();
		if	(person.getHand().isEmpty()) {
		    actions.add(BlackJackAction.BET_25);
		    actions.add(BlackJackAction.BET_50);
		    actions.add(BlackJackAction.BET_75);
		}	else if (!person.isPersonState(PersonState.INACTIVE))	{
		    actions.add(BlackJackAction.STAND);
		    actions.add(BlackJackAction.HIT);

		    if	(person.getHand().getSize() == 2) {
			actions.add(BlackJackAction.DOUBLE);
			actions.add(BlackJackAction.SURRENDER);
		    }

		    if	(isSplittable(person))	{
			//names.add("Split");
		    }
		}
		return actions;
	    }

	  @Override  public void makeMove(CardGameMove blackJackAction) throws CardGameActionException {
		switch((BlackJackAction)blackJackAction){
		    case HIT:
			hit();
			break;
		    case STAND:
			stand();
			break;
		    case SURRENDER:
			surrender();
			break;
		    case DOUBLE:
			doubleDown();
			break;
		    case SPLIT:
			hit();
			break;
		    case BET_25:
			buyCards(currentPlayer, 25);
			break;
		    case BET_50:
			buyCards(currentPlayer,50);
			break;
		    case BET_75:
			buyCards(currentPlayer,75);
			break;
		    case DOUBLE_HIT:
			doubleDown();
			break;
		    case DOUBLE_STAND:
			doubleDown();
			break;
		    case DOUBLE_SURRENDER:
			surrender();
			break;
		    case SURRENDER_HIT:
			surrender();
			break;
		    default:
			throw new CardGameActionException("there is no such action");
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
		currentPlayer.changePersonState(PersonState.INACTIVE);
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
	setSuperGameType(this);
    }

    private void setSuperGameType(final BlackJack blackJack) {
	super.gameType = blackJack;
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
