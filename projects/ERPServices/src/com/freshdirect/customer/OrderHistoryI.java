package com.freshdirect.customer;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.fdstore.EnumEStoreId;

public interface OrderHistoryI extends Serializable {
	
	public int getDeliveredOrderCount();

	public Date getFirstOrderDate();
	
	public Date getFirstOrderDateByStore(EnumEStoreId estoreId) ;

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
	
	public int getValidOrderCount(EnumDeliveryType deliveryType);
	
    public int getValidOrderCount(EnumEStoreId storeId);

	public int getValidPhoneOrderCount();
	
	public int getSettledOrderCount();
	
	public double getOrderSubTotalForChefsTableEligibility();
	
	public int getOrderCountForChefsTableEligibility();
	
	public int getTotalRegularOrderCount();
	
	public int getSettledECheckOrderCount();
	
	public int getUnSettledEBTOrderCount();
	
	public int getUnSettledEBTOrderCount(String currSaleId);
	
	public int getValidMasterPassOrderCount();
	
	public boolean hasSettledOrders();
}