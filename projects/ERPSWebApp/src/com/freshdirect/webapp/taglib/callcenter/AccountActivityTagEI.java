package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.jsp.tagext.*;


public class AccountActivityTagEI extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("activities"),
                            "java.util.List<ErpActivityRecord>",
                            true,
                            VariableInfo.NESTED)
        };
    }
}