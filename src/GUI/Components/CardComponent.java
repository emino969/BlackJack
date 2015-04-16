package GUI.Components;

import CardGameExceptions.NoSuchCardException;
import Cards.Card;

import java.awt.*;

/**
 * Graphical component corresponding to one card in either deck or hand
 */

public class CardComponent extends AbstractClickableComponent
{
    private static final int CARD_SIZE_X = 50;
    private static final int CARD_SIZE_Y = 75;
    private Card card;

    @Override public boolean isClicked() {
	return card.isClicked();
    }

    @Override public void setClicked(final boolean b) {
	card.setClicked(b);
    }

    @Override public void clickAction() {
	card.clickAction();
    }

    public CardComponent(final int x, final int y,  final Card card, PersonComponent personComponent) {
	this.setBounds(x, y, CARD_SIZE_X, CARD_SIZE_Y);
	this.card = card;
	personComponent.addToPokerComponent(this);

    }

    public void drawCard(Graphics2D g, boolean visable) {
	if (card.isClicked()) {
	    g.setColor(Color.BLUE);
	    g.fillRect(getX(), getY(), CARD_SIZE_X + 5, CARD_SIZE_Y + 5);
  }
	if (visable) {
	    g.setColor(Color.WHITE);
	    g.fillRect(getX(), getY(), CARD_SIZE_X, CARD_SIZE_Y);

	    Font font = new Font("Serif", Font.BOLD, 15);
	    g.setFont(font);
	    g.setColor(card.getColorByCardType());
	    try {
		g.drawString(String.valueOf(card.getCardIntValue()), getX() + 7, getY() + 15);
		g.drawString(String.valueOf(card.getCardIntValue()), getX() + CARD_SIZE_X - 18, getY() + CARD_SIZE_Y - 7);
	    } catch (NoSuchCardException e) {
	    }
	} else {
	    g.setColor(Color.BLUE);
	    g.fillRect(getX(), getY(), CARD_SIZE_X, CARD_SIZE_Y);

	}
    }


}
