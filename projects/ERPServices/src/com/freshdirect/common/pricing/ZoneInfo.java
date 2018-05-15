package com.freshdirect.common.pricing;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ZoneInfo implements java.io.Serializable, Comparable<ZoneInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 440859145002964597L;
	private final String zoneId;
	private final String salesOrg;
	private final String distributionChanel;
	private final ZoneInfo parent;

	public enum PricingIndicator {
		BASE("B"), SALE("S");

		private PricingIndicator(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		private String value;
	};

	private final PricingIndicator pricingIndicator;

	@JsonCreator
	public ZoneInfo(@JsonProperty("zoneId") String zoneId, @JsonProperty("salesOrg") String salesOrg,
			@JsonProperty("distributionChanel") String distributionChanel,
			@JsonProperty("pricingIndicator") PricingIndicator pricingIndicator, @JsonProperty("parent") ZoneInfo parent) {
		this.zoneId = zoneId;
		this.salesOrg = salesOrg;
		this.distributionChanel = distributionChanel;
		this.parent = parent;
		this.pricingIndicator = pricingIndicator;
	}

	public ZoneInfo(String zoneId, String salesOrg, String distributionChanel) {
		this.zoneId = zoneId;
		this.salesOrg = salesOrg;
		this.distributionChanel = distributionChanel;
		this.parent = null;
		this.pricingIndicator = PricingIndicator.SALE;
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
		result = prime * result + ((distributionChanel == null) ? 0 : distributionChanel.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((salesOrg == null) ? 0 : salesOrg.hashCode());
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

	public String stringWithDelimter(String delimter) {
		if (salesOrg == null || distributionChanel == null || zoneId == null)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append(salesOrg).append(delimter).append(distributionChanel).append(delimter).append(zoneId).append(delimter)
				.append(this.pricingIndicator);
		if (parent == null)
			return sb.toString();

		String parentStringified = parent.stringWithDelimter(delimter);
		if (parentStringified != null && !parentStringified.isEmpty()) {
			sb.append(delimter).append(parentStringified);
		}
		return sb.toString();
	}

	/**
	 * Recursively builds zonInfo object with infinite parents from passed string
	 * array
	 * 
	 * @param toDistribute
	 * @return
	 */
	public static ZoneInfo distributeIntoObject(String[] toDistribute) {
		ZoneInfo zi = null;
		if (toDistribute.length > 0) {

			if (toDistribute.length == 3) {
				zi = new ZoneInfo(toDistribute[2], toDistribute[0], toDistribute[1]);
			} else {
				zi = new ZoneInfo(toDistribute[2], toDistribute[0], toDistribute[1],
						PricingIndicator.valueOf(toDistribute[3]),
						toDistribute.length > 4
								? ZoneInfo
										.distributeIntoObject(Arrays.copyOfRange(toDistribute, 4, toDistribute.length))
								: null);
			}
		}

		return zi;
	}

	@Override
	public String toString() {
		return "ZoneInfo [zoneId=" + zoneId + ", salesOrg=" + salesOrg + ", distributionChanel=" + distributionChanel
				+ ", pricingIndicator=" + pricingIndicator + ", parent=" + parent + "]";
	}

	/**
	 * @param aThat
	 *            is a non-null ZoneInfo.
	 *
	 * @throws NullPointerException
	 *             if aThat is null.
	 */
	@Override
	public int compareTo(ZoneInfo other) {

		final int EQUAL = 0;

		if (this == other)
			return EQUAL;

		int comparison = this.zoneId.compareTo(other.zoneId);
		if (comparison != EQUAL)
			return comparison;

		comparison = this.salesOrg.compareTo(other.salesOrg);
		if (comparison != EQUAL)
			return comparison;

		comparison = this.distributionChanel.compareTo(other.distributionChanel);
		if (comparison != EQUAL)
			return comparison;

		comparison = (null == this.parent && null == other.parent) ? EQUAL
				: (null == this.parent ? -1 : this.parent.compareTo(other.parent));
		if (comparison != EQUAL)
			return comparison;

		/*
		 * comparison = this.parent.compareTo(other.parent); if (comparison != EQUAL)
		 * return comparison;
		 */

		// verify that compareTo is consistent with equals (optional)
		assert this.equals(other) : "compareTo inconsistent with equals.";

		return EQUAL;
	}

	public boolean hasParentZone() {
		return !(this.parent == null);
	}

	public ZoneInfo getParentZone() {
		return this.parent;
	}

	/**
	 * @return the pricingIndicator
	 */
	public PricingIndicator getPricingIndicator() {
		return pricingIndicator;
	}

	public String getZoneId() {
		return zoneId;
	}

	public ZoneInfo getParent() {
		return parent;
	}

}
