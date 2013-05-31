package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

/**Redirects the user to the appropriate welcome page*/
public class WelcomeExperienceTag extends SimpleTagSupport {
	
	public static String WELCOME_PAGE_URL = "/welcome.jsp";
	private static final Logger LOGGER = LoggerFactory.getInstance(WelcomeExperienceTag.class);

	@Override
	public void doTag() {
		PageContext ctx = (PageContext) getJspContext();
		HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
		FDSessionUser user = (FDSessionUser) ctx.getSession().getAttribute(SessionName.USER);

		//Currently this tag is only used on index.jsp - if it's needed elsewhere please introduce a location attribute
		if(user.isUserCreatedInThisSession() && !user.isNewUserWelcomePageShown()){
			try {
				user.setNewUserWelcomePageShown(true);
				response.sendRedirect(WELCOME_PAGE_URL);
			
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}

	}
}