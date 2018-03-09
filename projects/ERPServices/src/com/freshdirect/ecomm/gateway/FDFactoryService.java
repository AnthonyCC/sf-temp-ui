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
import com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome;
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


public class FDFactoryService extends ExtTimeAbstractEcommService implements FDFactoryServiceI {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDFactoryService.class);
	

	private static FDFactoryService INSTANCE;
	
	private static final String ERP_SKUS_SAPID = "erpinfo/skusbysapid";
	private static final String ERP_LOAD_MODIFIED_SKUS = "erpinfo/loadModifiedSkus";
	private static final String FDFACTORY_FDPRODUCTINFO_SKUCODE = "productinfo/productinfobysku";
	private static final String FDFACTORY_FDPRODUCTINFO_SKUCODE_VERSION = "productinfo/productInfobyskuandversion";
	private static final String FDFACTORY_FDPRODUCTINFO_SKUCODES = "productinfo/productsinfobyskus";
	private static final String FDFACTORY_PRODUCTINFO_SKUCODES = "productinfo/productbyskuandversion";
	
	public static FDFactoryServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDFactoryService();

		return INSTANCE;
	}
	
	@Override
	public FDProductInfo getProductInfo(String skuCode) throws FDSkuNotFoundException, RemoteException {

		Response<ErpProductInfoModelData> response = null;
		ErpProductInfoModel model=null;
		FDProductInfo fdProductInfo=null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODE)+"/"+skuCode.trim(),  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(response.getData() == null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			model = ModelConverter.buildProdInfoMod(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		try {
			fdProductInfo= productHelper.getFDProductInfoNew(model);
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new FDResourceException(e);
		}//::FDX::
		return fdProductInfo;
	
	}
	
	
	
	@Override
	public FDProductInfo getProductInfo(String skuCode, int version) throws RemoteException,FDSkuNotFoundException {
		Response<ErpProductInfoModelData> response = null;
		FDProductInfo fdProductInfo;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODE_VERSION)+"/"+skuCode.trim()+"/"+version,  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			ErpProductInfoModel model = ModelConverter.buildProdInfoMod(response.getData());
			fdProductInfo = productHelper.getFDProductInfoNew(model);
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return fdProductInfo;
	}
	
	@Override
	public Collection getProductInfos(String[] skus) throws FDResourceException, RemoteException {
		Response<Collection<ErpProductInfoModelData>> response = null;
		Request<String[]> request = new Request<String[]>();
		Collection<FDProductInfo> fdProductInfos = new ArrayList<FDProductInfo>();
			try {
				request.setData(skus);
				String inputJson;
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODES),new TypeReference<Response<Collection<ErpProductInfoModelData>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
				for (ErpProductInfoModelData fdProductInfoData : response.getData()) {
					ErpProductInfoModel model = ModelConverter.buildProdInfoMod(fdProductInfoData);
					fdProductInfos.add(productHelper.getFDProductInfoNew(model));
				}
				
			} catch (FDEcommServiceException e) {
				
				throw new RemoteException(e.getMessage());
			} 
			return fdProductInfos;
	}
	
	private FDProductHelper productHelper = new FDProductHelper();
	@Override
	public FDProduct getProduct(String sku, int version) throws RemoteException,FDSkuNotFoundException {
		Response<ErpMaterialData> response = null;
		FDProduct fdProduct;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_PRODUCTINFO_SKUCODES)+"/"+sku.trim()+"/"+version,  new TypeReference<Response<ErpMaterialData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}else if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			
			ErpMaterialModel model = ModelConverter.convertErpMaterialDataToModel(response.getData());
			fdProduct = productHelper.getFDProduct(model);
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return fdProduct;
	}
	
	@Override
	public Collection<String> findSkusBySapId(String sapId) throws RemoteException, FDSkuNotFoundException {
		
		Response<Collection<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKUS_SAPID)+"/"+sapId.trim(),  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Set<String> getModifiedSkus(long lastModified) throws RemoteException {
		Response<Set<String> > response = new Response<Set<String> >(); 
		try {
			response = httpGetData(getFdCommerceEndPoint(ERP_LOAD_MODIFIED_SKUS)+"/"+lastModified, Response.class);
		
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		
		}
		return response.getData();
	}

}