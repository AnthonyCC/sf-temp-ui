package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiTruckMasterInfo;
import com.sap.mw.jco.JCO;

public class JcoBapiTruckMasterInfo extends JcoBapiFunction implements BapiTruckMasterInfo {
	private Date[] commitedDates;
	private double[] commitedQtys;
	private String[] truckNumbers;
	private String[] truckTypes;
	private String[] truckLicencePlates;
	private String[] plant;
	private String[] location; 
    private Map truckMastarMap;
	
	
	public JcoBapiTruckMasterInfo() {
		super(ErpServicesProperties.getTruckInfoFunctionName());//("ZBAPI_TRUCK_INFO");
	}


	public void addRequest(Date requestedDate) {
		JCO.Table wmdvsx = this.function.getTableParameterList().getTable("ZBAPI_TRUCK_INFO");
		wmdvsx.appendRow();
		//wmdvsx.setValue(requestedDate, "REQ_DATE");
		//wmdvsx.setValue(quantity, "REQ_QTY");
	}

	public void processResponse() throws BapiException {
		super.processResponse();

		JCO.Table wmdvex = function.getTableParameterList().getTable("ZTRUCK_INFO");

		// fill inventory entries
		this.truckNumbers = new String[wmdvex.getNumRows()];
		this.truckTypes = new String[truckNumbers.length];
		this.truckLicencePlates = new String[truckNumbers.length];
		this.plant= new String[truckNumbers.length];
		this.location= new String[truckNumbers.length];
		wmdvex.firstRow();
		for (int i = 0; i < truckNumbers.length; i++) {
			this.plant[i] = wmdvex.getString("WERKS");
			this.truckNumbers[i] = wmdvex.getString("ZZPHYTRK");
			this.truckTypes[i] = wmdvex.getString("ZZTRUCKLENGHT");
			this.truckLicencePlates[i] = wmdvex.getString("ZZPLATE");
			this.location[i] = wmdvex.getString("LOCATION");
			wmdvex.nextRow();
		}
		
		truckMastarMap=new HashMap();
		truckMastarMap.put("truckNumbers",truckNumbers);
		truckMastarMap.put("truckTypes",truckTypes);
		truckMastarMap.put("truckLicencePlates",truckLicencePlates);
		truckMastarMap.put("location",location);
		//System.out.println("truckNumbers :"+truckNumbers.length);
		
	}

	public int getCommitedLength() {
		return this.commitedDates.length;
	}

	public Date getCommitedDate(int index) {
		return this.commitedDates[index];
	}

	public double getCommitedQty(int index) {
		return this.commitedQtys[index];
	}

	

	public String getTruckNumber(int index) {
		// TODO Auto-generated method stub
		 return this.truckNumbers[index];
	}

	public String getTruckType(int index) {
		// TODO Auto-generated method stub
		 return this.truckTypes[index];
	}

	public String getTruckLicencePlate(int index) {
		// TODO Auto-generated method stub
		 return this.truckLicencePlates[index];
	}

	public String getTruckLocation(int index) {
		// TODO Auto-generated method stub
		 return this.location[index];
	}

	public Map getTruckMasterInfo() {
		// TODO Auto-generated method stub
		return truckMastarMap;
	}
	
}
