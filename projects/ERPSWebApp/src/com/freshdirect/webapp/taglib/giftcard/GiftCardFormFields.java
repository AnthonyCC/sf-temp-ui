package com.freshdirect.webapp.taglib.giftcard;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class GiftCardFormFields implements Serializable{
	private static final long serialVersionUID = 7966498572174869631L;

	public String fldAmount = null;
	public String fldAltAmount = null;
	public String fldYourName = null;
	public String fldYourEmail = null;
	public String fldRecipientName = null;
	public String fldRecipientEmail = null;
	public String fldDeliveryMethod = null;
	public String fldMessage = null;
	public String gcTemplateId = null;
	public String fldQuantity=null;

	
	public GiftCardFormFields(HttpServletRequest request) {
    	this.fldAmount = request.getParameter("fldAmount");
    	this.fldAltAmount =  request.getParameter("fldAltAmount");
    	this.fldYourName =  request.getParameter(EnumUserInfoName.GC_BUYER_NAME.getCode());
    	this.fldRecipientName =  request.getParameter(EnumUserInfoName.GC_BUYER_NAME.getCode());
    	this.fldYourEmail =  request.getParameter(EnumUserInfoName.GC_BUYER_EMAIL.getCode());
    	this.fldRecipientEmail =  request.getParameter(EnumUserInfoName.GC_BUYER_EMAIL.getCode());
    	this.fldDeliveryMethod =  request.getParameter(EnumUserInfoName.DLV_METHOD.getCode());
    	this.fldMessage =  request.getParameter("fldMessage");
    	this.gcTemplateId = request.getParameter("gcTemplateId");
    	this.fldQuantity = request.getParameter(EnumUserInfoName.GC_QUANTITY.getCode());
	}


    /**
     * Checks for gift card data validity.
     * @param HttpServletRequest request
     * @param ActionResult result
     */
    public void validateBulkGiftCard(FDUserI user, ActionResult result) throws FDResourceException {
    	if(fldYourName==null || fldYourName.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_BUYER_NAME.getCode(), "Invalid or missing name"));
        }
    	
    	if(fldYourEmail==null || fldYourEmail.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_BUYER_EMAIL.getCode(), "Invalid or missing email"));
        }else if(!EmailUtil.isValidEmailAddress(fldYourEmail)){
    		result.addError(new ActionError(EnumUserInfoName.GC_BUYER_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
    	}
    	
    	if((fldQuantity==null || fldQuantity.length() < 1)) {
            result.addError(new ActionError(EnumUserInfoName.GC_QUANTITY.getCode(), "Invalid or missing quantity"));
        }else{
        	try {
				Integer qty = Integer.parseInt(fldQuantity);
				if(qty<1){
					result.addError(new ActionError(EnumUserInfoName.GC_QUANTITY.getCode(), "Invalid or missing quantity"));
				}
			} catch (NumberFormatException e) {
				result.addError(new ActionError(EnumUserInfoName.GC_QUANTITY.getCode(), "Invalid or missing quantity"));
			}
        }
    	if(("OTHER".equals(fldAmount) ||fldAmount==null || "".equals(fldAmount)) && ( fldAltAmount==null || "".equals(fldAltAmount))) {
            result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
        }else {
        	if(fldAltAmount != null && fldAltAmount.length() > 0) {
        		try {
					double amount = Double.parseDouble(fldAltAmount);
					if(amount <=0){
						result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
					}else{
						if(amount < FDStoreProperties.getGiftCardMinAmount()){
							result.addError(new ActionError("gc_amount_minimum", formatGCMinMaxMsg(user, SystemMessageList.MSG_GC_MIN_AMOUNT, FDStoreProperties.getGiftCardMinAmount())));
						}
						if(amount > FDStoreProperties.getGiftCardMaxAmount()){
							result.addError(new ActionError("gc_amount_maximum", formatGCMinMaxMsg(user, SystemMessageList.MSG_GC_MAX_AMOUNT, FDStoreProperties.getGiftCardMaxAmount())));
						}
					}
				}catch (NumberFormatException e) {
					  result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
				}

        	}
        }
    	
    }
    
	private String formatGCMinMaxMsg(FDUserI user, String pattern, double amount) {
		String formattedAmt = FormatterUtil.formatToGrouping(amount);
		return MessageFormat.format(
			pattern,
			new Object[] {formattedAmt, UserUtil.getCustomerServiceContact(user)});
	}


    
    
    /**
     * Adds new recipient to SavedRecipientModel
     */
    public FDBulkRecipientModel populateBulkSavedRecipient(FDUserI user) throws FDResourceException {
    		FDBulkRecipientModel srm=new FDBulkRecipientModel(); 
    	    srm.setRecipientEmail(fldRecipientEmail);
	    	srm.setSenderEmail(fldYourEmail);
	    	srm.setDeliveryMode(EnumGCDeliveryMode.PDF);
	    	srm.setPersonalMessage(fldMessage);
	    	srm.setFdUserId(user.getPrimaryKey());
	    	srm.setSenderName(fldYourName);
	    	srm.setRecipientName(fldRecipientName);
	    	srm.setQuantity(fldQuantity);
	    	srm.setTemplateId(gcTemplateId);
	    	if(fldAmount != null && fldAmount.length() > 1 && !fldAmount.equalsIgnoreCase("other")) {
	    		srm.setAmount(Double.parseDouble(fldAmount));
	    	} else if(fldAltAmount != null && fldAltAmount.length() > 1) {
	    		srm.setAmount(Double.parseDouble(fldAltAmount));
	    	}
	    	return srm;		    	    
    }

}