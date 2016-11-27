package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.dlvpass.DlvPassAvailabilityControllerTag;

public class DlvPassAvailabilityControllerTagWrapper extends ControllerTagWrapper implements RequestParamName, SessionParamName {

    public DlvPassAvailabilityControllerTagWrapper(DlvPassAvailabilityControllerTag wrapTarget, SessionUser user) {
        super(wrapTarget, user);
    }

    /**
     * @param sessionUser
     */
    public DlvPassAvailabilityControllerTagWrapper(SessionUser sessionUser) {
        this(new DlvPassAvailabilityControllerTag(), sessionUser);
    }

    /**
     * @return
     * @throws FDException
     */
    public ResultBundle checkDeliveryPassAvailability() throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, }, new String[] { SESSION_PARAM_USER, }); //gets,sets
        addExpectedRequestValues(new String[] {}, new String[] {});//gets,sets
        setMethodMode(false);
        ActionResult actionResult = executeTagLogic();

        List<DlvPassAvailabilityInfo> unavailableList = (List<DlvPassAvailabilityInfo>) getResult();
        if (null != unavailableList) {
            for (DlvPassAvailabilityInfo unavailableItem : unavailableList) {
                actionResult.addError(new ActionError(unavailableItem.getKey().toString(), unavailableItem.getReason()));
            }
        }

        return new ResultBundle(actionResult, this);
    }

}
