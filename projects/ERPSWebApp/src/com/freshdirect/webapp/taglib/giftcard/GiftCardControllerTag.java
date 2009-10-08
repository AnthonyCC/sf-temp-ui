package com.freshdirect.webapp.taglib.giftcard;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.callcenter.ComplaintCreatorTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class GiftCardControllerTag extends AbstractControllerTag{
	
	private final int GC_RETRY_COUNT = 4;
	
	private final int GC_RETRY_WARNING_COUNT = 3;
	
	private static Category LOGGER 	= LoggerFactory.getInstance( GiftCardControllerTag.class );

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
		FDActionInfo info = AccountActivityUtil.getActionInfo(session);
		try{
        	if ("setAllowGCUsage".equalsIgnoreCase(this.getActionName())) {
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
			if ("applyGiftCard".equalsIgnoreCase(this.getActionName())) {
				if(!user.getFDCustomer().getProfile().allowApplyGC()) {
					actionResult.addError(new ActionError("account_locked",formatMessage(SystemMessageList.ACCOUNT_LOCKED_FOR_GC)));
					return true;
				}
				/*if(user.getGCRetryCount() >= GC_RETRY_COUNT){
					//Lock the customer account from applying gift card.
					user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "false");
					FDCustomerManager.setProfileAttribute(user.getIdentity(),"allow_apply_gc", "false",info);
					actionResult.addError(new ActionError("account_locked",formatMessage(SystemMessageList.ACCOUNT_LOCKED_FOR_GC)));
					return true;
				}
				if(user.getGCRetryCount() >= GC_RETRY_WARNING_COUNT){
					//Display warning message
					actionResult.addError(new ActionError("apply_gc_warning",formatMessage(SystemMessageList.APPLY_GC_WARNING)));
				}*/
				String givexNum = request.getParameter("givexNum");
				if(givexNum!=null && givexNum.trim().length()>0){
				try {
					ErpGiftCardModel gcModel = FDCustomerManager.applyGiftCard(user.getIdentity(), givexNum.trim(), info);
					if(gcModel.getBalance() == 0){
						actionResult.addError(new ActionError("card_zero_balance", SystemMessageList.APPLY_GC_WITH_ZERO_BALANCE));
					}
					user.getGiftCardList().addGiftCard(new FDGiftCardModel(gcModel));
					user.resetGCRetryCount();
				} catch(InvalidCardException e){
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
				/*if(actionResult.isSuccess()){
					this.redirectTo("/gift_card/giftcard_summary.jsp");
				}*/
			}
		}catch (FDResourceException e) {
			actionResult.setError(e.getMessage());
			throw new JspException(e);
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
