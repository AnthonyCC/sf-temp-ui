package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

/**
 * RecommenderStrategy represents a selection strategy for Smart YMAL Recommenders.<br/>
 * It has a <i>generator expression</i>, <i>scoring expression</i>, and <i>attributes</i> : sampling, topN, topPercent, exponent (for power sampling).
 * 
 * @author csongor
 * @author treer
 *  
 * @see Recommender
 * @see SmartYMALRecommendationService
 * @see {@link YmalSet}	
 * 
 * @since 0903_R1
 */

public class RecommenderStrategy extends ContentNodeModelImpl {

	private static final long serialVersionUID = -3242163864963292458L;

	public RecommenderStrategy(ContentKey key) {
		super(key);
	}

	public String getDescription() {
		return getFullName();
	}
	
	public String getGenerator() {
		return getAttribute("generator", "" );
	}
	
	public String getScoring() {
		return getAttribute("scoring", null );
	}
	
	public String getSampling() {
		return getAttribute("sampling", EnumSampling.DETERMINISTIC );
	}
	
	public int getTopN() {
		return getAttribute("top_n", 20 );
	}
	
	public double getTopPercent() {
		return getAttribute("top_percent", 20.0 );
	}
	
	public double getExponent() {
		return getAttribute("exponent", 0.66 );
	}
	
	public boolean isShowTemporaryUnavailable() {
	    return getAttribute("show_temp_unavailable", false);
	}

	public boolean isBrandUniqSort() {
	    return getAttribute("brand_uniq_sort", false);
	}

}
