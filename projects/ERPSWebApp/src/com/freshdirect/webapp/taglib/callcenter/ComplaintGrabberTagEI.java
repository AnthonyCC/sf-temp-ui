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
public class ComplaintGrabberTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("complaints"),
                           "java.util.Collection",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("lineComplaints"),
                           "java.util.ArrayList",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("deptComplaints"),
                           "java.util.ArrayList",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("miscComplaints"),
                           "java.util.ArrayList",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("fullComplaints"),
                           "java.util.ArrayList",
                           true,
                           VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("restockComplaints"),
                           "java.util.ArrayList",
                           true,
                           VariableInfo.NESTED),
		};

    }

}
