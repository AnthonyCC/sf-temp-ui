package com.freshdirect.fdstore.content.test;


import java.util.Collection;
import java.util.NoSuchElementException;

import com.freshdirect.cms.fdstore.FDContentTypes;

import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;


/**
 * 
 * Can generate sample product sets with frequencies that match a reference frequency.
 * 
 * @author istvan
 *
 */
public class ProductSamplerImpl extends ContentNodeSamplerImpl implements ContentNodeSampler {
	
	/**
	 * 
	 * @param contentKeys of products
	 */
	public ProductSamplerImpl(Collection contentKeys) {
		super(FDContentTypes.PRODUCT,contentKeys);
		addContentKeys(contentKeys);
	}
	
	/**
	 * Constructor.
	 * 
	 * Will download all product content keys from CMS.
	 *
	 */
	public ProductSamplerImpl() {
		super(FDContentTypes.PRODUCT);
	}
	
	/**
	 * Set a frequnecy for a product corresponding to the given skuCode.
	 * @param skuCode
	 * @param frequency
	 */
	public void setFrequencyFromSku(String skuCode, int frequency) throws FDSkuNotFoundException {

		ProductModel productModel = fact.getProduct(skuCode);
		String productId = productModel.getContentKey().getId();
		if (!knownContentKeys.containsKey(productId)) 
			throw new NoSuchElementException("No product with SKU " + skuCode + " is in known content key set");
		sampler.addValue(productId);
	}
	

}
