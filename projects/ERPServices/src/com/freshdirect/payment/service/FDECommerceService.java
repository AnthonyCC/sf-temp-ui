package com.freshdirect.payment.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.log4j.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import weblogic.auddi.util.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttribute;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecommerce.data.attributes.FlatAttributeCollection;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.accounts.external.UserTokenData;
import com.freshdirect.ecommerce.data.enums.BillingCountryInfoData;
import com.freshdirect.ecommerce.data.enums.CrmCaseSubjectData;
import com.freshdirect.ecommerce.data.enums.DeliveryPassTypeData;
import com.freshdirect.ecommerce.data.enums.EnumFeaturedHeaderTypeData;
import com.freshdirect.ecommerce.data.enums.ErpAffiliateData;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
import com.freshdirect.ecommerce.data.payment.BINData;
import com.freshdirect.ecommerce.data.sessionimpressionlog.SessionImpressionLogEntryData;
import com.freshdirect.ecommerce.data.survey.FDSurveyData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;


public class FDECommerceService extends AbstractService implements IECommerceService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDECommerceService.class);

	private static final String SAP_PRODUCT_FAMILY_LOADER_GET_SKUCODE_BYPRODFLY_API ="dataloader/sap/skucodesbyproductfamily";
	private static final String SAP_PRODUCT_FAMILY_LOADER_LOAD_API ="dataloader/sap/productfamily";
	private static final String PRODUCT_BY_PROMO_TYPE = "productPromo/type/";
	private static final String PROD_MATERIAL_ATTRIBUTES = "attributes/ids";
	private static final String GET_ACTIVE_BINS = "bin/activebins";
	private static final String SAVE_ACTIVE_BINS = "bin/storebins";
	private static final String SAVE_ATTRIBUTES = "attributes/attributecollection";
	private static final String LOAD_ATTRIBUTES_DATE = "attributes/timestamp/since/";
	private static final String ERP_BATCH_PROCESS_API = "erp/batch/";
	private static final String ERP_RECENT_BATCHES_API = "erp/recentBatches";
	
	private static final String ZONE_INFO_MASTER = "zoneInfo/master";
	private static final String ALL_ZONE_INFO_MASTER = "zoneInfo/allzoneinfoMaster";
	private static final String LOAD_ZONE_ID = "zoneInfo/findzoneid";
	private static final String LOAD_ZONE_BY_SERVICETYPE = "zoneInfo/zoneidByServiceType";
	private static final String ALL_ZONE_INFO = "zoneInfo/allZoneInfo";
	private static final String LOAD_ZONE_BY_ZONEIDS = "zoneInfo/zoneInfoByIds";
	private static final String LOAD_ZONE_ID_ISPICK = "zoneInfo/zoneidPickup";

	private static final String FAMILYID_FOR_MATERIAL = "productfamily/familyid";
	private static final String FAMILY_INFO = "productfamily/familyinfo";
	private static final String SKU_FAMILY_INFO = "productfamily/skufamilyinfo";
	private static final String USERID_BY_USERTOKEN = "account/external/userIdbyusertoken";
	private static final String USER_EMAIL_EXIST = "account/external/checkemailexist";
	private static final String LINK_USER_TOKEN = "account/external/linkusertoken";
	private static final String CHECK_EXT_LOGIN_USER = "account/external/checkexternalloginuser";
	
	private static final String CONNECTED_PROVIDERS_BY_USERID = "account/external/providerbyuserid";
	private static final String LOAD_ENUMS = "enums/all";
	private static final String BRAND_SEARCH_BY_KEY ="brand/products/search";
	private static final String BRAND_SEARCH_BY_PRODUCT ="brand/products/products";
	private static final String BRAND_LAST_SENT_FEED ="brand/products/ordertime";
	private static final String BRAND_ORDER_SUBMIT_BYDATE ="brand/products/orderdetailsbydate";
	private static final String BRAND_ORDER_SUBMIT_SALEIDS ="brand/products/orderdetailsbysaleids";

	private static final String EVENT_LOGGER ="event/logger/log";
	
	private static final String EXTOLE_MANAGER_CREATE ="extolemanager/create";
	private static final String EXTOLE_MANAGER_APPROVE ="extolemanager/approve";
	private static final String EXTOLE_MANAGER_UPDATE ="extolemanager/update";
	private static final String EXTOLE_MANAGER_SAVE ="extolemanager/save";
	private static final String EXTOLE_MANAGER_CREATECONVERSION ="extolemanager/createconversion";
	private static final String EXTOLE_MANAGER_APPROVECONVERSION ="extolemanager/approveconversion";
	private static final String EXTOLE_MANAGER_DOWNLOAD ="extolemanager/download";
	private static final String LOG_ECOUPON_ACTIVITY = "couponactivity/log";
	private static final String LOG_EWALLET_ACTIVITY = "ewalletactivity/log";
	
	private static final String SURVEY ="survey";
	private static final String STORE_SURVEY = "survey/store";
	private static final String SURVEY_RESPONSE = "survey/surveyresponse";
	private static final String GET_CUSTOMER_PROFILE = "survey/customerprofile";
	
	private static final String SAP_GROUP_PRICE_LOADER_LOAD_API ="dataloader/sapGrp/groupScalePrice";
	
	private static final String GET_COO_API ="/coo";

	private static final String GET_EWALLET_BY_ID = "erp/ewallet/findbyid/";
	private static final String GET_EWALLET_BY_TYPE = "erp/ewallet/findbytype/";
	private static final String GET_CUSTEWALLET_TOKEN_BY_CUSTID = "erp/ewallet/get/";
	private static final String UPDATE_CUSTEWALLET_TOKEN = "erp/ewallet/update/";
	private static final String DELETE_CUSTEWALLET_TOKEN = "erp/ewallet/delete/";
	private static final String INSERT_CUSTEWALLET_TOKEN = "erp/ewallet/save";
	
	private static final String SAVE_LOG_ENTRY = "sessionimpression/logentry";
	private static final String SAVE_LOG_ENTRIES = "sessionimpression/logentries";
	
	private static FDECommerceService INSTANCE;


	public static IECommerceService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDECommerceService();

		return INSTANCE;
	}
	/**
	 *  This method implementation is to Post data for the
	 * @param inputJson
	 * @param url
	 * @param clazz
	 * @return
	 * @throws FDResourceException
	 */
protected <T> T postData(String inputJson, String url, Class<T> clazz) throws FDResourceException {
		
		try {
 			HttpEntity<String> entity = getEntity(inputJson);
			RestTemplate restTemplate = getRestTemplate();
			ResponseEntity<T> response = restTemplate.postForEntity(new URI(url),
					entity, clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			LOGGER.info("input json:"+inputJson);
			throw new FDResourceException("EComm API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException("API syntax error");
		}
	}
	
	/**
	 *  This method implementation is to get data for the HTTP GET
	 * @param url
	 * @param clazz
	 * @return
	 * @throws FDResourceException
	 */
	protected <T> T getData( String url, Class<T> clazz) throws FDResourceException {
		
		try {
			RestTemplate restTemplate = getRestTemplate();		
			ResponseEntity<T> response = restTemplate.getForEntity(new URI(url),clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException("EComm API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException("API syntax error");
		}
	}
	protected <T,E> Response<T> httpGetDataTypeMap( String url, TypeReference<E> type) throws FDResourceException {
		Response<T> responseOfTypestring = null;
		RestTemplate restTemplate = getRestTemplate();	
		ResponseEntity<String> response;
		try {
			response = restTemplate.getForEntity(new URI(url),String.class);
			responseOfTypestring = getMapper().readValue(response.getBody(), type);
		} catch (JsonParseException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "Json Parsing failure");
		} catch (JsonMappingException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "Json Mapping failure");
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "API connection failure");
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "API Syntax failure");
		}
				
	return responseOfTypestring;
}
	protected <T,E> Response<T> postDataTypeMap(String inputJson, String url, TypeReference<E> type) throws FDResourceException {
		Response<T> responseOfTypestring = null;
		RestTemplate restTemplate = getRestTemplate();	
		ResponseEntity<String> response;
		
		HttpEntity<String> entity = getEntity(inputJson);
		try {
			response = restTemplate.postForEntity(new URI(url),entity, String.class);
			responseOfTypestring = getMapper().readValue(response.getBody(), type);
		} catch (JsonParseException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "Json Parsing failure");
		} catch (JsonMappingException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "Json Mapping failure");
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "API connection failure");
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException(e, "API Syntax failure");
		}
				
	return responseOfTypestring;
}
	
	@Override
	public Map<String,List<String>> updateCacheWithProdFly(
			List<String> familyIds) throws FDResourceException {
		try {
			Request<List<String>> request = new Request<List<String>>();
			request.setData(familyIds);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<Map<String,List<String>>> response = this.postData(inputJson, getFdCommerceEndPoint(SAP_PRODUCT_FAMILY_LOADER_GET_SKUCODE_BYPRODFLY_API), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public void loadData(List<ErpProductFamilyModel> productFamilyList)
			throws FDResourceException {
		try {
			Request<List<ErpProductFamilyModel>> request = new Request<List<ErpProductFamilyModel>>();
			request.setData(productFamilyList);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(SAP_PRODUCT_FAMILY_LOADER_LOAD_API), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	@Override
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(
			String ppType) throws FDResourceException {
	
			Response<Map<ZoneInfo, List<FDProductPromotionInfo>>> response;
			try {
				response = httpGetDataTypeMap(getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE+ppType), new TypeReference<Response<Map<ZoneInfo, List<FDProductPromotionInfo>>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					return response.getData();
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
		}
	@Override
	public NavigableMap<Long, BINInfo> getActiveBINs()
			throws FDResourceException {
		Response<NavigableMap<Long, BINData>> response;
	
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GET_ACTIVE_BINS), new TypeReference<Response<NavigableMap<Long, BINData>>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
				
				return buildBinInfoModel(response.getData());
	
		}
	@Override
	public void saveBINInfo(List<List<BINInfo>> binInfos)
			throws FDResourceException {Request<List<List<BINData>>> request = new Request<List<List<BINData>>>();
			List<BINData> binDataList = new ArrayList<BINData>(binInfos.size());
			List<List<BINData>> binDataRequest = new ArrayList<List<BINData>>();
			for (List<BINInfo> bininfol : binInfos) {
				for(BINInfo bininfo : bininfol){
				binDataList.add(buildBinDataModel(bininfo));
				}
				binDataRequest.add(binDataList);
			}
			request.setData(binDataRequest);
			String inputJson;
			Response<String> response = null;
			try {
				inputJson = buildRequest(request);
				 response = postData(inputJson, getFdCommerceEndPoint(SAVE_ACTIVE_BINS), Response.class);
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
			} catch (FDPayPalServiceException e) {
				
				throw new FDResourceException(response.getMessage());
			}
			}
	//Move the below method to util class
	private NavigableMap<Long, BINInfo> buildBinInfoModel(
			NavigableMap<Long, BINData> data) {
		NavigableMap<Long, BINInfo> binInfoData = new TreeMap();
		for(Long key: data.keySet()){
			BINData binData = data.get(key);
			BINInfo bINInfo = new BINInfo(binData.getId(), binData.getLowRange(), binData.getHighRange(), binData.getSequence(),EnumCardType.getEnum(binData.getCardType()));
			binInfoData.put(key, bINInfo);
		}
		return binInfoData;
	}
	//Move the below method to util class
	private BINData buildBinDataModel(BINInfo binInfo) {

			BINData binData = new BINData();
			binData.setId(binInfo.getId());
			binData.setLowRange(binInfo.getLowRange());
			binData.setHighRange(binInfo.getLowRange());
			binData.setSequence(binInfo.getSequence());
			binData.setCardType(binInfo.getCardType().getFdName());
		return binData;
	}
	@Override
	public com.freshdirect.content.attributes.FlatAttributeCollection getAttributes(String[] rootIds) {
		
		Response<FlatAttributeCollection> response = null;
		FlatAttributeCollection result = null;
		Request<String[]> request = new Request<String[]>();
			try {
				request.setData(rootIds);
				String inputJson;
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PROD_MATERIAL_ATTRIBUTES),new TypeReference<Response<FlatAttributeCollection>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (FDPayPalServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return buildFlatAttributeModel(response.getData());
	}

	@Override
	public void storeAttributes(
			com.freshdirect.content.attributes.FlatAttributeCollection attrs,
			String user, String sapId) throws FDResourceException {
		
		String inputJson;
		Response<String> response = null;
		try {
			inputJson = buildRequest(buildFlatAttributeModel(attrs));
			 response = postData(inputJson, getFdCommerceEndPoint(SAVE_ATTRIBUTES+"?sapId="+sapId+"&user="+user), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDPayPalServiceException e) {
			
			throw new FDResourceException(response.getMessage());
		}
	}
	private com.freshdirect.content.attributes.FlatAttributeCollection buildFlatAttributeModel(
			FlatAttributeCollection data) {
			List<FlatAttribute> lst = new ArrayList();
			for(com.freshdirect.ecommerce.data.attributes.FlatAttribute flatAtt:data.getFlatAttributes()){
				FlatAttribute flata = new FlatAttribute(flatAtt.getIdPath(), flatAtt.getName(), flatAtt.getValue());
				lst.add(flata);
			}
			com.freshdirect.content.attributes.FlatAttributeCollection  flatCollection = new com.freshdirect.content.attributes.FlatAttributeCollection(lst);
			
		return flatCollection;
	}
	private FlatAttributeCollection buildFlatAttributeModel(
			com.freshdirect.content.attributes.FlatAttributeCollection data) {
			List<com.freshdirect.ecommerce.data.attributes.FlatAttribute> lst = new ArrayList();
			for(FlatAttribute flatAtt:data.getFlatAttributes()){
				com.freshdirect.ecommerce.data.attributes.FlatAttribute flata = new com.freshdirect.ecommerce.data.attributes.FlatAttribute(flatAtt.getIdPath(), flatAtt.getName(), flatAtt.getValue());
				lst.add(flata);
			}
			FlatAttributeCollection  flatCollection = new FlatAttributeCollection(lst);
			
		return flatCollection;
	}
	@Override
	public Map loadAttributes(Date since) throws AttributeException {
		
		Response<Map<String, List<com.freshdirect.ecommerce.data.attributes.FlatAttribute>>> response = null;
			try {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = format1.format(since); 
				response =httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ATTRIBUTES_DATE+date1),new TypeReference<Response<Map<String, List<com.freshdirect.ecommerce.data.attributes.FlatAttribute>>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
			} catch (FDResourceException e) {
				throw new AttributeException(e);
			}
			Map<String, List<FlatAttribute>> results = new HashMap();
			Map<String, List<com.freshdirect.ecommerce.data.attributes.FlatAttribute>> responses = response.getData();
			for(Map.Entry<String, List<com.freshdirect.ecommerce.data.attributes.FlatAttribute>> entry : responses.entrySet()){
				List<FlatAttribute> valueList = new ArrayList<FlatAttribute>();
				List<com.freshdirect.ecommerce.data.attributes.FlatAttribute> valueListinternal = entry.getValue();
				for(com.freshdirect.ecommerce.data.attributes.FlatAttribute flatAtt:valueListinternal){
					FlatAttribute flata = new FlatAttribute(flatAtt.getIdPath(), flatAtt.getName(), flatAtt.getValue());
					flata.setLastModified(flatAtt.getLastModifiedDate());
					valueList.add(flata);
				}
				results.put(entry.getKey(), valueList);
			}
			return results;
	}
	@Override
	public BatchModel getBatch(int batchId) throws FDResourceException {
		Response<BatchModel> response;
		
		response = httpGetDataTypeMap(getFdCommerceEndPoint(ERP_BATCH_PROCESS_API+batchId), new TypeReference<Response<BatchModel>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
			return response.getData();

	}
	@Override
	public Collection getRecentBatches() throws FDResourceException {
		Response<Collection<BatchModel>> response;
		
		response = httpGetDataTypeMap(getFdCommerceEndPoint(ERP_RECENT_BATCHES_API), new TypeReference<Response<Collection<BatchModel>>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
			return response.getData();

	}
	
	@Override
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException, FDResourceException {
		Response<ErpZoneMasterInfo> response = new Response<ErpZoneMasterInfo>();
		response = httpGetDataTypeMap(getFdCommerceEndPoint(ZONE_INFO_MASTER+"?zoneId="+ zoneId), new TypeReference<Response<ErpZoneMasterInfo>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
			return response.getData();
	}

	@Override
	public Collection<Object> loadAllZoneInfoMaster() throws RemoteException, FDResourceException {
		/*Response<Collection<Object>> response;
		response = httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ALL_ZONE_INFO), new TypeReference<Response<Collection<Object>>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();*/
		

			Response<Collection<Object>> response = getData(getFdCommerceEndPoint(ALL_ZONE_INFO_MASTER), Response.class);
			return response.getData();

		
	}
	@Override
	public String findZoneId(String zoneServiceType, String zipCode) throws RemoteException, FDResourceException{
		Response<String> response = new Response<String>(); 
	
			response = getData(getFdCommerceEndPoint(LOAD_ZONE_ID)+"?zoneServiceType="+zoneServiceType+"&zipCode="+zipCode, Response.class);
	
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}
	@Override
	public String findZoneId(String zoneServiceType, String zipCode, boolean isPickupOnlyORNotServicebleZip) throws RemoteException{
		Response<String> response = new Response<String>(); 
	
			try {
				response = getData(getFdCommerceEndPoint(LOAD_ZONE_ID_ISPICK)+"?zoneServiceType="+zoneServiceType+"&zipCode="+zipCode+"&isPickup="+isPickupOnlyORNotServicebleZip, Response.class);
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return response.getData();
	}
	
	@Override
	public String findZoneId(String servType) throws RemoteException {
		Response<String> response = new Response<String>(); 

			try {
				response = getData(getFdCommerceEndPoint(LOAD_ZONE_BY_SERVICETYPE)+"?zoneServiceType="+servType, Response.class);
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return response.getData();
	}

	@Override
	public List<ErpZoneMasterInfo> getAllZoneInfoDetails()
			throws RemoteException {
		Response<List<ErpZoneMasterInfo>> response = new Response<List<ErpZoneMasterInfo>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(ALL_ZONE_INFO), new TypeReference<Response<List<ErpZoneMasterInfo>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return response.getData();
	}
	
	@Override
	public Collection<Object> findZoneInfoMaster(String[] zoneIds)
			throws RemoteException {
		Response<Collection<Object>> response = new Response<Collection<Object>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ZONE_BY_ZONEIDS), new TypeReference<Collection<Object>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return response.getData();
	}
	
	
	
	
	
	@Override
	public String getFamilyIdForMaterial(String matId) throws RemoteException, FDResourceException {
		Response<String> response = new Response<String>();
		try{
			response = httpGetData(getFdCommerceEndPoint(FAMILYID_FOR_MATERIAL)+"?matId=", Response.class);
		} catch(FDPayPalServiceException e){
			throw new FDResourceException(e, e.getMessage());
		} 
		return response.getData();
	}
	@Override
	public ErpProductFamilyModel findFamilyInfo(String familyId) throws RemoteException, FDResourceException {
		Response<ErpProductFamilyModel> response = new Response<ErpProductFamilyModel>();
		response = httpGetDataTypeMap(getFdCommerceEndPoint(FAMILY_INFO+"?familyId="+ familyId), new TypeReference<Response<ErpProductFamilyModel>>() {});
		return response.getData();
	}
	@Override
	public ErpProductFamilyModel findSkuFamilyInfo(String materialId) throws RemoteException, FDResourceException {
		Response<ErpProductFamilyModel> response = new Response<ErpProductFamilyModel>();
		response = httpGetDataTypeMap(getFdCommerceEndPoint(SKU_FAMILY_INFO+"?materialId="+materialId), new TypeReference<Response<ErpProductFamilyModel>>() {});
		return response.getData();
	}
	@Override
	public String getUserIdForUserToken(String userToken) {
		Response<String> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(USERID_BY_USERTOKEN+"?userToken="+ userToken),
					new TypeReference<Response<String>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isUserEmailAlreadyExist(String email) {
		Response<Boolean> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(USER_EMAIL_EXIST+"?emailId="+email),new TypeReference<Response<Boolean>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public int isUserEmailAlreadyExist(String email, String provider) {
		Response<Integer> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(USER_EMAIL_EXIST+"/provider?emailId="+ email +"&provider=" + provider),new TypeReference<Response<Integer>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void linkUserTokenToUserId(String customerId, String userId,
			String userToken, String identityToken, String provider,
			String displayName, String preferredUserName, String email,
			String emailVerified) {
		Request<UserTokenData> request = new Request<UserTokenData>();
		UserTokenData userTokenData = buildUserTokenData(customerId, userId,
				userToken, identityToken, provider, displayName,
				preferredUserName, email, emailVerified);
		request.setData(userTokenData);
		Response<String> response = null;
		String inputJson = null;
		try {
			inputJson = buildRequest(request);
			response = postData(inputJson,
					getFdCommerceEndPoint(LINK_USER_TOKEN), Response.class);
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDPayPalServiceException e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
		} catch (FDResourceException e) {
			Logger.error(e.getMessage());
			e.printStackTrace();
		}

	}
	private UserTokenData buildUserTokenData(String customerId, String userId,
			String userToken, String identityToken, String provider,
			String displayName, String preferredUserName, String email,
			String emailVerified) {
		UserTokenData userTokenData = new UserTokenData();
		userTokenData.setCustomerId(customerId);
		userTokenData.setUserId(userId);
		userTokenData.setUserToken(userToken);
		userTokenData.setIdentityToken(identityToken);
		userTokenData.setProvider(provider);
		userTokenData.setDisplayName(displayName);
		userTokenData.setPreferredUserName(preferredUserName);
		userTokenData.setEmail(email);
		userTokenData.setEmailVerified(emailVerified);
		return userTokenData;
	}
	@Override
	public List<String> getConnectedProvidersByUserId(String userId,
			EnumExternalLoginSource source) {
		
		Response<List<String>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(CONNECTED_PROVIDERS_BY_USERID+"?userId="+userId+"&source="+source.name()),new TypeReference<Response<List<String>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isExternalLoginOnlyUser(String userId,
			EnumExternalLoginSource source) {
		Response<Boolean> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(CHECK_EXT_LOGIN_USER+"?userId="+userId+"&source="+source),new TypeReference<Response<Boolean>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	
	}
	@Override
	public void unlinkExternalAccountWithUser(String email, String userToken,
			String provider) {
		try {
			String inputJson;
			inputJson = buildRequest(null);
			postDataTypeMap(inputJson, getFdCommerceEndPoint("account/external/unlink?email="
					+ email
					+ "&provider="
					+ provider
					+ "&userToken="
					+ userToken), new TypeReference<Response<Void>>() {});
			
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void unlinkExternalAccountWithUser(String customerId, String provider) {
		try {
			String inputJson;
			inputJson = buildRequest(null);
			postDataTypeMap(inputJson,
					getFdCommerceEndPoint("account/external/unlink/provider?customerId="+customerId+"&provider="+provider),new TypeReference<Response<Void>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public boolean isSocialLoginOnlyUser(String customer_id) {
		Response<Boolean> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint("account/external/checksocialloginuser?customerId="+customer_id),new TypeReference<Response<Boolean>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	
	}
	@Override
	public List<String> getConnectedProvidersByUserId(String userId) {

		
		Response<List<String>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint("/account/external/providers?userId="+userId),new TypeReference<Response<List<String>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return response.getData();
	
	}
	@Override
	public <E> List loadEnum(String daoClassName) throws RemoteException {
		Response<List> response = null;
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ENUMS+"?daoClassName="+daoClassName),(TypeReference<E>)typeReferenceFor(daoClassName));
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException();
		}

		return buildServiceEnumModel(response.getData());
	}
	
	private List buildServiceEnumModel(List data) {
		if(data.size()>0){
			if(data.get(1) instanceof BillingCountryInfoData ){
				return ModelConverter.buildBillingCountryInfoList(data);
			}else if(data.get(1) instanceof ErpAffiliateData ){
				return ModelConverter.buildErpAffiliateList(data);
			}else if(data.get(1) instanceof DeliveryPassTypeData ){
				return ModelConverter.buildDeliveryPassTypeList(data);
			}else if(data.get(1) instanceof EnumFeaturedHeaderTypeData ){
				return ModelConverter.buildEnumFeaturedHeaderTypeList(data);
			}else if(data.get(1) instanceof CrmCaseSubjectData ){
				return ModelConverter.buildCrmCaseSubjectList(data);
			}
		}

		return null;
	}
	private <E> E typeReferenceFor(String daoClassName) {
		
		
		if (daoClassName.equals("BillingCountryDAO")) {
			return   (E) new TypeReference<Response<List<BillingCountryInfoData>>>(){} ;
		} else if (daoClassName.equals("ErpAffiliateDAO")) {
			return  (E) new TypeReference<Response<List<ErpAffiliateData>>>() {} ;
		} else if (daoClassName.equals("DlvPassTypeDAO")) {
			return  (E) new TypeReference<Response<List<DeliveryPassTypeData>>>() {} ;
		} else if (daoClassName.equals("EnumFeaturedHeaderTypeDAO")) {
			return  (E) new TypeReference<Response<List<EnumFeaturedHeaderTypeData>>>() {} ;
		} else if (daoClassName.equals("CrmCaseSubjectDAO")) {
			return   (E) new TypeReference<Response<List<CrmCaseSubjectData>>>() {} ;
		}
		return null;
	}
	@Override
	public 	Map<ErpCOOLKey, ErpCOOLInfo> getCountryOfOriginData(Date since)
			throws RemoteException {
			Response<List<CountryOfOriginData>> response = new Response<List<CountryOfOriginData>>();

				try {
					response= httpGetDataTypeMap(getFdCommerceEndPoint(GET_COO_API),  new TypeReference<Response<List<CountryOfOriginData>>>() {});
					
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Map<ErpCOOLKey, ErpCOOLInfo> data = ModelConverter.buildCoolModel(response.getData());
			return data;
		
	}
	/*@Override // Will be removed
	public 	void updateCOOLInfo(List<ErpCOOLInfo> erpCOOLInfoList)
			throws RemoteException {
		
					Request<List<CountryOfOriginData>> request = new Request<List<CountryOfOriginData>>();
					request.setData(ModelConverter.buildCoolModelData(erpCOOLInfoList));
					try {
						String inputJson = buildRequest(request);
	
					Response<List<CountryOfOriginData>> response = this.postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_COO_API),  new TypeReference<Response<List<CountryOfOriginData>>>() {});
					
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FDPayPalServiceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
	}
	*/
	@Override
	public HLBrandProductAdResponse getSearchbykeyword(
			HLBrandProductAdRequest hLRequestData) throws RemoteException {
		try {
			Request<HLBrandProductAdRequest> request = new Request<HLBrandProductAdRequest>();
			request.setData(hLRequestData);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response = this.postData(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_KEY), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public HLBrandProductAdResponse getCategoryProducts(
			HLBrandProductAdRequest hLRequestData) throws RemoteException {
		try {
			Request<HLBrandProductAdRequest> request = new Request<HLBrandProductAdRequest>();
			request.setData(hLRequestData);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response = this.getData(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_PRODUCT), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public Date getLastSentFeedOrderTime() throws RemoteException {
		try {
			Request<HLBrandProductAdRequest> request = new Request<HLBrandProductAdRequest>();
			@SuppressWarnings("unchecked")
			Response<Date> response = this.getData(getFdCommerceEndPoint(BRAND_LAST_SENT_FEED), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void submittedOrderdDetailsToHL(Date orderFeedDateFrom)
			throws RemoteException {
		try {
			Request<Date> request = new Request<Date>();
			request.setData(orderFeedDateFrom);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response = this.postData(inputJson, getFdCommerceEndPoint(BRAND_ORDER_SUBMIT_BYDATE), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void submittedOrderdDetailsToHL(List<String> ordersList)
			throws RemoteException {
		try {
			Request<List<String>> request = new Request<List<String>>();
			request.setData(ordersList);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response = this.postData(inputJson, getFdCommerceEndPoint(BRAND_ORDER_SUBMIT_SALEIDS), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<ExtoleConversionRequest> getExtoleCreateConversionRequest() throws FDResourceException,
			RemoteException {
		Response<List<ExtoleConversionRequest>> response = new Response<List<ExtoleConversionRequest>>();
		try{
			response = httpGetData(getFdCommerceEndPoint(EXTOLE_MANAGER_CREATE), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch(FDPayPalServiceException e){
			throw new FDResourceException(e, e.getMessage());
		} 
		return response.getData();
		
	}
	@Override
	public List<ExtoleConversionRequest> getExtoleApproveConversionRequest() throws FDResourceException,
			RemoteException {
		Response<List<ExtoleConversionRequest>> response = new Response<List<ExtoleConversionRequest>>();
		try{
			response = httpGetData(getFdCommerceEndPoint(EXTOLE_MANAGER_APPROVE), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch(FDPayPalServiceException e){
			throw new FDResourceException(e, e.getMessage());
		} 
		return response.getData();
	}
	@Override
	public void updateConversionRequest(ExtoleResponse convResponse) throws FDResourceException,
			RemoteException {
		Request<ExtoleResponse> request = new Request<ExtoleResponse>();
		request.setData(convResponse);
		Response<HLBrandProductAdResponse> response = null;
		String inputJson;
		try {
			inputJson = buildRequest(request.getData());
			response = this.postData(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_UPDATE), Response.class);
			if(!response.getResponseCode().equals("CREATED")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			throw new FDResourceException(response.getMessage());
		}
		
	}
	@Override
	public void saveExtoleRewardsFile(List<FDRafCreditModel> rewards) throws FDResourceException,
			RemoteException {
		Request<List<FDRafCreditModel>> request = new Request<List<FDRafCreditModel>>();
		request.setData(rewards);
		Response<HLBrandProductAdResponse> response = null;
		String inputJson;
		try {
			inputJson = buildRequest(request.getData());
			response = this.postData(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_SAVE), Response.class);
			if(!response.getResponseCode().equals("CREATED")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			throw new FDResourceException(e.getMessage());
		}
		
	}
	@Override
	public void createConversion() throws ExtoleServiceException, IOException, FDResourceException,
			RemoteException {

		String inputJson=null;
		Response<String> response = null;
		response = postData(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_CREATECONVERSION), Response.class);
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}else if(response.getResponseCode().equals(HttpStatus.BAD_GATEWAY)){
			throw new ExtoleServiceException(response.getMessage());
		}
	}
	@Override
	public void approveConversion() throws ExtoleServiceException, IOException, FDResourceException,
			RemoteException {
		String inputJson=null;
		Response<String> response = null;
		response = postData(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_APPROVECONVERSION), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		
	}
	@Override
	public void downloadAndSaveRewards(String fileName) throws ExtoleServiceException, IOException,
			FDResourceException, RemoteException, ParseException {
		String inputJson=null;
		Response<String> response = null;
		response = postData(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_DOWNLOAD+"?fileName="+fileName), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		
	}

	@Override
	public void log(FDWebEvent fdWebEvent) throws RemoteException {
		try {
			Request<FDWebEvent> request = new Request<FDWebEvent>();
			request.setData(fdWebEvent);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<Void> response = this.getData(inputJson, getFdCommerceEndPoint(EVENT_LOGGER), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
//			return response.getData();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	@Override
	public void logCouponActivity(FDCouponActivityLogModel log)throws FDResourceException,RemoteException{
	
		try {
			Request<FDCouponActivityLogModel> couponActivityLogReq = new Request<FDCouponActivityLogModel>();
			couponActivityLogReq.setData(log);
			String inputJson = buildRequest(couponActivityLogReq);
			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(LOG_ECOUPON_ACTIVITY), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void logActivity(EwalletActivityLogModel logModel)throws RemoteException{
		
		try {
			Request<EwalletActivityLogModel> ewalletActivityLogReq = new Request<EwalletActivityLogModel>();
			ewalletActivityLogReq.setData(logModel);
			String inputJson = buildRequest(ewalletActivityLogReq);
			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(LOG_EWALLET_ACTIVITY), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public FDSurveyData getSurvey(SurveyKeyData key) throws RemoteException {
		Response<FDSurveyData> response = new Response<FDSurveyData>();
		try {
			Request<SurveyKeyData> request = new Request<SurveyKeyData>();
			request.setData(key);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SURVEY), new TypeReference<Response<FDSurveyData>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public FDSurveyResponseData getCustomerProfile(FDIdentity identity,
			EnumServiceType serviceType) throws RemoteException {
		Response<FDSurveyResponseData> response= null;
		try {
			Request<SurveyData> request = buildSurveyData(identity, serviceType);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_CUSTOMER_PROFILE), new TypeReference<Response<FDSurveyResponseData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	private Request<SurveyData> buildSurveyData(FDIdentity identity,
			EnumServiceType serviceType) {
		Request<SurveyData> request = new Request<SurveyData>();
		SurveyData surveyData = new SurveyData();
		surveyData.setErpCustomerid(identity.getErpCustomerPK());
		surveyData.setFdCustomerId(identity.getFDCustomerPK());
		surveyData.setServiceType(serviceType.toString());
		request.setData(surveyData);
		return request;
	}
	@Override
	public FDSurveyResponseData getSurveyResponse(FDIdentity identity, SurveyKeyData key) throws RemoteException {
		Response<FDSurveyResponseData> response = null;
		try {
			Request<SurveyData> request = new Request<SurveyData>();
			SurveyData surveyData = new SurveyData();
			surveyData.setErpCustomerid(identity.getErpCustomerPK());
			surveyData.setFdCustomerId(identity.getFDCustomerPK());
			surveyData.setServiceType(key.getUserType().toString());
			surveyData.setSurveyType(key.getSurveyType());
			request.setData(surveyData);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SURVEY_RESPONSE), new TypeReference<Response<FDSurveyResponseData>>() {});
			
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public void storeSurvey(FDSurveyResponseData survey) throws FDResourceException {
		try {
			String inputJson = buildRequest(survey);
			Response<FDSurveyResponseData> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_SURVEY), new TypeReference<Response<Void>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	@Override
	public void loadGroupPriceData(List<ErpGrpPriceModel> grpPricelist)
			throws FDResourceException {
			try {
				Request<List<ErpGrpPriceModel>> request = new Request<List<ErpGrpPriceModel>>();
				request.setData(grpPricelist);
				String inputJson = buildRequest(request);
				@SuppressWarnings("unchecked")
				Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(SAP_GROUP_PRICE_LOADER_LOAD_API), Response.class);
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
			} catch (FDPayPalServiceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
			
	}
	
	@Override
	public ErpEWalletModel findEWalletById(String eWalletId) throws RemoteException {
		try {
			Response<ErpEWalletModel> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_EWALLET_BY_ID) + eWalletId,
					new TypeReference<Response<ErpEWalletModel>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public ErpEWalletModel findEWalletByType(String eWalletType) throws RemoteException {
		try {
			Response<ErpEWalletModel> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_EWALLET_BY_TYPE) + eWalletType,
					new TypeReference<Response<ErpEWalletModel>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public ErpCustEWalletModel getLongAccessTokenByCustID(String custID, String eWalletType) throws RemoteException {
		try {
			Response<ErpCustEWalletModel> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CUSTEWALLET_TOKEN_BY_CUSTID) + custID + "/" + eWalletType, new TypeReference<Response<ErpCustEWalletModel>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public int updateLongAccessToken(String custId, String longAccessToken, String eWalletType) throws RemoteException {
		try {
			Response<Integer> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPDATE_CUSTEWALLET_TOKEN) + custId + "/" + longAccessToken + "/" + eWalletType,
					new TypeReference<Response<Integer>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public int deleteLongAccessToken(String custId, String eWalletID) throws RemoteException {
		try {
			Response<Integer> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(DELETE_CUSTEWALLET_TOKEN) + custId + "/" + eWalletID,
					new TypeReference<Response<Integer>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<ErpEWalletModel> getAllEWallets() throws RemoteException {
		try {
			@SuppressWarnings("unchecked")
			Response<List<ErpEWalletModel>> response = this.getData(getFdCommerceEndPoint(""), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public int insertCustomerLongAccessToken(ErpCustEWalletModel custEWallet) throws RemoteException {
		try {
			Request<ErpCustEWalletModel> request = new Request<ErpCustEWalletModel>();
			if(custEWallet.getPK() == null){	// While inserting into DB it will generate the new ID.
				custEWallet.setPK(new PrimaryKey());
			}
			request.setData(custEWallet);
			String inputJson = buildRequest(request);
			Response<Integer> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(INSERT_CUSTEWALLET_TOKEN), new TypeReference<Response<Integer>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void saveLogEntry(Request<SessionImpressionLogEntryData> entry) throws FDResourceException {
		try {
			String inputJson = buildRequest(entry);
			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SAVE_LOG_ENTRY), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	
	@Override
	public void saveLogEntries(Request<Collection<SessionImpressionLogEntryData>> entries) throws FDResourceException,RemoteException {
		try {
			String inputJson = buildRequest(entries);
			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SAVE_LOG_ENTRIES), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}	

}
