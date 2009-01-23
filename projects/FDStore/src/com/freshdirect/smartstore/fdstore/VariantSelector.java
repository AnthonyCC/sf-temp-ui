package com.freshdirect.smartstore.fdstore;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.smartstore.RecommendationService;

/**
 * Variant selection based on cohort name or user id.
 * 
 * Variants (aka particular {@link RecommendationService recommendation services}) are assigned based on
 * user ids (cohorts). The two public method is {@link #select(String)}, or the {@link #getService(String)}
 * 
 * 
 * For each site feature these cohorts can be assigned differently to the n variants.
 * This class first identifies which cohort the user belongs to and then returns the corresponding
 * variant.
 * 
 * 
 * @author istvan
 * @author zsombor
 */
public class VariantSelector {
	
    Map services = new HashMap();
    
    
    public void addCohort(String cohortName, RecommendationService service) {
        this.services.put(cohortName, service);
    }
    
    public RecommendationService getService(String cohortName) {
        return (RecommendationService) services.get(cohortName);
    }
	
    /**
     * Select a variant based on user id.
     * @param erpUserId user id
     * @return variant
     */
    public RecommendationService select(String erpUserId) {
        String cohortName = CohortSelector.getInstance().getCohortName(erpUserId);
        return (RecommendationService) services.get(cohortName);
    }
    
}
