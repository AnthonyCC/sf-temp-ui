package com.freshdirect.webapp.ajax.product;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class PopulatorUtil {
	private final static Logger LOGGER = Logger.getLogger(PopulatorUtil.class.getName());
	
	public static final ProductModel getProduct( String productId, String categoryId ) {
		if ( categoryId == null ) {
			// get product in its primary home
			return (ProductModel)ContentFactory.getInstance().getContentNodeByKey( new ContentKey(ContentType.get( "Product" ), productId) );
		} else {
			// get product in specified category context
			return ContentFactory.getInstance().getProductByName( categoryId, productId );
		}		
	}
	
	public static final SkuModel getDefSku( ProductModel product ) {
		SkuModel sku = product.getDefaultSku();
		if ( sku == null ) {			
			// temporary unav item?
			sku = product.getDefaultTemporaryUnavSku();
		}
		return sku;
	}

	public static final float calculateSafeMaximumQuantity( FDUserI user, ProductModel product ) {
		float min = product.getQuantityMinimum();
		float max = user.getQuantityMaximum( product );
		float inc = product.getQuantityIncrement();
		
		while ( min <= max - inc ) 
			min += inc;	
		return min;
	}



	/**
	 * Check if a product is newly created in CMS
	 * and corresponding ERPS data is not assigned yet.
	 * 
	 * @return
	 * 
	 * @throws FDSkuNotFoundException 
	 * @throws FDResourceException 
	 */
	public static final boolean isProductIncomplete(ProductModel prd) throws FDResourceException, FDSkuNotFoundException {
		if (!FDStoreProperties.getPreviewMode()) {
			// usual business
			return false;
		}

		if (prd.getSkus().size() == 0) {
			// No SKUs found. This is really bad.
			// Let the execution go and break somewhere else
			throw new FDSkuNotFoundException("Product " + prd.getContentName() + " contains NO SKUs!");
		}
		
		// now pick the first SKU
		// Theoretically there should be only one by now 
		
		SkuModel sku = PopulatorUtil.getDefSku(prd);
		if (sku == null) {
			return true;
		}

		FDProductInfo pInfo = null;
		try {
			pInfo = sku.getProductInfo();
		} catch (FDSkuNotFoundException ex) {
			return true;
		}

		// LOGGER.debug("SKU Version" + pInfo.getVersion());
		
		return pInfo == null || pInfo.getVersion() == 0;
	}
}
