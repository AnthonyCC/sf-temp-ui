package com.freshdirect.transadmin.web.json;

import com.freshdirect.routing.model.IOrderModel;

public interface ICapacityProvider {
	
	IOrderModel getRoutingOrderByReservation(String reservationId);
	
	int updateRoutingOrderByReservation(String reservationId, String orderSize, String serviceTime);
	
	int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, String baseDate, String cutOff);
	
	int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, String baseDate, String cutOff, String accessCode);
	
	int flagReRouteReservation(String deliveryDate, String zone);
	
	int forceWaveInstance(String waveInstanceId);
	
	String doLockWaveSyncActivity();
	
	boolean isWaveSyncronizationLocked();
	
	int flagReservationStatus(String deliveryDate, String cutOff, String windowStartTime, String windowEndTime, String zone);
}
