package com.freshdirect.webapp.ajax.expresscheckout.timeslot.service;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.Util;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.webapp.action.fdstore.ChooseTimeslotAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class TimeslotService {

	private static final TimeslotService INSTANCE = new TimeslotService();

	private TimeslotService() {
	}

	public static TimeslotService defaultService() {
		return INSTANCE;
	}

	public FormTimeslotData loadCartTimeslot(final FDCartI cart) {
		FormTimeslotData timeslotData = new FormTimeslotData();
		FDReservation reservation = cart.getDeliveryReservation();
		if (reservation != null) {
			Date startTime = reservation.getStartTime();
			Calendar startTimeCalendar = DateUtil.toCalendar(startTime);
			String dayNames[] = new DateFormatSymbols().getWeekdays();
			timeslotData.setId(reservation.getTimeslotId());
			timeslotData.setYear(String.valueOf(startTimeCalendar.get(Calendar.YEAR)));
			timeslotData.setMonth(String.valueOf(startTimeCalendar.get(Calendar.MONTH) + 1));
			timeslotData.setDayOfMonth(String.valueOf(startTimeCalendar.get(Calendar.DAY_OF_MONTH)));
			timeslotData.setDayOfWeek(dayNames[startTimeCalendar.get(Calendar.DAY_OF_WEEK)]);
			timeslotData.setTimePeriod(format(startTime, reservation.getEndTime()));
		}
		return timeslotData;
	}

	public List<ValidationError> reserveDeliveryTimeSlot(FormDataRequest timeslotRequestData, HttpSession session) throws FDResourceException {
		try {
			List<ValidationError> validationErrors = new ArrayList<ValidationError>();
			String deliveryTimeSlotId = FormDataService.defaultService().get(timeslotRequestData, "deliveryTimeslotId");
			ActionResult actionResult = new ActionResult();
			ChooseTimeslotAction.reserveDeliveryTimeSlot(session, deliveryTimeSlotId, null, actionResult);
			for (ActionError error : actionResult.getErrors()) {
				validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
			}
			return validationErrors;
		} catch (ReservationException e) {
			throw new FDResourceException(e);
		}
	}
	
	public void releaseTimeslot(FDUserI user) throws FDResourceException {
		FDCartModel cart = user.getShoppingCart();
		FDReservation timeslotReservation = cart.getDeliveryReservation();
		if (timeslotReservation != null) {
			String rsvId = timeslotReservation.getPK().getId();
			ErpAddressModel erpAddress = cart.getDeliveryAddress();
			
			TimeslotEvent event = new TimeslotEvent((user.getApplication() != null) ? user.getApplication().getCode() : "", cart.isDlvPassApplied(), cart.getDeliverySurcharge(),
					cart.isDeliveryChargeWaived(), (cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, user.getPrimaryKey(), EnumCompanyCode.fd.name());
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
		String zoneId = null;
		if (cart != null && cart.getZoneInfo() != null)
			zoneId = cart.getZoneInfo().getZoneId();

		String transactionSource = (user.getApplication() != null) ? user.getApplication().getCode() : "";
		boolean dlvPassApplied = (cart != null) ? cart.isDlvPassApplied() : false;
		double deliveryCharge = (cart != null) ? cart.getDeliverySurcharge() : 0.00;
		boolean deliveryChargeWaived = (cart != null) ? cart.isDeliveryChargeWaived() : false;

		return new TimeslotEvent(transactionSource, dlvPassApplied, deliveryCharge, deliveryChargeWaived, 
				(cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, user.getPrimaryKey(), EnumCompanyCode.fd.name());
	}

	private String format(Date startDate, Date endDate) {
		return format(true, DateUtil.toCalendar(startDate), DateUtil.toCalendar(endDate));
	}

	private String format(boolean forceAmPm, Calendar startCal, Calendar endCal) {
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
			sb.append(":").append(minute);
		}

		if (marker == Calendar.AM) {
			sb.append("AM");
		} else {
			sb.append("PM");
		}
	}

}
