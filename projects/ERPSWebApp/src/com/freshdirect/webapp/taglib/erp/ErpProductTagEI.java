/*
 * ErpProductEI.java
 *
 * Created on September 12, 2001, 7:19 PM
 */

package com.freshdirect.webapp.taglib.erp;

/**
 *
 * @author  knadeem
 * @version 
 */

import javax.servlet.jsp.tagext.*;

public class ErpProductTagEI extends TagExtraInfo {

   /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("id"),
                            "com.freshdirect.erp.model.ErpProductModel",
                            true,
                            VariableInfo.NESTED)
	  
        };

    }
}
