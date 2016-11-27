package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmFindCasesTag extends AbstractGetterTag {

	private CrmCaseTemplate template;

	public void setTemplate(CrmCaseTemplate template) {
		this.template = template;
	}

	protected Object getResult() throws Exception {
		return CrmSession.findCases(this.pageContext.getSession(), template);

	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
}
