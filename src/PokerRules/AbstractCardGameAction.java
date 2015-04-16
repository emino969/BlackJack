package PokerRules;

import Person.Person;

import java.util.ArrayList;

public interface AbstractCardGameAction
{
    public ArrayList<String> getOptions(Person person);
    public void makeMove(String name);
    public String getHandValue(Person person);
}
