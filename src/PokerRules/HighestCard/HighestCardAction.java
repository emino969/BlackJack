package PokerRules.HighestCard;

public enum HighestCardAction
{
    STAND,
    CHANGE_CARD;

    @Override public String toString() {
	String string = super.toString();
	string = string.charAt(0) + string.toLowerCase().replaceFirst(String.valueOf(string.charAt(0)).toLowerCase(), "");
	return string;
    }

}
