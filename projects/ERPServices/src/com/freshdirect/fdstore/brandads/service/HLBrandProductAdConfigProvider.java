package com.freshdirect.fdstore.brandads.service;

import java.io.Serializable;

import com.freshdirect.ErpServicesProperties;

public class HLBrandProductAdConfigProvider implements BrandProductAdConfigProvider, Serializable {


	private static final long serialVersionUID = 9063954495984648495L;
	private static HLBrandProductAdConfigProvider configProvider = new HLBrandProductAdConfigProvider();
	@Override
	public String getBrandProductAdProviderURL() {
		return ErpServicesProperties.getHLBrandProductAdvertiseURL();
	}
	@Override
	public String getHLBrandProductAdvertiseConfirmationURL() {
		return ErpServicesProperties.getHLBrandProductAdvertiseConfirmationURL();
	}


	@Override
	public Integer getConnectionTimeoutPeriod() {
		return ErpServicesProperties.getHLConnectionTimeoutPeriod();
	}

	@Override
	public Integer getReadTimeoutPeriod() {
		return ErpServicesProperties.getHLReadTimeoutPeriod();
	}

	@Override
	public String getBrandProductAdProviderAPIKey() {
		return ErpServicesProperties.getHLBrandProductAdvertiseAPIKey();
	}

	public static HLBrandProductAdConfigProvider getInstance(){
		return configProvider;
	}

	@Override
	public  String getBrandProductAdProviderPlatform(){
		return ErpServicesProperties.getBrandProductAdProviderPlatform();
	}

	@Override
	public  String getBrandProductAdProviderCulture(){
		return ErpServicesProperties.getBrandProductAdProviderCulture();
	}

	@Override
	public  String getBrandProductAdProviderIc() {
		return ErpServicesProperties.getBrandProductAdProviderIc();
	}

	@Override
	public  String getBrandProductAdProviderMediaSource(){
		return ErpServicesProperties.getBrandProductAdProviderMediaSource();
	}

	@Override
	public  String getBrandProductAdProviderHlpt(){
		return ErpServicesProperties.getBrandProductAdProviderHlpt();
	}

	@Override
	public  String getBrandProductAdProviderStrategy(){
		return ErpServicesProperties.getBrandProductAdProviderStrategy();
	}

	@Override
	public  String getBrandProductAdProviderConformationHlpt(){
		return ErpServicesProperties.getBrandProductAdProviderConformationHlpt();
	}

	@Override
	public  String getBrandProductAdProviderCategoryURL(){
		return ErpServicesProperties.getBrandProductAdProviderCategoryURL();
	}

	@Override
	public  String getBrandProductAdProviderCreative(){

		return ErpServicesProperties.getBrandProductAdProviderCreative();
	}

	@Override
	public  String getBrandProductAdProviderAallowMarketplace(){

		return ErpServicesProperties.getBrandProductAdProviderAllowMarketplace();
	}

	@Override
	public  String getBrandProductAdProviderCategoryHlpt(){

		return ErpServicesProperties.getBrandProductAdProviderCategoryHlpt();
	}

	@Override
	public  String getBrandProductAdProviderCategoryPlatform(){

		return ErpServicesProperties.getBrandProductAdProviderCategoryPlatform();
	}

	@Override
	public  String getBrandProductAdProviderMinmes(){

		return ErpServicesProperties.getBrandProductAdProviderMinmes();
	}

	@Override
	public  String getBrandProductAdProviderMaxmes(){

		return ErpServicesProperties.getBrandProductAdProviderMaxmes();
	}

	@Override
	public  String getBrandProductAdProviderPgn(){

		return ErpServicesProperties.getBrandProductAdProviderPgn();
	}

	@Override
	public  String getBrandProductsBeacon(){
		return ErpServicesProperties.getBrandProductsBeacon();
	}
	/*APPDEV-6204*/
	@Override
	public String getBrandProductAdProviderHomeHlpt() {
		return ErpServicesProperties.getBrandProductAdProviderHomeHlpt();
	}

	@Override
	public  String getBrandProductAdProviderHomePageCreative(){

		return ErpServicesProperties.getBrandProductAdProviderHomePageCreative();
	}

	@Override
	public  String getBrandProductAdProviderPdpCreative(){

		return ErpServicesProperties.getBrandProductAdProviderPdpCreative();
	}

	@Override
	public String getBrandProductAdHomePageTaxonomy() {
		return ErpServicesProperties.getBrandProductAdHomePageTaxonomy();
	}
	@Override
	public String getHomeProductAdProviderMaxmes() {
		return ErpServicesProperties.getHomeProductAdProviderMaxmes();
	}
	@Override
	public String getPdpProductAdProviderMaxmes() {
		return ErpServicesProperties.getPdpProductAdProviderMaxmes();
	}
	
	@Override
	public String getBrandProductAdProviderPdpHlpt() {
		return ErpServicesProperties.getBrandProductAdProviderPdpHlpt();
	}
	
	@Override
	public String getBrandProductAdProviderPdpURL() {
		return ErpServicesProperties.getBrandProductAdProviderPdpURL();
	}
	
	@Override
	public String getBrandProductAdProviderPdpUpdateURL() {
		return ErpServicesProperties.getBrandProductAdProviderPdpUpdateURL();
	}
	
	@Override
	public String getBrandProductAdProviderHomePageURL(){
		return ErpServicesProperties.getBrandProductAdProviderHomePageURL();
	}

}
