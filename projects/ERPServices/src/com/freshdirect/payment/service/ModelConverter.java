package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.affiliate.ErpAffiliate;
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
import com.freshdirect.ecommerce.data.logger.recommendation.FDRecommendationEventData;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;

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


}
