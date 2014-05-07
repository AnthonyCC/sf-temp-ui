package com.freshdirect.routing.service;

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
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IRoutingInfoService {		
	
	Collection getRoutingScenarios()  throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws RoutingServiceException;
	
	Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws RoutingServiceException;
	
	Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws RoutingServiceException;
	
	int flagReRouteReservation(Date deliveryDate, List<String> zones) throws RoutingServiceException;
	
	Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree(Date deliveryDate)  throws RoutingServiceException;
	
	Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> getWaveInstanceTree(Date deliveryDate, EnumWaveInstanceStatus status)  throws RoutingServiceException;
	
	List<IWaveInstance> getWaveInstanceWithErrors()  throws RoutingServiceException;
	
	Set<String> getInSyncWaveInstanceZones(Date deliveryDate)  throws RoutingServiceException;
	
	boolean isPlanPublished(Date deliveryDate)  throws RoutingServiceException;
	
	Map<Date, List<String>> getDynamicEnabledZoneMapping()  throws RoutingServiceException;

	Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedTrailerDispatchTree(Date deliveryDate, Date cutOff)  throws RoutingServiceException;

	Map<String, TrnFacilityType> retrieveTrnFacilitys() throws RoutingServiceException;

	Map<String, TrnFacility> retrieveTrnFacilityLocations() throws RoutingServiceException;

	List<String> getStaticZonesByDate(Date deliveryDate);

	Map<RoutingTimeOfDay, Integer>  getCutoffSequence() throws RoutingServiceException;

	List<IRegionModel> getRegions() throws RoutingServiceException;

	List<IWaveInstance> getWavesByDispatchTime(Date deliveryDate) throws RoutingServiceException; 
	
	Map<RoutingTimeOfDay, Integer> getPlantCapacityByDispatchTime(Date deliveryDate) throws RoutingServiceException; 
	
	Map<RoutingTimeOfDay, RoutingTimeOfDay> getPlantDispatchMapping() throws RoutingServiceException; 
	
	List<Date> getDeliveryDates() throws RoutingServiceException;

	Map<String, Map<RoutingTimeOfDay, List<IRouteModel>>> getStaticRoutesByArea(
			Date deliveryDate) throws RoutingServiceException;

	Set retrieveRoutingWaveInstIds(Date deliveryDate) throws RoutingServiceException;

	IServiceTimeScenarioModel getRoutingScenarioEx(final Date deliveryDate, final Date cutoff, final Date startTime, final Date endTime) throws RoutingServiceException;
	
	WaveSyncLockActivity isWaveSyncronizationLocked() throws RoutingServiceException;
	
    int addWaveSyncLockActivity(String userId) throws RoutingServiceException;
	
    int releaseWaveSyncLock(String userId) throws RoutingServiceException;

	int flagReservationStatus(Date date, Date cutoff, String startTime, String endTime, String[] zoneArray) throws RoutingServiceException;
}
