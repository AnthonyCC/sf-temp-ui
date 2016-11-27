package com.freshdirect.transadmin.datamanager.model;

import java.util.Date;

public interface IOrderRouteInfo {
	
	public Date getDeliveryDate();
	public void setDeliveryDate(Date deliveryDate);
	public String getDeliveryModel();
	public void setDeliveryModel(String deliveryModel);
	public String getFixedServiceTime();
	public void setFixedServiceTime(String fixedServiceTime);
	public String getLocationId();
	public void setLocationId(String locationId);
	public Date getOrderBeginDate();
	public void setOrderBeginDate(Date orderBeginDate);
	public Date getOrderEndDate();
	public void setOrderEndDate(Date orderEndDate);
	public String getOrderNumber();
	public void setOrderNumber(String orderNumber);
	public String getPlant();
	public void setPlant(String plant);
	public String getRouteId();
	public void setRouteId(String routeId);
	public Date getRouteStartTime();
	public void setRouteStartTime(Date routeStartTime);
	public Date getStopArrivalTime();
	public void setStopArrivalTime(Date stopArrivalTime);
	public String getStopNumber();
	public void setStopNumber(String stopNumber);
	public Date getTimeWindowStart();
	public void setTimeWindowStart(Date timeWindowStart);
	public Date getTimeWindowStop();
	public void setTimeWindowStop(Date timeWindowStop);
	public String getTotalSize1();
	public void setTotalSize1(String totalSize1);
	public String getTotalSize2();
	public void setTotalSize2(String totalSize2);
}
