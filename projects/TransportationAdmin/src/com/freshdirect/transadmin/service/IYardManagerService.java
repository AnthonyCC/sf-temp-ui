package com.freshdirect.transadmin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public interface IYardManagerService {
	
	void addParkingLocation(Set<ParkingLocation> locations) throws TransAdminServiceException;
	
	void clearParkingLocation(Set<String> loactionLst) throws TransAdminServiceException;
	
	Map<String, ParkingLocation> getParkingLocation() throws TransAdminServiceException;
	
	void addParkingSlot(ParkingSlot slot) throws TransAdminServiceException;
	
	List<ParkingSlot> getParkingSlots(String parkingLocName)  throws TransAdminServiceException;
	
	Map<String, EnumAssetStatus> getAssets(List<String> assetTypes) throws TransAdminServiceException;
	
	UPSRouteInfo getUPSRouteInfo(final Date deliveryDate, final String routeNo) throws TransAdminServiceException;
	
	void deleteParkingSlot(List<String> slotNumbers) throws TransAdminServiceException;
	
	void updateParkingSlot(ParkingSlot slot) throws TransAdminServiceException;
	
	ParkingSlot getParkingSlot(String slotNumber) throws TransAdminServiceException;
}
