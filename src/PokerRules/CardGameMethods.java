package PokerRules;

import Person.Person;

/**
 * implement these methods to add a cardGame, current
 */
public interface CardGameMethods
{
    void makeBet(Person person, int amount);

    void getNextPlayer();

    Person nextPerson();

    void runGameForward();

    boolean isOver();

    void getWinner();

    void makeToWinner(Person person);

    void makeToLoser(Person person);

    void setStartingStates();

    void startGame();

    void addPlayer(Person player);

    void nextMove();

    boolean gameFinished();
}
