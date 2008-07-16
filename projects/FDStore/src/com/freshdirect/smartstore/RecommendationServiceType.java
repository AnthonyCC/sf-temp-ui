package com.freshdirect.smartstore;

import java.io.Serializable;

import org.apache.commons.lang.enums.Enum;

/**
 * Type of recommendation service.
 * @author istvan
 *
 */
public class RecommendationServiceType extends Enum implements Serializable {

	// generated id.
	private static final long serialVersionUID = -14506744566071264L;

	/** DYF: frequently bought. */
	public final static RecommendationServiceType FREQUENTLY_BOUGHT_DYF = 
		new RecommendationServiceType("freqbought_dyf");
	
	/** DYF: random selection. */	
	public final static RecommendationServiceType RANDOM_DYF =
		new RecommendationServiceType("random_dyf");
	
	/** _ANY_: no recommendation. */
	public final static RecommendationServiceType NIL =
		new RecommendationServiceType("nil");
	
	/** _ANY_: composite configuration; services made up of services. */
	public final static RecommendationServiceType COMPOSITE =
		new RecommendationServiceType("composite");
	
	/**
	 * Constructor.
	 * Not public, since it is a constant.
	 * @param name
	 */
	protected RecommendationServiceType(String name) {
		super(name);
	}
	
	/**
	 * Helper to make enum by name.
	 * @param name
	 * @return existing enum or null
	 */
	public static RecommendationServiceType getEnum(String name) {
		return (RecommendationServiceType)getEnum(RecommendationServiceType.class, name);
	}

	
}
