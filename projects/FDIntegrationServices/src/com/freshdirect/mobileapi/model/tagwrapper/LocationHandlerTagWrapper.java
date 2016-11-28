package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.EmailCapture;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.location.LocationHandlerTag;

public class LocationHandlerTagWrapper extends SimpleTagWrapper implements RequestParamName, SessionParamName, SessionName{

    public LocationHandlerTagWrapper(SessionUser user) {
        super(new LocationHandlerTag(), user.getFDSessionUser());
    }

    public ActionResult setFutureZoneNotificationFdx(EmailCapture zipcheck) throws FDException {
        String[] requestProperties = new String[] { LocationHandlerTag.SELECTED_ADDRESS_ATTR, 
                LocationHandlerTag.SELECTED_PICKUP_DEPOT_ID_ATTR, LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR, 
                LocationHandlerTag.DISABLED_ATTR, LocationHandlerTag.SERVICE_TYPE_MODIFICATION_ENABLED,
                LocationHandlerTag.SERVER_ERROR_ATTR, LocationHandlerTag.ACTION_RESULT_ATTR};
        addExpectedRequestValues(requestProperties, requestProperties);

        addRequestValue(EnumUserInfoName.DLV_ZIPCODE.getCode(), zipcheck.getZipCode());
        addRequestValue(EnumUserInfoName.EMAIL.getCode(), zipcheck.getEmail());
        
        ((LocationHandlerTag)wrapTarget).setAction("futureZoneNotificationFdx");
        setMethodMode(true);

        return executeTagLogic(LocationHandlerTag.ACTION_RESULT_ATTR);
    }
}
