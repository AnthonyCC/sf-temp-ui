package com.freshdirect.routing.model;

public interface ICancelOrderModel extends IActiveOrderModel{

	int getReservationCount();
	void setReservationCount(int reservationCount);
}
