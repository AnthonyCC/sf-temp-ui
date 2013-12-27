package com.freshdirect.routing.model;

import java.util.Date;

public class DeliveryWindowMetrics  extends BaseModel  implements IDeliveryWindowMetrics {
	
	private Date deliveryStartTime;
	
	private Date deliveryEndTime;
	
	private Date displayStartTime;
	
	private Date displayEndTime;
	
	private int vehiclesInUse;

	private int  allocatedVehicles;
	
	private int confirmedItems;

	private double confirmedServiceTime;

	private double confirmedTravelTime;
	
	private int reservedItems;

	private int reservedDeliveryQuantity;

	private int reservedPickupQuantity;

	private double reservedServiceTime;

	private double reservedTravelTime;
	
	private int orderCapacity;
	
	private int orderCtCapacity;
	
	private int totalConfirmedOrders;
	
	private int totalAllocatedOrders;
	
	private double totalCapacityTime;
	
	private int orderPremiumCapacity;
	
	private int orderPremiumCtCapacity;
	
	private Date deliveryDate;
	
	/** Number of reservations in base pool */
	private int baseAllocation;

	/** Number of reservations in CT pool */
	private int chefsTableAllocation;

	private int premiumAllocation;
	
	private int premiumCtAllocation;
	
	private boolean isDynamic;
	
	public int getOrderCtCapacity() {
		return orderCtCapacity;
	}

	public void setOrderCtCapacity(int ctCapacity) {
		this.orderCtCapacity = ctCapacity;
	}

	public double getTotalCapacityTime() {
		return totalCapacityTime;
	}

	public void setTotalCapacityTime(double totalCapacityTime) {
		this.totalCapacityTime = totalCapacityTime;
	}

	public Date getDeliveryStartTime() {
		return deliveryStartTime;
	}

	public void setDeliveryStartTime(Date deliveryStartTime) {
		this.deliveryStartTime = deliveryStartTime;
	}

	public Date getDeliveryEndTime() {
		return deliveryEndTime;
	}

	public void setDeliveryEndTime(Date deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}

	public int getVehiclesInUse() {
		return vehiclesInUse;
	}

	public void setVehiclesInUse(int vehiclesInUse) {
		this.vehiclesInUse = vehiclesInUse;
	}

	public int getAllocatedVehicles() {
		return allocatedVehicles;
	}

	public void setAllocatedVehicles(int allocatedVehicles) {
		this.allocatedVehicles = allocatedVehicles;
	}

	public int getConfirmedItems() {
		return confirmedItems;
	}

	public void setConfirmedItems(int confirmedItems) {
		this.confirmedItems = confirmedItems;
	}

	public double getConfirmedServiceTime() {
		return confirmedServiceTime;
	}

	public void setConfirmedServiceTime(double confirmedServiceTime) {
		this.confirmedServiceTime = confirmedServiceTime;
	}

	public double getConfirmedTravelTime() {
		return confirmedTravelTime;
	}

	public void setConfirmedTravelTime(double confirmedTravelTime) {
		this.confirmedTravelTime = confirmedTravelTime;
	}

	public int getReservedItems() {
		return reservedItems;
	}

	public void setReservedItems(int reservedItems) {
		this.reservedItems = reservedItems;
	}

	public int getReservedDeliveryQuantity() {
		return reservedDeliveryQuantity;
	}

	public void setReservedDeliveryQuantity(int reservedDeliveryQuantity) {
		this.reservedDeliveryQuantity = reservedDeliveryQuantity;
	}

	public int getReservedPickupQuantity() {
		return reservedPickupQuantity;
	}

	public void setReservedPickupQuantity(int reservedPickupQuantity) {
		this.reservedPickupQuantity = reservedPickupQuantity;
	}

	public double getReservedServiceTime() {
		return reservedServiceTime;
	}

	public void setReservedServiceTime(double reservedServiceTime) {
		this.reservedServiceTime = reservedServiceTime;
	}

	public double getReservedTravelTime() {
		return reservedTravelTime;
	}

	public void setReservedTravelTime(double reservedTravelTime) {
		this.reservedTravelTime = reservedTravelTime;
	}

	public int getOrderCapacity() {
		return orderCapacity;
	}

	public void setOrderCapacity(int orderCapacity) {
		this.orderCapacity = orderCapacity;
	}

	public int getTotalConfirmedOrders() {
		return totalConfirmedOrders;
	}

	public void setTotalConfirmedOrders(int totalConfirmedOrders) {
		this.totalConfirmedOrders = totalConfirmedOrders;
	}

	public int getTotalAllocatedOrders() {
		return totalAllocatedOrders;
	}

	public void setTotalAllocatedOrders(int totalAllocatedOrders) {
		this.totalAllocatedOrders = totalAllocatedOrders;
	}

	@Override
	public int getOrderPremiumCapacity() {
		return orderPremiumCapacity;
	}

	@Override
	public int getOrderPremiumCtCapacity() {
		return orderPremiumCtCapacity;
	}

	@Override
	public void setOrderPremiumCapacity(int orderPremiumCapacity) {
		 this.orderPremiumCapacity = orderPremiumCapacity;
	}

	@Override
	public void setOrderPremiumCtCapacity(int orderPremiumCtCapacity) {
		 this.orderPremiumCtCapacity = orderPremiumCtCapacity;
	}

	public Date getDisplayStartTime() {
		return displayStartTime;
	}

	public void setDisplayStartTime(Date displayStartTime) {
		this.displayStartTime = displayStartTime;
	}

	public Date getDisplayEndTime() {
		return displayEndTime;
	}

	public void setDisplayEndTime(Date displayEndTime) {
		this.displayEndTime = displayEndTime;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public int getBaseAllocation() {
		return baseAllocation;
	}

	public void setBaseAllocation(int baseAllocation) {
		this.baseAllocation = baseAllocation;
	}

	public int getChefsTableAllocation() {
		return chefsTableAllocation;
	}

	public void setChefsTableAllocation(int chefsTableAllocation) {
		this.chefsTableAllocation = chefsTableAllocation;
	}

	public int getPremiumAllocation() {
		return premiumAllocation;
	}

	public void setPremiumAllocation(int premiumAllocation) {
		this.premiumAllocation = premiumAllocation;
	}

	public int getPremiumCtAllocation() {
		return premiumCtAllocation;
	}

	public void setPremiumCtAllocation(int premiumCtAllocation) {
		this.premiumCtAllocation = premiumCtAllocation;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}	
}
