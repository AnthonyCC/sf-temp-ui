package com.freshdirect.smartstore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.Collections;


/**
 * Recommendation service configuration.
 * 
 * @author istvan
 *
 */
public class RecommendationServiceConfig implements Serializable {

    public static final String PIP_DEFAULT_LABEL = "YOUR FAVORITES";
    public static final String PIP_DEFAULT_INNERTEXT = "These are some of the items you've purchased most often.";
    
    
	/** Id of the variant. */
	protected String variantId;
	
	/** Service type. */
	protected RecommendationServiceType type;

	protected Map params;
	
	/**
	 * Constructor.
	 * @param name
	 */
	public RecommendationServiceConfig(String variantId, RecommendationServiceType type) {
		this.variantId = variantId;
		this.type = type;
	}

	// auto generated serial version id
	private static final long serialVersionUID = -5942360122361860134L;
	
	/**
	 * Get the hash code.
	 * @see #equals(Object)
	 * @return {@link #getVariantId()}.{@link String#hashCode()}
	 */
	public int hashCode() {
		return getVariantId().hashCode();
	}
	
	/**
	 * Equality.
	 * Two configurations are the same if they have the same name.
	 * Configurations have id's in the corresponding database table.
	 * @return if the two configs have the same name
	 */
	public boolean equals(Object o) {
		if (!(o instanceof RecommendationServiceConfig)) return false;
		return ((RecommendationServiceConfig)o).getVariantId().equals(getVariantId());
	}
	
	/**
	 * Get config name.
	 * @return name
	 */
	public String getVariantId() {
		return variantId;
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
	 * @return {@link #getVariantId()}
	 */
	public String toString() {
		return getVariantId();
	}
	

	public String get(String key) {
	    return (String) (params!=null ? params.get(key) : null);
	}

        public String get(String key, String defaultValue) {
            String value = (String) (params!=null ? params.get(key) : defaultValue);
            if (value==null) {
                return defaultValue;
            }
            return value;
        }
        
        public String getPresentationTitle() {
            return get("prez_title",PIP_DEFAULT_LABEL);
        }
        
        public String getPresentationDescription() {
            return get("prez_desc", PIP_DEFAULT_INNERTEXT);
        }
	
	public RecommendationServiceConfig set(String key, String value) {
	    if (params==null) {
	        params = new HashMap();
	    }
	    params.put(key, value);
	    return this;
	}
	
	public Set keys() {
		return params != null ? params.keySet() : Collections.emptySet();
	}
}
