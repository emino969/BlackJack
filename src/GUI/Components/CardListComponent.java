package GUI.Components;

import Cards.CardList;
import Person.Person;

import java.awt.*;

public class CardListComponent
{
    private  PersonComponent personComponent;
    private CardList cardList;
    private TableComponent tableComponent;
    public void Component(PersonComponent personComponent) {
    }

   /* private void drawHand(CardList cl, final Graphics g, int x, int y)	{
	for (int i = 0; i < cl.getSize(); i++) {
	    if	(ts.equals(TableSeat.FIRST_SIDE)) {
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX, y + 80, this, imageHandler);
	    }	else if(ts.equals(TableSeat.SECOND_SIDE))	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX - 50, y, this, imageHandler);
	    }	else	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX, y - 90, this, imageHandler);
	    }
	}
    }*/
}
