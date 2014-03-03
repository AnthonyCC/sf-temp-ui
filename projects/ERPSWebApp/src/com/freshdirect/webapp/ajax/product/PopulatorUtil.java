package com.freshdirect.webapp.ajax.product;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;

public class PopulatorUtil {
	
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
}
