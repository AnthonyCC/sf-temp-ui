package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.model.TrnFacility;
import com.freshdirect.routing.model.TrnFacilityType;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IRoutingInfoDAO {
	
	Collection getRoutingScenarios()  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws SQLException;
	
	IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws SQLException;
	
	Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws SQLException;
	
	Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws SQLException;
	
	int flagReRouteReservation(final Date deliveryDate, final List<String> zones) throws SQLException;
	
	Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree(final Date deliveryDate)  throws SQLException;
	
	Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> getWaveInstanceTree(final Date deliveryDate,final  EnumWaveInstanceStatus status)  throws SQLException;
	
	List<IWaveInstance> getWaveInstanceWithErrors()  throws SQLException;
	
	Set<String> getInSyncWaveInstanceZones(final Date deliveryDate)  throws SQLException;
	
	boolean isPlanPublished(final Date deliveryDate)  throws SQLException;
	
	Map<Date, List<String>> getDynamicEnabledZoneMapping()  throws SQLException;

	Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedTrailerDispatchTree(final Date deliveryDate, final Date cutOff)  throws SQLException;

	Map<String, TrnFacilityType> retrieveTrnFacilitys()throws SQLException;

	Map<String, TrnFacility> retrieveTrnFacilityLocations()throws SQLException;
	
	List<String> getStaticZonesByDate(Date deliveryDate)  throws SQLException;

	Map<RoutingTimeOfDay, Integer> getCutoffSequence()   throws SQLException;

	List<IRegionModel> getRegions()  throws SQLException;
	
}
