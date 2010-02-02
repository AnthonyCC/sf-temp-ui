/**
 * 
 */
package com.freshdirect.fdstore.util;

import java.util.Date;

import com.freshdirect.delivery.TimeslotCapacityContext;
import com.freshdirect.delivery.TimeslotCapacityWrapper;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;

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
	public static boolean getAvailableCapacity(DlvTimeslotModel timeslotModel,
			Date currentTime, int page, int autoRelease) {
		
		TimeslotCapacityContext context = new TimeslotCapacityContext();
		context.setChefsTable(PAGE_CHEFSTABLE == page);
		context.setCurrentTime(currentTime);
		
		TimeslotCapacityWrapper capacityProvider = new TimeslotCapacityWrapper(timeslotModel, context);
		return capacityProvider.isAvailable();	
	}
	
		
	public static IDeliveryWindowMetrics recalculateCapacity(DlvTimeslotModel timeslotModel) {
		
		IDeliveryWindowMetrics _metrics = null;
		if(timeslotModel != null ) {
			
			if(timeslotModel.getRoutingSlot() != null 
										&& timeslotModel.getRoutingSlot().getDeliveryMetrics() != null ) {
				TimeslotCapacityContext context = new TimeslotCapacityContext();
								
				TimeslotCapacityWrapper capacityProvider = new TimeslotCapacityWrapper(timeslotModel, context);
				
				_metrics = timeslotModel.getRoutingSlot().getDeliveryMetrics();
				
				_metrics.setOrderCapacity(capacityProvider.getCalculatedDynamicCapacity());
				_metrics.setOrderCtCapacity(capacityProvider.getCalculatedDynamicCTCapacity());
			}
		}
		return _metrics;
	}

}
