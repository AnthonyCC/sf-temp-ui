package com.freshdirect.transadmin.model;

public class CancelOrderModel extends ActiveOrderModel implements ICancelOrderModel{
	
	private int reservationCount;

	public int getReservationCount() {
		return reservationCount;
	}

	public void setReservationCount(int reservationCount) {
		this.reservationCount = reservationCount;
	}
	
}
