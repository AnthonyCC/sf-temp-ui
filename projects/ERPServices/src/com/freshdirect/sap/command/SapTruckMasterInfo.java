/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.Map;

import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiTruckMasterInfo;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapTruckMasterInfo extends SapCommandSupport {
	
	
	private Map<String, ErpTruckMasterInfo> truckMap;
	
	public SapTruckMasterInfo() {
		//this.orderIdList = orderIdList;		
	}

	public void execute() throws SapException {

		
		BapiTruckMasterInfo bapi = BapiFactory.getInstance().getBapiTruckMasterInfoBuilder();
		//bapi.addRequest(new Date());
		this.invoke(bapi);
		truckMap = bapi.getTruckMasterInfo();
		
	}
	
	public Map<String, ErpTruckMasterInfo> getTruckMasterInfos() {
		return truckMap;
	}

}
