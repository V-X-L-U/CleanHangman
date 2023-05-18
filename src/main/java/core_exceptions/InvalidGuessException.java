package core_exceptions;

public class InvalidGuessException extends Exception
{
    public InvalidGuessException()
    {
        super("Invalid guess: a guess should be a single alphabet [a-zA-Z]");
    }
}
