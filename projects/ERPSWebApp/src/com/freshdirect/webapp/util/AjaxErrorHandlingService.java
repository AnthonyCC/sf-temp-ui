package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AjaxErrorHandlingService {

    private static final String ERROR_MESSAGE_SEPARATOR = "@@@@";
    private static final AjaxErrorHandlingService INSTANCE = new AjaxErrorHandlingService();

    private AjaxErrorHandlingService() {
    }

    public static AjaxErrorHandlingService defaultService() {
        return INSTANCE;
    }

    public String populateErrorMessage(String specificErrorMessage, String customerServiceContact) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        if (specificErrorMessage != null && !specificErrorMessage.isEmpty()) {
            errorMessageBuilder.append(specificErrorMessage);
            errorMessageBuilder.append(ERROR_MESSAGE_SEPARATOR);
            errorMessageBuilder.append(" Please try to fix the above mentioned error.");
            errorMessageBuilder.append(" If you have any issues, please contact the FreshDirect Customer Service representative at ");
            errorMessageBuilder.append(customerServiceContact);
            errorMessageBuilder.append(" for assistance. Sorry for the inconvenience.");
        } else {
            errorMessageBuilder.append("Internal error occured, please refresh the page! ");
            errorMessageBuilder.append(ERROR_MESSAGE_SEPARATOR);
            errorMessageBuilder.append(" If you have any issues, please contact the FreshDirect Customer Service representative at ");
            errorMessageBuilder.append(customerServiceContact);
            errorMessageBuilder.append(" for assistance. Sorry for the inconvenience.");
        }
        return errorMessageBuilder.toString();
    }

    public String prepareErrorMessageForAjaxCall(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(500);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/json");
        return (String) request.getAttribute("javax.servlet.error.message");

    }

    public String getPrimaryErrorMessage(String compositeErrorMessage) {
        return splitErrorMessage(compositeErrorMessage, 0);
    }

    public String getSecondaryErrorMessage(String compositeErrorMessage) {
        return splitErrorMessage(compositeErrorMessage, 1);
    }

    private String splitErrorMessage(String compositeErrorMessage, int index) {
        String result = null;
        if (compositeErrorMessage != null && !compositeErrorMessage.isEmpty() && 0 <= index) {
            String[] errorMessages = compositeErrorMessage.split(ERROR_MESSAGE_SEPARATOR);
            if (errorMessages != null && index < errorMessages.length) {
                result = errorMessages[index];
            }
        }
        return result;
    }
}
