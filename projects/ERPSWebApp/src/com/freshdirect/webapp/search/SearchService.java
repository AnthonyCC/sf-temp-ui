package com.freshdirect.webapp.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
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

    private static class UnbxdSearchResult {

        public final String suggestedTerm;
        public final List<String> spellingSuggestions;

        public final List<UnbxdSearchResponseProduct> products;

        public UnbxdSearchResult(String suggestedTerm, List<String> spellingSuggestions, List<UnbxdSearchResponseProduct> products) {
            this.suggestedTerm = suggestedTerm;
            this.spellingSuggestions = spellingSuggestions;
            this.products = products;
        }
    }

    /**
     * Run UNBXD search operation with the given search term Repeat the search when no products yielded with search suggestions
     * 
     * @param searchTerm
     * @return result of the search operation wrapped into temporary class
     * 
     * @throws IOException
     *             search request failed
     */
    private static UnbxdSearchResult searchUnbxd(final String searchTerm) throws IOException {
        final UnbxdIntegrationService unbxdService = UnbxdIntegrationService.getDefaultService();

        final UnbxdSearchResult result;

        // do the search
        UnbxdSearchResponseRoot searchResult = unbxdService.searchProducts(searchTerm);

        // No products found, repeat the search with suggestion if any
        if (searchResult.getDidYouMeans() != null && !searchResult.getDidYouMeans().isEmpty()) {

            final String suggestedTerm = searchResult.getDidYouMeans().get(0).getSuggestion();

            // collect all suggestions
            final List<String> suggestions = new ArrayList<String>();
            for (UnbxdSearchDidYouMean dymItem : searchResult.getDidYouMeans()) {
                suggestions.add(dymItem.getSuggestion());
            }

            if (searchResult.getResponse().getProducts() == null || searchResult.getResponse().getProducts().isEmpty()) {
                LOGGER.debug("No product found for term '" + searchTerm + "'; look for suggested products using suggestion '" + suggestedTerm + "'");
                // repeat search with first suggested term
                searchResult = unbxdService.searchProducts(suggestedTerm);
                result = new UnbxdSearchResult(suggestedTerm, suggestions, searchResult.getResponse().getProducts());
            } else {
                result = new UnbxdSearchResult(null, suggestions, searchResult.getResponse().getProducts());
            }

        } else {
            result = new UnbxdSearchResult(null, Collections.<String> emptyList(), searchResult.getResponse().getProducts());
        }

        return result;
    }

    /**
     * Compose final search result from the ones made by Lucene and UNBXD
     * 
     * @param searchTerm
     *            original search term
     * @param luceneResult
     *            outcome of Lucene search
     * @param unbxdResult
     *            outcome of UNBXD search
     * 
     * @return composed search result
     */
    private static SearchResults composeSearchResults(final String searchTerm, SearchResults luceneResult, UnbxdSearchResult unbxdResult) {
        final List<FilteringSortingItem<ProductModel>> prods = filterProducts(unbxdResult.products);
        // FIXME - category hits are not supported yet
        final List<FilteringSortingItem<CategoryModel>> categories = Collections.emptyList();
        final boolean quoted = ContentSearchUtil.isQuoted(searchTerm);

        final SearchResults finalResult = new SearchResults(prods, luceneResult.getRecipes(), categories, searchTerm, quoted);

        finalResult.setSuggestedTerm(unbxdResult.suggestedTerm);
        finalResult.setSpellingSuggestions(unbxdResult.spellingSuggestions);

        return finalResult;
    }

    /**
     * Iterate over UNBXD product results and transforms them to ProductModel instances sorting out non-displayable ones
     * 
     * @param unbxdProducts
     * @return
     */
    private static List<FilteringSortingItem<ProductModel>> filterProducts(Collection<UnbxdSearchResponseProduct> unbxdProducts) {
        if (unbxdProducts == null || unbxdProducts.size() == 0)
            return Collections.emptyList();

        List<FilteringSortingItem<ProductModel>> result = new ArrayList<FilteringSortingItem<ProductModel>>(unbxdProducts.size());
        for (final UnbxdSearchResponseProduct unbxdProduct : unbxdProducts) {
            final ContentNodeModel model = ContentFactory.getInstance().getContentNodeByKey(unbxdProduct.getContentKey());
            if (model instanceof ProductModel && ContentSearchUtil.isDisplayable((ProductModel) model)) {
                result.add(new FilteringSortingItem<ProductModel>((ProductModel) model));
            }
        }

        return result;
    }

    public static SearchService getInstance() {
        return INSTANCE;
    }

    /**
     * Perform search operation
     * 
     * @param searchTerm
     *            search Term
     * @param cookies
     *            cookies (required by UNBXD feature check)
     * @param user
     *            (required by UNBXD feature check)
     * @param requestUrl
     * @param referer
     * @return
     */
    public SearchResults searchProducts(String searchTerm, Cookie[] cookies, FDUserI user, String requestUrl, String referer) {
        SearchResults searchResults = new SearchResults(); 
        if(searchTerm != null && searchTerm.trim().length() > 0){
            // get search results from Lucene service
            searchResults = ContentSearch.getInstance().searchProducts(searchTerm);
            
            if(FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, cookies, user)){
                // notify UNBXD analytics
                SearchEventTag.doSendEvent(user, requestUrl, referer, searchTerm);
            }
    
            if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, cookies, user)) {
                // when UNBXD service is turned on ...
                try {
                    // perform search with UNBXD as well
                    final UnbxdSearchResult internalResult = searchUnbxd(searchTerm);
    
                    // join results in a new one
                    searchResults = composeSearchResults(searchTerm, searchResults, internalResult);
    
                } catch (IOException e) {
                    if (!FDStoreProperties.getUnbxdFallbackOnError()) {
                        LOGGER.error("Error while calling unbxd search, fallback on error is false", e);
                        throw new UnbxdServiceUnavailableException("UNBXD search service is unavailable");
                    } else {
                        LOGGER.error("Error while calling unbxd search, fallback to internal search", e);
                    }
    
                }
    
            }
        }
        return searchResults;
    }
}
