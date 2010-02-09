package com.freshdirect.delivery;

import java.io.Serializable;

import org.apache.log4j.Category;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class TimeslotCapacityWrapper implements Serializable {

	private static final Category LOGGER = LoggerFactory.getInstance(TimeslotCapacityWrapper.class);
	
	private DlvTimeslotModel timeslot;

	private TimeslotCapacityContext context;

	public TimeslotCapacityWrapper(DlvTimeslotModel timeslot,
			TimeslotCapacityContext context) {
		super();
		this.timeslot = timeslot;
		this.context = context;
	}

	public boolean isAvailable() {
		
		if(timeslot.getRoutingSlot() != null && timeslot.getRoutingSlot().isManuallyClosed()) {
			LOGGER.debug("Manually CLosed" +timeslot);
			return false;
		} else {
			if(context.isChefsTable()) {
				if(useDynamic()) {
					LOGGER.debug(isDynamicCapacityAvailable()+"-CTDYNA->" +timeslot);
					return isDynamicCapacityAvailable();
				} else {
					//LOGGER.debug((timeslot.getTotalAvailable() > 0)+"-CTNORMAL->" +timeslot);
					return timeslot.getTotalAvailable() > 0;
				}			
			} else {
				if (timeslot.isCTCapacityReleased(context.getCurrentTime())) {
					if(useDynamic()) {
						LOGGER.debug(isDynamicCapacityAvailable()+"-DYNACTRELEASED->" +timeslot);
						return isDynamicCapacityAvailable();
					} else {
						//LOGGER.debug((timeslot.getTotalAvailable() > 0)+"-NORMALCTRELEASED->" +timeslot);
						return timeslot.getTotalAvailable() > 0;
					}				
				} else {
					if(useDynamic()) {
						/*LOGGER.debug(isDynamicCapacityAvailable()+","+hasDynamicBaseAvailable()+"=="
												+ timeslot.getRoutingSlot().getDeliveryCost().getPercentageAvailable()+" > "
												+ timeslot.getChefsTablePercentage()
												+"-DYNA->" +timeslot);*/
						return isDynamicCapacityAvailable() && (timeslot.getBaseAvailable() > 0 || timeslot.getChefsTableCapacity() == 0);
					} else {
						//LOGGER.debug((timeslot.getBaseAvailable() > 0)+"-CTNORMAL->" +timeslot);
						return timeslot.getBaseAvailable() > 0;
					}				
				}
			}	
		}
	}
	
	public boolean useDynamic() {
		return timeslot != null && timeslot.getRoutingSlot() != null 
							&& timeslot.getRoutingSlot().isDynamicActive() && FDStoreProperties.isDynamicRoutingEnabled();
	}
	
	/*public boolean isAvailable() {
		
		if(timeslot.getRoutingSlot().isManuallyClosed() || 
				(timeslot.getRoutingSlot().isDynamicActive() && !isDynamicCapacityAvailable())) {
			
			return false;
		} else {
			if(context.isChefsTable()) {
				return timeslot.getTotalAvailable() > 0;
			} else {
				if (timeslot.isCTCapacityReleased(context.getCurrentTime())) {
					return timeslot.getTotalAvailable() > 0;
				} else {
					return timeslot.getBaseAvailable() > 0;
				}
			}
		}
	} */

	public boolean isDynamicCapacityAvailable() {
		return timeslot.getRoutingSlot() != null 
		&& timeslot.getRoutingSlot().getDeliveryCost() != null 
		&& timeslot.getRoutingSlot().getDeliveryCost().isAvailable();
	}

	public boolean hasDynamicBaseAvailable() {		
		return (isDynamicCapacityAvailable()
				&& timeslot.getRoutingSlot().getDeliveryCost().getPercentageAvailable()
						> timeslot.getChefsTablePercentage());
	}

	public int getCalculatedDynamicCapacity() {

		if(timeslot.getRoutingSlot() != null 
				&& timeslot.getRoutingSlot().getDeliveryMetrics() != null ) {
			
			return (int)(((timeslot.getRoutingSlot().getDeliveryMetrics().getTotalCapacityTime() - 
							(timeslot.getRoutingSlot().getDeliveryMetrics().getConfirmedServiceTime() 
							+ timeslot.getRoutingSlot().getDeliveryMetrics().getConfirmedTravelTime()
							+ timeslot.getRoutingSlot().getDeliveryMetrics().getReservedServiceTime()
							+ timeslot.getRoutingSlot().getDeliveryMetrics().getReservedTravelTime()))/(60.0))
							* timeslot.getRoutingSlot().getSchedulerId().getArea().getDeliveryRate())
							+ timeslot.getTotalAllocation();

		}
		return 0;
	}

	public int getCalculatedDynamicCTCapacity() {

		if(timeslot.getRoutingSlot() != null 
				&& timeslot.getRoutingSlot().getDeliveryMetrics() != null ) {

			int newCapacity = (int)(((timeslot.getRoutingSlot().getDeliveryMetrics().getTotalCapacityTime() - 
											(timeslot.getRoutingSlot().getDeliveryMetrics().getConfirmedServiceTime() 
											+ timeslot.getRoutingSlot().getDeliveryMetrics().getConfirmedTravelTime()
											+ timeslot.getRoutingSlot().getDeliveryMetrics().getReservedServiceTime()
											+ timeslot.getRoutingSlot().getDeliveryMetrics().getReservedTravelTime()))/(60.0))
											* timeslot.getRoutingSlot().getSchedulerId().getArea().getDeliveryRate())
											+ timeslot.getTotalAllocation();
			if(timeslot.getCapacity() != 0) {
				return (int)(((double)timeslot.getChefsTableCapacity()/timeslot.getCapacity() )* (double)newCapacity);				
			} 

		}
		return 0;
	}

}
