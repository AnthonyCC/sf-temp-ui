/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.erp;

import javax.servlet.jsp.tagext.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CharValuePriceTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("id"),
                           "com.freshdirect.erp.model.ErpCharacteristicValuePriceModel",
                           true,
                           VariableInfo.NESTED)
        };

    }

}
