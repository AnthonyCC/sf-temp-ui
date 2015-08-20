package com.freshdirect.common.pricing;

public final class ZoneInfo implements java.io.Serializable, Comparable<ZoneInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 440859145002964597L;
	private final String zoneId;
	private final String salesOrg;
	private final String distributionChanel;
	private final ZoneInfo parent;
	
	public ZoneInfo(String zoneId,String salesOrg,String distributionChanel,ZoneInfo parent) {
		this.zoneId=zoneId;
		this.salesOrg=salesOrg;
		this.distributionChanel=distributionChanel;
		this.parent=parent;
	}
	public ZoneInfo(String zoneId,String salesOrg,String distributionChanel) {
		this.zoneId=zoneId;
		this.salesOrg=salesOrg;
		this.distributionChanel=distributionChanel;
		this.parent=null;
	}

	public String getPricingZoneId() {
		return zoneId;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public String getDistributionChanel() {
		return distributionChanel;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((distributionChanel == null) ? 0 : distributionChanel
						.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result
				+ ((salesOrg == null) ? 0 : salesOrg.hashCode());
		result = prime * result + ((zoneId == null) ? 0 : zoneId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZoneInfo other = (ZoneInfo) obj;
		if (distributionChanel == null) {
			if (other.distributionChanel != null)
				return false;
		} else if (!distributionChanel.equals(other.distributionChanel))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (salesOrg == null) {
			if (other.salesOrg != null)
				return false;
		} else if (!salesOrg.equals(other.salesOrg))
			return false;
		if (zoneId == null) {
			if (other.zoneId != null)
				return false;
		} else if (!zoneId.equals(other.zoneId))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "ZoneInfo [zoneId=" + zoneId + ", salesOrg=" + salesOrg
				+ ", distributionChanel=" + distributionChanel + ", parent="
				+ parent + "]";
	}
	/**
	  * @param aThat is a non-null ZoneInfo.
	  *
	  * @throws NullPointerException if aThat is null.
	  */
	  @Override public int compareTo(ZoneInfo other) {
		 
	    
	    final int EQUAL = 0;
	    

	   
	    if (this == other) return EQUAL;

	   
	    
	    int comparison = this.zoneId.compareTo(other.zoneId);
	    if (comparison != EQUAL) return comparison;

	    comparison = this.salesOrg.compareTo(other.salesOrg);
	    if (comparison != EQUAL) return comparison;

	    comparison = this.distributionChanel.compareTo(other.distributionChanel);
	    if (comparison != EQUAL) return comparison;

	    comparison = this.parent.compareTo(other.parent);
	    if (comparison != EQUAL) return comparison;
	    
	    //verify that compareTo is consistent with equals (optional)
	    assert this.equals(other) : "compareTo inconsistent with equals.";

	    return EQUAL;
	  }
	
	public boolean hasParentZone() {
		return !(this.parent==null);
	}
	
	public ZoneInfo getParentZone() {
		return this.parent;
	}
	
	
}
