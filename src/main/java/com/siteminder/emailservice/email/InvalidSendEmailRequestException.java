package com.siteminder.emailservice.email;

public class InvalidSendEmailRequestException extends RuntimeException
{
    public InvalidSendEmailRequestException(String message)
    {
        super(message);
    }
}
