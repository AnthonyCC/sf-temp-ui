package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.ReserveTimeslotControllerTag;

public class ReserveTimeslotControllerTagWrapper extends ControllerTagWrapper implements RequestParamName, SessionParamName, MessageCodes {

    public static final String ACTION_RESERVE_TIMESLOT = "reserveTimeslot";

    public static final String ACTION_UPDATE_WEEKLY_RESERVATION = "updateWeeklyReservation"; //Not supported

    public static final String ACTION_CHANGE_RESERVATION = "changeReservation"; //Not supported

    public static final String ACTION_CANCEL_RESERVATION = "cancelReservation";

    public ReserveTimeslotControllerTagWrapper(SessionUser sessionUser) {
        super(new ReserveTimeslotControllerTag(), sessionUser);
    }

    public ResultBundle cancelReservation(String addressId, String timeslotId, String reservationType) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] {}); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE }, new String[] {});//gets,sets

        addRequestValue(REQ_PARAM_ADDRESS_ID, addressId);
        addRequestValue(REQ_PARAM_SLOT_ID, timeslotId);
        addRequestValue(REQ_PARAM_RESERVATION_TYPE, reservationType);

        getWrapTarget().setActionName(ACTION_CANCEL_RESERVATION);
        setMethodMode(true);
        ResultBundle result = new ResultBundle(executeTagLogic(), this);
        return result;
    }

    public ResultBundle makeReservation(String addressId, String timeslotId, String reservationType) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] {}); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE }, new String[] {});//gets,sets

        addRequestValue(REQ_PARAM_ADDRESS_ID, addressId);
        addRequestValue(REQ_PARAM_SLOT_ID, timeslotId);
        addRequestValue(REQ_PARAM_RESERVATION_TYPE, reservationType);

        getWrapTarget().setActionName(ACTION_RESERVE_TIMESLOT);
        setMethodMode(true);
        ResultBundle result = new ResultBundle(executeTagLogic(), this);
        return result;
    }

}
