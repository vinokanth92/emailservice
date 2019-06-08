package com.siteminder.emailservice.email;

import javax.websocket.SendResult;

public interface EmailService
{
    SendEmailResponse sendEmail(SendEmailRequest request);
}
