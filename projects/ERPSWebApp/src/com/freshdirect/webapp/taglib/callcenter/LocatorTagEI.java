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
public class LocatorTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     *
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("results"),
            				 "java.util.List",
            				 true,
            				 VariableInfo.NESTED)
        };

    }

} // class LocatorTagEI
