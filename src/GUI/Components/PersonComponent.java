package GUI.Components;

import Cards.CardList;
import Person.*;
import Pictures.Images;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PersonComponent extends JComponent
{
    private Person person;
    private TableComponent tableComponent;
    private int xPos,yPos;
    private ArrayList<CardListComponent> cardLists;
    private int personRadius = 50;
    private final static int cardSpaceX = 25; //The space between the cards on the table if there is more than 1 card per player
    private final static int cardSpaceY = 15;
    private final static int spaceBetweenHands = 5;
    private final static int PERSON_RECTANGLE = 10;
    private final static int RECTANGLE_BAR = 2;
    private Images imageHandler;
    private FontMetrics fm;
    private TableSeat ts;
    private int width, height;
    private String name;

    public PersonComponent(final Person person, Images imageHandler) {
	this.person = person;
	this.imageHandler = imageHandler;
	this.fm = getFontMetrics(new Font("Serif", Font.BOLD, 20));
	this.name = person.getName();
	this.width = getMinimumWidth() + PERSON_RECTANGLE * 3;
	this.height = fm.getHeight() + PERSON_RECTANGLE * 4;
    }

    public void drawPlayer(final Graphics g, int x, int y, TableSeat ts ) {
	this.ts = ts;
        y -= personRadius;

	switch (person.getState()){
	    case LOSER:
		g.setColor(Color.RED);
		break;
	    case TURN:
		g.setColor(Color.YELLOW);
		break;
	    case WINNER:
		g.setColor(Color.GREEN);
		break;
	    default:
		g.setColor(Color.BLUE);
		break;
	}

	//MAKE BOXES WITH INFO
	g.setFont(new Font("Serif", Font.BOLD, 20));
	g.fillRoundRect(x, y, width, height, 10, 10);

	g.setColor(Color.BLACK);
	g.fillRoundRect(x + RECTANGLE_BAR / 2, y + RECTANGLE_BAR / 2, width - RECTANGLE_BAR, height - RECTANGLE_BAR, 10, 10);

	g.setColor(Color.WHITE);
	g.drawString(name, x + PERSON_RECTANGLE, y + fm.getHeight());
	g.drawString(String.valueOf(person.getPot().getAmount()) + '$', x + PERSON_RECTANGLE, y + 2 * fm.getHeight());

	drawPlayerCards(g, x, y); //A generalized solution for painting the cards
    }

    public int getStringWidth(String string)	{
	int pixelLength = 0;
	for (int i = 0; i < string.length(); i++) {
	    pixelLength += fm.charWidth(string.charAt(i));
	}
	return pixelLength;
    }

    public int getMinimumWidth()	{
	int nameLength = getStringWidth(person.getName());
	int potLength = getStringWidth(String.valueOf(person.getPot().getAmount()) + '$');
	if	(nameLength >= potLength)	{
	    return nameLength;
	}	else	{
	    return potLength;
	}
    }

    public int getHeight()	{
	return height;
    }

    public int getWidth()	{
	return width;
    }

    private void drawPlayerCards(final Graphics g, int x, int y)	{
	int numberOfHands = person.getHands().size();
	int calculatedSizeX = numberOfHands * cardSpaceX + (numberOfHands - 1) * spaceBetweenHands;
	for (int i = 0; i < numberOfHands; i++) {
	    int currentX = x + (i - 1) * cardSpaceX + (i - 1) * spaceBetweenHands - calculatedSizeX / 2;
	    int currentY = y;
	    CardList currentHand = person.getHands().get(i);
	    drawHand(currentHand, g, currentX, currentY);
	}
    }

    private void drawHand(CardList cl, final Graphics g, int x, int y)	{
	for (int i = 0; i < cl.getSize(); i++) {
	    if	(ts.equals(TableSeat.FIRST_SIDE)) {
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX, y + 80, this, imageHandler);
	    }	else if(ts.equals(TableSeat.SECOND_SIDE))	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX - 50, y, this, imageHandler);
	    }	else	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX, y - 90, this, imageHandler);
	    }
	}
    }

    public void addToPokerComponent(final CardComponent cardComponent) {
	tableComponent.addToPokerComponent(cardComponent);
    }
}
