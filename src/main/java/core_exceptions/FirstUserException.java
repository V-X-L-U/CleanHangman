package core_exceptions;

public class FirstUserException extends Exception
{
    public FirstUserException(String operation)
    {
        super(String.format("Operation `%s` not allowed for first user", operation));
    }
}
