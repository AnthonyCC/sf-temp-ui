package com.freshdirect.mobileapi.service;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;

public class ProductRecommenderService {

    private static final Logger LOGGER = LoggerFactory.getInstance(ProductRecommenderService.class);

    // TODO make it configurable
    private boolean debugRecommender = false;

    public Recommendations recommendDYFForFoodKickCart(FDUserI customer) {
        if (customer == null) {
            LOGGER.error("Recommender invoked with null customer parameter!");
            return null;
        }

        final SessionInput input = new SessionInput(customer);
        input.setMaxRecommendations(5);
        input.setExcludeAlcoholicContent(false);
        input.setTraceMode(debugRecommender);

        return doRecommend(EnumSiteFeature.getEnum("DYF_FK"), input, customer);
    }

    private Recommendations doRecommend(EnumSiteFeature siteFeature, SessionInput input, FDUserI customer) {
        Recommendations result = null;
        try {
            result = FDStoreRecommender.getInstance().getRecommendations(siteFeature, customer, input);

            // debug section
            final int numberOfRecommendedProducts = result.sizeOfRecommendedContent();
            final String customerId = customer.getIdentity().getErpCustomerPK();
            final String cohortName = customer.getCohortName();
            final String variantId = result.getVariant().getId();

            LOGGER.debug(numberOfRecommendedProducts + " products were recommended to customer id=" + customerId + ", cohort=" + cohortName + " by variant " + variantId + " of " + siteFeature + " site feature");
        } catch (FDResourceException exc) {
            LOGGER.error("Failed to get recommendations for customer " + customer.getIdentity(), exc );
        }
        return result;
    }
}
