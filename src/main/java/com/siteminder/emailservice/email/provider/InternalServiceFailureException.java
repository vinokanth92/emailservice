package com.siteminder.emailservice.email.provider;

public class InternalServiceFailureException extends RuntimeException
{
    public InternalServiceFailureException(Exception e)
    {
        super(e);
    }

    public InternalServiceFailureException(String message)
    {
        super(message);
    }
}
