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
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiRouteMasterInfo;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapRouteMasterInfo extends SapCommandSupport {
	
	
	private List routeInfoDetails;
	private String requestedDate;
	
	public SapRouteMasterInfo(String requestedDate) {
        this.requestedDate=requestedDate;		
	}

	public void execute() throws SapException {

		BapiRouteMasterInfo bapi = BapiFactory.getInstance().getBapiRouteMasterInfoBuilder();
		bapi.addRequest(this.requestedDate);
		this.invoke(bapi);
		routeInfoDetails=new ArrayList();
		Map routeMap = bapi.getRouteMasterInfo();
		String routeNumber[]=(String[])routeMap.get("routeNumbers");
		String firstDlvTime[]=(String[])routeMap.get("firstDlvTime");
		
		
		for(int i=0;i<routeNumber.length;i++){									
			ErpRouteMasterInfo info=new ErpRouteMasterInfo(bapi.getRouteNumber(i),bapi.getZoneNumber(i),bapi.getTime(i),bapi.getNumberOfStops(i),bapi.getFirstDlvTime(i), bapi.getTruckNumber(i));
			routeInfoDetails.add(info);
		}
		
	}
	
	public List getRouteMasterInfos() {
		return routeInfoDetails;
	}
}
