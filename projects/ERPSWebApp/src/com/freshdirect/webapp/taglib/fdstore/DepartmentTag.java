package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class DepartmentTag extends AbstractGetterTag<ContentNodeModel> {

    private static final long serialVersionUID = 3814269255327865319L;

    private String departmentId;

    public void setDepartmentId(String deptId) {
        this.departmentId = deptId;
    }

	@Override
    protected ContentNodeModel getResult() throws FDResourceException {
        ContentNodeModel res = ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, this.departmentId);
        if (res == null) {
            res = ContentFactory.getInstance().getContentNode(FDContentTypes.RECIPE_DEPARTMENT, this.departmentId);
        }
        PopulatorUtil.isNodeNotFound(res, "id:" + departmentId);
        return res;
    }

    public static class TagEI extends TagExtraInfo {

        @Override
        public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] { new VariableInfo(data.getAttributeString("id"), "com.freshdirect.storeapi.content.ContentNodeModel", true, VariableInfo.AT_END) };
        }

    }

}
