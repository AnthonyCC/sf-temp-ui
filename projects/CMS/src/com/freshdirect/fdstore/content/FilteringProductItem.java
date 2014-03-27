package com.freshdirect.fdstore.content;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;

/**
 * Encapsulates data needed for CMS based product filtering and sorting
 *
 */
public class FilteringProductItem {
	/**
	 * Mandatory parameter
	 */
	private ProductModel productModel;


	/**
	 * Optional, lazy initialized property
	 */
	private FDProductResource fdPrdResource = new FDProductResource();
	private FDProductInfoResource fdPrdInfoResource = new FDProductInfoResource();
	
	public FilteringProductItem(ProductModel productModel) {
		this.productModel = productModel;
	}
	
	public ProductModel getProductModel() {
		return productModel;
	}
	
	public FDProduct getFdProduct() throws FDResourceException {
		return fdPrdResource.getResource();
	}

	public FDProductInfo getFdProductInfo() throws FDResourceException {
		return fdPrdInfoResource.getResource();
	}
	

	private class FDProductResource extends FilterResource<FDProduct> {

		@Override
		protected FDProduct obtainResource() throws FDResourceException, FDSkuNotFoundException {
			SkuModel sku = PopulatorUtil.getDefSku(productModel);
			if (sku==null) {
				return null;
			} else {
				return sku.getProduct();
			}
		}
		
	}

	private class FDProductInfoResource extends FilterResource<FDProductInfo> {

		@Override
		protected FDProductInfo obtainResource() throws FDResourceException, FDSkuNotFoundException {
			SkuModel sku = PopulatorUtil.getDefSku(productModel);
			if (sku==null) {
				return null;
			} else {
				return sku.getProductInfo();
			}
		}
		
	}
}
