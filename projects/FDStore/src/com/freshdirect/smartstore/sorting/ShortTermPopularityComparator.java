package com.freshdirect.smartstore.sorting;

import java.util.List;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.service.SearchScoringRegistry;

public class ShortTermPopularityComparator extends PopularityComparator {

    public ShortTermPopularityComparator(boolean inverse, PricingContext pricingContext) {
        this(inverse, false, null, pricingContext);
    }
    
    ShortTermPopularityComparator(boolean inverse, boolean hideUnavailable, List<ProductModel> products, PricingContext pricingContext) {
        super(inverse, hideUnavailable, products, pricingContext, new ScriptedContentNodeComparator(ScoreProvider.getInstance(), null,
                        pricingContext, SearchScoringRegistry.getInstance().getShortTermPopularityScoringAlgorithm()));
    }

}
