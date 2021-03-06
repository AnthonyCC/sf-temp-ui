package com.freshdirect.payment.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Category;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.ErpActivityRecordData;
import com.freshdirect.ecommerce.data.customer.ErpGrpPriceModelData;
import com.freshdirect.ecommerce.data.customer.accounts.external.UserTokenData;
import com.freshdirect.ecommerce.data.delivery.AddressAndRestrictedAdressData;
import com.freshdirect.ecommerce.data.delivery.AddressRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
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
import com.freshdirect.ecommerce.data.ecoupon.CouponWalletRequestData;
import com.freshdirect.ecommerce.data.ecoupon.FDCouponActivityContextData;
import com.freshdirect.ecommerce.data.ecoupon.FDCouponActivityLogData;
import com.freshdirect.ecommerce.data.ecoupon.FDCouponCustomerData;
import com.freshdirect.ecommerce.data.enums.BillingCountryInfoData;
import com.freshdirect.ecommerce.data.enums.CrmCasePriorityData;
import com.freshdirect.ecommerce.data.enums.CrmCaseSubjectData;
import com.freshdirect.ecommerce.data.enums.CrmEnumTypeData;
import com.freshdirect.ecommerce.data.enums.DeliveryPassTypeData;
import com.freshdirect.ecommerce.data.enums.EnumComplaintDlvIssueTypeData;
import com.freshdirect.ecommerce.data.enums.EnumFeaturedHeaderTypeData;
import com.freshdirect.ecommerce.data.erp.ewallet.ErpCustEWalletData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpRestrictedAvailabilityData;
import com.freshdirect.ecommerce.data.erp.inventory.RestrictedInfoParam;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicValuePriceData;
import com.freshdirect.ecommerce.data.erp.material.ErpClassData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialSalesAreaData;
import com.freshdirect.ecommerce.data.erp.material.ErpPlantMaterialData;
import com.freshdirect.ecommerce.data.erp.material.ErpSalesUnitData;
import com.freshdirect.ecommerce.data.erp.material.SkuPrefixParam;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialPriceData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductPromotionPreviewInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.FDProductPromotionInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoDataWrapper;
import com.freshdirect.ecommerce.data.fdstore.FDGroupData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.fdstore.GroupScalePricingData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoFDGroupWrapper;
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.ecommerce.data.mail.EmailData;
import com.freshdirect.ecommerce.data.messages.SystemMessages;
import com.freshdirect.ecommerce.data.payment.BINData;
import com.freshdirect.ecommerce.data.payment.ErpPaymentMethodData;
import com.freshdirect.ecommerce.data.payment.FDGatewayActivityLogModelData;
import com.freshdirect.ecommerce.data.payment.RestrictedPaymentMethodCriteriaData;
import com.freshdirect.ecommerce.data.payment.RestrictedPaymentMethodData;
import com.freshdirect.ecommerce.data.routing.SubmitOrderRequestData;
import com.freshdirect.ecommerce.data.rules.RuleData;
import com.freshdirect.ecommerce.data.sap.CharacteristicValueMapData;
import com.freshdirect.ecommerce.data.sap.LoadDataInputData;
import com.freshdirect.ecommerce.data.sap.LoadMatPlantAndSalesInputData;
import com.freshdirect.ecommerce.data.sap.LoadPriceInputData;
import com.freshdirect.ecommerce.data.sap.LoadSalesUnitsInputData;
import com.freshdirect.ecommerce.data.sap.MaterialBatchInfoData;
import com.freshdirect.ecommerce.data.security.TicketData;
import com.freshdirect.ecommerce.data.sessionimpressionlog.SessionImpressionLogEntryData;
import com.freshdirect.ecommerce.data.smartstore.EnumSiteFeatureData;
import com.freshdirect.ecommerce.data.smartstore.ProductFactorParam;
import com.freshdirect.ecommerce.data.smartstore.ScoreResult;
import com.freshdirect.ecommerce.data.zoneInfo.ErpMasterInfoData;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialBatchHistoryModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.ReservationException;
import com.freshdirect.logistics.delivery.model.ReservationUnavailableException;
import com.freshdirect.logistics.delivery.model.SiteAnnouncement;
import com.freshdirect.logistics.delivery.model.SystemMessageList;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.logistics.fdstore.ZipCodeAttributes;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;
import com.freshdirect.rules.Rule;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.security.ticket.Ticket;
import com.freshdirect.sms.model.st.STSmsResponse;


public class FDECommerceService extends AbstractEcommService implements IECommerceService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDECommerceService.class);


	private static final String SAP_PRODUCT_FAMILY_LOADER_GET_SKUCODE_BYPRODFLY_API ="dataloader/sap/skucodesbyproductfamily";
	private static final String SAP_PRODUCT_FAMILY_LOADER_LOAD_API ="dataloader/sap/productfamily";
	private static final String PRODUCT_BY_PROMO_TYPE = "productPromo/type/";
	private static final String PROMOTION_BY_TYPE = "productPromo/promtype/";
	private static final String PROMOTION_PREVIEW = "productPromo/";
	private static final String GET_ACTIVE_BINS = "bin/activebins";
	private static final String SAVE_ACTIVE_BINS = "bin/storebins";
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

	private static final String UPLOAD_PRODUCT_FEED = "productfeed/upload";

	private static final String USERID_BY_USERTOKEN = "account/external/userIdbyusertoken";
	private static final String USER_EMAIL_EXIST = "account/external/checkemailexist";
	private static final String LINK_USER_TOKEN = "account/external/linkusertoken";
	private static final String CHECK_EXT_LOGIN_USER = "account/external/checkexternalloginuser";

	private static final String CONNECTED_PROVIDERS_BY_USERID = "account/external/providerbyuserid";
	private static final String LOAD_ENUMS = "enums/all";
	private static final String BRAND_SEARCH_BY_KEY ="brand/products/search";
	private static final String BRAND_SEARCH_BY_PRODUCT ="brand/products/products";
	private static final String BRAND_SEARCH_BY_HOME_PRODUCT ="brand/products/homeAdProd";
	private static final String BRAND_SEARCH_BY_PDP_PRODUCT ="brand/products/pdpAdProduct";
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

	private static final String CMS_FEED_API = "cms/feed/";
	private static final String SAP_GROUP_PRICE_LOADER_LOAD_API ="dataloader/sapGrp/groupScalePrice";


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
	private static final String DLV_MANAGER_ZIPATTRIBUTES_BY_ZIP = "dlvmanager/zipCodeAttributes";
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
	private static final String SMS_MESSAGE_GET = "sms/get";
	private static final String SMS_MESSAGE_CASECREATION_UPATE= "sms/updatecasestatus";

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



	private static final String ERP_PRODUCTINFO_SKUCODE_VERSION = "erpinfo/productbyskuandversion";
	private static final String ERP_GOING_OUTOFSTOCK_SALAREA ="erpinfo/goingOutOfStockSalesAreas";
	private static final String ERP_PEAK_PRODUCE_SKUS_BY_DEPARTMENT = "erpinfo/peakproduceskusbydepartment";
	private static final String SEND_GIFTCARD = "giftcardGateway/send/";

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
	private static final String MODIFY_ORDER_REQUEST = "routing/modifyorderrequest";
	private static final String CANCEL_ORDER_REQUEST = "routing/cancelorderrequest";
	private static final String SUBMIT_ORDER_REQUEST = "routing/submitorderrequest";

	private static final String GET_TICKET = "ticket";
	private static final String UPDATE_TICKET = "ticket/update";
	private static final String ADD_TICKET = "ticket/create";


	private static final String GET_START_DATES = "variant/startdates";
	private static final String GET_VARIANTS = "variant";
	private static final String GET_VARIANT = "variant/site/";
	private static final String GET_COHORTS = "variant/cohort";
	private static final String GET_COHORTS_NAMES = "variant/cohortname";
	private static final String GET_FDX_QUERYFORSALESPICKELIGIBLE = "fdxorderpick/queryforsalespickeligible";
	private static final String POST_FDX_ELIGIBLE_SENDORDERSTOSAP = "fdxorderpick/sendorderstosap";

	private static final String SAP_MATERIAL_INFO="saploader/materialinfo";
	private static final String SAP_FIND_BY_SAP_ID="/findBySapId/";
	private static final String SAP_SAVE_DATA="saploader/save";
	private static final String SAP_UPDATE_DATA="saploader/update";
	private static final String SAP_LOAD_DATA="saploader/loaddata";
	private static final String SAP_LOAD_SALES="saploader/loadsaleunit";
	private static final String SAP_LOAD_PRICE="saploader/loadprice";
	private static final String SAP_LOAD_MAT_PLANT_SALES_AREA="saploader/loadmatplantsandsalesarea";

	private static final String ECOUPON_LOAD_SAVE = "ecoupon/loadsave";
	private static final String ECOUPON_ACTIVE_COUPONS = "ecoupon/activecoupons";
	private static final String ECOUPON_ACTIVE_COUPON_DATE = "ecoupon/activecouponsbydate/";
	private static final String ECOUPON_WALLET = "ecoupon/couponwallet";
	private static final String ECOUPON_CLIP_COUPON = "ecoupon/clipcoupon/";
	private static final String ECOUPON_EVALUATE = "ecoupon/evaluate";
	private static final String ECOUPON_CANCEL_PENDING = "ecoupon/post/cancelpendingcoupontrans";
	private static final String ECOUPON_CONF_PENDING = "ecoupon/confirmpendingcoupontrans/";
	private static final String ECOUPON_CONFM_PENDING = "ecoupon/confirmpendingcouponsales";
	private static final String ECOUPON_SUB_PENDING = "ecoupon/submitpendingcouponsales";
	private static final String ECOUPON_SUBM_PENDING = "ecoupon/submitcoupontrans/";


	private static final String FDFACTORY_CHARECTERISTIC_BYMATID = "productinfo/findByMaterialId";


	private static final String CREATE_FRAUD_ENTRY="fraudactivity/create";
	private static final String FIND_FRAUD_ENTRY_ID="fraudactivity/findbyid";
	private static final String FIND_FRAUD_ENTRY_CUSTID_STATUS="fraudactivity/findbycustomerandstatus";
	private static final String FIND_FRAUD_ENTRY_ID_STATUS="fraudactivity/findbyidandstatus";
	private static final String FIND_FRAUD_ENTRY_BY_CRITERIA="fraudactivity/findbycriteria";
	private static final String UPDATE_FRAUD_ENTRY="fraudactivity/update";
	private static final String REMOVE_FRAUD_ENTRY="fraudactivity/delete";
	private static final String VERIFY_FRAUD_ENTRY="fraudactivity/validate";
	private static final String LOAD_ALL_FRAUD_ENTRY_PATTERN="fraudactivity/loadpattern";
	private static final String LOAD_ALL_FRAUD_ENTRY="fraudactivity/loadrestrictedpaymentmethd";
	private static final String LOAD_ALL_BAD_ENTRY="fraudactivity/loadbadpaymentmethods";
	private static final String LOAD_ALL_FRAUD_ENTRY_BY_ACCOUNTINFO="fraudactivity/paymentinfobyaccount";



	private static final String ENQUEUE_EMAIL = "mailer/enqueue";
	private static final String SITEMAP_GENERATE = "sitemap/generate";

	private static final String CHECK_TOKEN_VALID = "paymentManager/isValidVaultToken";

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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException {

		Response<List<ZoneInfoDataWrapper>> response;
		try {

			response = httpGetDataTypeMap(getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE + ppType),
					new TypeReference<Response<List<ZoneInfoDataWrapper>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

			Map<ZoneInfo, List<FDProductPromotionInfo>> data = new HashMap<ZoneInfo, List<FDProductPromotionInfo>>();
			for (ZoneInfoDataWrapper wrapper : response.getData()) {
				ZoneInfo key = ModelConverter.buildZoneInfo(wrapper.getKey());
				List<FDProductPromotionInfo> value = ModelConverter.buildFDProductPromotionInfo(wrapper.getValue());
				data.put(key, value);
			}
			return data;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType, Date lastPublished)
			throws FDResourceException {

		Response<List<ZoneInfoDataWrapper>> response;
		try {
			long date1 = 0;
			if (lastPublished != null) {
				date1 = lastPublished.getTime();
			}
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE + ppType + "/lastPublishDate/" + date1),
					new TypeReference<Response<List<ZoneInfoDataWrapper>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

			Map<ZoneInfo, List<FDProductPromotionInfo>> data = new HashMap<ZoneInfo, List<FDProductPromotionInfo>>();
			for (ZoneInfoDataWrapper wrapper : response.getData()) {
				ZoneInfo key = ModelConverter.buildZoneInfo(wrapper.getKey());
				List<FDProductPromotionInfo> value = ModelConverter.buildFDProductPromotionInfo(wrapper.getValue());
				data.put(key, value);
			}
			return data;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> getAllPromotionsByType(String ppType,
			Date lastPublished) throws FDResourceException {

		Response<Map<String, List<ZoneInfoDataWrapper>>> response;
		try {
			long date1 = 0;
			if (lastPublished != null) {
				date1 = lastPublished.getTime();
			}

			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(PROMOTION_BY_TYPE + ppType + "/lastPublishDate/" + date1),
					new TypeReference<Response<Map<String, List<ZoneInfoDataWrapper>>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

			Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> data = new HashMap<String, Map<ZoneInfo, List<FDProductPromotionInfo>>>();
			for (String key : response.getData().keySet()) {

				Map<ZoneInfo, List<FDProductPromotionInfo>> zoneData = new HashMap<ZoneInfo, List<FDProductPromotionInfo>>();
				for (ZoneInfoDataWrapper wrapper : response.getData().get(key)) {
					ZoneInfo zoneInfo = ModelConverter.buildZoneInfo(wrapper.getKey());
					List<FDProductPromotionInfo> value = ModelConverter.buildFDProductPromotionInfo(wrapper.getValue());
					zoneData.put(zoneInfo, value);
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
	public List<FDProductPromotionInfo> getProductsByZoneAndType(String ppType, String zoneId)
			throws FDResourceException {

		Response<List<FDProductPromotionInfoData>> response;
		try {

			response = httpGetDataTypeMap(getFdCommerceEndPoint(PRODUCT_BY_PROMO_TYPE + ppType + "/zoneId/" + zoneId),
					new TypeReference<Response<List<FDProductPromotionInfoData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

			List<FDProductPromotionInfo> data = new ArrayList<FDProductPromotionInfo>();
			data = ModelConverter.buildFDProductPromotionInfo(response.getData());
			return data;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)
			throws FDResourceException {

		Response<ErpProductPromotionPreviewInfoData> response;
		try {

			response = httpGetDataTypeMap(getFdCommerceEndPoint(PROMOTION_PREVIEW + "promPreview/" + ppPreviewId),
					new TypeReference<Response<ErpProductPromotionPreviewInfoData>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

			ErpProductPromotionPreviewInfo data;
			data = ModelConverter.buildErpProductPromotionPreviewInfo(response.getData());
			return data;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}

	}

	@Override
	public NavigableMap<Long, BINInfo> getActiveBINs() throws FDResourceException {
		Response<NavigableMap<Long, BINData>> response;

		response = httpGetDataTypeMap(getFdCommerceEndPoint(GET_ACTIVE_BINS),
				new TypeReference<Response<NavigableMap<Long, BINData>>>() {
				});
		if (!response.getResponseCode().equals("OK"))
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
			} catch (FDEcommServiceException e) {

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
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException, FDResourceException {
		Response<ErpMasterInfoData> response = new Response<ErpMasterInfoData>();
		response = httpGetDataTypeMap(getFdCommerceEndPoint(ZONE_INFO_MASTER+"?zoneId="+ zoneId), new TypeReference<Response<ErpMasterInfoData>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
			return ModelConverter.buildErpMasterInfo(response.getData());
	}

	@Override
	public Collection<String> loadAllZoneInfoMaster() throws RemoteException, FDResourceException {


			Response<Collection<String>> response = httpGetData(getFdCommerceEndPoint(ALL_ZONE_INFO_MASTER), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			return response.getData();


	}
	@Override
	public String findZoneId(String zoneServiceType, String zipCode) throws RemoteException, FDResourceException{
		Response<String> response = new Response<String>();

			response = httpGetData(getFdCommerceEndPoint(LOAD_ZONE_ID)+"?zoneServiceType="+zoneServiceType+"&zipCode="+zipCode, Response.class);

		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}
	@Override
	public String findZoneId(String zoneServiceType, String zipCode, boolean isPickupOnlyORNotServicebleZip) throws RemoteException{
		Response<String> response = new Response<String>();

			try {
				LOGGER.info("give input are zoneServiceType: "+zoneServiceType+" zipCode: "+zipCode+" isPickupOnlyORNotServicebleZip: "+isPickupOnlyORNotServicebleZip+" starting time: "+System.currentTimeMillis());
				response = httpGetData(getFdCommerceEndPoint(LOAD_ZONE_ID_ISPICK)+"?zoneServiceType="+zoneServiceType+"&zipCode="+zipCode+"&isPickup="+isPickupOnlyORNotServicebleZip, Response.class);
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
				response = httpGetData(getFdCommerceEndPoint(LOAD_ZONE_BY_SERVICETYPE)+"?zoneServiceType="+servType, Response.class);
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
		Response<List<ErpMasterInfoData>> response = new Response<List<ErpMasterInfoData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(ALL_ZONE_INFO), new TypeReference<Response<List<ErpMasterInfoData>>>() {});
		} catch (FDResourceException e) {

			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return ModelConverter.buildErpMasterInfoList(response.getData());
	}

	@Override
	public Collection<ErpZoneMasterInfo> findZoneInfoMaster(String[] zoneIds)
			throws RemoteException {
		try{
		Request<String[]> request = new Request<String[]>();
		request.setData(zoneIds);
		String inputJson = buildRequest(request);
		Response<Collection<ErpMasterInfoData>> response = null;
		response =  this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOAD_ZONE_BY_ZONEIDS), new TypeReference<Response<Collection<ErpMasterInfoData>>>() {});
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}
		return ModelConverter.buildErpMasterInfoList((List<ErpMasterInfoData>) response.getData());
	} catch (FDEcommServiceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}catch (FDResourceException e){
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}
	}





	@Override
	public String getFamilyIdForMaterial(String matId) throws RemoteException, FDResourceException {
		Response<String> response = new Response<String>();
		try{
			response = httpGetData(getFdCommerceEndPoint(FAMILYID_FOR_MATERIAL)+"?matId=", Response.class);
		} catch(FDResourceException e){
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSocialLoginOnlyUser(String customer_id) {
		Response<Boolean> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint("account/external/checksocialloginuser?customerId=" + customer_id),
					new TypeReference<Response<Boolean>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

			return response.getData();
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		}
		return false;

	}
	@Override
	public List<String> getConnectedProvidersByUserId(String userId) throws RemoteException {
		Response<List<String>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint("/account/external/providers?userId="+userId),new TypeReference<Response<List<String>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
            throw new RemoteException(e.getMessage(), e);
		}
		return response != null ? response.getData() : null;

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
			}else if(data.get(1) instanceof ErpAffiliate ){
				return data;
			}else if(data.get(1) instanceof DeliveryPassTypeData ){
				return ModelConverter.buildDeliveryPassTypeList(data);
			}else if(data.get(1) instanceof EnumFeaturedHeaderTypeData ){
				return ModelConverter.buildEnumFeaturedHeaderTypeList(data);
			}else if(data.get(1) instanceof CrmCaseSubjectData ){
				return ModelConverter.buildCrmCaseSubjectList(data);
			}else if(data.get(1) instanceof EnumComplaintDlvIssueTypeData){
				return ModelConverter.buildComplaintDlvIssueList(data);
			}else if(data.get(1) instanceof CrmCasePriorityData){
				return ModelConverter.buildCasePriorityList(data);
			}else if(data.get(1) instanceof CrmEnumTypeData){
				CrmEnumTypeData obj = (CrmEnumTypeData)data.get(1);
				if(obj.getDaoClassName().equals("CrmAgentRoleDAO"))
					return ModelConverter.buildCrmAgentRileList(data);
				if(obj.getDaoClassName().equals("CrmCaseActionTypeDAO"))
					return ModelConverter.buildCaseActionTypeList(data);
				if(obj.getDaoClassName().equals("CrmCaseOriginDAO"))
					return ModelConverter.buildCrmCaseOriginList(data);
				if(obj.getDaoClassName().equals("CrmCaseQueueDAO"))
					return ModelConverter.buildCrmCaseQueueList(data);
				if(obj.getDaoClassName().equals("CrmCaseStateDAO"))
					return ModelConverter.buildCrmCaseStateList(data);
				if(obj.getDaoClassName().equals("CrmDepartmentDAO"))
					return ModelConverter.buildCrmDepartmentList(data);
			}
		}

		return null;
	}

	private <E> E typeReferenceFor(String daoClassName) {


		if (daoClassName.equals("BillingCountryDAO")) {
			return   (E) new TypeReference<Response<List<BillingCountryInfoData>>>(){} ;
		} else if (daoClassName.equals("ErpAffiliateDAO")) {
			return  (E) new TypeReference<Response<List<ErpAffiliate>>>() {} ;
		} else if (daoClassName.equals("DlvPassTypeDAO")) {
			return  (E) new TypeReference<Response<List<DeliveryPassTypeData>>>() {} ;
		} else if (daoClassName.equals("EnumFeaturedHeaderTypeDAO")) {
			return  (E) new TypeReference<Response<List<EnumFeaturedHeaderTypeData>>>() {} ;
		} else if (daoClassName.equals("CrmCaseSubjectDAO")) {
			return   (E) new TypeReference<Response<List<CrmCaseSubjectData>>>() {} ;
		}else if (daoClassName.equals("CrmComplaintDlvTypeDAO")) {
			return   (E) new TypeReference<Response<List<EnumComplaintDlvIssueTypeData>>>() {} ;
		}else if (daoClassName.equals("CrmCasePriorityDAO")) {
			return   (E) new TypeReference<Response<List<CrmCasePriorityData>>>() {} ;
		}else  {
			return   (E) new TypeReference<Response<List<CrmEnumTypeData>>>() {} ;
		}

	}

	
	
	@Override
	public HLBrandProductAdResponse getSearchbykeyword(
			HLBrandProductAdRequest hLRequestData) throws RemoteException {
		Response<HLBrandProductAdResponse> responses = null;
		try {
			Request<HLBrandProductAdRequest> request = new Request<HLBrandProductAdRequest>();
			request.setData(hLRequestData);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response = this.postData(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_KEY), Response.class);
			responses =  this.postDataTypeMap(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_KEY), new TypeReference<Response<HLBrandProductAdResponse>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return responses.getData();
		} catch (FDEcommServiceException e) {
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
			Response<HLBrandProductAdResponse> response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_PRODUCT), new TypeReference<Response<HLBrandProductAdResponse>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
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
			new Request<HLBrandProductAdRequest>();
			@SuppressWarnings("unchecked")
			Response<Long> response = this.httpGetData(getFdCommerceEndPoint(BRAND_LAST_SENT_FEED), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return new Timestamp(response.getData());
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
			Response<HLBrandProductAdResponse> response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(BRAND_ORDER_SUBMIT_BYDATE), new TypeReference<Response<HLBrandProductAdResponse>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}

		} catch (FDEcommServiceException e) {
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
			Response<HLBrandProductAdResponse> response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(BRAND_ORDER_SUBMIT_SALEIDS), new TypeReference<Response<HLBrandProductAdResponse>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}

		} catch (FDEcommServiceException e) {
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
		} catch(FDResourceException e){
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
		} catch(FDResourceException e){
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
//			response = this.postData(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_UPDATE), Response.class);
			response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_UPDATE), new TypeReference<Response<HLBrandProductAdResponse>>() {});
			if(!response.getResponseCode().equals("CREATED")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
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
			response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(EXTOLE_MANAGER_SAVE), new TypeReference<Response<HLBrandProductAdResponse>>() {});
			if(!response.getResponseCode().equals("CREATED")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
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
			Response<Void> response = this.postData(inputJson, getFdCommerceEndPoint(EVENT_LOGGER), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
//			return response.getData();
		} catch (FDEcommServiceException e) {
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
			Request<FDCouponActivityLogData> couponActivityLogReq = new Request<FDCouponActivityLogData>();

			FDCouponActivityLogData fDCouActLogData = ModelConverter.buildCouponActivityData(log);
			couponActivityLogReq.setData(fDCouActLogData);
			String inputJson = buildRequest(couponActivityLogReq);
			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(LOG_ECOUPON_ACTIVITY), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
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
			} catch (FDEcommServiceException e) {
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
			Response<ErpCustEWalletData> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CUSTEWALLET_TOKEN_BY_CUSTID) + custID + "/" + eWalletType, new TypeReference<Response<ErpCustEWalletData>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return ModelConverter.buildErpCustEwalletModel(response.getData());
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
			Response<List<ErpEWalletModel>> response = this.httpGetData(getFdCommerceEndPoint(""), Response.class);
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException {
		Response<List<SiteAnnouncement>> response = this.httpGetData(getFdCommerceEndPoint(DLV_MANAGER_FDANNOUNCEMENT), Response.class);
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
		Set<StateCounty> stateCountySet = new HashSet<StateCounty>();
		Response<List<StateCountyData>> response = this.httpGetDataTypeMap((getFdCommerceEndPoint(DLV_MANAGER_COUNTIES_BY_STATE)+"/"+state), new TypeReference<Response<List<StateCountyData>>>() {});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		for (StateCountyData stateCountyData : response.getData()) {
			StateCounty stateCounty = new StateCounty(stateCountyData.getState(), stateCountyData.getCounty(),
					stateCountyData.getCity());
			stateCountySet.add(stateCounty);

		}
		return stateCountySet;
	}

	@Override
	public StateCounty lookupStateCountyByZip(String zip) throws FDResourceException {
		Response<StateCountyData> response = this.httpGetDataTypeMap((getFdCommerceEndPoint(DLV_MANAGER_COUNTIES_BY_ZIP+"/"+zip)), new TypeReference<Response<StateCountyData>>() {});
		StateCountyData stateCountyData = response.getData();
		StateCounty stateCounty = new StateCounty(stateCountyData.getState(), stateCountyData.getCounty(),
				stateCountyData.getCity());
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return stateCounty;
	}

	//OPT-44 start
	@Override
	public ZipCodeAttributes lookupZipCodeAttributes(String zip) throws FDResourceException {
		Response<ZipCodeAttributes> response = this.httpGetDataTypeMap((getFdCommerceEndPoint(DLV_MANAGER_ZIPATTRIBUTES_BY_ZIP+"/"+zip)), new TypeReference<Response<ZipCodeAttributes>>() {});
		ZipCodeAttributes zipAttributes = response.getData();
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return zipAttributes;
	}
	//OPT-44 end

	@Override
	public int unlockInModifyOrders() throws FDResourceException {
		Response<Integer> response = this.httpGetData(getFdCommerceEndPoint(DLV_MANAGER_RELEASE_ORDER_LOCK), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}

	@Override
	public int queryForMissingFdxOrders() throws FDResourceException {
		Response<Integer> response = this.httpGetData(getFdCommerceEndPoint(DLV_MANAGER_MISSING_ORDER), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		return response.getData();
	}

	@Override
	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws FDResourceException, ReservationException {
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

			if (!response.getResponseCode().equals("OK")) {
				if(response.getRequestIdentifier()!=null&&response.getRequestIdentifier().equals("ReservationUnavailableException"))
					throw new ReservationUnavailableException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
				if(response.getRequestIdentifier()!=null&&response.getRequestIdentifier().equals("ReservationException"))
					throw new ReservationException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
			}
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e);
		} catch (FDResourceException e) {
			throw new FDResourceException(e);
		}

	}

	@Override
	public void recommitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1) throws FDResourceException, ReservationException {
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
			if (!response.getResponseCode().equals("OK")) {
				if(response.getRequestIdentifier()!=null&&response.getRequestIdentifier().equals("ReservationUnavailableException"))
					throw new ReservationUnavailableException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
				if(response.getRequestIdentifier()!=null&&response.getRequestIdentifier().equals("ReservationException"))
					throw new ReservationException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
			}
		} catch (FDEcommServiceException e) {
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
			LOGGER.info("sending request to SF2.0 updateInventories, request=" + inputJson);
			Response<Void> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_INVENTORY_UPDATE),
					new TypeReference<Response<Void>>() {
					});

			if(!response.getResponseCode().equals("OK")){
				LOGGER.error("Error occurs in updateInventories inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error occurs in updateInventories erpInventoryEntryModel=" + erpInventoryEntryModel, e);
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
		throw new FDResourceException(e, "Unable to process the request.");
		}

	}

	@Override
	public boolean smsOptIn(String customerId, String mobileNumber,String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_ALERT_OPTIN +"?customerId="+customerId+"&mobileNumber="+PhoneNumber.normalize(mobileNumber)+"&eStoreId="+eStoreId)), new TypeReference<Response<Boolean>>() {});
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
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_ALERT_OPTIN_NONMARKETING +"?customerId="+customerId+"&mobileNumber="+PhoneNumber.normalize(mobileNumber)+"&eStoreId="+eStoreId)), new TypeReference<Response<Boolean>>() {});
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
		response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_ALERT_OPTIN_MARKETING +"?customerId="+customerId+"&mobileNumber="+PhoneNumber.normalize(mobileNumber)+"&eStoreId="+eStoreId)), new TypeReference<Response<Boolean>>() {});
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
			EnumEStoreId eStoreId,boolean isCaseCreated) throws  FDResourceException {
		try {
			String inputJson;
			Request<RecievedSmsData> recieveSmsData = ModelConverter.buildSmsDataRequest(
					 PhoneNumber.normalize(mobileNumber), shortCode, carrierName, receivedDate,
					message, eStoreId,isCaseCreated);
			inputJson = buildRequest(recieveSmsData);
			postDataTypeMap(inputJson, getFdCommerceEndPoint(SMS_MESSAGE_UPDATE), new TypeReference<Response<Void>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOrderCancel(String customerId, String mobileNumber,String orderId, String eStoreId) throws  FDResourceException {
		Response<Boolean> response = null;
		try {
			Request<SmsOrderData> request = ModelConverter.buildSmsOrderDataRequest(customerId, PhoneNumber.normalize(mobileNumber), orderId, eStoreId);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_ORDER_CANCEL),Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOrderConfirmation(String customerId, String mobileNumber,String orderId, String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
			Request<SmsOrderData> request = ModelConverter.buildSmsOrderDataRequest(customerId, PhoneNumber.normalize(mobileNumber), orderId, eStoreId);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_ORDER_CONFIRM), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}
	@Override
	public boolean smsOrderModification(String customerId, String mobileNumber,String orderId, String eStoreId) throws FDResourceException {
		Response<Boolean> response = null;
		try {
			Request<SmsOrderData> request = ModelConverter.buildSmsOrderDataRequest(customerId, PhoneNumber.normalize(mobileNumber), orderId, eStoreId);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(SMS_ALERT_ORDER_MODIFY),Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
		return response.getData();
	}

	@Override
	public List<RecievedSmsData>  getReceivedSmsData() throws FDResourceException{
		Response<List<RecievedSmsData>> response =null;
			try {
			response = this.httpGetDataTypeMap((getFdCommerceEndPoint(SMS_MESSAGE_GET)), new TypeReference<Response<List<RecievedSmsData>>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
				throw new FDResourceException(e, "Unable to process the request.");
			}
			return response.getData();

	}


	@Override
	public void updateCaseCreationStatus(String smsId, Boolean isCaseCreated)throws FDResourceException {
		try {
			String inputJson;
			Request<RecievedSmsData> recieveSmsData = ModelConverter.buildSmsDataRequest(smsId,isCaseCreated);
			inputJson = buildRequest(recieveSmsData);
			postDataTypeMap(inputJson, getFdCommerceEndPoint(SMS_MESSAGE_CASECREATION_UPATE), new TypeReference<Response<Void>>() {});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
		}
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
			} catch (FDEcommServiceException e) {
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
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
			} catch (FDResourceException e) {
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
		long date1 =0;

		if(lastModified!=null){
		date1 = lastModified.getTime();
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
			} catch (FDEcommServiceException e) {
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
				if(!response.getResponseCode().equals(HttpStatus.OK.name())){
					throw new FDResourceException(response.getMessage());
				}
				result = response.getData();
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			} catch (FDEcommServiceException e) {
				LOGGER.error(e.getMessage());
				throw new FDRuntimeException(e, "Unable to process the request.");
			}
			return result.getResult();
	}

	// GETCMSDATA BY STORE
	// story 17-22
	@Override
	public String getCmsFeed(String storeID) throws FDResourceException {
		String payload;
		String responseStr = "";
		try {

			Request<String> request = new Request<String>();

			request.setData("");
			/*
			 * This returns the entire json as a string for processing in a later step
			 *
			 */
			responseStr = httpGetData(getFdCommerceEndPoint(CMS_FEED_API + storeID), String.class);

			Response<String> responseOfTypestring;
			
			responseOfTypestring = getMapper().readValue(responseStr, new TypeReference<Response<String>>() {
			});
			// check to see if everything was ok with the restful service call.
			if (!responseOfTypestring.getResponseCode().equalsIgnoreCase(HttpStatus.OK.name())) {

				if (SystemMessages.GENERIC_ERROR_MESSAGE.equals(responseOfTypestring.getMessage())) {
					LOGGER.error(" getCmsFeed: storeID=" + storeID );
					throw new FDResourceException(
							responseOfTypestring.getError().get(SystemMessages.GENERIC_ERROR_MESSAGE).toString());
				}
				LOGGER.error(" getCmsFeed: storeID=" + storeID);
				throw new FDResourceException(responseOfTypestring.getMessage());
			}
			payload = responseOfTypestring.getData();
		} catch (Exception ex) {

			LOGGER.error(" getCmsFeed: storeID=" + storeID , ex);
			throw new FDResourceException("Could not retrieve cms information for id: " + storeID);

		}

		return payload;

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
				new Request<String>();
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
				new Request<String>();
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
				new Request<String>();
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
				new Request<String>();
				response = this.httpGetData(getFdCommerceEndPoint(SMARTSTORE_PREFERRED_WINEPRICE)+"/"+erpCustomerId, Response.class);
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
	public void logGatewayActivity(GatewayType gatewayType,com.freshdirect.payment.gateway.Response resp)throws RemoteException{
		try {
			FDGatewayActivityLogModel logModel = ModelConverter.getFDGatewayGatewayActivityLogModel(gatewayType,resp);
			Request<FDGatewayActivityLogModelData> gatewayActivityLogReq = new Request<FDGatewayActivityLogModelData>();
			gatewayActivityLogReq.setData(ModelConverter.convertFDGatewayActivityLogModel(logModel));
			String inputJson = buildRequest(gatewayActivityLogReq);
			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(LOG_GATEWAY_ACTIVITY), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
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
		this.httpGetData(getFdCommerceEndPoint(DELETE_RULE+"?ruleId="+ruleId), Response.class);
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

		} catch (FDEcommServiceException e) {
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
			} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		}
		return ccActivityResponse;
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
	public Collection<ErpMaterialSalesAreaModel> getGoingOutOfStockSalesAreas()
			throws RemoteException {
		Response<Collection<ErpMaterialSalesAreaData>> response = null;
		Collection<ErpMaterialSalesAreaModel> erpMaterialSalesAreaModel;
		new Request<String[]>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_GOING_OUTOFSTOCK_SALAREA),  new TypeReference<Response<Collection<ErpMaterialSalesAreaData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpMaterialSalesAreaModel = ErpMaterialSalesAreaModelConverter.convertListDataToListModel(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		}
		return erpMaterialSalesAreaModel;
	}



	@Override
	public Collection<String> findPeakProduceSKUsByDepartment(List<String> skuPrefixes) throws RemoteException {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
			} catch (FDEcommServiceException e) {
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
		catch (FDEcommServiceException e) {
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
		catch (FDEcommServiceException e) {
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
		catch (FDEcommServiceException e) {
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
		catch (FDEcommServiceException e) {
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
		catch (FDEcommServiceException e) {
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
		catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		try {
			Response<String> response = this.postDataTypeMap(null, getFdCommerceEndPoint(CANCEL_ORDER_REQUEST+"?saleId="+saleId),  new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}

		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}

	}
	@Override
	public void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType,
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol) throws RemoteException {

		SubmitOrderRequestData submitOrderRequestData = new SubmitOrderRequestData();
		submitOrderRequestData = buildOrderRequestData(saleId, parentOrderId, tip, reservationId, firstName, lastName, deliveryInstructions, serviceType,
				 unattendedInstr, PhoneNumber.normalize(orderMobileNumber),erpOrderId,containsAlcohol);
		Request<SubmitOrderRequestData> request = new Request<SubmitOrderRequestData>();
		try {
			request.setData(submitOrderRequestData);
			String inputJson = buildRequest(request);
			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(MODIFY_ORDER_REQUEST),  new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}

	}
	@Override
	public void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) throws RemoteException {
		try {
			String addressString = buildRequest(address);
			Response<String> response = this.postDataTypeMap(addressString, getFdCommerceEndPoint(RESERVATION_UPDATE+"?reservationId="+reservationId+"&sapOrderNumber="+sapOrderNumber),  new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
			//
		}

	}
	@Override
	public void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType,
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol) throws RemoteException {
		SubmitOrderRequestData submitOrderRequestData = new SubmitOrderRequestData();
		submitOrderRequestData = buildOrderRequestData(saleId, parentOrderId, tip, reservationId, firstName, lastName, deliveryInstructions, serviceType,
				 unattendedInstr, PhoneNumber.normalize(orderMobileNumber),erpOrderId,containsAlcohol);
		Request<SubmitOrderRequestData> request = new Request<SubmitOrderRequestData>();
		try {
			request.setData(submitOrderRequestData);
			String inputJson = buildRequest(request);
			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SUBMIT_ORDER_REQUEST),  new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}

	}

	private SubmitOrderRequestData buildOrderRequestData(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType,
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol){
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



	@Override
	 public Ticket createTicket(Ticket ticket) throws RemoteException {
	Request<TicketData> request = new Request<TicketData>();
	Response<TicketData> response= null;
	try {
		request.setData(ModelConverter.buildTicketData(ticket));
		String inputJson = buildRequest(request);
		response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_TICKET),  new TypeReference<Response<TicketData>>() {});
	} catch (FDResourceException e){
		LOGGER.error(e.getMessage());
		throw new FDRuntimeException(e, "Unable to process the request.");
	} catch (FDEcommServiceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}
	return ModelConverter.buildTicket(response.getData());
	}

	@Override
	public Ticket updateTicket(Ticket ticket) throws RemoteException {
		Request<TicketData> request = new Request<TicketData>();
		Response<TicketData> response= null;
		try {
			request.setData(ModelConverter.buildTicketData(ticket));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_TICKET),  new TypeReference<Response<TicketData>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildTicket(response.getData());
	}
	@Override
	public Ticket retrieveTicket(String key) throws RemoteException {
		Response<TicketData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_TICKET+"?key="+key),  new TypeReference<Response<TicketData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildTicket(response.getData());
	}

	@Override
	public Map<String, String> getVariantMap(EnumSiteFeatureData feature)throws RemoteException {
		Response<Map<String, String>> response = null;
		Request<EnumSiteFeatureData> request = new Request<EnumSiteFeatureData>();
		try {
			request.setData(feature);
			String inputJson;
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_VARIANT),new TypeReference<Response<Map<String, String>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public Map<String, String> getVariantMap(EnumSiteFeatureData feature, Date date)
			throws RemoteException {
		Response<Map<String, String>> response = null;
		Request<EnumSiteFeatureData> request = new Request<EnumSiteFeatureData>();
		try {
			request.setData(feature);
			String inputJson;
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_VARIANTS+"/date/"+date.getTime()),new TypeReference<Response<Map<String, String>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public Map<String, Integer> getCohorts() throws RemoteException {
		Response<Map<String, Integer>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_COHORTS),  new TypeReference<Response<Map<String, Integer>>>(){});
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
	public List<String> getCohortNames() throws RemoteException {
		Response< List<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_COHORTS_NAMES),  new TypeReference<Response<List<String>>>(){});
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
	public List<Date> getStartDates() throws RemoteException {
		List<Date> dateList = new ArrayList<Date>();
		try {
			Response<List<Long>> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_START_DATES),  new TypeReference<Response<List<Long>>>(){});
			for (Long date : response.getData()) {
				dateList.add(new java.sql.Date(date));
			}
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());

		}
		return dateList;
	}

	@Override
	public List<SapOrderPickEligibleInfo> queryForFDXSalesPickEligible() throws RemoteException  {
		Response<List<SapOrderPickEligibleInfo>> response = new Response<List<SapOrderPickEligibleInfo>>();
		try {
			response = 	httpGetDataTypeMap( getFdCommerceEndPoint(GET_FDX_QUERYFORSALESPICKELIGIBLE), new TypeReference<Response<List<SapOrderPickEligibleInfo>>>(){});
			List<SapOrderPickEligibleInfo> sapOrderPickeligibleList = response.getData();
			return sapOrderPickeligibleList;
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Failure in queryForSalesPickEligible  Error: ",e);
			throw new RemoteException(" queryForSalesPickEligible failure" , e);
		}
	}
	@Override
	public void sendFDXEligibleOrdersToSap(List<SapOrderPickEligibleInfo> eligibleSapOrderLst) throws RemoteException  {

		Response<String> response = null;
		try {
			Request<List<SapOrderPickEligibleInfo> > requestofSapList = new Request<List<SapOrderPickEligibleInfo> >();
			requestofSapList.setData(eligibleSapOrderLst);
			String inputJson = buildRequest(requestofSapList);
			LOGGER.debug("sendFDXEligibleOrdersToSap calling url: "+getFdCommerceEndPoint(POST_FDX_ELIGIBLE_SENDORDERSTOSAP) );
			response = 	postData(   inputJson,getFdCommerceEndPoint(POST_FDX_ELIGIBLE_SENDORDERSTOSAP),Response.class);
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException( "Unable to process the request.",e);
		}
		catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Failure in sendFDXEligibleOrdersToSap  Error: ",e);
			throw new RemoteException(" sendFDXEligibleOrdersToSap failure" , e);
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());

	}
	//POST_FDX_ELIGIBLE_SENDORDERSTOSAP

	@Override
	public ErpMaterialBatchHistoryModel getMaterialBatchInfo() throws RemoteException {
		Response<MaterialBatchInfoData> response = new Response<MaterialBatchInfoData>();
		MaterialBatchInfoData data = new MaterialBatchInfoData();
		ErpMaterialBatchHistoryModel model = new ErpMaterialBatchHistoryModel();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SAP_MATERIAL_INFO), new TypeReference<Response<MaterialBatchInfoData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			data = response.getData();
			model.setCreatedDate(data.getCreatedDate());
			model.setStatus(EnumApprovalStatus.getApprovalStatus(data.getStatus()));
			model.setVersion(data.getVersion());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return model;
	}




	@Override
	public int createBatch() throws RemoteException{
		int batchNumber = 0;
		Response<Integer> response = new Response<Integer>();
		String inputJson = null;
		try {
			response = postData(inputJson,getFdCommerceEndPoint(SAP_SAVE_DATA), Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		Integer data  = response.getData();
		batchNumber = data.intValue();
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return batchNumber;
	}

	@Override
	public void updateBatchStatus(int batchNumber, EnumApprovalStatus batchStatus) throws RemoteException{
		Response<Void> response = new Response<Void>();
		String inputJson = null;
		try{
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAP_UPDATE_DATA)+"/"+batchNumber+"/"+batchStatus.getStatusCode(),new TypeReference<Response<Void>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}


	@Override
	public void loadData(int batchNumber, ErpMaterialModel model,
			Map<String, ErpClassModel> createErpClassModel,
			Map<ErpCharacteristicValuePriceModel, Map<String, String>> chMap) throws RemoteException {
		Response<Void> response = null;
		LoadDataInputData sapLoadDataInputParam = new LoadDataInputData();
		Request<LoadDataInputData> request = new Request<LoadDataInputData>();
		Map<String,ErpClassData> erpClassMap = new HashMap<String, ErpClassData>();
		List<CharacteristicValueMapData> charValueMapList = new ArrayList<CharacteristicValueMapData>();
		ErpMaterialData material;
		try {

			material = ModelConverter.convertErpMaterialModelToData(model);
			if (createErpClassModel != null) {
				for (Entry<String, ErpClassModel> classMap : createErpClassModel.entrySet()) {
					ErpClassData data = ModelConverter.convertErpClassModelToData(classMap.getValue());
					erpClassMap.put(classMap.getKey(), data);
				}
			}
			if (chMap != null) {
				for (Entry<ErpCharacteristicValuePriceModel, Map<String, String>> charValueMap : chMap.entrySet()) {
					CharacteristicValueMapData characteristicValueMap = new CharacteristicValueMapData();
					ErpCharacteristicValuePriceData data = ModelConverter
							.createErpCharacteristicValuePriceData(charValueMap.getKey());
					characteristicValueMap.setCaracteristicMapValue(charValueMap.getValue());
					characteristicValueMap.setErpCharValueData(data);
					charValueMapList.add(characteristicValueMap);
				}
			}
			sapLoadDataInputParam.setBatchNumber(batchNumber);
			sapLoadDataInputParam.setMaterial(material);
			sapLoadDataInputParam.setCharacteristicValueMap(charValueMapList);
			sapLoadDataInputParam.setClasses(erpClassMap);
			request.setData(sapLoadDataInputParam);
			String inputJson;
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAP_LOAD_DATA),new TypeReference<Response>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void loadSalesUnits(int batchNumber, String materialNo, Set<ErpSalesUnitModel> salesUnitModels)  throws RemoteException{
		Response<Void> response = new Response<Void>();
		String inputJson = null;
		Request<LoadSalesUnitsInputData> request = new Request<LoadSalesUnitsInputData>();
		LoadSalesUnitsInputData inputParam = new LoadSalesUnitsInputData();
		inputParam.setBatchNumber(batchNumber);
		inputParam.setMaterialNo(materialNo);
		Set<ErpSalesUnitData> salesUnits = new HashSet<ErpSalesUnitData>();
		for (ErpSalesUnitModel erpSaleUnitModel : salesUnitModels) {
			ErpSalesUnitData data = ModelConverter.convertErpSaleUnitModelToData(erpSaleUnitModel);
			salesUnits.add(data);
		}
		inputParam.setSalesUnits(salesUnits);
		request.setData(inputParam);

		try{
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAP_LOAD_SALES),new TypeReference<Response<Void>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void loadPriceRows(int batchNumber, String materialNo, List<ErpMaterialPriceModel> priceRowModels)  throws RemoteException{
		Response<Void> response = new Response<Void>();
		String inputJson = null;
		Request<LoadPriceInputData> request = new Request<LoadPriceInputData>();
		LoadPriceInputData inputParam = new LoadPriceInputData();
		inputParam.setBatchNumber(batchNumber);
		inputParam.setMaterialNo(materialNo);

		List<ErpMaterialPriceData> priceRows = new ArrayList<ErpMaterialPriceData>();
		for (ErpMaterialPriceModel erpMatPriceModel : priceRowModels) {
			ErpMaterialPriceData data = ModelConverter.convertErpMaterialPriceModelToData(erpMatPriceModel);
			priceRows.add(data);
		}
		inputParam.setPriceRows(priceRows);
		request.setData(inputParam);
		try{
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAP_LOAD_PRICE),new TypeReference<Response<Void>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void loadMaterialPlantsAndSalesAreas(int batchNumber, String materialNo, List<ErpPlantMaterialModel> erpPlantModels, List<ErpMaterialSalesAreaModel> salesAreaModels) throws RemoteException{
		Response<Void> response = new Response<Void>();
		String inputJson = null;
		Request<LoadMatPlantAndSalesInputData> request = new Request<LoadMatPlantAndSalesInputData>();
		LoadMatPlantAndSalesInputData inputParam = new LoadMatPlantAndSalesInputData();
		inputParam.setBatchNumber(batchNumber);
		inputParam.setMaterialNo(materialNo);

		List<ErpPlantMaterialData> plants = new ArrayList<ErpPlantMaterialData>();
		for (ErpPlantMaterialModel erpPlantMatModel : erpPlantModels) {
			ErpPlantMaterialData data = ModelConverter.convertErpPlantMaterialModelToData(erpPlantMatModel);
			plants.add(data);
		}
		inputParam.setPlants(plants);

		List<ErpMaterialSalesAreaData> salesAreas = new ArrayList<ErpMaterialSalesAreaData>();
		for (ErpMaterialSalesAreaModel erpMatSaleAreaModel : salesAreaModels) {
			ErpMaterialSalesAreaData data = ModelConverter.convertErpMaterialSalesAreaModelToData(erpMatSaleAreaModel);
			salesAreas.add(data);
		}
		inputParam.setSalesAreas(salesAreas);

		request.setData(inputParam);
		try{
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAP_LOAD_MAT_PLANT_SALES_AREA),new TypeReference<Response<Void>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	/*************************FDCouponManagerSessionBean*******************/


	@Override
	public void loadAndSaveCoupons(FDCouponActivityContext context) throws RemoteException {
		try {
			Request<FDCouponActivityContextData> request = new Request<FDCouponActivityContextData>();
			request.setData(getMapper().convertValue(context, FDCouponActivityContextData.class));
			String inputJson = buildRequest(request);

			Response<Void> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ECOUPON_LOAD_SAVE), new TypeReference<Response<Void>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<FDCouponInfo> getActiveCoupons() throws RemoteException {
		Response<List<FDCouponInfo>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_ACTIVE_COUPONS), new TypeReference<Response<List<FDCouponInfo>>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List<FDCouponInfo> getActiveCoupons(Date lastModified) throws RemoteException {
		Response<List<FDCouponInfo>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_ACTIVE_COUPON_DATE + lastModified.getTime()), new TypeReference<Response<List<FDCouponInfo>>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws RemoteException {
		Response<FDCustomerCouponWallet> response = null;
		try {
			Request<CouponWalletRequestData> request = new Request<CouponWalletRequestData>();
			FDCouponCustomerData fdCouponCustomerData = getMapper().convertValue(couponCustomer, FDCouponCustomerData.class);
			FDCouponActivityContextData fdCouponActivityContextData = getMapper().convertValue(context, FDCouponActivityContextData.class);
			request.setData(ModelConverter.convertCouponWalletRequest(fdCouponCustomerData, fdCouponActivityContextData));
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ECOUPON_WALLET), new TypeReference<Response<FDCustomerCouponWallet>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws RemoteException {
		Response<Boolean> response = null;
		try {
			Request<CouponWalletRequestData> request = new Request<CouponWalletRequestData>();
			FDCouponCustomerData fdCouponCustomerData = getMapper().convertValue(couponCustomer, FDCouponCustomerData.class);
			FDCouponActivityContextData fdCouponActivityContextData = getMapper().convertValue(context, FDCouponActivityContextData.class);
			request.setData(ModelConverter.convertCouponWalletRequest(fdCouponCustomerData, fdCouponActivityContextData));
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ECOUPON_CLIP_COUPON + couponId), new TypeReference<Response<Boolean>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public Map<String, FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart, FDCouponActivityContext context) throws RemoteException {
		Response<Map<String, FDCouponEligibleInfo>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("couponCart", getMapper().convertValue(couponCart, JsonNode.class));
			rootNode.set("context", getMapper().convertValue(context, JsonNode.class));
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ECOUPON_EVALUATE), new TypeReference<Response<Map<String, FDCouponEligibleInfo>>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void postCancelPendingCouponTransactions() throws RemoteException {
		Response<Object> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_CANCEL_PENDING), new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void postConfirmPendingCouponTransactions(String saleId) throws RemoteException {
		Response<Object> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_CONF_PENDING + saleId), new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<String> getConfirmPendingCouponSales() throws RemoteException {
		Response<List<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_CONFM_PENDING), new TypeReference<Response<List<String>>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List<String> getSubmitPendingCouponSales() throws RemoteException {
		Response<List<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_SUB_PENDING), new TypeReference<Response<List<String>>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void postSubmitPendingCouponTransactions(String saleId) throws RemoteException {
		Response<Object> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ECOUPON_SUBM_PENDING + saleId), new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}


	@Override
	public List<String> getVariants(EnumSiteFeatureData feature) throws RemoteException {
		Response< List<String>> response = null;
		Request<EnumSiteFeatureData> request = new Request<EnumSiteFeatureData>();
		try {
			request.setData(feature);
			String inputJson;
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_VARIANTS),new TypeReference<Response< List<String>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

//	@Override
/*	public Collection<Object> getVariants(EnumSiteFeatureData feature) throws RemoteException {
//		Collection<VariantData>
		Response<Object> response = null;
		try {
			Request<EnumSiteFeatureData> request = new Request<EnumSiteFeatureData>();
			request.setData(feature);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(""), new TypeReference<Response<Object>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return null;
	}*/


	@Override
    public void enqueueEmail(EmailI email) throws RemoteException {
		Response<String> response = null;
		try {
			Request<EmailData> request = new Request<EmailData>();
			request.setData(ModelConverter.buildEmailData(email));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ENQUEUE_EMAIL), new TypeReference<Response<String>>() {
			});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}



	@Override
	public Collection<ErpCharacteristicValuePriceModel> findByMaterialId(String materialId, int version) throws RemoteException {
		Response<Collection<ErpCharacteristicValuePriceData>> response = null;
		Collection<ErpCharacteristicValuePriceModel>	charModel = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_CHARECTERISTIC_BYMATID)+"/"+materialId+"/"+version,  new TypeReference<Response<Collection<ErpCharacteristicValuePriceData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			charModel=new ArrayList();
			for(ErpCharacteristicValuePriceData dataList:response.getData()){
				ErpCharacteristicValuePriceModel model = ModelConverter.createErpCharacteristicValuePriceModel(dataList);
				charModel.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return charModel;
	}


	@Override
	public PrimaryKey createRestrictedPaymentMethod(
			RestrictedPaymentMethodModel restrictedPaymentMethod) throws RemoteException {
		Response<String> response = null;
		Request<RestrictedPaymentMethodData> request = new Request<RestrictedPaymentMethodData>();
		RestrictedPaymentMethodData data;
		String inputJson;
		PrimaryKey key = null;
			try {
				data = ModelConverter.buildRestrictedPaymentMethodData(restrictedPaymentMethod);
				request.setData(data);
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_FRAUD_ENTRY),new TypeReference<Response<String>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				key = new PrimaryKey(response.getData());
			} catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}
			return key;
	}

	@Override
	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPrimaryKey(
			PrimaryKey pk) throws RemoteException {
		Response<RestrictedPaymentMethodData> response = null;
		RestrictedPaymentMethodModel model = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FIND_FRAUD_ENTRY_ID)+"/"+pk.getId(),  new TypeReference<Response<RestrictedPaymentMethodData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			model = ModelConverter.buildRestrictedPaymentMethodModel(response.getData());

		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return model;
	}
	@Override
	public List<RestrictedPaymentMethodModel> findRestrictedPaymentMethodByCustomerId(
			String customerId, EnumRestrictedPaymentMethodStatus status) throws RemoteException {
		Response<List<RestrictedPaymentMethodData>> response = null;
		List<RestrictedPaymentMethodModel> models = new ArrayList<RestrictedPaymentMethodModel>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FIND_FRAUD_ENTRY_CUSTID_STATUS)+"/"+customerId+"/"+status.getName(),  new TypeReference<Response<List<RestrictedPaymentMethodData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for (RestrictedPaymentMethodData restrictedPaymentMethodData : response.getData()) {
				RestrictedPaymentMethodModel model = ModelConverter.buildRestrictedPaymentMethodModel(restrictedPaymentMethodData);
				models.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return models;
	}
	@Override
	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPaymentMethodId(
			String paymentMethodId, EnumRestrictedPaymentMethodStatus status) throws RemoteException {
		Response<RestrictedPaymentMethodData> response = null;
		RestrictedPaymentMethodModel model;
		String paymentMethodStatus  = null;
		if(status != null){
			paymentMethodStatus= status.getName();
		}else {
			paymentMethodStatus = null;
		}
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FIND_FRAUD_ENTRY_ID_STATUS)+"/"+paymentMethodId+"/"+paymentMethodStatus,  new TypeReference<Response<RestrictedPaymentMethodData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
				model = ModelConverter.buildRestrictedPaymentMethodModel(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return model;
	}

	@Override
	public void storeRestrictedPaymentMethod(
			RestrictedPaymentMethodModel restrictedPaymentMethod) throws RemoteException {
		Response response = null;
		Request<RestrictedPaymentMethodData> request = new Request<RestrictedPaymentMethodData>();
		String inputJson;
		try{
			RestrictedPaymentMethodData data = ModelConverter.buildRestrictedPaymentMethodData(restrictedPaymentMethod);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_FRAUD_ENTRY),new TypeReference<Response>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}


	}
	@Override
	public void removeRestrictedPaymentMethod(PrimaryKey pk,
			String lastModifyUser) throws RemoteException {
		Response response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(REMOVE_FRAUD_ENTRY)+"/"+pk.getId()+"/"+lastModifyUser,  new TypeReference<Response>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public List<RestrictedPaymentMethodModel> loadAllPatterns() throws RemoteException {
		Response<List<RestrictedPaymentMethodData>> response = new Response<List<RestrictedPaymentMethodData>>();
		List<RestrictedPaymentMethodModel> models = new ArrayList<RestrictedPaymentMethodModel>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ALL_FRAUD_ENTRY_PATTERN),  new TypeReference<Response<List<RestrictedPaymentMethodData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for (RestrictedPaymentMethodData restrictedPaymentMethodData : response.getData()) {
				RestrictedPaymentMethodModel model = ModelConverter.buildRestrictedPaymentMethodModel(restrictedPaymentMethodData);
				models.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return models;

	}
	@Override
	public List<RestrictedPaymentMethodModel> loadAllRestrictedPaymentMethods() throws RemoteException {
		Response<List<RestrictedPaymentMethodData>> response = new Response<List<RestrictedPaymentMethodData>>();
		List<RestrictedPaymentMethodModel> models = new ArrayList<RestrictedPaymentMethodModel>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ALL_FRAUD_ENTRY),  new TypeReference<Response<List<RestrictedPaymentMethodData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for (RestrictedPaymentMethodData restrictedPaymentMethodData : response.getData()) {
				RestrictedPaymentMethodModel model = ModelConverter.buildRestrictedPaymentMethodModel(restrictedPaymentMethodData);
				models.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return models;

	}
	@Override
	public List<RestrictedPaymentMethodModel> loadAllBadPaymentMethods() throws RemoteException {
		Response<List<RestrictedPaymentMethodData>> response = new Response<List<RestrictedPaymentMethodData>>();
		List<RestrictedPaymentMethodModel> models = new ArrayList<RestrictedPaymentMethodModel>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ALL_BAD_ENTRY),  new TypeReference<Response<List<RestrictedPaymentMethodData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for (RestrictedPaymentMethodData restrictedPaymentMethodData : response.getData()) {
				RestrictedPaymentMethodModel model = ModelConverter.buildRestrictedPaymentMethodModel(restrictedPaymentMethodData);
				models.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return models;

	}
	@Override
	public boolean checkBadAccount(ErpPaymentMethodI erpPaymentMethod,
			boolean useBadAccountCache) throws RemoteException {
		Response response = null;
		Request<ErpPaymentMethodData> request = new Request<ErpPaymentMethodData>();
		String inputJson;
		boolean status;
		try{
			ErpPaymentMethodData data = ModelConverter.buildErpPaymentMethodData(erpPaymentMethod);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(VERIFY_FRAUD_ENTRY)+"/"+useBadAccountCache,new TypeReference<Response>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			status = Boolean.getBoolean(response.getData().toString());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return status;
	}
	@Override
	public ErpPaymentMethodI findPaymentMethodByAccountInfo(
			RestrictedPaymentMethodModel restrictedPaymentMethod) throws RemoteException {
		Response<ErpPaymentMethodData> response = null;
		Request<RestrictedPaymentMethodData> request = new Request<RestrictedPaymentMethodData>();
		String inputJson;
		ErpPaymentMethodI erpPaymentMethod;
		try{
			RestrictedPaymentMethodData data = ModelConverter.buildRestrictedPaymentMethodData(restrictedPaymentMethod);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(LOAD_ALL_FRAUD_ENTRY_BY_ACCOUNTINFO),new TypeReference<Response<ErpPaymentMethodData>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			erpPaymentMethod = ModelConverter.buildErpPaymentMethodModel(response.getData());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return erpPaymentMethod;
	}

	@Override
	public HLBrandProductAdResponse getHLadproductToHomeByFDPriority(
			HLBrandProductAdRequest hLBrandProductAdRequest) throws RemoteException {
		try {
			Request<HLBrandProductAdRequest> request = new Request<HLBrandProductAdRequest>();
			request.setData(hLBrandProductAdRequest);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_HOME_PRODUCT), new TypeReference<Response<HLBrandProductAdResponse>>() {});

			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public HLBrandProductAdResponse getPdpAdProduct(
			HLBrandProductAdRequest hLBrandProductAdRequest)
			throws RemoteException {
		try {
			Request<HLBrandProductAdRequest> request = new Request<HLBrandProductAdRequest>();
			request.setData(hLBrandProductAdRequest);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<HLBrandProductAdResponse> response=this.postDataTypeMap(inputJson, getFdCommerceEndPoint(BRAND_SEARCH_BY_PDP_PRODUCT), new TypeReference<Response<HLBrandProductAdResponse>>() {});

			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public boolean uploadProductFeed() throws FDResourceException {
		try {
			Response<Boolean> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPLOAD_PRODUCT_FEED),
					new TypeReference<Response<Boolean>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e);
		}
	}


	@Override
	public void generateSitemap() throws RemoteException {
		try {
			LOGGER.info("Sitemap generation calling SF2.0 Servies");
			Response<String> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SITEMAP_GENERATE),
					new TypeReference<Response<String>>() {});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			if (!response.getData().equals("success")) {
				throw new FDResourceException(response.getMessage());
			}
			LOGGER.info("Sitemap generation calling SF2.0 Servies End ");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean isValidVaultToken(String token, String customerId) throws RemoteException {
		Request<String> request = new Request<String>();
		request.setData(token);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Boolean> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CHECK_TOKEN_VALID)+"/" + customerId, new TypeReference<Response<Boolean>>() {});
			if (!response.getResponseCode().equals("OK")) {
				return true;
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RemoteException(e.getMessage());
		}
	}

	private RestrictedPaymentMethodCriteriaData getCriteriaData(RestrictedPaymentMethodCriteria criteria ) {



        if(criteria==null) {

                        return null;

}

        RestrictedPaymentMethodCriteriaData data=new RestrictedPaymentMethodCriteriaData();

        data.setAbaRouteNumber(criteria.getAbaRouteNumber());

        data.setAccountNumber(criteria.getAccountNumber());

        data.setBankAccountType(criteria.getBankAccountType()!=null?criteria.getBankAccountType().getName():null);

        data.setCreateDate(criteria.getCreateDate());

        data.setCustomerID(criteria.getCustomerID());

        data.setFirstName(criteria.getFirstName());

        data.setLastName(criteria.getLastName());

        data.setReason(criteria.getReason()!=null?criteria.getReason().getName():null);

        data.setStatus(criteria.getStatus()!=null?criteria.getStatus().getName():null);

        return data;
	}
	@Override
    public List<RestrictedPaymentMethodModel> findRestrictedPaymentMethods(
            RestrictedPaymentMethodCriteria criteria) throws RemoteException {
        Response<List<RestrictedPaymentMethodData>> response = null;
        Request<RestrictedPaymentMethodCriteria> request = new Request<RestrictedPaymentMethodCriteria>();
        List<RestrictedPaymentMethodModel> models = new ArrayList<RestrictedPaymentMethodModel>();
        String inputJson;
            try {
                request.setData(criteria);
                inputJson = buildRequest(request);
                response = postDataTypeMap(inputJson,getFdCommerceEndPoint(FIND_FRAUD_ENTRY_BY_CRITERIA),new TypeReference<Response<List<RestrictedPaymentMethodData>>>() {});
                if(!response.getResponseCode().equals("OK"))
                    throw new FDResourceException(response.getMessage());
                for (RestrictedPaymentMethodData restrictedPaymentMethodData : response.getData()) {
                    RestrictedPaymentMethodModel model = ModelConverter.buildRestrictedPaymentMethodModel(restrictedPaymentMethodData);
                    models.add(model);
                }
            } catch (FDEcommServiceException e) {
                throw new RemoteException(e.getMessage());
            }catch (FDResourceException e) {
                throw new RemoteException(e.getMessage());
            }
            return models;
    }
	@Override
	public ErpMaterialModel findBySapID(String materialNo) throws RemoteException {
		Response<ErpMaterialModel> response = new Response<ErpMaterialModel>();
		String inputJson = null;
		try{
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAP_FIND_BY_SAP_ID)+materialNo,new TypeReference<Response<ErpMaterialModel>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

}