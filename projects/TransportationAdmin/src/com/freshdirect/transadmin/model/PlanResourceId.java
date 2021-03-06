package com.freshdirect.transadmin.model;

// Generated Nov 18, 2008 3:11:21 PM by Hibernate Tools 3.2.2.GA

/**
 * PlanResourceId generated by hbm2java
 */
public class PlanResourceId implements java.io.Serializable {

	private String planId;
	private String resourceId;

	public PlanResourceId() {
	}

	public PlanResourceId(String planId, String resourceId) {
		this.planId = planId;
		this.resourceId = resourceId;
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PlanResourceId))
			return false;
		PlanResourceId castOther = (PlanResourceId) other;

		return ((this.getPlanId() == castOther.getPlanId()) || (this
				.getPlanId() != null
				&& castOther.getPlanId() != null && this.getPlanId().equals(
				castOther.getPlanId())))
				&& ((this.getResourceId() == castOther.getResourceId()) || (this
						.getResourceId() != null
						&& castOther.getResourceId() != null && this
						.getResourceId().equals(castOther.getResourceId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPlanId() == null ? 0 : this.getPlanId().hashCode());
		result = 37
				* result
				+ (getResourceId() == null ? 0 : this.getResourceId()
						.hashCode());
		return result;
	}

}
