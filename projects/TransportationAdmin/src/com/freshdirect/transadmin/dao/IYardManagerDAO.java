package com.freshdirect.transadmin.dao;

import java.sql.SQLException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.model.UPSRouteInfo;

public interface IYardManagerDAO {
		
	public void addParkingLocation(Set<ParkingLocation> locations) throws SQLException;
	
	void clearParkingLocation(Set<String> loactionLst) throws SQLException;
	
	Map<String, ParkingLocation> getParkingLocation() throws SQLException;
	
	void addParkingSlot(ParkingSlot slot) throws SQLException;
	
	List<ParkingSlot> getParkingSlots(String parkingLocName) throws SQLException;
	
	Map<String, EnumAssetStatus> getAssets(List<String> assetTypes) throws SQLException;
	
	UPSRouteInfo getUPSRouteInfo(final Date deliveryDate, final String routeNo) throws SQLException;
	
	void deleteParkingSlot(List<String> slotNumbers) throws SQLException;
	
	void updateParkingSlot(ParkingSlot slot) throws SQLException;
	
	ParkingSlot getParkingSlot(String slotNumber) throws SQLException;
}
