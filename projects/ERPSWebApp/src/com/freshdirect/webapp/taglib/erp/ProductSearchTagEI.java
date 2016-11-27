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
public class ProductSearchTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("results"),
                           "java.util.Collection",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo("searchterm",
                            "java.lang.String",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("searchtype",
                            "java.lang.String",
                            true,
                            VariableInfo.NESTED)              
        };

    }

}
