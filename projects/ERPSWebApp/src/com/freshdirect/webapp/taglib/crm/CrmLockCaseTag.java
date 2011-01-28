package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.fdstore.FDResourceException;

public class CrmLockCaseTag extends TagSupport {

	private CrmCaseModel cm;

	public void setCase(CrmCaseModel cm) {
		this.cm = cm;
	}

	public int doStartTag() throws JspException {
		HttpSession sess = pageContext.getSession();
		try {
			CrmSession.setLockedCase(sess, cm);
		} catch (FDResourceException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

}
