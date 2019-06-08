package com.siteminder.emailservice.email.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier(value = "SendGrid")
public class SendGridEmailProvider implements EmailProvider
{
    @Value("${email-providers.sendgrid.api-key}")
    private String apiKey;

    private final String BASE_URL = "https://api.sendgrid.com/v3/mail/send";

    @Override
    public void send(EmailProviderSendRequest request) throws EmailServiceUnavailableException
    {
        HttpResponse<String> response = Unirest.post(BASE_URL)
                                               .header("authorization", "Bearer " + apiKey)
                                               .header("content-type", "application/json")
                                               .body("{\"personalizations\":[{\"to\":[{\"email\":\"john.doe@example.com\",\"name\":\"John Doe\"}],\"dynamic_template_data\":{\"verb\":\"\",\"adjective\":\"\",\"noun\":\"\",\"currentDayofWeek\":\"\"},\"subject\":\"Hello, World!\"}],\"from\":{\"email\":\"noreply@johndoe.com\",\"name\":\"John Doe\"},\"reply_to\":{\"email\":\"noreply@johndoe.com\",\"name\":\"John Doe\"},\"template_id\":\"d-8096b5dacb254c8b882816f22d1d11fe\"}")
                                               .asString();

    }

    private String getRequestBody(EmailProviderSendRequest request)
    {
        Gson gson = new Gson();
        JsonArray personalization = new JsonArray();

    }

    private JsonArray getTos(List<String> tos)
    {
        JsonArray array = new JsonArray();

        for (String to : tos)
        {
            JsonObject object = new JsonObject();
            object.addProperty("email", to);
            
            array.add(object);
        }

        return array;
    }
}
