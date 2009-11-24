package com.freshdirect.smartstore;

import com.freshdirect.framework.util.Reference;
import com.freshdirect.smartstore.service.VariantRegistry;

public class VariantReference extends Reference<Variant, String> {

    public VariantReference(Variant model) {
        super(model);
    }
    
    @Override
    protected String getKey(Variant model) {
        return model.getId();
    }
    
    @Override
    protected Variant lookup(String key) {
        return VariantRegistry.getInstance().getService(key);
    }

}
