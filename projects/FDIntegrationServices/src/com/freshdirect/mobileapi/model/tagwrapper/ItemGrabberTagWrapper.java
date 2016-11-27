package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.layout.ItemGrabberTag;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;

public class ItemGrabberTagWrapper extends TagWrapper {
    
    public ItemGrabberTagWrapper(SessionUser user) {
        super(new ItemGrabberTag(), user.getFDSessionUser());
    }
    public ItemGrabberTagWrapper(FDUserI user) {
        super(new ItemGrabberTag(), user);
    }

    @Override
    protected Object getResult() throws FDException {
        return this.pageContext.getAttribute(GET_RESULT);
    }

    public List getProducts(Settings lms, ContentNodeModel currentFolder) throws FDException {
        ((ItemGrabberTag) this.wrapTarget).setId(GET_RESULT);
        addExpectedRequestValues(new String[] { GET_RESULT }, new String[] { GET_RESULT });
        ((ItemGrabberTag) this.wrapTarget).setCategory(currentFolder);
        ((ItemGrabberTag) this.wrapTarget).setDepth(lms.getGrabberDepth());
        ((ItemGrabberTag) this.wrapTarget).setIgnoreShowChildren(lms.isIgnoreShowChildren());
        ((ItemGrabberTag) this.wrapTarget).setFilterDiscontinued(lms.isFilterDiscontinued());
        ((ItemGrabberTag) this.wrapTarget).setReturnHiddenFolders(lms.isReturnHiddenFolders());
        ((ItemGrabberTag) this.wrapTarget).setIgnoreDuplicateProducts(lms.isIgnoreDuplicateProducts());
        ((ItemGrabberTag) this.wrapTarget).setReturnSecondaryFolders(lms.isReturnSecondaryFolders());
        //((ItemGrabberTag) this.wrapTarget).setReturnSkus(lms.isReturnSkus());        
        ((ItemGrabberTag) this.wrapTarget).setReturnSkus(false); //Override so that we return products always
        try {
            wrapTarget.doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
        return (List) getResult();
    }
}
