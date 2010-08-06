package com.freshdirect.webapp.crm;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class CrmLoginFilter implements Filter {

	private FilterConfig filterConfig;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String allowedPage = this.filterConfig.getInitParameter("allowedPage");
		String admDir = this.filterConfig.getInitParameter("adminDir");
		String supDir = this.filterConfig.getInitParameter("supervisorDir");
		String monDir = this.filterConfig.getInitParameter("monitorDir");
		String promoDir = this.filterConfig.getInitParameter("promotionDir");
		String noAuthPage = this.filterConfig.getInitParameter("noAuthPage");

		CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());
		if (agent == null) {
			boolean shouldRedirect = true;
			if (allowedPage != null) {
				String[] aPages = allowedPage.split(";");
				for (String p : aPages) {
					if (request.getRequestURI().equals(p)) {
						// found exception, no redirection required
						shouldRedirect = false;
						break;
					}
				}
			}
			if (shouldRedirect)
				response.sendRedirect("/");
		}
		
		if (request.getRequestURI().indexOf(admDir) >= 0) {
			if (!agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {
				response.sendRedirect(noAuthPage);
				return;
			}
		}
		if (request.getRequestURI().indexOf(supDir) >= 0) {
			if (!agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))
				&& !agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE))
				&& !agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE))) {
				response.sendRedirect(noAuthPage);
				return;
			}
		}
		if (request.getRequestURI().indexOf(monDir) >= 0) {
			if (!agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))
				&& !agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE))
				&& !agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE))
				&& !agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE))) {
				response.sendRedirect(noAuthPage);
				return;
			}
		}
		if (request.getRequestURI().indexOf(promoDir+"/") >= 0 && (request.getRequestURI().indexOf("/promotion/promo_view.jsp")<=-1 && request.getRequestURI().indexOf("/promotion/export_promo_list.jsp")<=-1 && request.getRequestURI().indexOf("/promotion/promo_details.jsp")<=-1)) {
			if (!agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {
				response.sendRedirect(noAuthPage);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
