package com.freshdirect.smartstore.impl;


import java.util.ArrayList;
import java.util.List;

import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductReference;

public class ProductSampleRecommendationService extends AbstractRecommendationService {

    public ProductSampleRecommendationService(Variant variant, ImpressionSampler sampler, boolean includeCartItems) {
        super(variant, sampler, includeCartItems);
    }

    @Override
    protected List<ContentNodeModel> doRecommendNodes(SessionInput input) {
        List<ContentNodeModel> nodes = new ArrayList<ContentNodeModel>();
        for (ProductReference sample : input.getProductSamples()) {
            nodes.add(sample.lookupProductModel());
        }
        return nodes;
    }

}
