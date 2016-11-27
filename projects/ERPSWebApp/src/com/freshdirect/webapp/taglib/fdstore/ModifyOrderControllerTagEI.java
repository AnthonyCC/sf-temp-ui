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
public class ModifyOrderControllerTagEI extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("result"),
                            "com.freshdirect.framework.webapp.ActionResult",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("allowNewCharges",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("allowComplaint",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("allowCancelOrder",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("allowModifyOrder",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("allowReturnOrder",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED),
			new VariableInfo("isRefusedOrder",
							"java.lang.Boolean",
							true,
							VariableInfo.NESTED),
            new VariableInfo("allowResubmitOrder",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo("hasPaymentException",
                            "java.lang.Boolean",
                            true,
                            VariableInfo.NESTED)
        };
    }
}