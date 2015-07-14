package com.freshdirect.fdlogistics.services.impl;

import org.apache.log4j.Category;

import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDLogisticsServiceProxy {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDLogisticsServiceProxy.class);


	public IAirclicService getAirclicService() {
		return LogisticsServiceLocator.getInstance().getAirclicService();
	}

	

}
