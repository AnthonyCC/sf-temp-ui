package com.freshdirect.webapp.taglib.callcenter;

import java.util.Collections;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class AccountActivityTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static final long	serialVersionUID	= -8382817181253847058L;

	private static Category LOGGER = LoggerFactory.getInstance(AccountActivityTag.class);

	private String activities;
	private EnumAccountActivityType activityType;

	public void setActivities(String s) {
		this.activities = s;
	}
	
	public void setActivityType(EnumAccountActivityType activityType) {
		this.activityType = activityType;
	}

	public int doStartTag() throws JspException {

		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		try {
			ErpActivityRecord template = new ErpActivityRecord();
			template.setCustomerId(user.getIdentity().getErpCustomerPK());
			template.setActivityType(this.activityType);
			pageContext.setAttribute(this.activities, ActivityLog.getInstance().findActivityByTemplate(template));
		} catch (FDResourceException ex) {
			LOGGER.debug("Error getting customer activity...", ex);
			pageContext.setAttribute(this.activities, Collections.<ErpActivityRecord>emptyList());
		}
		return EVAL_BODY_BUFFERED;
	}
}
