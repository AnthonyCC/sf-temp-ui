package com.freshdirect.storeapi.content;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * check availabilities light version by user context. 
 * Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
 * 
 * @author sbagavathiappan
 * 
 */
public class ProductAvailabilityByContextFilter extends AbstractProductFilter {

    private static final Logger LOGGER = LoggerFactory.getInstance(ProductAvailabilityByContextFilter.class);
    
    //Return true if product is available(not filtered)
    public boolean applyTest(ProductModel product) throws FDResourceException {
    	return !isAlcoholRestrictedByContextAndSku(product); // For now only this later on plant and other factor will play
    }

    public static boolean isAlcoholRestrictedByContextAndSku(ProductModel product) throws FDResourceException  {
		try {			
			//Figuring out if product contains alcohol is like finding life in mars...
			return ( ContentFactory.getInstance().getCurrentUserContext() != null 
						&& ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext() != null
						&& ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().isAlcoholRestricted()
						&& product.getCategory() != null 
						&& (((product.getCategory().isHavingBeer() 
									|| (product.getPrimaryHome() != null && product.getPrimaryHome().isHavingBeer())))   
						|| (product.getSkus() != null && product.getSkus().size() > 0 
						&& product.getSku(0).getProduct().isAlcohol())));
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw e;
		} catch (FDSkuNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return false;
	}
}
