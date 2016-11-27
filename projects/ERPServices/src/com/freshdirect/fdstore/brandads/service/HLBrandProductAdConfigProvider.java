package com.freshdirect.fdstore.brandads.service;

import java.io.Serializable;

import com.freshdirect.ErpServicesProperties;

public class HLBrandProductAdConfigProvider implements BrandProductAdConfigProvider, Serializable {

	private static HLBrandProductAdConfigProvider configProvider = new HLBrandProductAdConfigProvider();
	public String getBrandProductAdProviderURL() {
		return ErpServicesProperties.getHLBrandProductAdvertiseURL();
	}
	public String getHLBrandProductAdvertiseConfirmationURL() {
		return ErpServicesProperties.getHLBrandProductAdvertiseConfirmationURL();
	}
	
	
	public Integer getConnectionTimeoutPeriod() {
		return ErpServicesProperties.getHLConnectionTimeoutPeriod();
	}

	public Integer getReadTimeoutPeriod() {
		return ErpServicesProperties.getHLReadTimeoutPeriod();
	}
	
	public String getBrandProductAdProviderAPIKey() {
		return ErpServicesProperties.getHLBrandProductAdvertiseAPIKey();
	}
	
	public static HLBrandProductAdConfigProvider getInstance(){
		return configProvider;
	}
	
	public  String getBrandProductAdProviderPlatform(){
		return ErpServicesProperties.getBrandProductAdProviderPlatform();
	}
	
	public  String getBrandProductAdProviderCulture(){
		return ErpServicesProperties.getBrandProductAdProviderCulture();
	}
	
	public  String getBrandProductAdProviderIc() {
		return ErpServicesProperties.getBrandProductAdProviderIc();
	}
	
	public  String getBrandProductAdProviderMediaSource(){
		return ErpServicesProperties.getBrandProductAdProviderMediaSource();
	}
	
	public  String getBrandProductAdProviderHlpt(){
		return ErpServicesProperties.getBrandProductAdProviderHlpt();
	}
	
	public  String getBrandProductAdProviderStrategy(){
		return ErpServicesProperties.getBrandProductAdProviderStrategy();
	}
	
	public  String getBrandProductAdProviderConformationHlpt(){
		return ErpServicesProperties.getBrandProductAdProviderConformationHlpt();
	}
	
	public  String getBrandProductAdProviderCategoryURL(){
		return ErpServicesProperties.getBrandProductAdProviderCategoryURL();
	}
	
	public  String getBrandProductAdProviderCreative(){
		
		return ErpServicesProperties.getBrandProductAdProviderCreative();
	}
	
	public  String getBrandProductAdProviderAallowMarketplace(){
		
		return ErpServicesProperties.getBrandProductAdProviderAllowMarketplace();
	}
	
	public  String getBrandProductAdProviderCategoryHlpt(){
		
		return ErpServicesProperties.getBrandProductAdProviderCategoryHlpt();
	}
	
	public  String getBrandProductAdProviderCategoryPlatform(){
		
		return ErpServicesProperties.getBrandProductAdProviderCategoryPlatform();
	}
	
	public  String getBrandProductAdProviderMinmes(){
		
		return ErpServicesProperties.getBrandProductAdProviderMinmes();
	}
	
	public  String getBrandProductAdProviderMaxmes(){
		
		return ErpServicesProperties.getBrandProductAdProviderMaxmes();
	}
	
	public  String getBrandProductAdProviderPgn(){
		
		return ErpServicesProperties.getBrandProductAdProviderPgn();
	}
	
	
	
}
