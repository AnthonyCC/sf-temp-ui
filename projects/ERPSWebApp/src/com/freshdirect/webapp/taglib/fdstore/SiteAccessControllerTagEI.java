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
public class SiteAccessControllerTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     *
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("result"),
                "com.freshdirect.framework.webapp.ActionResult",true, VariableInfo.NESTED)
        };

    }

}
