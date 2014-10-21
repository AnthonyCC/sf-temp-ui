package com.freshdirect.fdstore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.external.CachingExternalRecommender;
import com.freshdirect.smartstore.external.ExternalRecommender;
import com.freshdirect.smartstore.external.ExternalRecommenderRegistry;
import com.freshdirect.smartstore.external.ExternalRecommenderType;
import com.freshdirect.smartstore.external.NoSuchExternalRecommenderException;
import com.freshdirect.smartstore.external.certona.CertonaInfrastructure;
import com.freshdirect.smartstore.external.certona.CertonaRecommender;
import com.freshdirect.smartstore.fdstore.VariantSelector;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;


/**
 * Utility class created for the transitional period of
 * switching SmartStore to Certona service.
 * 
 * It helps deciding whether a variant assigned to certain site features
 * may serve Certona recommendations or stay with Scarab
 * 
 * NOTE: this class may be removed in the future when
 * transitional period ends
 * 
 * @author segabor
 *
 * @ticket APPDEV-3863
 *
 */
public class CertonaTransitionUtil {
	private static final Logger LOGGER = Logger.getLogger(CertonaTransitionUtil.class.getName());

	/**
	 * Global flag indicating that transitional period is live
	 * False value means that period is over and this class
	 * should not be used anymore
	 * 
	 * FIXME: is it useful?
	 */
	public static final boolean TRANSITIONAL_PERIOD = true;

	static {
		if (TRANSITIONAL_PERIOD) {
			LOGGER.info("Certona transition period is active");
		} else {
			LOGGER.warn("Certona transition period is over. This logic is no longer used.");
		}
	}


	/**
	 * Predefined list of site features that may contain
	 * mixed scarab/certona variants
	 * 
	 * @ticket APPDEV-3863
	 * NOTE: this featuer may be removed in the future
	 * 
	 */
	public static final String[] CERTONA_SF = new String[] {
		"RIGHT_NAV_PERS", "RIGHT_NAV_RLTD",
		"BRWS_CAT_LST", "BRWS_PRD_LST",
		"YMAL", "YMAL_PDTL",
		"SRCH"
	};



	/**
	 * Helper method to decide if particular site feature
	 * is affected in serving mixed certona/scarab variants
	 * 
	 * @param feature
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isCertonaRelated(EnumSiteFeature feature) {
		if (TRANSITIONAL_PERIOD == false) {
			LOGGER.warn("Method invoked after transitionl period is over.");
		}


		if (feature == null) {
			return false;
		}
		
		final String featName = feature.getName();
		for (String sfName : CERTONA_SF) {
			if (sfName.equalsIgnoreCase( featName)) {
				LOGGER.debug(featName + " is identified as Certona SF");
				return true;
			}
		}
		
		return false;
	}



	@SuppressWarnings("unused")
	public static boolean isCertonaBasedCohort(final String cohortName, final EnumSiteFeature siteFeature) {
		if (TRANSITIONAL_PERIOD == false) {
			LOGGER.warn("Method invoked after transitionl period is over.");
		}

		
		if (cohortName == null || siteFeature == null) {
			return false;
		}

		// only certain site features are set to contain certona variants
		if (!isCertonaRelated(siteFeature) ) {
			return false;
		}

		final boolean isCertonaBased = getCertonaInfo(siteFeature, cohortName) != null;

		if (isCertonaBased) {
			LOGGER.debug("Cohort " + cohortName + " gets Certona recommendations for site feature " + siteFeature.getName());
		} else {
			LOGGER.debug("Cohort " + cohortName + " gets legacy recommendations for site feature " + siteFeature.getName());
		}
		return isCertonaBased;
	}
	
	public static class CertonaInfo {
		// public EnumSiteFeature siteFeature;
		public String cohortName;
		public Variant variant;
		/** @see registered names {@link CertonaInfrastructure} */
		public String certonaRecommenderName; 
		public String certonaScheme;
	}


	public static CertonaInfo getCertonaInfo(final EnumSiteFeature siteFeature, final String cohortName) {
		if (cohortName == null || siteFeature == null) {
			return null;
		}

		// pick variant set for the given site feature
		final VariantSelector selector = VariantSelectorFactory.getSelector(siteFeature);
		
		LOGGER.debug("Pick variant for cohort " + cohortName);
		final Variant v = selector.getVariant(cohortName);
		if (v == null || v.getRecommender() == null) {
			LOGGER.error("Incomplete/bogus variant returned, fall back to legacy");
			return null;
		}

		// final boolean isCertonaBased = v.getRecommender() instanceof CertonaRecommender;
		boolean isCertonaBased = false;
		RecommendationService r = v.getRecommender();
		if (r instanceof ScriptedRecommendationService) {
			ScriptedRecommendationService srs = (ScriptedRecommendationService) r;
			final String gen = srs.getGenerator().toString();
			
			// extract certona recommender from generator name
			Pattern p = Pattern.compile("\\w+_(\\w+)\\(\\)");
			Matcher m = p.matcher(gen);
			if (m.matches() && m.groupCount() > 0) {
				String n = m.group(1);
				// String n should match one of certona recommender names
				// stored in {@link CertonaInfrastructure} class
				isCertonaBased = n != null && n.startsWith("certona");
				
				if (isCertonaBased) {
					CertonaInfo ci = new CertonaInfo();
					
					// ci.siteFeature = siteFeature;
					ci.cohortName = cohortName;
					ci.variant = r.getVariant();
					ci.certonaRecommenderName = n;

					for (ExternalRecommenderType t : ExternalRecommenderType.values()) {
						ExternalRecommender recommender = null;
						try {
							recommender = ExternalRecommenderRegistry.getInstance(n, t);
						} catch (IllegalArgumentException e) {
						} catch (NoSuchExternalRecommenderException e) {
						}
						
						if (recommender instanceof CachingExternalRecommender) {
							recommender = ((CachingExternalRecommender) recommender).getOriginalRecommender();
						}

						if (recommender instanceof CertonaRecommender) {
							CertonaRecommender cRec = (CertonaRecommender) recommender;
							ci.certonaScheme = cRec.getScheme();
							break;
						}
					}

					return ci;
				}
			}
		}
		
		return null;
	}
}
