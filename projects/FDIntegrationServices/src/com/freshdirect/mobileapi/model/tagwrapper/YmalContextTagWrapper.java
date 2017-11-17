package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.smartstore.YmalContextTag;

/**
 * @author Rob
 *
 */
public class YmalContextTagWrapper extends GetterTagWrapper implements RequestParamName {

    public YmalContextTagWrapper(SessionUser user) {
        super(new YmalContextTag(), user);
    }

    public YmalSource getContext() throws FDException {
        addExpectedRequestValues(new String[] { REQ_PARAM_YMAL_RESET }, new String[] { REQ_PARAM_YMAL_RESET });//gets,sets
        return (YmalSource) getResult();
    }

}
