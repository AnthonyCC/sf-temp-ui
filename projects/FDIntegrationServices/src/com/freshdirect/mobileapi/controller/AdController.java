package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.ads.AdQueryStringFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.AdQueryStringResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.util.JspMethods;


public class AdController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(AdController.class);

    private static final String ACTION_GET_AD_QUERYSTRING = "queryparams";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action, SessionUser user)
            throws JsonException, FDException, ServiceException, NoSessionException, IOException, TemplateException {
        Message responseMessage = null;
        if (ACTION_GET_AD_QUERYSTRING.equals(action)) {
            FDUserI fdUser = user != null ? user.getFDSessionUser() : null;

            final boolean isMobileWebCapable = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, fdUser) && JspMethods.isMobile(request.getHeader("User-Agent"));

            final Map<String, String> queryParams = AdQueryStringFactory.composeAdQueryParams(fdUser, request, isMobileWebCapable);

            responseMessage = new AdQueryStringResponse(queryParams);
            responseMessage.setStatus(Message.STATUS_SUCCESS);
        } else {
            responseMessage = getErrorMessage(MessageCodes.ERR_SYSTEM, "Invalid or missing action.");
        }

        setResponseMessage(model, responseMessage, user);

        return model;
    }
}
