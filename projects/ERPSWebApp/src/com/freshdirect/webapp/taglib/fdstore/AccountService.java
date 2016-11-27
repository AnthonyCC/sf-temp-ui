package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.freshdirect.framework.webapp.ActionResult;

public interface AccountService {

	public String login(HttpSession session, HttpServletRequest request,
			HttpServletResponse response);
	
	public String register(FDSessionUser user, PageContext pageContext, ActionResult actionResult,
			int registrationType) throws Exception;

}
