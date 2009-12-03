package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetDlvRestrictionsTagWrapper extends GetterTagWrapper {

    public GetDlvRestrictionsTagWrapper(AbstractGetterTag wrapTarget) {
        super(wrapTarget);
    }

    public List<RestrictionI> getRestrictions() throws FDException {
        return (List<RestrictionI>) getResult();
    }

}
