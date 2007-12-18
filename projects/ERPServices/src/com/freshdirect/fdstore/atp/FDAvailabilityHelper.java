package com.freshdirect.fdstore.atp;

import java.util.Calendar;
import java.util.Date;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.OncePerRequestDateCache;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

public class FDAvailabilityHelper {

	private FDAvailabilityHelper() {
	}

	/**
	 * @return first date within horizon days on which the item is available
	 */
	public static Date getFirstAvailableDate(FDAvailabilityI av) {
		DateRange dr = OncePerRequestDateCache.getAvailabilityHorizon();
		if(dr != null) {
			return av.getFirstAvailableDate(dr);
		}
		return getFirstAvailableDate(av, ErpServicesProperties.getHorizonDays());
	}

	public static Date getFirstAvailableDate(FDAvailabilityI av, int horizonDays) {
		Date d = OncePerRequestDateCache.getToday();
		if(d == null){
			d = new Date();
		}
		return getFirstAvailableDate(av, d, horizonDays);
	}

	/**
	 * @return first availability from trunc(baseDate) + 1
	 */
	protected static Date getFirstAvailableDate(FDAvailabilityI av, Date baseDate, int horizonDays) {
		
		Calendar startCal = DateUtil.truncate(DateUtil.toCalendar(baseDate));
		Calendar endCal = (Calendar) startCal.clone();
		
		startCal.add(Calendar.DATE, 1);
		endCal.add(Calendar.DATE, horizonDays);
		
		return av.getFirstAvailableDate(new DateRange(startCal.getTime(), endCal.getTime()));
	}

}