package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.sampling.ImpressionSampler;

public class ProductDonationRecommendationService extends AbstractRecommendationService {

    private static final Category LOGGER = LoggerFactory.getInstance(ProductDonationRecommendationService.class);

    public ProductDonationRecommendationService(Variant variant, ImpressionSampler sampler, boolean includeCartItems) {
        super(variant, sampler, includeCartItems);
    }

    @Override
    protected List<ContentNodeModel> doRecommendNodes(SessionInput input) {
        List<ContentNodeModel> nodes = new ArrayList<ContentNodeModel>();
        for (String productId : FDStoreProperties.getPropDonationProductSamplesId()) {
            try {
                nodes.add(ContentFactory.getInstance().getProduct(productId));
            } catch (FDSkuNotFoundException e) {
                LOGGER.warn("Sku not found: " + productId, e);
            }
        }
        return nodes;
    }

}
