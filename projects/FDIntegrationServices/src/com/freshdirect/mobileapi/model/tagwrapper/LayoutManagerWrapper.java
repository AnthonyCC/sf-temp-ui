package com.freshdirect.mobileapi.model.tagwrapper;

 import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;

public class LayoutManagerWrapper extends TagWrapper implements RequestParamName {

    private static final String LAYOUT_NAME = "LAYOUT_NAME";

    public LayoutManagerWrapper(SessionUser user) {
        super(new LayoutManager(), user.getFDSessionUser());
    }

    private String id;

    @Override
    public Object getResult() throws FDException {
        return getPageContextAttribute(id);
    }

    public Settings getLayoutManagerSettings(ContentNodeModel currentFolder) throws FDException {
        Settings layoutManagerSetting = null;
        addExpectedRequestValues(new String[] { id, LAYOUT_NAME, REQ_PARAM_SORTBY, REQ_PARAM_GROCERY_VIRTUAL, REQ_PARAM_SORT_DESCENDING },
                new String[] { id, LAYOUT_NAME });
        boolean isDept = (currentFolder instanceof DepartmentModel);
        boolean isCat = (currentFolder instanceof CategoryModel);

        ((LayoutManager) this.wrapTarget).setIsDepartment(isDept);
        ((LayoutManager) this.wrapTarget).setIsCategory(isCat);
        ((LayoutManager) this.wrapTarget).setCurrentNode(currentFolder);
        ((LayoutManager) this.wrapTarget).setLayoutSettingsName(LAYOUT_NAME);

        try {
            wrapTarget.doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        ActionResult result = (ActionResult) getResult();
        if (result.isSuccess()) {
            layoutManagerSetting = (Settings) getPageContextAttribute(LAYOUT_NAME);
        }
        return layoutManagerSetting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
