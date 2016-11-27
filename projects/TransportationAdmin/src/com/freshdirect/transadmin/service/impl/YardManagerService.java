package com.freshdirect.transadmin.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.dao.IYardManagerDAO;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.service.IYardManagerService;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public class YardManagerService implements IYardManagerService {
	
	private IYardManagerDAO yardMngDAOImpl;

	public IYardManagerDAO getYardMngDAOImpl() {
		return yardMngDAOImpl;
	}

	public void setYardMngDAOImpl(IYardManagerDAO yardMngDAOImpl) {
		this.yardMngDAOImpl = yardMngDAOImpl;
	}

	public void addParkingLocation(Set<ParkingLocation> locations) throws TransAdminServiceException {
		try{
			yardMngDAOImpl.addParkingLocation(locations);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
	public void clearParkingLocation(Set<String> loactionLst) throws TransAdminServiceException {
		try{
			yardMngDAOImpl.clearParkingLocation(loactionLst);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
	public Map<String, ParkingLocation> getParkingLocation() throws TransAdminServiceException {
		try{
			return yardMngDAOImpl.getParkingLocation();
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
	public void addParkingSlot(ParkingSlot slot) throws TransAdminServiceException {
		try{
			yardMngDAOImpl.addParkingSlot(slot);
		} catch (DataIntegrityViolationException e) {
			throw new TransAdminServiceException("Parking slot already exists with the same slot number.", e, IIssue.EMPTY);
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
	public List<ParkingSlot> getParkingSlots(String parkingLocName) throws TransAdminServiceException {
		try{
			return yardMngDAOImpl.getParkingSlots(parkingLocName);	
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
	public Map<String, EnumAssetStatus> getAssets(List<String> assetTypes) throws TransAdminServiceException {
		try{
			return yardMngDAOImpl.getAssets(assetTypes);	
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	public UPSRouteInfo getUPSRouteInfo(final Date deliveryDate, final String routeNo) throws TransAdminServiceException {
		try{
			return yardMngDAOImpl.getUPSRouteInfo(deliveryDate, routeNo) ;	
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	public void deleteParkingSlot(List<String> slotNumbers) throws TransAdminServiceException {
		try{
			yardMngDAOImpl.deleteParkingSlot(slotNumbers) ;	
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	public void updateParkingSlot(ParkingSlot slot) throws TransAdminServiceException {
		try{
			yardMngDAOImpl.updateParkingSlot(slot) ;	
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
	public ParkingSlot getParkingSlot(String slotNumber) throws TransAdminServiceException {
		try{
			return yardMngDAOImpl.getParkingSlot(slotNumber) ;	
		} catch (SQLException e) {
			throw new TransAdminServiceException(e, IIssue.PROCESS_YARDMONITOR_ERROR);
		}
	}
	
}
