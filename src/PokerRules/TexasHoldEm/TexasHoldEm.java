package PokerRules.TexasHoldEm;

import Money.Pot;
import Person.Person;
import Person.PersonState;
import Person.Player;
import PokerRules.CardGameMethods;
import PokerRules.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

public class TexasHoldEm extends Game implements CardGameMethods
{
    private boolean betPlaced;
    private static final int FAST_DELAY = 1000;
    private final static int startingBet = 20;
    private TexasHoldEmMoves actions;
    private int round;
    private int currentHighestBet;
    private Map<Person, TexasHoldEmState> personMap;
    private Map<Person, Integer> personBets;
    private Action move  = new AbstractAction()
    {
	@Override public void actionPerformed(final ActionEvent e) {
	    runGameForward();
	}
    };


    public TexasHoldEm() {
	this.round = 0;
	setDealer(new TexasDealer(new Pot(1000)));
	dealer.setGame(this);
	this.actions = new TexasHoldEmMoves()
	{

	    @Override public void knock() {
		currentPlayer.changePersonState(PersonState.PLACED_BET);
	    }

	    @Override public void fold() {
		currentPlayer.changePersonState(PersonState.LOSER);

	    }

	    @Override public void call() {
		makeBet(currentPlayer, dealer.getMinimumBet());
	    }

	    @Override public ArrayList<String> getOptions(final Person person) {
		ArrayList<String> options = new ArrayList<String>();
		switch(person.getState()){
		    case TURN:
			options.add("Knock");
			options.add("Call");
			options.add("Bet");
			options.add("Fold");
			options.add("Bet 25$");
			options.add("Bet 50$");
			options.add("Bet 75$");
			options.add("Bet 100$");
			break;
		    case WAITING:
			options.add("Call");
			options.add("Fold");
			break;
		    case PLACED_BET:
			options.add("Call");
			options.add("Fold");
			break;
		    case INACTIVE:
			break;


		}
		return options;
	    }

	    @Override public void makeMove(final String name) {
		System.out.println(name);
		switch (name) {
		    case "Knock":
			knock();
			break;
		    case "Fold":
			fold();
			break;
		    case "Call":
			call();
			break;
		    case "Bet 25$":
			makeBet(currentPlayer, 25);
			break;
		    case "Bet 50$":
			makeBet(currentPlayer, 50);
			break;
		    case "Bet 75$":
			makeBet(currentPlayer, 75);
			break;
		    case "Bet 100$":
			currentPlayer.bet(100);
			break;
		    default:
			break;

		}
	    }

	    @Override public String getHandValue(final Person person) {
		return null;
	    }


	};
	setOptions(actions);

    }
    public void runGameForward()	{
	/** Give next player the turn */
	updatePlayerStates(currentPlayer);
	currentPlayer.turn();
	if(gameIsOver())	{
	    if(isOverState)	{
		clockTimer.stop();
		getWinner();
		//Extremely important this comes before getNextPlayer()
		notifyListeners();
		//Otherwhise when the game is finished the getNextPlayer method will be stuck in a loop searching for players
	    }else if(dealerIsInactive())	{
	    }
		activateDealer();
		clockTimer.setDelay(FAST_DELAY); //Make the dealer finish faster
		clockTimer.restart();
		getNextPlayer();
	    }
	else{
	    getNextPlayer();
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

    @Override public boolean isOver() {
	return isEveryOneInactive() || isOverState;
    }


    private boolean gameIsOver() {
	int brokeBots = 0;
	for (Person player : players) {
	    if(player.getPot().getAmount() < startingBet){
		if(player.getClass() == Player.class) return true;
		brokeBots +=1;
	    }
	}
	if(brokeBots <= getPlayersSize() - 1) return true;

	return false;
    }

    private boolean roundIsOver(){
	for(Person person: getActivePlayers()){
	   if (person.getState() != PersonState.PLACED_BET) return false;
	}
	getWinner();
	return true;
    }

    public void getWinner() {

    }

    @Override public void makeToLoser(final Person person) {

    }

    @Override public void makeToWinner(Person person)	{
	int victoryAmount = 0;
	int bet = personBets.get(person);

	dealer.getTablePot().subtractAmount(bet);
	dealer.getPot().subtractAmount(victoryAmount); //Dealer pays all profits from his own pocket!
	person.addToPot(victoryAmount + bet);
	person.changePersonState(PersonState.WINNER);

    }

    @Override public void makeBet(final Person person, final int amount) {
	person.bet(amount);
	person.changePersonState(PersonState.PLACED_BET);

    }

    public void getNextPlayer()	{
	while(currentPlayer.isPersonState(PersonState.LOSER)) {
	    setCurrentPlayer(nextPerson());
	}
	notifyListeners();

    }

    public Person nextPerson()	{
	if	(!getActivePlayers().isEmpty()) {
	    if	(getActivePlayers().contains(currentPlayer)) {
		return getPersonByIndex((currentPlayerIndex + 1) % getActivePlayers().size());
	    }	else	{
		return getPersonByIndex(currentPlayerIndex % getActivePlayers().size());
	    }
	}	else	{
	    return null;
	}
    }
    private boolean dealerIsInactive() {
	return dealer.isPersonState(PersonState.INACTIVE);
    }

    @Override public void startGame() {
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	clockTimer.addActionListener(move);

    }

    public void setStartingStates() {
	for (Person person : getOnlyPlayers()) {
	    person.changePersonState(PersonState.WAITING);
	}
    }

    @Override public boolean gameFinished() {
	return isOver();
    }

    @Override public void restartGame() {
	super.restartGame();
    }

    @Override public void addPlayer(Person player)	{
	player.setGame(this);
	players.add(player);
    }

    @Override public void nextMove() {
	clockTimer.restart();
	runGameForward();
    }


    public void updatePlayerStates(final Person personState){

    }
}


