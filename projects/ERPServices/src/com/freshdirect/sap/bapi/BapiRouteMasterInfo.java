package com.freshdirect.sap.bapi;

import java.util.Date;
import java.util.Map;

public interface BapiRouteMasterInfo  extends BapiFunctionI{

	public void addRequest(String requestedDate);

	public String getRouteNumber(int index);
	public String getZoneNumber(int index);
	public String getTime(int index);
	public String getNumberOfStops(int index);
	public String getTruckNumber(int index);
	public String getFirstDlvTime(int index);
	public Map getRouteMasterInfo();
}
