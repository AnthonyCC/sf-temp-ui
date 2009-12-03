package com.freshdirect.mobileapi.model.filter;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.AbstractProductFilter;
import com.freshdirect.fdstore.content.ProductModel;

/**
 * Mobile specific Filter
 * 
 * @author rsung
 * 
 */
public class IphoneFilter extends AbstractProductFilter {

    public boolean applyTest(ProductModel prod) throws FDResourceException {
        return !prod.hideIphone();
    }

}
