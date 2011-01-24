package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.dao.IRoutingInfoDAO;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
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

	public IRoutingInfoDAO getRoutingInfoDAOImpl() {
		return routingInfoDAOImpl;
	}

	public void setRoutingInfoDAOImpl(IRoutingInfoDAO routingInfoDAOImpl) {
		this.routingInfoDAOImpl = routingInfoDAOImpl;
	}

	
}
