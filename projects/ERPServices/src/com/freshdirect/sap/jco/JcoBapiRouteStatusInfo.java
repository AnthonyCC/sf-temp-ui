package com.freshdirect.sap.jco;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.freshdirect.customer.ErpRouteStatusInfo;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiRouteStatusInfo;
import com.sap.mw.jco.JCO;

public class JcoBapiRouteStatusInfo extends JcoBapiFunction implements BapiRouteStatusInfo {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	private ErpRouteStatusInfo routeInfo;
	
	public JcoBapiRouteStatusInfo() {
		super("");
	}

	@Override
	public void addRequest(String requestedDate, String routeNumber) {
		this.function.getImportParameterList().setValue(routeNumber, "");						
		try {			
			this.function.getImportParameterList().setValue(DATE_FORMAT.parse(requestedDate), "VDATU");			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void processResponse() throws BapiException {
		
		super.processResponse();
		JCO.Table wmdvex = function.getTableParameterList().getTable("");
		wmdvex.firstRow();
		routeInfo = new ErpRouteStatusInfo(wmdvex.getString(""),wmdvex.getString(""));

	}

	@Override
	public ErpRouteStatusInfo getRouteMasterInfo() {
		return routeInfo;
	}

}
