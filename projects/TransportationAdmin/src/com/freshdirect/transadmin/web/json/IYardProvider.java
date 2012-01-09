package com.freshdirect.transadmin.web.json;

import java.util.List;

import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.web.model.RouteInfoCommand;
import com.freshdirect.transadmin.web.model.YardMonitorCommand;

public interface IYardProvider {
	
	List<ParkingLocation> getParkingLocation();
	
	boolean addParkingLocation(String[][] location);
	
	String addParkingSlot(String slotNum, String slotDesc, String barcodeStatus, String pavedStatus, String parkingLocName);
	
	List<ParkingSlot> getParkingSlot(String parkingLocName);
	
	YardMonitorCommand getYardMonitorSummary();
	
	RouteInfoCommand getRouteStatusInfo(String routeNo);
	
}
