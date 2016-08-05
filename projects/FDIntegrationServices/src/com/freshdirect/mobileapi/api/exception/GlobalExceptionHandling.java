package com.freshdirect.mobileapi.api.exception;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.service.ServiceException;

/**
 * Catches exceptions globally in the project.
 */
@ControllerAdvice
public class GlobalExceptionHandling {

    private static Category LOGGER = LoggerFactory.getInstance(GlobalExceptionHandling.class);

    @ExceptionHandler(FDException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(FDException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(ServiceException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(ValidationException e) {
        LOGGER.error("Exception happened", e);
        Message message = new Message();
        for (Map.Entry<String, String> entry : e.getErrors().entrySet()) {
            message.setFailureMessage(entry.getValue());
            LOGGER.error(entry.getValue());
        }
        return message;
    }

    @ExceptionHandler(FDResourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(FDResourceException e) {
        LOGGER.error("Exception happened", e);
        String errorMessage = null;
        if (e.getNestedException() != null) {
            errorMessage = e.getNestedException().getMessage();
        } else {
            errorMessage = e.getMessage();
        }
        return Message.createFailureMessage(errorMessage);
    }

    @ExceptionHandler(FDAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(FDAuthenticationException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(ErpInvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(ErpInvalidPasswordException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(JsonParseException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(JsonMappingException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handle(IOException e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Message handle(Exception e) {
        LOGGER.error("Exception happened", e);
        return Message.createFailureMessage(e.getMessage());
    }
}
