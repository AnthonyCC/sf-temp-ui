package com.freshdirect.fdstore;

/**
 * 
 * @author ksriram
 *
 */
public class FDProductPromotionPreviewInfo extends FDProductPromotionInfo {

	
	private FDProductInfo fdProductInfo;
	private FDProduct fdProduct;
	
	public FDProductPromotionPreviewInfo(FDProductPromotionInfo fdProductPromotionInfo) {
		this.setSkuCode(fdProductPromotionInfo.getSkuCode());
		this.setMatNumber(fdProductPromotionInfo.getMatNumber());
		this.setZoneId(fdProductPromotionInfo.getZoneId());
		this.setFeatured(fdProductPromotionInfo.isFeatured());
		this.setFeaturedHeader(fdProductPromotionInfo.getFeaturedHeader());
		this.setPriority(fdProductPromotionInfo.getPriority());
		this.setVersion(fdProductPromotionInfo.getVersion());
		this.setType(fdProductPromotionInfo.getType());
		this.setErpCategory(fdProductPromotionInfo.getErpCategory());
		this.setErpCatPosition(fdProductPromotionInfo.getErpCatPosition());
		this.setErpPromtoionId(fdProductPromotionInfo.getErpPromtoionId());
	}
	
	public FDProductInfo getFdProductInfo() {
		return fdProductInfo;
	}
	public void setFdProductInfo(FDProductInfo fdProductInfo) {
		this.fdProductInfo = fdProductInfo;
	}

	public FDProduct getFdProduct() {
		return fdProduct;
	}

	public void setFdProduct(FDProduct fdProduct) {
		this.fdProduct = fdProduct;
	}
	
	
}
