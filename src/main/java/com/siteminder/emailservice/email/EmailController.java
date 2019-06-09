package com.siteminder.emailservice.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/v1/emails")
public class EmailController
{
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    public EmailController(EmailService emailService)
    {
        this.emailService = emailService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ResponseBody
    public SendEmailResponse sendEmail(@RequestBody SendEmailRequest request)
    {
        logger.info("Received new SendEmailRequest");
        return emailService.sendEmail(request);
    }
}
