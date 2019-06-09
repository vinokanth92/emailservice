package com.siteminder.emailservice.email;

import org.springframework.http.HttpStatus;

public final class SendEmailResponse
{
    private String status;
    private String requestId;

    public SendEmailResponse(String status, String requestId)
    {
        this.status = status;
        this.requestId = requestId;
    }

    public String getStatus()
    {
        return status;
    }

    public String getRequestId()
    {
        return requestId;
    }
}
