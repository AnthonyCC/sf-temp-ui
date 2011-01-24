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
	private ErpActivityRecord template;
	
	

	public void setTemplate(ErpActivityRecord template) {
		this.template = template;
	}

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
			if(null == template){
				template = new ErpActivityRecord();
			}
			template.setCustomerId(user.getIdentity().getErpCustomerPK());
//			template.setActivityType(this.activityType);
			if(null != template.getFromDate() && null != template.getToDate() && template.getToDate().before(template.getFromDate())){
				pageContext.setAttribute("endDateBeforeErr", " 'To' date should not be before 'From' date for filtering.");
			}else{
				pageContext.setAttribute(this.activities, ActivityLog.getInstance().findActivityByTemplate(template));
			}
			pageContext.setAttribute("filterList", ActivityLog.getInstance().getFilterLists(template));
		} catch (FDResourceException ex) {
			LOGGER.debug("Error getting customer activity...", ex);
			pageContext.setAttribute(this.activities, Collections.<ErpActivityRecord>emptyList());
		}
		return EVAL_BODY_BUFFERED;
	}
}
