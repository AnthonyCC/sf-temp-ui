package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecomm.gateway.CrmManagerService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class StandingOrder3CronService extends AbstractEcommService implements StandingOrder3CronServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CrmManagerService.class);
	
	private static final String QUERY_DEACTIVATE_TIMESLOTS = "standingOrder3Cron/queryDeactivateTimeslots";
	private static final String REMOVE_TIMESLOT_FROM_SO = "standingOrder3Cron/removeTimeSlotInfoFromSO";
	private static final String REMOVE_SO_FROM_LOGISTICS = "standingOrder3Cron/removeSOfromLogistics";
	
	private static StandingOrder3CronServiceI INSTANCE;
	
	public static StandingOrder3CronServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StandingOrder3CronService();

		return INSTANCE;
	}
	
	@Override
	public List<String> queryForDeactivatingTimeslotEligible() throws FDResourceException, RemoteException {
		Response<List<String>> response = null;
		response = httpGetData(getFdCommerceEndPoint(QUERY_DEACTIVATE_TIMESLOTS),Response.class);
		if(response.getResponseCode().equals(HttpStatus.UNPROCESSABLE_ENTITY.toString()))
			throw new FDResourceException(response.getMessage());
		if(!response.getResponseCode().equalsIgnoreCase("OK"))
			throw new RemoteException(response.getMessage());
		
		return response.getData();
	}

	@Override
	public void removeTimeSlotInfoFromSO(List<String> soIds) throws FDResourceException, RemoteException {
		Response<String> response = null;
		Request<List<String>> request = new Request<List<String>>();
		String inputJson;
		try {
			request.setData(soIds);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(REMOVE_TIMESLOT_FROM_SO), Response.class);
			if(response.getResponseCode().equals(HttpStatus.UNPROCESSABLE_ENTITY.toString()))
				throw new FDResourceException(response.getMessage());
			if(!response.getResponseCode().equalsIgnoreCase("OK"))
				throw new RemoteException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void removeSOfromLogistics(List<String> soIds) throws FDResourceException, RemoteException {
		Response<String> response = null;
		Request<List<String>> request = new Request<List<String>>();
		String inputJson;
		try {
			request.setData(soIds);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(REMOVE_SO_FROM_LOGISTICS), Response.class);
			if(response.getResponseCode().equals(HttpStatus.UNPROCESSABLE_ENTITY.toString()))
				throw new FDResourceException(response.getMessage());
			if(!response.getResponseCode().equalsIgnoreCase("OK"))
				throw new RemoteException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}

	}

}
