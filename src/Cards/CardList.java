package Cards;

import CardGameExceptions.NoSuchCardException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardList
{
    private List<Card> cardList;

    public CardList()   {
        this.cardList = new ArrayList<Card>();
    }

    public boolean contains(Card card)  {
        return cardList.contains(card);
    }

    public boolean isEmpty()    {
        return cardList.isEmpty();
    }

    public boolean removeCard(Card card)    {
        return cardList.remove(card);
    }

    public boolean addCard(Card card)  {
	return cardList.add(card);
    }

    public boolean addHiddenCard(Card card)	{
	card.setNonVisible();
	return cardList.add(card);
    }

    public boolean isAllCardsVisible()	{
	for	(Card card : cardList)	{
	    if	(!card.isVisible())	{
		return false;
	    }
	}
	return true;
    }

    public void clearCardList() {
        cardList.clear();
    }

    public int getSize()    {
        return cardList.size();
    }

    public Card getCard(int index)	{
	return cardList.get(index);
    }

    public void setCardList(ArrayList<Card> cardList){
          this.cardList = cardList;
    }

    public void createOrdinaryDeck(){
	for (CardType cardType : CardType.values()) {
	    for (CardValue cardValue : CardValue.values()) {
		if(cardValue != CardValue.BOTTOM_ACE) cardList.add(new Card(cardType, cardValue));
	    }
	}
    }

    public void addCardList(CardList cl)	{
	for (int i = 0; i < cl.getSize(); i++) {
	    cardList.add(cl.getCard(i));
	}
    }

    public void shuffleDeck()   {
        Collections.shuffle(cardList);
    }

    public void printDeck() {
        for (Card card : cardList)  {
            System.out.println(card);
        }
    }

    public Card popCard()   { //Named popCard because it also removes the card from the deck
        Card card = cardList.get(0);
        removeCard(card);
        return card;
    }

    public Card getCardByIndex(int index) throws IndexOutOfBoundsException{
	return cardList.get(index);
    }

    public int getSumAceOnTop()	{
	int sum = 0;
	for (int i = 0; i < this.getSize(); i++) {
	    try {
		if	(this.getCardByIndex(i).getValue().equals(CardValue.TOP_ACE))	{
		    /** Ace is worth 11 when counted on top */
		    sum += 11;
		}	else if (this.getCardByIndex(i).getCardIntValue() > 10)	{
		    /** Accoding to blackjack rules everything excepts Ace is worth 10 */
		    sum += 10;
		}	else	{
		    sum += this.getCardByIndex(i).getCardIntValue();
		}
	    }	catch(NoSuchCardException e)	{
		System.out.println("Something went very wrong!");
	    }
	}
	return sum;
    }

    public int getSumAceOnBottom()	{ //For BlackJack when Ace can be both on top and bottom
	int sum = 0;
	for (int i = 0; i < this.getSize(); i++) {
	    try {
		if	(this.getCardByIndex(i).getValue().equals(CardValue.TOP_ACE))	{
		    /** If Ace is counted on bottom */
		    sum += 1;
		}	else if (this.getCardByIndex(i).getCardIntValue() > 10)	{
		    /** Accoding to blackjack rules everything excepts Ace is worth 10 */
		    sum += 10;
		}	else	{
		    sum += this.getCardByIndex(i).getCardIntValue();
		}
	    }	catch(NoSuchCardException e)	{
		System.out.println("Something went very wrong!");
	    }
	}
	return sum;
    }

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cardList) {
            stringBuilder.append(card).append(" ");
        }
        return stringBuilder.toString();    }

    public List<Card> getCardList() {
        return cardList;
    }

    public boolean containsCardValue(final CardValue cardValue) {
        for (Card card : cardList) {
            if(card.getCardValue() == cardValue) return true;
        }
        return false;
    }
}
