package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class RoutingAnalyzerCommand implements Serializable, Runnable {
	
	private IOrderModel order;
	
	private Date deliveryDate;
	
	private List<IDeliverySlot> routingTimeSlots;
	
	private RoutingAnalyzerContext context;
	
	private Exception exception;
	
		
	public Date getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	public Exception getException() {
		return exception;
	}


	public void setException(Exception exception) {
		this.exception = exception;
	}


	public IOrderModel getOrder() {
		return order;
	}


	public void setOrder(IOrderModel order) {
		this.order = order;
	}

	public List<IDeliverySlot> getRoutingTimeSlots() {
		return routingTimeSlots;
	}


	public void setRoutingTimeSlots(List<IDeliverySlot> routingTimeSlots) {
		this.routingTimeSlots = routingTimeSlots;
	}


	public RoutingAnalyzerContext getContext() {
		return context;
	}


	public void setContext(RoutingAnalyzerContext context) {
		this.context = context;
	}


	public void execute()  {
		
		DeliveryServiceProxy dlvService = new DeliveryServiceProxy();
		String zoneCode = null;
				
		if(routingTimeSlots != null && routingTimeSlots.size() > 0 && RoutingUtil.isDynamicEnabled(routingTimeSlots)) {
			try {
				zoneCode = routingTimeSlots.get(0).getZoneCode();		
				order.getDeliveryInfo().setDeliveryZone(dlvService.getDeliveryZone(zoneCode));
				order.getDeliveryInfo().setDeliveryDate(deliveryDate);
				IServiceTimeScenarioModel srvScenario = RoutingUtil.getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
				order.getDeliveryInfo().setPackagingInfo(RoutingUtil.estimateOrderSize(order, srvScenario, context.getHistoryPackageInfo()));
				order.getDeliveryInfo().setServiceTime(dlvService.estimateOrderServiceTime(order, srvScenario));
				
				List<IDeliverySlot> slots = schedulerAnalyzeOrder(order, deliveryDate,1, this.getRoutingTimeSlots());
				
				if(slots != null && slots != null) {
					for (IDeliverySlot slot : slots) {
						for (FDTimeslot dlvslot : this.getContext().getDlvTimeSlots()) {
							if(dlvslot.isMatching(slot.getSchedulerId().getDeliveryDate(), slot.getStartTime(), slot.getStopTime())) {
								
								dlvslot.getDlvTimeslot().getRoutingSlot().setDeliveryCost(slot.getDeliveryCost());
								dlvslot.getDlvTimeslot().getRoutingSlot().setDeliveryMetrics(slot.getDeliveryMetrics());
								break;
							}
						}
					}
				}
			} catch (Exception exp) {
				this.setException(exp);
				exp.printStackTrace();
			}
		}
	}
	
	private static List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, Date startDate
																, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException {

		return new RoutingEngineServiceProxy().schedulerAnalyzeOrder(orderModel, 
																		RoutingServicesProperties.getDefaultLocationType(), 
																		RoutingServicesProperties.getDefaultOrderType(), 
																		startDate, noOfDays, slots);
	}
	
	 public void run () {
		 this.execute();
	 }
}
