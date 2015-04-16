package GUI;

import Money.Pot;
import Person.BotTypes.BlackJackBot;
import Person.Player;
import Pictures.Images;
import PokerRules.BlackJack.BlackJack;
import PokerRules.Game;

import javax.swing.*;
import java.awt.*;

public class PokerFrame extends JFrame
{
    public final Game game;
    private static final int closeOperation = EXIT_ON_CLOSE;
    private static final int defaultWidth = 800;
    private static final int defaultHeight = 600;
    private Images imageHandler;

    public PokerFrame() {
	Player Emil = new Player("Emil", new Pot(1000));
	this.game = new BlackJack(); //Temporary for
	BlackJackBot PlayerBot = new BlackJackBot("Bot", new Pot(1000), (BlackJack) game);
	BlackJackBot SuperMario = new BlackJackBot("SuperMario", new Pot(1000),(BlackJack) game);
	BlackJackBot SuperBot = new BlackJackBot("SuperBot", new Pot(1000),(BlackJack) game);
	BlackJackBot SuperPlayer = new BlackJackBot("SuperPlayer", new Pot(1000), (BlackJack)game);
	this.imageHandler = new Images();
	game.addPlayer(Emil);
	game.addPlayer(PlayerBot);
	game.addPlayer(SuperMario);
	game.addPlayer(SuperBot);
	game.addPlayer(SuperPlayer);
	game.startGame();
	this.setDefaultCloseOperation(closeOperation);
	setSize(new Dimension(defaultWidth, defaultHeight));
	setContentPane(new JLabel(imageHandler.getBackgroundImageIcon()));
	setLayout(new BorderLayout());

	PokerComponent comp = new PokerComponent(game, this);
	PlayerFrame playerFrame = new PlayerFrame(game);
	add(comp);
	add(playerFrame, BorderLayout.AFTER_LAST_LINE);
    }
}