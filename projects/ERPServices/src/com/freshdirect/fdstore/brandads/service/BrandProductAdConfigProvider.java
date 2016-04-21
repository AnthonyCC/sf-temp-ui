package com.freshdirect.fdstore.brandads.service;

import com.freshdirect.ErpServicesProperties;

public interface BrandProductAdConfigProvider {

	public  String getBrandProductAdProviderURL() ;
	
	public  Integer getConnectionTimeoutPeriod() ;

	public  Integer getReadTimeoutPeriod() ;
	
	public  String getBrandProductAdProviderAPIKey() ;
	
	public  String getBrandProductAdProviderPlatform() ;
	
	public  String getBrandProductAdProviderCulture() ;
	
	public  String getBrandProductAdProviderIc() ;
	
	public  String getBrandProductAdProviderMediaSource() ;
	
	public  String getBrandProductAdProviderHlpt() ;
	
	public  String getBrandProductAdProviderStrategy() ;
	
	public  String getHLBrandProductAdvertiseConfirmationURL();
	
	public  String getBrandProductAdProviderConformationHlpt() ;
	
}
