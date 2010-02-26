package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.MessageFormat;

import javassist.bytecode.Descriptor.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.util.JspMethods;

public class AddSavedRecipientControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(AddSavedRecipientControllerTag.class);
	private String gcAddCardPage = "/gift_card/purchase/add_giftcard.jsp";
	private String gcSubmitOrderPage = "/gift_card/purchase/purchase_giftcard.jsp";
	private static final String GIFTCARD_DEFAULT_ZPCOD="11101";
	private static final EnumServiceType GIFTCARD_DEFAULT_SERVICE_TYPE=EnumServiceType.PICKUP;
	
	FDUser user = null;
	
	// Var's declared in the TLD for this tag
    String actionName = null;
    String successPage = null;
    String resultName = null;
    
    String fldAmount = null;
    String fldAltAmount = null;
    String fldYourName = null;
    String fldYourEmail = null;
    String fldRecipientName = null;
    String fldRecipientEmail = null;
    String fldDeliveryMethod = null;
    String fldMessage = null;
    String gcTemplateId = null;
    
    public String getSuccessPage() {
        return this.successPage;
    }

    public void setSuccessPage(String sp) {
        this.successPage = sp;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        ActionResult result = new ActionResult();

        HttpSession session = pageContext.getSession();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        FDSessionUser fs_user = (FDSessionUser)session.getAttribute(USER);
        if(fs_user==null){
        	try{
        	FDUser fduser=FDCustomerManager.createNewUser(GIFTCARD_DEFAULT_ZPCOD, GIFTCARD_DEFAULT_SERVICE_TYPE);
        	fs_user = new FDSessionUser(fduser,request.getSession());        	
        	fs_user.setSelectedServiceType(GIFTCARD_DEFAULT_SERVICE_TYPE);
    		//Added the following line for zone pricing to keep user service type up-to-date.
    		user.setZPServiceType(GIFTCARD_DEFAULT_SERVICE_TYPE);
        	DlvServiceSelectionResult s_result=new DlvServiceSelectionResult();
    		//result.addServiceStatus(EnumServiceType.HOME, EnumDeliveryStatus.DELIVER);
        	s_result.addServiceStatus(GIFTCARD_DEFAULT_SERVICE_TYPE, EnumDeliveryStatus.DELIVER);
        	fs_user.setAvailableServices(s_result.getAvailableServices());					
    		CookieMonster.storeCookie(fs_user, response);
        	}catch (FDResourceException re) {
    			LOGGER.warn("FDResourceException occured while trying to create the new Gift Card User", re);
    			result.addError(true, "technicalDifficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
    	}
    	 
        }
        user = fs_user.getUser();
        boolean isCartChanged = false;
        

        //
        // perform any actions requested by the user if the request was a POST
        //
        if (("POST".equalsIgnoreCase(request.getMethod()))) {

            LOGGER.debug("POST dumpRequest: ============");
            JspMethods.dumpRequest(request);
            LOGGER.debug("POST dumpRequest: ============");
            
            //LOGGER.debug("setSuccessPage null: ");
        	//setSuccessPage(null);

            try {
            	if ("addSavedRecipient".equalsIgnoreCase(actionName) ) {
                    getFormData(request, result);
                    result = validateGiftCard(request, result);
                    
                    if(result.getErrors().isEmpty()) {
                        LOGGER.debug("inside addSavedRecipient successPage: "+successPage);
                        SavedRecipientModel srm = populateSavedRecipient();
                        //FDCustomerManager.storeSavedRecipient(user, srm);
                        user.getRecipentList().addRecipient(srm);
                    	//Set the last entered sender name and sender email to request.
                        fs_user.setLastSenderName(fldYourName);
                    	fs_user.setLastSenderEmail(fldYourEmail);
                    	isCartChanged = true;
                    	fs_user.setLastRecipAdded(true);
                    }
                } else if ("editSavedRecipient".equalsIgnoreCase(actionName)) {
                    getFormData(request, result);
                    validateGiftCard(request, result);
                    if(result.getErrors().isEmpty()) {
                    	SavedRecipientModel srm = populateSavedRecipient();
                    	//srm.setId(request.getParameter("recipId"));
                    	String repId = request.getParameter("recipId");
                		if (repId == null) {
                			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
                		} else {
	                		int repIndex = -1;
	                		try {
	                			repIndex = user.getRecipentList().getRecipientIndex(Integer.parseInt(repId));
	                		} catch (NumberFormatException nfe) {
	                			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	                		}
	                		if (repIndex == -1) {
	                			result.addError(new ActionError("system",SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	                		} else {
	                			//update recipient
	                			user.getRecipentList().setRecipient(repIndex, srm);
	                			isCartChanged = true;
	                			fs_user.setLastRecipAdded(true);
	                		}
                		}
                    	//FDCustomerManager.updateSavedRecipient(user, srm);
                    }
                } else if("checkout".equalsIgnoreCase(actionName)) {
                	UserValidationUtil.validateRecipientListEmpty(request, result);
                	setSuccessPage(gcSubmitOrderPage);
                	fs_user.setLastRecipAdded(false);
                }
                	
            } catch (FDResourceException ex) {
                LOGGER.warn("FDResourceException while trying to update customer info & addresses", ex);
                result.addError(new ActionError("technical_difficulty", "Could not update profile due to technical difficulty."));
            }
            //
            // redirect to success page if an action was successfully performed and a success page was defined
            //

            if (result.getErrors().isEmpty() && (successPage != null)) {
                LOGGER.debug("Success, redirecting to: "+successPage);                
                try {
                    response.sendRedirect(response.encodeRedirectURL(successPage));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                } catch (IOException ioe) {
                    //
                    // if there was a problem redirecting, continue and evaluate/skip tag body as usual
                    //
                    LOGGER.warn("IOException during redirect", ioe);
                }
            }
        } else  if (("GET".equalsIgnoreCase(request.getMethod()))) {

            //LOGGER.debug("GET dumpRequest: ============");
            //JspMethods.dumpRequest(request);
            //LOGGER.debug("GET dumpRequest: ============");
            
            LOGGER.debug("setSuccessPage null: ");
        	setSuccessPage(null);
            
        	if("deleteSavedRecipient".equalsIgnoreCase(actionName)) {
            	String repId = request.getParameter("deleteId");
        		if (repId == null) {
        			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
        		} else {
            		int repIndex = -1;
            		try {
            			repIndex = user.getRecipentList().getRecipientIndex(Integer.parseInt(repId));
            		} catch (NumberFormatException nfe) {
            			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
            		}
            		if (repIndex == -1) {
            			result.addError(new ActionError("system",SystemMessageList.MSG_IDENTIFY_RECIPIENT));
            		} else {
            			//remove recipient
            			user.getRecipentList().removeRecipient(repIndex);
            			isCartChanged = true;
                    	fs_user.setLastRecipAdded(true);
		                setSuccessPage(gcAddCardPage);
            		}
        		}

            	//FDCustomerManager.deleteSavedRecipient(request.getParameter("deleteId"));
            }
        	

            //
            // redirect to success page if an action was successfully performed and a success page was defined
            //

            if (result.getErrors().isEmpty() && (successPage != null)) {
                LOGGER.debug("Success GET, redirecting to: "+successPage);                
                try {
                    response.sendRedirect(response.encodeRedirectURL(successPage));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                } catch (IOException ioe) {
                    //
                    // if there was a problem redirecting, continue and evaluate/skip tag body as usual
                    //
                    LOGGER.warn("IOException during redirect", ioe);
                }
            }
        	
        }
        if(isCartChanged){
        	fs_user.saveCart();
        }
        //
        // place the result as a scripting variable in the page
        //
        pageContext.setAttribute(resultName, result);
        return EVAL_BODY_BUFFERED;
    }
    
    /**
     * Retrieves form field data for processing by the tag.
     * @param HttpServletRequest contains the form fields to be retrieved
     */
    private void getFormData(HttpServletRequest request, ActionResult result){
    	
    	fldAmount = request.getParameter("fldAmount");
    	fldAltAmount =  request.getParameter("fldAltAmount");
    	fldYourName =  request.getParameter(EnumUserInfoName.GC_BUYER_NAME.getCode());
    	fldRecipientName =  request.getParameter(EnumUserInfoName.GC_RECIPIENT_NAME.getCode());
    	fldYourEmail =  request.getParameter(EnumUserInfoName.GC_BUYER_EMAIL.getCode());
    	fldRecipientEmail =  request.getParameter(EnumUserInfoName.GC_RECIPIENT_EMAIL.getCode());
    	fldDeliveryMethod =  request.getParameter(EnumUserInfoName.DLV_METHOD.getCode());
    	fldMessage =  request.getParameter("fldMessage");
    	gcTemplateId =request.getParameter("gcTemplateId");
    }
    
    /**
     * Checks for gift card data validity.
     * @param HttpServletRequest request
     * @param ActionResult result
     */
    private ActionResult validateGiftCard(HttpServletRequest request, ActionResult result) throws FDResourceException {
    	if(fldYourName==null || "".equals(fldYourName)) {
            result.addError(new ActionError(EnumUserInfoName.GC_BUYER_NAME.getCode(), "Invalid or missing name"));
        }
    	if(fldRecipientName==null || "".equals(fldRecipientName)) {
            result.addError(new ActionError(EnumUserInfoName.GC_RECIPIENT_NAME.getCode(), "Invalid or missing name"));
        }
    	if(fldYourEmail==null || "".equals(fldYourEmail)) {
            result.addError(new ActionError(EnumUserInfoName.GC_BUYER_EMAIL.getCode(), "Invalid or missing email"));
        } else if(!EmailUtil.isValidEmailAddress(fldYourEmail)){
    		result.addError(new ActionError(EnumUserInfoName.GC_BUYER_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
    	}
    	if(fldDeliveryMethod==null || "".equals(fldDeliveryMethod)) {
            result.addError(new ActionError(EnumUserInfoName.DLV_METHOD.getCode(), "Invalid or missing delivery method"));
        }
    	if(fldDeliveryMethod != null ){//&& fldDeliveryMethod.equals("E")){
    		if(fldRecipientEmail==null || "".equals(fldRecipientEmail)) {
    			result.addError(new ActionError(EnumUserInfoName.GC_RECIPIENT_EMAIL.getCode(), "Invalid or missing email"));
    		} else if(!EmailUtil.isValidEmailAddress(fldRecipientEmail)){
        		result.addError(new ActionError(EnumUserInfoName.GC_RECIPIENT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
        	}
        }
    	
    	if((fldAmount==null || "".equals(fldAmount) || "OTHER".equalsIgnoreCase(fldAmount)) && ( fldAltAmount==null || "".equals(fldAltAmount))) {
            result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
        }else {
        	if(fldAltAmount != null && fldAltAmount.length() > 0) {
        		try {
					double amount = Double.parseDouble(fldAltAmount);
					if(amount < FDStoreProperties.getGiftCardMinAmount()){
						result.addError(new ActionError("gc_amount_minimum", formatGCMinMaxMsg(SystemMessageList.MSG_GC_MIN_AMOUNT, FDStoreProperties.getGiftCardMinAmount())));
					}
					if(amount > FDStoreProperties.getGiftCardMaxAmount()){
						result.addError(new ActionError("gc_amount_maximum", formatGCMinMaxMsg(SystemMessageList.MSG_GC_MAX_AMOUNT, FDStoreProperties.getGiftCardMaxAmount())));
					}
				} catch (NumberFormatException e) {
					  result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
				}

        	}
        }
    	
    	//UserValidationUtil.validateGCOrderMinimum(request, result);
    	return result;
    }
    
    /**
     * Adds new recipient to SavedRecipientModel
     */
    private SavedRecipientModel populateSavedRecipient() throws FDResourceException {
    	SavedRecipientModel srm = new SavedRecipientModel();
    	srm.setRecipientEmail(fldRecipientEmail);
    	srm.setSenderEmail(fldYourEmail);
    	srm.setDeliveryMode(EnumGCDeliveryMode.getEnum(fldDeliveryMethod));
    	srm.setPersonalMessage(fldMessage);
    	srm.setFdUserId(user.getPrimaryKey());
    	srm.setSenderName(fldYourName);
    	srm.setRecipientName(fldRecipientName);
    	srm.setTemplateId(gcTemplateId);
    	if(fldAmount != null && fldAmount.length() > 1 && !fldAmount.equalsIgnoreCase("other")) {
    		srm.setAmount(Double.parseDouble(fldAmount));
    	} else if(fldAltAmount != null && fldAltAmount.length() > 1) {
    		srm.setAmount(Double.parseDouble(fldAltAmount));
    	} 
    	return srm;
    }
        
	private String formatGCMinMaxMsg(String pattern, double amount) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String formattedAmt = FormatterUtil.formatToGrouping(amount);
		return MessageFormat.format(
			pattern,
			new Object[] {formattedAmt, UserUtil.getCustomerServiceContact(request)});
	}
            
}
		
