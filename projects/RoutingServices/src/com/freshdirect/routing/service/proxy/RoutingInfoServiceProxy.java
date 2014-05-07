package com.freshdirect.routing.service.proxy;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.model.TrnFacility;
import com.freshdirect.routing.model.TrnFacilityType;
import com.freshdirect.routing.model.WaveSyncLockActivity;
import com.freshdirect.routing.service.IRoutingInfoService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class RoutingInfoServiceProxy  extends BaseServiceProxy  {
	
	public Collection getRoutingScenarios()  throws RoutingServiceException {
		return getService().getRoutingScenarios();
	}
	
	public IServiceTimeScenarioModel getRoutingScenarioByDate(Date deliveryDate)  throws RoutingServiceException {
		return getService().getRoutingScenarioByDate(deliveryDate);
	}
	
	public IServiceTimeScenarioModel getRoutingScenarioEx(Date deliveryDate, Date cutoff, Date startTime, Date endTime)  throws RoutingServiceException {
		return getService().getRoutingScenarioEx(deliveryDate, cutoff, startTime, endTime);
	}	
	public IServiceTimeScenarioModel getRoutingScenarioByCode(String code)  throws RoutingServiceException {
		return getService().getRoutingScenarioByCode(code);
	}
	
	public Map<String, IZoneScenarioModel> getRoutingScenarioMapping(String code)  throws RoutingServiceException {
		return getService().getRoutingScenarioMapping(code);
	}
	
	public Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws RoutingServiceException {
		return getService().getRoutingServiceTimeTypes();
	}
	
	public int flagReRouteReservation(Date deliveryDate, List<String> zones) throws RoutingServiceException {
		return getService().flagReRouteReservation(deliveryDate, zones);
	}
	
	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree(Date deliveryDate)  throws RoutingServiceException {
		return getService().getPlannedDispatchTree(deliveryDate);
	}
	
	public Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> getWaveInstanceTree(Date deliveryDate, EnumWaveInstanceStatus status)  throws RoutingServiceException  {
		return getService().getWaveInstanceTree(deliveryDate, status);
	}
	
	public List<IWaveInstance> getWaveInstanceWithErrors()  throws RoutingServiceException {
		return getService().getWaveInstanceWithErrors();
	}
	
	public Set<String> getInSyncWaveInstanceZones(Date deliveryDate)  throws RoutingServiceException {
		return getService().getInSyncWaveInstanceZones(deliveryDate);
	}
	
	public boolean isPlanPublished(Date deliveryDate)  throws RoutingServiceException {
		return getService().isPlanPublished(deliveryDate);
	}
	
	public Map<Date, List<String>> getDynamicEnabledZoneMapping()  throws RoutingServiceException {
		return getService().getDynamicEnabledZoneMapping();
	}
		
	public IRoutingInfoService getService() {
		return RoutingServiceLocator.getInstance().getRoutingInfoService();
	}

	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedTrailerDispatchTree(Date deliveryDate, Date cutOff)  throws RoutingServiceException {
		return getService().getPlannedTrailerDispatchTree(deliveryDate, cutOff);
    }

	public Map<String, TrnFacilityType> retrieveTrnFacilitys()throws RoutingServiceException {
		return getService().retrieveTrnFacilitys();
	}

	public Map<String, TrnFacility> retrieveTrnFacilityLocations() throws RoutingServiceException {
		return getService().retrieveTrnFacilityLocations();
	}

	public List<String> getStaticZonesByDate(Date deliveryDate){
		return getService().getStaticZonesByDate(deliveryDate);
	}
	public Map<RoutingTimeOfDay, Integer> getCutoffSequence() throws RoutingServiceException {
		return getService().getCutoffSequence();
	}
	
	public List<IRegionModel> getRegions(){
		return getService().getRegions();
	}
	public List<IWaveInstance> getWavesByDispatchTime(Date deliveryDate) throws RoutingServiceException{
		return getService().getWavesByDispatchTime(deliveryDate);
	} 
	
	public Map<RoutingTimeOfDay, Integer> getPlantCapacityByDispatchTime(Date deliveryDate) throws RoutingServiceException{
		return getService().getPlantCapacityByDispatchTime(deliveryDate);
	} 
	public Map<RoutingTimeOfDay, RoutingTimeOfDay> getPlantDispatchMapping() throws RoutingServiceException{
		return getService().getPlantDispatchMapping();
	} 
	
	public List<Date> getDeliveryDates() throws RoutingServiceException{
		return getService().getDeliveryDates();
	}

	public Map<String, Map<RoutingTimeOfDay, List<IRouteModel>>> getStaticRoutesByArea(
			Date deliveryDate) {
		return getService().getStaticRoutesByArea(deliveryDate);
	}

	public Set retrieveRoutingWaveInstIds(Date deliveryDate) {
		return getService().retrieveRoutingWaveInstIds(deliveryDate);
	}
	
	public WaveSyncLockActivity isWaveSyncronizationLocked() {
		return getService().isWaveSyncronizationLocked();
	}
	
    public int addWaveSyncLockActivity(String userId) {
    	return getService().addWaveSyncLockActivity(userId);
    }
	
    public int releaseWaveSyncLock(String userId) {
    	return getService().releaseWaveSyncLock(userId);
    }

	public int flagReservationStatus(Date date, Date cutoff, String startTime,
			String endTime, String[] zoneArray) {		
		return  getService().flagReservationStatus(date, cutoff, startTime, endTime, zoneArray);
	}
}
