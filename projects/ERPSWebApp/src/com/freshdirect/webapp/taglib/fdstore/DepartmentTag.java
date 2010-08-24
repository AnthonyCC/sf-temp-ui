package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class DepartmentTag extends AbstractGetterTag<ContentNodeModel> {

	private static final long serialVersionUID = 3814269255327865319L;
	
	private String departmentId;

    public void setDepartmentId(String deptId) {
        this.departmentId = deptId;
    }
    
	protected ContentNodeModel getResult() throws FDResourceException {
		return  ContentFactory.getInstance().getContentNode( this.departmentId );
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
