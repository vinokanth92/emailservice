package com.siteminder.emailservice.email;

import com.siteminder.emailservice.email.provider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class EmailServiceImpl implements EmailService
{
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    @Qualifier(value = "SendGrid")
    private EmailProvider sendGrid;

    @Autowired
    @Qualifier(value = "MailGun")
    private EmailProvider mailGun;

    @Override
    public SendEmailResponse sendEmail(SendEmailRequest request)
    {
        SendEmailRequestValidator.validate(request);
        EmailProviderSendRequest sendRequest = getEmailSendRequest(request);

        try
        {
            logger.info("Received new email send request and generated internal ID: " + sendRequest.getId() + ". Attempting delivery via provider SendGrid");
            sendGrid.send(sendRequest);

            return new SendEmailResponse("Queued", sendRequest.getId());
        }
        catch (EmailServiceUnavailableException e)
        {
            logger.error("Email deliver via SendGrid failed for request with ID: " + sendRequest.getId() + ". Failing over to MailGun");
        }

        try
        {
            logger.info("Reattempting delivery via provider Mail Gun for request with ID: " + sendRequest.getId());
            mailGun.send(sendRequest);

            return new SendEmailResponse("Queued", sendRequest.getId());
        }
        catch (EmailServiceUnavailableException e)
        {
            logger.error("Email delivery via MailGun failed for request with ID: " + sendRequest.getId());
            throw new InternalServiceFailureException("Email delivery for request with ID: " + sendRequest.getId() + " failed with all possible providers.");
        }
    }

    private EmailProviderSendRequest getEmailSendRequest(SendEmailRequest request)
    {
        EmailProviderSendRequest sendRequest = new EmailProviderSendRequest(request.getFrom(), request.getTo());
        sendRequest.setSubject(request.getSubject());
        sendRequest.setBody(request.getBody());
        sendRequest.setCcs(request.getCcs());
        sendRequest.setBcs(request.getBcs());

        return sendRequest;
    }
}
