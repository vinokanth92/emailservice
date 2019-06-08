package com.siteminder.emailservice.email;

import com.siteminder.emailservice.email.provider.EmailProvider;
import com.siteminder.emailservice.email.provider.EmailProviderSendRequest;
import com.siteminder.emailservice.email.provider.EmailServiceUnavailableException;
import com.siteminder.emailservice.util.EmailValidator;
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
        EmailValidator.validate(request);
        EmailProviderSendRequest sendRequest = getEmailSendRequest(request);

        try
        {
            logger.info("Received new email send request. Attempting delivery via provider SendGrid");
            mailGun.send(sendRequest);
        }
        catch (EmailServiceUnavailableException e)
        {
            logger.error("Email deliver via SendGrid failed. Failing over to MailGun");
        }

        try
        {
            logger.info("Reattempting delivery via provider SendGrid");
            mailGun.send(null);
        }
        catch (EmailServiceUnavailableException e)
        {
            logger.error("Email delivery via MailGun failed. Failing over to MailGun");
        }

        return null;
    }

    private EmailProviderSendRequest getEmailSendRequest(SendEmailRequest request)
    {
        EmailProviderSendRequest sendRequest = new EmailProviderSendRequest(request.getFrom(), request.getTo());
        sendRequest.setSubject(request.getSubject());
        sendRequest.setBody(request.getBody());

        return sendRequest;
    }
}
