package com.freshdirect.webapp.taglib.giftcard;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class GiftCardControllerTag extends AbstractControllerTag{
	private static final long serialVersionUID = -1544805052660494678L;

	private final int GC_RETRY_COUNT = 4;
	
	private final int GC_RETRY_WARNING_COUNT = 3;
	
	@SuppressWarnings("unused")
	private static Category LOGGER 	= LoggerFactory.getInstance( GiftCardControllerTag.class );

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
		FDActionInfo info = AccountActivityUtil.getActionInfo(session);
		try{
        	String actionName = this.getActionName();
			if ("setAllowGCUsage".equalsIgnoreCase(actionName)) {
        		//System.out.println("setAllowGCUsage()=================");
        		/*
        		 * Toggle profile value for allow_apply_gc
        		 *	true = customer allowed to use gift card
        		 * 	false = customer NOT allowed to use gift card
        		 * */
        		if(null !=request.getParameter("allowGCUsageTRUE") || null != request.getParameter("allowGCUsageFALSE")){
	        		boolean allowGCUsage = user.getFDCustomer().getProfile().allowApplyGC();
	        		if (allowGCUsage) {
	        			user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "false");
						FDCustomerManager.setProfileAttribute(user.getIdentity(),"allow_apply_gc", "false",info);
	        		}else{
	        			user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "true");
						FDCustomerManager.setProfileAttribute(user.getIdentity(),"allow_apply_gc", "true",info);
						//System.out.println("resetGCRetryCount()=================");
						user.resetGCRetryCount();
	        		}
        		}
        	}
			if ("applyGiftCard".equalsIgnoreCase(actionName)) {
				if(!user.getFDCustomer().getProfile().allowApplyGC()) {
					actionResult.addError(new ActionError("account_locked",formatMessage(SystemMessageList.ACCOUNT_LOCKED_FOR_GC)));
					return true;
				}
				String givexNum = request.getParameter("givexNum");
				if(givexNum!=null && givexNum.trim().length()>0){
				try {
					ErpGiftCardModel gcModel = FDCustomerManager.applyGiftCard(user.getIdentity(), givexNum.trim(), info);
					if(gcModel.getBalance() == 0){
						actionResult.addError(new ActionError("card_zero_balance", SystemMessageList.APPLY_GC_WITH_ZERO_BALANCE));
					}
					user.getGiftCardList().addGiftCard(new FDGiftCardModel(gcModel));
					user.resetGCRetryCount();
				} 
				catch(ServiceUnavailableException se){
					actionResult.addError(new ActionError("service_unavailable",SystemMessageList.MSG_GC_SERVICE_UNAVAILABLE));
				}
				catch(InvalidCardException e){
					user.incrementGCRetryCount();
					if(user.getGCRetryCount() >= GC_RETRY_COUNT){
						//Lock the customer account from applying gift card.
						user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "false");
						FDCustomerManager.setProfileAttribute(user.getIdentity(),"allow_apply_gc", "false",info);
						actionResult.addError(new ActionError("account_locked",formatMessage(SystemMessageList.ACCOUNT_LOCKED_FOR_GC)));
						return true;
					}
					if(user.getGCRetryCount() >= GC_RETRY_WARNING_COUNT){
						//Display warning message
						actionResult.addError(new ActionError("apply_gc_warning",formatMessage(SystemMessageList.APPLY_GC_WARNING)));
					}
					actionResult.addError(new ActionError("invalid_card", SystemMessageList.MSG_GC_INVALID));
				}catch(CardOnHoldException e){
					//user.incrementGCRetryCount();
					actionResult.addError(new ActionError("card_on_hold", formatMessage(SystemMessageList.MSG_GC_ON_HOLD)));
				}catch(CardInUseException ce){
					//user.incrementGCRetryCount();
					if(ce.getCardOwner().equals(user.getUserId())){
						actionResult.addError(new ActionError("card_in_use", formatMessage(SystemMessageList.MSG_GC_ALREADY_ADDED)));
					}else {
						actionResult.addError(new ActionError("card_in_use", formatMessage(SystemMessageList.MSG_GC_IN_USE)));
					}
				}
				}else{
					actionResult.addError(new ActionError("invalid_card", SystemMessageList.MSG_GC_INVALID));					
				}
				if(actionResult.isSuccess() && request.getRequestURI().indexOf("giftcard_summary")>-1){
					this.redirectTo("/gift_card/giftcard_summary.jsp");
				}
			}
			
			
			//
			// ---
			//
			final String method = request.getMethod();
			if ("GET".equalsIgnoreCase( method )) {
	        	if("deleteBulkSavedRecipient".equalsIgnoreCase(actionName)) {
	        		// user = fs_user.getUser();   
	            	String repId = request.getParameter("deleteId");
	        		if (repId == null) {
	        			actionResult.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	        		} else {
	        			int repIndex = -1;
	            		try {
	            			repIndex = user.getBulkRecipentList().getRecipientIndex(Integer.parseInt(repId));

	            			user.getBulkRecipentList().removeOrderLineById(repIndex);
	            			user.getBulkRecipentList().constructFDRecipientsList();
	            		} catch (NumberFormatException nfe) {
	            			actionResult.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	            		}
	            		if (repIndex == -1) {
	            			actionResult.addError(new ActionError("system",SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	            		} else {
	            			//remove recipient
	            			user.getBulkRecipentList().removeRecipient(repIndex);
	            		}
	        		}
	            }
				
			} else if ("POST".equalsIgnoreCase(method)) {
            	if ("addBulkSavedRecipient".equalsIgnoreCase(actionName) ) {
            		GiftCardFormFields fld = new GiftCardFormFields(request);
                    fld.validateBulkGiftCard(user, actionResult);
                    if(actionResult.getErrors().isEmpty()) {
                    	FDBulkRecipientModel srm = fld.populateBulkSavedRecipient(user);
                                            	
                        user.getBulkRecipentList().addRecipient(srm);  
                        
                    } 
            	}            	
            	else if ("editBulkSavedRecipient".equalsIgnoreCase(actionName)) {            		
            		GiftCardFormFields fld = new GiftCardFormFields(request);
                    fld.validateBulkGiftCard(user, actionResult);
                    if(actionResult.getErrors().isEmpty()) {
                    	FDBulkRecipientModel srm = fld.populateBulkSavedRecipient(user);
                    	
                    	String repId = request.getParameter("recipId");
                		if (repId == null) {
                			actionResult.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
                		} else {
	                		int repIndex = -1;
	                		try {
	                			repIndex = user.getBulkRecipentList().getRecipientIndex(Integer.parseInt(repId));

	                			
	                		} catch (NumberFormatException nfe) {
	                			actionResult.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	                		}
	                		if (repIndex == -1) {
	                			actionResult.addError(new ActionError("system",SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	                		} else {
	                			//update recipient
	                			user.getBulkRecipentList().setRecipient(repIndex, srm);
	                		}
                		}
                    }
                }
				
			}
			
			
		}catch (FDResourceException e) {
			actionResult.addError(new ActionError("technical_difficulty", "Unable to process your request due to technical difficulty."));
			//throw new JspException(e);
		}
		return true;
	}
	
	private String formatMessage(String pattern){
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		return MessageFormat.format(
				pattern,
				new Object[] {UserUtil.getCustomerServiceContact(request)});		
	}
}
