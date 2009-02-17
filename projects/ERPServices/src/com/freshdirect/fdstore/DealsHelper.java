package com.freshdirect.fdstore;

public class DealsHelper {
	
	public static final String ALL_SKUS="ALL";
	
	public static final String SKU_PREFIX_SEPARATOR=",";
	
	public static int getMaxFeaturedDealsForPage() {
		return FDStoreProperties.getMaxFeaturedDealsForPage();
	}
	public static int getMinFeaturedDealsForPage() {
		return FDStoreProperties.getMinFeaturedDealsForPage();
	}
	
	public static int getMaxFeaturedDealsPerLine() {
		return FDStoreProperties.getMaxFeaturedDealsPerLine();
	}

	public static int getDealsLowerLimit() {
		return FDStoreProperties.getDealsLowerLimit();
	}
	public static int getDealsUpperLimit() {
		return FDStoreProperties.getDealsUpperLimit();
	}
	
	public static String getDealsSkuPrefixes() {
		return FDStoreProperties.getDealsSkuPrefixes();
	}

}
