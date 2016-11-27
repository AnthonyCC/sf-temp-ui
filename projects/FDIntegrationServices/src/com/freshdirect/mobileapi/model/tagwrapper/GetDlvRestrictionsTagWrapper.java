package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.GetDlvRestrictionsTag;

public class GetDlvRestrictionsTagWrapper extends GetterTagWrapper {

    public GetDlvRestrictionsTagWrapper(FDUserI user) {
        super(new GetDlvRestrictionsTag(), user);
    }

    public List<RestrictionI> getRestrictions(EnumDlvRestrictionReason reason) throws FDException {        
        ((GetDlvRestrictionsTag)wrapTarget).setReason(reason);
        return (List<RestrictionI>) getResult();
    }

}
