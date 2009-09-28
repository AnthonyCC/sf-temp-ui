package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegisterGiftCardBuyerAction;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class GiftCardBuyerControllerTag extends AbstractControllerTag implements SessionName { 

	private static Category LOGGER = LoggerFactory.getInstance(GiftCardBuyerControllerTag.class);
	private String fraudPage;
	private int registrationType;
	private FDUserI user = null;

	public void setFraudPage(String s) {
		this.fraudPage = s;
	}
	
	public void setRegistrationType(int registrationType) {
		this.registrationType = registrationType;
	}

	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		String actionName = this.getActionName();
		try {
			if ("registerGiftCardBuyer".equalsIgnoreCase(actionName)) {
				RegisterGiftCardBuyerAction ra = new RegisterGiftCardBuyerAction(this.registrationType);

				HttpContext ctx =
					new HttpContext(
						this.pageContext.getSession(),
						(HttpServletRequest) this.pageContext.getRequest(),
						(HttpServletResponse) this.pageContext.getResponse());

				ra.setHttpContext(ctx);
				ra.setResult(actionResult);
				ra.setFraudPage(this.fraudPage);
				ra.setSuccessPage(this.getSuccessPage());

				ra.execute();
				this.setSuccessPage(ra.getSuccessPage()); //reset if changed.

				/*
				user = getUser();
				System.out.println("GiftCardBuyerControllerTag:  performAction() - user = " + user);
				ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(request, actionResult, user.getIdentity());
                if(actionResult.isSuccess()){
                    PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, actionResult, user);
            		if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
            	        String terms = request.getParameter(PaymentMethodName.TERMS);
            	        actionResult.addError(
            	    	        terms == null || terms.length() <= 0,
            	    	        PaymentMethodName.TERMS,SystemMessageList.MSG_REQUIRED
            	    	        );
            			if (actionResult.isSuccess() && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
            				paymentMethod.setIsTermsAccepted(true);
            			}
            		}
                    if(actionResult.isSuccess()) {
                        PaymentMethodUtil.addPaymentMethod(request, actionResult, paymentMethod);
                    }
                }
                */
			}
		} catch (Exception ex) {
				LOGGER.error("Error performing action " + actionName, ex);
				actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return true;
	}
	
	protected FDIdentity getIdentity() {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		return (user == null) ? null : user.getIdentity();
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

	private FDUserI getUser() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(USER);
		return user;
	}
}