package com.freshdirect.ecomm.gateway;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import weblogic.auddi.util.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttribute;
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
import com.freshdirect.ecomm.gateway.CustomResponseDeserializer;
import com.freshdirect.ecommerce.data.attributes.FlatAttributeCollection;
import com.freshdirect.ecommerce.data.cms.CmsCreateFeedParams;
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
import com.freshdirect.ecommerce.data.ecoupon.CartCouponData;
import com.freshdirect.ecommerce.data.ecoupon.CouponOrderData;
import com.freshdirect.ecommerce.data.ecoupon.CouponWalletRequestData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponTransactionModelData;
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
import com.freshdirect.ecommerce.data.enums.ErpAffiliateData;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
import com.freshdirect.ecommerce.data.erp.ewallet.ErpCustEWalletData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpRestrictedAvailabilityData;
import com.freshdirect.ecommerce.data.erp.inventory.RestrictedInfoParam;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicValuePriceData;
import com.freshdirect.ecommerce.data.erp.material.ErpClassData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialInfoModelData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialSalesAreaData;
import com.freshdirect.ecommerce.data.erp.material.ErpPlantMaterialData;
import com.freshdirect.ecommerce.data.erp.material.ErpSalesUnitData;
import com.freshdirect.ecommerce.data.erp.material.OverrideSkuAttrParam;
import com.freshdirect.ecommerce.data.erp.material.SkuPrefixParam;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialPriceData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductPromotionPreviewInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.FDProductPromotionInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoDataWrapper;
import com.freshdirect.ecommerce.data.fdstore.FDGroupData;
import com.freshdirect.ecommerce.data.fdstore.FDProductData;
import com.freshdirect.ecommerce.data.fdstore.FDProductInfoData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.fdstore.GroupScalePricingData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoFDGroupWrapper;
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.ecommerce.data.mail.EmailData;
import com.freshdirect.ecommerce.data.payment.BINData;
import com.freshdirect.ecommerce.data.payment.ErpPaymentMethodData;
import com.freshdirect.ecommerce.data.payment.FDGatewayActivityLogModelData;
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
import com.freshdirect.ecommerce.data.survey.FDSurveyData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.ecommerce.data.zoneInfo.ErpMasterInfoData;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialBatchHistoryModel;
import com.freshdirect.erp.model.ErpMaterialInfoModel;
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
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponHistoryInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDProductHelper;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.SiteAnnouncement;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.logistics.fdstore.ZipCodeAttributes;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;
import com.freshdirect.payment.service.ErpProductInfoModelConvert;
import com.freshdirect.payment.service.ModelConverter;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;
import com.freshdirect.rules.Rule;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.security.ticket.Ticket;
import com.freshdirect.sms.model.st.STSmsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.freshdirect.content.attributes.FlatAttributeCollection;
//import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;


public class ErpInfoService extends ExtTimeAbstractEcommService implements ErpInfoServiceI {

	private final static Category LOGGER = LoggerFactory
			.getInstance(ErpInfoService.class);
	

	private static ErpInfoService INSTANCE;
	
	private static final String ERP_MATERIALINFO_VERSION = "erpinfo/materialbybatch";
	private static final String ERP_MATERIALINFO_SAPID = "erpinfo/materialsbysapid";
	private static final String ERP_MATERIALINFO_SKUCODE = "erpinfo/materialsbyskucode";
	private static final String ERP_MATERIALINFO_DESCRIPTION = "erpinfo/materialsbydescription";
	private static final String ERP_MATERIALINFO_CHARACTERISTIC = "erpinfo/materialsbyclassandcharacteristic";
	private static final String ERP_MATERIALSINFO_CLASS= "erpinfo/materialsbyclass";
	private static final String ERP_PRODUCTINFO_SKUCODE = "erpinfo/productbysku";
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
	private static final String ERP_LOAD_MODIFIED_SKUS = "erpinfo/loadModifiedSkus";
	private static final String ERP_NEW_SKUS_DAYS = "erpinfo/newskucodes";
	private static final String ERP_SKUS_OLDNESS = "erpinfo/skuoldness";
	private static final String ERP_REINTRODUCED_SKUCODES = "erpinfo/reintroducedskucodes";
	private static final String ERP_OUTOFSTOCK_SKUCODES = "erpinfo/outofstockskucodes";
	private static final String ERP_SKUS_BY_DEAL = "erpinfo/skusbydeal";
	private static final String ERP_NEW_SKUS = "erpinfo/newskus";
	private static final String ERP_BACK_IN_STOCK_SKUS = "erpinfo/backinstockskus";
	private static final String ERP_OVERRIDDEN_NEW_SKUS = "erpinfo/overriddennewskus";
	private static final String ERP_OVERRIDDEN_BACK_IN_STOCK_SKUS = "erpinfo/overriddenbackinstockskus";
	private static final String ERP_SKU_AVAILABILITY_HISTORY = "erpinfo/skuavailabilityhistory";
	private static final String ERP_REFRESH_NEW_AND_BACK_VIEWS = "erpinfo/refreshnewandbackviews";
	private static final String ERP_OVERRIDDEN_NEWNESS = "erpinfo/overriddennewness";
	private static final String ERP_OVERRIDDEN_BACK_IN_STOCK = "erpinfo/overriddenbackinstock";
	

	public static ErpInfoServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ErpInfoService();

		return INSTANCE;
	}
	

	@Override
	public Collection findMaterialsByBatch(int batchNum) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_VERSION)+"/"+batchNum, new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsBySapId(String sapId) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_SAPID)+"/"+sapId, new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsBySku(String skuCode) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_SKUCODE)+"/"+skuCode, new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsByDescription(String description) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_DESCRIPTION)+"/"+URLEncoder.encode(description), new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
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
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			className = className.replace('_', '-');
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALSINFO_CLASS)+"/"+className,  new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
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
	//FdFactory
	@Override
	public ErpInventoryModel getInventoryInfo(String materialNo) throws RemoteException {
		
		Response<ErpInventoryData> response = null;
		ErpInventoryModel erpInventoryModel = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_INVENTORY_INFO)+"/"+materialNo,  new TypeReference<Response<ErpInventoryData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpInventoryModel = ModelConverter.convertErpInventoryDataToModel(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpInventoryModel;
	}
	//FdFactory
	@Override
	public Map<String,ErpInventoryModel> loadInventoryInfo(Date date) throws RemoteException {
		
		Response<Map<String,ErpInventoryData>> response = null;
		Map<String,ErpInventoryModel> erpInventoryModelMap = null;
		try {
			long date1 = 0;
			if(date!=null){
			date1 = date.getTime(); 
			}
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_LOAD_INVENTORY_INFO)+"/"+date1,  new TypeReference<Response<Map<String,ErpInventoryData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpInventoryModelMap = ModelConverter.convertErpInventoryDataMapToModelMap(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpInventoryModelMap;
	}
	//FdFactory

	// FdFactory
	@Override
	public Collection<String> findNewSkuCodes(int days) throws RemoteException {
		
		Response<Collection<String>> response = null;
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
	// FdFactory
	@Override
	public Map<String, Integer> getSkusOldness() throws RemoteException {
		
		Response<Map<String, Integer>> response = null;
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
	
	
	// FDFactory
	@Override
	public Collection<String> findReintroducedSkuCodes(int days) throws RemoteException {
		Response<Collection<String>> response = null;
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
	// FDFactory
	@Override
	public Collection<String> findOutOfStockSkuCodes() throws RemoteException {
		Response<Collection<String>> response = null;
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
	
	// FDFactory
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, String> getOverriddenNewness(String sku) throws RemoteException {
		Response<Map<String, String>> response = null;
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
	// FdFactory
	@Override
	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
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
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

}