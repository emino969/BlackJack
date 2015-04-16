package Person;

import Cards.Card;
import Cards.CardList;
import Money.Pot;

public class Dealer extends Person
{
    private CardList thrownCards, gameCards;
    private Pot tablePot;
    private int minimumBet = 20;
    private int cardSpace;

    public Dealer(Pot pot) {
	super("Dealer", pot);
	this.thrownCards = new CardList();
	this.gameCards = new CardList();
	this.tablePot = new Pot(0);
	gameCards.createOrdinaryDeck();
	gameCards.shuffleDeck();
	changePersonState(PersonState.INACTIVE); //Default for Dealer
    }

    public CardList getTableDeck()	{
	return gameCards;
    }

    public void givePot(Person person){
        person.addToPot(tablePot.getAmount());
        tablePot.clearPot();
    }

    public void giveAmountToPerson(Person person, int amount)	{//In case of multiple winners or a tie
	person.addToPot(amount);
	tablePot.subtractAmount(amount);
    }

    public void addCardToThrownCards(Card card)	{
	thrownCards.addCard(card);
    }

    public void startNewGame()	{
	/** add all the thrownCards to hand and shuffle */
	gameCards.addCardList(thrownCards);
	thrownCards.clearCardList();
	gameCards.shuffleDeck();
    }

    public Pot getTablePot()	{
	return tablePot;
    }

    public void giveNCardsToPlayer(Person person, int N)	{
	for (int i = 0; i < N; i++) {
	    person.addCard(popCard());
	}
    }
    public void giveNCardsToPlayer(Person person, int N, boolean hidden)	{
	for (int i = 0; i < N; i++) {
            Card card = popCard();
            if(person != game.getPlayer()) card.setNonVisible();
	    person.addCard(card);
	}
    }

    @Override public Card popCard()	{
	return gameCards.popCard();
    }

    public void giveStartingCards()	{}
    public CardList getThrownCards(){
        return thrownCards;
    }

    public int getCardSpace() {
        cardSpace = 2;
        return cardSpace;
    }
}
