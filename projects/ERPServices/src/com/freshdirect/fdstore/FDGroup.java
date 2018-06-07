package com.freshdirect.fdstore;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FDGroup implements Serializable {

	private static final long	serialVersionUID	= -5832100527957081984L;

	private final String groupId;
	
	private final int version;
    
	private boolean skipProductPriceValidation; 
    
    public FDGroup(FDGroup group) {
    	this(group.getGroupId(), group.getVersion()); 
    }

	public FDGroup(@JsonProperty("groupId") String groupId, @JsonProperty("version") int version) {
		if (groupId == null)
			throw new IllegalArgumentException("Group ID cannot be null");
		this.groupId = groupId;
		this.version = version;
	}


	public void setSkipProductPriceValidation(boolean skipValidation) {
		this.skipProductPriceValidation = skipValidation;
	}

	public boolean isSkipProductPriceValidation() {
		return skipProductPriceValidation;
	}

	/**
	 * Get Group Id.
	 *
	 * @return Group Id
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * Get the business object version.
	 *
	 * @return version number
	 */
	public int getVersion() {
		return this.version;
	}
	
	@Override
	public String toString() {
		return "FDGroup[" + this.groupId + ", " + this.version + "]";
	}
	
	@Override
	public final int hashCode() {
		return this.groupId.hashCode() ^ this.version;
	}

	@Override
	public final boolean equals(Object o) {
		if (!(o instanceof FDGroup)) return false;
		FDGroup group = (FDGroup)o;
		return this.groupId.equals(group.groupId) && (this.version==group.version);
	}
	
	
	public String getGroupPageUrl( String skuCode ) {
        return "/group.jsp?grpId=" + groupId + "&version=" + version + "&skuCode=" + skuCode;		
	}

}