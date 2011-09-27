package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;

/**
 * @author Rob
 *
 */
public class ExternalInterfaceController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(ExternalInterfaceController.class);

    private static final String PARAM_ORDER_ID = "data";
    
    private static final String PARAM_SECURITY_ID = "t001_x";

    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
    	if(MobileApiProperties.isExternalInterfaceEnabled()) {
	        String saleId = request.getParameter(PARAM_ORDER_ID);
	        String securityId = request.getParameter(PARAM_SECURITY_ID);
	        
	        LOGGER.info("T001: Send Contact Notification " + saleId);
	        
	        Message responseMessage = null;
	        if(saleId != null && saleId.trim().length() > 0) {        	
	        	
	        	try {
	        		FDOrderI order = FDCustomerManager.getOrder(saleId);
		        	if(order != null) {
		        		LOGGER.info("T001: Customer Notification " + order.getCustomerId());
		        		FDCustomerInfo fdInfo = FDCustomerManager.getCustomerInfo(new FDIdentity( order.getCustomerId(), null));
		        		FDCustomerManager.doEmail(FDEmailFactory.getInstance().createOrderIvrContactEmail(fdInfo, saleId));
		        		responseMessage = Message.createSuccessMessage("T001 Successfully.");
		        	} else {
		        		LOGGER.info("T001: Unable to find Order " + saleId);
		        	}
	        	} catch(Exception e) {
	        		e.printStackTrace();
	        		LOGGER.info("T001_EXP: Unable to find Order " + saleId);
	        	}
	        }
	        if(responseMessage == null) {
	        	LOGGER.info("T001: Failed Contact Notification " + saleId);
	        	responseMessage = new Message();
	        	responseMessage.addErrorMessage("T001 Failed.");
	        }
	        setResponseMessage(model, responseMessage, user);
    	}
        return model;
    }

}
