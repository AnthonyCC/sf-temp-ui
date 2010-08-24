package com.freshdirect.fdstore.content;

import com.freshdirect.fdstore.FDResourceException;

/**
 * Brand Filter
 * 
 * @author zsombor, segabor
 * 
 */
public class BrandFilter extends AbstractProductFilter {
    String  brand;

    public BrandFilter(String brand) {
        this.brand = brand;
    }

    public boolean applyTest(ProductModel prod) throws FDResourceException {
        if (brand != null) {
            for ( BrandModel bm : prod.getBrands() ) {
                if (bm.getContentKey().getId().equals(brand)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
