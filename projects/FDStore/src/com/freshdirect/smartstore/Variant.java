package com.freshdirect.smartstore;

import java.io.Serializable;

import com.freshdirect.fdstore.util.EnumSiteFeature;

/**
 * SmartStore algorithm variant.
 * 
 * An instance represents a configured algorithm. 
 * 
 * @author istvan
 *
 */
public class Variant implements Serializable, Comparable {
	/**
	 * Variant used for "empty" recommendations.
	 * @author segabor
	 */
	public static final Variant BAD_VARIANT = 
		new Variant("(invalid)", EnumSiteFeature.DYF, null);

	// generated id
	private static final long serialVersionUID = -2411779130333500328L;

	// site feature
	private EnumSiteFeature siteFeature;
	
	// service type
	private RecommendationServiceConfig serviceConfig;
	
	// id of variant
	private String id;
	
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
	public Variant(String id, EnumSiteFeature siteFeature, RecommendationServiceConfig serviceConfig) {
		this.id = id;
		this.siteFeature = siteFeature;
		this.serviceConfig = serviceConfig;
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
	
	
}
