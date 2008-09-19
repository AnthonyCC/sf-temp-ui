package com.freshdirect.webapp.util;


import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * A wrapper for the product.
 * 
 * Subclasses can provide additional information.
 * 
 * @author istvan
 *
 */
public class ProductImpression {
	
	protected ProductModel productModel;
	
	/**
	 * Constructor.
	 * @param productModel product
	 */
	public ProductImpression(ProductModel productModel) {
		this.productModel = productModel;
	}
	
	/**
	 * Get product model.
	 * @return the product set in the constructor
	 */
	public ProductModel getProductModel() {
		return productModel;
	}
	
	/**
	 * Whether this product impression is transactional.
	 * @return false
	 */
	public boolean isTransactional() {
		return false;
	}
	
	/**
	 * Get the FD product corresponding to this impression.
	 * 
	 * This is a "convenience" method.
	 * 
	 * @return the {@link FDProduct} corresponding to the default sku
	 */
	public FDProduct getFDProduct() {
		try {
			FDProductInfo productInfo = 
				FDCachedFactory.getProductInfo(getSku().getSkuCode());
			return FDCachedFactory.getProduct(productInfo);
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	/**
	 * Get the default sku.
	 * 
	 * @return the product model's default sku
	 * @see ProductModel#getDefaultSku()
	 */
	public SkuModel getSku() {
		return getProductModel().getDefaultSku();
	}
	

	


	/**
	 * Perform a full check on impression against ability to display
	 *
	 * @return
	 */
	public boolean validate() {
		boolean isValid = true;
		
		try {
			SkuModel sku = getSku();
			FDProduct p = getFDProduct();

			isValid = !(
				sku == null ||
				sku.getSkuCode() == null ||
				productModel.getFullName() == null ||
				productModel.getPrimaryBrandName() == null ||
				productModel.getSizeDescription() == null ||
				productModel.getDefaultPrice() == null ||
				p == null ||
				p.getPricing() == null
			);

		} catch (Exception exc) {
			// invalidate product
			isValid = false;
		}
		
		
		return isValid;
	}
}
