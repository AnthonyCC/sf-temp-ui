package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.OrderEstimationResult;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
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
		RoutingInfoServiceProxy routingInfoproxy = new RoutingInfoServiceProxy();
		
		String zoneCode = null;
				
		if(routingTimeSlots != null && routingTimeSlots.size() > 0 && RoutingUtil.isDynamicEnabled(routingTimeSlots)) {
			try {
				zoneCode = routingTimeSlots.get(0).getZoneCode();
				IZoneModel zoneModel = dlvService.getDeliveryZone(zoneCode);
				order.getDeliveryInfo().setDeliveryZone(zoneModel);
				order.getDeliveryInfo().setDeliveryDate(deliveryDate);
				IServiceTimeScenarioModel srvScenario = RoutingUtil.getRoutingScenario(order.getDeliveryInfo().getDeliveryDate());
				OrderEstimationResult calculatedSize = RoutingUtil.estimateOrderSize(order, srvScenario, context.getHistoryPackageInfo());
				order.getDeliveryInfo().setPackagingDetail(calculatedSize.getPackagingModel());
				order.getDeliveryInfo().setCalculatedOrderSize(calculatedSize.getCalculatedOrderSize());
				
				srvScenario.setZoneConfiguration(routingInfoproxy.getRoutingScenarioMapping(srvScenario.getCode()));
				if(zoneModel.getServiceTimeType().getCode() != null) {
					zoneModel.setServiceTimeType(getContext().getServiceTimeTypes().get(zoneModel.getServiceTimeType().getCode()));
				} else {
					zoneModel.setServiceTimeType(null);
				}
				if(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType() != null) {
					order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(getContext().getServiceTimeTypes()
																.get(order.getDeliveryInfo().getDeliveryLocation().getServiceTimeType().getCode()));
				} else {
					order.getDeliveryInfo().getDeliveryLocation().setServiceTimeType(null);
				}
				if(order.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
						&& order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType() != null) {
					order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(getContext().getServiceTimeTypes()
							.get(order.getDeliveryInfo().getDeliveryLocation().getBuilding().getServiceTimeType().getCode()));
				} else {
					order.getDeliveryInfo().getDeliveryLocation().getBuilding().setServiceTimeType(null);
				}
				
				
				order.getDeliveryInfo().setCalculatedServiceTime(dlvService.getServiceTime(order, srvScenario));
				
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
