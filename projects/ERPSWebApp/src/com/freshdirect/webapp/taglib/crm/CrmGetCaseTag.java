package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.crm.CrmManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetCaseTag extends AbstractGetterTag {

	private String caseId;

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	protected Object getResult() throws Exception {
		return CrmManager.getInstance().getCaseByPk(this.caseId);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.crm.CrmCaseModel";
		}
	}

}
