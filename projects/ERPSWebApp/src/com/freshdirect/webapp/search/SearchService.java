package com.freshdirect.webapp.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.ContentSearchUtil;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.search.unbxd.UnbxdIntegrationService;
import com.freshdirect.webapp.search.unbxd.UnbxdServiceUnavailableException;
import com.freshdirect.webapp.search.unbxd.dto.UnbxdSearchDidYouMean;
import com.freshdirect.webapp.search.unbxd.dto.UnbxdSearchResponseProduct;
import com.freshdirect.webapp.search.unbxd.dto.UnbxdSearchResponseRoot;
import com.freshdirect.webapp.taglib.unbxd.SearchEventTag;

public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getInstance(SearchService.class);

    private static final SearchService INSTANCE = new SearchService();

    public static SearchService getInstance() {
        return INSTANCE;
    }

    public SearchResults searchProducts(String searchTerm, Cookie[] cookies, FDUserI user, String requestUrl, String referer) {
        SearchResults searchResults = ContentSearch.getInstance().searchProducts(searchTerm);

        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, cookies, user)) {
            UnbxdSearchResponseRoot results;
            try {
                results = UnbxdIntegrationService.getDefaultService().searchProducts(searchTerm);

                if ((results.getResponse().getProducts() == null || results.getResponse().getProducts().isEmpty())
                        && (results.getDidYouMeans() != null && !results.getDidYouMeans().isEmpty())) {
                    searchResults.setSuggestedTerm(results.getDidYouMeans().get(0).getSuggestion());
                    searchResults.setSpellingSuggestions(new ArrayList<String>());
                    for (UnbxdSearchDidYouMean didYouMean : results.getDidYouMeans()) {
                        searchResults.getSpellingSuggestions().add(didYouMean.getSuggestion());
                    }
                    results = UnbxdIntegrationService.getDefaultService().searchProducts(results.getDidYouMeans().get(0).getSuggestion());
                }

                List<FilteringSortingItem<ProductModel>> products = new ArrayList<FilteringSortingItem<ProductModel>>();
                for (UnbxdSearchResponseProduct product : results.getResponse().getProducts()) {
                    ContentKey contentKey = product.getContentKey();
                    if (contentKey != null) {
                        ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNodeByKey(contentKey);
                        if ((contentNodeModel != null) && (contentNodeModel instanceof ProductModel) && ContentSearchUtil.isDisplayable((ProductModel) contentNodeModel)) {
                            products.add(new FilteringSortingItem<ProductModel>((ProductModel) contentNodeModel));
                        }
                    }
                }

                searchResults.getProducts().clear();
                searchResults.getProducts().addAll(products);
                
                //unbxd analytics
                SearchEventTag.doSendEvent(user, requestUrl, referer, searchTerm);

            } catch (IOException e) {
                if (!FDStoreProperties.getUnbxdFallbackOnError()) {
                    LOGGER.error("Error while calling unbxd search, fallback on error is false", e);
                    throw new UnbxdServiceUnavailableException("UNBXD search service is unavailable");
                } else {
                    LOGGER.error("Error while calling unbxd search, fallback to internal search", e);
                }

            }

        }

        return searchResults;
    }

}
