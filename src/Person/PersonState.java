package Person;

/**
 * States that a person may have during gameplay
 */
public enum PersonState
{
    /**
     * a person in turnstate is currently active and the game is waiting for this player to finnish
     * its turn.
     */
    TURN,
    /**
     * a waiting player is currently waiting for their turn
     */
    WAITING,
    /**
     * hurray your a winner!
     */
    WINNER,
    /**
     * you not so much..
     */
    LOSER,
    /**
     * ...
     */
    INACTIVE, //Players that is inactive from making moves but haven't lost or won yet.
    /**
     * player has placed a bet already
     */
    PLACED_BET,
    /**
     * has folded
     */
    FOLDED
}
