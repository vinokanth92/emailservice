package com.siteminder.emailservice.email.provider;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailProviderStatusCodeResolver
{
    private static final Logger logger = LoggerFactory.getLogger(EmailProviderStatusCodeResolver.class);

    public static void resolve(HttpResponse<JsonNode> response) throws EmailServiceUnavailableException
    {
        if (response.getStatus() == 200)
            logger.info("Received ACK for email delivery");

        else if (response.getStatus() >= 400 && response.getStatus() < 500)
        {
            // Status codes of 4xx denotes client error
            // Since these error's cannot be recovered throw internal service failure exception
            logger.error("Email delivery failed due to client error. Error: " + response.getBody().toString());
            throw new InternalServiceFailureException(response.toString());
        }
        else if (response.getStatus() >= 500)
        {
            logger.error("Email delivery failed due to MailGun server error. Status code: " + response.getStatus());
            throw new EmailServiceUnavailableException();
        }
    }
}
