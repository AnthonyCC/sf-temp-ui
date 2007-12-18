package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetAgentTag extends AbstractGetterTag {

	private static final String AGENT_SESSION_NAME = "EDITING_AGENT";
	private String agentId;

	public void setAgentId(String agentID) {
		this.agentId = agentID;
	}

	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = (CrmAgentModel) session.getAttribute(AGENT_SESSION_NAME);
		if (agent == null || !agent.getPK().getId().equalsIgnoreCase(this.agentId)) {
			try{
				agent = CrmManager.getInstance().getAgentByPk(this.agentId);
				session.setAttribute(AGENT_SESSION_NAME, agent);
			}catch(FDResourceException e){
				throw new JspException(e);
			}
		}
		return agent;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.crm.CrmAgentModel";
		}
	}

}
