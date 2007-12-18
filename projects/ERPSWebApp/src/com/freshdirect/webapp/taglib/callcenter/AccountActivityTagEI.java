/*
 * AtpControllerTagEI.java
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
public class AccountActivityTagEI extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("activities"),
                            "java.util.List",
                            true,
                            VariableInfo.NESTED)
        };
    }
}