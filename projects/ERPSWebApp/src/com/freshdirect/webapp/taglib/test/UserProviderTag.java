package com.freshdirect.webapp.taglib.test;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class UserProviderTag extends SimpleTagSupport{
	
	@Override
	public void doTag() throws JspException, IOException {
		PageContext ctx=(PageContext)getJspContext();
		
		HttpSession session=ctx.getSession();
		
		FDUserI user=(FDUserI)session.getAttribute(SessionName.USER);
		
		ctx.setAttribute("user", user);
	}

}
