package com.freshdirect.sap.jco;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.freshdirect.customer.ErpRouteStatusInfo;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiRouteStatusInfo;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class JcoBapiRouteStatusInfo extends JcoBapiFunction implements BapiRouteStatusInfo {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	private ErpRouteStatusInfo routeInfo;
	
	public JcoBapiRouteStatusInfo() throws JCoException {
		super("ZBAPI_TRUCK_LOAD_STATUS");
	}

	@Override
	public void addRequest(String requestedDate, String routeNumber) {
		this.function.getImportParameterList().setValue("I_ROUTE", routeNumber);
		try {			
			this.function.getImportParameterList().setValue("I_VDATU", DATE_FORMAT.parse(requestedDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void processResponse() throws BapiException
	{
		super.processResponse();
	
		JCoTable _routeStatusTbl = function.getTableParameterList().getTable("T_ROUTE_STATUS");
		
		if (_routeStatusTbl.getNumRows() > 0)
		{
			_routeStatusTbl.firstRow();
			routeInfo = new ErpRouteStatusInfo(
					_routeStatusTbl.getString("ROUTE"),
					_routeStatusTbl.getString("STATUS"),
					_routeStatusTbl.getString("STTXT"));
		}
	}

	@Override
	public ErpRouteStatusInfo getRouteStatusInfo() {
		return routeInfo;
	}

}