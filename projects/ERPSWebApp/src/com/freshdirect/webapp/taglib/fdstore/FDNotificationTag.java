package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.notification.FDNotification;
import com.freshdirect.fdstore.notification.ejb.FDNotificationManager;

public class FDNotificationTag extends SimpleTagSupport {

	private String attrName;

	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException, IOException {
		
		PageContext ctx = (PageContext) getJspContext();
		HttpSession session = ctx.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		
		List<FDNotification> notificationList=new ArrayList<FDNotification>();
		
		try {
			notificationList=(List<FDNotification>)FDNotificationManager.getInstance().loadCostumerNotifications(user.getIdentity());
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
		
		ctx.setAttribute(attrName, notificationList);
		
		getJspBody().invoke(null);

	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

}
