package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.dao.IRoutingInfoDAO;
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
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class RoutingInfoService extends BaseService implements IRoutingInfoService {
	
	private IRoutingInfoDAO routingInfoDAOImpl;
	
	public Collection getRoutingScenarios() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingScenarios();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public IServiceTimeScenarioModel getRoutingScenarioByDate(Date deliveryDate)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingScenarioByDate(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public IServiceTimeScenarioModel getRoutingScenarioByCode(String code)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingScenarioByCode(code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public Map<String, IZoneScenarioModel> getRoutingScenarioMapping(String code)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingScenarioMapping(code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingServiceTimeTypes();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public int flagReRouteReservation(Date deliveryDate, List<String> zones) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.flagReRouteReservation(deliveryDate, zones);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree(Date deliveryDate)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getPlannedDispatchTree(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> getWaveInstanceTree(Date deliveryDate, EnumWaveInstanceStatus status)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getWaveInstanceTree(deliveryDate, status);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public List<IWaveInstance> getWaveInstanceWithErrors()  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getWaveInstanceWithErrors();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public Set<String> getInSyncWaveInstanceZones(Date deliveryDate)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getInSyncWaveInstanceZones(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public boolean isPlanPublished(Date deliveryDate)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.isPlanPublished(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}	
	}
	public Map<Date, List<String>> getDynamicEnabledZoneMapping()  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getDynamicEnabledZoneMapping();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}	
	}
	
	public List<String> getStaticZonesByDate(Date deliveryDate)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getStaticZonesByDate(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}	
	}
	
	public IRoutingInfoDAO getRoutingInfoDAOImpl() {
		return routingInfoDAOImpl;
	}

	public void setRoutingInfoDAOImpl(IRoutingInfoDAO routingInfoDAOImpl) {
		this.routingInfoDAOImpl = routingInfoDAOImpl;
	}

	public Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedTrailerDispatchTree(Date deliveryDate, Date cutOff)  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getPlannedTrailerDispatchTree(deliveryDate, cutOff);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public Map<String, TrnFacilityType> retrieveTrnFacilitys()  throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.retrieveTrnFacilitys();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	public Map<String, TrnFacility> retrieveTrnFacilityLocations() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.retrieveTrnFacilityLocations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public Map<RoutingTimeOfDay, Integer> getCutoffSequence() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getCutoffSequence();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	public List<IRegionModel> getRegions() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRegions();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}

	public List<IWaveInstance> getWavesByDispatchTime(Date deliveryDate) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getWavesByDispatchTime(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	public  Map<RoutingTimeOfDay, Integer> getPlantCapacityByDispatchTime(Date deliveryDate) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getPlantCapacityByDispatchTime(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	public  Map<RoutingTimeOfDay, RoutingTimeOfDay> getPlantDispatchMapping() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getPlantDispatchMapping();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	public  List<Date> getDeliveryDates() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getDeliveryDates();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}

	@Override
	public Map<String, Map<RoutingTimeOfDay, List<IRouteModel>>> getStaticRoutesByArea(
			Date deliveryDate) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getStaticRoutesByArea(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}

	@Override
	public Set retrieveRoutingWaveInstIds(Date deliveryDate)
			throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.retrieveRoutingWaveInstIds(deliveryDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}

	@Override
	public IServiceTimeScenarioModel getRoutingScenarioEx(Date deliveryDate, Date cutoff, Date startTime, Date endTime)
			throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingScenarioEx(deliveryDate, cutoff, startTime, endTime);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	@Override
	public WaveSyncLockActivity isWaveSyncronizationLocked() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.isWaveSyncronizationLocked();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	@Override
	public int addWaveSyncLockActivity(String userId) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.addWaveSyncLockActivity(userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	@Override
	public int releaseWaveSyncLock(String userId) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.releaseWaveSyncLock(userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
	
	public int flagReservationStatus(Date date, Date cutoff, String startTime, String endTime, String[] zoneArray) throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.flagReservationStatus(date, cutoff, startTime, endTime, zoneArray);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}
}
