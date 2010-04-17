package com.freshdirect.fdstore.standingorders.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.CTDeliveryCapacityLogic;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.webapp.ActionResult;

public class DeliveryTimeValidator {
	private final static long RESERVATION_MILLISECONDS = 45 * 60 * 1000; // 45 minutes

	public static FDReservation checkDeliveryTime(FDStandingOrder standingOrder, FDUserI user, ErpAddressModel deliveryAddress,
			ActionResult result) throws FDResourceException {
		
		Date nextDate = standingOrder.getNextDeliveryDate();
		Date beginTime = standingOrder.getStartTime();
		Date endTime = standingOrder.getEndTime();

		boolean chefsTable = user.isChefsTable();

		List<FDTimeslot> deliverySlots = FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(
				DateUtil.truncate(nextDate), DateUtil.addDays(DateUtil.truncate(nextDate), 1), deliveryAddress);

		FDTimeslot deliverySlot = null;
		for (FDTimeslot candidateSlot : deliverySlots) {
			if (isTheSameHour(beginTime, candidateSlot.getBegDateTime()) && isTheSameHour(endTime, candidateSlot.getEndDateTime())) {
				deliverySlot = candidateSlot;
				break;
			}
		}
		if (deliverySlot == null) {
			result.addError(true, Validations.DELIVERY_TIME, "no delivery timeslot found for " + DateUtil.formatDay(beginTime)
					+ " " + DateUtil.formatTime(beginTime) + " - " + DateUtil.formatTime(endTime));
			return null;
		}

		String ctDeliveryProfile = CTDeliveryCapacityLogic.isEligible(user, deliverySlot);
		if (deliverySlot.getBaseAvailable() > 0 || chefsTable) {
			ctDeliveryProfile = null;
		}
		if (deliverySlot.getBaseAvailable() <= 0 && ctDeliveryProfile != null) {
			chefsTable = true;
		}

		try {
			// reserve the new slot
			String custId = user.getIdentity().getErpCustomerPK();
			FDReservation reservation = FDDeliveryManager.getInstance().reserveTimeslot(deliverySlot, custId,
					RESERVATION_MILLISECONDS, EnumReservationType.STANDARD_RESERVATION, deliveryAddress, chefsTable,
					ctDeliveryProfile, false);

			return reservation;

		} catch (ReservationUnavailableException re) {
			result.addError(true, Validations.DELIVERY_TIME, "chosen delivery slot full");
		} catch (ReservationException re) {
			result.addError(true, Validations.DELIVERY_TIME, "error reserving delivery slot");
		}

		return null;
	}

	private static boolean isTheSameHour(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		if (cal1.get(Calendar.DAY_OF_WEEK) != cal2.get(Calendar.DAY_OF_WEEK))
			return false;
		if (cal1.get(Calendar.HOUR_OF_DAY) != cal2.get(Calendar.HOUR_OF_DAY))
			return false;
		if (cal1.get(Calendar.MINUTE) != cal2.get(Calendar.MINUTE))
			return false;
		if (cal1.get(Calendar.SECOND) != cal2.get(Calendar.SECOND))
			return false;
		if (cal1.get(Calendar.MILLISECOND) != cal2.get(Calendar.MILLISECOND))
			return false;

		return true;
	}
}
