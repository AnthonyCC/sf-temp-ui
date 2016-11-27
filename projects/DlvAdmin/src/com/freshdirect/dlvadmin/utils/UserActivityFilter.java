/**
 * 
 */
package com.freshdirect.dlvadmin.utils;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.audit.SessionAuditor;
import com.freshdirect.dlvadmin.PageRegistry;

/**
 * @author kocka
 *
 */
public class UserActivityFilter implements Filter {

	public static final String LAST_INTERACTION_PARAM = "last.user.interacton";
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String user = req.getRemoteUser();
		if (user != null) {
			HttpSession session = req.getSession(true);
			Date lastInteraction = (Date) session.getAttribute(LAST_INTERACTION_PARAM);
			try {
				if (lastInteraction == null) {
					lastInteraction = new Date(session.getCreationTime());
						String role = PageRegistry.getUserRole(request);
						SessionAuditor.getInstance().userLoggedIn(session.getId(), user, role);
				} else {
					lastInteraction = new Date();
					SessionAuditor.getInstance().userInteraction(session.getId());
				}
			} catch (DlvResourceException e) {
				throw new ServletException(e);
			}
			session.setAttribute(LAST_INTERACTION_PARAM, lastInteraction);
		}
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

}
