package com.freshdirect.sap.bapi;

import java.util.Date;
import java.util.Map;

public interface BapiTruckMasterInfo  extends BapiFunctionI{

	public void addRequest(Date requestedDate);

	public String getTruckNumber(int index);
	public String getTruckType(int index);
	public String getTruckLicencePlate(int index);
	public String getTruckLocation(int index);
	
	public Map getTruckMasterInfo();
}
