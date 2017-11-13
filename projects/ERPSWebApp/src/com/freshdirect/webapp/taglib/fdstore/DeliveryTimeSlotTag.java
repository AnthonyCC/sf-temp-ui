package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class DeliveryTimeSlotTag extends AbstractGetterTag<Result> {

    private static final long serialVersionUID = 4638621907476643671L;

    private ErpAddressModel address;
    private boolean deliveryInfo = false;
    private boolean returnSameDaySlots = true;

    private TimeslotContext timeSlotContext;

    // selected timeslot ID
    private String timeSlotId = "";

    private boolean forceOrder = false;

    // Flag that indicates generic timeslots will be returned
    private boolean generic = false;

    public void setAddress(ErpAddressModel address) {
        this.address = address;
    }

    public void setDeliveryInfo(boolean deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public void setTimeSlotContext(TimeslotContext timeSlotContext) {
        this.timeSlotContext = timeSlotContext;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public void setReturnSameDaySlots(boolean returnSameDaySlots) {
        this.returnSameDaySlots = returnSameDaySlots;
    }

    /**
     * Used in CRM interface
     * 
     * @param forceOrder
     */
    public void setForceOrder(boolean forceOrder) {
        this.forceOrder = forceOrder;
    }


    /**
     * Tag setter method
     * 
     * @param generic
     */
    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    @Override
    protected Result getResult() throws FDResourceException, ReservationException {
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        boolean isEventLogged = "GET".equalsIgnoreCase(request.getMethod()) || "Y".equals(request.getParameter("addressChange"));
        ErpAddressModel addressModel = null;

        if (StandingOrderHelper.isSO3StandingOrder(user)) {
            addressModel = null != user.getCurrentStandingOrder().getAddressId() ? user.getCurrentStandingOrder().getDeliveryAddress() : null;
            if (null == addressModel) {
                return null;
            }
        } else if (address == null) {
            return null;
        }

        // [APPDEV-2149] go a different way
        if (generic && user.isNewSO3Enabled()) {
            return TimeslotService.defaultService().getGenericTimeslots(addressModel, user, timeSlotContext, isEventLogged);
        } else if (generic) {
            return TimeslotService.defaultService().getGenericTimeslots(address, user, timeSlotContext, isEventLogged);
        }
        return TimeslotService.defaultService().getTimeslot(session, address, user, timeSlotContext, isEventLogged, timeSlotId, forceOrder, deliveryInfo, returnSameDaySlots);
    }

    public static class TagEI extends AbstractGetterTag.TagEI {

        @Override
        protected String getResultType() {
            return Result.class.getName();
        }
    }

}

