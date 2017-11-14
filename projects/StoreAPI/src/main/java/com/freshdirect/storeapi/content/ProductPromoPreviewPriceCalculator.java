package com.freshdirect.storeapi.content;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;

/**
 * 
 * @author ksriram
 *
 */
public class ProductPromoPreviewPriceCalculator extends PriceCalculator {

	public ProductPromoPreviewPriceCalculator(PricingContext ctx,
			ProductModel product,SkuModel sku,FDProductInfo fdProdInfo) {
		super(ctx, product,sku);
		this.productInfo=fdProdInfo;
	}

	@Override
	public FDProductInfo getProductInfo() throws FDResourceException,
			FDSkuNotFoundException {
		return productInfo;
	}
	
	public ProductPromoPreviewPriceCalculator(PricingContext ctx,
			ProductModel product,SkuModel sku,FDProductInfo fdProdInfo,FDProduct fdProduct) {
		super(ctx, product,sku);
		this.productInfo=fdProdInfo;
		this.fdProduct=fdProduct;
	}

}
