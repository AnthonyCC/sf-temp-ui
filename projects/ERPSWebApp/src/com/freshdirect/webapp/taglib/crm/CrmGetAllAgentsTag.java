package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.crm.CrmManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetAllAgentsTag extends AbstractGetterTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6727768147461970837L;
	private boolean useCache = true;

	public void setUseCache(boolean b) {
		this.useCache = b;
	}

	protected Object getResult() throws Exception {
		//return CrmManager.getInstance().getAllAgents(useCache);
		return CrmManager.getInstance().getAllAgentsFromLDAP(useCache);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.HashMap";
		}
	}
}
