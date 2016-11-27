package com.freshdirect.transadmin.model;

public class EmployeeSupervisorId implements java.io.Serializable {

	private String kronosId;
	private String supervisorId;
	private String supervisorName;

	public EmployeeSupervisorId() {
	}

	public EmployeeSupervisorId(String kronosId, String supervisorId) {
		this.kronosId = kronosId;
		this.supervisorId = supervisorId;
	}

	public String getKronosId() {
		return this.kronosId;
	}

	public void setKronosId(String kronosId) {
		this.kronosId = kronosId;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}
	
	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EmployeeSupervisorId))
			return false;
		EmployeeSupervisorId castOther = (EmployeeSupervisorId) other;

		return ((this.getKronosId() == castOther.getKronosId()) || (this
				.getKronosId() != null
				&& castOther.getKronosId() != null && this.getKronosId()
				.equals(castOther.getKronosId())))
				&& ((this.getSupervisorId() == castOther.getSupervisorId()) || (this.getSupervisorId() != null
						&& castOther.getSupervisorId() != null && this.getSupervisorId()
						.equals(castOther.getSupervisorId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getKronosId() == null ? 0 : this.getKronosId().hashCode());
		result = 37 * result
				+ (getSupervisorId() == null ? 0 : this.getSupervisorId().hashCode());
		return result;
	}

}
