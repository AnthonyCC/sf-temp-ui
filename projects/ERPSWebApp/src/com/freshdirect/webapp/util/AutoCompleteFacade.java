package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.cos.util.CosFeatureUtil;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.search.unbxd.UnbxdIntegrationService;
import com.freshdirect.webapp.search.unbxd.UnbxdSearchProperties;
import com.freshdirect.webapp.search.unbxd.UnbxdServiceUnavailableException;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * 
 * @author greg
 *
 *         Smart Search AutoComplete facade class
 */
public class AutoCompleteFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static Category LOGGER = LoggerFactory.getInstance(AutoCompleteFacade.class);

    private List<String> terms;

    public AutoCompleteFacade() {
        terms = new ArrayList<String>();
        terms.add("milk");
        terms.add("banana");
        terms.add("apple");
        terms.add("pie");
        terms.add("foo");
        terms.add("bar");

        Collections.sort(terms);
    }

    public List<String> getTerms(String prefix, HttpServletRequest request) {
        LOGGER.info("autocomplete for " + prefix);
        List<String> results;
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
        Cookie[] cookies = request.getCookies();

        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, cookies, user)) {
            try {
                UnbxdSearchProperties searchProperties = new UnbxdSearchProperties();
                boolean corporateSearch = CosFeatureUtil.isUnbxdCosAction(user, cookies);
                searchProperties.setCorporateSearch(corporateSearch);

                results = UnbxdIntegrationService.getDefaultService().suggestProducts(prefix, searchProperties);
            } catch (IOException e) {
                if (FDStoreProperties.getUnbxdFallbackOnError()) {
                    LOGGER.error("Error while calling UNBXD autocomplete, fallback to internal autocomplete");
                    results = ContentSearch.getInstance().getAutocompletions(prefix);
                } else {
                    LOGGER.error("Error while calling UNBXD autocomplete, fallback on error is false");
                    throw new UnbxdServiceUnavailableException("UNBXD autosuggest service is unavailable");
                }
            }
        } else {
            results = ContentSearch.getInstance().getAutocompletions(prefix);
        }
        return results;
    }

    public static AutoCompleteFacade create() {
        return new AutoCompleteFacade();
    }

}
