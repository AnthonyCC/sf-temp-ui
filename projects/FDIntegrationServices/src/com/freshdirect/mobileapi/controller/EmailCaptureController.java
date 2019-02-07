package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.EnumIPhoneCaptureType;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.EmailCapture;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;

/**
 * @author Rob
 *
 */
public class EmailCaptureController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(EmailCaptureController.class);

    private static final String PARAM_EMAIL = "email";
    
    private static final String PARAM_SOURCE_ANDROID = "ANW"; 
    
    private static final String ACTION_EMAIL_CAPTURE_EX = "emailcaptureEx";

    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
    	if(ACTION_EMAIL_CAPTURE_EX.equals(action)) { 
            Message responseMessage = null;
    		EmailCapture requestMessage = parseRequestObject(request, response,
    				EmailCapture.class);
    		String email = requestMessage.getEmail();
    		String zipCode = requestMessage.getZipCode();
    		String serviceType = requestMessage.getServiceType();
            if (EmailUtil.isValidEmailAddress(email)) {
                   if (FDCustomerManager.iPhoneCaptureEmail(email, zipCode, serviceType)) {
                    responseMessage = Message.createSuccessMessage("Email address has been submitted successfully.");
                   } else {
    				responseMessage = getErrorMessage(ERR_SYSTEM, ERR_SYSTEM_MESSAGE);
    			}
            } else {
                responseMessage = getErrorMessage(ERR_INVALID_EMAIL, "The email address you entered does not appear to be valid.  Maybe you mistyped something.");
            }        
            setResponseMessage(model, responseMessage, user);
    		return model;
    	} else {
        String emailAddress = request.getParameter(PARAM_EMAIL);
        Message responseMessage = null;
        if (EmailUtil.isValidEmailAddress(emailAddress)) {
        	String source = getPostData(request, response);
        	EnumTransactionSource srcEnum = getTransactionSourceEnum(request, source);
            if (EnumIPhoneCaptureType.UNREGISTERED.equals(FDCustomerManager.iPhoneCaptureEmail(emailAddress, srcEnum))) {
				LOGGER.info("controller: email is unregistered " + emailAddress);
                responseMessage = Message.createSuccessMessage("Email address has been submitted successfully.");
            } else if (EnumIPhoneCaptureType.EXISTING.equals(FDCustomerManager.iPhoneCaptureEmail(emailAddress, srcEnum))) {
				LOGGER.info("controller:  existing iphone capture email: " + emailAddress);
                responseMessage = getErrorMessage(ERR_INVALID_EMAIL,
                "The email address you entered matches an existing account in our system. Please sign in to start shopping. If you have forgotten your password or need additional assistance, visit our website or call (1-866-283-7374).");
            } else {
				responseMessage = getErrorMessage(ERR_SYSTEM, ERR_SYSTEM_MESSAGE);
			}
        } else {
            responseMessage = getErrorMessage(ERR_INVALID_EMAIL, "The email address you entered does not appear to be valid.  Maybe you mistyped something.");
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    }
}
