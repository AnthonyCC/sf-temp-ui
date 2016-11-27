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
public class GetCustomerObjectsTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("fdCustomerId"),
                            "com.freshdirect.fdstore.customer.FDCustomerModel",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("erpCustomerId"),
                            "com.freshdirect.customer.ErpCustomerModel",
                            true,
                            VariableInfo.NESTED),
            new VariableInfo(data.getAttributeString("erpCustomerInfoId"),
                            "com.freshdirect.customer.ErpCustomerInfoModel",
                            true,
                            VariableInfo.NESTED)
        };

    }

}
