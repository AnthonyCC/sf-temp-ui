package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ContentNodeModel;

public class ContentTreeTagEI extends TagExtraInfo {
	/**
	 * Return information about the scripting variables to be created.
	 */
	public VariableInfo[] getVariableInfo(TagData data) {
		List<VariableInfo> variables = new ArrayList<VariableInfo>();
		if (data.getAttributeString("depthName") != null) {
			variables.add(new VariableInfo(data.getAttributeString("depthName"), Integer.class.getName(), true, VariableInfo.NESTED));
		}
		if (data.getAttributeString("nextDepthName") != null) {
			variables.add(new VariableInfo(data.getAttributeString("nextDepthName"), Integer.class.getName(), true, VariableInfo.NESTED));
		}
		if (data.getAttributeString("childCountName") != null) {
			variables.add(new VariableInfo(data.getAttributeString("childCountName"), Integer.class.getName(), true, VariableInfo.NESTED));
		}
		if (data.getAttributeString("selectedName") != null) {
			variables.add(new VariableInfo(data.getAttributeString("selectedName"), Boolean.class.getName(), true, VariableInfo.NESTED));
		}
		if (data.getAttributeString("contentNodeName") != null) {
			variables.add(new VariableInfo(data.getAttributeString("contentNodeName"), ContentNodeModel.class.getName(), true, VariableInfo.NESTED));
		}
		return (VariableInfo[]) variables.toArray(new VariableInfo[variables.size()]);

	}

}
