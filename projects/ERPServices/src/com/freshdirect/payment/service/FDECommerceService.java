package com.freshdirect.payment.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.ObjectNotFoundException;

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
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttribute;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecommerce.data.attributes.FlatAttributeCollection;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.ErpActivityRecordData;
import com.freshdirect.ecommerce.data.customer.ErpGrpPriceModelData;
import com.freshdirect.ecommerce.data.customer.accounts.external.UserTokenData;
import com.freshdirect.ecommerce.data.delivery.AbstractRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AddressAndRestrictedAdressData;
import com.freshdirect.ecommerce.data.delivery.AddressRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.OneTimeReverseRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictedAddressModelData;
import com.freshdirect.ecommerce.data.delivery.RestrictionData;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.ecommerce.data.delivery.sms.SmsAlertETAInfoData;
import com.freshdirect.ecommerce.data.delivery.sms.SmsOrderData;
import com.freshdirect.ecommerce.data.dlv.AddressData;
import com.freshdirect.ecommerce.data.dlv.ContactAddressData;
import com.freshdirect.ecommerce.data.dlv.FutureZoneNotificationParam;
import com.freshdirect.ecommerce.data.dlv.MunicipalityInfoData;
import com.freshdirect.ecommerce.data.dlv.OrderContextData;
import com.freshdirect.ecommerce.data.dlv.ReservationParam;
import com.freshdirect.ecommerce.data.dlv.StateCountyData;
import com.freshdirect.ecommerce.data.dlv.TimeslotEventData;
import com.freshdirect.ecommerce.data.enums.BillingCountryInfoData;
import com.freshdirect.ecommerce.data.enums.CrmCaseSubjectData;
import com.freshdirect.ecommerce.data.enums.DeliveryPassTypeData;
import com.freshdirect.ecommerce.data.enums.EnumFeaturedHeaderTypeData;
import com.freshdirect.ecommerce.data.enums.ErpAffiliateData;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpRestrictedAvailabilityData;
import com.freshdirect.ecommerce.data.erp.inventory.RestrictedInfoParam;
import com.freshdirect.ecommerce.data.erp.material.OverrideSkuAttrParam;
import com.freshdirect.ecommerce.data.erp.material.SkuPrefixParam;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductPromotionPreviewInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.FDProductPromotionInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoDataWrapper;
import com.freshdirect.ecommerce.data.fdstore.FDGroupData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.fdstore.GroupScalePricingData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoFDGroupWrapper;
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.ecommerce.data.payment.BINData;
import com.freshdirect.ecommerce.data.payment.FDGatewayActivityLogModelData;
import com.freshdirect.ecommerce.data.routing.SubmitOrderRequestData;
import com.freshdirect.ecommerce.data.rules.RuleData;
import com.freshdirect.ecommerce.data.sessionimpressionlog.SessionImpressionLogEntryData;
import com.freshdirect.ecommerce.data.smartstore.ProductFactorParam;
import com.freshdirect.ecommerce.data.smartstore.ScoreResult;
import com.freshdirect.ecommerce.data.survey.FDSurveyData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.EnumRestrictedAddressReason;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.SiteAnnouncement;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;
import com.freshdirect.rules.Rule;
import com.freshdirect.sms.model.st.STSmsResponse;
//import com.freshdirect.content.attributes.FlatAttributeCollection;
//import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;


public class FDECommerceService extends AbstractService implements IECommerceService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDECommerceService.class);

	private static final String SAP_PRODUCT_FAMILY_LOADER_GET_SKUCODE_BYPRODFLY_API ="dataloader/sap/skucodesbyproductfamily";
	private static final String SAP_PRODUCT_FAMILY_LOADER_LOAD_API ="dataloader/sap/productfamily";
	private static final String PRODUCT_BY_PROMO_TYPE = "productPromo/type/";
	private static final String PROMOTION_BY_TYPE = "productPromo/promtype/";
	private static final String PROMOTION_PREVIEW = "productPromo/";
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

	private static final String DLV_MANAGER_FUTURENOTIFICATION = "dlvmanager/futurezonealert";
	private static final String DLV_MANAGER_FDANNOUNCEMENT = "dlvmanager/fdannouncement";
	private static final String DLV_MANAGER_LOGFAILED_FDXDORDER = "dlvmanager/logfailedorder";
	private static final String DLV_MANAGER_MUNICIPALITY_INFO = "dlvmanager/municipalityinfo";
	private static final String DLV_MANAGER_SEND_ORDER_FEED = "dlvmanager/transmitorderinformation";
	private static final String DLV_MANAGER_SEND_LATE_ORDER_FEED = "dlvmanager/deliveryattemptinfo";
	private static final String DLV_MANAGER_COMMIT_RESERVATION = "dlvmanager/savereservation";
	private static final String DLV_MANAGER_RECOMMIT_RESERVATION = "dlvmanager/recommitreservation";
	private static final String DLV_MANAGER_COUNTIES_BY_STATE = "dlvmanager/countiesbystate";
	private static final String DLV_MANAGER_RELEASE_ORDER_LOCK = "dlvmanager/releaseorderlock";
	private static final String DLV_MANAGER_COUNTIES_BY_ZIP = "dlvmanager/countiesbyzip";
	private static final String DLV_MANAGER_CARTON_SCAN_INFO = "dlvmanager/cartoninfo";
	private static final String DLV_MANAGER_MISSING_ORDER = "dlvmanager/updateorder";
	
	private static final String ERP_INVENTORY_UPDATE = "erpinventory/updateinventory";
	private static final String ERP_INVENTORY_UPDATE_RESTRICT_INFO = "erpinventory/updaterestrictedinfos";

	private static final String SMS_ALERT_OPTIN = "sms/optin";
	private static final String SMS_ALERT_OPTIN_NONMARKETING = "sms/optin/nonmarketing";
	private static final String SMS_ALERT_OPTIN_MARKETING = "sms/optin/marketing";
	private static final String SMS_ALERT_ORDER_CANCEL = "sms/order/cancel";
	private static final String SMS_ALERT_ORDER_CONFIRM = "sms/order/confirm";
	private static final String SMS_ALERT_ORDER_MODIFY = "sms/order/modify";
	private static final String SMS_ALERT_EXPIRE_OPTIN = "sms/optin/expire";
	private static final String SMS_TO_GATEWAY = "sms/sendtogateway";
	private static final String SMS_MESSAGE_UPDATE = "sms/update";

	private static final String LOG_EVENT_RECOMMENDATION = "eventlogger/recommendation/log";
	private static final String LOG_BATCHEVENT_RECOMMENDATION = "eventlogger/recommendation/logbatch";
	
	
	private static final String SMARTSTORE_PERSONALIZED_FACTORS = "scorefactor/personalfactorscores";
	private static final String SMARTSTORE_GLOBAL_FACTORS = "scorefactor/globalfactorscores";
	private static final String SMARTSTORE_PERSONALIZED_FACTORS_NAME = "scorefactor/personalfactorsname";
	private static final String SMARTSTORE_GLOBAL_FACTORS_NAME = "scorefactor/globalfactorsname";
	private static final String SMARTSTORE_GLOBAL_PRODUCTS = "scorefactor/globalproducts";
	private static final String SMARTSTORE_PERSONALIZED_PRODUCTS = "scorefactor/personalproducts";
	private static final String SMARTSTORE_PRODUCT_RECOMMENDATIONS = "scorefactor/recommendglobalproducts";
	private static final String SMARTSTORE_PERSONAL_RECOMMENDATIONS = "scorefactor/recommendpersonalproducts";
	private static final String SMARTSTORE_PREFERRED_WINEPRICE = "scorefactor/wineprice";
	
	private static FDECommerceService INSTANCE;
	
	private static final String ERP_GROUP_SCALE_API ="groupScale";
	private static final String ERP_GROUP_SCALE_IDS_API ="groupScale/grpInfoByIds";
	private static final String ERP_GROUP_SCALE_ID_API ="groupScale/grpInfoById";
	private static final String ERP_GROUP_INFO_BY_MATID_API ="groupScale/grpInfoByMatId";
	private static final String ERP_GROUP_IDENTITY_BY_MATID_API ="groupScale/grpIdentityByMatId";
	private static final String ERP_GROUP_FILTERED_SKU_API ="groupScale/getFilteredSkus";
	private static final String ERP_ALL_GROUP_INFO_API ="groupScale/getAllGrpInfoMaster";
	private static final String ERP_LATEST_VER_FOR_GRPID_API ="groupScale/getLatestVerNumb";
	private static final String ERP_GRP_FOR_MAT_ID_API ="groupScale/getGrpforMatId";
	private static final String ERP_LATEST_GRP_API ="groupScale/getLatestActGrp";
	private static final String ERP_LAST_MOD_GRP_API ="groupScale/lastModifiedGroups";
	private static final String LOG_GATEWAY_ACTIVITY = "gatewayactivity/log";

	private static final String GET_RULE = "rule/findByRuleId";
	private static final String DELETE_RULE = "rule/delete";
	private static final String STORE_RULES = "rule/store";
	private static final String GET_RULES = "rule";

	private static final String ACTIVITY_TEMPLATE = "activity/template";
	private static final String CC_ACTIVITIES = "activity/ccactivity";
	private static final String LOG_ACTIVITY_NEWTXN = "activity/log/newTx";
	private static final String LOG_ACTIVITY = "activity/log";
	private static final String FILTER_LISTS = "activity/log/filterlist";
	
	
	
	private static final String ERP_MATERIALINFO_VERSION = "erpinfo/materialbybatch";
	private static final String ERP_MATERIALINFO_SAPID = "erpinfo/materialsbysapid";
	private static final String ERP_MATERIALINFO_SKUCODE = "erpinfo/materialsbyskucode";
	private static final String ERP_MATERIALINFO_DESCRIPTION = "erpinfo/materialsbydescription";
	private static final String ERP_MATERIALINFO_CHARACTERISTIC = "erpinfo/materialsbyclassandcharacteristic";
	private static final String ERP_MATERIALSINFO_CLASS= "erpinfo/materialsbyclass";
	private static final String ERP_PRODUCTINFO_SKUCODE = "erpinfo/productbysku";
	private static final String ERP_PRODUCTINFO_SKUCODE_VERSION = "erpinfo/productbyskuandversion";
	private static final String ERP_PRODUCT_SKUS = "erpinfo/productsbyskus";
	private static final String ERP_PRODUCTINFO_SAPID = "erpinfo/productbysapid";
	private static final String ERP_SKUS_SAPID = "erpinfo/skusbysapid";
	private static final String ERP_PRODUCTINFO_DESCRIPTION = "erpinfo/productsbydescription";
	private static final String ERP_PRODUCTINFO_LIKESKU = "erpinfo/productslikesku";
	private static final String ERP_PRODUCTINFO_UPC = "erpinfo/productsbyupc";
	private static final String ERP_PRODUCTINFO_CUSTOMER_UPC = "erpinfo/productsbycustomerupc";
	private static final String ERP_PRODUCTINFO_LIKEUPC = "erpinfo/productslikeupc";
	private static final String ERP_INVENTORY_INFO = "erpinfo/inventoryinfo";
	private static final String ERP_LOAD_INVENTORY_INFO = "erpinfo/loadinventory";
	
	private static final String ERP_NEW_SKUS_DAYS = "erpinfo/newskucodes";
	private static final String ERP_SKUS_OLDNESS = "erpinfo/skuoldness";
	private static final String ERP_REINTRODUCED_SKUCODES = "erpinfo/reintroducedskucodes";
	private static final String ERP_OUTOFSTOCK_SKUCODES = "erpinfo/outofstockskucodes";
	private static final String ERP_SKUS_BY_DEAL = "erpinfo/skusbydeal";
	private static final String ERP_PEAK_PRODUCE_SKUS_BY_DEPARTMENT = "erpinfo/peakproduceskusbydepartment";
	private static final String ERP_NEW_SKUS = "erpinfo/newskus";
	private static final String ERP_BACK_IN_STOCK_SKUS = "erpinfo/backinstockskus";
	private static final String ERP_OVERRIDDEN_NEW_SKUS = "erpinfo/overriddennewskus";
	private static final String ERP_OVERRIDDEN_BACK_IN_STOCK_SKUS = "erpinfo/overriddenbackinstockskus";
	private static final String ERP_SKU_AVAILABILITY_HISTORY = "erpinfo/skuavailabilityhistory";
	private static final String ERP_REFRESH_NEW_AND_BACK_VIEWS = "erpinfo/refreshnewandbackviews";
//	availabledeliverydates
	
	private static final String ERP_OVERRIDDEN_NEWNESS = "erpinfo/overriddennewness";
	private static final String ERP_OVERRIDDEN_BACK_IN_STOCK = "erpinfo/overriddenbackinstock";
	

	private static final String SEND_GIFTCARD = "giftcard/send/";
	
	private static final String GET_DLV_RESTRICTIONS = "restriction/delivery";
	private static final String GET_ALCOHOL_RESTRICTIONS = "restriction/alcohol";
	private static final String GET_ADDRESS_RESTRICTION = "restriction/address";
	private static final String STORE_DLV_RESTRICTION = "restriction/delivery/store";
	private static final String STORE_ADDRESS_RESTRICTION = "restriction/address/store";
	private static final String DELETE_DLV_RESTRICTION = "restriction/delete";
	private static final String DELETE_ALCOHOL_RETRICTION = "restriction/alcohol/delete";
	private static final String DELETE_ADDRESS_RESTRICTION = "restriction/address/delete";
	private static final String ADD_ADDRESS_RESTRICTION = "restriction/address/add";
	private static final String ALCOHOL_RESTRICTED_FLAG = "restriction/alcohol/flag";
	private static final String MUNICIPALITY_STATE_COUNTIES = "restriction/muncipality/state";
	private static final String STORE_ALCOHOL_RESTRICTION = "restriction/alcohol/store";
	private static final String ADD_ALCOHOL_RESTRICTION = "restriction/alcohol/add";
	private static final String CHECK_ALCOHOL_RESTRICTION = "restriction/alcoholdelivery";
	private static final String CHECK_ADDRESS_RESTRICTION = "restriction/checkaddress";
	private static final String ADD_DLV_RESTRICTION = "restriction/delivery/add";
	private static final String GET_RESTRICTIONS = "restriction/delivery";
	private static final String RESERVATION_UPDATE = "routing/reservationupdate/"; 
	private static final String MODIFYORDERREQUEST = "routing/modifyorderrequest";
	private static final String CANCEL_ORDER_REQUEST = "routing/cancelorderrequest";
	private static final String SUBMIT_ORDER_REQUEST = "routing/submitorderrequest";
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
			System.out.println(response.getBody());
			responseOfTypestring =getMapper().readValue(response.getBody(), type);
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
	
			Response<List<ZoneInfoDataWrapper>> response;
			try {
				
				response = httpGetDataTypeMap(getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE+ppType), new TypeReference<Response<List<ZoneInfoDataWrapper>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				
					Map<ZoneInfo, List<FDProductPromotionInfo>> data = new HashMap();
					for(ZoneInfoDataWrapper wrapper: response.getData()){
						ZoneInfo key = ModelConverter.buildZoneInfo(wrapper.getKey());
						List<FDProductPromotionInfo> value = ModelConverter.buildFDProductPromotionInfo(wrapper.getValue());
						data.put(key,value);
					}
					return data;
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
		}
	@Override
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(
			String ppType, Date lastPublished) throws FDResourceException {
	
			Response<List<ZoneInfoDataWrapper>> response;
			try {
				
				response = httpGetDataTypeMap(getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE+ppType+"/lastPublishDate/"+lastPublished), new TypeReference<Response<List<ZoneInfoDataWrapper>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				
					Map<ZoneInfo, List<FDProductPromotionInfo>> data = new HashMap();
					for(ZoneInfoDataWrapper wrapper: response.getData()){
						ZoneInfo key = ModelConverter.buildZoneInfo(wrapper.getKey());
						List<FDProductPromotionInfo> value = ModelConverter.buildFDProductPromotionInfo(wrapper.getValue());
						data.put(key,value);
					}
					return data;
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
		}
	
	
	@Override
	public Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> getAllPromotionsByType(
			String ppType, Date lastPublished) throws FDResourceException {
	
			Response<Map<String, List<ZoneInfoDataWrapper>>> response;
			try {
				
				response = httpGetDataTypeMap(getFdCommerceEndPoint(PROMOTION_BY_TYPE+ppType+"/lastPublishDate/"+lastPublished), new TypeReference<Response<Map<String, List<ZoneInfoDataWrapper>>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				
				Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> data = new HashMap();
				for(String key:response.getData().keySet() ){
					
					Map<ZoneInfo, List<FDProductPromotionInfo>> zoneData = new HashMap();
					for(ZoneInfoDataWrapper wrapper: response.getData().get(key)){
						ZoneInfo zoneInfo = ModelConverter.buildZoneInfo(wrapper.getKey());
						List<FDProductPromotionInfo> value = ModelConverter.buildFDProductPromotionInfo(wrapper.getValue());
						zoneData.put(zoneInfo,value);
					}
					data.put(key, zoneData);
				}
					return data;
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
		}
	
	@Override
	public List<FDProductPromotionInfo> getProductsByZoneAndType(
			String ppType, String zoneId) throws FDResourceException {
	
			Response<List<FDProductPromotionInfoData>> response;
			try {
				
				response = httpGetDataTypeMap(getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE+ppType+"/zoneId/"+zoneId), new TypeReference<Response<List<FDProductPromotionInfoData>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				
				List<FDProductPromotionInfo> data = new ArrayList();
				data = ModelConverter.buildFDProductPromotionInfo(response.getData());
				return data;
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
		}
	
	@Override
	public ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(
			String ppPreviewId) throws FDResourceException {
		// TODO Auto-generated method stub 
		
		Response<ErpProductPromotionPreviewInfoData> response;
		try {
			
			response = httpGetDataTypeMap(getFdCommerceEndPoint(PROMOTION_PREVIEW+"promPreview/"+ppPreviewId), new TypeReference<Response<ErpProductPromotionPreviewInfoData>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			
			ErpProductPromotionPreviewInfo data ;
			data = ModelConverter.buildErpProductPromotionPreviewInfo(response.getData());
			return data;
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
				String date1=null;
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				if(since!=null){
				date1 = format1.format(since); 
				}
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
				LOGGER.info("give input are zoneServiceType: "+zoneServiceType+" zipCode: "+zipCode+" isPickupOnlyORNotServicebleZip: "+isPickupOnlyORNotServicebleZip+" starting time: "+System.currentTimeMillis());
				response = getData(getFdCommerceEndPoint(LOAD_ZONE_ID_ISPICK)+"?zoneServiceType="+zoneServiceType+"&zipCode="+zipCode+"&isPickup="+isPickupOnlyORNotServicebleZip, Response.class);
				LOGGER.info("Ending time: "+System.currentTimeMillis());
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
	public void saveExtoleRewardsFile(List<FDRafCreditModel> rewards) throws FDResourceException,RemoteException {
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
	public Map<String, DeliveryException> getCartonScanInfo()
			throws FDResourceException {
		Response<Map<String, DeliveryException>> response =	this.httpGetDataTypeMap(getFdCommerceEndPoint(DLV_MANAGER_CARTON_SCAN_INFO), new TypeReference<Response<Map<String,DeliveryException>>>() { });
		return response.getData();
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
				Request<List<ErpGrpPriceModelData>> request = new Request<List<ErpGrpPriceModelData>>();
				List<ErpGrpPriceModelData> listData = getMapper().convertValue(grpPricelist, new TypeReference<List<ErpGrpPriceModelData>>() {});
				request.setData(listData);
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
	
	@Override
	public void saveFutureZoneNotification(String email, String zip,
			String serviceType) throws FDResourceException {
		String inputJson=null;
		Request<FutureZoneNotificationParam> request = new Request<FutureZoneNotificationParam>();
		FutureZoneNotificationParam  notificationParam = new FutureZoneNotificationParam();
		notificationParam.setEmail(email);
		notificationParam.setServiceType(serviceType);
		notificationParam.setZip(zip);
		
		request.setData(notificationParam);
		try {
			inputJson = buildRequest(request);
			Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_FUTURENOTIFICATION), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDPayPalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException {
		String inputJson=null;
		Response<List<SiteAnnouncement>> response = this.getData(getFdCommerceEndPoint(DLV_MANAGER_FDANNOUNCEMENT), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}
	
	@Override
	public void logFailedFdxOrder(String orderId) throws FDResourceException {
		String inputJson=null;
		Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_LOGFAILED_FDXDORDER+"/"+orderId), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
	
	}
	
	@Override
	public List<MunicipalityInfo> getMunicipalityInfos() throws FDResourceException {
		List<MunicipalityInfo> minInfos = new ArrayList<MunicipalityInfo>();
		Response<List<MunicipalityInfoData>> response = httpGetDataTypeMap(getFdCommerceEndPoint(DLV_MANAGER_MUNICIPALITY_INFO), new TypeReference<Response<List<MunicipalityInfoData>>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		for (MunicipalityInfoData muniInfoData : response.getData()) {
			MunicipalityInfo municipalityInfo = new MunicipalityInfo(muniInfoData.getId(), muniInfoData.getState(), muniInfoData.getCounty(),
					muniInfoData.getCity(), muniInfoData.getGlCode(),
					muniInfoData.getTaxRate(), muniInfoData.getBottleDeposit(), muniInfoData.isAlcoholRestricted(), EnumTaxationType.getEnum(muniInfoData.getTaxationType()));
			minInfos.add(municipalityInfo);
		}
		return minInfos;
		
	}
	
	@Override
	public void sendOrderSizeFeed() throws FDResourceException {
		String inputJson=null;
		Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_SEND_ORDER_FEED), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
	
	}
	
	@Override
	public void sendLateOrderFeed() throws FDResourceException {
		String inputJson=null;
		Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_SEND_LATE_ORDER_FEED), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
	}
	
	@Override
	public Set<StateCounty> getCountiesByState(String state) throws FDResourceException {
		String inputJson=null;
		Set<StateCounty> stateCountySet = new HashSet<StateCounty>();
		Response<List<StateCountyData>> response = this.httpGetDataTypeMap((getFdCommerceEndPoint(DLV_MANAGER_COUNTIES_BY_STATE)+"/"+state), new TypeReference<Response<List<StateCountyData>>>() {});
		/*if(!response.getResponseCode().equals(HttpStatus.OK))
			throw new FDResourceException(response.getMessage());*/
		for (StateCountyData stateCountyData : response.getData()) {
			StateCounty stateCounty = new StateCounty(stateCountyData.getState(), stateCountyData.getCounty(),
					stateCountyData.getCity());
			stateCountySet.add(stateCounty);
			
		}
		return stateCountySet;
	}
	
	@Override
	public StateCounty lookupStateCountyByZip(String zip) throws FDResourceException {
		String inputJson=null;
		Response<StateCountyData> response = this.httpGetDataTypeMap((getFdCommerceEndPoint(DLV_MANAGER_COUNTIES_BY_ZIP+"/"+zip)), new TypeReference<Response<StateCountyData>>() {});
		StateCountyData stateCountyData = response.getData();
		StateCounty stateCounty = new StateCounty(stateCountyData.getState(), stateCountyData.getCounty(),
				stateCountyData.getCity());
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return stateCounty;
	}
	
	@Override
	public int unlockInModifyOrders() throws FDResourceException {
		String inputJson=null;
		Response<Integer> response = this.getData(getFdCommerceEndPoint(DLV_MANAGER_RELEASE_ORDER_LOCK), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}
	
	@Override
	public int queryForMissingFdxOrders() throws FDResourceException {
		String inputJson=null;
		Response<Integer> response = this.getData(getFdCommerceEndPoint(DLV_MANAGER_MISSING_ORDER), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}
	
	@Override
	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws FDResourceException {
		Request<ReservationParam> request = new Request<ReservationParam>();
		OrderContextData orderData = getMapper().convertValue(context, OrderContextData.class);
		ContactAddressData contactAddressData = getMapper().convertValue(address, ContactAddressData.class);
				
		TimeslotEventData timeSlotEventData = getMapper().convertValue(event, TimeslotEventData.class);
		
		ReservationParam  reservationParam = new ReservationParam();
		reservationParam.setRsvId(rsvId);
		reservationParam.setCustomerId(customerId);
		reservationParam.setOrderContext(orderData);
		reservationParam.setAddress(contactAddressData);
		reservationParam.setPr1(pr1);
		reservationParam.setEvent(timeSlotEventData);
		request.setData(reservationParam);
		String inputJson;
		try {			
			inputJson = buildRequest(request);
			Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_COMMIT_RESERVATION), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			throw new FDResourceException(e);
		} catch (FDResourceException e) {
			throw new FDResourceException(e);
		}
		
	}
		
	@Override
	public void recommitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1) throws FDResourceException {
		Request<ReservationParam> request = new Request<ReservationParam>();
		OrderContextData orderData = getMapper().convertValue(context, OrderContextData.class);
		ContactAddressData contactAddressData = getMapper().convertValue(address, ContactAddressData.class);
		ReservationParam  reservationParam = new ReservationParam();
		
		reservationParam.setRsvId(rsvId);
		reservationParam.setCustomerId(customerId);
		reservationParam.setOrderContext(orderData);
		reservationParam.setAddress(contactAddressData);
		reservationParam.setPr1(pr1);
		
		request.setData(reservationParam);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(DLV_MANAGER_RECOMMIT_RESERVATION), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			throw new FDResourceException(e);
		} catch (FDResourceException e) {
			throw new FDResourceException(e);
		}
		
	}
	
	@Override
	public void updateInventories(List<ErpInventoryModel> erpInventoryEntryModel) throws FDResourceException {
		try {
			
			Request<List<ErpInventoryModel>> request = new Request<List<ErpInventoryModel>>();
			request.setData(erpInventoryEntryModel);
			String inputJson = buildRequest(request);
			
			Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(ERP_INVENTORY_UPDATE), Response.class);
			
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		
	}
	@Override
	public void updateRestrictedInfos(
			Set<ErpRestrictedAvailabilityModel> restrictedInfos,
			Set<String> deletedMaterials) throws FDResourceException {

		Request<RestrictedInfoParam> request = new Request<RestrictedInfoParam>();
		RestrictedInfoParam restrictedInfoParam = new RestrictedInfoParam();
		try {
			Set<ErpRestrictedAvailabilityData> erpRestrictedAvailabilityData = getMapper().convertValue(restrictedInfos, Set.class);
			restrictedInfoParam.setDeletedMaterials(deletedMaterials);
			restrictedInfoParam.setRestrictedInfos(erpRestrictedAvailabilityData);
		
			request.setData(restrictedInfoParam);
			String inputJson = buildRequest(request);
			Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(ERP_INVENTORY_UPDATE_RESTRICT_INFO), Response.class);
		
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
		throw new FDResourceException(e, "Unable to process the request.");
		}
		
	}

	@Override
	public boolean smsOptIn(String customerId, String mobileNumber,String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_ALERT_OPTIN +"?customerId="+customerId+"&mobileNumber="+mobileNumber+"&eStoreId="+eStoreId)), new TypeReference<Response<Boolean>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOptInNonMarketing(String customerId, String mobileNumber,String eStoreId) throws FDResourceException  {
		Response<Boolean> response = null;
		try {
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_ALERT_OPTIN_NONMARKETING +"?customerId="+customerId+"&mobileNumber="+mobileNumber+"&eStoreId="+eStoreId)), new TypeReference<Response<Boolean>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOptInMarketing(String customerId, String mobileNumber,String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_ALERT_OPTIN_MARKETING +"?customerId="+customerId+"&mobileNumber="+mobileNumber+"&eStoreId="+eStoreId)), new TypeReference<Response<Boolean>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public void expireOptin() throws FDResourceException {
		try {
			String inputJson = null;
			this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_EXPIRE_OPTIN), Response.class);
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	@Override
	public void updateSmsReceived(String mobileNumber, String shortCode,String carrierName, Date receivedDate, String message,
			EnumEStoreId eStoreId) throws  FDResourceException {
		try {
			String inputJson;
			Request<RecievedSmsData> recieveSmsData = ModelConverter.buildSmsDataRequest(
					mobileNumber, shortCode, carrierName, receivedDate,
					message, eStoreId);
			inputJson = buildRequest(recieveSmsData);
			postDataTypeMap(inputJson, getFdCommerceEndPoint(SMS_MESSAGE_UPDATE), new TypeReference<Response<Void>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} 
	}
	
	@Override
	public List<STSmsResponse> sendSmsToGateway(List<SmsAlertETAInfoData> etaInfoList) throws FDResourceException {
		Response<List<STSmsResponse>> response =null;
		try {
			String inputJson;
			Request<List<SmsAlertETAInfoData>> recieveSmsData = new Request<List<SmsAlertETAInfoData>>();
			recieveSmsData.setData(etaInfoList);
			inputJson = buildRequest(recieveSmsData);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SMS_TO_GATEWAY), new TypeReference<List<STSmsResponse>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} 
		return response.getData();
	}
	@Override
	public boolean smsOrderCancel(String customerId, String mobileNumber,String orderId, String eStoreId) throws  FDResourceException {
		Response<Boolean> response = null;
		try {
			Request<SmsOrderData> request = ModelConverter.buildSmsOrderDataRequest(customerId, mobileNumber, orderId, eStoreId);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_ORDER_CANCEL),Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOrderConfirmation(String customerId, String mobileNumber,String orderId, String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
			Request<SmsOrderData> request = ModelConverter.buildSmsOrderDataRequest(customerId, mobileNumber, orderId, eStoreId);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_ORDER_CONFIRM), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOrderModification(String customerId, String mobileNumber,String orderId, String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
			Request<SmsOrderData> request = ModelConverter.buildSmsOrderDataRequest(customerId, mobileNumber, orderId, eStoreId);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_ORDER_MODIFY),Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	
	
	@Override
	public Collection<GroupScalePricing> findGrpInfoMaster(FDGroup[] grpIds)
			throws RemoteException {
		Response<Collection<GroupScalePricingData>> response =null;
			try {
				String inputJson;
				Request<FDGroup[]> request = new Request<FDGroup[]>();
				request.setData(grpIds);
				inputJson = buildRequest(request);
				response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_GROUP_SCALE_IDS_API), new TypeReference<Response<Collection<GroupScalePricingData>>>() {});
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new RemoteException( e.getMessage());
			} catch (FDPayPalServiceException e) {
				LOGGER.error(e.getMessage());
				throw new RemoteException( e.getMessage());
			} 
			
			Collection<GroupScalePricing> finalData = new ArrayList();
			for(GroupScalePricingData obj:response.getData()){
				finalData.add(ModelConverter.buildGroupScalePricing(obj));
			}
			
			return finalData;
			}
	@Override
	public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException {
		Response<Collection<FDGroupData>> response =null;
		try {
		
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_ALL_GROUP_INFO_API), new TypeReference<Response<Collection<FDGroupData>>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		Collection<FDGroup> responseData = new ArrayList();
		for(FDGroupData obj:response.getData()){
			responseData.add(ModelConverter.buildFDGroup(obj));
		}
		return responseData;
		}
	@Override
	public GroupScalePricing findGrpInfoMaster(FDGroup group)
			throws FDGroupNotFoundException, RemoteException {
		Response<GroupScalePricingData> response =null;
		try {
			String inputJson;
			Request<FDGroup> request = new Request<FDGroup>();
			request.setData(group);
			inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_GROUP_SCALE_ID_API), new TypeReference<Response<GroupScalePricingData>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		return ModelConverter.buildGroupScalePricing(response.getData());
		}
	
	@Override
	public Map<String, FDGroup> getGroupIdentityForMaterial(String matId)
			throws RemoteException {
		Response<Map<String, FDGroupData>> response =null;
		try {
		
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_GROUP_INFO_BY_MATID_API+"/"+matId), new TypeReference<Response<Map<String, FDGroupData>>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		Map<String, FDGroup> responseData = new HashMap();
		for(String obj:response.getData().keySet()){
			responseData.put(obj, ModelConverter.buildFDGroup(response.getData().get(obj)));
		}
		return responseData;
		}
	
	@Override
	public Map<SalesAreaInfo, FDGroup> getGroupIdentitiesForMaterial(
			String matId) throws RemoteException {
		Response<List<SalesAreaInfoFDGroupWrapper>> response =null;
		try {
		
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_GROUP_IDENTITY_BY_MATID_API+"/"+matId), new TypeReference<Response<List<SalesAreaInfoFDGroupWrapper>>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		Map<SalesAreaInfo, FDGroup> responseData = new HashMap();
		for(SalesAreaInfoFDGroupWrapper obj:response.getData()){
			responseData.put(ModelConverter.buildSalesAreaInfo(obj.getSalesAreaInfoData()), ModelConverter.buildFDGroup(obj.getfDGroupData()));
		}
		return responseData;
		}
	@Override
	public Collection getFilteredSkus(List skuList) throws RemoteException {
		Response<List<FDSkuData>> response =null;
		try {
			String inputJson;
			Request<List<FDSkuData>> request = new Request<List<FDSkuData>>();
			List<FDSkuData> skuData = new ArrayList();
			for(Object obj :skuList){FDSku sku=(FDSku)obj;skuData.add(ModelConverter.buildFDSkyData(sku));}
			request.setData(skuData);
			inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_GROUP_FILTERED_SKU_API), new TypeReference<Response<List<FDSkuData>>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		
		List<FDSku> responseData = new ArrayList();
		for(FDSkuData skuData :response.getData()){
			responseData.add(ModelConverter.buildFDSky(skuData));
		}
		return responseData;
		}
	
	
	@Override
	public int getLatestVersionNumber(String grpId) throws RemoteException {
		Response<String> response =null;

			try {
				response = this.httpGetData(getFdCommerceEndPoint(ERP_LATEST_VER_FOR_GRPID_API+"/"+grpId), Response.class);
			} catch (FDPayPalServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RemoteException( e.getMessage());
			}

		return Integer.valueOf(response.getData());
		}
	@Override
	public Collection<FDGroup> findGrpsForMaterial(String matId)
			throws RemoteException {
		Response<Collection<FDGroupData>> response =null;
		try {
		
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_GRP_FOR_MAT_ID_API+"/"+matId), new TypeReference<Response<Collection<FDGroupData>>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		Collection<FDGroup> responseData = new ArrayList();
		for(FDGroupData obj:response.getData()){
			responseData.add(ModelConverter.buildFDGroup(obj));
		}
		return responseData;
		}
	
	@Override
	public FDGroup getLatestActiveGroup(String groupID)
			throws FDGroupNotFoundException, RemoteException {
		Response<FDGroupData> response =null;
		try {
		
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_LATEST_GRP_API+"/"+groupID), new TypeReference<Response<FDGroupData>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		return ModelConverter.buildFDGroup(response.getData());
		}
	
	@Override
	public Map<String, List<String>> getModifiedOnlyGroups(Date lastModified)
			throws RemoteException {
		String date1 =null;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		if(lastModified!=null){
		date1 = format1.format(lastModified); 
		}
		Response<Map<String, List<String>>> response =null;
		try {
		
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_LAST_MOD_GRP_API+"/"+date1), new TypeReference<Response<Map<String, List<String>>>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( e.getMessage());
		} 
		return response.getData();
		
	}

	
	@Override
	public void log(Class<? extends FDRecommendationEvent> eventClazz, Collection<RecommendationEventsAggregate> events) throws RemoteException{
		try {
			Request<Collection<RecommendationEventsAggregate>> request = new Request<Collection<RecommendationEventsAggregate>>();
			request.setData(events);
			String inputJson = buildRequest(request);
			Response<String> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(LOG_BATCHEVENT_RECOMMENDATION) + "?className=" + eventClazz.getName(), new TypeReference<Response<String>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void log(FDRecommendationEvent event, int frequency) throws RemoteException{
		try {
			Request<FDRecommendationEventData> request = new Request<FDRecommendationEventData>();
			request.setData(ModelConverter.convertFDRecommendationEvent(event));
			String inputJson = buildRequest(request);
			Response<String> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(LOG_EVENT_RECOMMENDATION) + "?frequency=" + frequency, new TypeReference<Response<String>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public Map<String, double[]> getPersonalizedFactors(String eStoreId,
			String erpCustomerId, List<String> factors) {
		Response<ScoreResult> response = null;
		ScoreResult result;
			try {
				Request<ProductFactorParam> request = new Request<ProductFactorParam>();
				ProductFactorParam productFactorParam = new ProductFactorParam();
				productFactorParam.seteStoreId(eStoreId);
				productFactorParam.setErpCustomerId(erpCustomerId);
				productFactorParam.setFactors(factors);
				request.setData(productFactorParam);
				String inputJson = buildRequest(request);
				response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SMARTSTORE_PERSONALIZED_FACTORS),new TypeReference<Response<ScoreResult>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} catch (FDPayPalServiceException e) {
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} 
			return result.getResult();
		}
	
	@Override
	public Map<String, double[]> getGlobalFactors(String eStoreId, List<String> factors) {
		Response<ScoreResult> response = null;
		ScoreResult result;
			try {
				Request<ProductFactorParam> request = new Request<ProductFactorParam>();
				ProductFactorParam productFactorParam = new ProductFactorParam();
				productFactorParam.seteStoreId(eStoreId);
				productFactorParam.setFactors(factors);
				request.setData(productFactorParam);
				String inputJson = buildRequest(request);
				response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SMARTSTORE_GLOBAL_FACTORS),new TypeReference<Response<ScoreResult>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} catch (FDPayPalServiceException e) {
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} 
			return result.getResult();
	}
	
	@Override
	public Set<String> getPersonalizedFactorNames() {
		Response<Set<String>> response = null;
		Set<String> result;
			try {
				response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SMARTSTORE_PERSONALIZED_FACTORS_NAME), new TypeReference<Response<Set<String>>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result;
	}
	
	@Override
	public Set<String> getGlobalFactorNames() {
		Response<Set<String> > response = null;
		Set<String> result;
			try {
				Request<String> request = new Request<String>();
				response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SMARTSTORE_GLOBAL_FACTORS_NAME), new TypeReference<Response<Set<String>>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result;
	}

	@Override
	public Set<String> getGlobalProducts(String eStoreId) {
		Response<Set<String> > response = null;
		Set<String>  result;
			try {
				response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SMARTSTORE_GLOBAL_PRODUCTS)+"/"+eStoreId, new TypeReference<Response<Set<String> >>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result;
	}

	@Override
	public Set<String> getPersonalizedProducts(String eStoreId,String erpCustomerId) {
		Response<Set<String> > response = null;
		Set<String>  result;
			try {
				Request<String> request = new Request<String>();
				response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SMARTSTORE_PERSONALIZED_PRODUCTS)+"/"+eStoreId+"/"+erpCustomerId, new TypeReference<Response<Set<String>>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result;
	}
	@Override
	public List<String> getProductRecommendations(String recommender,String contentKey) {
		Response<List<String>> response = null;
		List<String>  result;
			try {
				response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SMARTSTORE_PRODUCT_RECOMMENDATIONS)+"/"+recommender+"/"+contentKey, new TypeReference<Response<List<String>>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result;
	}
	@Override
	public List<String> getPersonalRecommendations(String recommender,String erpCustomerId) {
		Response<List<String>> response = null;
		List<String> result;
			try {
				Request<String> request = new Request<String>();
				response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SMARTSTORE_PERSONAL_RECOMMENDATIONS)+"/"+recommender+"/"+erpCustomerId, new TypeReference<Response<List<String>>>() {});
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result;
	}

	@Override
	public String getPreferredWinePrice(String erpCustomerId) {
		Response<String> response = null;
			try {
				Request<String> request = new Request<String>();
				response = this.getData(getFdCommerceEndPoint(SMARTSTORE_PREFERRED_WINEPRICE)+"/"+erpCustomerId, Response.class);
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
				
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return response.getData();
	}
		
	@Override
	public void logGatewayActivity(FDGatewayActivityLogModel logModel)throws RemoteException{
		try {
			Request<FDGatewayActivityLogModelData> gatewayActivityLogReq = new Request<FDGatewayActivityLogModelData>();
			gatewayActivityLogReq.setData(ModelConverter.convertFDGatewayActivityLogModel(logModel));
			String inputJson = buildRequest(gatewayActivityLogReq);
			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(LOG_GATEWAY_ACTIVITY), Response.class);
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
	public Map<String, Rule> getRules(String subsystem)throws FDResourceException, RemoteException {
		Map<String, Rule>  rules = null;
		try {
		Response<Map<String, RuleData>> response = this.httpGetDataTypeMap((getFdCommerceEndPoint(GET_RULES +"?subsystem="+ subsystem)), new TypeReference<Response<Map<String, RuleData>>>() {});
		rules = ModelConverter.buildRuleMap(response.getData());
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return rules;
	}
	@Override
	public Rule getRule(String ruleId) throws FDResourceException,RemoteException {

		Response<Rule> response = null;
		try {
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(GET_RULE +"?ruleId="+ruleId)), new TypeReference<Response<Rule>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public void deleteRule(String ruleId) throws FDResourceException,RemoteException {
		try {
		this.getData(getFdCommerceEndPoint(DELETE_RULE+"?ruleId="+ruleId), Response.class);
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	@Override
	public void storeRule(Rule rule) throws FDResourceException,RemoteException {
		String inputJson;
		try {
			Request<RuleData> ruleData = new Request<RuleData>();
			ruleData.setData(ModelConverter.buildRuleData(rule));
			inputJson = buildRequest(ruleData);
			this.postData(inputJson, getFdCommerceEndPoint(STORE_RULES), Response.class);
			
		} catch (FDPayPalServiceException e) {
			throw new FDResourceException(e, "Unable to process the request.");
		}
		catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	
	@Override
	public Collection<ErpActivityRecord> findActivityByTemplate(
			ErpActivityRecord template) throws FDResourceException,
			RemoteException {
			Collection<ErpActivityRecord> erpActivityRecordResponse = null;
			try {
				Request<ErpActivityRecordData> request = new Request<ErpActivityRecordData>();
				ErpActivityRecordData erpActivityRecordData = ModelConverter.buildErpActivityRecordData(template);
				request.setData(erpActivityRecordData);
				String inputJson = buildRequest(request);
				Response<Collection<ErpActivityRecordData>> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ACTIVITY_TEMPLATE),new TypeReference<Response<Collection<ErpActivityRecordData>>>() {});
				erpActivityRecordResponse = ModelConverter.buildErpActivityRecord(response.getData());
				if(!response.getResponseCode().equals("OK")){
					throw new FDResourceException(response.getMessage());
				}
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} catch (FDPayPalServiceException e) {
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} 
			return erpActivityRecordResponse;
	}
	@Override
	public void logActivity(ErpActivityRecord rec) throws RemoteException {
		try {
			Request<ErpActivityRecordData> request = new Request<ErpActivityRecordData>();
			ErpActivityRecordData erpActivityRecordData = ModelConverter.buildErpActivityRecordData(rec);
			request.setData(erpActivityRecordData);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOG_ACTIVITY),new TypeReference<Response<Void>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} 
}
	@Override
	public void logActivityNewTX(ErpActivityRecord rec)throws  RemoteException {
		try {
			Request<ErpActivityRecordData> request = new Request<ErpActivityRecordData>();
			ErpActivityRecordData erpActivityRecordData = ModelConverter.buildErpActivityRecordData(rec);
			request.setData(erpActivityRecordData);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOG_ACTIVITY_NEWTXN),new TypeReference<Response<Void>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} 
}
	@Override
	public Map<String, List> getFilterLists(ErpActivityRecord template)throws FDResourceException, RemoteException {
		Map<String, List> filterListResponse = null;
		try {
			Request<ErpActivityRecordData> request = new Request<ErpActivityRecordData>();
			ErpActivityRecordData erpActivityRecordData = ModelConverter.buildErpActivityRecordData(template);
			request.setData(erpActivityRecordData);
			String inputJson = buildRequest(request);
			Response<Map<String, List<String>>> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(FILTER_LISTS),new TypeReference<Response<Map<String, List<String>>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			filterListResponse = ModelConverter.buildFilterListResponse(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} 
		return filterListResponse;

	}
	@Override
	public Collection<ErpActivityRecord> getCCActivitiesByTemplate(
			ErpActivityRecord template) throws FDResourceException,
			RemoteException {
		Collection<ErpActivityRecord> ccActivityResponse = null;
		try {
			Request<ErpActivityRecordData> request = new Request<ErpActivityRecordData>();
			ErpActivityRecordData erpActivityRecordData = ModelConverter.buildErpActivityRecordData(template);
			request.setData(erpActivityRecordData);
			String inputJson = buildRequest(request);
			Response<Collection<ErpActivityRecordData>> response  = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CC_ACTIVITIES),new TypeReference<Response<Collection<ErpActivityRecordData>>>() {});
			ccActivityResponse = ModelConverter.buildErpActivityRecord(response.getData());
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} 
		return ccActivityResponse;
	}
	
	@Override
	public Collection findMaterialsByBatch(int batchNum) throws RemoteException {
		Response<Collection> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_VERSION)+"/"+batchNum, new TypeReference<Response<Collection>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public Collection findMaterialsBySapId(String sapId) throws RemoteException {
		Response<Collection> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_SAPID)+"/"+sapId, new TypeReference<Response<Collection>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public Collection findMaterialsBySku(String skuCode) throws RemoteException {
		Response<Collection> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_SKUCODE)+"/"+skuCode, new TypeReference<Response<Collection>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public Collection findMaterialsByDescription(String description) throws RemoteException {
		Response<Collection> response = null;
		try {
			
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_DESCRIPTION)+"/"+URLEncoder.encode(description), new TypeReference<Response<Collection>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public Collection findMaterialsByCharacteristic(String characteristic) throws RemoteException {
		Response<Collection> response = null;
		try {
			characteristic = characteristic.replace('_', '-');
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_CHARACTERISTIC)+"/"+URLEncoder.encode(characteristic), new TypeReference<Response<Collection>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	

	@Override
	public Collection findMaterialsByClass(String className) throws RemoteException {
		Response<Collection> response = null;
		Collection erpMaterialInfoModels;
		try {
			className = className.replace('_', '-');
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALSINFO_CLASS)+"/"+className,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public ErpProductInfoModel findProductBySku(String skuCode) throws RemoteException, ObjectNotFoundException {
		Response<ErpProductInfoModelData> response = null;
		ErpProductInfoModel erpProductInfoModel;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_SKUCODE)+"/"+skuCode,  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(response.getData() == null){
				throw new ObjectNotFoundException(response.getMessage());
			}
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModel = ErpProductInfoModelConvert.convertModelToData(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModel;
	}
	
	@Override
	public Collection<ErpProductInfoModel> findProductsBySapId(String sapId) throws RemoteException {
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_SAPID)+"/"+sapId,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	@Override
	public Collection findProductsByDescription(String description) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_DESCRIPTION)+"/"+URLEncoder.encode(description),  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public Collection findProductsLikeSku(String sku) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_LIKESKU)+"/"+sku,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public Collection<ErpProductInfoModel> findProductsByUPC(String upc) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_UPC)+"/"+upc,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	
	@Override
	public Collection<String> findProductsByCustomerUPC(String erpCustomerPK, String upc) throws RemoteException {
		
		Response<Collection<String>> response = null;
		Collection<String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_CUSTOMER_UPC)+"/"+erpCustomerPK+"/"+upc,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Collection<ErpProductInfoModel> findProductsLikeUPC(String upc) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_LIKEUPC)+"/"+upc,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public ErpInventoryModel getInventoryInfo(String materialNo) throws RemoteException {
		
		Response<ErpInventoryData> response = null;
		ErpInventoryModel erpInventoryModel = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_INVENTORY_INFO)+"/"+materialNo,  new TypeReference<Response<ErpInventoryData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpInventoryModel = ErpInventoryModelConvert.convertDataToModel(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpInventoryModel;
	}
	
	@Override
	public Map<String,ErpInventoryModel> loadInventoryInfo(Date date) throws RemoteException {
		
		Response<Map<String,ErpInventoryData>> response = null;
		Map<String,ErpInventoryModel> erpInventoryModelMap = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_LOAD_INVENTORY_INFO)+"/"+date,  new TypeReference<Response<Map<String,ErpInventoryData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpInventoryModelMap = ErpInventoryModelConvert.convertDataMapToModelMap(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpInventoryModelMap;
	}
	
	@Override
	public Collection<String> findSkusBySapId(String sapId) throws RemoteException {
		
		Response<Collection<String>> response = null;
		Collection<String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKUS_SAPID)+"/"+sapId,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Collection<String> findNewSkuCodes(int days) throws RemoteException {
		
		Response<Collection<String>> response = null;
		Collection<String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_NEW_SKUS_DAYS)+"/"+days,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Map<String, Integer> getSkusOldness() throws RemoteException {
		
		Response<Map<String, Integer>> response = null;
		Collection<Map<String, Integer>> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKUS_OLDNESS),  new TypeReference<Response<Map<String, Integer>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public ErpProductInfoModel findProductBySkuAndVersion(String skuCode, int version) throws RemoteException {
		Response<ErpProductInfoModelData> response = null;
		ErpProductInfoModel erpProductInfoModel;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_SKUCODE_VERSION)+"/"+skuCode+"/"+version,  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModel = ErpProductInfoModelConvert.convertModelToData(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModel;
	}
	@Override
	public Collection<String> findReintroducedSkuCodes(int days) throws RemoteException {
		Response<Collection<String>> response = null;
		Collection<String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_REINTRODUCED_SKUCODES)+"/"+days,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Collection<String> findOutOfStockSkuCodes() throws RemoteException {
		Response<Collection<String>> response = null;
		Collection<String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OUTOFSTOCK_SKUCODES),  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	@Override
	public Collection<ErpProductInfoModel> findProductsBySku(String[] skuCodes) throws RemoteException {
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		Request<String[]> request = new Request<String[]>();
		try {
			request.setData(skuCodes);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_PRODUCT_SKUS),  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public void setOverriddenNewness(String sku,
			Map<String, String> salesAreaOverrides) throws RemoteException {
		Response<Void> response = null;
		Request<OverrideSkuAttrParam> request = new Request<OverrideSkuAttrParam>();
		try {
			OverrideSkuAttrParam overrideskuattrparam = new OverrideSkuAttrParam();
			overrideskuattrparam.setSalesAreaOverrides(salesAreaOverrides);
			overrideskuattrparam.setSku(sku);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(ERP_OVERRIDDEN_NEWNESS),  Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	@Override
	public void setOverriddenBackInStock(String sku,
			Map<String, String> salesAreaOverrides) throws RemoteException {
		Response<Void> response = null;
		Request<OverrideSkuAttrParam> request = new Request<OverrideSkuAttrParam>();
		try {
			OverrideSkuAttrParam overrideskuattrparam = new OverrideSkuAttrParam();
			overrideskuattrparam.setSku(sku);
			overrideskuattrparam.setSalesAreaOverrides(salesAreaOverrides);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(ERP_OVERRIDDEN_BACK_IN_STOCK),  Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, String> getOverriddenNewness(String sku) throws RemoteException {
		Response<Map<String, String>> response = null;
		Map<String, String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_NEWNESS)+"/"+sku,  new TypeReference<Response<Map<String, String>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Map<String, String> getOverriddenBackInStock(String sku) throws RemoteException {
		Response<Map<String, String>> response = null;
		Map<String, String> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_BACK_IN_STOCK)+"/"+sku,  new TypeReference<Response<Map<String, String>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Map<String, Map<String,Date>> getNewSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		Map<String, Map<String,Date>> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_NEW_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Map<String, Map<String,Date>> getBackInStockSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		Map<String, Map<String,Date>> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_BACK_IN_STOCK_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	@Override
	public Map<String, Map<String,Date>> getOverriddenNewSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		Map<String, Map<String,Date>> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_NEW_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		Map<String, Map<String,Date>> productIds;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_BACK_IN_STOCK_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	@Override
	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws RemoteException {
		Response<List<SkuAvailabilityHistory>> response = null;
		List<SkuAvailabilityHistory> skuAvailabilityHistories;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKU_AVAILABILITY_HISTORY)+"/"+skuCode,  new TypeReference<Response<List<SkuAvailabilityHistory>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public void refreshNewAndBackViews() throws RemoteException {
		Response<Void> response = null;
		List<SkuAvailabilityHistory> skuAvailabilityHistories;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_REFRESH_NEW_AND_BACK_VIEWS),  new TypeReference<Response<Void>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public Collection<String> findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) throws RemoteException {
		Response<Collection> response = null;
		Request<SkuPrefixParam> request = new Request<SkuPrefixParam>();
		try {
			SkuPrefixParam overrideskuattrparam = new SkuPrefixParam();
			overrideskuattrparam.setLowerLimit(lowerLimit);
			overrideskuattrparam.setUpperLimit(upperLimit);
			overrideskuattrparam.setSkuPrefixes(skuPrefixes);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_SKUS_BY_DEAL),  new TypeReference<Response<Collection<String>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Collection<String> findPeakProduceSKUsByDepartment(List skuPrefixes) throws RemoteException {
		Response<Collection> response = null;
		Request<SkuPrefixParam> request = new Request<SkuPrefixParam>();
		try {
			SkuPrefixParam overrideskuattrparam = new SkuPrefixParam();
			overrideskuattrparam.setSkuPrefixes(skuPrefixes);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_PEAK_PRODUCE_SKUS_BY_DEPARTMENT),  new TypeReference<Response<Collection<String>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}


	@Override
	public void sendRegisterGiftCard(String saleId, double saleAmount) throws RemoteException {

		Response<String> response = null;
		try {
			response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SEND_GIFTCARD + saleId + "/" + saleAmount)), new TypeReference<Response<Object>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		}
	}
	@Override
	public List getDlvRestrictions(String dlvReason, String dlvType,String dlvCriterion) throws FDResourceException, RemoteException {
		Response<List<RestrictionData>> response = null;
		List dlvrestictions = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_DLV_RESTRICTIONS+"/dlvReason/"+dlvReason+"/dlvType/"+dlvType+"/dlvCriterion/"+dlvCriterion),  new TypeReference<Response<List<RestrictionData>>>(){});
			dlvrestictions = DlvRestrictionModelConverter.buildDlvRestrictionListResponse(response.getData());
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return dlvrestictions;
	}
	@Override
	public Object  getDlvRestriction(String restrictionId)throws FDResourceException, RemoteException {
		Response<RestrictionData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_DLV_RESTRICTIONS+"/restrictionId/"+restrictionId),  new TypeReference<Response<RestrictionData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return DlvRestrictionModelConverter.buildDlvRestrictionResponse(response.getData());
	}
	@Override
	public AlcoholRestrictionData getAlcoholRestriction(String restrictionId,String municipalityId) throws FDResourceException, RemoteException {
		Response<AlcoholRestrictionData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ALCOHOL_RESTRICTIONS+"?restrictionId="+restrictionId+"&municipalityId="+municipalityId),  new TypeReference<Response<AlcoholRestrictionData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public RestrictedAddressModelData getAddressRestriction(String address1,String apartment, String zipCode) throws FDResourceException,
			RemoteException {
		Response<RestrictedAddressModelData> response = null;
		Request<AddressRestrictionData> request = new Request<AddressRestrictionData>();
		try {
			request.setData(DlvRestrictionModelConverter.buildAddressRestrictionData( address1, apartment,  zipCode));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_ADDRESS_RESTRICTION),  new TypeReference<Response<RestrictedAddressModelData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public void storeDlvRestriction(RestrictionData restriction)throws FDResourceException, RemoteException {
		Request<RestrictionData> request = new Request<RestrictionData>();
		try {
			request.setData(restriction);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_DLV_RESTRICTION),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}	
	@Override
	public void storeAddressRestriction(AddressAndRestrictedAdressData restriction)
			throws FDResourceException, RemoteException {
		Request<AddressAndRestrictedAdressData> request = new Request<AddressAndRestrictedAdressData>();
			try {
				request.setData(restriction);
				String inputJson = buildRequest(request);
				this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_ADDRESS_RESTRICTION),  new TypeReference<Response<Object>>() {});
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} catch (FDPayPalServiceException e) {
				LOGGER.error(e.getMessage());
				throw new RemoteException(e.getMessage());
			}}
	@Override
	public void deleteDlvRestriction(String restrictionId)throws FDResourceException, RemoteException {
		try {
			this.httpGetDataTypeMap(getFdCommerceEndPoint(DELETE_DLV_RESTRICTION+"?restrictionId="+restrictionId),  new TypeReference<Response<Object>>(){});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public void deleteAlcoholRestriction(String restrictionId)throws FDResourceException, RemoteException {
		try {
			this.httpGetDataTypeMap(getFdCommerceEndPoint(DELETE_ALCOHOL_RETRICTION+"?restrictionId="+restrictionId),  new TypeReference<Response<Object>>(){});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public void deleteAddressRestriction(String address1, String apartment,String zipCode) throws FDResourceException, RemoteException {
		Request<AddressRestrictionData> request = new Request<AddressRestrictionData>();
		try {
			request.setData(DlvRestrictionModelConverter.buildAddressRestrictionData(address1, apartment, zipCode));
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(DELETE_ADDRESS_RESTRICTION),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void addAddressRestriction(RestrictedAddressModelData restriction)throws FDResourceException, RemoteException {
		Request<RestrictedAddressModelData> request = new Request<RestrictedAddressModelData>();
		try {
			request.setData(restriction);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_ADDRESS_RESTRICTION),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public void setAlcoholRestrictedFlag(String municipalityId,boolean restricted) throws FDResourceException, RemoteException {
		try {
			this.postDataTypeMap(null, getFdCommerceEndPoint(ALCOHOL_RESTRICTED_FLAG+"?municipalityId="+municipalityId+"&restricted="+restricted),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
		
	}
	@Override
	public Map<String, List<String>> getMunicipalityStateCounties() throws FDResourceException, RemoteException {
		Response<Map<String, List<String>>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(MUNICIPALITY_STATE_COUNTIES),  new TypeReference<Response<Map<String, List<String>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public void storeAlcoholRestriction(AlcoholRestrictionData restriction)throws FDResourceException, RemoteException {
		Request<AlcoholRestrictionData> request = new Request<AlcoholRestrictionData>();
		try {
			request.setData(restriction);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_ALCOHOL_RESTRICTION),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public String addAlcoholRestriction(AlcoholRestrictionData restriction)throws FDResourceException, RemoteException {
		Request<AlcoholRestrictionData> request = new Request<AlcoholRestrictionData>();
		Response<String> response = new Response<String>();
		try {
			request.setData(restriction);
			String inputJson = buildRequest(request);
			response =this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_ALCOHOL_RESTRICTION),  new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
		
	}
	@Override
	public boolean checkForAlcoholDelivery(String scrubbedAddress,String zipcode, String apartment) throws RemoteException {
		Request<AddressRestrictionData> request = new Request<AddressRestrictionData>();
		Response<Boolean> response = new Response<Boolean>();
		try {
			request.setData(DlvRestrictionModelConverter.buildAddressRestrictionData(scrubbedAddress, apartment, zipcode));
			String inputJson = buildRequest(request);
			response =this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CHECK_ALCOHOL_RESTRICTION),  new TypeReference<Response<Boolean>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
		
	}
	@Override
	public String checkAddressForRestrictions(AddressData address) throws RemoteException {
		Request<AddressData> request = new Request<AddressData>();
		Response<String> response = new Response<String>();
		try {
			request.setData(address);
			String inputJson = buildRequest(request);
			response =this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CHECK_ADDRESS_RESTRICTION),  new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
		
	}
	@Override
	public void addDlvRestriction(RestrictionData restriction)throws FDResourceException, RemoteException {
		Request<RestrictionData> request = new Request<RestrictionData>();
		try {
			request.setData(restriction);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_DLV_RESTRICTION),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public List<RestrictionData> getDlvRestrictions()throws FDResourceException, RemoteException {
		Response<List<RestrictionData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_RESTRICTIONS),  new TypeReference<Response<List<RestrictionData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public void sendCancelOrderRequest(String saleId) throws RemoteException {
		String inputJson;
		Response<String> response = null;
		try {
			this.postDataTypeMap(null, getFdCommerceEndPoint(CANCEL_ORDER_REQUEST+"?saleid="+saleId),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId) throws RemoteException {
		
		SubmitOrderRequestData submitOrderRequestData = new SubmitOrderRequestData();
		submitOrderRequestData = buildOrderRequestData(saleId, parentOrderId, tip, reservationId, firstName, lastName, deliveryInstructions, serviceType, 
				 unattendedInstr, orderMobileNumber,erpOrderId);
		Request<SubmitOrderRequestData> request = new Request<SubmitOrderRequestData>();
		try {
			request.setData(submitOrderRequestData);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(MODIFYORDERREQUEST),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) throws RemoteException {
		try {
			String inputJson = buildRequest(address);
			this.postDataTypeMap(null, getFdCommerceEndPoint(RESERVATION_UPDATE+"?reservationid="+reservationId+"&sapordernumber="+sapOrderNumber),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	@Override
	public void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId) throws RemoteException {
		SubmitOrderRequestData submitOrderRequestData = new SubmitOrderRequestData();
		submitOrderRequestData = buildOrderRequestData(saleId, parentOrderId, tip, reservationId, firstName, lastName, deliveryInstructions, serviceType, 
				 unattendedInstr, orderMobileNumber,erpOrderId);
		Request<SubmitOrderRequestData> request = new Request<SubmitOrderRequestData>();
		try {
			request.setData(submitOrderRequestData);
			String inputJson = buildRequest(request);
			this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SUBMIT_ORDER_REQUEST),  new TypeReference<Response<Object>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}
	
	private SubmitOrderRequestData buildOrderRequestData(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId){
		SubmitOrderRequestData submitOrderRequestData = new SubmitOrderRequestData();
		submitOrderRequestData.setSaleId(saleId);
		submitOrderRequestData.setParentOrderId(parentOrderId);
		submitOrderRequestData.setTip(tip);
		submitOrderRequestData.setReservationId(reservationId);
		submitOrderRequestData.setFirstName(firstName);
		submitOrderRequestData.setLastName(lastName);
		submitOrderRequestData.setDeliveryInstructions(deliveryInstructions);
		submitOrderRequestData.setServiceType(serviceType);
		submitOrderRequestData.setUnattendedInstr(unattendedInstr);
		submitOrderRequestData.setOrderMobileNumber(orderMobileNumber);
		submitOrderRequestData.setErpOrderId(erpOrderId);
		return submitOrderRequestData;
	}
	
}