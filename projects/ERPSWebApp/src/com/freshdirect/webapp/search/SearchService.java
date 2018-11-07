package com.freshdirect.webapp.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentSearchUtil;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SearchResults;
import com.freshdirect.webapp.cos.util.CosFeatureUtil;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.search.unbxd.UnbxdIntegrationService;
import com.freshdirect.webapp.search.unbxd.UnbxdSearchProperties;
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
    private static UnbxdSearchResult searchUnbxd(final String searchTerm, UnbxdSearchProperties searchProperties) throws IOException {
        final UnbxdIntegrationService unbxdService = UnbxdIntegrationService.getDefaultService();

        final UnbxdSearchResult result;

        // do the search
        UnbxdSearchResponseRoot searchResult = unbxdService.searchProducts(searchTerm, searchProperties);

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
                searchResult = unbxdService.searchProducts(suggestedTerm, searchProperties);
                result = new UnbxdSearchResult(suggestedTerm, suggestions, searchResult.getResponse().getProducts());
            } else {
                result = new UnbxdSearchResult(searchTerm, suggestions, searchResult.getResponse().getProducts());
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
    private static SearchResults composeSearchResults(final String searchTerm, SearchResults luceneResult, UnbxdSearchResult unbxdResult, boolean isMobileRequest) {
        final List<FilteringSortingItem<ProductModel>> prods = filterProducts(unbxdResult.products);

        if (isMobileRequest) {
            // [APPDEV-7601] apply HIDE_IPHONE filter to products when request comes via Mobile API
            Iterator<FilteringSortingItem<ProductModel>> it = prods.iterator();
            while (it.hasNext()) {
                if (it.next().getModel().isHideIphone()) {
                    it.remove();
                }
            }
        }

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


    public SearchResults searchProducts(String searchTerm, Cookie[] cookies, FDUserI user, String requestUrl, String referer) {
    	return searchProducts(searchTerm, cookies, user, requestUrl, referer, true, true);
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
    public SearchResults searchProducts(String searchTerm, Cookie[] cookies, FDUserI user, String requestUrl, String referer, boolean searchRecipe, boolean isAutosuggest) {
        SearchResults searchResults = new SearchResults();

        boolean isUnbxdSearchEnabled = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, cookies, user) && !user.isRobot();
        if(user != null && user.isRobot()) {
        	LOGGER.warn("FDBOTSEARCH01 for " + user.getCookie() + ":" + searchTerm + ":" + requestUrl + ":" + referer );
        }
        if(searchTerm != null && searchTerm.trim().length() > 0){
            final boolean isMobileRequest = user != null && user.getApplication() != null && user.getApplication().isMobileBound();

			searchResults = isUnbxdSearchEnabled
					? (searchRecipe ? StoreServiceLocator.contentSearch().searchRecipes(searchTerm) : searchResults)
					: StoreServiceLocator.contentSearch().searchProducts(searchTerm);

			if (!isUnbxdSearchEnabled && isMobileRequest && !searchResults.isEmpty()) {
			    searchResults = applyMobileFilterToSearchResults(searchResults);
			}

			final boolean cosAction = CosFeatureUtil.isUnbxdCosAction(user, cookies);

            if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, cookies, user)) {
                // notify UNBXD analytics
                SearchEventTag.doSendEvent(user, requestUrl, referer, searchTerm, cosAction, isAutosuggest);
            }

            if (isUnbxdSearchEnabled) {
                // when UNBXD service is turned on ...
                try {
                    // perform search with UNBXD as well
                    UnbxdSearchProperties searchProperties = new UnbxdSearchProperties();
                    searchProperties.setCorporateSearch(cosAction);

                    final UnbxdSearchResult internalResult = searchUnbxd(searchTerm, searchProperties);

                    // join results in a new one
                    searchResults = composeSearchResults(searchTerm, searchResults, internalResult, isMobileRequest);

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

    private SearchResults applyMobileFilterToSearchResults(SearchResults searchResults) {
        List<FilteringSortingItem<ProductModel>> filteredProducts = filterFilteringProductListForMobileFrontEnd(searchResults.getProducts());
        Map<String, List<ProductModel>> filteredAssortProducts = filterProductMapForMobileFrontEnd(searchResults.getAssortProducts());
        List<FilteringSortingItem<ProductModel>> filteredAdProducts = filterFilteringProductListForMobileFrontEnd(searchResults.getAdProducts());
        List<ProductModel> filteredDDPPProducts = filterProductListForMobileFrontEnd(searchResults.getDDPPProducts());

        SearchResults newSearchResults = new SearchResults(filteredProducts, searchResults.getRecipes(), searchResults.getCategories(), searchResults.getSearchTerm(), searchResults.isPhrase());
        newSearchResults.setAssortProducts(filteredAssortProducts);
        newSearchResults.setAdProducts(filteredAdProducts);
        newSearchResults.setDDPPProducts(filteredDDPPProducts);

        newSearchResults.setSpellingSuggestions(searchResults.getSpellingSuggestions());
        newSearchResults.setEmptyProductsPageBeacon(searchResults.getEmptyProductsPageBeacon());
        newSearchResults.setHlProductsCount(searchResults.getHlProductsCount());

        return newSearchResults;
    }

    private Map<String, List<ProductModel>> filterProductMapForMobileFrontEnd(Map<String, List<ProductModel>> productsMap) {
        if (productsMap == null) {
            return null;
        }

        Map<String, List<ProductModel>> filteredMap = new HashMap<String, List<ProductModel>>();
        for (Map.Entry<String, List<ProductModel>> entry : productsMap.entrySet()) {
            filteredMap.put(entry.getKey(), filterProductListForMobileFrontEnd(entry.getValue()));
        }

        return filteredMap;
    }

    private List<FilteringSortingItem<ProductModel>> filterFilteringProductListForMobileFrontEnd(List<FilteringSortingItem<ProductModel>> products) {
        if (products == null) {
            return null;
        }

        List<FilteringSortingItem<ProductModel>> filteredProducts = new ArrayList<FilteringSortingItem<ProductModel>>();
        for (FilteringSortingItem<ProductModel> filteringItem : products) {
            if (!filteringItem.getModel().isHideIphone()) {
                filteredProducts.add(filteringItem);
            }
        }

        return filteredProducts;
    }

    private List<ProductModel> filterProductListForMobileFrontEnd(List<ProductModel> products) {
        if (products == null) {
            return null;
        }

        List<ProductModel> filteredProducts = new ArrayList<ProductModel>();
        for (ProductModel product : products) {
            if (!product.isHideIphone()) {
                filteredProducts.add(product);
            }
        }

        return filteredProducts;
    }
}
