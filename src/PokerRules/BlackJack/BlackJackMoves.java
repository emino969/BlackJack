package PokerRules.BlackJack;

import PokerRules.AbstractCardGameAction;

public interface BlackJackMoves extends AbstractCardGameAction
{
    public void hit();
    public void stand();
    public void doubleDown();
    public void surrender();
    public void split();
}
