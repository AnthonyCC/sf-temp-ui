package com.freshdirect.smartstore.fdstore;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.smartstore.Variant;

/**
 * A list of recommended contents tagged with a variant.
 * @author istvan
 *
 */
public class Recommendations implements Serializable {
	
	private static final long serialVersionUID = 8230385944777453868L;
	private Variant variant;
	private List contentNodes;
	
	
	/**
	 * Constructor.
	 * @param variant 
	 * @param contentKeys List<{@link ProductModel}>
	 */
	public Recommendations(Variant variant, List contentKeys) {
		this.variant = variant;
		this.contentNodes = contentKeys;
	}
	/**
	 * Get recommended content keys.
	 * @return List<{@link ProductModel}>
	 */
	public List getContentNodes() {
		return contentNodes;
	}
	
	/**
	 * Get variant.
	 * @return variant
	 */
	public Variant getVariant() {
		return variant;
	}

}
