package com.freshdirect.smartstore.fdstore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;

/**
 * Factory for variant selectors.
 * 
 * The factory maps site features to {@link VariantSelector variant selectors}
 * and implements the singleton pattern.
 * @author istvan
 *
 */
public class VariantSelectorFactory {

	private static Map selectors = new HashMap();
	
	/**
	 * Get the appropriate variant selector.
	 * 
	 * If the site feature has not yet been exercised, its parameters
	 * will be loaded and the instance created.
	 * 
	 * @param siteFeature
	 * @return variant selector corresponding to site feature.
	 */
	public synchronized static VariantSelector getInstance(final EnumSiteFeature siteFeature) {
		
		VariantSelector selector = (VariantSelector)selectors.get(siteFeature);
		if (selector == null) {
			if (siteFeature.equals(EnumSiteFeature.DYF)) {
				selector = new VariantSelector() {
					
					protected void init() {
						Map id2vrnt = SmartStoreServiceConfiguration.getInstance().getServices(siteFeature);
						
						
						VariantSelection helper = VariantSelection.getInstance();
						
						// fetch variant assingment (cohort -> variant map)
						Map assignment = helper.getVariantMap(EnumSiteFeature.DYF);
						
						// fetch cohort -> weight map
						Map cohorts = helper.getCohorts();
						
						// iterate over cohorts
						for (Iterator it=assignment.keySet().iterator(); it.hasNext(); ) {
							String cohortId = (String) it.next();
							Integer weight = (Integer) cohorts.get(cohortId);
							addCohort(cohortId,
								(RecommendationService) id2vrnt.get(assignment.get(cohortId)),
								weight.intValue());
						}
					}			
				};
			}
			
			selector.init();
			selectors.put(siteFeature,selector);
		}
		return selector;
	}
}
