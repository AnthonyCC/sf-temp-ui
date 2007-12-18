package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;

public class CrmCurrentAgentTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private String id;
	
	public void setId(String id){
		this.id = id;
	}
	
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		if(agent == null){
			throw new JspException("Required Agent not Found");
		}
		pageContext.setAttribute(this.id, agent);
		return EVAL_BODY_BUFFERED;
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					"com.freshdirect.crm.CrmAgentModel",
					true,
					VariableInfo.NESTED )
			};
		}
	}
}
