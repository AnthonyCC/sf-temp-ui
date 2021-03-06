package com.freshdirect.transadmin.model;

// Generated Nov 18, 2008 3:11:21 PM by Hibernate Tools 3.2.2.GA

/**
 * ZonetypeResourceId generated by hbm2java
 */
public class ZonetypeResourceId implements java.io.Serializable, TrnBaseEntityI {

	private String zonetypeId;
	private String role;

	public ZonetypeResourceId() {
	}

	public ZonetypeResourceId(String zonetypeId, String role) {
		this.zonetypeId = zonetypeId;
		this.role = role;
	}

	public String getZonetypeId() {
		return this.zonetypeId;
	}

	public void setZonetypeId(String zonetypeId) {
		this.zonetypeId = zonetypeId;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ZonetypeResourceId))
			return false;
		ZonetypeResourceId castOther = (ZonetypeResourceId) other;

		return ((this.getZonetypeId() == castOther.getZonetypeId()) || (this
				.getZonetypeId() != null
				&& castOther.getZonetypeId() != null && this.getZonetypeId()
				.equals(castOther.getZonetypeId())))
				&& ((this.getRole() == castOther.getRole()) || (this.getRole() != null
						&& castOther.getRole() != null && this.getRole()
						.equals(castOther.getRole())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getZonetypeId() == null ? 0 : this.getZonetypeId()
						.hashCode());
		result = 37 * result
				+ (getRole() == null ? 0 : this.getRole().hashCode());
		return result;
	}

	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

}
