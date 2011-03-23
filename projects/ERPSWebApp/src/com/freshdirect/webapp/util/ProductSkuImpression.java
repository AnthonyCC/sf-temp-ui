package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * This product impression encapsulates a sku too.
 * @author zsombor
 *
 */
public class ProductSkuImpression extends ProductImpression {

    protected SkuModel selectedSku;
    
    public ProductSkuImpression(ProductModel productModel, String selectedSkuCode) {
        super(productModel, null);
        this.selectedSku = productModel.getSku(selectedSkuCode);
        if (selectedSku == null) 
                throw new FDRuntimeException(selectedSkuCode + " not in " + productModel.getFullName());
        setCalculator(new PriceCalculator(productModel.getPricingContext(), this.productModel, selectedSku));
    }

    public ProductSkuImpression(ProductModel productModel, SkuModel sku) {
        super(productModel, new PriceCalculator(productModel.getPricingContext(), productModel, sku));
        this.selectedSku = sku;
    }

    /**
     * Get the default sku.
     * This method masks the product model's sku with 
     * the {@link #getSelectedSku() selected sku}.
     * @return selected sku
     */
    @Override
    public SkuModel getSku() {
        return selectedSku;
    }

}
