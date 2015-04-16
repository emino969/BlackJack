package PokerRules;

import Person.Person;

/**
 * declares default behaviour for cardGameMethods.
 */
public class CardGame implements CardGameMethods
{
    @Override public void makeBet(final Person person, final int amount) {

    }

    @Override public void getNextPlayer() {

    }

    @Override public Person nextPerson() {
	return null;
    }

    @Override public void runGameForward() {

    }

    @Override public boolean isOver() {
	return false;
    }

    @Override public void getWinner() {

    }

    @Override public void setStartingStates() {

    }

    @Override public void startGame() {

    }

    @Override public void nextMove() {

    }

    @Override public boolean gameFinished() {
	return false;
    }

    @Override public void addPlayer(final Person player) {

    }

    @Override public void makeToLoser(final Person person) {

    }

    @Override public void makeToWinner(final Person person) {

    }
}
