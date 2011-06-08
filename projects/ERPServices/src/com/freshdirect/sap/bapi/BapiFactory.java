/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.sap.command.SapSendHandOff;
import com.freshdirect.sap.jco.JcoBapiFunctionFactory;

/**
 * Abstract factory for BAPIs.
 * 
 * @version $Revision$
 * @author $Author$
 */
public abstract class BapiFactory {

	private final static BapiFactory INSTANCE = new JcoBapiFunctionFactory();

	public static BapiFactory getInstance() {
		return INSTANCE;
	}

	public abstract BapiCreateCustomer getCreateCustomerBuilder();

	public abstract BapiSalesOrderCreate getSalesOrderCreateBuilder(
			EnumSaleType saleType);

	public abstract BapiMaterialAvailability getMaterialAvailabilityBuilder();

	public abstract BapiTruckMasterInfo getBapiTruckMasterInfoBuilder();
	
	public abstract BapiRouteMasterInfo getBapiRouteMasterInfoBuilder();

	public abstract BapiSalesOrderSimulate getSalesOrderSimulateBuilder();

	public abstract BapiSalesOrderSimulate getCompositeSimulateBuilder();

	public abstract BapiSalesOrderChange getSalesOrderChangeBuilder();

	public abstract BapiSalesOrderCancel getSalesOrderCancelBuilder();

	public abstract BapiSendSettlement getSendSettlementBuilder();

	public abstract BapiPostReturnI getPostReturnBuilder();

	public abstract BapiCartonInfo getCartonInfoProvider();
	
	public abstract BapiSendEmployeeInfo getSendEmployeeInfoSender();
	
	public abstract BapiSendHandOff getHandOffSender();
	
	public abstract BapiSendPhysicalTruckInfo getHandOffPhysicalTruckInfoSender();

}
