package com.freshdirect.webapp.taglib.crm;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;

public class CrmLockCaseTag extends TagSupport {

	private CrmCaseModel cm;

	public void setCase(CrmCaseModel cm) {
		this.cm = cm;
	}

	public int doStartTag() throws JspException {
		HttpSession sess = pageContext.getSession();
		ServletRequest request = pageContext.getRequest();
		String agentId = CrmSecurityManager.getUserName(request);
		try {
			CrmSession.setLockedCase(sess, cm, agentId);
		} catch (FDResourceException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

}
