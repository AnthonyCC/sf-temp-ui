package com.freshdirect.fdstore.atp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.framework.util.DateRange;

public class FDCompositeAvailability implements FDAvailabilityI {

	private final Map availabilities;
	private final boolean needAllAvailable;

	public FDCompositeAvailability(Map availabilities) {
		this(availabilities, true);
	}

	/**
	 * @param inventories Map of component key -> FDAvailabilityI
	 * @param needAllAvailable
	 * 		if true, return availability if ALL components are available
	 * 		if false, return available if ANY of the components are available
	 */
	public FDCompositeAvailability(Map availabilities, boolean needAllAvailable) {
		this.availabilities = availabilities;
		this.needAllAvailable = needAllAvailable;
	}

	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		return checkAvailability(false, requestedRange);
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		return checkAvailability(true, requestedRange);
	}

	public Date getFirstAvailableDate(DateRange requestedRange) {
		Date firstAv = null;
		for (Iterator i = this.availabilities.values().iterator(); i.hasNext();) {
			FDAvailabilityI av = (FDAvailabilityI) i.next();
			Date date = av.getFirstAvailableDate(requestedRange);
			if (date == null) {
				return null;
			}
			if (firstAv == null
					|| (firstAv != null && firstAv.before(date))) {
				firstAv = date;
			}
		}
		return firstAv;
	}
	
	private FDAvailabilityInfo checkAvailability(boolean completely, DateRange requestedRange) {
		Map unav = new HashMap();
		for (Iterator i = this.availabilities.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Entry) i.next();
			FDAvailabilityI inv = (FDAvailabilityI) entry.getValue();
			FDAvailabilityInfo info = completely ? inv.availableCompletely(requestedRange) : inv.availableSomeTime(requestedRange);
			if (!info.isAvailable()) {
				unav.put(entry.getKey(), info);
			}
		}

		if (unav.isEmpty()) {
			return FDAvailabilityInfo.AVAILABLE;
		}

		if (!needAllAvailable && unav.size() != this.availabilities.size()) {
			return FDAvailabilityInfo.AVAILABLE;
		}

		return new FDCompositeAvailabilityInfo(false, unav);
	}

}