package CardGameExceptions;

public class NoSuchCardException extends Exception
{
    public NoSuchCardException(final String message) {
	super(message);
    }
}
