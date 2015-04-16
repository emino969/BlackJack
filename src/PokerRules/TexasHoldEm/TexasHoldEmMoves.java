package PokerRules.TexasHoldEm;

import PokerRules.AbstractCardGameAction;

public abstract interface TexasHoldEmMoves extends AbstractCardGameAction
{
    public void knock();
    public void fold();
    public void call();
}
