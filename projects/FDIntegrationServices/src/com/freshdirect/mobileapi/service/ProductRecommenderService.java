package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.model.RecommenderTitleAndDescription;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;

public class ProductRecommenderService {

    private static final Logger LOGGER = LoggerFactory.getInstance(ProductRecommenderService.class);

    private static final String PRODUCT_SAMPLE_SITE_FEATURE = "PRODUCT_SAMPLE";

    private boolean debugRecommender = false;

    public List<Recommendations> recommendForFoodKickCart(FDUserI customer) {
        List<String> siteFeatureIds = readSiteFeatureListConfiguration(!customer.isHavingEnoughValidOrders());
        if (siteFeatureIds == null || siteFeatureIds.isEmpty()) {
            return Collections.emptyList();
        }

        final int maxRecommendations = FDStoreProperties.getMaxProductsOnFoodKickViewCartRecommenders();

        // collect list of site features configured for FK view cart page
        List<EnumSiteFeature> siteFeatures = decodeSiteFeatureIDs(siteFeatureIds);

        List<Recommendations> cartRecommendations = new ArrayList<Recommendations>(siteFeatures.size());
        for (EnumSiteFeature siteFeature: siteFeatures) {
            final SessionInput input = new SessionInput(customer);
            input.setMaxRecommendations(maxRecommendations);
            input.setExcludeAlcoholicContent(false);
            input.setTraceMode(debugRecommender);

            Recommendations deliveredRecommendations = doRecommend(siteFeature, input, customer);
            if (deliveredRecommendations != null) {
                cartRecommendations.add(deliveredRecommendations);
            }
        }

        return cartRecommendations;
    }

    private List<String> readSiteFeatureListConfiguration(boolean newCustomer) {
        List<String> siteFeatureIds = newCustomer
                ? FDStoreProperties.getSiteFeatureListOnFoodKickViewCartPageForNewCustomers()
                : FDStoreProperties.getSiteFeatureListOnFoodKickViewCartPageForExistingCustomers();

        return siteFeatureIds;
    }

    private List<EnumSiteFeature> decodeSiteFeatureIDs(List<String> siteFeatureTokens) {
        List<EnumSiteFeature> decodedSiteFeatures = new ArrayList<EnumSiteFeature>();
        for (String siteFeatureId : siteFeatureTokens) {
            EnumSiteFeature siteFeature = EnumSiteFeature.getEnum(siteFeatureId);
            if (siteFeature != null && !EnumSiteFeature.NIL.equals(siteFeature)) {
                decodedSiteFeatures.add(siteFeature);
            }
        }
        return decodedSiteFeatures;
    }

    public RecommenderTitleAndDescription getRecommenderDisplayableFields(Variant variant) {
        String prezTitle = extractRecommenderTitle(variant);
        String description = extractRecommenderDescription(variant);

        return new RecommenderTitleAndDescription(prezTitle, description);
    }

    private String extractRecommenderDescription(Variant variant) {
        String description = variant.getServiceConfig().getPresentationDescription();
        EnumSiteFeature siteFeature = variant.getSiteFeature();
        if (PRODUCT_SAMPLE_SITE_FEATURE.equals(siteFeature.getName())) {
            description = description.replace("%%N%%", String.valueOf(FDStoreProperties.getProductSamplesMaxBuyProductsLimit()));
            description = description.replace("%%Q%%", String.valueOf(FDStoreProperties.getProductSamplesMaxQuantityLimit()));
        }
        return description;
    }

    private String extractRecommenderTitle(Variant variant) {
        String prezTitle = variant.getServiceConfig().getPresentationTitle();
        if (prezTitle == null) {
            EnumSiteFeature siteFeature = variant.getSiteFeature();
            prezTitle = siteFeature.getTitle();
            if (prezTitle == null) {
                prezTitle = siteFeature.getName();
            }
        }
        return prezTitle;
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
