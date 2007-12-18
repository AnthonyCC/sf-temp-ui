package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
/**
 * @author knadeem
 */
public class CrmLoginControllerTag extends AbstractControllerTag {

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			if ("login".equalsIgnoreCase(this.getActionName())) {

				String userId = request.getParameter("userId");
				String password = request.getParameter("password");

				actionResult.addError(userId == null || userId.length() <= 0, "userId", "required");
				actionResult.addError(password == null || password.length() <= 0, "password", "required");

				if (actionResult.isFailure()) {
					return true;
				}
				
				HttpSession session = pageContext.getSession();
				
				session.setAttribute(SessionName.APPLICATION, "CALLCENTER");
				
				CrmAgentModel agent = CrmManager.getInstance().loginAgent(userId, password);
				CrmSession.setCurrentAgent(session, agent);
				
				CrmStatus status = CrmManager.getInstance().getSessionStatus(agent.getPK());
				if(status == null){
					status = new CrmStatus(agent.getPK());
				}
				
				CrmSessionStatus sessStatus = new CrmSessionStatus(status, session);
				CrmSession.setSessionStatus(session, sessStatus);
				
				String restoreUrl = sessStatus.getRedirectUrl();
				if(restoreUrl != null){
					setSuccessPage(restoreUrl);
				}
			}
			return true;

		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (CrmAuthenticationException e) {
			actionResult.setError(e.getReason());
			return true;
		}
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
	}

}
