package com.siteminder.emailservice.email.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier(value = "SendGrid")
public class SendGridEmailProvider implements EmailProvider
{
    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailProvider.class);

    @Value("${email-providers.sendgrid.api-key}")
    private String apiKey;

    private final String BASE_URL = "https://api.sendgrid.com/v3/mail/send";

    @Override
    public void send(EmailProviderSendRequest request) throws EmailServiceUnavailableException
    {
        try
        {
            RequestBodyEntity body = Unirest.post(BASE_URL)
                                            .header("authorization", "Bearer " + apiKey)
                                            .header("content-type", "application/json")
                                            .body(getRequestBody(request));

            HttpResponse<JsonNode> response = body.asJson();
        }
        catch (UnirestException e)
        {

        }
    }

    private String getRequestBody(EmailProviderSendRequest request)
    {
        JsonObject body = new JsonObject();
        body.addProperty("subject", request.getSubject());

        JsonObject from = new JsonObject();
        from.addProperty("email", request.getFrom());
        body.add("from", from);

        JsonObject content = new JsonObject();
        content.addProperty("type", "text/plain");
        content.addProperty("value", request.getBody());

        JsonArray contentArray = new JsonArray();
        contentArray.add(content);
        body.add("content", contentArray);

        body.add("personalizations", getPersonalizationArray(request));
        return body.toString();
    }

    private JsonArray getPersonalizationArray(EmailProviderSendRequest request)
    {
        JsonArray personalizationArray = new JsonArray();
        JsonObject personalizationItem = new JsonObject();
        personalizationItem.add("to", getEmailArray(request.getTo()));
        personalizationItem.add("cc", getEmailArray(request.getCcs()));
        personalizationItem.add("bcc", getEmailArray(request.getBcs()));

        personalizationArray.add(personalizationItem);
        return personalizationArray;
    }

    private JsonArray getEmailArray(List<String> emails)
    {
        JsonArray array = new JsonArray();

        for (String email : emails)
        {
            JsonObject object = new JsonObject();
            object.addProperty("email", email);

            array.add(object);
        }

        return array;
    }
}
