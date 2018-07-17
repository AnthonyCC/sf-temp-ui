package com.freshdirect.payment.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.content.nutrition.EnumAllergenValue;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumKosherSymbolValue;
import com.freshdirect.content.nutrition.EnumKosherTypeValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.NutritionInfoAttribute;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.content.nutrition.panel.NutritionPanelType;
import com.freshdirect.content.nutrition.panel.NutritionSection;
import com.freshdirect.content.nutrition.panel.NutritionSectionType;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmDepartment;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumComplaintDlvIssueType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpECheckModel;
import com.freshdirect.customer.ErpEbtCardModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPayPalCardModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.pricing.CharacteristicValuePriceData;
import com.freshdirect.ecommerce.data.common.pricing.PricingData;
import com.freshdirect.ecommerce.data.common.pricing.SalesUnitRatioData;
import com.freshdirect.ecommerce.data.common.pricing.ZonePriceInfoListingData;
import com.freshdirect.ecommerce.data.common.pricing.ZonePriceListingData;
import com.freshdirect.ecommerce.data.customer.ErpActivityRecordData;
import com.freshdirect.ecommerce.data.customer.ErpCartonDetailsData;
import com.freshdirect.ecommerce.data.customer.ErpCartonInfoData;
import com.freshdirect.ecommerce.data.customer.ErpComplaintData;
import com.freshdirect.ecommerce.data.customer.ErpSaleData;
import com.freshdirect.ecommerce.data.customer.complaint.ErpComplaintReasonData;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.ecommerce.data.delivery.sms.SmsOrderData;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassData;
import com.freshdirect.ecommerce.data.ecoupon.CartCouponData;
import com.freshdirect.ecommerce.data.ecoupon.CouponCartData;
import com.freshdirect.ecommerce.data.ecoupon.CouponOrderData;
import com.freshdirect.ecommerce.data.ecoupon.CouponWalletRequestData;
import com.freshdirect.ecommerce.data.ecoupon.DiscountData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponDiscountLineModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponTransactionDetailModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponTransactionModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpOrderLineModelData;
import com.freshdirect.ecommerce.data.ecoupon.FDConfigurationData;
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
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryEntryData;
import com.freshdirect.ecommerce.data.erp.inventory.FDAvailabilityData;
import com.freshdirect.ecommerce.data.erp.material.AttributeCollectionData;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicData;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicValueData;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicValuePriceData;
import com.freshdirect.ecommerce.data.erp.material.ErpClassData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialInfoModelData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialSalesAreaData;
import com.freshdirect.ecommerce.data.erp.material.ErpPlantMaterialData;
import com.freshdirect.ecommerce.data.erp.material.ErpSalesUnitData;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialPriceData;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialSalesAreaInfoData;
import com.freshdirect.ecommerce.data.erp.model.ErpPlantMaterialInfoData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductPromotionPreviewInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.FDProductPromotionInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoDataWrapper;
import com.freshdirect.ecommerce.data.fdstore.FDGroupData;
import com.freshdirect.ecommerce.data.fdstore.FDMaterialData;
import com.freshdirect.ecommerce.data.fdstore.FDMaterialSalesAreaData;
import com.freshdirect.ecommerce.data.fdstore.FDNutritionData;
import com.freshdirect.ecommerce.data.fdstore.FDPlantMaterialData;
import com.freshdirect.ecommerce.data.fdstore.FDProductData;
import com.freshdirect.ecommerce.data.fdstore.FDProductInfoData;
import com.freshdirect.ecommerce.data.fdstore.FDSalesUnitData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.fdstore.FDVariationData;
import com.freshdirect.ecommerce.data.fdstore.FDVariationOptionData;
import com.freshdirect.ecommerce.data.fdstore.GroupScalePricingData;
import com.freshdirect.ecommerce.data.fdstore.GrpZonePriceListingData;
import com.freshdirect.ecommerce.data.fdstore.GrpZonePriceListingWrapper;
import com.freshdirect.ecommerce.data.fdstore.GrpZonePriceModelData;
import com.freshdirect.ecommerce.data.fdstore.MaterialSalesAreaData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoData;
import com.freshdirect.ecommerce.data.fdstore.ZonePriceData;
import com.freshdirect.ecommerce.data.fdstore.ZonePriceInfoData;
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.ecommerce.data.mail.EmailAddressData;
import com.freshdirect.ecommerce.data.mail.EmailData;
import com.freshdirect.ecommerce.data.nutrition.ErpNutritionInfoTypeAttrWrapper;
import com.freshdirect.ecommerce.data.nutrition.ErpNutritionInfoTypeData;
import com.freshdirect.ecommerce.data.nutrition.ErpNutritionModelData;
import com.freshdirect.ecommerce.data.nutrition.NutritionInfoAttributeData;
import com.freshdirect.ecommerce.data.nutrition.panel.NutritionPanelData;
import com.freshdirect.ecommerce.data.nutrition.panel.NutritionSectionData;
import com.freshdirect.ecommerce.data.payment.ErpPaymentMethodData;
import com.freshdirect.ecommerce.data.payment.FDGatewayActivityLogModelData;
import com.freshdirect.ecommerce.data.payment.RestrictedPaymentMethodData;
import com.freshdirect.ecommerce.data.referral.CustomerCreditData;
import com.freshdirect.ecommerce.data.referral.FNLNZipData;
import com.freshdirect.ecommerce.data.referral.FailedAttemptData;
import com.freshdirect.ecommerce.data.referral.MailData;
import com.freshdirect.ecommerce.data.referral.UserCreditData;
import com.freshdirect.ecommerce.data.rules.RuleData;
import com.freshdirect.ecommerce.data.sap.ErpTransactionData;
import com.freshdirect.ecommerce.data.security.TicketData;
import com.freshdirect.ecommerce.data.utill.DayOfWeekSetData;
import com.freshdirect.ecommerce.data.zoneInfo.ErpMasterInfoData;
import com.freshdirect.ecommerce.data.zoneInfo.ErpZoneRegionInfoData;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.erp.EnumProductApprovalStatus;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialInfoModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.EnumDayPartValueType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDMaterialSalesArea;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDPlantMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.GrpZonePriceListing;
import com.freshdirect.fdstore.GrpZonePriceModel;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDCompositeAvailability;
import com.freshdirect.fdstore.atp.FDStatusAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionDetailModel;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.framework.mail.FTLEmailI;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.fraud.EnumRestrictedPatternType;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.EnumRestrictionReason;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;
import com.freshdirect.rules.Rule;
import com.freshdirect.security.ticket.Ticket;


public class ModelConverter {

	private static final int MAX_COUNTRY = 5;

	public static List buildBillingCountryInfoList(List data) {
		
		List<BillingCountryInfo> l = new ArrayList<BillingCountryInfo>();
		String activeCountry="";
		String _countryName="";
		String _zipCheck="";
		
		String countryCode="";
		String countryName="";
		String regionCode="";
		String regionName="";
		String zipCheck="";
		String regionCodeExt="";
		List<BillingRegionInfo> regions=new ArrayList<BillingRegionInfo>();
		 for(Object obj :data) {
			 BillingCountryInfoData billData =(BillingCountryInfoData)obj;
			countryCode= billData.getCountryCode();
			countryName = billData.getCountryName();
			regionCode= billData.getRegionCode();
			regionName = billData.getRegionName();
			zipCheck=billData.getCountryZipCheckRegex();
			regionCodeExt=billData.getRegionCodeExternal();
			if(activeCountry=="" ) {
				activeCountry=countryCode;
				_countryName=countryName;
				_zipCheck=zipCheck;
				regions=new ArrayList<BillingRegionInfo>();
				regions.add(new BillingRegionInfo(countryCode,regionCode,regionName,regionCodeExt));
			} else if(activeCountry.equals(countryCode)) {
				regions.add(new BillingRegionInfo(countryCode,regionCode,regionName,regionCodeExt));
			} else {
				Collections.sort(regions,BillingRegionInfo.COMPARE_BY_NAME);
				l.add(new BillingCountryInfo(activeCountry, _countryName,Collections.unmodifiableList(regions),_zipCheck));
				regions=new ArrayList<BillingRegionInfo>();
				regions.add(new BillingRegionInfo(countryCode,regionCode,regionName,regionCodeExt));
				activeCountry=countryCode;
				_countryName=countryName;
				_zipCheck=zipCheck;
			}
			
		}
		if(regions.size()>0) {
			Collections.sort(regions,BillingRegionInfo.COMPARE_BY_NAME);
			l.add(new BillingCountryInfo(activeCountry, _countryName,Collections.unmodifiableList(regions),_zipCheck));
		}
		return l;
	}

	public static List buildErpAffiliateList(List data) {
		
		List l = new ArrayList();
		for (Object  obj:data) {
			ErpAffiliateData erpAffiliate = (ErpAffiliateData)obj;
			
			l.add(
				new ErpAffiliate(
						erpAffiliate.getCode(),
						erpAffiliate.getName(),
						erpAffiliate.getDescription(),
						erpAffiliate.getTaxConditionType(),
						erpAffiliate.getDepositConditionType(),
						erpAffiliate.getMerchants(),
						erpAffiliate.getPaymentechTxDivisions()));
		}


		return l;
	}

	public static List buildDeliveryPassTypeList(List data) {
		List<DeliveryPassType> l = new ArrayList<DeliveryPassType>();
		for (Object obj: data) {
			DeliveryPassTypeData deliveryPassTypeData = (DeliveryPassTypeData)obj;
			l.add(buildDeliveryPassType(deliveryPassTypeData));
		}

		return l;
	}

	private static DeliveryPassType buildDeliveryPassType(
			DeliveryPassTypeData deliveryPassTypeData) {
		
		List<EnumDeliveryType> deliveryTypes = null;
		List<EnumEStoreId> eStoreIds = null;
		if (deliveryPassTypeData.getDeliveryTypes() != null) {
			deliveryTypes = new ArrayList<EnumDeliveryType>();
			for (String deliveryType : deliveryPassTypeData.getDeliveryTypes()) {
				deliveryTypes.add(EnumDeliveryType.getDeliveryType(deliveryType));
			}
		}

		if (deliveryPassTypeData.getEStoreIds() != null) {
			eStoreIds = new ArrayList<EnumEStoreId>();
			for (String eStoreId : deliveryPassTypeData.getEStoreIds()) {
				eStoreIds.add(EnumEStoreId.valueOfContentId(eStoreId));
			}
		}
		return new DeliveryPassType(deliveryPassTypeData.getCode(), deliveryPassTypeData.getName(),
				deliveryPassTypeData.getNoOfDeliveries(), deliveryPassTypeData.getDuration(),
				deliveryPassTypeData.isUnlimited(), deliveryPassTypeData.getProfileValue(),
				deliveryPassTypeData.isAutoRenewDP(), deliveryPassTypeData.isFreeTrialDP(),
				deliveryPassTypeData.isFreeTrialRestricted(), deliveryPassTypeData.getAutoRenewalSKU(),
				deliveryPassTypeData.getEligibleDlvDays(), deliveryTypes, eStoreIds, null);
	}

	public static List buildCrmCaseSubjectList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmCaseSubjectData crmSub = (CrmCaseSubjectData)obj;
		CrmCaseSubject subject = new CrmCaseSubject(crmSub.getQueueCode(),
				crmSub.getCode(),
				crmSub.getName(),
				crmSub.getDescription(),
				crmSub.isObsolete(), 
				crmSub.getPriorityCode(),
				crmSub.isCartonsRequired());
		l.add(subject);
		}
		return l;
	}

	public static List buildEnumFeaturedHeaderTypeList(List data) {
		List<EnumFeaturedHeaderType> l = new ArrayList<EnumFeaturedHeaderType>();
		for (Object obj: data) {
			EnumFeaturedHeaderTypeData deliveryPassTypeData = (EnumFeaturedHeaderTypeData)obj;
			l.add(new EnumFeaturedHeaderType(deliveryPassTypeData.getCode(),
					deliveryPassTypeData.getName(),
					deliveryPassTypeData.getDescription()));
		}

		return l;
	}

	public static Map<ErpCOOLKey, ErpCOOLInfo> buildCoolModel(
			List<CountryOfOriginData> list) {
		Map<ErpCOOLKey, ErpCOOLInfo> erpCOOLInfo=new HashMap<ErpCOOLKey, ErpCOOLInfo>();
		ErpCOOLInfo info=null;
		ErpCOOLKey key=null;
		for(CountryOfOriginData data: list) {
			if(erpCOOLInfo==null)
				erpCOOLInfo=new HashMap<ErpCOOLKey, ErpCOOLInfo>();
			info=new ErpCOOLInfo(data.getSapID(),data.getSapDesc(),getCountryInfo(data.getCountry1(),data.getCountry2(),data.getCountry3(),data.getCountry4(),data.getCountry5()),data.getLastModifiedDate(),data.getPlantId());
			key= new ErpCOOLKey(info.getSapID(),info.getPlantId());
			erpCOOLInfo.put(key, info);
		}
		return erpCOOLInfo;
	}
	private static List<String> getCountryInfo(String country1, String country2, String country3, String country4, String country5) {
		List<String> countryInfo=new ArrayList<String>(3);
		if(!StringUtil.isEmpty(country1))
			countryInfo.add(country1);
		if(!StringUtil.isEmpty(country2))
			countryInfo.add(country2);
		if(!StringUtil.isEmpty(country3))
			countryInfo.add(country3);
		if(!StringUtil.isEmpty(country4))
			countryInfo.add(country4);
		if(!StringUtil.isEmpty(country5))
			countryInfo.add(country5);
		return countryInfo;
	}

	public static List<CountryOfOriginData> buildCoolModelData(
			List<ErpCOOLInfo> erpCOOLInfoList) {
		List<CountryOfOriginData> data = new ArrayList();
		for(ErpCOOLInfo erpCOOLInfo: erpCOOLInfoList){
			int size = erpCOOLInfo.getCountryInfo().size();
			CountryOfOriginData cooData = new CountryOfOriginData(
					erpCOOLInfo.getSapID(),
					erpCOOLInfo.getSapID(),
					erpCOOLInfo.getSapDesc(),
					erpCOOLInfo.getPlantId(),
					erpCOOLInfo.getCountryInfo().get(0).toString(),
					erpCOOLInfo.getCountryInfo().get(1).toString(),
					erpCOOLInfo.getCountryInfo().get(2).toString(),
					erpCOOLInfo.getCountryInfo().get(3).toString(),
					erpCOOLInfo.getCountryInfo().get(4).toString(),
					erpCOOLInfo.getCountryInfo(),
					erpCOOLInfo.getLastModifiedDate());
			data.add(cooData);
			}			

		return data;
	}
	
	public static Request<SmsOrderData> buildSmsOrderDataRequest(String customerId,
			String mobileNumber, String orderId, String eStoreId) {
		Request<SmsOrderData> request = new Request<SmsOrderData>();
		SmsOrderData smsOrderData = new SmsOrderData();
		smsOrderData.setCustomerId(customerId);
		smsOrderData.setMobileNumber(mobileNumber);
		smsOrderData.setOrderId(orderId);
		smsOrderData.seteStoreId(eStoreId);
		request.setData(smsOrderData);
		return request;
	}
	
	public static  Request<RecievedSmsData> buildSmsDataRequest(String mobileNumber,
			String shortCode, String carrierName, Date receivedDate,
			String message, EnumEStoreId eStoreId) {
		Request<RecievedSmsData> recieveSmsData = new Request<RecievedSmsData>();
		RecievedSmsData smsData = new RecievedSmsData();
		smsData.setMobileNumber(mobileNumber);
		smsData.setShortCode(shortCode);
		smsData.setCarrierName(carrierName);
		smsData.setReceivedDate(receivedDate);
		smsData.setMessage(message);
		smsData.seteStoreId(eStoreId.getContentId());
		recieveSmsData.setData(smsData);
		return recieveSmsData;
	}
	
	public static FDRecommendationEventData convertFDRecommendationEvent(FDRecommendationEvent event) {
		if(event==null)
			return null;
		FDRecommendationEventData fdRecommendationEventData = new FDRecommendationEventData();
		fdRecommendationEventData.setContentId(event.getContentId());
		fdRecommendationEventData.seteStoreId(event.getEStoreId());
		fdRecommendationEventData.setTimestamp(event.getTimeStamp());
		fdRecommendationEventData.setVariantId(event.getVariantId());
		return fdRecommendationEventData;
	}

	public static ZoneInfo buildZoneInfo(ZoneInfoData key) {
		return new ZoneInfo(key.getZoneId(),key.getSalesOrg(),key.getDistributionChanel());
	}

	public static List<FDProductPromotionInfo> buildFDProductPromotionInfo(
			List<FDProductPromotionInfoData> value) {
		 List<FDProductPromotionInfo> data = new ArrayList<FDProductPromotionInfo>();
		 for(FDProductPromotionInfoData obj:value){
			 SalesAreaInfo sAinfo = buildSalesAreaInfo(obj);
			 FDProductPromotionInfo model = new FDProductPromotionInfo();
			 model.setId(obj.getId());
			 model.setVersion(obj.getVersion());
			 model.setZoneId(obj.getZoneId());
			 model.setSkuCode(obj.getSkuCode());
			 model.setMatNumber(obj.getMatNumber());
			 model.setPriority(obj.getPriority());
			 model.setFeatured(obj.isFeatured());
			 model.setFeaturedHeader(obj.getFeaturedHeader());
			 model.setType(obj.getType());
			 model.setErpCategory(obj.getErpCategory());
			 model.setErpCatPosition(obj.getErpCatPosition());
			 model.setErpDeptId(obj.getErpDeptId());
			 model.setErpPromtoionId(obj.getErpPromtoionId());
			 
			 model.setSalesArea(sAinfo);
			 
			 data.add(model);
		 }
		return data;
	}

	private static SalesAreaInfo buildSalesAreaInfo(
			FDProductPromotionInfoData obj) {
		SalesAreaInfo sAinfo = new SalesAreaInfo(obj.getSalesArea().getSalesOrg(),obj.getSalesArea().getDistChannel());
		return sAinfo;
	}

	public static ErpProductPromotionPreviewInfo buildErpProductPromotionPreviewInfo(
			ErpProductPromotionPreviewInfoData data) {
    	ErpProductPromotionPreviewInfo response = new ErpProductPromotionPreviewInfo();
    	Map<String,ErpProductInfoModel> prodInfoMod = new HashMap<String,ErpProductInfoModel>();
    	for(String key: data.getErpProductInfoMap().keySet()){
    		prodInfoMod.put(key, buildProdInfoMod(data.getErpProductInfoMap().get(key)));}
    	response.setErpProductInfoMap(prodInfoMod);
    	response.setProductPromotionInfoMap(buildZoneInfo(data.getProductPromotionInfoMap()));
		return response;
	}
	public static ErpProductInfoModel buildProdInfoMod(
			ErpProductInfoModelData erpProductInfoModelData) {
		if(erpProductInfoModelData==null)
			return null;
		ErpProductInfoModel responseData = new ErpProductInfoModel(
				erpProductInfoModelData.getSkuCode(), 
				erpProductInfoModelData.getVersion(), 
				erpProductInfoModelData.getMaterialNumbers().clone(), 
				erpProductInfoModelData.getDescription(), 
				buildErpMaterialPrice(erpProductInfoModelData.getMaterialPrices()), 
				erpProductInfoModelData.getUpc(), 
				buildPlantMAterialInfo(erpProductInfoModelData.getMaterialPlants()),
				buildMaterialSaleInfo(erpProductInfoModelData.getMaterialSalesAreas()), 
				EnumAlcoholicContent.getAlcoholicContent(String.valueOf(erpProductInfoModelData.isAlcohol())));

		return responseData;
	}

	
	public static ErpMaterialSalesAreaInfo[] buildMaterialSaleInfo(
			ErpMaterialSalesAreaInfoData[] materialSalesAreas) {
		if(materialSalesAreas==null||materialSalesAreas.length==0)
			return null;
		ErpMaterialSalesAreaInfo[] data = new ErpMaterialSalesAreaInfo[materialSalesAreas.length];
		for(int i=0;i<materialSalesAreas.length;i++){
			SalesAreaInfo sa = new SalesAreaInfo(materialSalesAreas[i].getSalesAreaInfo().getSalesOrg(),materialSalesAreas[i].getSalesAreaInfo().getDistChannel());

			ErpMaterialSalesAreaInfo matData = new ErpMaterialSalesAreaInfo(sa, 
					materialSalesAreas[i].getUnavailabilityStatus(), 
					materialSalesAreas[i].getUnavailabilityDate(), 
					materialSalesAreas[i].getUnavailabilityReason(), 
					materialSalesAreas[i].getDayPartType(), 
					materialSalesAreas[i].getPickingPlantId());
			data[i]=matData;
		}
			
		return data;
	}

	public static ErpPlantMaterialInfo[] buildPlantMAterialInfo(
			ErpPlantMaterialInfoData[] materialPlants) {
		if(materialPlants==null||materialPlants.length==0)
			return null;
		ErpPlantMaterialInfo[] data = new ErpPlantMaterialInfo[materialPlants.length];
		DayOfWeekSet  ds=null;
		for(int i=0;i<materialPlants.length;i++){
			if(materialPlants[i].getBlockedDays().getDaysOfWeek()==null||!materialPlants[i].getBlockedDays().getDaysOfWeek().isEmpty()){
				 SortedSet set =materialPlants[i].getBlockedDays().getDaysOfWeek();
				 int[] days = new int[set.size()];
				 int j = 0;
				for(Iterator it = set.iterator(); it.hasNext();){int elem = Integer.parseInt((String)it.next());days[j]=elem;j++;}
			 ds = new DayOfWeekSet(days);
			}
			ErpPlantMaterialInfo matData = new ErpPlantMaterialInfo(
					materialPlants[i].isKosherProduction(), 
					materialPlants[i].isPlatter(), 
					ds, 
					EnumATPRule.getEnum(materialPlants[i].getAtpRule()), 
					materialPlants[i].getRating(), 
					materialPlants[i].getFreshness(),
					materialPlants[i].getSustainabilityRating(),
					materialPlants[i].getPlantId(),
					materialPlants[i].isLimitedQuantity());
			data[i]=matData;
		}
			
		return data;
	}


	public static ErpMaterialPrice[] buildErpMaterialPrice(
			ErpMaterialPriceData[] materialPrices) {
		if(materialPrices==null||materialPrices.length==0)
				return null;
			ErpMaterialPrice[] data = new ErpMaterialPrice[materialPrices.length];
			for(int i=0;i<materialPrices.length;i++){
				
				ErpMaterialPrice matData = new ErpMaterialPrice(
						materialPrices[i].getPrice(), 
						materialPrices[i].getPricingUnit(), 
						materialPrices[i].getPromoPrice(), 
						materialPrices[i].getScaleUnit(), 
						materialPrices[i].getScaleQuantity(), 
						materialPrices[i].getSapZoneId(),
						materialPrices[i].getSalesOrg(),
						materialPrices[i].getDistChannel());
				data[i]=matData;
			}
				
			return data;
		}

	public static Map<ZoneInfo, List<FDProductPromotionInfo>> buildZoneInfo(
			 List<ZoneInfoDataWrapper> zoneInfoData) {
		Map<ZoneInfo, List<FDProductPromotionInfo>> data = new HashMap();
		for(ZoneInfoDataWrapper zoneInfoWrap: zoneInfoData){
			ZoneInfo zoneInfo = new ZoneInfo(zoneInfoWrap.getKey().getZoneId(),zoneInfoWrap.getKey().getSalesOrg(),zoneInfoWrap.getKey().getDistributionChanel());
			List<FDProductPromotionInfo> prodPromoInofList = new ArrayList();
			prodPromoInofList = buildFDProductPromotionInfo(zoneInfoWrap.getValue());
			data.put(zoneInfo, prodPromoInofList);
		}
  	return data;
	}

	public static GroupScalePricing buildGroupScalePricing(
			GroupScalePricingData data) {
		GroupScalePricing fDGroup = new GroupScalePricing(data.getGroupId(), data.getVersion(), 
				data.getLongDesc(), data.getShortDesc(), data.isActive(),
				buildGrpZonePrice(data.getGrpZonePriceList()), data.getMatList(), data.getSkuList()); 
			return fDGroup;
			}

	public static GrpZonePriceListing buildGrpZonePrice(
			GrpZonePriceListingData grpZonePriceList) {
		GrpZonePriceListing listing = new GrpZonePriceListing();
		for(GrpZonePriceListingWrapper obj: grpZonePriceList.getGrpZonePriceListingWrapper()){
			listing.addGrpZonePrice(buildZoneInfo(obj.getZoneInfoData()), buildGrpZonePriceModel(obj.getGrpZonePriceModelData()));
			
		}
		return listing;
	}

	public static GrpZonePriceModel buildGrpZonePriceModel(
			GrpZonePriceModelData grpZonePriceModelData) {
		 MaterialPrice[] matPrices = new MaterialPrice[grpZonePriceModelData.getMaterialPrices().length];
		 for(int i=0;i<grpZonePriceModelData.getMaterialPrices().length;i++){
			 matPrices[i] = buildMaterialPrice(grpZonePriceModelData.getMaterialPrices()[i]);
		 }
		GrpZonePriceModel zonePrisingModel = new GrpZonePriceModel(buildZoneInfo(grpZonePriceModelData.getZoneInfo()), matPrices, grpZonePriceModelData.isGrpScalePresent()); 
		return zonePrisingModel;
	}

	public static MaterialPrice buildMaterialPrice(
			com.freshdirect.ecommerce.data.common.pricing.MaterialPrice materialPrice) {
		
		MaterialPrice matPrice = new MaterialPrice(materialPrice.getPrice(), materialPrice.getPricingUnit(), materialPrice.getScaleLowerBound(), 
				materialPrice.getScaleUpperBound(), materialPrice.getScaleUnit(), materialPrice.getPromoPrice());
		
		return matPrice;
	}

	public static FDGroup buildFDGroup(FDGroupData fdGroupData) {
		FDGroup fDGroup = new FDGroup(fdGroupData.getGroupId(),fdGroupData.getVersion());
		fDGroup.setSkipProductPriceValidation(fdGroupData.isSkipProductPriceValidation());
		return fDGroup;
	}

	public static SalesAreaInfo buildSalesAreaInfo(
			SalesAreaInfoData salesAreaInfoData) {
		SalesAreaInfo salesAreaInfo = new SalesAreaInfo(salesAreaInfoData.getSalesOrg(),salesAreaInfoData.getDistChannel());
		return salesAreaInfo;
	}

	public static FDSkuData buildFDSkyData(FDSku sku) {
		FDSkuData skuData = new FDSkuData();
		skuData.setSkuCode(sku.getSkuCode());
		skuData.setVersion(sku.getVersion());
		return skuData;
	}

	public static FDSku buildFDSky(FDSkuData skuData) {
		FDSku sku = new FDSku(skuData.getSkuCode(),skuData.getVersion());
		return sku;
	}

	public static Map<String, Rule> buildRuleMap(Map<String, RuleData> rules) {
		Map<String, Rule> ruleMap = new TreeMap<String, Rule>();
		for (String key: rules.keySet()){
			RuleData ruleData = rules.get(key);
    		Rule rule = new Rule();
    		rule.setId(ruleData.getId());
    		rule.setName(ruleData.getName());
    		rule.setStartDate(ruleData.getStartDate());
    		rule.setEndDate(ruleData.getEndDate());
    		rule.setOutcomeStr(ruleData.getOutcomeStr());
    		rule.setSubsystem(ruleData.getSubsystem());
    		rule.setPriority(ruleData.getPriority());
    		rule.setConditionStr(ruleData.getConditionStr());
    		ruleMap.put(key, rule);
		}
		return ruleMap;
	}

	public static RuleData buildRuleData(Rule ruleObj) {
    		RuleData ruleData = new RuleData();
    		ruleData.setId(ruleObj.getId());
    		ruleData.setName(ruleObj.getName());
    		ruleData.setStartDate(ruleObj.getStartDate());
    		ruleData.setEndDate(ruleObj.getEndDate());
    		ruleData.setOutcomeStr(ruleObj.getOutcomeStr());
    		ruleData.setSubsystem(ruleObj.getSubsystem());
    		ruleData.setPriority(ruleObj.getPriority());
    		ruleData.setConditionStr(ruleObj.getConditionStr());
		return ruleData;
	}
	
	
	public static FDGatewayActivityLogModelData convertFDGatewayActivityLogModel(FDGatewayActivityLogModel activityLogModel) {
		if(activityLogModel==null)
			return null;
		
		FDGatewayActivityLogModelData fdGatewayActivityLogModelData = new FDGatewayActivityLogModelData();
		fdGatewayActivityLogModelData.setAccountNumLast4(activityLogModel.getAccountNumLast4());
		fdGatewayActivityLogModelData.setAddressLine1(activityLogModel.getAddressLine1());
		fdGatewayActivityLogModelData.setAddressLine2(activityLogModel.getAddressLine2());
		fdGatewayActivityLogModelData.setAmount(activityLogModel.getAmount());
		fdGatewayActivityLogModelData.setApproved(activityLogModel.isApproved());
		fdGatewayActivityLogModelData.setAuthCode(activityLogModel.getAuthCode());
		fdGatewayActivityLogModelData.setAVSMatch(activityLogModel.isAVSMatch());
		fdGatewayActivityLogModelData.setAvsResponse(activityLogModel.getAvsResponse());
		if(activityLogModel.getBankAccountType() != null)
		fdGatewayActivityLogModelData.setBankAccountType(activityLogModel.getBankAccountType().name());
		if(activityLogModel.getCardType() != null)
		fdGatewayActivityLogModelData.setCardType(activityLogModel.getCardType().name());
		fdGatewayActivityLogModelData.setCity(activityLogModel.getCity());
		fdGatewayActivityLogModelData.setCountryCode(activityLogModel.getCountryCode());
		fdGatewayActivityLogModelData.setCustomerId(activityLogModel.getCustomerId());
		fdGatewayActivityLogModelData.setCustomerName(activityLogModel.getCustomerName());
		fdGatewayActivityLogModelData.setCVVMatch(activityLogModel.isCVVMatch());
		fdGatewayActivityLogModelData.setCvvResponse(activityLogModel.getCvvResponse());
		fdGatewayActivityLogModelData.setDeclined(activityLogModel.isDeclined());
		fdGatewayActivityLogModelData.setDeviceId(activityLogModel.getDeviceId());
		if(activityLogModel.getEStoreId() != null)
		fdGatewayActivityLogModelData.seteStoreId(activityLogModel.getEStoreId().name());
		fdGatewayActivityLogModelData.seteWalletId(activityLogModel.geteWalletId());
		fdGatewayActivityLogModelData.seteWalletTxId(activityLogModel.geteWalletTxId());
		fdGatewayActivityLogModelData.setExpirationDate(activityLogModel.getExpirationDate());
		fdGatewayActivityLogModelData.setGatewayOrderID(activityLogModel.getGatewayOrderID());
		if(activityLogModel.getGatewayType() != null)
		fdGatewayActivityLogModelData.setGatewayType(activityLogModel.getGatewayType().getName());
		fdGatewayActivityLogModelData.setMerchant(activityLogModel.getMerchant());
		if(activityLogModel.getPaymentType() != null)
		fdGatewayActivityLogModelData.setPaymentType(activityLogModel.getPaymentType().name());
		fdGatewayActivityLogModelData.setProcessingError(activityLogModel.isProcessingError());
		fdGatewayActivityLogModelData.setProfileId(activityLogModel.getProfileId());
		fdGatewayActivityLogModelData.setRequestProcessed(activityLogModel.isRequestProcessed());
		fdGatewayActivityLogModelData.setResponseCode(activityLogModel.getResponseCode());
		fdGatewayActivityLogModelData.setResponseCodeAlt(activityLogModel.getResponseCodeAlt());
		fdGatewayActivityLogModelData.setState(activityLogModel.getState());
		fdGatewayActivityLogModelData.setStatusCode(activityLogModel.getStatusCode());
		fdGatewayActivityLogModelData.setStatusMsg(activityLogModel.getStatusMsg());
		if(activityLogModel.getTransactionType() != null)
		fdGatewayActivityLogModelData.setTransactionType(activityLogModel.getTransactionType().name());
		fdGatewayActivityLogModelData.setTxRefIdx(activityLogModel.getTxRefIdx());
		fdGatewayActivityLogModelData.setTxRefNum(activityLogModel.getTxRefNum());
		
		return fdGatewayActivityLogModelData;
	}

	public static ErpActivityRecordData buildErpActivityRecordData(ErpActivityRecord template) {
		ErpActivityRecordData erpActivityRecordData = new ErpActivityRecordData();
		erpActivityRecordData.setCustomerId(template.getCustomerId());
		erpActivityRecordData.setCustFirstName(template.getCustFirstName());
		erpActivityRecordData.setCustLastName(template.getCustLastName());
		erpActivityRecordData.setSource(template.getSource() != null? template.getSource().getCode() : null);
		erpActivityRecordData.setInitiator(template.getInitiator());
		erpActivityRecordData.setType(template.getActivityType() != null ?template.getActivityType().getCode() : null);
		erpActivityRecordData.setNote(template.getNote()== null ?null : template.getNote());
		erpActivityRecordData.setDate(template.getDate()!= null ?template.getDate() : null);
		erpActivityRecordData.setDeliveryPassId(template.getDeliveryPassId());
		erpActivityRecordData.setChangeOrderId(template.getChangeOrderId());
		erpActivityRecordData.setReason(template.getReason());
		erpActivityRecordData.setFromDate(template.getFromDate());
		erpActivityRecordData.setFromDateStr(template.getFromDateStr());
		erpActivityRecordData.setToDate(template.getToDate());
		erpActivityRecordData.setToDateStr(template.getToDateStr());
		return erpActivityRecordData;
		
	}

	public static Map<String, List> buildFilterListResponse(Map<String, List<String>> data) {
		Map<String, List> filterListresponse = new HashMap<String, List>();
		for (String key : data.keySet()) {
			List<String> response = data.get(key);
			if(key.equals("activity_id")){
				List<EnumAccountActivityType> activityType = new ArrayList<EnumAccountActivityType>();
				for (String activityId : response) {
					activityType.add(EnumAccountActivityType.getActivityType(activityId));
				}
				filterListresponse.put(key, activityType);
			}
			else if(key.equals("source")){
				List<EnumTransactionSource> sourceList = new ArrayList<EnumTransactionSource>();
				for (String source : response) {
					sourceList.add(EnumTransactionSource.getTransactionSource(source));
				}
				filterListresponse.put(key, sourceList);
			}
			else{
				filterListresponse.put(key, response);
			}
		}
		return filterListresponse;
	}

	public static Collection<ErpActivityRecord> buildErpActivityRecord(Collection<ErpActivityRecordData> data) {
		List<ErpActivityRecord> activityRecordList = new ArrayList<ErpActivityRecord>();
		for (ErpActivityRecordData erpActivityRecordData : data) {
			ErpActivityRecord erpActivityRecord = new ErpActivityRecord();
			erpActivityRecord.setCustomerId(erpActivityRecordData.getCustomerId());
			erpActivityRecord.setCustFirstName(erpActivityRecordData.getCustFirstName());
			erpActivityRecord.setCustLastName(erpActivityRecordData.getCustLastName());
			erpActivityRecord.setSource(erpActivityRecordData.getSource() != null? EnumTransactionSource.getTransactionSource(erpActivityRecordData.getSource()) : null);
			erpActivityRecord.setInitiator(erpActivityRecordData.getInitiator());
			erpActivityRecord.setActivityType(erpActivityRecordData.getType() != null ?EnumAccountActivityType.getActivityType(erpActivityRecordData.getType()) : null);
			erpActivityRecord.setNote(erpActivityRecordData.getNote());
			erpActivityRecord.setDate(erpActivityRecordData.getDate());
			erpActivityRecord.setDeliveryPassId(erpActivityRecordData.getDeliveryPassId());
			erpActivityRecord.setChangeOrderId(erpActivityRecordData.getChangeOrderId());
			erpActivityRecord.setReason(erpActivityRecordData.getReason());
			erpActivityRecord.setFromDate(erpActivityRecordData.getFromDate());
			erpActivityRecord.setFromDateStr(erpActivityRecordData.getFromDateStr());
			erpActivityRecord.setToDate(erpActivityRecordData.getToDate());
			erpActivityRecord.setToDateStr(erpActivityRecordData.getToDateStr());
			activityRecordList.add(erpActivityRecord);
		}
			return activityRecordList;
			
		}

	public static TicketData buildTicketData(Ticket ticket) {
		TicketData ticketData = new TicketData();
		ticketData.setExpiration(ticket.getExpiration());
		ticketData.setKey(ticket.getKey());
		ticketData.setOwner(ticket.getOwner());
		ticketData.setPurpose(ticket.getPurpose());
		ticketData.setUsed(ticket.isUsed());
		return ticketData;
	}
	
	public static Ticket buildTicket(TicketData ticketData) {
		Ticket ticketRequest= new Ticket(ticketData.getKey(), ticketData.getOwner(), 
				ticketData.getPurpose(), ticketData.getExpiration(), ticketData.isUsed());
		return ticketRequest;
		
	}

	public static ErpMaterialData convertErpMaterialModelToData(
			ErpMaterialModel model) {
		ErpMaterialData data = new ErpMaterialData();
		List<ErpClassData> erpClassDatas = createErpClassDataList(model.getClasses());
		List<ErpMaterialPriceData> erpMaterialPriceDatas = createErpMaterialPriceDataList(model.getPrices());
		List<ErpSalesUnitData> salesUnitDatas = createErpSalesUnitDataList(model.getSalesUnits());
		List<ErpSalesUnitData> displaySalesUnitDatas = createErpSalesUnitDataList(model.getDisplaySalesUnits());
		List<ErpPlantMaterialData> erpPlantMaterialDatas = createErpPlantMaterialDataList(model.getMaterialPlants());
		List<ErpMaterialSalesAreaData> erpMaterialSalesAreaDatas = createErpMaterialSalesAreaDataList(model.getMaterialSalesAreas());
		// http://jira.freshdirect.com/browse/SF17-72
		//data.setId(model.getId());
		data.setBaseUnit(model.getBaseUnit());
		data.setUPC(model.getUPC());
		data.setDescription(model.getDescription());
		data.setQuantityCharacteristic(model.getQuantityCharacteristic());
		data.setSalesUnitCharacteristic(model.getSalesUnitCharacteristic());
		data.setMaterialGroup(model.getMaterialGroup());
		data.setSapId(model.getSapId());
		data.setSalesUnits(getErpSaleUnitModelList(model.getSalesUnits()));
		data.setAlcoholicContent(model.getAlcoholicContent() != null ? model.getAlcoholicContent().getCode() : null);
		data.setTaxable(model.isTaxable());
		data.setTaxCode(model.getTaxCode());
		data.setDisplaySalesUnits(getErpSaleUnitModelList(model.getDisplaySalesUnits()));
		data.setSkuCode(model.getSkuCode());
		data.setDaysFresh(model.getDaysFresh());
		data.setMaterialType( model.getMaterialType());
		data.setApprovalStatus(model.getApprovalStatus() != null ? model.getApprovalStatus().getStatusCode() : null);

		data.setPrices(erpMaterialPriceDatas);
		data.setClasses(erpClassDatas);
		data.setDisplaySalesUnits(displaySalesUnitDatas);
		data.setMaterialPlants(erpPlantMaterialDatas);
		data.setMaterialSalesAreas(erpMaterialSalesAreaDatas);
		data.setSalesUnits(salesUnitDatas);
		return data;
	}
	
	public static ErpMaterialModel convertErpMaterialDataToModel(
			ErpMaterialData data) {
		
		List<ErpClassModel> erpClass = createErpClassList(data.getClasses());
		List<ErpMaterialPriceModel> erpMaterialPrice = createErpMaterialPriceList(data.getPrices());
		List<ErpSalesUnitModel> salesUnits = createErpSalesUnitList(data.getSalesUnits());
		List<ErpSalesUnitModel> displaySalesUnits = createErpSalesUnitList(data.getDisplaySalesUnits());
		List<ErpPlantMaterialModel> erpPlantMaterial = createErpPlantMaterialList(data.getMaterialPlants());
		List<ErpMaterialSalesAreaModel> erpMaterialSalesArea = createErpMaterialSalesAreaList(data.getMaterialSalesAreas());
	
		
		ErpMaterialModel model = new ErpMaterialModel(data.getSapId(), 
				data.getBaseUnit(), 
				data.getDescription(), 
				data.getUPC(),
				data.getQuantityCharacteristic(),
				data.getSalesUnitCharacteristic(), 
				EnumAlcoholicContent.getAlcoholicContent(data.getAlcoholicContent()), 
				data.isTaxable(), 
				data.getTaxCode(), 
				data.getSkuCode(),
				data.getDaysFresh(),EnumProductApprovalStatus.getApprovalStatus(data.getApprovalStatus()), 
				data.getMaterialType(), erpMaterialPrice,
				salesUnits, erpClass, displaySalesUnits,
				erpPlantMaterial,erpMaterialSalesArea, data.getMaterialGroup());
		
		VersionedPrimaryKey primaryKey = new VersionedPrimaryKey(data.getVersionedPrimaryKeyData().getMaterialId(), data.getVersionedPrimaryKeyData().getVersionId());
		model.setPK(primaryKey);
		return model;
	}
	private static List<ErpMaterialSalesAreaModel> createErpMaterialSalesAreaList(
			List<ErpMaterialSalesAreaData> materialSalesAreas) {
		List<ErpMaterialSalesAreaModel> list = new ArrayList<ErpMaterialSalesAreaModel> ();
		for(ErpMaterialSalesAreaData data: materialSalesAreas){
			ErpMaterialSalesAreaModel model = new ErpMaterialSalesAreaModel(data.getSalesOrg(),
					data.getDistChannel(),data.getUnavailabilityStatus(),data.getUnavailabilityDate(),data.getUnavailabilityReason(),
					data.getSkuCode(),data.getDayPartSelling(),data.getPickingPlantId());
			list.add(model);
		}
		return list;
	}

	private static List<ErpPlantMaterialModel> createErpPlantMaterialList(
			List<ErpPlantMaterialData> materialPlants) {
		List<ErpPlantMaterialModel>  erpPlantMaterialList = new ArrayList<ErpPlantMaterialModel>();
		for (ErpPlantMaterialData erpPlantMaterialModel : materialPlants) {
			ErpPlantMaterialModel model = new ErpPlantMaterialModel();
			model.setAtpRule(EnumATPRule.getEnum(erpPlantMaterialModel.getAtpRule()));
			if(erpPlantMaterialModel.getBlockedDays()!=null){
			int[] weekDays =new int[erpPlantMaterialModel.getBlockedDays().getDaysOfWeek().toArray().length];
			Object[] obj = erpPlantMaterialModel.getBlockedDays().getDaysOfWeek().toArray();
		    for (int i = 0; i < obj.length; i++){
		    	int tempval =(Integer)obj[i];
		    	weekDays[i]=tempval;
		    }
			model.setBlockedDays(new DayOfWeekSet(weekDays));
			}
			erpPlantMaterialList.add(model);
		}
		return erpPlantMaterialList;
	}

	private static List createErpSalesUnitList(List<ErpSalesUnitData> salesUnits) {
		List<ErpSalesUnitModel> erpSaleUnits = new ArrayList<ErpSalesUnitModel>();
		for (ErpSalesUnitData salesUnit : salesUnits) {
			ErpSalesUnitModel model = new	ErpSalesUnitModel();
			model.setAlternativeUnit(salesUnit.getAlternativeUnit());
			model.setBaseUnit(salesUnit.getBaseUnit());
			model.setDenominator(salesUnit.getDenominator());
			model.setDescription(salesUnit.getDescription());
			model.setDisplayInd(salesUnit.isDisplayInd());
			// http://jira.freshdirect.com/browse/SF17-72
			//model.setId(salesUnit.getId());
			model.setNumerator(salesUnit.getNumerator());
			model.setUnitPriceDenominator(salesUnit.getUnitPriceDenominator());
			model.setUnitPriceDescription(salesUnit.getUnitPriceDescription());
			model.setUnitPriceNumerator(salesUnit.getUnitPriceNumerator());
			erpSaleUnits.add(model);
			}
		return erpSaleUnits;
		}

	
	public static List<ErpMaterialPriceModel> createErpMaterialPriceList(List<ErpMaterialPriceData> erpMaterialPricedata) {
		List<ErpMaterialPriceModel>  erpPlantMaterial = new ArrayList<ErpMaterialPriceModel>();
		for (ErpMaterialPriceData data: erpMaterialPricedata) {
		
			ErpMaterialPriceModel model = new ErpMaterialPriceModel
					(data.getSapId(), data.getPrice(), data.getPricingUnit(),
							 data.getScaleQuantity(),data.getScaleUnit(), data.getSapZoneId(),
							data.getPromoPrice(), data.getSalesOrg(),data.getDistChannel())
					;
			erpPlantMaterial.add(model);
		}
		return erpPlantMaterial;
	}

	private static List<ErpClassModel> createErpClassList(
			List<ErpClassData> classes) {
		List<ErpClassModel> classlist = new ArrayList<ErpClassModel>();
		for(ErpClassData data : classes){
			classlist.add(new ErpClassModel(data.getSapId(),createErpCharacteristicList(data.getCharacteristics())));
		}
		return classlist;
	}

	private static List<ErpCharacteristicModel> createErpCharacteristicList(List<ErpCharacteristicData> data) {
		List<ErpCharacteristicModel>  erpCharacteristicModel = new ArrayList<ErpCharacteristicModel>();
		for (ErpCharacteristicData erpClassdata : data) {
			erpCharacteristicModel.add(new ErpCharacteristicModel(erpClassdata.getName(),createErpCharacteristicValueList(erpClassdata.getCharacteristicValues())));
		}
		return erpCharacteristicModel;
	}
	
	public static List<ErpCharacteristicValueModel> createErpCharacteristicValueList(List<ErpCharacteristicValueData> list) {
		List<ErpCharacteristicValueModel>  erpCharacteristicValueModel = new ArrayList<ErpCharacteristicValueModel>();
		for (ErpCharacteristicValueData erpCharacteristicValueData : list) {
			ErpCharacteristicValueModel model = new ErpCharacteristicValueModel(erpCharacteristicValueData.getName(),erpCharacteristicValueData.getDescription());
			model.setId(erpCharacteristicValueData.getId());
			model.setPK(new PrimaryKey(erpCharacteristicValueData.getId()));
			erpCharacteristicValueModel.add(model);
		}
		return erpCharacteristicValueModel;
	}
	
	private static List getErpSaleUnitModelList(
			List<ErpSalesUnitModel> salesUnits) {
		List<ErpSalesUnitData> erpSaleUnits = new ArrayList<ErpSalesUnitData>();
		for (ErpSalesUnitModel salesUnit : salesUnits) {
				ErpSalesUnitData model = convertErpSaleUnitModelToData(salesUnit);
				erpSaleUnits.add(model);
			}
		return erpSaleUnits;
		}

	/*private static ErpSalesUnitData convertErpSaleUnitModelToData(
			ErpSalesUnitModel salesUnit) {
		ErpSalesUnitData erpSalesUnitData = new ErpSalesUnitData();
		erpSalesUnitData.setId(salesUnit.getId());
		erpSalesUnitData.setAlternativeUnit(salesUnit.getAlternativeUnit());
		erpSalesUnitData.setBaseUnit(salesUnit.getBaseUnit());
		erpSalesUnitData.setNumerator(salesUnit.getNumerator());
		erpSalesUnitData.setDenominator(salesUnit.getDenominator());
		erpSalesUnitData.setDescription(salesUnit.getDescription());
		erpSalesUnitData.setUnitPriceNumerator(salesUnit.getUnitPriceNumerator());
		erpSalesUnitData.setUnitPriceDenominator(salesUnit.getUnitPriceDenominator());
		erpSalesUnitData.setUnitPriceUOM(salesUnit.getUnitPriceUOM());
		erpSalesUnitData.setUnitPriceDescription(salesUnit.getUnitPriceDescription());
		erpSalesUnitData.setDisplayInd(salesUnit.isDisplayInd());
		return erpSalesUnitData;
	}*/
	
	public static List<ErpMaterialSalesAreaData> createErpMaterialSalesAreaDataList(List<ErpMaterialSalesAreaModel> list) {
		List<ErpMaterialSalesAreaData> erpMaterialSalesAreaDatas = new ArrayList<ErpMaterialSalesAreaData>();
		for (ErpMaterialSalesAreaModel erpMaterialSalesAreaModel : list) {
			ErpMaterialSalesAreaData data = convertErpMaterialSalesAreaModelToData(erpMaterialSalesAreaModel);
			erpMaterialSalesAreaDatas.add(data);
		}
		return erpMaterialSalesAreaDatas;
	}

	public static ErpMaterialSalesAreaData convertErpMaterialSalesAreaModelToData(
			ErpMaterialSalesAreaModel erpMaterialSalesAreaModel) {
		ErpMaterialSalesAreaData data = new  ErpMaterialSalesAreaData();
		data.setDayPartSelling(erpMaterialSalesAreaModel.getDayPartSelling());
		data.setDistChannel(erpMaterialSalesAreaModel.getDistChannel());
		// http://jira.freshdirect.com/browse/SF17-72
		//data.setId(erpMaterialSalesAreaModel.getId());
		data.setPickingPlantId(erpMaterialSalesAreaModel.getPickingPlantId());
		data.setSalesOrg(erpMaterialSalesAreaModel.getSalesOrg());
		data.setSkuCode(erpMaterialSalesAreaModel.getSkuCode());
		Date str1 = erpMaterialSalesAreaModel.getUnavailabilityDate();
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
		String str = dateFormat.format(str1);
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data.setUnavailabilityDate(date);
		
		data.setUnavailabilityReason(erpMaterialSalesAreaModel.getUnavailabilityReason());
		data.setUnavailabilityStatus(erpMaterialSalesAreaModel.getUnavailabilityStatus());
		return data;
	}
	

	public static List<ErpPlantMaterialData> createErpPlantMaterialDataList(List<ErpPlantMaterialModel> list) {
		List<ErpPlantMaterialData>  erpPlantMaterialDatas = new ArrayList<ErpPlantMaterialData>();
		for (ErpPlantMaterialModel erpPlantMaterialModel : list) {
			ErpPlantMaterialData data = convertErpPlantMaterialModelToData(erpPlantMaterialModel);
			erpPlantMaterialDatas.add(data);
		}
		return erpPlantMaterialDatas;
	}

	public static ErpPlantMaterialData convertErpPlantMaterialModelToData(
			ErpPlantMaterialModel erpPlantMaterialModel) {
		ErpPlantMaterialData data = new ErpPlantMaterialData();
		data.setAtpRule(erpPlantMaterialModel.getAtpRule().getName());
		data.setBlockedDays(getDayOfWeekSetData(erpPlantMaterialModel.getBlockedDays()));
		data.setDays_in_house(erpPlantMaterialModel.getDays_in_house());
		data.setHideOutOfStock(erpPlantMaterialModel.isHideOutOfStock());
		// http://jira.freshdirect.com/browse/SF17-72
		// data.setId(erpPlantMaterialModel.getId());
		data.setKosherProduction(erpPlantMaterialModel.isKosherProduction());
		data.setLeadTime(erpPlantMaterialModel.getLeadTime());
		data.setNumberOfDaysFresh(erpPlantMaterialModel.getNumberOfDaysFresh());
		data.setPlantId(erpPlantMaterialModel.getPlantId());
		data.setPlatter(erpPlantMaterialModel.isPlatter());
		data.setRating(erpPlantMaterialModel.getRating());
		data.setSustainabilityRating(erpPlantMaterialModel.getSustainabilityRating());
		return data;
	}
	
	private static DayOfWeekSetData getDayOfWeekSetData(DayOfWeekSet blockedDays) {
		DayOfWeekSetData data = new DayOfWeekSetData();
		if (blockedDays != null) {
			String dayString = blockedDays.encode();
			char[] daysArray = dayString.trim().toCharArray();
			for (int i = 0; i < daysArray.length; i++) {
				data.getDaysOfWeek().add(daysArray[i]);
			}
		}
		return data;
	}
	
	public static List<ErpSalesUnitData> createErpSalesUnitDataList(List<ErpSalesUnitModel> salesUnits) {
			List<ErpSalesUnitData> salesUnitDatas = new ArrayList<ErpSalesUnitData>();
			for (ErpSalesUnitModel erpSalesUnitModel : salesUnits) {
				ErpSalesUnitData data = convertErpSaleUnitModelToData(erpSalesUnitModel);
				salesUnitDatas.add(data);
			}
			return salesUnitDatas;
		}

	public static ErpSalesUnitData convertErpSaleUnitModelToData(
			ErpSalesUnitModel erpSalesUnitModel) {
		ErpSalesUnitData data = new ErpSalesUnitData();
		data.setAlternativeUnit(erpSalesUnitModel.getAlternativeUnit());
		data.setBaseUnit(erpSalesUnitModel.getBaseUnit());
		data.setDenominator(erpSalesUnitModel.getDenominator());
		data.setDescription(erpSalesUnitModel.getDescription());
		data.setDisplayInd(erpSalesUnitModel.isDisplayInd());
		// http://jira.freshdirect.com/browse/SF17-72
		//data.setId(erpSalesUnitModel.getId());
		data.setNumerator(erpSalesUnitModel.getNumerator());
		data.setUnitPriceDenominator(erpSalesUnitModel.getUnitPriceDenominator());
		data.setUnitPriceDescription(erpSalesUnitModel.getUnitPriceDescription());
		data.setUnitPriceUOM(erpSalesUnitModel.getUnitPriceUOM());
		return data;
	}

	public static List<ErpMaterialPriceData> createErpMaterialPriceDataList(List<ErpMaterialPriceModel> erpMaterialPriceModels) {
		List<ErpMaterialPriceData>  erpPlantMaterialDatas = new ArrayList<ErpMaterialPriceData>();
		for (ErpMaterialPriceModel erpMaterialPriceModel : erpMaterialPriceModels) {
			ErpMaterialPriceData data = convertErpMaterialPriceModelToData(erpMaterialPriceModel);
			erpPlantMaterialDatas.add(data);
		}
		return erpPlantMaterialDatas;
	}

	public static ErpMaterialPriceData convertErpMaterialPriceModelToData(
			ErpMaterialPriceModel erpMaterialPriceModel) {
		ErpMaterialPriceData data = new ErpMaterialPriceData();
		data.setDistChannel(erpMaterialPriceModel.getDistChannel());
		// http://jira.freshdirect.com/browse/SF17-72
		// data.setId(erpMaterialPriceModel.getId());
		data.setPrice(erpMaterialPriceModel.getPrice());
		data.setPricingUnit(erpMaterialPriceModel.getPricingUnit());
		data.setPromoPrice(erpMaterialPriceModel.getPromoPrice());
		data.setSalesOrg(erpMaterialPriceModel.getSalesOrg());
		data.setSapId(erpMaterialPriceModel.getSapId());
		data.setSapZoneId(erpMaterialPriceModel.getSapZoneId());
		data.setScaleQuantity(erpMaterialPriceModel.getScaleQuantity());
		data.setScaleUnit(erpMaterialPriceModel.getScaleUnit());
		return data;
	}
	public static List<ErpClassData> createErpClassDataList(List<ErpClassModel> erpClassModels) {
		List<ErpClassData>  erpClassDatas = new ArrayList<ErpClassData>();
		for (ErpClassModel erpClassModel : erpClassModels) {
			ErpClassData data = convertErpClassModelToData(erpClassModel);
			erpClassDatas.add(data);
		}
		return erpClassDatas;
	}
	
	public static ErpClassData convertErpClassModelToData(ErpClassModel erpClassModel) {
		ErpClassData data = new ErpClassData();
		data.setId(erpClassModel.getId());
		data.setSapId(erpClassModel.getSapId());
		data.setCharacteristics(createErpCharacteristicDataList(erpClassModel.getCharacteristics()));
		return data;
	}
	
	private static List<ErpCharacteristicData> createErpCharacteristicDataList(List<ErpCharacteristicModel> erpClassModels) {
		List<ErpCharacteristicData>  erpCharacteristicDatas = new ArrayList<ErpCharacteristicData>();
		for (ErpCharacteristicModel erpClassModel : erpClassModels) {
			ErpCharacteristicData data = convertErpCharacteristicModelToData(erpClassModel);
			erpCharacteristicDatas.add(data);
		}
		return erpCharacteristicDatas;
	}

	public static ErpCharacteristicData convertErpCharacteristicModelToData(
			ErpCharacteristicModel erpClassModel) {
		ErpCharacteristicData data = new ErpCharacteristicData();
		data.setId(erpClassModel.getId());
		data.setName(erpClassModel.getName());
		data.setCharacteristicValues(createErpCharacteristicValueDataList(erpClassModel.getCharacteristicValues()));
		return data;
	}
		
	public static List<ErpCharacteristicValueData> createErpCharacteristicValueDataList(List<ErpCharacteristicValueModel> list) {
		List<ErpCharacteristicValueData>  erpCharacteristicValueDatas = new ArrayList<ErpCharacteristicValueData>();
		for (ErpCharacteristicValueModel erpCharacteristicValueModel : list) {
			ErpCharacteristicValueData data = converErpCharactisticModelToData(erpCharacteristicValueModel);
			erpCharacteristicValueDatas.add(data);
		}
		return erpCharacteristicValueDatas;
	}

	public static ErpCharacteristicValueData converErpCharactisticModelToData(
			ErpCharacteristicValueModel erpCharacteristicValueModel) {
		ErpCharacteristicValueData data = new ErpCharacteristicValueData();
		data.setId(erpCharacteristicValueModel.getId());
		data.setName(erpCharacteristicValueModel.getName());
		data.setDescription(erpCharacteristicValueModel.getDescription());
		return data;
	}

	public static ErpCharacteristicValuePriceData createErpCharacteristicValuePriceData(
			ErpCharacteristicValuePriceModel key) {
		ErpCharacteristicValuePriceData data = new ErpCharacteristicValuePriceData();
		data.setCharacteristicName(key.getCharacteristicName());
		data.setCharacteristicValueId(key.getCharacteristicValueId());
		data.setCharacteristicValueName(key.getCharacteristicValueName());
		data.setClassName(key.getClassName());
		data.setConditionType(key.getConditionType());
		data.setDistChannel(key.getDistChannel());
		data.setId(key.getId());
		data.setMaterialId(key.getMaterialId());
		data.setPrice(key.getPrice());
		data.setPricingUnit(key.getPricingUnit());
		data.setSalesOrg(key.getSalesOrg());
		data.setSapId(key.getSapId());
		return data;
	}
	
	public static ErpCharacteristicValuePriceModel createErpCharacteristicValuePriceModel(
			ErpCharacteristicValuePriceData  data) {
		ErpCharacteristicValuePriceModel model = new ErpCharacteristicValuePriceModel();
		model.setCharacteristicName(data.getCharacteristicName());
		model.setCharacteristicValueId(data.getCharacteristicValueId());
		model.setCharacteristicValueName(data.getCharacteristicValueName());
		model.setClassName(data.getClassName());
		model.setConditionType(data.getConditionType());
		model.setDistChannel(data.getDistChannel());
		model.setId(data.getId());
		model.setMaterialId(data.getMaterialId());
		model.setPrice(data.getPrice());
		model.setPricingUnit(data.getPricingUnit());
		model.setSalesOrg(data.getSalesOrg());
		model.setSapId(data.getSapId());
		return model;
	}

	public static ErpInventoryModel convertErpInventoryDataToModel(
			ErpInventoryData erpInventoryData) {
		List<ErpInventoryEntryModel> erpInventoryEntry = createErpInventoryEntryModel(erpInventoryData.getEntries());
		ErpInventoryModel model = new ErpInventoryModel(
				erpInventoryData.getSapId(), erpInventoryData.getLastUpdated(),
				erpInventoryEntry);
		return model;
	}
	
	public static List<ErpInventoryEntryModel> createErpInventoryEntryModel(
			List<ErpInventoryEntryData> entries) {
		List<ErpInventoryEntryModel> entryModels = new ArrayList<ErpInventoryEntryModel>();
		for (ErpInventoryEntryData erpInventoryEntryData : entries) {
			ErpInventoryEntryModel erpInventoryEntryModel = new ErpInventoryEntryModel(erpInventoryEntryData.getStartDate(),erpInventoryEntryData.getQuantity(),erpInventoryEntryData.getPlantId());
			entryModels.add(erpInventoryEntryModel);
		}
		return entryModels;
	}

	public static Map<String, ErpInventoryModel> convertErpInventoryDataMapToModelMap(
			Map<String, ErpInventoryData> data) {
		Map<String,ErpInventoryModel> modelMap = new HashMap<String, ErpInventoryModel>();
		if(data!=null)
		for (Entry<String, ErpInventoryData> dataMap : data.entrySet()) {
			String key = dataMap.getKey();
			ErpInventoryModel model = convertErpInventoryDataToModel(dataMap.getValue());
			modelMap.put(key, model);
		}
		return modelMap;
	}
	
	public static FDCouponActivityContextData convertFDCouponActivityContext(FDCouponActivityContext FDCouponActivityContext){
		return null;
	}

	public static CouponWalletRequestData convertCouponWalletRequest(FDCouponCustomerData fdCouponCustomerData, FDCouponActivityContextData context) {
		CouponWalletRequestData couponWalletRequestData = new CouponWalletRequestData();
		couponWalletRequestData.setFDCouponActivityContextData(context);
		couponWalletRequestData.setFdCouponCustomerData(fdCouponCustomerData);
		return couponWalletRequestData;
	}

	public static ErpCouponTransactionModelData buildErpCouponTransactionModel(ErpCouponTransactionModel transModel) {
		ErpCouponTransactionModelData erpCouponTransactionModelData = new  ErpCouponTransactionModelData();
		erpCouponTransactionModelData.setCreateTime(transModel.getCreateTime());
		erpCouponTransactionModelData.setErrorDetails(transModel.getErrorDetails());
		erpCouponTransactionModelData.setErrorMessage(transModel.getErrorMessage());
		erpCouponTransactionModelData.setId(transModel.getId());
		erpCouponTransactionModelData.setSaleActionId(transModel.getSaleActionId());
		erpCouponTransactionModelData.setSaleId(transModel.getSaleId());
		List<ErpCouponTransactionDetailModelData> erpCouponTransactionDetailModels = new ArrayList<ErpCouponTransactionDetailModelData>(); 
		if (transModel.getTranDetails() != null) {
			for (ErpCouponTransactionDetailModel erpCouponTransaction : transModel.getTranDetails()) {
				ErpCouponTransactionDetailModelData couponTransactionModelData = new ErpCouponTransactionDetailModelData();
				couponTransactionModelData.setCouponId(erpCouponTransaction.getCouponId());
				couponTransactionModelData.setCouponLineId(erpCouponTransaction.getCouponLineId());
				couponTransactionModelData.setCouponTransId(erpCouponTransaction.getCouponTransId());
				couponTransactionModelData.setDiscountAmt(erpCouponTransaction.getDiscountAmt());
				couponTransactionModelData.setId(erpCouponTransaction.getId());
				couponTransactionModelData.setTransTime(erpCouponTransaction.getTransTime());
				erpCouponTransactionDetailModels.add(couponTransactionModelData);
			}
		}
		erpCouponTransactionModelData.setTranDetails(erpCouponTransactionDetailModels);
		if(transModel.getTranStatus()!=null)
		erpCouponTransactionModelData.setTranStatus(transModel.getTranStatus().getName());
		erpCouponTransactionModelData.setTranTime(transModel.getTranTime());
		if(transModel.getTranType()!= null)
		erpCouponTransactionModelData.setTranType(transModel.getTranType().getName());
		return erpCouponTransactionModelData;
	}

	public static EmailData buildEmailData(EmailI email) {
		EmailData emailData = new EmailData();
		buildData(emailData, email);
		if(email instanceof XMLEmailI){
			XMLEmailI xmlEmailI = (XMLEmailI) email;
			emailData.setHtmlEmail(xmlEmailI.isHtmlEmail());
			emailData.setXML(xmlEmailI.getXML());
			emailData.setXslPath(xmlEmailI.getXslPath());
			emailData.setEmailType("XMLEmailI");
		} else if(email instanceof FTLEmailI){
			FTLEmailI ftlEmailI = (FTLEmailI) email;
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (String key : ftlEmailI.getParameters().keySet()) {
				map.put(key, ftlEmailI.getParameters().get(key));
			}
			emailData.setParameters(map);
			emailData.setEmailType("FTLEmailI");
		}
		return emailData;
	}
	
	public static void buildData(EmailData emailData, EmailI email){
		emailData.setBCCList(email.getBCCList());
		emailData.setCCList(email.getCCList());
		emailData.setRecipient(email.getRecipient());
		emailData.setSubject(email.getSubject());
		EmailAddressData emailAddressData = new EmailAddressData();
		if (email.getFromAddress() != null) {
			emailAddressData.setAddress(email.getFromAddress().getAddress());
			emailAddressData.setName(email.getFromAddress().getName());
			emailData.setFromAddress(emailAddressData);
		}
	}
	public static List buildComplaintDlvIssueList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			EnumComplaintDlvIssueTypeData complData = (EnumComplaintDlvIssueTypeData)obj;
			EnumComplaintDlvIssueType comp = new EnumComplaintDlvIssueType(
					complData.getCode(),
					complData.getName(),
					complData.getDescription()	);
		l.add(comp);
		}
		return l;
	}
	public static List buildCasePriorityList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmCasePriorityData casePriorityData = (CrmCasePriorityData)obj;
			CrmCasePriority comp = new CrmCasePriority(
					casePriorityData.getCode(),
					casePriorityData.getName(),
					casePriorityData.getDescription(),
					casePriorityData.getPriority());
		l.add(comp);
		}
		return l;
	}


	public static FDProductInfo fdProductInfoDataToModel(
			FDProductInfoData fdProductInfoData) {
		ZonePriceInfoListing zonePriceInfoList = createZonePriceInfoList(fdProductInfoData.getZonePriceInfoList());
		Map<String,FDGroup> groups = createFDGrpMap(fdProductInfoData.getGroups());
		List<FDPlantMaterial> plantMaterialInfo = createPlantMaterialInfoList(fdProductInfoData.getPlantMaterialInfo());
		Map<String, FDMaterialSalesArea> materialAvailability = createMaterialAvailability(fdProductInfoData.getMaterialAvailability());
		FDProductInfo fdProductInfo = new FDProductInfo(
				fdProductInfoData.getSkuCode(),
				fdProductInfoData.getVersion(), 
				fdProductInfoData.getMaterialNumber(), 
				groups,fdProductInfoData.getUpc(),
				plantMaterialInfo, zonePriceInfoList, 
				materialAvailability,
				EnumAlcoholicContent.getAlcoholicContent(fdProductInfoData.getAlcoholType()));
		
		return fdProductInfo;
	}

	private static List<FDPlantMaterial> createPlantMaterialInfoList(
			Map<String, FDPlantMaterialData> plantMaterialInfo) {
		List<FDPlantMaterial> fdPlantMaterials = new ArrayList<FDPlantMaterial>();
		if(plantMaterialInfo!=null)
		for (Entry<String, FDPlantMaterialData> fdPlantMaterial : plantMaterialInfo.entrySet()) {
			String keyData = fdPlantMaterial.getKey();
			fdPlantMaterials.add(buildFDPlantMaterial(fdPlantMaterial.getValue()));
		}	
		return fdPlantMaterials;
	}

	private static Map<String, FDMaterialSalesArea> createMaterialAvailability(
			Map<String, FDMaterialSalesAreaData> materialAvailability) {
		Map<String,FDMaterialSalesArea> plantMap = new HashMap<String, FDMaterialSalesArea>();
		if(materialAvailability!=null)
		for (Entry<String, FDMaterialSalesAreaData> grpMap : materialAvailability.entrySet()) {
			String key= grpMap.getKey();
			FDMaterialSalesArea value = buildFDMaterialSalesArea(grpMap.getValue());
			plantMap.put(key, value);
		}
		return plantMap;
	}


	public static List buildCrmAgentRileList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmEnumTypeData crmEnumTypeData = (CrmEnumTypeData)obj;
			CrmAgentRole comp = new CrmAgentRole(
					crmEnumTypeData.getCode(),
					crmEnumTypeData.getName(),
					crmEnumTypeData.getDescription(),
					crmEnumTypeData.getLdapRoleName());
		l.add(comp);
		}
		return l;
		
	}

	public static List buildCaseActionTypeList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmEnumTypeData crmEnumTypeData = (CrmEnumTypeData)obj;
			CrmCaseActionType comp = new CrmCaseActionType(
					crmEnumTypeData.getCode(),
					crmEnumTypeData.getName(),
					crmEnumTypeData.getDescription()
				);
		l.add(comp);
		}
		return l;
		
	}

	public static List buildCrmCaseOriginList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmEnumTypeData crmEnumTypeData = (CrmEnumTypeData)obj;
			CrmCaseOrigin comp = new CrmCaseOrigin(
					crmEnumTypeData.getCode(),
					crmEnumTypeData.getName(),
					crmEnumTypeData.getDescription(),
					crmEnumTypeData.isObsolete()
				);
		l.add(comp);
		}
		return l;
		
	}

	public static List buildCrmCaseQueueList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmEnumTypeData crmEnumTypeData = (CrmEnumTypeData)obj;
			CrmCaseQueue comp = new CrmCaseQueue(
					crmEnumTypeData.getCode(),
					crmEnumTypeData.getName(),
					crmEnumTypeData.getDescription(),
					crmEnumTypeData.isObsolete()
				);
		l.add(comp);
		}
		return l;
		
	}

	public static List buildCrmCaseStateList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmEnumTypeData crmEnumTypeData = (CrmEnumTypeData)obj;
			CrmCaseState comp = new CrmCaseState(
					crmEnumTypeData.getCode(),
					crmEnumTypeData.getName(),
					crmEnumTypeData.getDescription()
				);
		l.add(comp);
		}
		return l;
		
	}

	public static List buildCrmDepartmentList(List data) {
		List l = new ArrayList();
		for(Object obj:data){
			CrmEnumTypeData crmEnumTypeData = (CrmEnumTypeData)obj;
			CrmDepartment comp = new CrmDepartment(
					crmEnumTypeData.getCode(),
					crmEnumTypeData.getName(),
					crmEnumTypeData.getDescription(),
					crmEnumTypeData.isObsolete()
				);
		l.add(comp);
		}
		return l;
		
	}

	

	public static FDMaterialSalesArea buildFDMaterialSalesArea(
			FDMaterialSalesAreaData value) {
		FDMaterialSalesArea area = new FDMaterialSalesArea();
		area.setDayPartValueType(value.getDayPartValueType()!=null? EnumDayPartValueType.getEnum(value.getDayPartValueType()):null);
		area.setPickingPlantId(value.getPickingPlantId());
		area.setSalesAreaInfo(buildSalesAreaInfo(value.getSalesAreaInfo()));
		area.setUnavailabilityDate((value.getUnavailabilityDate()!=null)?new Date(value.getUnavailabilityDate().getTime()):null);
		area.setUnavailabilityReason(value.getUnavailabilityReason());
		area.setUnavailabilityStatus(value.getUnavailabilityStatus());
		return area;
	}

	private static Map<String, FDPlantMaterial> createPlantMaterialInfoMap(
			Map<String, FDPlantMaterialData> plantMaterialInfo) {
		Map<String,FDPlantMaterial> plantMap = new HashMap<String, FDPlantMaterial>();
		for (Entry<String, FDPlantMaterialData> plantDataMap : plantMaterialInfo.entrySet()) {
			String key= plantDataMap.getKey();
			FDPlantMaterial value = buildFDPlantMaterial(plantDataMap.getValue());
			plantMap.put(key, value);
		}
		return plantMap;
	}

	public static FDPlantMaterial buildFDPlantMaterial(
			FDPlantMaterialData value) {
		FDPlantMaterial fdPlantMaterial = new FDPlantMaterial(value.getAtpRule()!=null?EnumATPRule.getEnum(value.getAtpRule()):null, 
				value.isKosherProduction(), 
				value.isPlatter(), 
				getDayOfWeekSet(value.getBlockedDays()), 
				value.getLeadTime(), 
				value.getPlantId(), 
				value.getFreshness(), 
				value.getRating()!=null?EnumOrderLineRating.getEnumByStatusCode(value.getRating()):null, 
				value.getSustainabilityRating()!=null?EnumSustainabilityRating.getEnumByStatusCode(value.getSustainabilityRating()):null , value.isLimitedQuantity());
		return fdPlantMaterial;
	}

	private static DayOfWeekSet getDayOfWeekSet(DayOfWeekSetData blockedDays) {

		DayOfWeekSet dayOfWeekSet = DayOfWeekSet.decode(null);
		if(blockedDays != null){
			Iterator iterator = blockedDays.getDaysOfWeek().iterator();
			while(iterator.hasNext()){
				String dayNumber = (String) iterator.next();
				dayOfWeekSet = DayOfWeekSet.decode(dayNumber);
			}
		}
		return dayOfWeekSet;
	}

	private static Map<String, FDGroup> createFDGrpMap(
			Map<String, FDGroupData> map) {
		Map<String,FDGroup> fdMap = new HashMap<String, FDGroup>();
		if(map!=null)
		for (Entry<String, FDGroupData> grpMap : map.entrySet()) {
			String key= grpMap.getKey();
			FDGroup value = buildFDGroup(grpMap.getValue());
			fdMap.put(key, value);
		}
		return fdMap;
	}

	private static ZonePriceInfoListing createZonePriceInfoList(List<ZonePriceInfoListingData> zonePriceInfoList) {
		ZonePriceInfoListing zonePriceInfoListing = new ZonePriceInfoListing();
		ZoneInfo zoneInfo = null;
		ZonePriceInfoModel zonePrice = null;
		for (ZonePriceInfoListingData zonePriceInfoListingData : zonePriceInfoList) {
			if(zonePriceInfoListingData.getZoneInfoData()!=null){
			zoneInfo  = buildZoneInfo(zonePriceInfoListingData.getZoneInfoData()); 
			zonePrice = buildZonePriceInfo(zonePriceInfoListingData.getZonePriceInfo());
			zonePriceInfoListing.addZonePriceInfo(zoneInfo, zonePrice);
			}
		}
		return zonePriceInfoListing;
	}

	public static ZonePriceInfoModel buildZonePriceInfo(ZonePriceInfoData zonePriceInfo) {
		ZonePriceInfoModel zonePriceInfoModel = new ZonePriceInfoModel(zonePriceInfo.getSellingPrice(), 
				zonePriceInfo.getPromoPrice(), zonePriceInfo.getDefaultPriceUnit(), zonePriceInfo.getDisplayableDefaultPriceUnit(), zonePriceInfo.isItemOnSale(), zonePriceInfo.getDealPercentage(), 
				zonePriceInfo.getTieredDealPercentage(), buildZoneInfo(zonePriceInfo.getZoneInfo()), zonePriceInfo.isShowBurstImage());
		return zonePriceInfoModel;
	}

	public static RestrictedPaymentMethodData buildRestrictedPaymentMethodData(RestrictedPaymentMethodModel model) {
		RestrictedPaymentMethodData data = new RestrictedPaymentMethodData();
		data.setAbaRouteNumber(model.getAbaRouteNumber());
		if(model.getAbaRoutePatternType()!=null)
		data.setAbaRoutePatternType(model.getAbaRoutePatternType().getName());
		data.setAccountNumber(model.getAccountNumber());
		if(model.getAccountPatternType()!=null)
		data.setAccountPatternType(model.getAccountPatternType().getName());
		if(model.getBankAccountType()!=null)
		data.setBankAccountType(model.getBankAccountType().getName());
		data.setBankName(model.getBankName());
		if(model.getCardType()!=null)
		data.setCardType(model.getCardType().getName());
		data.setCaseId(model.getCaseId());
		data.setCreateDate(model.getCreateDate());
		data.setCreateUser(model.getCreateUser());
		data.setCustomerId(model.getCustomerId());
		data.setExpirationDate(model.getExpirationDate());
		data.setFirstName(model.getFirstName());
		data.setId(model.getId());
		data.setLastModifyDate(model.getLastModifyDate());
		data.setLastModifyUser(model.getLastModifyUser());
		data.setLastName(model.getLastName());
		data.setNote(model.getNote());
		data.setPaymentMethodId(model.getPaymentMethodId());
		if(model.getPaymentMethodType()!=null)
		data.setPaymentMethodType(model.getPaymentMethodType().getName());
		data.setProfileID(model.getProfileID());
		if(model.getReason()!=null)
		data.setReason(model.getReason().getName());
		if(model.getSource()!=null)
		data.setSource(model.getSource().getCode());
		if(model.getStatus()!=null)
		data.setStatus(model.getStatus().getName());
		return data;
	}

	public static RestrictedPaymentMethodModel buildRestrictedPaymentMethodModel(
			RestrictedPaymentMethodData data) {
		if (data != null) {
			RestrictedPaymentMethodModel model = new RestrictedPaymentMethodModel();
			model.setAbaRouteNumber(data.getAbaRouteNumber());
			if (data.getAbaRoutePatternType() != null)
				model.setAbaRoutePatternType(EnumRestrictedPatternType
						.getEnum(data.getAbaRoutePatternType()));
			model.setAccountNumber(data.getAccountNumber());
			if (data.getAccountPatternType() != null)
				model.setAccountPatternType(EnumRestrictedPatternType
						.getEnum(data.getAccountPatternType()));
			if (data.getBankAccountType() != null)
				model.setBankAccountType(EnumBankAccountType.getEnum(data
						.getBankAccountType()));
			model.setBankName(data.getBankName());
			if (data.getCardType() != null)
				model.setCardType(EnumCardType.getEnum(data.getCardType()));
			model.setCaseId(data.getCaseId());
			model.setCreateDate(data.getCreateDate());
			model.setCreateUser(data.getCreateUser());
			model.setCustomerId(data.getCustomerId());
			model.setExpirationDate(data.getExpirationDate());
			model.setFirstName(data.getFirstName());
			model.setId(data.getId());
			model.setPK(new PrimaryKey(data.getId()));
			model.setLastModifyDate(data.getLastModifyDate());
			model.setLastModifyUser(data.getLastModifyUser());
			model.setLastName(data.getLastName());
			model.setNote(data.getNote());
			model.setPaymentMethodId(data.getPaymentMethodId());
			if (data.getPaymentMethodType() != null)
				model.setPaymentMethodType(EnumPaymentMethodType.getEnum(data
						.getPaymentMethodType()));
			model.setProfileID(data.getProfileID());
			if (data.getReason() != null)
				model.setReason(EnumRestrictionReason.getEnum(data.getReason()));
			if (data.getSource() != null)
				model.setSource(EnumTransactionSource.getTransactionSource(data
						.getSource()));
			if (data.getStatus() != null)
				model.setStatus(EnumRestrictedPaymentMethodStatus.getEnum(data
						.getStatus()));
			return model;
		}
		return null;
	}

	
	public static ErpPaymentMethodData buildErpPaymentMethodData(
			ErpPaymentMethodI erpPaymentMethod) {
		ErpPaymentMethodData data = new ErpPaymentMethodData();
		data.setAccountNumber(erpPaymentMethod.getAccountNumber());
		data.setPaymentType(erpPaymentMethod.getPaymentType().getName());
		if(erpPaymentMethod.getBankAccountType()!=null)
		data.setBankAccountType(erpPaymentMethod.getBankAccountType().getName());
		data.setAbaRouteNumber(erpPaymentMethod.getAbaRouteNumber());
		data.setCustomerId(erpPaymentMethod.getCustomerId());
		data.setPaymentMethodType(erpPaymentMethod.getPaymentMethodType().getName());
		return data;
	}

	public static ErpPaymentMethodI buildErpPaymentMethodModel(
			ErpPaymentMethodData data) {
		ErpPaymentMethodI model = null;
		if(data.getPaymentMethodType().equals(EnumPaymentMethodType.GIFTCARD.getName())){
			model = createErpGiftCardModel(data);
		}
		else if(data.getPaymentMethodType().equals(EnumPaymentMethodType.ECHECK.getName())){
			model = createECheckModelModel(data);

		}
		else if(data.getPaymentMethodType().equals(EnumPaymentMethodType.EBT.getName())){
			model = createErpEbtCardModel(data);
		}
		else if(data.equals(EnumPaymentMethodType.PAYPAL.getName())){
			model = createErpPayPalCardModel(data);
		}
		else{
			model = createErpCreditCardModel(data);
		}
		return model;
	}
	
	private static ErpPaymentMethodI createErpCreditCardModel(ErpPaymentMethodData source) {
		ErpCreditCardModel model = new ErpCreditCardModel();
		createErpPaymentMethod(model,source);
		model.setExpirationDate(new java.sql.Date(source.getExpirationDate()));
		model.setCardType(EnumCardType.getCardType(source.getCardType()));
		model.setAvsCkeckFailed(source.isAvsCkeckFailed());
		model.setBypassAVSCheck(source.isBypassAVSCheck());
		return model;
	}

	private static ErpPaymentMethodI createErpPayPalCardModel(ErpPaymentMethodData source) {
		ErpPayPalCardModel model = new ErpPayPalCardModel();
		createErpPaymentMethod(model,source);
		return model;
	}

	private static ErpPaymentMethodI createErpEbtCardModel(ErpPaymentMethodData source) {
		ErpEbtCardModel model = new ErpEbtCardModel();
		createErpPaymentMethod(model,source);
		model.setCardType(EnumCardType.getCardType(source.getCardType()));
		return model;
	}

	private static ErpPaymentMethodI createECheckModelModel(ErpPaymentMethodData source) {
		ErpECheckModel model = new ErpECheckModel();
		createErpPaymentMethod(model,source);
		model.setBankName(source.getBankName());
		model.setAbaRouteNumber(source.getAbaRouteNumber());
		model.setBankAccountType(EnumBankAccountType.getEnum(source.getBankAccountType()));
		model.setIsTermsAccepted(source.isTermsAccepted());
		return model;
	}

	private static ErpPaymentMethodI createErpGiftCardModel(ErpPaymentMethodData source) {
		ErpGiftCardModel model = new ErpGiftCardModel();
		createErpPaymentMethod(model,source);
		model.setBalance(source.getBalance());
		if(source.getStatus().equals("U")) {
			model.setStatus(EnumGiftCardStatus.UNKNOWN);
		}else if(source.getStatus().equals("A")) {
			model.setStatus(EnumGiftCardStatus.ACTIVE);
		}else if(source.getStatus().equals("I")) {
			model.setStatus(EnumGiftCardStatus.INACTIVE);
		}else if(source.getStatus().equals("Z")) {
			model.setStatus(EnumGiftCardStatus.ZERO_BALANCE);
		}
		model.setOriginalAmount(source.getOriginalAmount());
		model.setPurchaseSaleId(source.getPurchaseSaleId());
		if(source.getPurchaseDate() != null)
		model.setPurchaseDate(new java.sql.Date(source.getPurchaseDate()));
		return model;
		
	}
	
	private static void createErpPaymentMethod(ErpPaymentMethodModel model, ErpPaymentMethodData source) {
		model.setId(source.getId());
		model.setCustomerId(source.getCustomerId());
		model.setName(source.getName());
		model.setAccountNumber(source.getAccountNumber());
		if(source.getAddress() !=null &&source.getAddress().getId() == null)
			source.getAddress().setId("");
//		model.setAddress(mapperFacade.map(source.getAddress(), ContactAddressModel.class));
		model.setBillingRef(source.getBillingRef());
		model.setPaymentType(EnumPaymentType.getEnum(source.getPaymentType()));
		model.setReferencedOrder(source.getReferencedOrder());
		model.setCVV(source.getCvv());
		model.setProfileID(source.getProfileID());
		model.setAccountNumLast4(source.getAccountNumLast4());
		model.setBestNumberForBillingInquiries(source.getBestNumberForBillingInquiries());
		model.seteWalletID(source.geteWalletID());
		model.setVendorEWalletID(source.getVendorEWalletID());
		model.seteWalletTrxnId(source.geteWalletTrxnId());
		model.setEmailID(source.getEmailID());
		model.setDeviceId(source.getDeviceId());
		model.setDebitCard(source.isDebitCard());
		model.setPaymentType(EnumPaymentType.getEnum(source.getPaymentType()));
	}

	public static CustomerCreditData buildCustomerCreditData(String referral_customer_id, String customer_id, int ref_fee,
			String sale, String complaintId, String refPrgmId) {
		CustomerCreditData data = new CustomerCreditData();
		data.setComplaintId(complaintId);
		data.setRef_fee(ref_fee);
		data.setCustomer_id(customer_id);
		data.setReferral_customer_id(referral_customer_id);
		data.setRefPrgmId(refPrgmId);
		data.setSale(sale);
		return data;
	}

	public static FailedAttemptData buildFailesAttemptData(String email,
			String dupeCustID, String zipCode, String firstName,
			String lastName, String referral, String reason) {
		FailedAttemptData data = new FailedAttemptData();
		data.setDupeCustID(dupeCustID);
		data.setEmail(email);
		data.setFirstName(firstName);
		data.setLastName(lastName);
		data.setReason(reason);
		data.setReferral(referral);
		data.setZipCode(zipCode);
		return data;
	}

	public static FNLNZipData buildFNLNZipData(String firstName,String lastName, String zipCode, String customerId) {
		FNLNZipData data = new FNLNZipData();
		data.setCustomerId(customerId);
		data.setFirstName(firstName);
		data.setLastName(lastName);
		data.setZipCode(zipCode);
		return data;
	}

	public static List<ErpCustomerCreditModel> buildCustomerCreditModelList(List<UserCreditData> data) {
		List<ErpCustomerCreditModel> creditModelList = new ArrayList<ErpCustomerCreditModel>();
		for (UserCreditData userCreditData : data) {
			ErpCustomerCreditModel model = new ErpCustomerCreditModel();
			model.setAmount(userCreditData.getAmount());
			model.setcDate(userCreditData.getcDate());
			model.setDepartment(userCreditData.getDepartment());
			model.setSaleId(userCreditData.getSaleId());
			if(userCreditData.geteStore()!=null)
			model.seteStore(userCreditData.geteStore());
			creditModelList.add(model);
		}
		return creditModelList;
	}

	public static MailData buildMailData(String recipient_list,String mail_message, String identity, String rpid, String serverName) {
		MailData mail = new MailData();
		mail.setIdentity(identity);
		mail.setMail_message(mail_message);
		mail.setRecipient_list(recipient_list);
		mail.setRpid(rpid);
		mail.setServerName(serverName);
		return mail;
		
	}

	public static ErpNutritionModelData buildNutritionModelData(
			ErpNutritionModel nutritionModel) {
		ErpNutritionModelData data = new ErpNutritionModelData();
		data.setLastModifiedDate(nutritionModel.getLastModifiedDate());
		data.setSkuCode(nutritionModel.getSkuCode());
		data.setUpc(nutritionModel.getUpc());
		if(!nutritionModel.getUom().isEmpty())
		data.setUom(nutritionModel.getUom());
		if(!nutritionModel.getValues().isEmpty())
		data.setValues(nutritionModel.getValues());
		if(!nutritionModel.getNutritionInfo().isEmpty()){
			List<ErpNutritionInfoTypeAttrWrapper> wrapperList = new ArrayList();
			for (Map.Entry<ErpNutritionInfoType,NutritionInfoAttribute> entry : nutritionModel.getNutritionInfo().entrySet()){
				ErpNutritionInfoTypeAttrWrapper wrapper = new ErpNutritionInfoTypeAttrWrapper();
				wrapper.setAttributeData(buildNutritionInfoAttributeData(entry.getValue()));
				wrapper.setNutritionData(buildErpNutritionInfoTypeData(entry.getKey()));
				wrapperList.add(wrapper);
			}
		data.setInfo(wrapperList);
		}
		return data;
	}


	public static NutritionInfoAttributeData buildNutritionInfoAttributeData(
			NutritionInfoAttribute value) {
		NutritionInfoAttributeData data = new NutritionInfoAttributeData();
		data.setPriority(value.getPriority());
		data.setValue(value.getValue());
		data.setType(buildErpNutritionInfoTypeData(value.getNutritionInfoType()));
		return data;
	}

	public static ErpNutritionInfoTypeData buildErpNutritionInfoTypeData(ErpNutritionInfoType key) {
		ErpNutritionInfoTypeData data = new ErpNutritionInfoTypeData();
		data.setCode(key.getCode());
		data.setName(key.getName());
		data.setMultiValued(key.isMultiValued());
		return data;
	}

	public static ErpNutritionModel buildErpNutritionModel(
			ErpNutritionModelData nutritionModelData) {
		ErpNutritionModel data = new ErpNutritionModel();
		data.setLastModifiedDate(nutritionModelData.getLastModifiedDate());
		data.setSkuCode(nutritionModelData.getSkuCode());
		data.setUpc(nutritionModelData.getUpc());
		if(nutritionModelData.getUom()!=null){
			for (Map.Entry<String, String> entry : nutritionModelData.getUom().entrySet()){
				data.setUomFor(entry.getKey(),entry.getValue());
			}
		}
		if(nutritionModelData.getValues()!=null){
			for (Map.Entry<String, Object> entry : nutritionModelData.getValues().entrySet()){
				double val = (Double)entry.getValue();
				data.setValueFor(entry.getKey(),val);
			}
		}	

		if(nutritionModelData.getInfo()!=null){
			for (ErpNutritionInfoTypeAttrWrapper  wrrapper : nutritionModelData.getInfo()){
				if(wrrapper.getAttributeData().getValue()!=null){
					if(wrrapper.getAttributeData().getType().isMultiValued()){
						for(Object nInfoAttrdata:(List) wrrapper.getAttributeData().getValue()){
							NutritionInfoAttributeData nIAData = wrrapper.getAttributeData();
							ErpNutritionInfoType nutType= buildErpNutritionInfoType(nIAData.getType());
							String value = getValueType(nInfoAttrdata);

							NutritionInfoAttribute nutAttr = new NutritionInfoAttribute(nutType, nIAData.getPriority(), getValueForCode(nutType,value));
							data.addNutritionAttribute(nutAttr);	
						}
					
					}
					data.addNutritionAttribute(buildNutritionAttribute(wrrapper.getAttributeData()));
				}
				
			}
		}	
		return data;
	}


	public static NutritionInfoAttribute buildNutritionAttribute(
			NutritionInfoAttributeData value) {
		ErpNutritionInfoType nutType= buildErpNutritionInfoType(value.getType());
		NutritionInfoAttribute data = new NutritionInfoAttribute(nutType, value.getPriority(), buildEnumKosherSymbolValue(nutType,value.getValue()));
		return data;
	}

	private static Object buildEnumKosherSymbolValue(ErpNutritionInfoType nutType,Object obj) {
		   Object value = null;
		   if(obj==null)
			   return null;
		  String code =  getCode(obj);
		  value = getValueForCode(nutType,code);
         
		return value;
	}
	
	private static Object getValueForCode(ErpNutritionInfoType nutType,String code) {
		   Object value = null;
		   if(code==null)
			   return null;
		 if (nutType.equals(ErpNutritionInfoType.CLAIM)) {
          value = EnumClaimValue.getValueForCode(code);
      } else if (nutType.equals(ErpNutritionInfoType.KOSHER_SYMBOL)) {
          value = EnumKosherSymbolValue.getValueForCode(code);
      } else if (nutType.equals(ErpNutritionInfoType.KOSHER_TYPE)) {
          value =  EnumKosherTypeValue.getValueForCode(code);
      } else if (nutType.equals(ErpNutritionInfoType.ALLERGEN)) {
          value = EnumAllergenValue.getValueForCode(code);
      } else if (nutType.equals(ErpNutritionInfoType.ORGANIC)) {
          value = EnumOrganicValue.getValueForCode(code);
      } else {
          value = code;
      }
		return value;
	}
	private static String getCode(Object objData){
		String code=null;
		List objDataTemp = null;
		Map objDataMap=null;
		if(objData instanceof  ArrayList){
			objDataTemp = (ArrayList)objData;
			objDataMap = (Map)objDataTemp.get(0);
			Set keys = objDataMap.keySet();
			for(Object key:keys){
				String keyData=(String)key;
				if(keyData.equalsIgnoreCase("value")){
					Map coded = (Map)objDataMap.get(keyData);
					for(Object valueKey:coded.keySet()){
						String valuekeyData=(String)valueKey;
						if(valuekeyData.equalsIgnoreCase("Code"))
							code = (String)coded.get(valuekeyData);
					}
				}
			}
		}else if(objData instanceof  Map){
			objDataMap =(Map) objData;
			Set keys = objDataMap.keySet();
			for(Object key:keys){
				String keyData=(String)key;
				if(keyData.equalsIgnoreCase("Code"))
					code = (String)objDataMap.get(keyData);
			}
		}else if(objData instanceof  String){
			code = (String)objData;
			}
		
		return code;
		
	}
	
	private static String getValueType(Object objData){
		String code=null;
		List objDataTemp = null;
		Map objDataMap=null;
		if(objData instanceof  ArrayList){
			objDataTemp = (ArrayList)objData;
			objDataMap = (Map)objDataTemp.get(0);
			Set keys = objDataMap.keySet();
			for(Object key:keys){
				String keyData=(String)key;
				if(keyData.equalsIgnoreCase("value")){
					Map coded = (Map)objDataMap.get(keyData);
					for(Object valueKey:coded.keySet()){
						String valuekeyData=(String)valueKey;
						if(valuekeyData.equalsIgnoreCase("Code"))
							code = (String)coded.get(valuekeyData);
					}
				}
			}
		}else if(objData instanceof  Map){
			objDataMap =(Map) objData;
			Set keys = objDataMap.keySet();
			for(Object key:keys){
				String keyData=(String)key;
				if(keyData.equalsIgnoreCase("value")){
					Map coded = (Map)objDataMap.get(keyData);
					for(Object valueKey:coded.keySet()){
						String valuekeyData=(String)valueKey;
						if(valuekeyData.equalsIgnoreCase("Code"))
							code = (String)coded.get(valuekeyData);
					}
				}}
		}else if(objData instanceof  String){
			code = (String)objData;
			}
		
		return code;
		
	}
	public static ErpNutritionInfoType buildErpNutritionInfoType(ErpNutritionInfoTypeData key) {
		ErpNutritionInfoType data =    ErpNutritionInfoType.getInfoType(key.getCode());
		return data;
	}

	public static FDProduct buildFdProduct(FDProductData data) {
		FDProduct product = new FDProduct(data.getSkuCode(), data.getVersion(), data.getPricingDate(), buildFDMaterial(data.getMaterial()), 
				buildFDVariation(data.getVariations()), buildFDSalesUnitArray(data.getSalesUnits()),
				buildPricing(data.getPricing()), 
				buildFDNutrition(data.getNutrition()),
				buildFDSalesUnitArray(data.getDisplaySalesUnits()),
				buildNutritionPanel(data.getNutritionPanel()), data.getUpc());
		return product;
	}

	private static NutritionPanel buildNutritionPanel(
			NutritionPanelData nutritionPanel) {
		NutritionPanel panel = new NutritionPanel();
		if(nutritionPanel!=null){
		panel.setId(nutritionPanel.getId());
		panel.setLastModifiedDate(nutritionPanel.getLastModifiedDate());
		panel.setSkuCode(nutritionPanel.getSkuCode());
		panel.setSections(buildNutritionSection(nutritionPanel.getSections()));
		panel.setType(buildNutritionPanelType(nutritionPanel.getType()));
		}
		return panel;
	}

	private static List<NutritionSection> buildNutritionSection(
			List<NutritionSectionData> sections) {
		List<NutritionSection> nutriSections = new ArrayList<NutritionSection>();
		if(nutriSections!=null){
			for (NutritionSection nutritionSection : nutriSections) {
				NutritionSection section = new NutritionSection();
				section.setId(nutritionSection.getId());
				section.setImportance(nutritionSection.getImportance());
				section.setItems(nutritionSection.getItems());
				section.setPosition(nutritionSection.getPosition());
				section.setTitle(nutritionSection.getTitle());
				section.setType(buildNutritionSectionType(nutritionSection.getType()));
				nutriSections.add(nutritionSection);
			}
		}
			
		return nutriSections;
	}


	private static NutritionSectionType buildNutritionSectionType(
			NutritionSectionType type) {
		if (type.equals(NutritionSectionType.FREETEXT.getName()))
			return NutritionSectionType.FREETEXT;
		else if (type.equals(NutritionSectionType.INGREDIENT.getName()))
			return NutritionSectionType.INGREDIENT;
		else if (type.equals(NutritionSectionType.TABLE.getName()))
			return NutritionSectionType.TABLE;
		return type;
	}

	private static NutritionPanelType buildNutritionPanelType(String type) {
		if(type ==null)
			return NutritionPanelType.BABY;
		else if (type.equals(NutritionPanelType.CLASSIC.getName()))
			return NutritionPanelType.CLASSIC;
		else if (type.equals(NutritionPanelType.DRUG.getName()))
			return NutritionPanelType.DRUG;
		else if (type.equals(NutritionPanelType.PET.getName()))
			return NutritionPanelType.PET;
		else if (type.equals(NutritionPanelType.SUPPL.getName()))
			return NutritionPanelType.SUPPL;
		return null;
	}

	private static List<FDNutrition> buildFDNutrition(
			List<FDNutritionData> nutritionDatas) {
		List<FDNutrition> fdNutritions = new ArrayList<FDNutrition>();
		for (FDNutritionData fdNutrition : nutritionDatas) {
			FDNutrition model = new FDNutrition(fdNutrition.getName(), fdNutrition.getValue(), fdNutrition.getUom());
			fdNutritions.add(model);
		}
		return fdNutritions;
	}

	private static Pricing buildPricing(PricingData pricing) {
		ZonePriceListing zonePrice = buildZonePrice(pricing.getZonePriceList());
		SalesUnitRatio[] salesUnits =  buildSalesRatios(pricing.getSalesUnits());
		CharacteristicValuePrice[] cvPrices = buildCharacteristicValuePrice(pricing.getCvPrices());
		Pricing model = new Pricing(zonePrice, cvPrices, salesUnits, pricing.isWineOrSpirit());
		return model;
	}
	private static CharacteristicValuePrice[] buildCharacteristicValuePrice(
			CharacteristicValuePriceData[] characteristicValuePriceDatas) {
		CharacteristicValuePrice[] models = new CharacteristicValuePrice[0] ;
		if (characteristicValuePriceDatas != null
				&& characteristicValuePriceDatas.length > 0) {
			models = new CharacteristicValuePrice[characteristicValuePriceDatas.length];
			for (int i = 0; i < characteristicValuePriceDatas.length; i++) {
				CharacteristicValuePriceData chaValPrice = characteristicValuePriceDatas[i];
				CharacteristicValuePrice model = new CharacteristicValuePrice(
						chaValPrice.getCharName(),
						chaValPrice.getCharValueName(), chaValPrice.getPrice(),
						chaValPrice.getPricingUnit(),
						chaValPrice.getApplyHow(), chaValPrice.getSalesOrg(),
						chaValPrice.getDistChannel());
				models[i] = model;
			}
		}
		return models;
	}

	private static SalesUnitRatio[] buildSalesRatios(SalesUnitRatioData[] salesUnitRatios) {
		SalesUnitRatio[] models = new SalesUnitRatio[0];
		if(salesUnitRatios!=null && salesUnitRatios.length > 0){
			models = new SalesUnitRatio[salesUnitRatios.length];
			for(int i=0;i<salesUnitRatios.length;i++){
				SalesUnitRatioData salesUnitRatioData = salesUnitRatios[i];
				SalesUnitRatio salesUnitRatio = new SalesUnitRatio(salesUnitRatioData.getAlternateUnit(), salesUnitRatioData.getSalesUnit(), salesUnitRatioData.getRatio());
				models[i]= salesUnitRatio;
			}
		}
		return models;
	}

	private static ZonePriceListing buildZonePrice(
			List<ZonePriceListingData> zonePriceList) {
		ZonePriceListing model = new ZonePriceListing();
		Map<ZoneInfo,ZonePriceModel> zonePriceMap = new HashMap<ZoneInfo, ZonePriceModel>();
		ZoneInfo key;
		ZonePriceModel value;
		for (ZonePriceListingData zonePriceListingData : zonePriceList) {
			key = buildZoneInfo(zonePriceListingData.getKey()); 
			value = buildZonePriceModel(zonePriceListingData.getValue(),key);
			zonePriceMap.put(key, value);
		}
		model.reloadZonePrices(zonePriceMap);
		return model;
	}

	private static ZonePriceModel buildZonePriceModel(ZonePriceData value, ZoneInfo key) {
		MaterialPrice[] matPrices = buildMaterialPriceArray(value.getMaterialPrices());
		ZoneInfo zoneInfo = buildZoneInfo(value.getZoneInfo());
		ZonePriceModel model = new ZonePriceModel(zoneInfo, matPrices);
		return model;
	}

	private static MaterialPrice[] buildMaterialPriceArray(
			com.freshdirect.ecommerce.data.common.pricing.MaterialPrice[] materialPrices) {
		MaterialPrice[] price = new MaterialPrice[0];
		if(materialPrices!=null && materialPrices.length > 0){
			price = new MaterialPrice[materialPrices.length];
			for (int i = 0; i < materialPrices.length; i++) {
				MaterialPrice matPrice = buildMaterialPrice(materialPrices[i]);
				price[i] = matPrice;
			}
		}
		return price;
	}


	private static FDSalesUnit[] buildFDSalesUnitArray(
			FDSalesUnitData[] salesUnitDatas) {
		FDSalesUnit[] salesUnits = new FDSalesUnit[0];
		if(salesUnitDatas!=null && salesUnitDatas.length > 0){
			salesUnits = new FDSalesUnit[salesUnitDatas.length];
			for (int i = 0; i < salesUnitDatas.length; i++) {
				FDSalesUnitData saleUnit = salesUnitDatas[i];
				AttributesI attributes = new AttributeCollection(saleUnit.getAttributes());
				FDSalesUnit model = new FDSalesUnit(attributes, saleUnit.getName(), saleUnit.getDescription(), saleUnit.getNumerator(), saleUnit.getDenominator(), 
						saleUnit.getBaseUnit(), saleUnit.getUnitPriceNumerator(), saleUnit.getUnitPriceDenominator(), saleUnit.getUnitPriceUOM(), saleUnit.getUnitPriceDescription());
				salesUnits[i]=model; 
			}
		}
		return salesUnits;
	}

	private static FDVariation[] buildFDVariation(FDVariationData[] variationDatas) {
		FDVariation[] variations = new FDVariation[0];
		if(variationDatas!=null && variationDatas.length > 0){
			variations = new FDVariation[variationDatas.length];
			for (int i = 0; i < variationDatas.length; i++) {
				FDVariationData fdVariation = variationDatas[i];
				AttributesI attribute = new AttributeCollection(fdVariation.getAttributes());
				FDVariation variation = new FDVariation(attribute, fdVariation.getName(), buildVariationOptions(fdVariation.getVariationOptions()));
				variations[i]=variation; 
			}
		}else if(variationDatas==null){
			variations = new FDVariation[0];
		}
		return variations;
	}

	private static FDVariationOption[] buildVariationOptions(
			FDVariationOptionData[] variationOptionDatas) {
		FDVariationOption[] variationOptions = new FDVariationOption[0];
		if(variationOptionDatas!=null && variationOptionDatas.length > 0){
			variationOptions = new FDVariationOption[variationOptionDatas.length];
			for (int i = 0; i < variationOptionDatas.length; i++) {
				FDVariationOptionData fdVariationOption = variationOptionDatas[i];
				AttributesI attributes = new AttributeCollection(fdVariationOption.getAttributes());
				FDVariationOption options = new FDVariationOption(attributes, fdVariationOption.getName(), fdVariationOption.getDescription());
				variationOptions[i] = options;
			}
		}
		return variationOptions;
	}

	private static FDMaterial buildFDMaterial(FDMaterialData material) {
		AttributesI attribute = new AttributeCollection(material.getAttributes());
		FDMaterial model = new FDMaterial(attribute, material.getMaterialNumber(), material.getSalesUnitCharacteristic(), 
				material.getQuantityCharacteristic(),EnumAlcoholicContent.getAlcoholicContent(material.getAlcoholicContent()), 
				material.isTaxable(), material.getSkuCode(), material.getTaxCode(), material.getMaterialGroup());
		model.setMaterialPlants(buildFDPlantMaterialMap(material.getMaterialPlants()));
		model.setMaterialSalesAreas(buildSalesAreaMap(material.getMaterialSalesArea()));
		
		return model;
	}

	private static Map<SalesAreaInfo, FDMaterialSalesArea> buildSalesAreaMap(
			List<MaterialSalesAreaData> materialSalesArea) {
		Map<SalesAreaInfo, FDMaterialSalesArea> map = new HashMap<SalesAreaInfo, FDMaterialSalesArea>();
		for (MaterialSalesAreaData materialSalesAreaData : materialSalesArea) {
			SalesAreaInfo key = buildSalesAreaInfo(materialSalesAreaData.getKey());
			FDMaterialSalesArea value =buildFDMaterialSalesArea(materialSalesAreaData.getValue());
			map.put(key, value);
		}
		return map;
	}

	private static Map<String, FDPlantMaterial> buildFDPlantMaterialMap(
			Map<String, FDPlantMaterialData> materialPlants) {
		Map<String, FDPlantMaterial> plantMap = new HashMap<String, FDPlantMaterial>();
		for (Entry<String, FDPlantMaterialData> dataMap : materialPlants.entrySet()) {
			plantMap.put(dataMap.getKey(), buildFDPlantMaterial(dataMap.getValue()));
		}
		return plantMap;
	}

	public static Map<String, List<ErpComplaintReason>> buildErpComplaintReason(Map<String, List<ErpComplaintReasonData>> data) {
		Map<String, List<ErpComplaintReason>> complaintReasonMap = new HashMap<String, List<ErpComplaintReason>>();
		List<ErpComplaintReason> reasonList = new ArrayList<ErpComplaintReason>();
		for (String key :  data.keySet()) {
			List<ErpComplaintReasonData> complaintReasonDataList = data.get(key);
			for (ErpComplaintReasonData erpComplaintReasonData : complaintReasonDataList) {
				reasonList.add(buildErpComplaintReason(erpComplaintReasonData));
			}
			complaintReasonMap.put(key, reasonList);
			
		}
		return complaintReasonMap;
	}

	public static ErpComplaintReason buildErpComplaintReason(
			ErpComplaintReasonData erpComplaintReasonData) {
		ErpComplaintReason reason = new ErpComplaintReason(
				erpComplaintReasonData.getId(),
				erpComplaintReasonData.getDepartmentCode(),
				erpComplaintReasonData.getDepartmentName(),
				erpComplaintReasonData.getReason(),
				erpComplaintReasonData.getPriority(),
				erpComplaintReasonData.getSubjectCode(),
				erpComplaintReasonData.getDlvIssueType() != null ? EnumComplaintDlvIssueType
						.getEnum(erpComplaintReasonData.getDlvIssueType()): null);
		return reason;
		}

	public static DeliveryPassModel buildDeliveryPassModel(
			DeliveryPassData data) {
		DeliveryPassModel model = new DeliveryPassModel();
		model.setId(data.getId());
		model.setAmount(data.getAmount());
		model.setCustomerId(data.getCustomerId());
		model.setDescription(data.getDescription());
		model.setExpirationDate(data.getExpDate());
		model.setNoOfCredits(data.getNoOfCredits());
		model.setOrgExpirationDate(data.getOrgExpDate());
		model.setPurchaseDate(data.getPurchaseDate());
		model.setPurchaseOrderId(data.getPurchaseOrderId());
		model.setRemainingDlvs(data.getRemainingDlvs());
		model.setStatus(EnumDlvPassStatus.getEnum(data.getStatus()));
		model.setTotalNoOfDlvs(data.getTotalNoOfDlvs());
		model.setType(buildDeliveryPassType(data.getType()));
		model.setUsageCount(data.getUsageCount());
		return model;
	}

	public static List<DeliveryPassModel> buildDeliveryPassModelList(
			List<DeliveryPassData> data) {
		List<DeliveryPassModel> models = new ArrayList<DeliveryPassModel>();
		for (DeliveryPassData deliveryPassData : data) {
			models.add(buildDeliveryPassModel(deliveryPassData));
		}
		return models;
	}

	public static DeliveryPassData buildDeliveryPassData(DeliveryPassModel model) {
		DeliveryPassData data = new DeliveryPassData();
		data.setId(model.getId());
		data.setAmount(model.getAmount());
		data.setCustomerId(model.getCustomerId());
		data.setDescription(model.getDescription());
		data.setExpDate(model.getExpirationDate());
		data.setNoOfCredits(model.getNoOfCredits());
		data.setOrgExpDate(model.getOrgExpirationDate());
		data.setPurchaseDate(model.getPurchaseDate());
		data.setPurchaseOrderId(model.getPurchaseOrderId());
		data.setRemainingDlvs(model.getRemainingDlvs());
		data.setStatus(model.getStatus().getName());
		data.setTotalNoOfDlvs(model.getTotalNoOfDlvs());
		data.setType(buildDeliveryPassType(model.getType()));
		data.setUsageCount(model.getUsageCount());
		return data;
	}
	
	public static DeliveryPassTypeData buildDeliveryPassType(
			DeliveryPassType type) {
		if (type != null) {
			DeliveryPassTypeData data = new DeliveryPassTypeData();
			data.setAutoRenewalSKU(type.getAutoRenewalSKU());
			data.setAutoRenewDP(type.isAutoRenewDP());
			data.setCode(type.getCode());
			data.setDuration(type.getDuration());
			data.setFreeTrialDP(type.isFreeTrialDP());
			data.setFreeTrialRestricted(type.isFreeTrialRestricted());
			data.setName(type.getName());
			data.setNoOfDeliveries(type.getNoOfDeliveries());
			data.setProfileValue(type.getProfileValue());
			data.setUnlimited(type.isUnlimited());
			if (type.getDeliveryTypes() != null) {
				List<String> deliveryTypes = new ArrayList<String>();
				for (EnumDeliveryType deliveryType : type.getDeliveryTypes()) {
					deliveryTypes.add(deliveryType.getCode());
				}
				data.setDeliveryTypes(deliveryTypes);
			}
			if (type.geteStoreIds() != null) {
				List<String> eStoreIds = new ArrayList<String>();
				for (EnumEStoreId eStoreId : type.geteStoreIds()) {
					eStoreIds.add(eStoreId.getContentId());
				}
				data.setEStoreIds(eStoreIds);
			}
			return data;
		}
		return null;
	}

	public static FDCouponActivityLogData buildCouponActivityData(
			FDCouponActivityLogModel log) {
		FDCouponActivityLogData logData = new FDCouponActivityLogData();
		
		logData.setCouponId(log.getCouponId());
		logData.setCustomerId(log.getCustomerId());
		logData.setDetails(log.getDetails());
		logData.setEndTime(log.getEndTime());
		logData.setFdUserId(log.getFdUserId());
		logData.setInitiator(log.getInitiator());
		logData.setSaleId(log.getSaleId());
		logData.setSource(log.getSource().getCode());
		logData.setStartTime(log.getStartTime());
		logData.setTransType(log.getTransType().getName());
		
		return logData;
	}

	public static ErpMaterialInfoModel convertErpMaterialInfoDataToModel(
			ErpMaterialInfoModelData data) {
		ErpMaterialInfoModel model = new ErpMaterialInfoModel();
		model.setAttributes(buildAttributes(data.getAttributes()));
		model.setDescription(data.getDescription());
//		model.setPK(new PrimaryKey(data.getPk()));
		model.setId(data.getPk());
		model.setSapId(data.getSapId());
		return model;
	}

	public  static AttributeCollection buildAttributes(
			AttributeCollectionData attribute) {
		if(attribute==null)
			return null;
		AttributeCollection attributes = new AttributeCollection(attribute.getAttributes());
		return attributes;
	}

	public static List<ErpZoneMasterInfo> buildErpMasterInfoList(List<ErpMasterInfoData> data) {
		List<ErpZoneMasterInfo> zoneMasterInfoList = new ArrayList<ErpZoneMasterInfo>();
		for (ErpMasterInfoData erpMasterInfoData : data) {
			zoneMasterInfoList.add(buildErpMasterInfo(erpMasterInfoData));
		}
		return zoneMasterInfoList;
	}

	public static ErpZoneMasterInfo buildErpMasterInfo(ErpMasterInfoData erpMasterInfoData) {
		if(erpMasterInfoData != null){
		ErpZoneMasterInfo erpZoneMasterInfo = new ErpZoneMasterInfo(erpMasterInfoData.getSapId(), buildErpZoneRegionInfo(erpMasterInfoData.getRegion()), EnumZoneServiceType.getEnumByCode(erpMasterInfoData.getServiceType()),
				erpMasterInfoData.getDescription());
		erpZoneMasterInfo.setDefault(erpMasterInfoData.isDefault());
		erpZoneMasterInfo.setId(erpMasterInfoData.getId());
		erpZoneMasterInfo.setParentZone(buildErpMasterInfo(erpMasterInfoData.getParentZone()));
		erpZoneMasterInfo.setVersion(erpMasterInfoData.getVersion());
		return erpZoneMasterInfo;
		}
		return null;
	}

	private static ErpZoneRegionInfo buildErpZoneRegionInfo(ErpZoneRegionInfoData region) {
		ErpZoneRegionInfo regionInfo = new ErpZoneRegionInfo(region.getSapId(), region.getDescription());
		regionInfo.setId(region.getId());
		regionInfo.setVersion(region.getVersion());
		return regionInfo;
	}

	public static FDGatewayActivityLogModel getFDGatewayGatewayActivityLogModel(
			GatewayType gatewayType,
			com.freshdirect.payment.gateway.Response response) {
		FDGatewayActivityLogModel logModel=new FDGatewayActivityLogModel();
		logModel.setTransactionType(response.getTransactionType());
		logModel.setGatewayType(gatewayType);
		logModel.setApproved(response.isApproved());
		logModel.setAuthCode(response.getAuthCode());
		logModel.setAVSMatch(response.isAVSMatch());
		logModel.setAvsResponse(response.getAVSResponse());
		logModel.setProcessingError(response.isError());
		logModel.setDeclined(response.isDeclined());
		logModel.setRequestProcessed(response.isRequestProcessed());
		logModel.setResponseCode(response.getResponseCode());
		logModel.setResponseCodeAlt(response.getResponseCodeAlt());
		logModel.setStatusCode(response.getStatusCode());
		logModel.setStatusMsg(response.getStatusMessage());
		
		if(TransactionType.CC_VERIFY.equals(response.getTransactionType())) {
			logModel.setCVVMatch(response.isCVVMatch());
			logModel.setCvvResponse(response.getCVVResponse());
		}
		BillingInfo billingInfo=response.getBillingInfo();
		if(billingInfo==null)
			billingInfo=response.getRequest().getBillingInfo();
		PaymentMethod pm=null;
		if(billingInfo!=null) {
			pm=billingInfo.getPaymentMethod();
			logModel.setAmount(billingInfo.getAmount());
			if (billingInfo.getMerchant()!=null)
				logModel.setMerchant(billingInfo.getMerchant().name());
			logModel.setGatewayOrderID(billingInfo.getTransactionID());
			logModel.setTxRefNum(billingInfo.getTransactionRef());
			logModel.setTxRefIdx(billingInfo.getTransactionRefIndex());
			if(StringUtil.isEmpty(pm.getMaskedAccountNumber())){
				pm=response.getRequest().getBillingInfo().getPaymentMethod();
			}
			if(pm!=null && !StringUtil.isEmpty(pm.getMaskedAccountNumber())) {
				int l=pm.getMaskedAccountNumber().length();
				logModel.setAccountNumLast4(l>4?
						pm.getMaskedAccountNumber().substring(l-4,l):pm.getMaskedAccountNumber());
				logModel.setCustomerId(pm.getCustomerID());
				logModel.setProfileId(pm.getBillingProfileID());
				logModel.setCustomerName(pm.getCustomerName());
				logModel.setAddressLine1(pm.getAddressLine1());
				logModel.setAddressLine2(pm.getAddressLine2());
				logModel.setCity(pm.getCity());
				logModel.setState(pm.getState());
				logModel.setCountryCode(pm.getCountry());
				logModel.setZipCode(pm.getZipCode());
				logModel.setPaymentType(pm.getType());
				if(PaymentMethodType.CREDIT_CARD.equals(pm.getType())) {
				   CreditCard cc=(CreditCard)pm;
					logModel.setCardType(cc.getCreditCardType());
					logModel.setExpirationDate(cc.getExpirationDate());
				}else if (PaymentMethodType.PP.equals(pm.getType())) {
					logModel.setCardType(CreditCardType.PYPL);
				}
				else if(PaymentMethodType.ECHECK.equals(pm.getType())){
					ECheck ec=(ECheck)pm;
					logModel.setBankAccountType(ec.getBankAccountType());
				}
				
				if(pm.getEwalletId() != null && pm.getEwalletId().equals(""+EnumEwalletType.PP.getValue())){
					logModel.setDeviceId(pm.getDeviceId());
				}
				// Update EWallet ID
				logModel.seteWalletId(response.getEwalletId());
				logModel.seteWalletTxId(response.getEwalletTxId());
				logModel.setEStoreId(billingInfo.getEStoreId());
			}
			
		}
		return logModel;
		
	}

	public static ErpCustEWalletModel buildErpCustEwalletModel(ErpCustEWalletData data) {
		ErpCustEWalletModel erpCustEwalletModel = new ErpCustEWalletModel();
		erpCustEwalletModel.setCustomerId(data.getCustomerId());
		erpCustEwalletModel.setEmailId(data.getEmailId());
		erpCustEwalletModel.seteWalletId(data.geteWalletId());
		if(data.getId() != null)
		erpCustEwalletModel.setId(data.getId());
		erpCustEwalletModel.setLongAccessToken(data.getLongAccessToken());
		return erpCustEwalletModel;
	}

	public static FDAvailabilityI buildAvailableModelFromData(
			FDAvailabilityData fdAvailabilityData) {
		
		if(fdAvailabilityData.getAvailableType().equals("FDStockAvailability")){
		return buildfDStockAvailability(fdAvailabilityData);
		}else if(fdAvailabilityData.getAvailableType().equals("FDCompositeAvailability")){
		return buildfDCompositeAvailability(fdAvailabilityData);
		}else if(fdAvailabilityData.getAvailableType().equals("FDMuniAvailability")){
			return buildFDMuniAvailability(fdAvailabilityData);
		}else if(fdAvailabilityData.getAvailableType().equals("FDStatusAvailability")){
			return buildFDStatusAvailability(fdAvailabilityData);
		}else if(fdAvailabilityData.getAvailableType().equals("NullAvailability")){
			return buildNullAvailability(fdAvailabilityData);
		}
		return null;

	}
	
	public static FDAvailabilityI buildNullAvailability(FDAvailabilityData data) {
		if(data==null)
		return null;
		FDAvailabilityI valuee = new FDStockAvailability(convertErpInventoryDataToModel(data.getInventory()),  data.getReqQty(),  data.getMinQty(),  data.getQtyInc());
				
		return valuee;
	}

	public static FDAvailabilityI buildFDStatusAvailability(FDAvailabilityData data) {
		if(data==null)
		return null;
		FDAvailabilityI valuee = new FDStatusAvailability(EnumAvailabilityStatus.getEnumByStatusCode(data.getStatus()),buildAvailableModelFromData(data.getAvailability()));
				
		return valuee;
	
	}

	public static FDAvailabilityI buildFDMuniAvailability(FDAvailabilityData data) {
		if(data==null)
		return null;
		FDAvailabilityI valuee = new FDStockAvailability(convertErpInventoryDataToModel(data.getInventory()),  data.getReqQty(),  data.getMinQty(),  data.getQtyInc());
				
		return valuee;
	
	}

	public static FDAvailabilityI buildfDCompositeAvailability(FDAvailabilityData data) {
		if(data==null)
		return null;
		FDAvailabilityI valuee = null;
		for(String key:data.getAvailabilities().keySet()){
			Map<String,FDAvailabilityI> results = new HashMap();
			results.put(key, buildAvailableModelFromData(data.getAvailabilities().get(key)));
			 valuee = new FDCompositeAvailability(results);

		}

		return valuee;
	}

	public static FDAvailabilityI buildfDStockAvailability(FDAvailabilityData data) {
		if(data==null)
		return null;
		FDAvailabilityI valuee = new FDStockAvailability(convertErpInventoryDataToModel(data.getInventory()),  data.getReqQty(),  data.getMinQty(),  data.getQtyInc());
				
		return valuee;
	}

	public static ErpSaleModel buildErpSaleData(ErpSaleData data) {
		System.out.println("Creating abstractmodel $$$$");
		
		PrimaryKey customerPk = new PrimaryKey(data.getCustomerPk());
		EnumSaleStatus status = EnumSaleStatus.getSaleStatus(data.getStatus());
		String sapOrderNumber = data.getSapOrderNumber();
		ErpShippingInfo shippingInfo = new ErpShippingInfo(data.getShippingInfo().getWaveNumber(),
				data.getShippingInfo().getTruckNumber(), data.getShippingInfo().getStopSequence(), 
				data.getShippingInfo().getRegularCartons(), data.getShippingInfo().getFreezerCartons(), 
				data.getShippingInfo().getAlcoholCartons());
		
		String dlvPassId = data.getDeliveryPassId();
		String _saleType = data.getType();
		String standingOrderId = data.getStandingOrderId();
		EnumSaleType saleType=EnumSaleType.REGULAR;
		if(_saleType!=null) {
			saleType=EnumSaleType.getSaleType(_saleType);
		}
		String eStoreKey = data.geteStoreId();
		
		String in_modify = "X".equalsIgnoreCase(data.getIn_modify())?"YES":"NO";
		Date lock_timestamp = data.getLock_timestamp();
		EnumEStoreId eStoreId = (null ==eStoreKey||"".equals(eStoreKey))?EnumEStoreId.FD:EnumEStoreId.valueOfContentId(eStoreKey);
		
		boolean hasSignature = data.isHasSignature();
		
		
		String soName = data.getStandingOrderName();
		
		// load children

		List<ErpTransactionModel> txList = getTransactions(data.getTransactions());
		
		List<ErpComplaintModel> complaintList = getComplaints(data.getComplaints());

		Set<String> usedPromotionCodes = data.getUsedPromotionCodes();
		

		List<ErpCartonInfo> cartonInfo = null; 
		/*if(model.getCartonInfo()!=null && model.getCartonInfo().size()>0){
			cartonInfo = model.getCartonInfo();
		}else{*/
			cartonInfo =buildCartonInfoList( data.getCartonInfo());
		//}
		 		
		ErpSaleModel erpSaleModel = new ErpSaleModel(customerPk, status, txList, complaintList, sapOrderNumber, shippingInfo, usedPromotionCodes, 
				cartonInfo, dlvPassId, saleType, standingOrderId, hasSignature, eStoreId, soName, in_modify, lock_timestamp);
		erpSaleModel.setPK(new PrimaryKey(data.getCustomerPk()));
			
		erpSaleModel.setCartonMetrics(data.getCartonMetrics());		

		return erpSaleModel;
	}

	private static List<ErpCartonInfo> buildCartonInfoList(
			List<ErpCartonInfoData> cartonInfo) {
		List<ErpCartonInfo> modelList = new ArrayList();
		for(ErpCartonInfoData data:cartonInfo){
			ErpCartonInfo model = new ErpCartonInfo(data.getOrderNumber(), data.getSapNumber(), data.getCartonNumber(), data.getCartonType());
			model.setDetails(buildCartonDetailList(data.getDetails()));
			modelList.add(model);

		}
		
		return modelList;
	}

	private static List<ErpCartonDetails> buildCartonDetailList(
			List<ErpCartonDetailsData> details) {
		List<ErpCartonDetails> modelList = new ArrayList();
		for(ErpCartonDetailsData data:details){
			
		
			ErpCartonInfo erpCartonInfo = new ErpCartonInfo(data.getCartonInfo().getOrderNumber(), data.getCartonInfo().getSapNumber(), data.getCartonInfo().getCartonNumber(), data.getCartonInfo().getCartonType());
			erpCartonInfo.setDetails(buildCartonDetailList(data.getCartonInfo().getDetails()));
			
			ErpCartonDetails model = new ErpCartonDetails(
				erpCartonInfo,
				data.getOrderLineNumber(),
				data.getMaterialNumber(),
				data.getBarcode(),
				data.getActualQuantity(),
				data.getNetWeight(),
				Double.toString(data.getNetWeight())) ;
		}
		return modelList;
	}
	


	private static List<ErpComplaintModel> getComplaints(
			List<ErpComplaintData> complaints) {
		List<ErpComplaintModel> models = new ArrayList();
		for(ErpComplaintData data: complaints){
		ErpComplaintModel complaintModel = new ErpComplaintModel();
		complaintModel.setApprovedBy(data.getApprovedBy());
		complaintModel.setApprovedDate(data.getApprovedDate());
		complaintModel.setAutoCaseId(data.getAutoCaseId());
		complaintModel.setCreateDate(data.getCreateDate());
		complaintModel.setCreatedBy(data.getCreatedBy());
		if(data.getCustomer_email()!=null){
		ErpCustomerEmailModel model = new ErpCustomerEmailModel();
		model.setCustomMessage(data.getCustomer_email().getCustomMessage());
		model.setSignature(data.getCustomer_email().getSignature());
		model.setMailSent(data.getCustomer_email().isMailSent());
		complaintModel.setCustomerEmail(model);
		}
		}
		return models;
	}

	private static List<ErpTransactionModel> getTransactions(List<ErpTransactionData> transactions) {
		List<ErpTransactionModel> modelList = new ArrayList();

		for (ErpTransactionData entity : transactions) {
			EnumTransactionType txType = EnumTransactionType.getTransactionType(entity.getTransactionType());
				try {
					modelList.add(getTransaction(entity));
				} catch (ErpTransactionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return modelList;
	}
	
	private static ErpTransactionModel getTransaction(ErpTransactionData entity) throws ErpTransactionException {
		PrimaryKey ppk = new PrimaryKey(entity.getCustomerId());
		EnumTransactionType txType = EnumTransactionType.getTransactionType(entity.getTransactionType());
		

		/*if (EnumTransactionType.PREAUTH_GIFTCARD.equals(txType)) {
			return preAuthTransaction.getModel(entity);
		}
		if (EnumTransactionType.REVERSEAUTH_GIFTCARD.equals(txType)) {
			return reverseAuthTransaction.getModel(entity);
		}
		if (EnumTransactionType.POSTAUTH_GIFTCARD.equals(txType)) {
			return postAuthTransaction.getModel(entity);
		}
		if (EnumTransactionType.BALANCETRANSFER_GIFTCARD.equals(txType)) {
			return balanceTransfer.getModel(entity);
		}*/
		if(EnumTransactionType.CREATE_ORDER.equals(txType)){
			ErpAbstractOrderModel model = new ErpCreateOrderModel();
			return createOrderTransaction(entity,model);
		}
		if (EnumTransactionType.MODIFY_ORDER.equals(txType)) {
			ErpModifyOrderModel model = new ErpModifyOrderModel();
			return modifyOrderTransaction(entity,model);
		}
		/*if (EnumTransactionType.INVOICE.equals(txType)) {
			return invoiceTransaction.getModel(entity);
		}
		if (EnumTransactionType.AUTHORIZATION.equals(txType)) {
			return authorizationTransaction.getModel(entity);
		}
		if (EnumTransactionType.CAPTURE.equals(txType)) {
			return captureTransaction.getModel(entity);
		}
		if (EnumTransactionType.SUBMIT_FAILED.equals(txType)) {
			return submitFailedTransaction.getModel(entity);
		}
		if (EnumTransactionType.REVERSAL.equals(txType)) {
			return paymentMethodTransaction.getModel(entity, new ErpReversalModel());
		}
		if (EnumTransactionType.CASHBACK.equals(txType)) {
			return cashbackTransaction.getModel(entity);
		}
		if (EnumTransactionType.SETTLEMENT.equals(txType)) {
			return settlementTransaction.getModel(entity);
		}
		if (EnumTransactionType.CHARGEBACK.equals(txType)) {
			return abstractChargebackTransaction.getModel(entity, new ErpChargebackModel());
		}
		if (EnumTransactionType.ADJUSTMENT.equals(txType)) {
			return adjustmentTransaction.getModel(entity);
		}
		if (EnumTransactionType.CHARGE.equals(txType)) {
			return chargeTransaction.getModel(entity);
		}
		if (EnumTransactionType.CANCEL_ORDER.equals(txType)) {
			return cancelOrderTransaction.getModel(entity);
		}
		if (EnumTransactionType.RETURN_ORDER.equals(txType)) {
			return returnOrderTransaction.getModel(entity);
		}
		if (EnumTransactionType.REDELIVERY.equals(txType)) {
			return redeliveryTransaction.getModel(entity);
		}
		if (EnumTransactionType.VOID_CAPTURE.equals(txType)) {
			return voidCaptureTransaction.getModel(entity);
		}
		if (EnumTransactionType.CHARGEBACK_REVERSAL.equals(txType)) {
			return abstractChargebackTransaction.getModel(entity, new ErpChargebackReversalModel());
		}
		if (EnumTransactionType.SETTLEMENT_FAILED.equals(txType)) {
			return abstractSettlementTransaction.getModel(entity, new ErpFailedSettlementModel());
		}
		if (EnumTransactionType.SETTLEMENT_CHARGE.equals(txType)) {
			return abstractSettlementTransaction.getModel(entity, new ErpChargeSettlementModel());
		}
		if (EnumTransactionType.FUNDS_REDEPOSIT.equals(txType)) {
			return abstractSettlementTransaction.getModel(entity, new ErpFundsRedepositModel());
		}
		if (EnumTransactionType.SETTLEMENT_CHARGE_FAILED.equals(txType)) {
			return abstractSettlementTransaction.getModel(entity, new ErpFailedChargeSettlementModel());
		}
		if (EnumTransactionType.INVOICE_CHARGE.equals(txType)) {
			return chargeInvoiceTransaction.getModel(entity);
		}
		if (EnumTransactionType.DELIVERY_CONFIRM.equals(txType)) {
			return deliveryConfirmTransaction.getModel(entity);
		}
		if (EnumTransactionType.REGISTER_GIFTCARD.equals(txType)) {
			return registerGCTransaction.getModel(entity);
		}
		if (EnumTransactionType.EMAIL_GIFTCARD.equals(txType)) {
			return emailgiftCard.getModel(entity);
		}
		if (EnumTransactionType.GIFTCARD_DLV_CONFIRM.equals(txType)) {
			return giftCardDlvConfirmation.getModel(entity);
		}*/
		throw new ErpTransactionException("Unknown transaction type " + txType.getCode() + " encountered in sale " + ppk);
	
	}

	private static ErpTransactionModel modifyOrderTransaction(
			ErpTransactionData entity, ErpModifyOrderModel model) {
		
		return createOrderTransaction(entity,model);
	}

	private static ErpTransactionModel createOrderTransaction(
			ErpTransactionData entity, ErpAbstractOrderModel model) {

		model.setCustomerId(entity.getCustomerId());
		model.setId(entity.getCustomerId());
		model.setTransactionDate(entity.getTransactionDate());
		model.setTransactionSource(EnumTransactionSource.getTransactionSource(entity.getTransactionSource()));
		model.setPricingDate(entity.getPricingDate());
		model.setRequestedDate(entity.getRequestedDate());
		model.setSubTotal(entity.getSubTotal());
		model.setTax(entity.getTax());
		model.setCustomerServiceMessage(entity.getCustomerServiceMessage());
		model.setMarketingMessage(entity.getMarketingMessage());
		model.setTransactionInitiator(entity.getTransactionInitiator());
		model.setGlCode(entity.getGlCode());
		model.setBufferAmt(entity.getBufferAmt());
		String taxationType = entity.getTaxationType();
		model.setTaxationType((null!=taxationType && !"".equals(taxationType))?EnumNotificationType.getNotificationType(taxationType):null);
		
		model.setOrderLines(SapGatewayConverter.buildOrderLine(entity.getOrderLines()));

		// load customer info
		model.setDeliveryInfo(SapGatewayConverter.buildDeliveryInfo(entity.getDeliveryInfo()));
		// load payment info
		model.setPaymentMethod(SapGatewayConverter.buildPaymentMethodModel(entity.getPaymentMethod()));
		model.setAppliedCredits(SapGatewayConverter.buildAppliedCredits(entity.getAppliedCredits()));
		model.setCharges(SapGatewayConverter.buildChargeLineModel(entity.getCharges()));

		model.setDiscounts(SapGatewayConverter.buildDiscountDataList(entity.getDiscounts()));
		
//		model.setRecepientsList(SapGatewayConverter.buildRecepientsList(entity.getRecipientList()));
//		model.setAppliedGiftcards(SapGatewayConverter.buildAppliedGiftCards(entity.get));
		if(entity.getDiscount()!=null){
		ErpDiscountLineModel discountLineModel = new ErpDiscountLineModel(SapGatewayConverter.buildDiscount(entity.getDiscount()));
//		discountLineModel.setId(entity.getDiscount().getId());
		model.addDiscount(discountLineModel);
		}
		model.setRafTransModel(SapGatewayConverter.buildRefTransModel(entity.getRafTransModel()));
		
		return model;
	}
}

