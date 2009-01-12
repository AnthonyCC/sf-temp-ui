package com.freshdirect.sap.jco;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiRouteMasterInfo;
import com.sap.mw.jco.JCO;

public class JcoBapiRouteMasterInfo extends JcoBapiFunction implements BapiRouteMasterInfo {
	
	private String[] routeNumbers;
	private String[] zoneNumbers;
	private String[] times;
	private String[] numberOfStops;
	private String[] truckNumbers;
	private String[] firstDlvTime;
    private Map routeMastarMap;
    private static final DateFormat DATE_FORMAT=new SimpleDateFormat("dd-MMM-yyyy");
	
	public JcoBapiRouteMasterInfo() {
		super(ErpServicesProperties.getRouteInfoFunctionName());//("ZBAPI_ROUTE_INFO");
	}


	public void addRequest(String requestedDate) {
		
		
		int i=1000;
		this.function.getImportParameterList().setValue("1000", "WERKS");						
		try {			
			this.function.getImportParameterList().setValue(DATE_FORMAT.parse(requestedDate), "VDATU");			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processResponse() throws BapiException {
		super.processResponse();

		JCO.Table wmdvex = function.getTableParameterList().getTable("T_ZROUTE_INFO");

		// fill inventory entries
		this.routeNumbers = new String[wmdvex.getNumRows()];
		this.zoneNumbers = new String[routeNumbers.length];
		this.times = new String[routeNumbers.length];
		this.numberOfStops= new String[routeNumbers.length];
		this.truckNumbers = new String[routeNumbers.length];
		this.firstDlvTime=new String[routeNumbers.length];
		wmdvex.firstRow();
		for (int i = 0; i < routeNumbers.length; i++) {
			this.routeNumbers[i] = wmdvex.getString("ZZTRKNO");
			this.zoneNumbers[i] = wmdvex.getString("ZZNDEP");
			this.times[i] = wmdvex.getString("TIME");
			this.numberOfStops[i] = wmdvex.getString("STOPS");
			this.truckNumbers[i] = wmdvex.getString("ZZPHYTRK");
			this.firstDlvTime[i] = wmdvex.getString("ZZDESTIME");						
			//this.truckNumbers[i] = "371437";
			wmdvex.nextRow();
		}
		
		routeMastarMap=new HashMap();
		routeMastarMap.put("routeNumbers",routeNumbers);
		routeMastarMap.put("zoneNumbers",zoneNumbers);
		routeMastarMap.put("times",times);		
		routeMastarMap.put("numberOfStops",numberOfStops);
		routeMastarMap.put("truckNumbers", truckNumbers);
		routeMastarMap.put("firstDlvTime",firstDlvTime);
		System.out.println("numberOfStops :"+routeNumbers.length);
		
	}

		
	public String getRouteNumber(int index) {
		// TODO Auto-generated method stub
		 return this.routeNumbers[index];
	
	}
	public String getZoneNumber(int index) {
		// TODO Auto-generated method stub
		return this.zoneNumbers[index];
	}
	public String getTime(int index) {
		// TODO Auto-generated method stub
		return this.times[index];
	}
	public String getNumberOfStops(int index) {
		// TODO Auto-generated method stub
		return this.numberOfStops[index];
	}
	public String getTruckNumber(int index) {
		// TODO Auto-generated method stub
		return this.truckNumbers[index];
	}
	
	public String getFirstDlvTime(int index) {
		// TODO Auto-generated method stub
		return this.firstDlvTime[index];
	}
	
	public Map getRouteMasterInfo() {
		// TODO Auto-generated method stub
		return routeMastarMap;
	}
	
	
}
