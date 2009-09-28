package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegisterDonationBuyerAction;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class DonationBuyerControllerTag extends AbstractControllerTag implements
		SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(DonationBuyerControllerTag.class);
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
			if ("registerRobinHoodBuyer".equalsIgnoreCase(actionName)) {
				RegisterDonationBuyerAction ra = new RegisterDonationBuyerAction(this.registrationType);

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
