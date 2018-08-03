package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.standingorders.PlaceStandingOrderData;
import com.freshdirect.ecommerce.data.standingorders.ResultListData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrdersJobConfigData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ecomm.converter.StandingOrderConverter;
import com.freshdirect.fdstore.standingorders.SOResult.ResultList;
import com.freshdirect.fdstore.standingorders.StandingOrdersJobConfig;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;
import com.freshdirect.framework.util.log.LoggerFactory;

public class StandingOrdersService extends AbstractEcommService implements StandingOrdersServiceI {

private final static Category LOGGER = LoggerFactory.getInstance(StandingOrdersService.class);
	
	private static final String PLACE_STANDING_ORDERS = "standingOrder/place";
	private static final String PERSIST_UNAV_DETAILS_DB = "standingOrder/unavDetails";
	private static final String GET_DET_FOR_REPORT_GEN = "standingOrder/reportGeneration";
	
	private static StandingOrdersServiceI INSTANCE;
	
	public static StandingOrdersServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StandingOrdersService();

		return INSTANCE;
	}
	
	
	@Override
	public ResultList placeStandingOrders(Collection<String> soList, StandingOrdersJobConfig jobConfig)
			throws RemoteException {
		Response<ResultList> response = null;
		Request<PlaceStandingOrderData> request = new Request<PlaceStandingOrderData>();
		String inputJson;
		try{
			StandingOrdersJobConfigData standingOrdersJobConfigData = StandingOrderConverter.buildStandingOrdersJobConfigData(jobConfig);
			PlaceStandingOrderData data = new PlaceStandingOrderData();
			data.setJobConfig(standingOrdersJobConfigData);
			data.setSoList(soList);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(PLACE_STANDING_ORDERS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void persistUnavDetailsToDB(ResultList resultCounter) throws FDResourceException, RemoteException {
		Response<String> response = null;
		Request<ResultListData> request = new Request<ResultListData>();
		String inputJson;
		try{
			ResultListData data = StandingOrderConverter.buildResultListData(resultCounter);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(PERSIST_UNAV_DETAILS_DB), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public UnavDetailsReportingBean getDetailsForReportGeneration() throws FDResourceException, RemoteException {
		Response<UnavDetailsReportingBean> response = new Response<UnavDetailsReportingBean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_DET_FOR_REPORT_GEN),  new TypeReference<Response<UnavDetailsReportingBean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();	
	}
}
