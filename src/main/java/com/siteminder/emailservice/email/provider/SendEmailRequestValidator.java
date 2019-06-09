package com.siteminder.emailservice.email.provider;


import com.siteminder.emailservice.email.InvalidSendEmailRequestException;
import com.siteminder.emailservice.email.SendEmailRequest;

import java.util.ArrayList;
import java.util.List;

public class SendEmailRequestValidator
{
    public static void validate(SendEmailRequest request)
    {
        if (request.getFrom().isEmpty())
            throw new InvalidSendEmailRequestException("Required argument 'from' is not supplied. Please supply sender as a string of valid email ID.");

        else if (request.getTo().isEmpty())
            throw new InvalidSendEmailRequestException("Required argument 'to' is not supplied. Please supply recipients as an array of valid email IDs.");

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
