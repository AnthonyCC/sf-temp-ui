/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.ArrayList;
import java.util.List;
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
	
	
	private List truckInfoDetails;
	
	public SapTruckMasterInfo() {
		//this.orderIdList = orderIdList;		
	}

	public void execute() throws SapException {

		
		BapiTruckMasterInfo bapi = BapiFactory.getInstance().getBapiTruckMasterInfoBuilder();
		//bapi.addRequest(new Date());
		this.invoke(bapi);
		truckInfoDetails=new ArrayList();
		Map truckMap = bapi.getTruckMasterInfo();
		String truckNumber[]=(String[])truckMap.get("truckNumbers");
		String truckType[]=(String[])truckMap.get("truckTypes");
		String truckLicencePlate[]=(String[])truckMap.get("truckLicencePlates");
		String location[]=(String[])truckMap.get("location");
		for(int i=0;i<truckNumber.length;i++){									
			ErpTruckMasterInfo info=new ErpTruckMasterInfo(bapi.getTruckNumber(i),bapi.getTruckType(i),bapi.getTruckLicencePlate(i),bapi.getTruckLocation(i));
			truckInfoDetails.add(info);
		}
	}
	
	public List getTruckMasterInfos() {
		return truckInfoDetails;
	}
}
