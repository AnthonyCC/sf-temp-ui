package com.freshdirect.smartstore;

import java.io.Serializable;

import com.freshdirect.fdstore.util.EnumSiteFeature;


/**
 * An object representing the source (cause or trigger) of a recommendation.
 * @author istvan
 *
 */
public class Trigger implements Serializable {
	
	private static final long serialVersionUID = 5490518702714189684L;
	
	private EnumSiteFeature siteFeature;
	
	
	private int maxRecommendations;
	
	
	/**
	 * Site feature of recommendation service.
	 * Identifies the site source.
	 * @return site feature
	 */
	public EnumSiteFeature getSiteFeature() {
		return siteFeature;
	}
	
	/**
	 * Get the maximum number of recommendations the site feature can handle.
	 * @return max recommendations
	 */
	public int getMaxRecommendations() {
		return maxRecommendations;
	}
	
	/**
	 * Constructor.
	 * @param siteFeature site feature 
	 * @param maxRecommendations maximum recommendations rendered
	 */
	public Trigger(EnumSiteFeature siteFeature, int maxRecommendations) {
		this.siteFeature = siteFeature;
		this.maxRecommendations = maxRecommendations;
	}
	
	

}
