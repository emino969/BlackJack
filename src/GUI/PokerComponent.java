package GUI;

import GUI.Components.DealerComponent;
import GUI.Components.PlayerComponent;
import GUI.Components.TableComponent;
import GameListeners.GameListener;
import Person.Person;
import Pictures.Images;
import PokerRules.Game;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PokerComponent extends JComponent
{
    private Game game;
    private PokerFrame frame;
    private TableComponent tableComponent;
    private Images imageHandler;
    private static final int X_CONST = 70;
    private static final int Y_CONST = 90;
    private static final int ADJ_CONST = 50;

    public PokerComponent(Game game, PokerFrame frame)	{
	this.game = game;
	this.frame = frame;
	this.tableComponent = buildTableComponent();
	GameListener gl = new GameListener()	{
	    @Override public void gameChanged()	{
		repaint();
	    }
	};

	Game.addGameListener(gl);
	this.imageHandler = new Images();
    }

    private TableComponent buildTableComponent() {
	ArrayList<PlayerComponent> players = new ArrayList<PlayerComponent>();
	for (Person player : game.getOnlyPlayers()) {
	    players.add(new PlayerComponent(player, imageHandler));
	}
	TableComponent table = new TableComponent(players, this,
						  new DealerComponent(game.getDealer(), imageHandler),
						  frame.getWidth()/4 - X_CONST,
						  frame.getHeight()/3 - Y_CONST,
						  frame.getWidth()/2 + X_CONST * 2,
						  frame.getHeight()/3 + ADJ_CONST * 2,
						  imageHandler);
	return table;
    }

    @Override protected void paintComponent(Graphics g){
	super.paintComponent(g);
	tableComponent.drawTable(g);
    }

    public void drawTable(Graphics g){
	//g.setColor(ORANGE);
	//g.fillRect(frame.getWidth()/4, frame.getHeight()/3, frame.getWidth()/2, frame.getHeight()/3);
	//g.fillRect(150, 150, 400, 200);
    }

    public void drawPot(){
	//Ritar ut marker
    }

}
