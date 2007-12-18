/**
 * 
 */
package com.freshdirect.fdstore.util;

import java.util.Date;

import com.freshdirect.delivery.model.DlvTimeslotModel;

/**
 * Utility to calculate available capacity in given circumstances.
 */
public class TimeslotLogic {

	/** normal page (regular cust or CT cust on normal reservation) */
	public static final int PAGE_NORMAL = 0;

	/** chefstable page (CT cust only) */
	public static final int PAGE_CHEFSTABLE = 1;

	/**
	 * Calculate the availabel capacity for a page.
	 * 
	 * @param timeslotModel
	 *            the timeslot that should be checked
	 * @param currentTime
	 *            the date of the request
	 * @param page
	 *            one of {@link PAGE_NORMAL}, {@link PAGE_CHEFSTABLE}
	 * @param autoRelease
	 *            the number of minutes the auto-release occurs before the
	 *            cutoffTime
	 * @return the number of available orders.
	 */
	public static int getAvailableCapacity(DlvTimeslotModel timeslotModel,
			Date currentTime, int page, int autoRelease) {
		switch (page) {
		case PAGE_NORMAL:
			if (timeslotModel.isCTCapacityReleased(currentTime)) {
				return timeslotModel.getTotalAvailable();
			} else {
				return timeslotModel.getBaseAvailable();
			}
		case PAGE_CHEFSTABLE:
			return timeslotModel.getTotalAvailable();
		default:
			throw new IllegalArgumentException("No such page type: " + page);
		}
	}

}
