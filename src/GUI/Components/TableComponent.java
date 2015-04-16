package GUI.Components;
import GUI.PokerComponent;
import Pictures.Images;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TableComponent extends JComponent
{
    private ArrayList<PersonComponent> players;
    private PokerComponent pokerComponent;
    private DealerComponent dealer;
    private int width, height;
    private int tableX, tableY;
    private Color color = Color.ORANGE;
    private Images imageHandler;
    private static final int BAR_SIZE = 20;

    public TableComponent(final ArrayList<PersonComponent> players, PokerComponent pokerComponent, final DealerComponent dealer, final int tableX,
			  final int tableY, final int width, final int height, Images imageHandler)
    {
	this.players = players;
	this.dealer = dealer;
	this.pokerComponent = pokerComponent;
	this.width = width;
	this.height = height;
	this.tableX = tableX;
	this.tableY = tableY;
	this.imageHandler = imageHandler;
    }

    public TableComponent(final ArrayList<PersonComponent> players, final PokerComponent pokerComponent,
			  final DealerComponent dealer, Images imageHandler)
    {
	this.players = players;
	this.pokerComponent = pokerComponent;
	this.dealer = dealer;
	this.imageHandler = imageHandler;
    }

    public void drawTable(Graphics g)	{
	int xPos, yPos;
	g.setColor(Color.BLACK);
	g.fillRect(tableX - BAR_SIZE, tableY - BAR_SIZE, width + BAR_SIZE * 2, height + BAR_SIZE * 2);
	g.drawImage(imageHandler.getTable(), tableX, tableY, width, height, this);
	/**
	int xPos = tableX + 100; //dealer needs some space dammit!
	int yPos = tableY;
	 */
	for (PersonComponent player : players) {
	    yPos = getPrefferedYSeat(player);
	    xPos = getPrefferedXSeat(player);
	    player.drawPlayer(g, xPos, yPos, getTableSide(player));
	    xPos += getSpace();
	}
	dealer.drawDealer(g, tableX - 30, tableY + height / 2 - 30);
    }

    public int getSpace(){
	//int circumference = getPlayerSpace();
	//int playerspace = circumference/players.size();
	//return playerspace;
	return width / 4;
    }

    public int getPlayerSpace(){
	return 2*width + 2*height;
	//return width*2 + height; //dealer has one side to himself
    }

    private int getPrefferedXSeat(PersonComponent player)	{
	int playerIndex = players.indexOf(player);
	if	(playerIndex <= 2)	{
	    return 120 + getSpace() * (playerIndex + 1);
	}	else if(playerIndex >= 4)	{
	    return (120 + getSpace() * 3) - getSpace() * (playerIndex - 4);
	}	else	{
	    return tableX + width ;
	}
    }

    private int getPrefferedYSeat(PersonComponent player)	{
	int playerIndex = players.indexOf(player);
	if	(playerIndex <= 2) {
	    return tableY - player.getHeight() / 2;
	}	else if(playerIndex >= 4)	{
	    return tableY + height + player.getHeight();
	}	else	{
	    return tableY + height / 2;
	}
    }

    private TableSeat getTableSide(PersonComponent player)	{
	if	(players.indexOf(player) <= 2)	{
	    return TableSeat.FIRST_SIDE;
	}	else if(players.indexOf(player) == 3)	{
	    return TableSeat.SECOND_SIDE;
	}	else	{
	    return TableSeat.THIRD_SIDE;
	}
    }

    public void addToPokerComponent(final CardComponent cardComponent) {
	this.add(cardComponent);
    }
}

