package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.ecommerce.data.delivery.sms.SmsOrderData;
import com.freshdirect.ecommerce.data.enums.BillingCountryInfoData;
import com.freshdirect.ecommerce.data.enums.CrmCaseSubjectData;
import com.freshdirect.ecommerce.data.enums.DeliveryPassTypeData;
import com.freshdirect.ecommerce.data.enums.EnumFeaturedHeaderTypeData;
import com.freshdirect.ecommerce.data.enums.ErpAffiliateData;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.ecommerce.data.rules.RuleData;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
import com.freshdirect.rules.Rule;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
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
import com.freshdirect.ecommerce.data.payment.FDGatewayActivityLogModelData;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.GrpZonePriceListing;
import com.freshdirect.fdstore.GrpZonePriceModel;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;


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
				erpProductInfoModelData.isAlcohol());

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
					EnumATPRule.getEnum(materialPlants[i].getAtpRule().getName()), 
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
						materialPrices[i].getUnit(), 
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


}