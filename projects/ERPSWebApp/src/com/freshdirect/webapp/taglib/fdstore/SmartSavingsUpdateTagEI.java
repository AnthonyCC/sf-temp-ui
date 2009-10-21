package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class SmartSavingsUpdateTagEI extends TagExtraInfo {

    @Override
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
                new VariableInfo("smartSavingVariantId", "java.lang.String", true, VariableInfo.AT_BEGIN)                
        };
    }
}
