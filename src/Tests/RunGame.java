package Tests;

import GUI.PokerFrame;
import PokerRules.BlackJack.BlackJackAction;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	PokerFrame frame = new PokerFrame();
	frame.setVisible(true);
    }
}