package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.layout.ItemGrabberTag;

public class ItemGrabberTagWrapper extends TagWrapper {
    public ItemGrabberTagWrapper(SessionUser user) {
        super(new ItemGrabberTag(), user.getFDSessionUser());
    }

    private String id;

    @Override
    protected Object getResult() throws FDException {
        return this.pageContext.getAttribute(id);
    }

    public List getProducts(ContentNodeModel currentFolder) throws FDException {
        addExpectedRequestValues(new String[] { id }, new String[] { id });
        ((ItemGrabberTag) this.wrapTarget).setId(id);
        ((ItemGrabberTag) this.wrapTarget).setFilterDiscontinued(true);
        ((ItemGrabberTag) this.wrapTarget).setReturnSkus(false);
        ((ItemGrabberTag) this.wrapTarget).setDepth(1000);
        ((ItemGrabberTag) this.wrapTarget).setCategory(currentFolder);
        ((ItemGrabberTag) this.wrapTarget).setIgnoreDuplicateProducts(true);

        try {
            wrapTarget.doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        return (List) getResult();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
