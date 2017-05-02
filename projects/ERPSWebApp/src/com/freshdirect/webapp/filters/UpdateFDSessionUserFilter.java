package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class UpdateFDSessionUserFilter extends AbstractFilter {

	
	private final String filterName = this.getClass().getName();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		filterChain.doFilter(request, response);
		FDSessionUser user = (FDSessionUser) ((HttpServletRequest)request).getSession().getAttribute(SessionName.USER);
		if (user != null) {
			if (user.hasJustLoggedIn()) {
				Cookie cookie = new Cookie("hasJustLoggedIn", "true");
				cookie.setPath("/");
				((HttpServletResponse)response).addCookie(cookie);
			}
			if (user.hasJustSignedUp()) {
				Cookie cookie = new Cookie("hasJustSignedUp", "true");
				cookie.setPath("/");
				((HttpServletResponse)response).addCookie(cookie);
			}
		}
	}

	@Override
	public String getFilterName() {
		return filterName;
	}

}
