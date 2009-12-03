package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.GetDealsSKUTag;

public class GetDealsSKUTagWrapper extends GetterTagWrapper {

    public GetDealsSKUTagWrapper(SessionUser user) {
        super(new GetDealsSKUTag(), user);
    }

    @SuppressWarnings("unchecked")
    public List<SkuModel> getDealsSku() throws FDException {
        return (List) getResult();
    }

}
