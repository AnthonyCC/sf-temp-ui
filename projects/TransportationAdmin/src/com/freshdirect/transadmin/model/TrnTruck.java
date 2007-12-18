package com.freshdirect.transadmin.model;

// Generated Jul 30, 2007 6:23:08 PM by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

/**
 * TrnTruck generated by hbm2java
 */
public class TrnTruck  implements java.io.Serializable, TrnBaseEntityI {

	private String truckId;

	private String truckType;

	private String licensePlate;

	private String truckNumber;
	
	private String obsolete;

	private Set trnTruckassignments = new HashSet(0);

	public TrnTruck() {
	}

	public TrnTruck(String truckId) {
		this.truckId = truckId;
	}

	public TrnTruck(String truckId, String truckType, String licensePlate,
			String truckNumber, Set trnTruckassignments) {
		this.truckId = truckId;
		this.truckType = truckType;
		this.licensePlate = licensePlate;
		this.truckNumber = truckNumber;
		this.trnTruckassignments = trnTruckassignments;
	}

	public String getTruckId() {
		return this.truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

	public String getTruckType() {
		return this.truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getTruckNumber() {
		return this.truckNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	public Set getTrnTruckassignments() {
		return this.trnTruckassignments;
	}

	public void setTrnTruckassignments(Set trnTruckassignments) {
		this.trnTruckassignments = trnTruckassignments;
	}

	public String getObsolete() {
		return obsolete;
	}

	public void setObsolete(String obsolete) {
		this.obsolete = obsolete;
	}
	
	public boolean isObsoleteEntity() {
 		return false;
	}

}
