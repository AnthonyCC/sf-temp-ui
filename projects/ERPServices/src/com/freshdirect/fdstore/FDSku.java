package com.freshdirect.fdstore;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Lightweight class representing a SKU code / version pair.
 * 
 */
public class FDSku implements Serializable {

	private static final long	serialVersionUID	= -535972609840131406L;

	/** SKU code */
	private final String skuCode;
	
	/** Business object version */
	private final int version;
    
    
    public FDSku(FDSku sku) {
    	this(sku.getSkuCode(), sku.getVersion()); 
    }

	@JsonCreator
	public FDSku(@JsonProperty("skuCode") String skuCode, @JsonProperty("version") int version) {
		this.skuCode = skuCode;
		this.version = version;
	}
	
	/**
	 * Get SKU code.
	 *
	 * @return SKU code
	 */
	public String getSkuCode() {
		return this.skuCode;
	}

	/**
	 * Get the business object version.
	 *
	 * @return version number
	 */
	public int getVersion() {
		return this.version;
	}
	
	public String toString() {
		return "FDSku[" + this.skuCode + ", " + this.version + "]";
	}
	
	public final int hashCode() {
		return this.skuCode.hashCode() ^ this.version;
	}

	public final boolean equals(Object o) {
		if (!(o instanceof FDSku)) return false;
		FDSku sku = (FDSku)o;
		return this.skuCode.equals(sku.skuCode) && (this.version==sku.version);
	}

}