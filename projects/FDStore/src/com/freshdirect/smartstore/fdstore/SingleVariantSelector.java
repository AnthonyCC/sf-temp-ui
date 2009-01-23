package com.freshdirect.smartstore.fdstore;

import com.freshdirect.smartstore.RecommendationService;

/**
 * This VariantSelector returns the default variant for all users.
 * 
 * @author zsombor
 *
 */
public class SingleVariantSelector extends VariantSelector {

    RecommendationService service;
    
    public SingleVariantSelector(RecommendationService service) {
        this.service = service;
    }

    protected void init() {

    }

    public RecommendationService select(String erpUserId) {
        return service;
    }
    

}
