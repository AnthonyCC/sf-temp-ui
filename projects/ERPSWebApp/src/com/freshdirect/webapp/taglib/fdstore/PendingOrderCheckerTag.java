package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.customer.FDUserI;

public class PendingOrderCheckerTag extends SimpleTagSupport {
	
	private FDUserI user;
	
	@Override
	public void doTag() throws JspException, IOException {
		
		PageContext ctx = (PageContext) getJspContext();
		
		if(ctx.getAttribute("hasPendingOrder")!=null){
			return;
		}
		
		FDUserI user = getFDUser();
		if (user == null){
			ctx.setAttribute("hasPendingOrder", false);			
		} else {
			ctx.setAttribute("hasPendingOrder", user.isPopUpPendingOrderOverlay());			
		}
	}
	
	public FDUserI getFDUser() {
		PageContext ctx = (PageContext) getJspContext();
		if (user == null) {
			user = (FDUserI) ctx.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}

}
