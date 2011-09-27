package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class ContentTreeTagEI extends TagExtraInfo {
    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {
        List variables = new ArrayList();
        if (data.getAttributeString("depthName") != null) {
            variables.add(new VariableInfo(data.getAttributeString("depthName"), "java.lang.Integer", true, VariableInfo.NESTED));
        }
        if (data.getAttributeString("nextDepthName") != null) {
            variables.add(new VariableInfo(data.getAttributeString("nextDepthName"), "java.lang.Integer", true, VariableInfo.NESTED));
        }
        if (data.getAttributeString("childCountName") != null) {
            variables.add(new VariableInfo(data.getAttributeString("childCountName"), "java.lang.Integer", true, VariableInfo.NESTED));
        }
        if (data.getAttributeString("selectedName") != null) {
            variables.add(new VariableInfo(data.getAttributeString("selectedName"), "java.lang.Boolean", true, VariableInfo.NESTED));
        }
        if (data.getAttributeString("contentNodeName") != null) {
            variables.add(new VariableInfo(data.getAttributeString("contentNodeName"), "com.freshdirect.fdstore.content.ContentNodeModel", true,
                    VariableInfo.NESTED));
        }
        return (VariableInfo[]) variables.toArray(new VariableInfo[variables.size()]);

    }

}
