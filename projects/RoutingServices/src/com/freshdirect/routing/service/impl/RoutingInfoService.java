package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.Collection;

import com.freshdirect.routing.dao.IRoutingInfoDAO;
import com.freshdirect.routing.service.IRoutingInfoService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class RoutingInfoService implements IRoutingInfoService {
	
	private IRoutingInfoDAO routingInfoDAOImpl;
	
	public Collection getRoutingScenarios() throws RoutingServiceException {
		try {
			return routingInfoDAOImpl.getRoutingScenarios();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
	}

	public IRoutingInfoDAO getRoutingInfoDAOImpl() {
		return routingInfoDAOImpl;
	}

	public void setRoutingInfoDAOImpl(IRoutingInfoDAO routingInfoDAOImpl) {
		this.routingInfoDAOImpl = routingInfoDAOImpl;
	}

	
}
