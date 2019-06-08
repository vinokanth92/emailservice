package com.siteminder.emailservice.email.provider;


import com.siteminder.emailservice.email.InvalidSendEmailRequestException;
import com.siteminder.emailservice.email.SendEmailRequest;

import java.util.ArrayList;
import java.util.List;

public class SendEmailRequestValidator
{
    public static void validate(SendEmailRequest request)
    {
        if (request.getFrom() == null || request.getFrom().isEmpty())
            throw new InvalidSendEmailRequestException("Empty or null value for from field is illegal");

        else if (request.getTo().isEmpty())
            throw new InvalidSendEmailRequestException("Empty values for to field is illegal");

        // Validate the format of each individual email IDs
        List<String> emails = new ArrayList<>();
        emails.add(request.getFrom());
        emails.addAll(request.getTo());
        emails.addAll(request.getCcs());
        emails.addAll(request.getBcs());

        for (String email : emails)
        {
            if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email))
                throw new InvalidSendEmailRequestException("Email ID: " + email + " is in invalid format");
        }
    }
}
