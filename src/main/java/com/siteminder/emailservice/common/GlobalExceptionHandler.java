package com.siteminder.emailservice.common;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.siteminder.emailservice.email.InvalidSendEmailRequestException;
import com.siteminder.emailservice.email.provider.InternalServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @SuppressWarnings("unused")
    @ExceptionHandler(value = {InvalidSendEmailRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse illegalArgument(Exception ex)
    {
        logger.error("Bad request: " + ex.getMessage());
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(value = {JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse requestParseError(JsonMappingException ex)
    {
        String message = "Failed to parse request body. Invalid data type for field: " + ex.getPath().get(0).getFieldName() + ". Please refer to schema definition for the expected data type.";
        logger.error(message);

        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(value = {InternalServiceFailureException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorResponse requestParseError(InternalServiceFailureException ex)
    {
        logger.error("Internal service failure exception: " + ex);
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Sorry something went wrong on our end. Administrators have been notified and we are fixing it.");
    }
}
