package com.freshdirect.transadmin.web.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetWithStateDto;
import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetWithStateDto;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpRouteStatusInfo;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.command.SapRouteStatusInfo;
import com.freshdirect.telargo.model.FDAssetInfo;
import com.freshdirect.telargo.service.exception.TelargoServiceException;
import com.freshdirect.telargo.service.proxy.TelargoServiceProxy;
import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.constants.EnumRouteStatus;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.IYardManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.ParkingLocationSlotInfo;
import com.freshdirect.transadmin.web.model.ParkingLocationTruckInfo;
import com.freshdirect.transadmin.web.model.RouteInfoCommand;
import com.freshdirect.transadmin.web.model.YardMonitorCommand;

public class YardProviderController extends BaseJsonRpcController  implements IYardProvider {
	
	private static Logger LOGGER = LoggerFactory.getInstance(YardProviderController.class);
	
	private IYardManagerService yardManagerService;
	
	private DomainManagerI domainManagerService;
				
	public IYardManagerService getYardManagerService() {
		return yardManagerService;
	}

	public void setYardManagerService(IYardManagerService yardManagerService) {
		this.yardManagerService = yardManagerService;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public boolean addParkingLocation(String[][] location){
		
		Set<ParkingLocation> newLocations = new HashSet<ParkingLocation>();
		Map<String, ParkingLocation> previousLocs = yardManagerService.getParkingLocation();
		try{
			for(int i=0;i < location.length;i++) {
				ParkingLocation _locModel = new ParkingLocation(location[i][0], location[i][1]);
				if(!previousLocs.containsKey(_locModel.getLocationName()))
					newLocations.add(_locModel);
				else
					previousLocs.remove(_locModel.getLocationName());
			}
			if(previousLocs != null && previousLocs.size() > 0)
				yardManagerService.clearParkingLocation(previousLocs.keySet());
			if(newLocations.size() > 0) {				
				yardManagerService.addParkingLocation(newLocations);
			}
		} catch (TransAdminServiceException ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<ParkingLocation>  getParkingLocation(){
		
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
		for(Map.Entry<String, ParkingLocation> locEntry : locationMap.entrySet()){
			locations.add(locEntry.getValue());
		}
		return locations;
	}
	
	public String addParkingSlot(String slotNum, String slotDesc, String barcodeStatus, String pavedStatus,String parkingLocName){
		
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
		ParkingLocation location = locationMap.get(parkingLocName);
		StringBuffer exceptionStr = new StringBuffer();
		try{
			if(location != null){
				ParkingSlot slot = new ParkingSlot(slotNum, slotDesc, location, barcodeStatus, pavedStatus);
				yardManagerService.addParkingSlot(slot);
			}
		} catch (TransAdminServiceException e){
			 LOGGER.error("TransAdmin service exception", e);	
			 return exceptionStr.append(e.getIssueMessage()).toString();
		}
		return null;
		
	}
	
	public List<ParkingSlot> getParkingSlot(String parkingLocName) {
		
		List<ParkingSlot> slots = new ArrayList<ParkingSlot>();
		slots = yardManagerService.getParkingSlots(parkingLocName);
		return slots;
	}
	
	public YardMonitorCommand getYardMonitorSummary(){
		
		YardMonitorCommand command = new YardMonitorCommand();
		List<ParkingLocationSlotInfo> locSlotInfoLst = new ArrayList<ParkingLocationSlotInfo>();
		List<ParkingLocationTruckInfo> locTruckInfoLst = new ArrayList<ParkingLocationTruckInfo>();
		
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
		Map<String, Integer> locationMapping = new HashMap<String, Integer>();
		if(locationMap != null){
			List<ParkingSlot> locationSlots = null;
			for(Map.Entry<String, ParkingLocation> locEntry : locationMap.entrySet()){
				locationSlots = yardManagerService.getParkingSlots(locEntry.getKey());
				locationMapping.put(locEntry.getKey(), locationSlots != null ? locationSlots.size() : 0);
			}
		}
		
		List<ParkingSlot> slots = new ArrayList<ParkingSlot>();
		slots = yardManagerService.getParkingSlots(null);
		
		if(!TransportationAdminProperties.isTelargoServiceBlackhole()){
			
			TelargoServiceProxy proxy = new TelargoServiceProxy();
			ArrayOfAssetWithStateDto assetStateArray;
			try {
				assetStateArray = proxy.getAssetsWithLastState();		
				List<FDAssetInfo> assetInfos = new ArrayList<FDAssetInfo>();
				
				if (assetStateArray != null) {
					AssetWithStateDto[] assetStateDtos = assetStateArray.getAssetWithStateDto();
					if (assetStateDtos != null) {
						for (AssetWithStateDto assetDTO : assetStateDtos) {
							assetInfos.add(new FDAssetInfo(assetDTO));
						}
					} else {
						LOGGER.debug("########## Empty AssetWithStateDto ############### ");
					}
				} else {
					LOGGER.debug("########## Empty ArrayOfAssetWithStateDto ##########");
				}
				
				Map<String, Integer> locTruckMapping = new HashMap<String, Integer>();
				
				locTruckMapping.put(TransportationAdminProperties.getFDHeadQuarterKey(), 0);
				locTruckMapping.put(TransportationAdminProperties.getFDOnRoadKey(), 0);
				
				Map<ParkingSlot, Integer> usedSlotMapping = new HashMap<ParkingSlot, Integer>();		
				for (FDAssetInfo asset : assetInfos) {
					boolean foundSlot = false;
					if(asset.getLastLocation() != null){
						String[] locArray = StringUtil.decodeStrings(asset.getLastLocation());
						for(int i=0; i < locArray.length; i++){
							if(slots != null){
								for(ParkingSlot _slot : slots){
									if(_slot.getSlotNumber().trim().equalsIgnoreCase(locArray[i].trim())) {
										foundSlot = true;
										if(!usedSlotMapping.containsKey(_slot.getSlotNumber())){
											usedSlotMapping.put(_slot, 0);
										}
										usedSlotMapping.put(_slot, usedSlotMapping.get(_slot).intValue()+1);
										break;
									}
								}
							} else {
								break;
							}
							if(TransportationAdminProperties.getFDHeadQuarterKey().equalsIgnoreCase(locArray[i].trim())) {
								locTruckMapping.put(TransportationAdminProperties.getFDHeadQuarterKey()
										, locTruckMapping.get(TransportationAdminProperties.getFDHeadQuarterKey()).intValue()+1);
								foundSlot = true;
							}
							if(foundSlot) break;
						}
						if(asset.getLastLocation().startsWith(TransportationAdminProperties.getFDOnRoadTrucksIdentity())){
							locTruckMapping.put(TransportationAdminProperties.getFDOnRoadKey()
									, locTruckMapping.get(TransportationAdminProperties.getFDOnRoadKey()).intValue()+1);
						}
					} else {
						locTruckMapping.put(TransportationAdminProperties.getFDOnRoadKey()
								, locTruckMapping.get(TransportationAdminProperties.getFDOnRoadKey()).intValue()+1);
					}
				}
				
				ParkingLocationSlotInfo _locSlotInfo = null;
				for(Map.Entry<String, Integer> locEntry : locationMapping.entrySet()){
					_locSlotInfo = new ParkingLocationSlotInfo();
					int usedSlotsPerLoc = 0; 
					for(Map.Entry<ParkingSlot, Integer> slotEntry : usedSlotMapping.entrySet()){
						if(slotEntry.getKey().getLocation().getLocationName().equalsIgnoreCase(locEntry.getKey())){
							usedSlotsPerLoc = usedSlotsPerLoc + slotEntry.getValue();
						}
					}				
					_locSlotInfo.setLocation(locEntry.getKey());
					_locSlotInfo.setUsedSlots(usedSlotsPerLoc);
					_locSlotInfo.setAvailableSlots(locEntry.getValue() > 0 ? locEntry.getValue() - usedSlotsPerLoc : 0);
					locSlotInfoLst.add(_locSlotInfo);
				}
				Collections.sort(locSlotInfoLst, new ParkingLocationSlotInfoComparator());
				command.setLocationSlotSummary(locSlotInfoLst);
				
				ParkingLocationTruckInfo _locTruckInfo = null;
				for(Map.Entry<String, Integer> locEntry : locTruckMapping.entrySet()){
					_locTruckInfo = new ParkingLocationTruckInfo();
					_locTruckInfo.setLocation(locEntry.getKey());
					_locTruckInfo.setNoOfTrucks(locEntry.getValue());
					locTruckInfoLst.add(_locTruckInfo);
				}
				Collections.sort(locTruckInfoLst, new ParkingLocationTruckInfoComparator());
				command.setLocationTruckSummary(locTruckInfoLst);
				
			} catch (TelargoServiceException exp) {			
				exp.printStackTrace();
				LOGGER.error("########## Retrive Telargo data failed with exception ##########" + exp);
			}
		}
		return command;
	}
	
	private class ParkingLocationSlotInfoComparator implements Comparator<ParkingLocationSlotInfo> {

		public int compare(ParkingLocationSlotInfo loc1, ParkingLocationSlotInfo loc2) {
			if(loc1.getLocation() != null &&  loc2.getLocation() != null) {
				return loc1.getLocation().compareTo(loc2.getLocation());
			}
			return 0;
		}
	}
	
	private class ParkingLocationTruckInfoComparator implements Comparator<ParkingLocationTruckInfo> {

		public int compare(ParkingLocationTruckInfo loc1, ParkingLocationTruckInfo loc2) {
			if(loc1.getLocation() != null &&  loc2.getLocation() != null) {
				return loc1.getLocation().compareTo(loc2.getLocation());
			}
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public RouteInfoCommand getRouteStatusInfo(String routeNo){
				
		RouteInfoCommand command = new RouteInfoCommand();		
		List<String> assetTypes = new ArrayList<String>();
		assetTypes.add("TRUCK");assetTypes.add("TRAILER");
		Map<String, EnumAssetStatus> assetMapping = yardManagerService.getAssets(assetTypes);
		
		List<ParkingSlot> parkingSlots = new ArrayList<ParkingSlot>();
		parkingSlots = yardManagerService.getParkingSlots(null);
		
		try {
			
			routeNo = routeNo != null ? routeNo.trim() : "";
			String truckNo = null; String activeRouteNo = null;
			boolean matchFound = false, isRouteNo = false, hasActiveRoute = false, isOnFDProperty = false;
					
			FDRouteMasterInfo routeInfo = domainManagerService.getRouteMasterInfo(routeNo, new Date());
			if(routeInfo != null){
				isRouteNo = true;
				hasActiveRoute = true; matchFound = true;
				truckNo = routeInfo.getTruckNumber();
			} else {
				truckNo = routeNo;
				Collection routes = domainManagerService.getRoutes(TransStringUtil.getServerDate(new Date()));				
				if(routes != null){
					Iterator<ErpRouteMasterInfo> routeItr = routes.iterator();
					while(routeItr.hasNext()){
						ErpRouteMasterInfo _route = routeItr.next();
						if (_route.getTruckNumber() != null
								&& _route.getTruckNumber().equals(truckNo)) {
							matchFound = true;
							hasActiveRoute = true;
							activeRouteNo = _route.getRouteNumber();
							break;
						}
					}
					if(!matchFound && assetMapping.get(truckNo) != null){
						matchFound = true; 
					}
				}
			}
			
			Map<String, String> telargoAssetMapping = new HashMap<String, String>();
			
			if(matchFound && !TransportationAdminProperties.isTelargoServiceBlackhole()){
				TelargoServiceProxy proxy = new TelargoServiceProxy();
				ArrayOfAssetWithStateDto assetStateArray;
			
				assetStateArray = proxy.getAssetsWithLastState();		
				List<FDAssetInfo> assetInfos = new ArrayList<FDAssetInfo>();
				
				if (assetStateArray != null) {
					AssetWithStateDto[] assetStateDtos = assetStateArray.getAssetWithStateDto();
					if (assetStateDtos != null) {
						for (AssetWithStateDto assetDTO : assetStateDtos) {
							FDAssetInfo assetInfo = new FDAssetInfo(assetDTO);
							assetInfos.add(new FDAssetInfo(assetDTO));
							telargoAssetMapping.put(assetInfo.getInternalNo().trim(), assetInfo.getLastLocation());
						}
					} else {
						LOGGER.debug("########## Empty AssetWithStateDto ############### ");
					}
				} else {
					LOGGER.debug("########## Empty ArrayOfAssetWithStateDto ##########");
				}
				
				command.setServiceStatus(assetMapping.get(truckNo) != null ? assetMapping.get(truckNo).getDescription():">>");
				command.setCurrentLocation(telargoAssetMapping.get(truckNo) != null ? telargoAssetMapping.get(truckNo):"Last location not found");
				
				/*Is Route or truck*/
				if (isRouteNo) {
					if(!SapProperties.isBlackhole()) {
						SapRouteStatusInfo routeStatusInfo = new SapRouteStatusInfo(TransStringUtil.getServerDate(new Date()), routeNo);
						routeStatusInfo.execute();
						
						ErpRouteStatusInfo _routeStatusInfo = routeStatusInfo.getRouteStatusInfo();
						if(_routeStatusInfo != null){
							command.setLoadStatus(_routeStatusInfo.getStatusDesc());
						}
					} else {
						command.setLoadStatus(EnumRouteStatus.NODATA.value());
					}
				} else {
					/*has active route */
					if (!hasActiveRoute) {
						command.setLoadStatus(EnumRouteStatus.OPEN.value());
					} else {
						if(telargoAssetMapping.get(truckNo) != null){
							String[] locArray = StringUtil.decodeStrings(telargoAssetMapping.get(truckNo));
							for(int i=0; i < locArray.length; i++){
								if(parkingSlots != null){
									for(ParkingSlot _slot : parkingSlots){
										if(_slot.getSlotNumber().trim().equalsIgnoreCase(locArray[i].trim())) {
											isOnFDProperty = true;
											break;
										}
									}
								} else 
									break;
								
								if(isOnFDProperty)	break;
							}
						}
						if(isOnFDProperty && isPastFirstDeliveryWindow(activeRouteNo)){
							command.setLoadStatus(EnumRouteStatus.OPEN.value());
						} else 
							command.setLoadStatus(EnumRouteStatus.LOADED.value());
					}
				}
				return command;
			}			
			
		} catch(ParseException px){
			LOGGER.error("########## Parsing date failed with exception ##########" + px);
		} catch (TelargoServiceException exp) {			
			exp.printStackTrace();
			LOGGER.error("########## Retrive Telargo data failed with exception ##########" + exp);
		} catch (Exception ex) {			
			ex.printStackTrace();
			LOGGER.error("########## Exception when fetching route data ##########" + ex);
		}		
		return null;
	}

	private boolean isPastFirstDeliveryWindow(String activeRouteNo) {
		if(activeRouteNo != null){
			UPSRouteInfo routeInfo = yardManagerService.getUPSRouteInfo(new Date(), activeRouteNo);
			if(routeInfo != null && routeInfo.getFirstStopTime() != null){
				Date firstStopTime = TransStringUtil.getAdjustedHourOf(routeInfo.getFirstStopTime(), 4);
				if((new Date()).after(firstStopTime)){
					return true;
				}
			}
		}
		return false;
	}
	
}
