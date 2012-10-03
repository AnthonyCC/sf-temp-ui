package com.freshdirect.mobileapi.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerHome;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerSB;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.PhoneNumber;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
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
    
    private static final String PARAM_BCC_ID = "bcc";
    
    private static final String PARAM_CC_ID = "cc";
    
    private static final String PARAM_SECURITY_ID = "t001_x";
    
    private static final String ACTION_SEND_IVREMAIL = "sendIVREmail";

    private static final String ACTION_GET_IVR_CALLLOG = "getIVRCallLog";        
 
  	private static CallCenterManagerHome callCenterHome = null;
    
    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
    	
    	if(MobileApiProperties.isExternalInterfaceEnabled()) {
    		Message responseMessage = null;
    		
    		if(ACTION_SEND_IVREMAIL.equals(action)) {
    			
    	        String saleId = request.getParameter(PARAM_ORDER_ID);
    	        String securityId = request.getParameter(PARAM_SECURITY_ID);
    	        
    	        String bcc = request.getParameter(PARAM_BCC_ID);
    	        String cc = request.getParameter(PARAM_CC_ID);
    	        	        
    	        Collection<String> ccLst = null;
    	        Collection<String> bccLst = null;
    	        
    	        if(cc != null && cc.trim().length() > 0) {
    	        	ccLst = Arrays.asList(cc.split(","));
    	        }
    	        
    	        if(bcc != null && bcc.trim().length() > 0) {
    	        	bccLst = Arrays.asList(bcc.split(","));
    	        }
    	        
    	        LOGGER.info("T001: Send Contact Notification " + saleId+" ,cc:"+ccLst+" ,bcc:"+bccLst);
    	        
    	       
    	        if(saleId != null && saleId.trim().length() > 0) {        	
    	        	
    	        	try {
    	        		FDOrderI order = FDCustomerManager.getOrder(saleId);
    		        	if(order != null) {
    		        		LOGGER.info("T001: Customer Notification " + order.getCustomerId());
    		        		FDCustomerInfo fdInfo = FDCustomerManager.getCustomerInfo(new FDIdentity( order.getCustomerId(), null));
    		        		FDCustomerManager.doEmail(FDEmailFactory.getInstance().createOrderIvrContactEmail(fdInfo, saleId, ccLst, bccLst));
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
    			
    		} else if(ACTION_GET_IVR_CALLLOG.equals(action)) {
    			
    			CallLogModel requestMessage = null;
    			// Retrieving any possible payload
    	        String postData = getPostData(request, response);
    	        LOGGER.debug("PostData received: [" + postData + "]");
    	        if (StringUtils.isNotEmpty(postData)) {
    	            requestMessage = parseRequestObject(request, response, CallLogModel.class);            
    	        }
    			
      	       LOGGER.info("T002: Loading IVR Call log: " + requestMessage.getCallerId()+" ,orderNumber:"+requestMessage.getOrderNumber()+" ,phoneNumber:"+requestMessage.getPhoneNumber());
     	        
     	       lookupManagerHome();
     	       
     	       try {
	     	      
	     	        CallCenterManagerSB sb = callCenterHome.create();
	     	        sb.addNewIVRCallLog(requestMessage);
	     	   	    responseMessage = Message.createSuccessMessage("T002 Successfull.");
	     	        
     	        } catch(Exception e) {
	        		e.printStackTrace();
	        		LOGGER.info("T002_EXP: Unable to save IVR Call log for Order " + requestMessage.getOrderNumber());
	        	}
     	        if(responseMessage == null) {
	  	        	LOGGER.info("T002: Failed IVR call logging " + requestMessage.getOrderNumber());
	  	        	responseMessage = new Message();
	  	        	responseMessage.addErrorMessage("T002 Failed.");
	     	    }   
    		}
    		
	        setResponseMessage(model, responseMessage, user);
    	}
        return model;
    }
    
    protected static void lookupManagerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			callCenterHome = (CallCenterManagerHome) ctx.lookup(FDStoreProperties.getCallCenterManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}
}
