package com.freshdirect.webapp.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.TimeOfDay;

public class RestrictionUtil {

	private RestrictionUtil() {
	}

	/**
	 * @return time of platter-restriction today (null if no restriction)
	 */
	public static TimeOfDay getPlatterRestrictionStartTime() throws FDResourceException {
		Calendar now = Calendar.getInstance();
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
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
