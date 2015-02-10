package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.List;

import com.freshdirect.sap.bapi.BapiSendPhysicalTruckInfo;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSendPhysicalTruckInfo extends JcoBapiFunction implements BapiSendPhysicalTruckInfo {
	
	private JCoTable routes;	

	public JcoBapiSendPhysicalTruckInfo() throws JCoException {
		super("ZBAPI_PHYTRK_ASSIGN");
	}
	
	@Override
	public void setHandOffRoutes(List<HandOffRouteTruckIn> routesIn)
	{
		routes = this.function.getTableParameterList().getTable("T_ZTRK_ASSIGN");
	
		for(HandOffRouteTruckIn route : routesIn)
		{
			routes.insertRow(1);
		
			routes.setValue("ZZTRKNO", route.getRouteId()); // Route No			
			routes.setValue("ZZPHYTRK", route.getPhysicalTruckNumber()); //Truck Number
							
			routes.nextRow();
		}
	}
	
	@Override
	public void setParameters(String plantCode, Date deliveryDate)
	{
		this.function.getImportParameterList().setValue("I_WERKS", plantCode);
		this.function.getImportParameterList().setValue("I_VDATU", deliveryDate);
	}
	
	public String getHandOffResult() {	
		return null;
	}

	

}
