package PokerRules;

import CardGameExceptions.CardGameActionException;
import Person.Person;

import java.util.ArrayList;

public interface AbstractCardGameAction
{
    public ArrayList<CardGameMove> getOptions(Person person);
    public void makeMove(CardGameMove cardGameMove) throws CardGameActionException;
    public String getHandValue(Person person);
}
