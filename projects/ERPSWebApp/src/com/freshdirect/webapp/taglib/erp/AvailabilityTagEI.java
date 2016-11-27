/*
 * AvailabilityTagEI.java
 *
 * Created on March 4, 2002, 11:40 AM
 */

package com.freshdirect.webapp.taglib.erp;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.servlet.jsp.tagext.*;

public class AvailabilityTagEI extends TagExtraInfo{

	public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("id"), "com.freshdirect.erp.model.ErpInventoryModel", true, VariableInfo.NESTED)
        };

    }

}
