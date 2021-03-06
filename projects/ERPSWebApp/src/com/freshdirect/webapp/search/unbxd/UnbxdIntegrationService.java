package com.freshdirect.webapp.search.unbxd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.UnbxdAutosuggestResults;
import com.freshdirect.framework.marker.ThirdPartyIntegration;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.http.HttpService;
import com.freshdirect.webapp.autosuggest.unbxd.dto.UnbxdAutosuggestResponseRoot;
import com.freshdirect.webapp.search.unbxd.dto.UnbxdSearchResponseRoot;

/**
 * This is the service which communicates with the unbxd search and autossuggest endpoints
 *
 */
public class UnbxdIntegrationService implements ThirdPartyIntegration {

    private static final Logger LOGGER = LoggerFactory.getInstance(UnbxdIntegrationService.class);

    private static final Integer SEARCH_ROW_SIZE = 1000;
    private static final Integer MAX_AUTOSUGGEST_NUMBER = 20;

    private static final UnbxdIntegrationService INSTANCE = new UnbxdIntegrationService();

    private HttpService httpService = HttpService.defaultService();

    public static UnbxdIntegrationService getDefaultService() {
        return INSTANCE;
    }

    public UnbxdSearchResponseRoot searchProducts(String searchTerm, UnbxdSearchProperties searchProperties) throws IOException {
        UnbxdSearchResponseRoot results = httpService.getData(buildSearchUri(searchTerm, 1, searchProperties), UnbxdSearchResponseRoot.class);
        if ((results.getResponse().getNumberOfProducts() != null) && (results.getResponse().getNumberOfProducts().compareTo(SEARCH_ROW_SIZE) == 1)) {
            UnbxdSearchResponseRoot resultsPageTwo = httpService.getData(buildSearchUri(searchTerm, 2, searchProperties), UnbxdSearchResponseRoot.class);
            results.getResponse().getProducts().addAll(resultsPageTwo.getResponse().getProducts());
        }

        return results;
    }

    public List<UnbxdAutosuggestResults> suggestProducts(String term, UnbxdSearchProperties searchProperties) throws IOException {
        List<UnbxdAutosuggestResults> autosuggests = new ArrayList<UnbxdAutosuggestResults>();
        UnbxdAutosuggestResponseRoot results = httpService.getData(UnbxdUrlFactory.getAutoSuggestUrl(searchProperties) + URLEncoder.encode(term, CharEncoding.UTF_8),
                UnbxdAutosuggestResponseRoot.class);
        for (int i = 0; i < results.getResponse().getProducts().size() && i < MAX_AUTOSUGGEST_NUMBER; i++) {
        	results.getResponse().getProducts().get(i).setInternalQuery(term); /* we are storing internalQuery to re-use for payload on next Unbxd API */
            autosuggests.add(results.getResponse().getProducts().get(i));
        }
        return autosuggests;
    }

    private String buildSearchUri(String searchTerm, int page, UnbxdSearchProperties searchProperties) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder(UnbxdUrlFactory.getSearchUrl(searchProperties));
        stringBuilder.append(URLEncoder.encode(searchTerm, CharEncoding.UTF_8)).append("&rows=").append(SEARCH_ROW_SIZE).append("&page=").append(page).append("&fields=uniqueId");
        return stringBuilder.toString();
    }
}
