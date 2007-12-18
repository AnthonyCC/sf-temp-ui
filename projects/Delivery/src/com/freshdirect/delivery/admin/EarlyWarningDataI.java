package com.freshdirect.delivery.admin;

import java.io.Serializable;

public interface EarlyWarningDataI extends Serializable {

	public String getZoneCode();
	public String getZoneName();

	public int getCapacity();
	public int getOrder();
	public int getTotalAllocation();
	public double getPercentOrders();
	public double getPercentAllocation();
	
	public int getBaseCapacity();
	public int getBaseOrder();
	public int getBaseAllocation();
	public double getPercentBaseOrders();
	public double getPercentBaseAllocation();

	public int getCTCapacity();
	public int getCTAllocation();
	public double getPercentCTAllocation();
	public int getCTOrder();
	public double getPercentCTOrders();
	public boolean getCTActive();
}
