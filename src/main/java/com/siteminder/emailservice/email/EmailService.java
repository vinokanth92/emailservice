package com.siteminder.emailservice.email;

public interface EmailService
{
    SendEmailResponse sendEmail(SendEmailRequest request);
}
