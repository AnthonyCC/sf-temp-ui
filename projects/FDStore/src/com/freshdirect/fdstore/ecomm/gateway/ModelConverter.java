package com.freshdirect.fdstore.ecomm.gateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.SortedMap;

import weblogic.utils.collections.TreeMap;

import com.freshdirect.ecommerce.data.smartstore.CartTabStrategyPriorityData;
import com.freshdirect.ecommerce.data.smartstore.ConfigurationStatusData;
import com.freshdirect.ecommerce.data.smartstore.DynamicSiteFeatureData;
import com.freshdirect.ecommerce.data.smartstore.RecommendationServiceConfigData;
import com.freshdirect.ecommerce.data.smartstore.VariantData;
import com.freshdirect.fdstore.util.EnumSiteFeature;
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
		Variant variant = new Variant(variantData.getId(), EnumSiteFeature.getEnum(variantData.getSiteFeature()) , buildRecommendationServiceConfig(variantData.getServiceConfig()),buildTabStrategyPriorities(variantData.getTabStrategyPriorities()));
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
	
}
