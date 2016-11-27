/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.*;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class SideNavEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     *
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {

            new VariableInfo(data.getAttributeString("navList"),
                "java.util.List", true, VariableInfo.NESTED),
                
            new VariableInfo(data.getAttributeString("unavailableList"),
                "java.util.List", true, VariableInfo.NESTED),
            
            new VariableInfo(data.getAttributeString("topCategory"),
                "com.freshdirect.fdstore.content.CategoryModel", true, VariableInfo.NESTED)

        };

    }

}
