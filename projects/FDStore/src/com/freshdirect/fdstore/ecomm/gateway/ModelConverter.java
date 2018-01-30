package com.freshdirect.fdstore.ecomm.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import weblogic.utils.collections.TreeMap;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.OneTimeRestrictionData;
import com.freshdirect.ecommerce.data.delivery.OneTimeReverseRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RecurringRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictedAddressModelData;
import com.freshdirect.ecommerce.data.mail.EmailAddressData;
import com.freshdirect.ecommerce.data.mail.TransEmailInfoData;
import com.freshdirect.ecommerce.data.smartstore.CartTabStrategyPriorityData;
import com.freshdirect.ecommerce.data.smartstore.ConfigurationStatusData;
import com.freshdirect.ecommerce.data.smartstore.DynamicSiteFeatureData;
import com.freshdirect.ecommerce.data.smartstore.RecommendationServiceConfigData;
import com.freshdirect.ecommerce.data.smartstore.VariantData;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdstore.temails.TransEmailInfoModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.mail.EmailAddress;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.mail.EnumEmailType;
import com.freshdirect.mail.EnumTEmailProviderType;
import com.freshdirect.mail.EnumTEmailStatus;
import com.freshdirect.mail.EnumTranEmailType;
import com.freshdirect.smartstore.CartTabStrategyPriority;
import com.freshdirect.smartstore.ConfigurationStatus;
import com.freshdirect.smartstore.EnumConfigurationState;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.ejb.DynamicSiteFeature;

public class ModelConverter {

	public static Collection<Variant> buildVariantList(
			Collection<VariantData> data) {
		Collection<Variant>  variants = new ArrayList<Variant>();
		for (VariantData variantData : data) {
			Variant variant = buildVariant(variantData);
			variants.add(variant);
		}
				
		return variants;
	}

	private static Variant buildVariant(VariantData variantData) {
		EnumSiteFeature enumFeature = EnumSiteFeature.getEnum(variantData.getSiteFeature());
		Variant variant = new Variant(variantData.getId(), enumFeature, buildRecommendationServiceConfig(variantData.getServiceConfig()),buildTabStrategyPriorities(variantData.getTabStrategyPriorities()));
		return variant;
	}

	public static SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> buildTabStrategyPriorities(
			SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriorityData>> tabStrategyPriorities) {

		SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> tabSortedMap = new TreeMap<Integer, SortedMap<Integer,CartTabStrategyPriority>>();
		for (Entry<Integer, SortedMap<Integer, CartTabStrategyPriorityData>> outterMap : tabStrategyPriorities.entrySet()) {
			Integer key = outterMap.getKey();
			SortedMap<Integer, CartTabStrategyPriority> tabSortedMap2 = new TreeMap<Integer, CartTabStrategyPriority>();
			if(outterMap.getValue()!=null)
			for (Entry<Integer, CartTabStrategyPriorityData> innerMap : outterMap.getValue().entrySet()) {
				Integer key2 = innerMap.getKey();
				CartTabStrategyPriority priority = buildCartTabStratergyPriority(innerMap.getValue());
				tabSortedMap2.put(key2, priority);
			}
			tabSortedMap.put(key, tabSortedMap2);
		}
		return tabSortedMap;
	}

	public static CartTabStrategyPriority buildCartTabStratergyPriority(
			CartTabStrategyPriorityData data) {
		CartTabStrategyPriority cartTabStrategyPriority = new CartTabStrategyPriority(data.getStrategy(), data.getSiteFeature(), data.getPrimaryPriority(), data.getSecondaryPriority());
		return cartTabStrategyPriority;
	}

	public static RecommendationServiceConfig buildRecommendationServiceConfig(
			RecommendationServiceConfigData serviceConfig) {
		RecommendationServiceConfig config = new RecommendationServiceConfig(serviceConfig.getVariantId(), RecommendationServiceType.getEnum(serviceConfig.getType()));
		if(serviceConfig.getParams()!=null)
			for (Entry<String,String> iterable_element : serviceConfig.getParams().entrySet()) {
				config.set(iterable_element.getKey(), iterable_element.getValue());
			}
		SortedMap<String, ConfigurationStatus> configStatus = new TreeMap<String, ConfigurationStatus>();
		if(serviceConfig.getConfigStatus()!=null)
			for (Entry<String, ConfigurationStatusData> iterable_element : serviceConfig.getConfigStatus().entrySet()) {
				String key = iterable_element.getKey();
				ConfigurationStatus value =	buildConfigurationStatus(iterable_element.getValue());
				configStatus.put(key, value);
			}
		config.setConfigStatus(configStatus);
		return config;
	}

	public static ConfigurationStatus buildConfigurationStatus(
			ConfigurationStatusData value) {
		ConfigurationStatus status = new ConfigurationStatus(value.getName(), value.getLoadedValue(), value.getAppliedValue(), buildEnumConfigurationStatus(value.getState()), value.getMessage());
		return status;
	}

	private static EnumConfigurationState buildEnumConfigurationStatus(String state) {
		if(state.equalsIgnoreCase("CONFIGURED_OK")){
			return new EnumConfigurationState("CONFIGURED_OK",true);
		}else if(state.equalsIgnoreCase("CONFIGURED_DEFAULT")){
			return new EnumConfigurationState("CONFIGURED_DEFAULT", true);
		}else if(state.equalsIgnoreCase("CONFIGURED_OVERRIDDEN")){
			return new EnumConfigurationState("CONFIGURED_OVERRIDDEN", true);
		}else if(state.equalsIgnoreCase("CONFIGURED_WRONG")){
			return new EnumConfigurationState("CONFIGURED_WRONG", false);
		}else if(state.equalsIgnoreCase("CONFIGURED_WRONG_DEFAULT")){
			return new EnumConfigurationState("CONFIGURED_WRONG_DEFAULT", false);
		}else if(state.equalsIgnoreCase("CONFIGURED_UNUSED")){
			return new EnumConfigurationState("CONFIGURED_UNUSED", true);
		}else if(state.equalsIgnoreCase("UNCONFIGURED_OK")){
			return new EnumConfigurationState("UNCONFIGURED_OK", true,false);
		}else if(state.equalsIgnoreCase("UNCONFIGURED_DEFAULT")){
			return new EnumConfigurationState("UNCONFIGURED_DEFAULT", true,false);
		}else if(state.equalsIgnoreCase("UNCONFIGURED_OVERRIDDEN")){
			return new EnumConfigurationState("UNCONFIGURED_OVERRIDDEN", true,false);
		}else if(state.equalsIgnoreCase("UNCONFIGURED_WRONG")){
			return new EnumConfigurationState("UNCONFIGURED_WRONG", false,false);
		}
		return null;
	}

	public static Collection<DynamicSiteFeature> buildDynamicSiteFeatureList(
			Collection<DynamicSiteFeatureData> data) {
		Collection<DynamicSiteFeature> featureList = new ArrayList<DynamicSiteFeature>();
		for (DynamicSiteFeatureData dynamicSiteFeatureData : data) {
			DynamicSiteFeature dynamicSiteFeature = new DynamicSiteFeature(dynamicSiteFeatureData.getName(), dynamicSiteFeatureData.getTitle(), dynamicSiteFeatureData.getPrez_title(), dynamicSiteFeatureData.getPrez_desc(), dynamicSiteFeatureData.isSmartSaving());
			featureList.add(dynamicSiteFeature);
		}
		return featureList;
	}
	
	public static RestrictedAddressModel buildRestrictedAddressModel(RestrictedAddressModelData data) {
		RestrictedAddressModel restrictedAddress = new RestrictedAddressModel();
		restrictedAddress.setLastModified(data.getLastModified());
		restrictedAddress.setModifiedBy(data.getModifiedBy());
		restrictedAddress.setReason(EnumRestrictedAddressReason.getRestrictionReason(data.getReason()));
		restrictedAddress.setServiceType(EnumServiceType.getEnum(data.getServiceType()));
		restrictedAddress.setCompanyName(data.getCompanyName());
		restrictedAddress.setAddress1(data.getAddress1());
		restrictedAddress.setAddress2(data.getAddress2());
		restrictedAddress.setApartment(data.getApartment());
		restrictedAddress.setCity(data.getCity());
		restrictedAddress.setState(data.getState());
		restrictedAddress.setZipCode(data.getZipCode());
		if(data.getAddressInfoData() != null){
		AddressInfo addressinfo = new AddressInfo(data.getAddressInfoData().getZoneCode(), data.getAddressInfoData().getLongitude(),
				data.getAddressInfoData().getLatitude(), data.getAddressInfoData().getScrubbedStreet(), EnumAddressType.getEnum(data.getAddressInfoData().getAddressType()), data.getAddressInfoData().getCounty(),
				data.getAddressInfoData().getBuildingId(), data.getAddressInfoData().getLocationId());
		addressinfo.setZoneId(data.getAddressInfoData().getZoneId());
		addressinfo.setGeocodeException(data.getAddressInfoData().isGeocodeException());
		addressinfo.setSsScrubbedAddress(data.getAddressInfoData().getSsScrubbedAddress());
		restrictedAddress.setAddressInfo(addressinfo);
		}
		return restrictedAddress;
	}
	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");
	public  static RestrictionI buildRestriction(Object dlvRestriction) {
		if (dlvRestriction == null) {
			return null;
		}
		if (dlvRestriction instanceof RecurringRestrictionData) {
			RecurringRestrictionData recurringRestrictionData = (RecurringRestrictionData) dlvRestriction;
			TimeOfDay startDate = new TimeOfDay(recurringRestrictionData.getTimeOfDayRange().getStartDate().getNormalDate());
			TimeOfDay endDate = new TimeOfDay(recurringRestrictionData.getTimeOfDayRange().getEndDate().getNormalDate());
			if (JUST_BEFORE_MIDNIGHT.equals(endDate)) {
				endDate = TimeOfDay.NEXT_MIDNIGHT;
			}
			RecurringRestriction recurringRestriction = new RecurringRestriction(recurringRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(recurringRestrictionData.getCriterion()),
					EnumDlvRestrictionReason.getEnum(recurringRestrictionData.getReason()), recurringRestrictionData.getName(), recurringRestrictionData.getMessage(),  recurringRestrictionData.getDayOfWeek(),  
					startDate, endDate, recurringRestrictionData.getPath());
			return recurringRestriction;
		} else if (dlvRestriction instanceof OneTimeReverseRestrictionData) {
			OneTimeReverseRestrictionData oneTimeReverseRestrictionData = (OneTimeReverseRestrictionData) dlvRestriction;
			OneTimeReverseRestriction oneTimeReverseRestriction = new OneTimeReverseRestriction(oneTimeReverseRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(oneTimeReverseRestrictionData.getCriterion()), 
					EnumDlvRestrictionReason.getEnum(oneTimeReverseRestrictionData.getReason()),oneTimeReverseRestrictionData.getName(), oneTimeReverseRestrictionData.getMessage(),
					oneTimeReverseRestrictionData.getRange().getStartdate(), oneTimeReverseRestrictionData.getRange().getEndDate(), oneTimeReverseRestrictionData.getPath());
			return oneTimeReverseRestriction;
		} else if (dlvRestriction instanceof OneTimeRestrictionData) {
			OneTimeRestrictionData oneTimeRestrictionData = (OneTimeRestrictionData) dlvRestriction;
			OneTimeRestriction oneTimeRestriction = new OneTimeRestriction(oneTimeRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(oneTimeRestrictionData.getCriterion()), 
					EnumDlvRestrictionReason.getEnum(oneTimeRestrictionData.getReason()),oneTimeRestrictionData.getName(), oneTimeRestrictionData.getMessage(),
					oneTimeRestrictionData.getRange().getStartdate(), oneTimeRestrictionData.getRange().getEndDate(), oneTimeRestrictionData.getPath());
			return oneTimeRestriction;

		} else if (dlvRestriction instanceof AlcoholRestrictionData) {
			AlcoholRestrictionData alcoholRestrictionData = (AlcoholRestrictionData) dlvRestriction;
			AlcoholRestriction alcoholRestriction = new AlcoholRestriction(alcoholRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(alcoholRestrictionData.getCriterion()), 
					EnumDlvRestrictionReason.getEnum(alcoholRestrictionData.getReason()),alcoholRestrictionData.getName(), alcoholRestrictionData.getMessage(),
					alcoholRestrictionData.getDateRange().getStartdate(), alcoholRestrictionData.getDateRange().getEndDate(), EnumDlvRestrictionType.getEnum(alcoholRestrictionData.getType()),
					alcoholRestrictionData.getPath(), alcoholRestrictionData.getState(), alcoholRestrictionData.getCounty(),
					alcoholRestrictionData.getCity(), alcoholRestrictionData.getMunicipalityId(), alcoholRestrictionData.isAlcoholRestricted());
			return alcoholRestriction;
			}
		return null;
	}
	
	public static TEmailI buildTransMail(TransEmailInfoData data) {
		TransEmailInfoModel transEmailInfoModel = new TransEmailInfoModel();
		transEmailInfoModel.setBCCList(data.getBccList());
		transEmailInfoModel.setCCList(data.getCcList());
		transEmailInfoModel.setCroModDate(data.getCroModDate());
		transEmailInfoModel.setCustomerId(data.getCustomerId());
		transEmailInfoModel.setEmailContent(data.getEmailContent());
		transEmailInfoModel.setEmailStatus(EnumTEmailStatus.getEnum(data.getEmailStatus()));
		transEmailInfoModel.setEmailTransactionType(EnumTranEmailType.getEnum(data.getEmailTransactionType()));
		transEmailInfoModel.setEmailType(EnumEmailType.getEnum(data.getEmailType()));
		transEmailInfoModel.setFromAddress(buildEmailAddress(data.getFrom()));
		transEmailInfoModel.setId(data.getId());
		transEmailInfoModel.setOasQueryString(data.getOasQueryString());
		transEmailInfoModel.setOrderId(data.getOrderId());
		transEmailInfoModel.setProductionReady(data.isProductionReady());
		transEmailInfoModel.setProvider(EnumTEmailProviderType.getEnum(data.getProvider()));
		transEmailInfoModel.setRecipient(data.getRecipient());
		transEmailInfoModel.setSubject(data.getSubject());
		transEmailInfoModel.setTargetProgId(data.getTargetProgId());
		transEmailInfoModel.setTemplateId(data.getTemplateId());
		return transEmailInfoModel;
	}


	private static  EmailAddress buildEmailAddress(EmailAddressData from) {
		if(from != null) {
		EmailAddress emailAddress = new EmailAddress(from.getName(), from.getAddress());
		return emailAddress;
		}
		return null;
	}

	public static List buildTransMail(List<TransEmailInfoData> data) {
		List transMailList = new ArrayList();
		for (TransEmailInfoData transEmailInfoData : data) {
			transMailList.add(buildTransMail(transEmailInfoData));
		}
		return transMailList;
	}
	
}
