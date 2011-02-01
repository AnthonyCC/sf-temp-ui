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

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.crm.CrmSessionStatus;

public class CrmLoginFilter implements Filter {

	private static final Category LOGGER = LoggerFactory.getInstance(CrmLoginFilter.class);
	private FilterConfig filterConfig;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		

		String ldapRole = CrmSecurityManager.getUserRole(request);
		CrmAgentRole agentRole = CrmAgentRole.getEnumByLDAPRole(ldapRole);
		CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());
		CrmStatus status = null;
		if(null != request.getRemoteUser() && null == agent && null !=ldapRole){
			try {
				try {
					agent = CrmManager.getInstance().getAgentByLdapId(request.getRemoteUser());				
				} catch (CrmAuthenticationException e) {
					agent = createAgentByLdapId(request, agentRole);
				}
				CrmSession.setCurrentAgent(request.getSession(), agent);
				status = CrmManager.getInstance().getSessionStatus(agent.getPK());
			} catch (FDResourceException e) {
				throw new ServletException(e.getMessage());
			} 
			
			if(status == null){
				status = new CrmStatus(agent.getPK());
			}
			CrmSessionStatus sessStatus = new CrmSessionStatus(status, request.getSession());
			CrmSession.setSessionStatus(request.getSession(), sessStatus);
			String redirectUrl = sessStatus.getRedirectUrl();
			if(null ==redirectUrl){
				redirectUrl="/main/main_index.jsp";
				if(CrmAgentRole.OPS_CODE.equals(agentRole.getCode())||CrmAgentRole.SOP_CODE.equals(agentRole.getCode())){
					redirectUrl="/transportation/crmLateIssues.jsp?lateLog=true";
				}
			}
			
			if(null !=redirectUrl){
				response.sendRedirect(redirectUrl);
				return;
			}
		}
		String noAuthPage = this.filterConfig.getInitParameter("noAuthPage");
		if(null != request.getRemoteUser() && null==ldapRole){
			LOGGER.info("**** No matching role found for the user:"+request.getRemoteUser()+ "to access:"+request.getRequestURI());			
		}		
		String rootUri =  request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length());
		if(!CrmSecurityManager.hasAccessToPage(request, rootUri) && !CrmSecurityManager.hasAccessToPage(request)){
			LOGGER.info("**** Role:"+ldapRole+" Access Denied Resource:"+request.getRequestURI());
			response.sendRedirect(noAuthPage);
			return;
		}
		filterChain.doFilter(request, response);
	}

	private CrmAgentModel createAgentByLdapId(HttpServletRequest request,
			CrmAgentRole agentRole) throws FDResourceException
			{
		CrmAgentModel agent = null;
		try {			
			agent =populateAgentModel(request,agentRole);
			PrimaryKey agentPk = CrmManager.getInstance().createAgent(agent, null);
			agent.setPK(agentPk);
		} catch (ErpDuplicateUserIdException e) {
			throw new FDResourceException(e.getMessage());
		} catch (CrmAuthorizationException e) {
			throw new FDResourceException(e.getMessage());
		}
		return agent;
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
	
	private CrmAgentModel populateAgentModel(HttpServletRequest request,CrmAgentRole agentRole) {
		CrmAgentModel agent = new CrmAgentModel();
		agent.setFirstName(request.getRemoteUser());
		agent.setLastName(request.getRemoteUser());
		agent.setUserId(request.getRemoteUser());
		agent.setRole(agentRole);
		agent.setPassword(request.getRemoteUser());
		agent.setActive(true);
		agent.setLdapId(request.getRemoteUser());
		return agent;
	}
	
}
