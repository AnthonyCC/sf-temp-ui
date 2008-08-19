/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class DepartmentTag extends AbstractGetterTag {

    private String departmentId;

    public void setDepartmentId(String deptId) {
        this.departmentId = deptId;
    }
    
	protected Object getResult() throws FDResourceException {
		//System.out.println("!!!! Department id ="+this.departmentId);
		return  ContentFactory.getInstance().getContentNodeByName( this.departmentId );
	}

	public static class TagEI extends TagExtraInfo {

		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					"com.freshdirect.fdstore.content.ContentNodeModel",
					true,
					VariableInfo.AT_END)
			};
		}

	}

}
