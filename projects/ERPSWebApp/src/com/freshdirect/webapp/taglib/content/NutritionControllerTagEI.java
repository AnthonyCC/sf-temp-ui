/*
 * NutritionControllerEI.java
 *
 * Created on September 13, 2001, 2:34 PM
 */

package com.freshdirect.webapp.taglib.content;

/**
 *
 * @author  knadeem
 * @version
 */
import javax.servlet.jsp.tagext.*;

public class NutritionControllerTagEI extends TagExtraInfo {
    
    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {
        
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("userMessage"),
            "java.lang.String",
            true,
            VariableInfo.AT_END)
        };
    }
}
