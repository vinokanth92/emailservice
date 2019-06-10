package com.siteminder.emailservice.email.provider;

public interface EmailProvider
{
    void send(EmailProviderSendRequest request) throws EmailServiceUnavailableException;
}
