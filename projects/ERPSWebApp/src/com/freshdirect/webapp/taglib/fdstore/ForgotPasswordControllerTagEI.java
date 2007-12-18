/*
 * ForgotPasswordControllerEI.java
 *
 * Created on February 26, 2002, 4:56 PM
 */

package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.*;
/**
 *
 * @author  rfalck
 * @version 
 */
public class ForgotPasswordControllerTagEI extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("results"),
                            "com.freshdirect.framework.webapp.ActionResult",
                            true,
                            VariableInfo.NESTED),
             new VariableInfo(data.getAttributeString("password"),
                            "java.lang.String",
                            true,
                            VariableInfo.NESTED)                           
        };
    }
}