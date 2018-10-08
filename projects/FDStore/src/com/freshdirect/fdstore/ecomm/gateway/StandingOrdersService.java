package com.freshdirect.fdstore.ecomm.gateway;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.ecoupon.FDConfigurationData;
import com.freshdirect.ecommerce.data.ecoupon.FDSkuData;
import com.freshdirect.ecommerce.data.standingorders.PlaceStandingOrderData;
import com.freshdirect.ecommerce.data.standingorders.ResultData;
import com.freshdirect.ecommerce.data.standingorders.ResultListData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrdersJobConfigData;
import com.freshdirect.ecommerce.data.standingorders.UnAvailabilityDetailsData;
import com.freshdirect.ecommerce.data.standingorders.UnavailabilityDetailsWrapper;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.ecomm.converter.StandingOrderConverter;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.SOResult.ResultList;
import com.freshdirect.fdstore.standingorders.StandingOrdersJobConfig;
import com.freshdirect.fdstore.standingorders.UnAvailabilityDetails;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.ModelConverter;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.SOResult.Status;
import com.freshdirect.fdstore.standingorders.UnavailabilityReason;

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
		Response<ResultListData> response = null;
		Response<ResultList> info =null;
		Request<PlaceStandingOrderData> request = new Request<PlaceStandingOrderData>();
		String inputJson;
		ResultList result =null;

		try{
			StandingOrdersJobConfigData standingOrdersJobConfigData = StandingOrderConverter.buildStandingOrdersJobConfigData(jobConfig);
			PlaceStandingOrderData data = new PlaceStandingOrderData();
			data.setJobConfig(standingOrdersJobConfigData);
			data.setSoList(soList);
			request.setData(data);
			inputJson = buildRequest(request);
			String responseString = postData(inputJson,getFdCommerceEndPoint(PLACE_STANDING_ORDERS), String.class);
			
			response = getMapper().readValue(responseString, new TypeReference<Response<ResultListData>>() {
			});
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
			result = buildResultList(response.getData());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}catch (JsonMappingException e) {
			throw new RemoteException(e.getMessage());
		}catch (JsonParseException e) {
			throw new RemoteException(e.getMessage());
		}catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
		
		return result;
	}

	private ResultList buildResultList(ResultListData data) {
		ResultList resultList = new ResultList();
		resultList.setErrorOccured(data.isErrorOccured());
		resultList.setFailedCount(data.getFailedCount());
		resultList.setSkippedCount(data.getSkippedCount());
		resultList.setSuccessCount(data.getSuccessCount());
		resultList.setResults(buildResultListList(data));
		return resultList;
	}


	private List<Result> buildResultListList(ResultListData data) {
		List<Result> resultDataList = new ArrayList<Result>();
		List<ResultData> resultListData = data.getResult();
		for (ResultData resultdata : resultListData) {
			resultDataList.add(buildResult(resultdata));
		}
		return resultDataList;
	}
	private Result buildResult(ResultData resultData) {
		Result data = new Result();
		data.setCustomerId(resultData.getCustomerId());
		if(resultData.getErrorCode()!=null)
		data.setErrorCode(ErrorCode.valueOf(resultData.getErrorCode().toString()));
		data.setSaleId(resultData.getSaleId());
		data.setSoId(resultData.getSoId());
		data.setStatus(Status.valueOf(resultData.getStatus().toString()));
		if(resultData.getRequestedDate()!=null)
		data.setRequestedDate(new Date(resultData.getRequestedDate()));
		data.setUnavailabilityDetails(buildUnavailabilityDetails(resultData.getUnavailabilityDetails()));
		return data;
		
	}
	
	private Map<FDCartLineI, UnAvailabilityDetails> buildUnavailabilityDetails(
			List<UnavailabilityDetailsWrapper> unavailabilityDetails) {
		Map<FDCartLineI, UnAvailabilityDetails> unavailabilityMap = new HashMap<FDCartLineI, UnAvailabilityDetails>();
		for (UnavailabilityDetailsWrapper wrapper : unavailabilityDetails) {
			ErpOrderLineModel model = new ErpOrderLineModel();
			model.setSku(buildFDSku(wrapper.getCartLine().getSkuData()));
			model.setMaterialNumber(wrapper.getCartLine().getMaterialNumber());
			model.setConfiguration(buildConfiguration(wrapper.getCartLine().getConfigurationData()));
			FDCartLineI cartline =  new FDCartLineModel(model);
			cartline.setSku(buildFDSku(wrapper.getCartLine().getSkuData()));
			cartline.setSalesUnit(wrapper.getCartLine().getSalesUnit());
			cartline.setDescription(wrapper.getCartLine().getDescription());
		
			unavailabilityMap.put(cartline, buildUnavailabilityDeatils(wrapper.getUnavData()));
		}
		return unavailabilityMap;
	}

	private FDSku buildFDSku(FDSkuData skuData) {
		return new FDSku(skuData.getSkuCode(), skuData.getVersion());
	}
	private FDConfiguration buildConfiguration(FDConfigurationData configurationData) {
		FDConfiguration configuration=null;
				
		if(configurationData!=null)
		configuration = new FDConfiguration(configurationData.getQuantity(), configurationData.getSalesUnit());
		return configuration;
	}
	private UnAvailabilityDetails buildUnavailabilityDeatils(UnAvailabilityDetailsData unAvailabilityDetailsData) {
		return new UnAvailabilityDetails(unAvailabilityDetailsData.getUnavailQty(), UnavailabilityReason.valueOf(unAvailabilityDetailsData.getReason()), unAvailabilityDetailsData.getAltSkucode());
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
