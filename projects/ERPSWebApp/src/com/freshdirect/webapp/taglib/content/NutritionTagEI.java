/*
 * NutritionEI.java
 *
 * Created on September 13, 2001, 11:31 AM
 */

package com.freshdirect.webapp.taglib.content;

/**
 *
 * @author  knadeem
 * @version 
 */

import javax.servlet.jsp.tagext.*;

public class NutritionTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

	return new VariableInfo[] {
	    new VariableInfo(data.getAttributeString("id"),
	    "com.freshdirect.content.nutrition.ErpNutritionModel",
	    true,
	    VariableInfo.NESTED)
	    
	};
	
    }

}
