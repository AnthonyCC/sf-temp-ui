package com.freshdirect.fdlogistics.services.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecommerce.data.cms.CmsCreateFeedParams;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.dlv.CustomerData;
import com.freshdirect.ecommerce.data.dlv.FDReservationData;
import com.freshdirect.ecommerce.data.dlv.ReserveTimeParam;
import com.freshdirect.ecommerce.data.dlv.TimeslotEventData;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.services.ICommerceService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.ReservationException;
import com.freshdirect.logistics.delivery.model.SystemMessageList;
//import com.freshdirect.cms.ContentKey;
import com.freshdirect.logistics.delivery.model.ReservationUnavailableException;


public class FDCommerceService extends AbstractLogisticsService implements ICommerceService{

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDCommerceService.class);

	private static final String SAVE_PRICING_ZONES_API ="pricing/zone/loadData";
	private static final String COO_API ="/coo";
	private static final String DB_MONITOR ="monitor/dbcheck";
	private static final String TEST_DATA_CUSTOMERID ="test/dyfeligiblecustomerid";
	private static final String TEST_DATA_PING ="test/ping";
	private static final String TEST_DATA_ERP_USERID ="test/erpidbyuserid";
	private static final String TEST_DATA_CUSID_ERPID ="test/fdcustomerid";
	private static final String TEST_DATA_SKU_CODES ="test/skucodes";
	private static final String TEST_DATA_ERP_CUSTIDS ="test/erpcustomerids";
	private static final String GET_ERP_BATCH_API = "/erp/batch";
	private static final String CMS_FEED_API = "cms/feed/";
	private static final String SLASH = "/";
	
	private static final String DYF_MODEL_API_PRODUCT_FREQUENCIES = "DyfModel/productfrequencies/";
	private static final String DYF_MODEL_API_GLOBAL_PRODUCT_SCORES = "DyfModel/globalproductscores/";
	private static final String DYF_MODEL_API_PRODUCT_FREQUENCIES_MAP = "DyfModel/productfrequenciesmap/";
	private static final String DYF_MODEL_API_PRODUCTS = "DyfModel/products/";
	
	private static final String DLV_MANAGER_RESERVE_TIME = "dlvmanager/reservetime";
	
	public void loadData(List<ErpZoneMasterInfo> zoneInfoList) throws RemoteException{
		String inputJson = null;
		try {
			Request<List<ErpZoneMasterInfo>> request = new Request<List<ErpZoneMasterInfo>>();
			request.setData(zoneInfoList);
			inputJson = buildRequest(request);
			Response<String> response = getData(inputJson, getFdCommerceEndPoint(SAVE_PRICING_ZONES_API), Response.class);
			if(!response.getResponseCode().equalsIgnoreCase("OK"))
				throw new RemoteException(response.getMessage());
		} catch (FDLogisticsServiceException e) {
			
			LOGGER.error("Error in loadData, data=" + inputJson, e );
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	
	public void healthCheck() throws RemoteException{
		try {
			Response<String> response = httpGetData(getFdCommerceEndPoint(DB_MONITOR), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
		} catch (FDLogisticsServiceException e) {
			
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	public List<Long> getDYFEligibleCustomerIDs() throws RemoteException{
		try {

			Response<List<Long>> response =  httpGetDataTypeMap(getFdCommerceEndPoint(TEST_DATA_CUSTOMERID), new TypeReference<Response<List<Long>>>() {});
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}

		
	}
	@Override
	public boolean ping() throws RemoteException {
		try {
			Response<Boolean> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_PING), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}


	@Override
	public List<Long> getErpCustomerIds() throws RemoteException {
		try {
			Response<List<Long>> response =  httpGetDataTypeMap(getFdCommerceEndPoint(TEST_DATA_ERP_CUSTIDS), new TypeReference<Response<List<Long>>>() {});
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public String getFDCustomerIDForErpId(String erpCustomerPK)
			throws RemoteException {
		try {
			Response<String> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_CUSID_ERPID+"?erpId="+erpCustomerPK), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public String getErpIDForUserID(String userID) throws RemoteException {
		try {
			Response<String> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_ERP_USERID+"?userid="+userID), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public Collection<String> getSkuCodes() throws RemoteException {
		try {
			Response<Collection<String>> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_SKU_CODES), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
	@Override
public BatchModel getBatch(int versionID) throws FDResourceException{
		BatchModel batchModel = new BatchModel();
		long starttime = new java.util.Date().getTime();
		String responseStr = "";
		try {

			Request<String> request = new Request<String>();

			request.setData("");
			/*
			 * This returns the entire json as a string for processing in a later step
			 * 
			 */
			responseStr = httpGetData(getFdCommerceEndPoint(GET_ERP_BATCH_API + SLASH + Integer.toString(versionID)),
					String.class);
			
			Response<BatchModel> responseOfTypeBatchModel = new  Response<BatchModel>();
			/*
			 * In this type we are using com.fasterxml.jackson.databind.ObjectMapper in conjunction with a specific 
			 * TypeReference<Response<BatchModel>> to convert the entire json string to Response<BatchModel>
			 */
			responseOfTypeBatchModel = getMapper().readValue(responseStr, new TypeReference<Response<BatchModel>>() {});
			batchModel = responseOfTypeBatchModel.getData();
		} catch (Exception ex) {
			// (FDLogisticsServiceException e,JsonParseException e )
			if (ex instanceof FDLogisticsServiceException 
					|| ex instanceof JsonParseException
					|| ex instanceof IOException

			) {
				LOGGER.error("Exception converting Json response  to  Response<BatchModel> while getting getBatch "
						+ responseStr);
				LOGGER.error(ex);
				throw new FDResourceException("Could not retrieve Batch information for id: "+ versionID); 
			}

			else {
				LOGGER.error("unknown error while getting getBatch " + responseStr);
				LOGGER.error(ex);
				throw new FDResourceException("Could not retrieve Batch information for id: "+ versionID); 
			}

		}
		long endTime = new java.util.Date().getTime() - starttime;
		LOGGER.debug("getbatch elapsedTime: "+endTime );
		System.out.println("getbatch elapsedTime: "+endTime );
		return batchModel;

	}
	
	
	// GETCMSDATA BY STORE
	//story 17-22
        @Override
		public String getCmsFeed(String storeID) throws FDResourceException{
			String payload;
			String responseStr = "";
			try {

				Request<String> request = new Request<String>();

				request.setData("");
				/*
				 * This returns the entire json as a string for processing in a later step
				 * 
				 */
				//System.out.println(" the url your going to is: "+getFdCommerceEndPoint(CMS_FEED_API  + storeID) );
				responseStr = httpGetData(getFdCommerceEndPoint(CMS_FEED_API  + storeID),
						String.class);
				
				Response<String> responseOfTypestring = new  Response<String>();
				/*
				 * In this type we are using com.fasterxml.jackson.databind.ObjectMapper in conjunction with a specific 
				 * TypeReference<Response<BatchModel>> to convert the entire json string to Response<BatchModel>
				 */
			
				
				responseOfTypestring = getMapper().readValue(responseStr, new TypeReference<Response<String>>() {});
				payload = responseOfTypestring.getData();
			} catch (Exception ex) {
				// (FDLogisticsServiceException e,JsonParseException e )
				if (ex instanceof FDLogisticsServiceException 
						|| ex instanceof JsonParseException
						|| ex instanceof IOException

				) {
					LOGGER.error("Exception converting Json response  to  Response<BatchModel> while getting getBatch "
							+ responseStr);
					LOGGER.error(ex);
					throw new FDResourceException("Could not retrieve cms information for id: "+ storeID); 
				}

				else {
					LOGGER.error("unknown error while getting getBatch " + responseStr);
					LOGGER.error(ex);
					throw new FDResourceException("Could not retrieve cms information for id: "+ storeID); 
				}

			}

			return payload;

		}
		
		
		/**************************************************************/
		// create cms data
		@Override
		public String createFeedCmsFeed(String feedId, String storeId, String feedData) throws FDResourceException{
			String payload;

			String responseStr = "";
		
			CmsCreateFeedParams cmsParams = new CmsCreateFeedParams(feedId,storeId,feedData);
			try {

				Request<CmsCreateFeedParams> request = new Request<CmsCreateFeedParams>();

				request.setData(cmsParams);
				String inputJson = buildRequest(request);
				
				/*
				 * This returns the entire json as a string for processing in a later step
				 * 
				 */
				//System.out.println(" the url your going to is: "+getFdCommerceEndPoint(CMS_FEED_API  ) ); 
				responseStr = 	getData(inputJson, getFdCommerceEndPoint(CMS_FEED_API), String.class);

				LOGGER.debug("jOHNSON THE payload was:" + responseStr);

				Response<String> responseOfTypestring = new  Response<String>();
				/*
				 * In this type we are using com.fasterxml.jackson.databind.ObjectMapper in conjunction with a specific 
				 * TypeReference<Response<BatchModel>> to convert the entire json string to Response<BatchModel>
				 */
				LOGGER.debug("the json response you got back was: : "+responseStr );
				
				responseOfTypestring = getMapper().readValue(responseStr, new TypeReference<Response<String>>() {});
				payload  = responseOfTypestring.getData();
				LOGGER.debug("status: "+ responseOfTypestring.getResponseCode());
				
				
				/*
				 * In this type we are using com.fasterxml.jackson.databind.ObjectMapper in conjunction with a specific 
				 * TypeReference<Response<BatchModel>> to convert the entire json string to Response<BatchModel>
				 */
			

			} catch (Exception ex) {
				// (FDLogisticsServiceException e,JsonParseException e )
				if (ex instanceof FDLogisticsServiceException 
						|| ex instanceof JsonParseException
						|| ex instanceof IOException

				) {
					LOGGER.error("Exception converting Json response  to  Response<String> while getting getBatch "
							+ responseStr);
					LOGGER.error(ex);
					throw new FDResourceException("Could not create cms information for id: "+ feedId); 
				}

				else {
					LOGGER.error("unknown error while getting getBatch " + responseStr);
					LOGGER.error(ex);
					throw new FDResourceException("Could not create cms information for id: "+ feedId); 
				}

			}

			return payload;

		}
		
		/*******************************************************************************/
		
	@Override
	public Set<String> getDYFModelProducts(String customerID) throws FDResourceException, RemoteException {
		Response<Set<String>> responseOfSetStrings = new Response<Set<String>>();
		
		// Response<Map<ZoneInfo, List<FDProductPromotionInfo>>> response;
		try {
			responseOfSetStrings = httpGetDataTypeMap(getFdCommerceEndPoint(DYF_MODEL_API_PRODUCTS + customerID),
					new TypeReference<Response<Set<String>>>() {
				
					});

			if (!responseOfSetStrings.getResponseCode().equals("OK")) {
				throw new RemoteException(responseOfSetStrings.getMessage());
			}
			Set<String> setOfStrings = responseOfSetStrings.getData();


			return setOfStrings;
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}

	}

	@Override
	public Map<String, Float> getDYFModelProductFrequencies(String customerID) throws FDResourceException {
		Response<Map<String, Float>> responseofStrngFlt = new Response<Map<String, Float>>();
	
		Map<String, Float> intermediarymap = new HashMap<String, Float>();
		// Response<Map<ZoneInfo, List<FDProductPromotionInfo>>> response;
		try {
			responseofStrngFlt = httpGetDataTypeMap(
					getFdCommerceEndPoint(DYF_MODEL_API_PRODUCT_FREQUENCIES_MAP + customerID),
					new TypeReference<Response<Map<String, Float>>>() {
					});
			if (!responseofStrngFlt.getResponseCode().equals("OK")) {
				throw new FDResourceException(responseofStrngFlt.getMessage());
			}

			intermediarymap = responseofStrngFlt.getData();

			return intermediarymap;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDLogisticsServiceException e) {

			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e.getMessage(), e);
		}

	}

	@Override
	public Map<String , Float> getDYFModelGlobalProductscores() throws FDResourceException, RemoteException {
		Response<Map<String, Float>> responseofStrngFlt = new Response<Map<String, Float>>();
		Map<String, Float> intermediarymap = new HashMap<String, Float>();

		try {
			responseofStrngFlt = httpGetDataTypeMap(getFdCommerceEndPoint(DYF_MODEL_API_GLOBAL_PRODUCT_SCORES),
					new TypeReference<Response<Map<String, Float>>>() {
					});
			if (!responseofStrngFlt.getResponseCode().equals("OK")) {
				throw new RemoteException(responseofStrngFlt.getMessage());
			}

			intermediarymap = responseofStrngFlt.getData();

			return intermediarymap;
			
		} catch (FDLogisticsServiceException e) {

			LOGGER.error(e.getMessage());
			throw new FDResourceException(e.getMessage(), e);
		}

	}
	@Override
	public FDReservation reserveTimeslot(String timeslotId, String customerId,
			EnumReservationType type, Customer customer, boolean chefsTable,
			String ctDeliveryProfile, boolean isForced, TimeslotEvent event,
			boolean hasSteeringDiscount, String deliveryFeeTier) throws FDResourceException, ReservationException {

		Request<ReserveTimeParam> request = new Request<ReserveTimeParam>();
		FDReservation fdReservation = null;
		
		CustomerData customerData = getMapper().convertValue(customer, CustomerData.class);
		TimeslotEventData timeSlotevent = getMapper().convertValue(event, TimeslotEventData.class);
		ReserveTimeParam  reservationParam = new ReserveTimeParam(timeslotId, customerId,type.getName(),customerData,chefsTable,ctDeliveryProfile,isForced,timeSlotevent,hasSteeringDiscount,deliveryFeeTier);
		request.setData(reservationParam);
		
		String inputJson = null;
		try {
			inputJson = buildRequest(request);
			
			String str = getData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_RESERVE_TIME), String.class);
			Response<FDReservationData> response =	getMapper().readValue(str, new TypeReference<Response<FDReservationData>>() {});
				
			
//			Response<FDReservationData> response =	httpGetDataTypeMap(inputJson,getFdCommerceEndPoint(DLV_MANAGER_RESERVE_TIME), );
			if (!response.getResponseCode().equals("OK")) {
				if(response.getRequestIdentifier()!=null&&response.getRequestIdentifier().equals("ReservationUnavailableException"))
					throw new ReservationUnavailableException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
				if(response.getRequestIdentifier()!=null&&response.getRequestIdentifier().equals("ReservationException"))
					throw new ReservationException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
			}
			
			FDReservationData data = response.getData();
			if(data==null)
				throw new FDResourceException("Technical Issue, please try again");
			DlvManagerDecoder.setMapper(getMapper());
			fdReservation = DlvManagerDecoder.converter(data);
			
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new FDResourceException(e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new FDResourceException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new FDResourceException(e);
		}
		return fdReservation; 
		
	}

}
