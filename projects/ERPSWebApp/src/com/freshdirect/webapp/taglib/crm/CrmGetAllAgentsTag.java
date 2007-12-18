package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.crm.CrmManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetAllAgentsTag extends AbstractGetterTag {

	private boolean useCache = true;

	public void setUseCache(boolean b) {
		this.useCache = b;
	}

	protected Object getResult() throws Exception {
		return CrmManager.getInstance().getAllAgents(useCache);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.crm.CrmAgentList";
		}
	}
}
