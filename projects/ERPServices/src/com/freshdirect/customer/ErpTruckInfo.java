/*
 * Created on May 29, 2003
 */
package com.freshdirect.customer;

import java.io.Serializable;

/**
 * @author knadeem
 */
public class ErpTruckInfo implements Serializable {
	
	private final String truckNumber;
	private final int enrouteOrders;
	private final int totalOrders;
	
	public ErpTruckInfo(String truckNumber, int enrouteOrders, int totalOrders){
		this.truckNumber = truckNumber;
		this.enrouteOrders = enrouteOrders;
		this.totalOrders = totalOrders;
	}
	
	public String getTruckNumber(){
		return this.truckNumber;
	}
	
	public int getEnrouteOrders(){
		return this.enrouteOrders;
	}
	
	public int getTotalOrders(){
		return this.totalOrders;
	}
	
	public boolean isConfirmed(){
		return enrouteOrders == 0;
	}
	
	public boolean isPartialyConfirmed(){
		return (enrouteOrders > 0 && totalOrders > enrouteOrders);
	}

}
