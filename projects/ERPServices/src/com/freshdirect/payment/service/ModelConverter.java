package com.freshdirect.payment.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.ecommerce.data.enums.BillingCountryInfoData;
import com.freshdirect.ecommerce.data.enums.CrmCaseSubjectData;
import com.freshdirect.ecommerce.data.enums.DeliveryPassTypeData;
import com.freshdirect.ecommerce.data.enums.EnumFeaturedHeaderTypeData;
import com.freshdirect.ecommerce.data.enums.ErpAffiliateData;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;

public class ModelConverter {

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



}
