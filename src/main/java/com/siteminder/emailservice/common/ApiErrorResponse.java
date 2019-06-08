package com.siteminder.emailservice.common;


public class ApiErrorResponse
{
    private int statusCode;
    private String errorMessage;

    public ApiErrorResponse(int statusCode, String errorMessage)
    {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
