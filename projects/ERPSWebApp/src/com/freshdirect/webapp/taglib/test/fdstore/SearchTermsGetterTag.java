package com.freshdirect.webapp.taglib.test.fdstore;


import java.io.IOException;
import java.util.Collections;

import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SearchTermsGetterTag extends AbstractGetterTag implements SessionName {

	private static final long serialVersionUID = -2735377709584542816L;

	protected Object getResult()  {
		try {
			return DistributionHelper.getSearhTerms();
		} catch (IOException e) {
			return Collections.EMPTY_LIST;
		}
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
}
