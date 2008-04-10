/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.sap.bapi.BapiCreateCustomer;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiMaterialAvailability;
import com.freshdirect.sap.bapi.BapiPostReturnI;
import com.freshdirect.sap.bapi.BapiSalesOrderCancel;
import com.freshdirect.sap.bapi.BapiSalesOrderChange;
import com.freshdirect.sap.bapi.BapiSalesOrderCreate;
import com.freshdirect.sap.bapi.BapiSalesOrderSimulate;
import com.freshdirect.sap.bapi.BapiSendSettlement;


/**
 * Abstract factory for builders.
 *
 * @version $Revision$
 * @author $Author$
 */
public class JcoBapiFunctionFactory extends BapiFactory {
    
	public BapiCreateCustomer getCreateCustomerBuilder() {
		return new JcoBapiCreateCustomer();
	}

	public BapiSalesOrderCreate getSalesOrderCreateBuilder(EnumSaleType saleType) {
		String functionName="";
		if(EnumSaleType.REGULAR.equals(saleType)) {
			functionName="ZBAPI_SALESORDER_CREATEFROMDAT";
		}
		else if(EnumSaleType.SUBSCRIPTION.equals(saleType)) {
			functionName="ZBAPI_SALESORDER_XOR_CREATE";
		} 
		else {
			return new JcoBapiSalesOrderCreate();
		}
		return new JcoBapiSalesOrderCreate(functionName);
	}

	public BapiMaterialAvailability getMaterialAvailabilityBuilder() {
		return new JcoBapiMaterialAvailability();
	}
	
	public BapiSalesOrderSimulate getSalesOrderSimulateBuilder() {
		return new JcoBapiSalesOrderSimulate(false);
	}
	
	public BapiSalesOrderSimulate getCompositeSimulateBuilder() {
		return new JcoBapiSalesOrderSimulate(true);
	}

	public BapiSalesOrderChange getSalesOrderChangeBuilder() {
		return new JcoBapiSalesOrderChange();
	}

	public BapiSalesOrderCancel getSalesOrderCancelBuilder() {
		return new JcoBapiSalesOrderCancel();
	}

	public BapiSendSettlement getSendSettlementBuilder() {
		return new JcoBapiSendSettlement();
	}
	
	public BapiPostReturnI getPostReturnBuilder(){
		return new JcoBapiPostReturn();
	}
	
	
	/*public BapiSubscriptionOrderCreate getSubscriptionOrderCreateBuilder() {
		return new JcoBapiSubscriptionOrderCreate();
		
	}*/

}