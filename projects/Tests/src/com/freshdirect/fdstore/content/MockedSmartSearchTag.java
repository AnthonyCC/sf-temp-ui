package com.freshdirect.fdstore.content;

import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;

public class MockedSmartSearchTag extends SmartSearchTag {
	private static final long serialVersionUID = -5741255462791247292L;

	public MockedSmartSearchTag() {
		super();
	}

	@Override
	protected void postProcess(SearchResults results) {
		// null
	}
}
