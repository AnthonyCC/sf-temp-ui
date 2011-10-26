package com.freshdirect.transadmin.model;

public interface ICancelOrderModel extends IActiveOrderModel{

	int getReservationCount();
	void setReservationCount(int reservationCount);
}
