package GUI.Components;

import Person.Dealer;
import Pictures.Images;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.BLACK;

public class DealerComponent extends JComponent
{
    private Color color;
    private Dealer dealer;
    private int personRadius = 50;
    private final static int cardSpaceX = 25; //The space between the cards on the table if there is more than 1 card per player
    private final static int cardSpaceY = 15;
    private final static int PERSON_RECTANGLE = 10;
    private final static int RECTANGLE_BAR = 2;
    private int width, height;
    private String name;
    private Images imageHandler;
    private FontMetrics fm;

    public DealerComponent(Dealer dealer, Images imageHandler) {
	this.dealer = dealer;
	this.imageHandler = imageHandler;
	this.fm = getFontMetrics(new Font("Serif", Font.BOLD, 20));
	this.name = dealer.getName();
	this.width = getMinimumWidth() + PERSON_RECTANGLE * 3;
	this.height = fm.getHeight() + PERSON_RECTANGLE * 4;
    }


    public void drawDealer(final Graphics g, int x,  int y) {
        //g.setColor(Color.BLUE);
        drawDeck(g, x, y);
        drawPot(g, x, y);

	//MAKE BOXES WITH INFO
	if	(dealer.hasTurn())	{
	    g.setColor(Color.YELLOW);
	}	else	{
	    g.setColor(Color.BLUE);
	}

	g.setFont(new Font("Serif", Font.BOLD, 20));
	g.fillRoundRect(x - width, y, width, height, 10, 10);

	g.setColor(Color.BLACK);
	g.fillRoundRect(x + RECTANGLE_BAR / 2 - width, y + RECTANGLE_BAR / 2,
			width - RECTANGLE_BAR, height - RECTANGLE_BAR, 10, 10);

	g.setColor(Color.WHITE);
	g.drawString(name, x + PERSON_RECTANGLE - width, y + fm.getHeight());
	g.drawString(String.valueOf(dealer.getPot().getAmount()) + '$', x + PERSON_RECTANGLE - width, y + 2 * fm.getHeight());

        //g.fillOval(x, y, personRadius, personRadius);
        //g.setColor(BLACK);
        //g.drawString(dealer.getName(), x, y);
       // addGolfCap(g ,x ,y); // this is essential to functionality of the dealer

    }
    private void addGolfCap(Graphics g, int x, int y){
        g.setColor(Color.BLUE);
        g.fillRect(x + personRadius- 10, y, personRadius/4, personRadius);
    }

    private void drawDeck(Graphics g, final int x, final int y) {
        g.setColor(Color.RED);
	drawDealerCards(g, x, y); //A generalized solution for painting the cards
        //g.fillRect(x + 50, y + 50, 20, 10); // deck is slightly beside Dealer, has size W/H 20,10
    }
    private void drawPot(Graphics g, final int x, final int y){
        g.setColor(BLACK);
        g.drawString("Pot'o'gold: " +String.valueOf(dealer.getTablePot().getAmount()) + "$", x + 2* personRadius, y + 2*personRadius);
    }

    private void drawDealerCards(final Graphics g, int x, int y)	{
        for (int i = 0; i < dealer.getHand().getSize(); i++) {
	    dealer.getHand().getCardByIndex(i).draw((Graphics2D) g, x + i*dealer.getCardSpace() * cardSpaceX + 50, y, this, imageHandler);
	}
    }

    public int getStringWidth(String string)	{
	int pixelLength = 0;
	for (int i = 0; i < string.length(); i++) {
	    pixelLength += fm.charWidth(string.charAt(i));
	}
	return pixelLength;
    }

    public int getMinimumWidth()	{
	int nameLength = getStringWidth(dealer.getName());
	int potLength = getStringWidth(String.valueOf(dealer.getPot().getAmount()) + '$');
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
}
