package com.freshdirect.mobileapi.model.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductFilter;
import com.freshdirect.storeapi.content.ProductModel;

/**
 * Mobile specific Filter
 * 
 * @author rsung
 * 
 */
public class DiscontinuityFilter extends AbstractProductFilter {

    public boolean applyTest(ProductModel prod) throws FDResourceException {
        return !prod.isDiscontinued();
    }

}
