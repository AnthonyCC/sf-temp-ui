package com.freshdirect.sap.bapi;

import java.util.List;

import com.freshdirect.sap.SapEBTOrderSettlementInfo;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.sap.SapOrderSettlementInfo;

/**
 * 
 * @author tbalumuri
 *
 */
public interface BapiSendPickEligibleOrders extends BapiFunctionI {
	
	void sendOrders(List<SapOrderPickEligibleInfo> orders);
	
	public static final String FIELD_VDATU = "VDATU"; //Requested Delivery Date
	public static final String FIELD_VBELN = "VBELN"; //Sap Sales Order
	public static final String FIELD_WEBID = "WEBID"; //Web Sales Order
	
}
