package com.siteminder.emailservice.email.provider;

import com.siteminder.emailservice.email.SendEmailResponse;

public interface EmailProvider
{
    void send(EmailProviderSendRequest request) throws EmailServiceUnavailableException;
}
