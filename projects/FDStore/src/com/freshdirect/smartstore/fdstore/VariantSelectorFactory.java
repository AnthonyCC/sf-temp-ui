package com.freshdirect.smartstore.fdstore;

import java.util.Iterator;
import java.util.Map;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.LruCache;
import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.smartstore.RecommendationService;

/**
 * Factory for variant selectors.
 * 
 * The factory maps site features to {@link VariantSelector variant selectors}
 * and implements the singleton pattern.
 * 
 * @author istvan
 * 
 */
public class VariantSelectorFactory {

    private static LruCache selectors = new TimedLruCache(10, 24*60*60*1000);

    

    /**
     * Returns the cohort name (eg. 'C1', ...) for a given user ID
     * @param erpUserId
     * @return
     */
    public static String getCohortName(String erpUserId) {
        return CohortSelector.getInstance().getCohortName(erpUserId);
    }

    /**
     * Get the appropriate variant selector.
     * 
     * If the site feature has not yet been exercised, its parameters will be
     * loaded and the instance created.
     * 
     * @param siteFeature
     * @return variant selector corresponding to site feature.
     */
    public synchronized static VariantSelector getInstance(final EnumSiteFeature siteFeature) {

        VariantSelector selector = (VariantSelector) selectors.get(siteFeature);
        if (selector == null) {

            selector = new VariantSelector();
            Map id2vrnt = SmartStoreServiceConfiguration.getInstance().getServices(siteFeature);

            VariantSelection helper = VariantSelection.getInstance();

            // fetch variant assignment (cohort -> variant map)
            Map assignment = helper.getVariantMap(siteFeature);
            // iterate over cohorts
            for (Iterator it = assignment.keySet().iterator(); it.hasNext();) {
                String cohortId = (String) it.next();
                selector.addCohort(cohortId, (RecommendationService) id2vrnt.get(assignment.get(cohortId)));
            }

            selectors.put(siteFeature, selector);
        }
        return selector;
    }
    
    public synchronized static void setVariantSelector(final EnumSiteFeature siteFeature, VariantSelector selector) {
        selectors.put(siteFeature, selector);
    }
    
}
