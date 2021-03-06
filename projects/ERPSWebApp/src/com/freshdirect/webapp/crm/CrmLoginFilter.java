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
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.crm.CrmSessionStatus;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmLoginFilter implements Filter {

	private static final Category LOGGER = LoggerFactory.getInstance(CrmLoginFilter.class);
	private FilterConfig filterConfig;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		

		String ldapRole = CrmSecurityManager.getUserRole(request);
		CrmAgentRole agentRole = CrmAgentRole.getEnumByLDAPRole(ldapRole);
		CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());
		FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
		
		try {
			if(agent!=null && CrmManager.getInstance().isCRMRestrictionEnabled() && CrmManager.getInstance().isCRMRestrictedForAgent(agent.getLdapId()) && !request.getRequestURI().contains("restricted.jsp")) {
				String _redirectUrl = "/restricted.jsp";
				response.sendRedirect(_redirectUrl);
				return;
			}
		} catch (FDResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(user !=null){
			ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || user.isEligibleForDDPP());
		}
		CrmStatus status = null;
		if(null != request.getRemoteUser() && null == agent && null !=ldapRole){
			try {
				try {
					agent = CrmManager.getInstance().getAgentByLdapId(request.getRemoteUser());
					if(null != agent && null != agentRole && !agentRole.equals(agent.getRole())){
						agent = updateAgentsRole(agentRole, agent);
						//To update the cache for this agent.
						CrmManager.getInstance().forceRefresh();
					}
				} catch (CrmAuthenticationException e) {
					agent = createAgentByLdapId(request, agentRole);
					//To update the cache with for this agent.
					CrmManager.getInstance().forceRefresh();
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
			try {
				if(CrmManager.getInstance().isCRMRestrictionEnabled() && CrmManager.getInstance().isCRMRestrictedForAgent(agent.getLdapId())) {
					redirectUrl = "/restricted.jsp";
					response.sendRedirect(redirectUrl);
					return;
				}
			} catch (FDResourceException e) {
				throw new ServletException(e.getMessage());
			}
			request.getSession().setAttribute(SessionName.APPLICATION, "CALLCENTER");
			if(null ==redirectUrl){
				redirectUrl="/main/main_index.jsp";
				if(CrmAgentRole.OPS_CODE.equals(agentRole.getCode())||CrmAgentRole.SOP_CODE.equals(agentRole.getCode())){
					//redirectUrl="/transportation/crmLateIssues.jsp?lateLog=true";
					redirectUrl="/transportation/VSStatusLog.jsp";
				} else if(CrmAgentRole.HR_CODE.equals(agentRole.getCode())) {
					redirectUrl = "/promotion/promo_hronly.jsp";
				}
			}
			
			if(null !=redirectUrl){
				response.sendRedirect(redirectUrl);
				return;
			}
		}
		String noAuthPage = this.filterConfig.getInitParameter("noAuthPage");
		if(null != request.getRemoteUser() && null==ldapRole){
			LOGGER.info("**** No matching role found for the user:"+request.getRemoteUser()+ " to access:"+request.getRequestURI());			
		}		
		String rootUri =  request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length());
		if(!rootUri.equals("restricted.jsp")&&!CrmSecurityManager.hasAccessToPage(request, rootUri) && !CrmSecurityManager.hasAccessToPage(request)){
			LOGGER.info("**** Role:"+ldapRole+" Access Denied Resource:"+request.getRequestURI());
			response.sendRedirect(noAuthPage);
			return;
		}
		filterChain.doFilter(request, response);
	}

	private CrmAgentModel updateAgentsRole(CrmAgentRole agentRole, CrmAgentModel agent)
			throws FDResourceException{
		agent.setRole(agentRole);
		agent.setActive(true);
		try {
			CrmManager.getInstance().updateAgent(agent, null);
		}  catch (CrmAuthorizationException e) {
			throw new FDResourceException(e.getMessage());
		}
		return agent;
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
