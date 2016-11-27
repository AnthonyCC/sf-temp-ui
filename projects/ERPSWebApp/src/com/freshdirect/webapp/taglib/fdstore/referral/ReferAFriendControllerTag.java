package com.freshdirect.webapp.taglib.fdstore.referral;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.EmailUtil;
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
		LOGGER.debug("\n\naction:" + action);
		if("sendmails".equals(action)) {
			//Form submitted. read emails
			String recipient_list = request.getParameter("form_tags_input");
			String mail_message = request.getParameter("mail_message");
			String rpid = request.getParameter("rpid");
			LOGGER.debug("\n\n\nWe got the recipient_list:" + recipient_list);
			LOGGER.debug("mail_message:" + mail_message + "\nrpid:" + rpid);
			//validate emails
			StringTokenizer stokens = new StringTokenizer(recipient_list, ",");
			boolean valid = true;
			if(stokens.countTokens() == 0) {
				valid = false;
			} else {
				while(stokens.hasMoreTokens()) {
					String recipient = stokens.nextToken().trim();
					if(!EmailUtil.isValidEmailAddress(recipient)) {
						valid = false;
						break;
					}					
				}
			}
			if(mail_message == null || mail_message.length() == 0) {
				valid=false;
			}
			if(mail_message.length() > 160) {
				valid = false;
			}
			if(valid) {			
				try {
					FDReferralManager.sendMails(recipient_list, mail_message, user.getUser(), rpid, request.getServerName());
				} catch (FDResourceException e) {
					LOGGER.error("Error reading emails",e);
				}
			} 
		}
	
		return EVAL_BODY_BUFFERED;
	}


	
}
