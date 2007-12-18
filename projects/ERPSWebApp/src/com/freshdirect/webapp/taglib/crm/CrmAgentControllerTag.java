package com.freshdirect.webapp.taglib.crm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmAgentControllerTag extends AbstractControllerTag {

	private CrmAgentModel agent;
	private String verifyPassword;
	private String password;

	public void setAgent(CrmAgentModel agent) {
		this.agent = agent;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			if ("create_user".equalsIgnoreCase(this.getActionName())) {
				this.populateAgentModel(request);
				this.agent.setPassword(this.password);
				this.validateAgent(actionResult);
				if (!actionResult.isSuccess()) {
					return true;
				}
				CrmManager.getInstance().createAgent(this.agent, CrmSession.getCurrentAgent(pageContext.getSession()).getPK()); 
			}
			if ("update_user".equalsIgnoreCase(this.getActionName())) {
				this.populateCommon(request);
				if (!"".equals(this.verifyPassword) || !"".equals(password)) {
					this.validatePassword(actionResult);
				}
	
				if (!actionResult.isSuccess()) {
					return true;
				}
				if (!"".equals(this.password)) {
					this.agent.setPassword(this.password);
				}
				
				CrmManager.getInstance().updateAgent(this.agent, CrmSession.getCurrentAgent(pageContext.getSession()).getPK());
			}
			return true;
		}catch (FDResourceException e) {
			throw new JspException(e);
		} catch(CrmAuthorizationException e){
			actionResult.setError(e.getMessage());
			return true;
		} catch(ErpDuplicateUserIdException e){
			actionResult.setError(e.getMessage());
			return true;
		}
	}

	private void populateAgentModel(HttpServletRequest request) {
		this.populateCommon(request);
		this.agent.setUserId(NVL.apply(request.getParameter("user_id"), "").trim());
		this.agent.setFirstName(NVL.apply(request.getParameter("first_name"), "").trim());
		this.agent.setLastName(NVL.apply(request.getParameter("last_name"), "").trim());
	}

	private void populateCommon(HttpServletRequest request) {
		this.password = NVL.apply(request.getParameter("password"), "").trim();
		this.verifyPassword = NVL.apply(request.getParameter("verify_password"), "").trim();
		this.agent.setRole(CrmAgentRole.getEnum(request.getParameter("role")));
		this.agent.setActive(request.getParameter("active") != null);
		List agentQueues = new ArrayList();
		String[] queuesValues = request.getParameterValues("queues");
		if (queuesValues != null) {
			for (int i = 0; i < queuesValues.length; i++) {
				String value = queuesValues[i];
				agentQueues.add(CrmCaseQueue.getEnum(value));
			}
		}
		agent.setAgentQueues(agentQueues);
	}

	private void validateAgent(ActionResult result) {
		result.addError("".equals(this.agent.getUserId()), "user_id", "required");
		result.addError("".equals(this.agent.getFirstName()), "first_name", "required");
		result.addError("".equals(this.agent.getLastName()), "last_name", "required");
		result.addError(this.agent.getRole() == null, "role", "required");
		this.validatePassword(result);
	}

	private void validatePassword(ActionResult result) {
		result.addError("".equals(this.password), "password", "required");
		result.addError("".equals(this.verifyPassword), "verify_password", "required");
		result.addError(!this.verifyPassword.equals(this.password), "verify_password", "passwords must match");
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
