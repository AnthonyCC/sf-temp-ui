package com.freshdirect.webapp.taglib.smartstore;

import java.util.Set;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * SmartStore YMAL Context Tag
 * 
 * @author csongor
 * 
 */
public class YmalContextTag extends AbstractGetterTag implements SessionName {
	private static final long serialVersionUID = 5976696010559642821L;
	
	private Set excludedSkus;

	public void setExcludedSkus(Set excludedSkus) {
		this.excludedSkus = excludedSkus;
	}
	
	protected Object getResult() throws Exception {
		return YmalUtil.resolveYmalSource(
				(FDUserI) pageContext.getSession().getAttribute(USER),
				excludedSkus);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.fdstore.content.YmalSource";
        }
    }
}
