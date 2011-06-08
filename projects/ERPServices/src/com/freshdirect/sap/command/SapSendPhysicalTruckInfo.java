/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.Date;
import java.util.List;

import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiInfo;
import com.freshdirect.sap.bapi.BapiSendPhysicalTruckInfo;
import com.freshdirect.sap.bapi.BapiSendPhysicalTruckInfo.HandOffRouteTruckIn;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapSendPhysicalTruckInfo extends SapCommandSupport {

	private List<HandOffRouteTruckIn> routes;

	private String plantCode;
	private Date deliveryDate;
	private BapiInfo[] bapiInfos = null;
	
	public SapSendPhysicalTruckInfo(List<HandOffRouteTruckIn> routes,String plantCode, Date deliveryDate) {
		super();
		this.routes = routes;		
		this.plantCode = plantCode;
		this.deliveryDate = deliveryDate;		
	}


	public void execute() throws SapException {
		bapiInfos = null;
		BapiSendPhysicalTruckInfo bapi = BapiFactory.getInstance().getHandOffPhysicalTruckInfoSender();
		bapi.setParameters(plantCode, deliveryDate);
		bapi.setHandOffRoutes(routes);
		
		this.invoke(bapi);
		bapiInfos = bapi.getInfos();
	}


	public BapiInfo[] getBapiInfos() {
		return bapiInfos;
	}	
}
