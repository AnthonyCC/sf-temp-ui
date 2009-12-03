package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.mobileapi.controller.data.Message;
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

    protected boolean validateUser() {
        return false;
    }
    
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
        String emailAddress = request.getParameter(PARAM_EMAIL);
        Message responseMessage = null;
        if (EmailUtil.isValidEmailAddress(emailAddress)) {
            if (FDCustomerManager.iPhoneCaptureEmail(emailAddress)) {
                responseMessage = Message.createSuccessMessage("Email address has been submitted successfully.");
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
