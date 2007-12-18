/*
 * Class.java
 *
 * Created on October 30, 2001, 5:28 PM
 */

package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.jsp.tagext.*;

/**
 *
 * @author  rgayle
 * @version
 */
public class ComplaintCreatorTagEI extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("result"),
                            "com.freshdirect.framework.webapp.ActionResult",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("newComplaint"),
                            "com.freshdirect.customer.ErpComplaintModel",
                            true,
                            VariableInfo.NESTED),
           new VariableInfo(data.getAttributeString("emailPreview"),
							"com.freshdirect.framework.mail.XMLEmailI",
							true,
							VariableInfo.NESTED)
        };
    }
}