/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiSendPhysicalTruckInfo extends BapiFunctionI {
	
	public static interface HandOffRouteTruckIn {
		
		public String getRouteId();
		public String getPhysicalTruckNumber();
	}	
	
	void setHandOffRoutes(List<HandOffRouteTruckIn> routesIn);

	void setParameters(String plantCode, Date deliveryDate);
	
	String getHandOffResult();
}
