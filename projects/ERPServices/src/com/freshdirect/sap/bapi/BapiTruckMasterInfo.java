package com.freshdirect.sap.bapi;

import java.util.Date;
import java.util.Map;

import com.freshdirect.customer.ErpTruckMasterInfo;

public interface BapiTruckMasterInfo  extends BapiFunctionI{

	public void addRequest(Date requestedDate);

	public Map<String, ErpTruckMasterInfo> getTruckMasterInfo();
}
