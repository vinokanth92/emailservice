package com.siteminder.emailservice.email;

import java.util.Collections;
import java.util.List;

public final class SendEmailRequest
{
    private String from;
    private List<String> to;
    private String subject;
    private String body;
    private List<String> ccs;
    private List<String> bcs;

    public SendEmailRequest()
    {
        from = "";
        subject = "";
        body = "";

        to = Collections.emptyList();
        ccs = Collections.emptyList();
        bcs = Collections.emptyList();
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
        return bcs;
    }

    public void setBcs(List<String> bcs)
    {
        this.bcs = bcs;
    }
}
