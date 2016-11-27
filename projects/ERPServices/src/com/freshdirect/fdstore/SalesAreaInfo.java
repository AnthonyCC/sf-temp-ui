package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.framework.util.StringUtil;

/**
 * 
 * @author ksriram
 *
 */
public final class SalesAreaInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2188383684099715352L;
	private final String salesOrg;
	private final String distChannel;
	
	/**
	 * @param salesOrg
	 * @param distChannel
	 */
	/*public SalesAreaInfo(String salesOrg, String distChannel) {
		this.salesOrg = "1000".equals(salesOrg)?"0001":salesOrg;
		this.distChannel = "1000".equals(salesOrg)?"01":distChannel;
	}*/
	
	public SalesAreaInfo(String salesOrg, String distChannel) {
		this.salesOrg =StringUtil.isEmpty(salesOrg)?"0001":("1000".equals(salesOrg)?"0001":salesOrg);
		this.distChannel = StringUtil.isEmpty(distChannel)?"01":("1000".equals(distChannel)?"01":distChannel);
	}
	/**
	 * @return the salesOrg
	 */
	public String getSalesOrg() {
		return salesOrg;
	}
	/**
	 * @return the distChannel
	 */
	public String getDistChannel() {
		return distChannel;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SalesAreaInfo [salesOrg=" + salesOrg + ", distChannel="
				+ distChannel + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distChannel == null) ? 0 : distChannel.hashCode());
		result = prime * result
				+ ((salesOrg == null) ? 0 : salesOrg.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesAreaInfo other = (SalesAreaInfo) obj;
		if (distChannel == null) {
			if (other.distChannel != null)
				return false;
		} else if (!distChannel.equals(other.distChannel))
			return false;
		if (salesOrg == null) {
			if (other.salesOrg != null)
				return false;
		} else if (!salesOrg.equals(other.salesOrg))
			return false;
		return true;
	}
	
	
}
