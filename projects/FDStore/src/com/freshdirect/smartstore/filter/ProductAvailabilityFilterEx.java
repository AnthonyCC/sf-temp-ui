package com.freshdirect.smartstore.filter;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;

/**
 * Check whether product or one of its alternatives is available
 * 
 * @author segabor
 * 
 */
public class ProductAvailabilityFilterEx extends ProductAvailabilityFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductAvailabilityFilterEx.class);
    
        public ProductAvailabilityFilterEx(boolean showTempUnavailable) {
            super(showTempUnavailable);
        }
	
        @Override
        public ProductModel filterProduct(ProductModel model) {
            if (model == null || available(model)) {
                return model;
            }
    
            for (ContentNodeModel alternativeModel : model.getRecommendedAlternatives()) {
                if (alternativeModel instanceof ProductModel && available((ProductModel) alternativeModel)) {
                    LOGGER.debug("substituted: " + model.getContentKey() +" -> "+alternativeModel.getContentKey()+" "+alternativeModel.getFullName());
                    return (ProductModel) alternativeModel;
                } else if (alternativeModel instanceof SkuModel) {
                    return filter((ProductModel) alternativeModel.getParentNode());
                }
            }
            return null;
        }

}
