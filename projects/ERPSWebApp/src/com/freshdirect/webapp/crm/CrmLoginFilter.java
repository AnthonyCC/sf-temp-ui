package com.freshdirect.webapp.crm;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.crm.security.MenuManager;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.crm.CrmSessionStatus;

public class CrmLoginFilter implements Filter {
	
	private static final Category LOGGER = LoggerFactory.getInstance(CrmLoginFilter.class);

	private FilterConfig filterConfig;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String userRole = CrmSecurityManager.getUserRole(request);
		if(null != request.getRemoteUser() && null == CrmSession.getCurrentAgentStr(request.getSession())){
			try {
				CrmSession.setCurrentAgentStr(request.getSession(), request.getRemoteUser());
				
				CrmAgentRole crmRole = CrmAgentRole.getEnumByLDAPRole(userRole);
				CrmSession.setCurrentAgentRole(request.getSession(), crmRole);
				CrmStatus status = CrmManager.getInstance().getSessionStatus(request.getRemoteUser());
				if(status == null || status.getAgentId() == null){
					status = new CrmStatus(request.getRemoteUser());
				}
				CrmSessionStatus sessStatus = new CrmSessionStatus(status, request.getSession());
				CrmSession.setSessionStatus(request.getSession(), sessStatus);
				String redirectUrl = sessStatus.getRedirectUrl();
				if(null ==redirectUrl){
					redirectUrl="/main/main_index.jsp";
					if(CrmAgentRole.OPS_CODE.equals(crmRole.getCode())||CrmAgentRole.SOP_CODE.equals(crmRole.getCode())){
						redirectUrl="/transportation/crmLateIssues.jsp?lateLog=true";
					}
				}
				
				if(null !=redirectUrl){
					response.sendRedirect(redirectUrl);
					return;
				}
			} catch (FDResourceException e) {
				throw new ServletException(e.getMessage());
			}
		}
		String noAuthPage = this.filterConfig.getInitParameter("noAuthPage");
		String rootUri =  request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length());
		if(!CrmSecurityManager.hasAccessToPage(request, rootUri)){
			LOGGER.info("Role:"+userRole+" Access Denied Resource:"+rootUri);
			response.sendRedirect(noAuthPage);
			return;
		}
		
		/*String allowedPage = this.filterConfig.getInitParameter("allowedPage");
		String admDir = this.filterConfig.getInitParameter("adminDir");
		String supDir = this.filterConfig.getInitParameter("supervisorDir");
		String monDir = this.filterConfig.getInitParameter("monitorDir");
		String promoDir = this.filterConfig.getInitParameter("promotionDir");
		String noAuthPage = this.filterConfig.getInitParameter("noAuthPage");*/
		
		
/*		if(!linksList.contains("case_mgmt_index.jsp")){
			response.sendRedirect(noAuthPage);
			return;
		}

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
			if(request.getRequestURI().indexOf("/promotion/promo_ws_create.jsp") > -1 || request.getRequestURI().indexOf("/promotion/promo_ws_view.jsp") > -1){
				if (!agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE)) && !agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {
					response.sendRedirect(noAuthPage);
					return;
				}
			} else {
				if (!agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {
					response.sendRedirect(noAuthPage);
					return;
				}
			}
		}*/
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
