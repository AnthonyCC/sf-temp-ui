package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.*;

public class GiftCardBuyerControllerTagEI extends TagExtraInfo {
	public VariableInfo[] getVariableInfo(TagData data) {
		return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("result"),
						"com.freshdirect.framework.webapp.ActionResult",
						true, VariableInfo.NESTED)
	    };
	}
}
