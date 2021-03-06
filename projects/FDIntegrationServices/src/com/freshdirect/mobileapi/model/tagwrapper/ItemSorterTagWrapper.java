package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.layout.ItemSorterTag;

public class ItemSorterTagWrapper extends TagWrapper implements RequestParamName {

    private String id;

    public ItemSorterTagWrapper(SessionUser user) {
        super(new ItemSorterTag(), user.getFDSessionUser());
    }

    @Override
    protected Object getResult() throws FDException {
        return this.pageContext.getAttribute(id);
    }

    public void sort(List nodes, List strategy) throws FDException {
        addExpectedRequestValues(new String[] { id, REQ_PARAM_DEBUG }, new String[] { id });
        ((ItemSorterTag) this.wrapTarget).setUser(getUser());
        ((ItemSorterTag) this.wrapTarget).setNodes(nodes);
        ((ItemSorterTag) this.wrapTarget).setStrategy(strategy);

        try {
            wrapTarget.doStartTag();
        } catch (JspException e) {
            throw new FDException(e);
        }
    }

}
