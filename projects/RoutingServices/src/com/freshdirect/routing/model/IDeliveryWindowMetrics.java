package com.freshdirect.routing.model;

import java.util.Date;

public interface IDeliveryWindowMetrics {
	
	Date getDeliveryStartTime();

	Date getDeliveryEndTime();

	int getVehiclesInUse();

	int getAllocatedVehicles();

	int getConfirmedItems();

	int getConfirmedDeliveryQuantity();

	int getConfirmedPickupQuantity();

	int getConfirmedServiceTime();

	int getConfirmedTravelTime();

	int getReservedItems();

	int getReservedDeliveryQuantity();

	int getReservedPickupQuantity();

	int getReservedServiceTime();

	int getReservedTravelTime();	

	void setDeliveryStartTime(Date deliveryStartTime);
		
	void setDeliveryEndTime(Date deliveryEndTime);

		
	void setVehiclesInUse(int vehiclesInUse);
		
	void setAllocatedVehicles(int allocatedVehicles);
		
	void setConfirmedItems(int confirmedItems);

	void setConfirmedDeliveryQuantity(int confirmedDeliveryQuantity);		

	void setConfirmedPickupQuantity(int confirmedPickupQuantity);		

	void setConfirmedServiceTime(int confirmedServiceTime);		

	void setConfirmedTravelTime(int confirmedTravelTime);		

	void setReservedItems(int reservedItems);		

	void setReservedDeliveryQuantity(int reservedDeliveryQuantity);		

	void setReservedPickupQuantity(int reservedPickupQuantity);		

	void setReservedServiceTime(int reservedServiceTime);
		
	void setReservedTravelTime(int reservedTravelTime);
	
	int getOrderCapacity();

	void setOrderCapacity(int orderCapacity);

	int getTotalConfirmedOrders();

	void setTotalConfirmedOrders(int totalConfirmedOrders);

	int getTotalAllocatedOrders();

	void setTotalAllocatedOrders(int totalAllocatedOrders);


}
