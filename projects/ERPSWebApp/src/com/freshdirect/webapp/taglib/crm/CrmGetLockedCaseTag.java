package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetLockedCaseTag extends AbstractGetterTag {

	protected Object getResult() throws Exception {
		return CrmSession.getLockedCase(pageContext.getSession());
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.crm.CrmCaseModel";
		}
	}
}
