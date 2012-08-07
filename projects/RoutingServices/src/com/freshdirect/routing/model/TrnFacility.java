package com.freshdirect.routing.model;

import java.io.Serializable;
import java.math.BigInteger;

import com.freshdirect.routing.model.TrnFacilityType;

public class TrnFacility implements Serializable {
	
	private String facilityId;
	private String name;
	private String description;
	private String routingCode;
	private String prefix;
	private BigInteger leadFromTime;
	private BigInteger leadToTime;
	
	private TrnFacilityType trnFacilityType;

	public TrnFacilityType getTrnFacilityType() {
		return trnFacilityType;
	}

	public void setTrnFacilityType(TrnFacilityType trnFacilityType) {
		this.trnFacilityType = trnFacilityType;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getLeadFromTime() {
		return leadFromTime;
	}

	public void setLeadFromTime(BigInteger leadFromTime) {
		this.leadFromTime = leadFromTime;
	}

	public BigInteger getLeadToTime() {
		return leadToTime;
	}

	public void setLeadToTime(BigInteger leadToTime) {
		this.leadToTime = leadToTime;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TrnFacility other = (TrnFacility) obj;
		if (facilityId == null) {
			if (other.facilityId != null)
				return false;
		} else if (!facilityId.equals(other.facilityId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Facility [code=" + name + "]";
	}
	
	
}
