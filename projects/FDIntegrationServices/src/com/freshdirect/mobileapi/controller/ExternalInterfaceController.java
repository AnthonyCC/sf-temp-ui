package com.freshdirect.mobileapi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumDeliveryMenuOption;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.mobileapi.util.StringUtil;

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
    
    private static final String ACTION_GET_SMS_MESSAGE_RELAY="smsMessageRelay";
    
    private static final String ACTION_GET_SMS_FDX_MESSAGE_RELAY="fdxsmsMessageRelay";
    
      private static final String ACTION_GET_FDX_DEL_INFO="fdxdeliveryInfo";
    
   
        
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
     	       
     	       try {
     	    	   CallLogModel logModel = getCallLogModelFromRequest(request);
     	    	   LOGGER.info("T002: Loading IVR Call log: " + logModel.toString());
     	    	   if(!StringUtil.isEmpty(logModel.getOrderNumber())){
     	    		   CallCenterServices.addNewIVRCallLog(logModel);
     	    		   responseMessage = Message.createSuccessMessage("T002 Successfull.");
     	    	   } 
     	        } catch(Exception e) {
	        		e.printStackTrace();
	        		LOGGER.info("T002_EXP: Unable to save IVR Call log for Order ");
	        	}
     	        if(responseMessage == null) {
	  	        	LOGGER.info("T002: Failed IVR call logging ");
	  	        	responseMessage = new Message();
	  	        	responseMessage.addErrorMessage("T002 Failed.");
	     	    }   
    		} else if(ACTION_GET_SMS_MESSAGE_RELAY.equals(action)){
    			String mobileNumber=null;
    			try{
    				// Call SmsAlertsManager with the parameters of the request.
    				mobileNumber=request.getParameter("sender");
    				String shortCode= request.getParameter("code");
    				String carrierName=request.getParameter("carrier");
    				String receivedDate=request.getParameter("received");
    				String message = request.getParameter("text");
    				SMSAlertManager smsAlertManager = SMSAlertManager.getInstance();
    				smsAlertManager.captureMessageRelayed(mobileNumber, shortCode, carrierName, receivedDate, message, EnumEStoreId.FD);
    				responseMessage = Message.createSuccessMessage("T003 Successfull.");
    			} catch(Exception e) {
	        		LOGGER.info("T003_EXP: Unable to save SMS Message Relay received for this Mobile number"+mobileNumber);
	        	}
    			if(responseMessage == null) {
	  	        	LOGGER.info("T003: Failed SMS Message Relay ");
	  	        	responseMessage = new Message();
	  	        	responseMessage.addErrorMessage("T003 Failed.");
	     	    }  
    		}
    		
    		//Start::Added by Sathishkumar Merugu for FDX SMS Alert
    		
    		else if(ACTION_GET_SMS_FDX_MESSAGE_RELAY.equals(action)){
    			String mobileNumber=null;
    			try{
    				// Call SmsAlertsManager with the parameters of the request.
    				mobileNumber=request.getParameter("sender");
    				String shortCode= request.getParameter("code");
    				String carrierName=request.getParameter("carrier");
    				String receivedDate=request.getParameter("received");
    				String message = request.getParameter("text");
    				SMSAlertManager smsAlertManager = SMSAlertManager.getInstance();
    				smsAlertManager.captureMessageRelayed(mobileNumber, shortCode, carrierName, receivedDate, message, EnumEStoreId.FDX);
    				responseMessage = Message.createSuccessMessage("T004 Successfull.");
    			} catch(Exception e) {
	        		responseMessage=Message.createFailureMessage("T004 Failed.");
	        		LOGGER.info("T004_EXP: Unable to save FDX SMS Message Relay received for this Mobile number"+mobileNumber);
	  	        }  
			if(responseMessage == null) {
	  	        	LOGGER.info("T004: Failed FDX SMS Message Relay ");
	  	        	responseMessage = new Message();
	  	        	responseMessage.addErrorMessage("T004 Failed.");
	     	    }  
    		}
    	
    			else if(ACTION_GET_FDX_DEL_INFO.equals(action)){
    			
    				
    			try{
    				String carrier = request.getParameter("carrier");
    				if("/deliv".equalsIgnoreCase(carrier) || "/uberrush".equalsIgnoreCase(carrier)){
    					carrier = StringUtils.upperCase(carrier.substring(1, carrier.length()));
    					FDDeliveryManager fDDeliveryManager = FDDeliveryManager.getInstance();
        				InputStream is = request.getInputStream();
        				
        				String line=null;
        				StringBuilder sb = new StringBuilder();
        				BufferedReader br = null;
        				try{
	        				br = new BufferedReader(new InputStreamReader(is));
	        				while ((line = br.readLine()) != null) {
	        					sb.append(line);
	        				}
        				}catch (IOException e) {
        					e.printStackTrace();
        				} finally {
        					if (br != null) {
        						try {
        							br.close();
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        					}
        				}
        				fDDeliveryManager.captureDeliveryEventNotification(carrier, sb.toString().trim());
        				responseMessage = Message.createSuccessMessage("T005 Successful.");
        				
    				}else{
    					String payload = request.getParameter("data");
    					String[] temp = payload.split(",");
    					String erpOrderId ="", deliveryTime = "", nexStopErpOrderId = "", estDeliveryTime = "";
	       			  	 if(temp != null && temp.length > 0) {
	       			  		 erpOrderId=temp[0];
	       			  		 if(temp.length > 1) {
	       			  		 	deliveryTime=temp[1]; 
	       			  		 }
	       			  		 if(temp.length > 2){
	       			  			 nexStopErpOrderId=temp[2]; 
	       			  		 }
	       			  		 if(temp.length > 3){
	       			  			 estDeliveryTime=temp[3]; 
	       			  		 }
	       			  	FDDeliveryManager fDDeliveryManager = FDDeliveryManager.getInstance();
	    				fDDeliveryManager.captureFdxDeliveryInfo(erpOrderId,deliveryTime,nexStopErpOrderId,estDeliveryTime);
	    				responseMessage = Message.createSuccessMessage("T005 Successful.");
	    				}
	       			  	 
    				}
	    				
    				
    				
    				
    			} catch(Exception e) {
    				e.printStackTrace();
    				responseMessage=Message.createFailureMessage("T005 Failed.");
    				LOGGER.info("T005_EXP: Unable to save fdx delivery info received ");
    			}  
    			if(responseMessage == null) {
    				LOGGER.info("T005: Failed fdx delivery info Relay ");
    				responseMessage = new Message();
    				responseMessage.addErrorMessage("T005 Failed.");
    			}  
    		}
    		
    		//End::Added by Sathishkumar Merugu for FDX SMS Alert
	        setResponseMessage(model, responseMessage, user);
    	}
        return model;
    }
    
  
    private static final String PARAM_CALLER_GUIID = "data";
    private static final String PARAM_CALLER_ID = "callerId";
    private static final String PARAM_SALE_ID = "orderNumber";    
    private static final String PARAM_CALL_STARTTIME = "callStartTime";
    private static final String PARAM_CALL_DURATION = "callDuration";
    private static final String PARAM_CALL_OUTCOME = "callOutcome";
    private static final String PARAM_CALL_TALKTIME = "talkTime";
    private static final String PARAM_CALL_PHONENUMBER = "phoneNumber";
    private static final String PARAM_CALL_MENU = "menuOption";
    
    private CallLogModel getCallLogModelFromRequest(HttpServletRequest request) throws Exception {
    	
    	LOGGER.info("T002: Loading IVR Call log for orderId # " + request.getParameter(PARAM_SALE_ID));
    	LOGGER.info(PARAM_CALLER_GUIID +": "+ request.getParameter(PARAM_CALLER_GUIID));
    	LOGGER.info(PARAM_CALLER_ID +": "+ request.getParameter(PARAM_CALLER_ID) );
    	LOGGER.info(PARAM_SALE_ID +": "+ request.getParameter(PARAM_SALE_ID) );
    	LOGGER.info(PARAM_CALL_STARTTIME +": "+ request.getParameter(PARAM_CALL_STARTTIME) );
    	LOGGER.info(PARAM_CALL_DURATION +": "+ request.getParameter(PARAM_CALL_DURATION) );
    	LOGGER.info(PARAM_CALL_OUTCOME +": "+ request.getParameter(PARAM_CALL_OUTCOME) );
    	LOGGER.info(PARAM_CALL_TALKTIME +": "+ request.getParameter(PARAM_CALL_TALKTIME) );
    	LOGGER.info(PARAM_CALL_PHONENUMBER +": "+ request.getParameter(PARAM_CALL_PHONENUMBER) );
    	LOGGER.info(PARAM_CALL_MENU +": "+ request.getParameter(PARAM_CALL_MENU) );	
    	
    	
    	CallLogModel logModel = new CallLogModel();
    	logModel.setCallerGUIId(request.getParameter(PARAM_CALLER_GUIID));// Caller GUI ID will be passed in "data" param
    	logModel.setCallerId(request.getParameter(PARAM_CALLER_ID));
    	logModel.setOrderNumber(request.getParameter(PARAM_SALE_ID));
    	logModel.setStartTime(StringUtil.getDatewithTime(request.getParameter(PARAM_CALL_STARTTIME)));
    	logModel.setDuration(StringUtil.getInt(request.getParameter(PARAM_CALL_DURATION)));
    	logModel.setCallOutcome(request.getParameter(PARAM_CALL_OUTCOME));	       
    	logModel.setTalkTime(StringUtil.getInt(request.getParameter(PARAM_CALL_TALKTIME)));
    	logModel.setPhoneNumber(request.getParameter(PARAM_CALL_PHONENUMBER));
    	logModel.setMenuOption((request.getParameter(PARAM_CALL_MENU) != null && !"".equals(request.getParameter(PARAM_CALL_MENU))
    									&& EnumDeliveryMenuOption.getEnumByDesc(request.getParameter(PARAM_CALL_MENU)) != null) 
    									? EnumDeliveryMenuOption.getEnumByDesc(request.getParameter(PARAM_CALL_MENU)).getName() : null);    	
    	return logModel;  
    }    
    
}
