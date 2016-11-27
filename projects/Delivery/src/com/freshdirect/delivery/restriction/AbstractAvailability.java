package com.freshdirect.delivery.restriction;

import java.util.Calendar;
import java.util.Date;

import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

/**
 * Base class for availability implementations that provides an implementation
 * of {@link #getFirstAvailableDate(DateRange)}.
 */
public abstract class AbstractAvailability implements FDAvailabilityI {

	public Date getFirstAvailableDate(DateRange requestedRange) {
		Calendar rollingCal = DateUtil.truncate(DateUtil
				.toCalendar(requestedRange.getStartDate()));
		Calendar endCal = DateUtil.truncate(DateUtil.toCalendar(requestedRange
				.getEndDate()));

		while (rollingCal.before(endCal)) {
			DateRange range = new DateRange(rollingCal.getTime(), DateUtil
					.addDays(rollingCal.getTime(), 1));

			FDAvailabilityInfo info = this.availableSomeTime(range);
			if (info.isAvailable()) {
				return rollingCal.getTime();
			}
			rollingCal.add(Calendar.DATE, 1);
		}
		return null;
	}

}
