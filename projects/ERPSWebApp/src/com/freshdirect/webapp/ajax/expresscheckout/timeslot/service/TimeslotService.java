package com.freshdirect.webapp.ajax.expresscheckout.timeslot.service;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.webapp.action.fdstore.ChooseTimeslotAction;
import com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service.CoremetricsService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class TimeslotService {

    private static final Logger LOG = LoggerFactory.getInstance(TimeslotService.class);
    private static final String DEFAULT_TIMESLOT_ID = "timeslotId";
    private static final TimeslotService INSTANCE = new TimeslotService();

    private TimeslotService() {
    }

    public static TimeslotService defaultService() {
        return INSTANCE;
    }

    /**
     * Load cart timeslot
     * 
     * @param user Customer object @param cart Cart content @param matchTimeslotAndDlvAddress enforce same-zone check of timeslot and delivery address
     * 
     * @return
     */
    public FormTimeslotData loadCartTimeslot(FDUserI user, final FDCartI cart) {
        FormTimeslotData timeslotData = new FormTimeslotData();
        FDReservation reservation = cart.getDeliveryReservation();
        ErpAddressModel addressModel=null;
        addressModel=cart.getDeliveryAddress();
        ErpAddressModel originalAddress = (cart.getDeliveryReservation()!=null)?cart.getDeliveryReservation().getAddress():null;
        if(StandingOrderHelper.isSO3StandingOrder(user)){
        	StandingOrderHelper.loadSO3CartTimeSlot(timeslotData,user.getCurrentStandingOrder());
        } else if (reservation != null) {
            if (!(cart instanceof FDOrderI)) {
                // Deal with case when timeslot zone does not match
                // the zone of selected address assigned to cart

                // pick timeslot address
                String timeslotAddressId = reservation.getAddressId();
                // pick delivery address
                String dlvAddressId = null;
                if (cart != null && addressModel != null) {
                    dlvAddressId = addressModel.getId();
                }

               if(TimeslotLogic.isAddressChange(originalAddress, addressModel, timeslotAddressId, dlvAddressId)){
                         // timeslot has different address than the selected
                         LOG.warn("Delivery address does not match timeslot address. Discard selected timeslot (ID=" + reservation.getTimeslotId() + ")");

                         return timeslotData;
                    
                }
                		
            }
           
            Date startTime = reservation.getStartTime();
            Calendar startTimeCalendar = DateUtil.toCalendar(startTime);
            String dayNames[] = new DateFormatSymbols().getWeekdays();
            timeslotData.setId(NVL.apply(reservation.getTimeslotId(), DEFAULT_TIMESLOT_ID));
            timeslotData.setYear(String.valueOf(startTimeCalendar.get(Calendar.YEAR)));
            timeslotData.setMonth(String.valueOf(startTimeCalendar.get(Calendar.MONTH) + 1));
            timeslotData.setDayOfMonth(String.valueOf(startTimeCalendar.get(Calendar.DAY_OF_MONTH)));
            timeslotData.setDayOfWeek(dayNames[startTimeCalendar.get(Calendar.DAY_OF_WEEK)]);
            timeslotData.setTimePeriod(format(startTime, reservation.getEndTime()));
            timeslotData.setStartDate(startTime);
            timeslotData.setEndDate(reservation.getEndTime());
        }
        timeslotData.setOnOpenCoremetrics(CoremetricsService.defaultService().getCoremetricsData("timeslot"));
        timeslotData.setShowForceOrder(user.getMasqueradeContext() != null && user.getMasqueradeContext().isForceOrderAvailable() && !user.getMasqueradeContext().isAddOnOrderEnabled());
        timeslotData.setForceOrderEnabled(user.getMasqueradeContext() != null && user.getMasqueradeContext().isForceOrderEnabled());
        
        return timeslotData;
    }

	public List<ValidationError> reserveDeliveryTimeSlot(FormDataRequest timeslotRequestData, FDUserI user, HttpSession session) throws FDResourceException {
        String deliveryTimeSlotId = FormDataService.defaultService().get(timeslotRequestData, "deliveryTimeslotId");  
        String soFirstDate = FormDataService.defaultService().get(timeslotRequestData, "soFirstDate");        
        try {
            return reserveDeliveryTimeslot(deliveryTimeSlotId, session, soFirstDate);
        } catch (ReservationException e) {
            LOG.error(MessageFormat.format("Failed to reserve timeslot for timeslot id[{0}]:", deliveryTimeSlotId), e);
            throw new FDResourceException(e);
        }
    }

    public void applyPreReservedDeliveryTimeslot(HttpSession session) throws FDResourceException {
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        FDCartModel cart = user.getShoppingCart();
        if (cart.getDeliveryReservation() == null) {
            FDReservation reservation = user.getReservation();
            if (reservation != null && cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getId().equals(reservation.getAddressId())) {
                String timeslotId = reservation.getTimeslotId();
                try {
                    reserveDeliveryTimeslot(timeslotId, session);
                } catch (ReservationException e) {
                    LOG.error(MessageFormat.format("Failed to reserve timeslot for timeslot id[{0}]:", timeslotId), e);
                    throw new FDResourceException(e);
                }
            }
        }
    }

    public List<ValidationError> reserveDeliveryTimeslot(String deliveryTimeSlotId, HttpSession session, String soNextDeliveryDate) throws FDResourceException, ReservationException {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        ActionResult actionResult = new ActionResult();
        ChooseTimeslotAction.reserveDeliveryTimeSlot(session, deliveryTimeSlotId, null, actionResult, soNextDeliveryDate);
        for (ActionError error : actionResult.getErrors()) {
            validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
        }
        return validationErrors;
    }
    
    public List<ValidationError> reserveDeliveryTimeslot(String deliveryTimeSlotId, HttpSession session) throws FDResourceException, ReservationException {
        return reserveDeliveryTimeslot(deliveryTimeSlotId, session, null);
    }

    public void releaseTimeslot(FDUserI user) throws FDResourceException {
        FDCartModel cart = user.getShoppingCart();
        FDReservation timeslotReservation = cart.getDeliveryReservation();
        if (timeslotReservation != null) {
            String rsvId = timeslotReservation.getPK().getId();
            ErpAddressModel erpAddress = cart.getDeliveryAddress();

            TimeslotEvent event = new TimeslotEvent((user.getApplication() != null) ? user.getApplication().getCode() : "", cart.isDlvPassApplied(), cart.getDeliverySurcharge(),
                    cart.isDeliveryChargeWaived(), (cart.getZoneInfo() != null) ? cart.getZoneInfo().isCtActive() : false, user.getPrimaryKey(), EnumCompanyCode.fd.name());
            LOG.info("releaseReservation by ID: " + rsvId);
            FDDeliveryManager.getInstance().releaseReservation(rsvId, erpAddress, event, true);
            cart.setDeliveryReservation(null);
        }
    }

    public boolean isTimeslotSelected(final FDUserI user) {
        FDReservation deliveryReservation = user.getShoppingCart().getDeliveryReservation();
        return (deliveryReservation != null && deliveryReservation.getTimeslotId() != null);
    }

    public TimeslotEvent createTimeslotEventModel(FDUserI user) {
        FDCartModel cart = user.getShoppingCart();
        boolean isCtActive = (cart != null && cart.getZoneInfo() != null) ? cart.getZoneInfo().isCtActive() : false;
        String transactionSource = (user.getApplication() != null) ? user.getApplication().getCode() : "";
        boolean dlvPassApplied = (cart != null) ? cart.isDlvPassApplied() : false;
        double deliveryCharge = (cart != null) ? cart.getDeliverySurcharge() : 0.00;
        boolean deliveryChargeWaived = (cart != null) ? cart.isDeliveryChargeWaived() : false;

        return new TimeslotEvent(transactionSource, dlvPassApplied, deliveryCharge, deliveryChargeWaived, isCtActive, user.getPrimaryKey(), EnumCompanyCode.fd.name());
    }

    private String format(Date startDate, Date endDate) {
        return format(DateUtil.toCalendar(startDate), DateUtil.toCalendar(endDate));
    }

    private String format(Calendar startCal, Calendar endCal) {
        StringBuffer sb = new StringBuffer();

        formatCal(startCal, sb);
        sb.append(" - ");
        formatCal(endCal, sb);

        return sb.toString();
    }

    private void formatCal(Calendar cal, StringBuffer sb) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int marker = cal.get(Calendar.AM_PM);

        if (hour > 12) {
            sb.append(hour - 12);
        } else {
            sb.append(hour);
        }

        if (minute != 0) {
            sb.append(':').append(minute);
        }

        if (marker == Calendar.AM) {
            sb.append("AM");
        } else {
            sb.append("PM");
        }
    }

}
