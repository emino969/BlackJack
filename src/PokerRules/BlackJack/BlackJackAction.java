package PokerRules.BlackJack;

public enum BlackJackAction
{
    HIT,
    STAND,
    SPLIT,
    DOUBLE,
    DOUBLE_HIT,
    DOUBLE_SURRENDER,
    DOUBLE_STAND,
    SURRENDER,
    SURRENDER_HIT,;

    @Override public String toString() {
        String string = super.toString();
        String replacement =  String.valueOf(string.charAt(0));
        String lowerString = string.toLowerCase();
        lowerString = lowerString.replaceFirst(String.valueOf(lowerString.charAt(0)),replacement);
        return lowerString;
    }
}