package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.DeliveryTimeSlotTag;
import com.freshdirect.webapp.taglib.fdstore.Result;

public class DeliveryTimeSlotTagWrapper extends GetterTagWrapper {

    public DeliveryTimeSlotTagWrapper(SessionUser user) {
        super(new DeliveryTimeSlotTag(), user);
    }

    public Result getDeliveryTimeSlotResult(ErpAddressModel address) throws FDException {
        ((DeliveryTimeSlotTag)wrapTarget).setAddress(address);
        ((DeliveryTimeSlotTag)wrapTarget).setDeliveryInfo(true);
        
        return (Result) getResult();
    }

}
