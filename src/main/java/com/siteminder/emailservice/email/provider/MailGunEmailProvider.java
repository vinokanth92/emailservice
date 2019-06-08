package com.siteminder.emailservice.email.provider;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("MailGun")
public class MailGunEmailProvider implements EmailProvider
{
    private static final Logger logger = LoggerFactory.getLogger(MailGunEmailProvider.class);

    @Value(value = "${email-providers.mail-gun.api-key}")
    private String apiKey;

    @Value(value = "${email-providers.mail-gun.domain}")
    private String domain;

    private final static String BASE_URL = "https://api.mailgun.net/v3/";

    @Override
    public void send(EmailProviderSendRequest request) throws EmailServiceUnavailableException
    {
        try
        {
            logger.info("Attempting email deliver via MailGun");
            HttpRequestWithBody httpRequest = Unirest.post(BASE_URL + domain + "/messages")
                                         .basicAuth("api", apiKey)
                                         .queryString("subject", request.getSubject())
                                         .queryString("text", request.getBody());

            appendRecipients(httpRequest, request.getTo());
            appendCcs(httpRequest, request.getCcs());
            appendBcs(httpRequest, request.getBcs());

            HttpResponse<JsonNode> response = httpRequest.asJson();

            // Todo add email ID to log message
            if (response.getStatus() == 200)
                logger.info("Received ACK for email delivery");

            else if (response.getStatus() >= 400 && response.getStatus() < 500)
            {
                // Status codes of 4xx denotes client error
                // Since these error's cannot be recovered throw internal service failure exception
                logger.error("Email delivery failed due to client error. Status code: " + response.getStatus());
                throw new InternalServiceFailureException();
            }
            else if (response.getStatus() >= 500)
            {
                logger.error("Email delivery failed due to MailGun server error. Status code: " + response.getStatus());
                throw new EmailServiceUnavailableException();
            }
        }
        catch (UnirestException e)
        {
            logger.error("Email delivery failed. Failed to post request to MailGun service. Error: " + e);
            throw new InternalServiceFailureException();
        }
    }

    private static void appendRecipients(HttpRequestWithBody request, List<String> recipients)
    {
        for (String recipient : recipients)
            request.queryString("to", recipient);
    }

    private static void appendCcs(HttpRequestWithBody request, List<String> ccs)
    {
        for (String cc : ccs)
            request.queryString("cc", cc);
    }

    private static void appendBcs(HttpRequestWithBody request, List<String> bccs)
    {
        for (String bcc : bccs)
            request.queryString("bcc", bcc);
    }

}
