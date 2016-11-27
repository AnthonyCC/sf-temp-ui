package com.freshdirect.fdstore.atp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.framework.util.DateRange;

public class FDCompositeAvailability implements FDAvailabilityI {
	private static final long serialVersionUID = -8919371677626219573L;

	/**
	 * Map of FDAvailabilityI values indexed by either Integers or Strings
	 * Should be fixed.
	 */
	private final Map<String, FDAvailabilityI> availabilities;
	private final boolean needAllAvailable;

	public FDCompositeAvailability(Map<String, FDAvailabilityI> availabilities) {
		this(availabilities, true);
	}

	/**
	 * @param inventories Map of component key -> FDAvailabilityI
	 * @param needAllAvailable
	 * 		if true, return availability if ALL components are available
	 * 		if false, return available if ANY of the components are available
	 */
	public FDCompositeAvailability(Map<String, FDAvailabilityI> availabilities, boolean needAllAvailable) {
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
		for (Iterator<FDAvailabilityI> i = this.availabilities.values().iterator(); i.hasNext();) {
			FDAvailabilityI av = i.next();
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
		Map<String,FDAvailabilityInfo> unav = new HashMap<String,FDAvailabilityInfo>();
		for (Iterator<Map.Entry<String, FDAvailabilityI>> i = this.availabilities.entrySet().iterator(); i.hasNext();) {
			Map.Entry<String, FDAvailabilityI> entry = i.next();
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