package com.freshdirect.smartstore;

import java.io.Serializable;


/**
 * Recommendation service configuration.
 * 
 * @author istvan
 *
 */
public class RecommendationServiceConfig implements Serializable {
	
	/** Name of service config. */
	protected String name;
	
	/** Service type. */
	protected RecommendationServiceType type;

	/**
	 * Constructor.
	 * @param name
	 */
	public RecommendationServiceConfig(String name, RecommendationServiceType type) {
		this.name = name;
		this.type = type;
	}

	// auto generated serial version id
	private static final long serialVersionUID = -5942360122361860134L;
	
	/**
	 * Get the hash code.
	 * @see #equals(Object)
	 * @return {@link #getName()}.{@link String#hashCode()}
	 */
	public int hashCode() {
		return getName().hashCode();
	}
	
	/**
	 * Equality.
	 * Two configurations are the same if they have the same name.
	 * Configurations have id's in the corresponding database table.
	 * @return if the two configs have the same name
	 */
	public boolean equals(Object o) {
		if (!(o instanceof RecommendationServiceConfig)) return false;
		return ((RecommendationServiceConfig)o).getName().equals(getName());
	}
	
	/**
	 * Get config name.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get config type.
	 * @return config type
	 */
	public RecommendationServiceType getType() {
		return type;
	}
	
	/**
	 * As string.
	 * @return {@link #getName()}
	 */
	public String toString() {
		return getName();
	}
	
	
}
