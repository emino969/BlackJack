package Person.BotTypes;

import CardGameExceptions.NoSuchCardException;
import Cards.Card;
import Cards.CardList;
import Cards.CardType;
import GameListeners.GameListener;
import Money.Pot;
import Person.Person;
import PokerRules.Game;

import java.util.Random;

public class HighestCardBot extends Person implements GameListener
{
    private CardList playedCards;
    private Random random = new Random();
    public HighestCardBot(final String name, final Pot pot, final Game game) {
        super(name, pot);
        this.game = game;
    }

    @Override public void turn() {
        this.bet(getBetAmount());
        try {
            if(this.getHand().getCardByIndex(0).getCardIntValue() > 10) game.getActions().makeMove("Stand");
	    else{
		game.getActions().makeMove("Change card");
	    }
        } catch (NoSuchCardException e) {
            e.printStackTrace();
        }
    }

    public int getBetAmount() {
        Person dealer = game.getDealer();
        int bet = dealer.getMinimumBet();
        int higherCards = getHigherCards();
        double odds = (double) higherCards/52; // if highercards/52 isnt cast as double, java will give the result 0.0 as it rounds 0.xxx to  0.0
        bet += getBet(odds);
        if(bet > pot.getAmount()) bet = pot.getAmount();
        return bet;
    }
    private int getBet(double odds){
        System.out.println(name + odds);
        if(odds < 0.2) return highBet();
        else if(odds < 0.4) return mediumBet();
        else if( odds < 0.5) return lowBet();
        else return 0;
    }
    private int getHigherCards(){
        int cardValue = 0;
        try {
            cardValue = hand.getCardByIndex(0).getCardIntValue();
        } catch (NoSuchCardException e) {
            e.printStackTrace();
        }
        int higherCards = (14 - cardValue) * CardType.values().length ; // You have your own card
        if(playedCards != null) {
            for(Card card: playedCards.getCardList()) {
                try {
                    if (card.isVisible() && cardValue < card.getCardIntValue()) higherCards--;
                } catch (NoSuchCardException e) {
                    e.printStackTrace();
                }
                // if there is a higher card that has been played there is one less in play
            }

        }
        return higherCards;
    }
    private int highBet(){
        int bet = 250;
        int betFactor = random.nextInt(5);
        bet += betFactor * 100;
        return bet;
    }
    private int lowBet(){
        int bet = 0;
        int betFactor = random.nextInt(5);
        bet += betFactor*10;
        return bet;
    }
    private int mediumBet(){
        int bet = 100;
        int betFactor = random.nextInt(5);
        bet += betFactor * 50;
        return bet;
    }

    @Override public void gameChanged() {
        playedCards = game.getDealer().getThrownCards();
    }
}
