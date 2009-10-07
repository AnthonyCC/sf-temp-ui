package com.freshdirect.customer;

import java.io.Serializable;
import java.util.Date;

/**
 * @author skrishnasamy
 * @version 1.0
 * @created 19-Dec-2007 3:03:46 PM
 */
public interface OrderHistoryI extends Serializable {
	
	public int getDeliveredOrderCount();
	
	public Date getFirstNonPickupOrderDate();
	
	public Date getLastOrderCreateDate();
	
	public Date getLastOrderDlvDate();
	
	public String getLastOrderId();
	
	public EnumDeliveryType getLastOrderType();
	
	public String getLastOrderZone();
	
	public int getPhoneOrderCount();
	
	public int getReturnOrderCount();
	
	public String getSecondToLastSaleId();
	
	public int getTotalOrderCount();
	
	public int getValidECheckOrderCount();
	
	public int getValidOrderCount();
	
	public int getValidPhoneOrderCount();
	
	public int getSettledOrderCount();
	
	public double getOrderSubTotalForChefsTableEligibility();
	
	//public double getOrderCountForChefsTableEligibility();
	public int getTotalRegularOrderCount();
}