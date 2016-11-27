package com.freshdirect.transadmin.datamanager.model;

import java.util.Date;

public interface ITrailerRouteInfoModel {
	
	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate) ;
	String getTrailerNo();
	void setTrailerNo(String trailerNo);
	Date getDispatchTime();
	void setDispatchTime(Date dispatchTime);
	String getTotalDistance();
	void setTotalDistance(String totalDistance);
	Date getTotalTravelTime();
	void setTotalTravelTime(Date totalTravelTime) ;
	Date getTotalServiceTime();
	void setTotalServiceTime(Date totalServiceTime);
	Date getRouteCompleteTime();
	void setRouteCompleteTime(Date routeCompleteTime);
	int getDispatchSequence();
	void setDispatchSequence(int dispatchSequence);
	int getNoOfStops();
	void setNoOfStops(int noOfStops);
	String getRouteId();
	void setRouteId(String routeId);
	double getNoOfCartons();
	void setNoOfCartons(double noOfCartons) ;
}
