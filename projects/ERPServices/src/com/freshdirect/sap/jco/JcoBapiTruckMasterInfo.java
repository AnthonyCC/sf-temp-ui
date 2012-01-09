package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiTruckMasterInfo;
import com.sap.mw.jco.JCO;

public class JcoBapiTruckMasterInfo extends JcoBapiFunction implements BapiTruckMasterInfo {
	private Date[] commitedDates;
	private double[] commitedQtys;
    private Map<String, ErpTruckMasterInfo> truckMastarMap;
	
	
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
		
		int rowCount = wmdvex.getNumRows();
		wmdvex.firstRow();
		truckMastarMap=new HashMap<String, ErpTruckMasterInfo>();
		String truckNumber;
		ErpTruckMasterInfo truckInfo;
		for (int i = 0; i < rowCount; i++) {
			truckNumber = wmdvex.getString("ZZPHYTRK");
			truckInfo = new ErpTruckMasterInfo(truckNumber,wmdvex.getString("ZZTRUCKLENGHT"),wmdvex.getString("ZZPLATE"),wmdvex.getString("LOCATION"));
			truckMastarMap.put(truckNumber, truckInfo);
			wmdvex.nextRow();
		}
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

	public Map getTruckMasterInfo() {
		// TODO Auto-generated method stub
		return truckMastarMap;
	}
	
}
