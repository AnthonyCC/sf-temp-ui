package com.freshdirect.smartstore;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.fdstore.content.EnumBurstType;
import com.freshdirect.fdstore.util.EnumSiteFeature;

/**
 * SmartStore algorithm variant.
 * 
 * An instance represents a configured algorithm. 
 * 
 * @author istvan
 *
 */
public class Variant implements Comparable, Serializable {

	// site feature
	private EnumSiteFeature siteFeature;
	
	// service type
	private RecommendationServiceConfig serviceConfig;
	
	// id of variant
	private String id;
	
	private SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> tabStrategyPriorities;
	
	private boolean isSmartSavings = false;

	private RecommendationService recommender;


	/**
	 * Include alternative products in availability check
	 */
	private boolean useAlternatives = true;
	
	
	private Set<EnumBurstType> hideBursts;
	
	/**
	 * Get identifier.
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get site feature.
	 * @return site feature
	 */
	public EnumSiteFeature getSiteFeature() {
		return this.siteFeature;
	}
	
	/**
	 * Get service type.
	 * @return service type
	 */
	public RecommendationServiceConfig getServiceConfig() {
		return this.serviceConfig;
	}

	/**
	 * Constructor.
	 * @param id
	 * @param siteFeature {@link EnumSiteFeature}
	 */
	public Variant(String id, EnumSiteFeature siteFeature,
			RecommendationServiceConfig serviceConfig) {
		this.id = id;
		this.siteFeature = siteFeature;
		this.serviceConfig = serviceConfig;
		this.tabStrategyPriorities = new TreeMap<Integer, SortedMap<Integer, CartTabStrategyPriority>>();
	}
	
	public Variant(String id, EnumSiteFeature siteFeature,
			RecommendationServiceConfig serviceConfig, SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> tabStrategyPriorities) {
		this.id = id;
		this.siteFeature = siteFeature;
		this.serviceConfig = serviceConfig;
		this.tabStrategyPriorities = tabStrategyPriorities;
	}

	/**
	 * Compare method.
	 * @param other variant
	 * @return result of comparison on id
	 */
	public int compareTo(Object other) { 
		return getId().compareTo(((Variant)other).getId());
	}
	
	/**
	 * Hash code.
	 * @return getId().hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}
	
	/**
	 * Equality.
	 * @param o object
	 * @return whether the parameter is a {@link Variant} and has the same {@link #getId() id}
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Variant)) return false;
		else return ((Variant)o).getId().equals(getId());
	}

	
    public String toString() {
        return "Variant(" + id + ',' + siteFeature + ',' + serviceConfig + ',' + 
            (serviceConfig != null && serviceConfig.getType()!=null ? serviceConfig.getType().getName() : "null") + ')';
    }
	
    public SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> getTabStrategyPriorities() {
		return tabStrategyPriorities;
	}
   
    public boolean isSmartSavings() {
		return isSmartSavings;
	}
    
    public void setSmartSavings(boolean isSmartSavings) {
		this.isSmartSavings = isSmartSavings;
	}

	public void setRecommender(RecommendationService recommender) {
		this.recommender = recommender;
	}

	public RecommendationService getRecommender() {
		return recommender;
	}

	public void setUseAlternatives(boolean useAlternatives) {
		this.useAlternatives = useAlternatives;
	}

	public boolean isUseAlternatives() {
		return useAlternatives;
	}

	public Set<EnumBurstType> getHideBursts() {
		return hideBursts;
	}

	/**
	 * Specify bursts to hide on front-end
	 * @param hideBursts
	 */
	public void setHideBursts(Set<EnumBurstType> hideBursts) {
		this.hideBursts = hideBursts;
	}
}
