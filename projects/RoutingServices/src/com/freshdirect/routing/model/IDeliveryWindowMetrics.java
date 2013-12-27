package com.freshdirect.routing.model;

import java.util.Date;

public interface IDeliveryWindowMetrics {
	
	Date getDeliveryStartTime();

	Date getDeliveryEndTime();

	Date getDisplayStartTime();

	Date getDisplayEndTime();

	int getVehiclesInUse();

	int getAllocatedVehicles();

	int getConfirmedItems();

	double getConfirmedServiceTime();

	double getConfirmedTravelTime();

	int getReservedItems();

	double getReservedServiceTime();

	double getReservedTravelTime();	

	void setDeliveryStartTime(Date deliveryStartTime);
		
	void setDeliveryEndTime(Date deliveryEndTime);

	void setDisplayStartTime(Date deliveryStartTime);
	
	void setDisplayEndTime(Date deliveryEndTime);
		
	void setVehiclesInUse(int vehiclesInUse);
		
	void setAllocatedVehicles(int allocatedVehicles);
		
	void setConfirmedItems(int confirmedItems);

	void setConfirmedServiceTime(double confirmedServiceTime);		

	void setConfirmedTravelTime(double confirmedTravelTime);		

	void setReservedItems(int reservedItems);		

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

	int getOrderPremiumCapacity();

	int getOrderPremiumCtCapacity();
	
	void setOrderPremiumCapacity(int orderPremiumCapacity);

	void setOrderPremiumCtCapacity(int orderPremiumCtCapacity);
	
	Date getDeliveryDate();

	void setDeliveryDate(Date deliveryDate);
	
	int getBaseAllocation();

	void setBaseAllocation(int baseAllocation);

	int getChefsTableAllocation();

	void setChefsTableAllocation(int chefsTableAllocation);

	int getPremiumAllocation();

	void setPremiumAllocation(int premiumAllocation);

	int getPremiumCtAllocation();

	void setPremiumCtAllocation(int premiumCtAllocation);
	
	boolean isDynamic();

	void setDynamic(boolean isDynamic);
	

}
