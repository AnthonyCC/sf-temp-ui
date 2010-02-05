package com.freshdirect.transadmin.web.json;

import com.freshdirect.routing.model.IOrderModel;

public interface ICapacityProvider {
	
	IOrderModel getRoutingOrderByReservation(String reservationId);
	
	int updateRoutingOrderByReservation(String reservationId, String orderSize, String serviceTime);
	
	int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, String baseDate);
	
	int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, String baseDate, String accessCode);
	
}
