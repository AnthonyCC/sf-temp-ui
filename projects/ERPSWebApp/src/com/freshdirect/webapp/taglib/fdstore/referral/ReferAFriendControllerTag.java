package com.freshdirect.webapp.taglib.fdstore.referral;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ReferAFriendControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport  {
	
	private static final Category LOGGER = LoggerFactory.getInstance(ReferAFriendControllerTag.class);


	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (user.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		String action = request.getParameter("action");
		System.out.println("\n\naction:" + action);
		if("sendmails".equals(action)) {
			//Form submitted. read emails
			String recipient_list = request.getParameter("form_tags_input");
			String mail_message = request.getParameter("mail_message");
			String rpid = request.getParameter("rpid");
			System.out.println("\n\n\nWe got the recipient_list:" + recipient_list);
			System.out.println("mail_message:" + mail_message + "\nrpid:" + rpid);
			try {
				FDReferralManager.sendMails(recipient_list, mail_message, user.getUser(), rpid, request.getServerName());
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return EVAL_BODY_BUFFERED;
	}


	
}
