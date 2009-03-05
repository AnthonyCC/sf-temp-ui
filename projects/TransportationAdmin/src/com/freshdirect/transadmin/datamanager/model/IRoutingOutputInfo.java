package com.freshdirect.transadmin.datamanager.model;

public interface IRoutingOutputInfo {	
	
	byte[] getOrderFile();
	
	byte[] getTruckFile();
	
	String getCutOff();

	String getCutoffReportFilePath();
	
	byte[] getDepotRoutingFile();
	
	String getForce();

	String getOrderOutputFilePath();

	String getRoutingDepotZones();
	
	String getRoutingZones();

	String getTruckOutputFilePath();

	byte[] getDepotTruckScheduleFile();
	
	byte[] getTruckRoutingFile();

	String getDepotRoutingSessionDesc();

	String getTruckRoutingSessionDesc();
	
}
