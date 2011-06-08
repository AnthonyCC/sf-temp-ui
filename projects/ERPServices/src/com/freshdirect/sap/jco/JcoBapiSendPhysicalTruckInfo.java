/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.List;

import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.sap.bapi.BapiSendPhysicalTruckInfo;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSendPhysicalTruckInfo extends JcoBapiFunction implements BapiSendPhysicalTruckInfo {
	
	private JCO.Table routes;	

	public JcoBapiSendPhysicalTruckInfo() {
		super("ZBAPI_PHYTRK_ASSIGN");
	}
	
	@Override
	public void setHandOffRoutes(List<HandOffRouteTruckIn> routesIn) {
		
		routes = this.function.getTableParameterList().getTable("T_ZTRK_ASSIGN");
		for(HandOffRouteTruckIn route : routesIn) {
			routes.insertRow(1);
			routes.setValue(route.getRouteId(), "ZZTRKNO"); // Route No			
			routes.setValue(route.getPhysicalTruckNumber(), "ZZPHYTRK"); //Truck Number
							
			routes.nextRow();
		}
	}
	

	@Override
	public void setParameters(String plantCode, Date deliveryDate) {
		this.function.getImportParameterList().setValue(plantCode, "I_WERKS");
		this.function.getImportParameterList().setValue(deliveryDate, "I_VDATU");
	}
		
	private String formatTime(Date input) {
		if(input != null) {
			return QuickDateFormat.SHORT_TIME_FORMATTER.format(input);
		} else {
			return null;
		}
	}
	
	public String getHandOffResult() {	
		return null;
	}

	

}
