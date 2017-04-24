package com.freshdirect.webapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;

public class RestrictionUtil {

	private static  final DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
	
	private RestrictionUtil() {
	}

	/**
	 * @return time of platter-restriction today (null if no restriction)
	 */
	public static TimeOfDay getPlatterRestrictionStartTime() throws FDResourceException {
		Calendar now = Calendar.getInstance();
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		
		//Check for OTR platter restrictions first, and then RRN.
		List pr0 = FDDeliveryManager.getInstance().getDlvRestrictions().getRestrictions(
				EnumDlvRestrictionCriterion.PURCHASE,
				EnumDlvRestrictionReason.PLATTER,
				EnumDlvRestrictionType.ONE_TIME_RESTRICTION);
			for (Iterator i = pr0.iterator(); i.hasNext();) {
				OneTimeRestriction r = (OneTimeRestriction) i.next();
//				String startDate =dateFormat.format(r.getDateRange().getStartDate());
//				if(startDate.equals(dateFormat.format(now.getTime()))){
//					return new TimeOfDay(timeFormat.format(r.getDateRange().getStartDate()));
//				}
				if(DateUtil.isSameDay(now.getTime(),r.getDateRange().getStartDate())){
					return new TimeOfDay(timeFormat.format(r.getDateRange().getStartDate()));
				}
			}
		List pr = FDDeliveryManager.getInstance().getDlvRestrictions().getRestrictions(
			EnumDlvRestrictionCriterion.PURCHASE,
			EnumDlvRestrictionReason.PLATTER,
			EnumDlvRestrictionType.RECURRING_RESTRICTION);
		for (Iterator i = pr.iterator(); i.hasNext();) {
			RecurringRestriction r = (RecurringRestriction) i.next();
			if (dayOfWeek == r.getDayOfWeek()) {
				return r.getTimeRange().getStartTime();
			}
		}
		return null;
	}

}
