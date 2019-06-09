package com.siteminder.emailservice.email.provider;

import java.util.List;
import java.util.UUID;

public class EmailProviderSendRequest
{
    private String id;
    private String from;
    private List<String> to;
    private String subject;
    private String body;
    private List<String> ccs;
    private List<String> bbcs;

    // It's possible to send email without subject and body (this is guaranteed by email spec)
    // But from and to fields are required parameters and hence expected via constructor
    public EmailProviderSendRequest(String from, List<String> to)
    {
        id = UUID.randomUUID().toString();
        this.from = from;
        this.to = to;
    }

    public String getId()
    {
        return id;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public List<String> getTo()
    {
        return to;
    }

    public void setTo(List<String> to)
    {
        this.to = to;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public List<String> getCcs()
    {
        return ccs;
    }

    public void setCcs(List<String> ccs)
    {
        this.ccs = ccs;
    }

    public List<String> getBcs()
    {
        return bbcs;
    }

    public void setBcs(List<String> bcs)
    {
        this.bbcs = bcs;
    }
}
