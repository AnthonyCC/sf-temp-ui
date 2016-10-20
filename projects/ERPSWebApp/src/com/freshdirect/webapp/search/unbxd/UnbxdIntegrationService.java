package com.freshdirect.webapp.search.unbxd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.http.HttpService;
import com.freshdirect.webapp.autosuggest.unbxd.dto.UnbxdAutosuggestResponseRoot;
import com.freshdirect.webapp.search.unbxd.dto.UnbxdSearchResponseRoot;

public class UnbxdIntegrationService {

    private static final Logger LOGGER = LoggerFactory.getInstance(UnbxdIntegrationService.class);

    private static final String BASE_URL = FDStoreProperties.getUnbxdBaseUrl() + "/" + FDStoreProperties.getUnbxdApiKey() + "/" + FDStoreProperties.getUnbxdSiteKey();
    private static final String SEARCH_ENDPOINT = BASE_URL + "/search?q=";
    private static final String AUTOSUGGEST_ENDPOINT = BASE_URL + "/autosuggest?q=";
    private static final Integer SEARCH_ROW_SIZE = 1000;
    private static final Integer MAX_AUTOSUGGEST_NUMBER = 20;

    private static final UnbxdIntegrationService INSTANCE = new UnbxdIntegrationService();

    private HttpService httpService = HttpService.defaultService();

    public static UnbxdIntegrationService getDefaultService() {
        return INSTANCE;
    }

    public UnbxdSearchResponseRoot searchProducts(String searchTerm) throws IOException {
        UnbxdSearchResponseRoot results = httpService.getData(buildSearchUri(searchTerm, 1), UnbxdSearchResponseRoot.class);
        if ((results.getResponse().getNumberOfProducts() != null) && (results.getResponse().getNumberOfProducts().compareTo(SEARCH_ROW_SIZE) == 1)) {
            UnbxdSearchResponseRoot resultsPageTwo = httpService.getData(buildSearchUri(searchTerm, 2), UnbxdSearchResponseRoot.class);
            results.getResponse().getProducts().addAll(resultsPageTwo.getResponse().getProducts());
        }

        return results;
    }

    public List<String> suggestProducts(String term) throws IOException {
        List<String> autosuggests = new ArrayList<String>();
        UnbxdAutosuggestResponseRoot results = httpService.getData(AUTOSUGGEST_ENDPOINT + URLEncoder.encode(term, CharEncoding.UTF_8), UnbxdAutosuggestResponseRoot.class);
        for (int i = 0; i < results.getResponse().getProducts().size() && i < MAX_AUTOSUGGEST_NUMBER; i++) {
            autosuggests.add(results.getResponse().getProducts().get(i).getAutosuggest());
        }
        return autosuggests;
    }

    private String buildSearchUri(String searchTerm, int page) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder(SEARCH_ENDPOINT);
        stringBuilder.append(URLEncoder.encode(searchTerm, CharEncoding.UTF_8)).append("&rows=").append(SEARCH_ROW_SIZE).append("&page=").append(page).append("&fields=uniqueId");
        return stringBuilder.toString();
    }
}
