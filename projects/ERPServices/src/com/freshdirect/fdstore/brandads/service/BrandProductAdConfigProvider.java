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

	public  String getBrandProductAdProviderCategoryURL();

	public  String getBrandProductAdProviderCreative() ;

	public  String getBrandProductAdProviderAallowMarketplace() ;

	public  String getBrandProductAdProviderCategoryHlpt() ;

	public  String getBrandProductAdProviderCategoryPlatform() ;

	public  String getBrandProductAdProviderMinmes() ;

	public  String getBrandProductAdProviderMaxmes() ;

	public  String getBrandProductAdProviderPgn() ;

	public  String getBrandProductsBeacon() ;

	public String getBrandProductAdProviderHomeHlpt();

	public String getBrandProductAdProviderHomePageCreative();

	public String getBrandProductAdProviderPdpCreative();
	
	public String getBrandProductAdHomePageTaxonomy();

	public String getHomeProductAdProviderMaxmes();

	public  String getPdpProductAdProviderMaxmes();
	
	public String getBrandProductAdProviderPdpHlpt();
	
	public String getBrandProductAdProviderPdpURL();
	
	public String getBrandProductAdProviderPdpUpdateURL();
	
	public String getBrandProductAdProviderHomePageURL();
	
}