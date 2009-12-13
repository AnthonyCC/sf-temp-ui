package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.ContactUsData;
import com.freshdirect.mobileapi.controller.data.response.ContactUsInit;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.ContactUs;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;

public class ContactUsController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(ContactUsController.class);

    private static String ACTION_GET_FORM_DATA = "getformdata";

    private static String ACTION_SUBMIT = "submit";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_GET_FORM_DATA.equals(action)) {
            model = getFormData(model, user);
        } else if (ACTION_SUBMIT.equals(action)) {
            ContactUsData contactUsData = parseRequestObject(request, response, ContactUsData.class);
            model = submitForm(model, user, contactUsData, request);
        }
        return model;
    }

    /**
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ServiceException
     * @throws JsonException 
     */
    private ModelAndView getFormData(ModelAndView model, SessionUser user) throws FDException, ServiceException, JsonException {
        Message responseMessage = null;
        ContactUs contactUs = new ContactUs(user);

        Map<String, String> subjects = contactUs.getContactUsSubjects();
        List<Map<String, String>> orders = contactUs.getPreviousOrders();
        responseMessage = new ContactUsInit(subjects, orders);
        responseMessage.setSuccessMessage("Contact Us form data has been retrieved successfully.");
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param contactUsData
     * @param request
     * @return
     * @throws FDException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ServiceException
     * @throws JsonException 
     */
    private ModelAndView submitForm(ModelAndView model, SessionUser user, ContactUsData contactUsData, HttpServletRequest request)
            throws FDException, ServiceException, JsonException {
        Message responseMessage = null;

        ContactUs contactUs = new ContactUs(user);

        ResultBundle resultBundle = contactUs.submitContactUs(contactUsData.getSubject(), contactUsData.getOrderId(), contactUsData
                .getMessage());
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Request has been submitted successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;

    }

}
