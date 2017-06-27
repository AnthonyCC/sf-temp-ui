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
import java.util.SortedSet;
import java.util.TreeMap;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.ZoneInfo;
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
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.customer.ErpActivityRecordData;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.ecommerce.data.delivery.sms.SmsOrderData;
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
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryEntryData;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicData;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicValueData;
import com.freshdirect.ecommerce.data.erp.material.ErpCharacteristicValuePriceData;
import com.freshdirect.ecommerce.data.erp.material.ErpClassData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialData;
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
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.fdstore.GroupScalePricingData;
import com.freshdirect.ecommerce.data.fdstore.GrpZonePriceListingData;
import com.freshdirect.ecommerce.data.fdstore.GrpZonePriceListingWrapper;
import com.freshdirect.ecommerce.data.fdstore.GrpZonePriceModelData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoData;
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.ecommerce.data.mail.EmailAddressData;
import com.freshdirect.ecommerce.data.mail.EmailDataI;
import com.freshdirect.ecommerce.data.mail.XMLEmailDataI;
import com.freshdirect.ecommerce.data.payment.FDGatewayActivityLogModelData;
import com.freshdirect.ecommerce.data.rules.RuleData;
import com.freshdirect.ecommerce.data.security.TicketData;
import com.freshdirect.ecommerce.data.utill.DayOfWeekSetData;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpCharacteristicValueModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.GrpZonePriceListing;
import com.freshdirect.fdstore.GrpZonePriceModel;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionDetailModel;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
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
			l.add(new DeliveryPassType(deliveryPassTypeData.getCode(),
					deliveryPassTypeData.getName(),
					deliveryPassTypeData.getNoOfDeliveries(),
					deliveryPassTypeData.getDuration(),
					deliveryPassTypeData.isUnlimited(),
					deliveryPassTypeData.getProfileValue(),
					deliveryPassTypeData.isAutoRenewDP(),
					deliveryPassTypeData.isFreeTrialDP(),
					deliveryPassTypeData.isFreeTrialDP(),
					deliveryPassTypeData.getAutoRenewalSKU()));
		}

		return l;
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
		Map<ErpCOOLKey, ErpCOOLInfo> erpCOOLInfo=null;
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
		 List<FDProductPromotionInfo> data = new ArrayList();
		 for(FDProductPromotionInfoData obj:value){
			 SalesAreaInfo sAinfo = new SalesAreaInfo(obj.getSalesArea().getSalesOrg(),obj.getSalesArea().getDistChannel());
			 FDProductPromotionInfo model = new FDProductPromotionInfo();
			 model.setErpCategory(obj.getErpCategory());
			 model.setErpCatPosition(obj.getErpCatPosition());
			 model.setErpDeptId(obj.getErpDeptId());
			 model.setErpPromtoionId(obj.getErpPromtoionId());
			 model.setFeaturedHeader(obj.getFeaturedHeader());
			 model.setId(obj.getId());
			 model.setMatNumber(obj.getMatNumber());
			 model.setPriority(obj.getPriority());
			 model.setSalesArea(sAinfo);
			 model.setSkuCode(obj.getSkuCode());
			 model.setType(obj.getType());
			 model.setVersion(obj.getVersion());
			 model.setZoneId(obj.getZoneId());
			 data.add(model);
		 }
		return data;
	}

	public static ErpProductPromotionPreviewInfo buildErpProductPromotionPreviewInfo(
			ErpProductPromotionPreviewInfoData data) {
    	ErpProductPromotionPreviewInfo response = new ErpProductPromotionPreviewInfo();
    	Map<String,ErpProductInfoModel> prodInfoMod = new HashMap();
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
				buildMaterialPrice(erpProductInfoModelData.getMaterialPrices()), 
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
				for(Iterator it = set.iterator(); it.hasNext();){int elem = (Integer) it.next();days[j]=elem;j++;}
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


	public static ErpMaterialPrice[] buildMaterialPrice(
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
		fdGatewayActivityLogModelData.setBankAccountType(activityLogModel.getBankAccountType().name());
		fdGatewayActivityLogModelData.setCardType(activityLogModel.getCardType().name());
		fdGatewayActivityLogModelData.setCity(activityLogModel.getCity());
		fdGatewayActivityLogModelData.setCountryCode(activityLogModel.getCountryCode());
		fdGatewayActivityLogModelData.setCustomerId(activityLogModel.getCustomerId());
		fdGatewayActivityLogModelData.setCVVMatch(activityLogModel.isCVVMatch());
		fdGatewayActivityLogModelData.setCvvResponse(activityLogModel.getCvvResponse());
		fdGatewayActivityLogModelData.setDeclined(activityLogModel.isDeclined());
		fdGatewayActivityLogModelData.setDeviceId(activityLogModel.getDeviceId());
		fdGatewayActivityLogModelData.seteStoreId(activityLogModel.getEStoreId().name());
		fdGatewayActivityLogModelData.seteWalletId(activityLogModel.geteWalletId());
		fdGatewayActivityLogModelData.seteWalletTxId(activityLogModel.geteWalletTxId());
		fdGatewayActivityLogModelData.setExpirationDate(activityLogModel.getExpirationDate());
		fdGatewayActivityLogModelData.setGatewayOrderID(activityLogModel.getGatewayOrderID());
		fdGatewayActivityLogModelData.setGatewayType(activityLogModel.getGatewayType().getName());
		fdGatewayActivityLogModelData.setMerchant(activityLogModel.getMerchant());
		fdGatewayActivityLogModelData.setPaymentType(activityLogModel.getPaymentType().name());
		fdGatewayActivityLogModelData.setProcessingError(activityLogModel.isProcessingError());
		fdGatewayActivityLogModelData.setProfileId(activityLogModel.getProfileId());
		fdGatewayActivityLogModelData.setRequestProcessed(activityLogModel.isRequestProcessed());
		fdGatewayActivityLogModelData.setResponseCode(activityLogModel.getResponseCode());
		fdGatewayActivityLogModelData.setResponseCodeAlt(activityLogModel.getResponseCodeAlt());
		fdGatewayActivityLogModelData.setState(activityLogModel.getState());
		fdGatewayActivityLogModelData.setStatusCode(activityLogModel.getStatusCode());
		fdGatewayActivityLogModelData.setStatusMsg(activityLogModel.getStatusMsg());
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
		erpActivityRecordData.setNote(template.getNote());
		erpActivityRecordData.setDate(template.getDate());
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
		
		data.setId(model.getId());
		data.setBaseUnit(model.getBaseUnit());
		data.setUPC(model.getUPC());
		data.setDescription(model.getDescription());
		data.setQuantityCharacteristic(model.getQuantityCharacteristic());
		data.setSalesUnitCharacteristic(model.getSalesUnitCharacteristic());
		data.setMaterialGroup(model.getMaterialGroup());
		data.setSapId(model.getSapId());
		data.setSalesUnits(getErpSaleUnitModelList(model.getSalesUnits()));
		data.setAlcoholicContent(model.getAlcoholicContent().getCode());
		data.setTaxable(model.isTaxable());
		data.setTaxCode(model.getTaxCode());
		data.setDisplaySalesUnits(getErpSaleUnitModelList(model.getDisplaySalesUnits()));
		data.setSkuCode(model.getSkuCode());
		data.setDaysFresh(model.getDaysFresh());
		data.setMaterialType( model.getMaterialType());
		data.setApprovalStatus(model.getApprovalStatus().getStatusCode());

		data.setPrices(erpMaterialPriceDatas);
		data.setClasses(erpClassDatas);
		data.setDisplaySalesUnits(displaySalesUnitDatas);
		data.setMaterialPlants(erpPlantMaterialDatas);
		data.setMaterialSalesAreas(erpMaterialSalesAreaDatas);
		data.setSalesUnits(salesUnitDatas);
		return data;
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
		data.setId(erpMaterialSalesAreaModel.getId());
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
		data.setId(erpPlantMaterialModel.getId());
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
		data.setId(erpSalesUnitModel.getId());
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
		data.setId(erpMaterialPriceModel.getId());
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

	public static CartCouponData convertCartCouponData(CouponCart couponCart, FDCouponActivityContext context) {
		CartCouponData cartCouponData = new CartCouponData();
		CouponCartData couponCartData = new CouponCartData();
		couponCartData.setOrderId(couponCart.getOrderId());
		couponCartData.setTranType(couponCart.getTranType().getName());
		FDCouponCustomerData fdCouponCustomerData = new FDCouponCustomerData();
		if (couponCart.getCouponCustomer() != null) {
			fdCouponCustomerData.setCouponCustomerId(couponCart.getCouponCustomer().getCouponCustomerId());
			fdCouponCustomerData.setCouponUserId(couponCart.getCouponCustomer().getCouponUserId());
			fdCouponCustomerData.setZipCode(couponCart.getCouponCustomer().getZipCode());
		}

		couponCartData.setCouponCustomer(fdCouponCustomerData);

		List<ErpOrderLineModelData> erpOrderLineModelDatas = new ArrayList<ErpOrderLineModelData>();
		if (couponCart.getOrderLines() != null) {
			for (ErpOrderLineModel erpOrderLineModel : couponCart.getOrderLines()) {
				ErpOrderLineModelData erpOrderLineModelData = new ErpOrderLineModelData();
				if (erpOrderLineModel.getAddedFrom() != null)
					erpOrderLineModelData.setAddedFrom(erpOrderLineModel.getAddedFrom().getName());
				erpOrderLineModelData.setAddedFromSearch(erpOrderLineModel.isAddedFromSearch());
				erpOrderLineModelData.setAffiliateCode(erpOrderLineModel.getAffiliate().getCode());//
				erpOrderLineModelData.setAlcohol(erpOrderLineModel.isAlcohol());
				erpOrderLineModelData.setBeer(erpOrderLineModel.isBeer());
				erpOrderLineModelData.setCartLineId(erpOrderLineModel.getCartlineId());
				if (erpOrderLineModel.getConfiguration() != null) {
					FDConfigurationData fdConfigurationData = new FDConfigurationData();
					fdConfigurationData.setQuantity(erpOrderLineModel.getConfiguration().getQuantity());
					fdConfigurationData.setSalesUnit(erpOrderLineModel.getConfiguration().getSalesUnit());
					fdConfigurationData.setOptions(erpOrderLineModel.getConfiguration().getOptions());
					erpOrderLineModelData.setConfiguration(fdConfigurationData);
				}
				erpOrderLineModelData.setConfigurationDesc(erpOrderLineModel.getConfigurationDesc());
				erpOrderLineModelData.setCoremetricsPageContentHierarchy(erpOrderLineModel.getCoremetricsPageContentHierarchy());
				erpOrderLineModelData.setCoremetricsPageId(erpOrderLineModel.getCoremetricsPageId());
				erpOrderLineModelData.setCoremetricsVirtualCategory(erpOrderLineModel.getCoremetricsVirtualCategory());
				if(erpOrderLineModel.getCouponDiscount()!=null){
				ErpCouponDiscountLineModelData erpCouponDiscountLineModelData = new ErpCouponDiscountLineModelData();
				erpCouponDiscountLineModelData.setCouponDesc(erpOrderLineModel.getCouponDiscount().getCouponDesc());
				erpCouponDiscountLineModelData.setCouponId(erpOrderLineModel.getCouponDiscount().getCouponId());
				erpCouponDiscountLineModelData.setDiscountAmt(erpOrderLineModel.getCouponDiscount().getDiscountAmt());
				erpCouponDiscountLineModelData.setDiscountType(erpOrderLineModel.getCouponDiscount().getDiscountType().getName());
				erpCouponDiscountLineModelData.setOrderLineId(erpOrderLineModel.getCouponDiscount().getOrderLineId());
				erpCouponDiscountLineModelData.setRequiredQuantity(erpOrderLineModel.getCouponDiscount().getRequiredQuantity());
				erpCouponDiscountLineModelData.setVersion(erpOrderLineModel.getCouponDiscount().getVersion());
				 erpOrderLineModelData.setCouponDiscount(erpCouponDiscountLineModelData);
				}
				 erpOrderLineModelData.setDeliveryPass(erpOrderLineModelData.isDeliveryPass());
				erpOrderLineModelData.setDepartmentDesc(erpOrderLineModel.getDepartmentDesc());
				erpOrderLineModelData.setDepartmentDesc(erpOrderLineModel.getDepartmentDesc());
				erpOrderLineModelData.setDepositValue(erpOrderLineModel.getDepositValue());
				erpOrderLineModelData.setDescription(erpOrderLineModel.getDescription());
				if(erpOrderLineModel.getDiscount() != null){
					DiscountData discountData = new  DiscountData();
					discountData.setAmount(erpOrderLineModel.getDiscount().getAmount());
//					discountData.setDiscountType(erpOrderLineModel.getDiscount().getDiscountType().getId());
					discountData.setMaxPercentageDiscount(erpOrderLineModel.getDiscount().getMaxPercentageDiscount());
					discountData.setPromotionCode(erpOrderLineModel.getDiscount().getPromotionCode());
					discountData.setPromotionDescription(erpOrderLineModel.getDiscount().getPromotionDescription());
					discountData.setSkuLimit(erpOrderLineModel.getDiscount().getSkuLimit());
					erpOrderLineModelData.setDiscount(discountData);
				}
				erpOrderLineModelData.setDiscountAmount(erpOrderLineModel.getDiscountAmount());
				erpOrderLineModelData.setDiscountApplied(erpOrderLineModel.isDiscountFlag());
				erpOrderLineModelData.setDistChannel(erpOrderLineModel.getDistChannel());
				if (erpOrderLineModel.getEStoreId() != null)
					erpOrderLineModelData.seteStoreId(erpOrderLineModel.getEStoreId().getContentId());
				//erpOrderLineModelData.setExternalAgency(erpOrderLineModel.getExternalAgency());
				erpOrderLineModelData.setExternalGroup(erpOrderLineModel.getExternalGroup());
				erpOrderLineModelData.setExternalSource(erpOrderLineModel.getExternalSource());
				if(erpOrderLineModel.getFDGroup() != null){
				FDGroupData groupdata = new FDGroupData();
				groupdata.setGroupId(erpOrderLineModel.getFDGroup().getGroupId());
				groupdata.setSkipProductPriceValidation(erpOrderLineModel.getFDGroup().isSkipProductPriceValidation());
				groupdata.setVersion(erpOrderLineModel.getFDGroup().getVersion());
//				erpOrderLineModelData.setGroup(groupdata);
				}
				erpOrderLineModelData.setGrpQuantity(erpOrderLineModel.getGroupQuantity());
				erpOrderLineModelData.setMaterialGroup(erpOrderLineModel.getMaterialGroup());
				erpOrderLineModelData.setMaterialNumber(erpOrderLineModel.getMaterialNumber());
				erpOrderLineModelData.setOrderLineId(erpOrderLineModel.getOrderLineId());
				erpOrderLineModelData.setOrderLineNumber(erpOrderLineModel.getOrderLineNumber());
				erpOrderLineModelData.setPerishable(erpOrderLineModel.isPerishable());
				erpOrderLineModelData.setPlantID(erpOrderLineModel.getPlantID());
				erpOrderLineModelData.setPrice(erpOrderLineModel.getPrice());
				if(erpOrderLineModel.getProduceRating() != null)
				erpOrderLineModelData.setProduceRating(erpOrderLineModel.getProduceRating().getStatusCode());
				erpOrderLineModelData.setRecipeSourceId(erpOrderLineModel.getRecipeSourceId());
				erpOrderLineModelData.setRequestNotification(erpOrderLineModel.isRequestNotification());
				erpOrderLineModelData.setSalesOrg(erpOrderLineModel.getSalesOrg());
				erpOrderLineModelData.setScaleQuantity(erpOrderLineModel.getScaleQuantity());
				if (erpOrderLineModel.getSku() != null) {
					FDSkuData sku = new FDSkuData();
					sku.setSkuCode(erpOrderLineModel.getSku().getSkuCode());
					sku.setVersion(erpOrderLineModel.getSku().getVersion());
//					erpOrderLineModelData.setSku(sku);
				}
				if (erpOrderLineModel.getSource() != null)
					erpOrderLineModelData.setSource(erpOrderLineModel.getSource().getName());//
				if (erpOrderLineModel.getSustainabilityRating() != null)
					erpOrderLineModelData.setSustainabilityRating(erpOrderLineModel.getSustainabilityRating().getStatusCode());//
				if (erpOrderLineModel.getTaxationType() != null)
					erpOrderLineModelData.setTaxationType(erpOrderLineModel.getTaxationType().getName());
				erpOrderLineModelData.setTaxCode(erpOrderLineModel.getTaxCode());
				erpOrderLineModelData.setTaxRate(erpOrderLineModel.getTaxRate());
				erpOrderLineModelData.setUpc(erpOrderLineModel.getUpc());
				erpOrderLineModelData.setWine(erpOrderLineModel.isWine());

				erpOrderLineModelDatas.add(erpOrderLineModelData);
			}
		}

		couponCartData.setOrderLines(erpOrderLineModelDatas);
		cartCouponData.setCouponCartData(couponCartData);
		return cartCouponData;
	}

	public static CouponOrderData convertCouponOrderData(ErpCouponTransactionModel couponTransModel, FDCouponActivityContext context) {
		// TODO Auto-generated method stub
		return null;
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

	public static EmailDataI buildEmailDataI(EmailI email) {
		EmailDataI emailDataI = new EmailDataI();
		emailDataI.setBCCList(email.getBCCList());
		emailDataI.setCCList(email.getCCList());
		emailDataI.setRecipient(email.getRecipient());
		emailDataI.setSubject(email.getSubject());
		EmailAddressData emailAddressData = new EmailAddressData();
		if (email.getFromAddress() != null) {
			emailAddressData.setAddress(email.getFromAddress().getAddress());
			emailAddressData.setName(email.getFromAddress().getName());
			emailDataI.setFromAddress(emailAddressData);
		}
		if(email instanceof XMLEmailI){
			XMLEmailI xmlEmailI = (XMLEmailI) email;
			XMLEmailDataI xmlEmailDataI = new XMLEmailDataI();
			xmlEmailDataI.setHtmlEmail(xmlEmailI.isHtmlEmail());
			xmlEmailDataI.setXML(xmlEmailDataI.getXML());
			xmlEmailDataI.setXslPath(xmlEmailDataI.getXslPath());
		}
		return emailDataI;
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
	
		
}

