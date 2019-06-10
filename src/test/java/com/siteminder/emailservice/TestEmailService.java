package com.siteminder.emailservice;

import com.siteminder.emailservice.email.EmailService;
import com.siteminder.emailservice.email.InvalidSendEmailRequestException;
import com.siteminder.emailservice.email.SendEmailRequest;
import com.siteminder.emailservice.email.provider.EmailServiceUnavailableException;
import com.siteminder.emailservice.email.provider.MailGunEmailProvider;
import com.siteminder.emailservice.email.provider.SendEmailRequestValidator;
import com.siteminder.emailservice.email.provider.SendGridEmailProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@PropertySource("classpath:application.properties")
public class TestEmailService
{
    @MockBean
    private SendGridEmailProvider sendGrid;

    @MockBean
    private MailGunEmailProvider mailGun;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenSendGridIsUnavailableMailGunIsInvoked() throws Exception
    {
        String from = "test-from@test.com";
        List<String> to = Collections.singletonList("test-to@test.com");

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setFrom(from);
        sendEmailRequest.setTo(to);

        Mockito.doThrow(new EmailServiceUnavailableException()).when(sendGrid).send(any());
        emailService.sendEmail(sendEmailRequest);

        Mockito.verify(mailGun).send(any());
    }

    @Test
    public void testInvalidationOfSendEmailRequestWithNoRequiredArgs()
    {
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setFrom("");

        Assertions.assertThrows(InvalidSendEmailRequestException.class, () ->
        {
            SendEmailRequestValidator.validate(sendEmailRequest);
        });

        sendEmailRequest.setFrom("test@test.com");
        sendEmailRequest.setTo(Collections.emptyList());

        Assertions.assertThrows(InvalidSendEmailRequestException.class, () ->
        {
            SendEmailRequestValidator.validate(sendEmailRequest);
        });
    }

    @Test
    public void testIfInvalidEmailsFormatsAreDetected()
    {
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        final String VALID_EMAIL = "test@test.com";

        List<String> invalidEmails = new ArrayList<>();
        invalidEmails.add("invalid-string");
        invalidEmails.add("3434");
        invalidEmails.add("test@test");
        invalidEmails.add("4323@test");
        invalidEmails.add("www.test.com");
        invalidEmails.add("www.test.com.au");
        invalidEmails.add("#*&^$@test.com");

        // Test the validity of from field
        for (String invalidEmail : invalidEmails)
        {
            sendEmailRequest.setFrom(invalidEmail);
            Assertions.assertThrows(InvalidSendEmailRequestException.class, () ->
            {
                SendEmailRequestValidator.validate(sendEmailRequest);
            });
        }

        // Test the validity of to field
        sendEmailRequest.setFrom(VALID_EMAIL);
        sendEmailRequest.setTo(invalidEmails);

        Assertions.assertThrows(InvalidSendEmailRequestException.class, () -> {
            SendEmailRequestValidator.validate(sendEmailRequest);
        });

        // Test validity of bccs
        // Note: From and To having the same email ID is considered a valid request by design
        sendEmailRequest.setTo(Collections.singletonList(VALID_EMAIL));
        sendEmailRequest.setTo(Collections.singletonList(VALID_EMAIL));
        sendEmailRequest.setBcs(invalidEmails);

        Assertions.assertThrows(InvalidSendEmailRequestException.class, () -> {
            SendEmailRequestValidator.validate(sendEmailRequest);
        });

        // Test validity of ccs
        sendEmailRequest.setTo(Collections.singletonList(VALID_EMAIL));
        sendEmailRequest.setTo(Collections.singletonList(VALID_EMAIL));
        sendEmailRequest.setBcs(Collections.singletonList(VALID_EMAIL));
        sendEmailRequest.setCcs(invalidEmails);

        Assertions.assertThrows(InvalidSendEmailRequestException.class, () -> {
            SendEmailRequestValidator.validate(sendEmailRequest);
        });
    }
}
