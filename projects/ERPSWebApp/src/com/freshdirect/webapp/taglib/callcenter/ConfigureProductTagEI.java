/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.jsp.tagext.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ConfigureProductTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("configDesc"),
                           "java.lang.String",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("sku"),
                           "java.lang.String",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("configProduct"),
                           "com.freshdirect.fdstore.customer.FDCartLineModel",
                           true,
                           VariableInfo.NESTED)
		};

    }

}
