package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;

public class ProductGrabberModel extends ContentNodeModelImpl {

    private List<ProductFilterModel> productFilters = new ArrayList<ProductFilterModel>();
    private List<ProductContainer> scope = new ArrayList<ProductContainer>();

    public ProductGrabberModel(ContentKey key) {
        super(key);
    }

    public List<ProductFilterModel> getProductFilterModels() {
        ContentNodeModelUtil.refreshModels(this, "productFilters", productFilters, false);
        return new ArrayList<ProductFilterModel>(productFilters);
    }

    public List<ProductContainer> getScope() {
        ContentNodeModelUtil.refreshModels(this, "scope", scope, false);
        return new ArrayList<ProductContainer>(scope);
    }

}
