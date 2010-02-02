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

	double getConfirmedServiceTime();

	double getConfirmedTravelTime();

	int getReservedItems();

	int getReservedDeliveryQuantity();

	int getReservedPickupQuantity();

	double getReservedServiceTime();

	double getReservedTravelTime();	

	void setDeliveryStartTime(Date deliveryStartTime);
		
	void setDeliveryEndTime(Date deliveryEndTime);

		
	void setVehiclesInUse(int vehiclesInUse);
		
	void setAllocatedVehicles(int allocatedVehicles);
		
	void setConfirmedItems(int confirmedItems);

	void setConfirmedDeliveryQuantity(int confirmedDeliveryQuantity);		

	void setConfirmedPickupQuantity(int confirmedPickupQuantity);		

	void setConfirmedServiceTime(double confirmedServiceTime);		

	void setConfirmedTravelTime(double confirmedTravelTime);		

	void setReservedItems(int reservedItems);		

	void setReservedDeliveryQuantity(int reservedDeliveryQuantity);		

	void setReservedPickupQuantity(int reservedPickupQuantity);		

	void setReservedServiceTime(double reservedServiceTime);
		
	void setReservedTravelTime(double reservedTravelTime);
	
	int getOrderCapacity();

	void setOrderCapacity(int orderCapacity);

	int getTotalConfirmedOrders();

	void setTotalConfirmedOrders(int totalConfirmedOrders);

	int getTotalAllocatedOrders();

	void setTotalAllocatedOrders(int totalAllocatedOrders);
	
	double getTotalCapacityTime();

	void setTotalCapacityTime(double totalCapacityTime);
	
	int getOrderCtCapacity();

	void setOrderCtCapacity(int ctCapacity);

}
