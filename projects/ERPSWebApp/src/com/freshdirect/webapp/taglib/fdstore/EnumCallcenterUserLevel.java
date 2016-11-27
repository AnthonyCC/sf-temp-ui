/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumCallcenterUserLevel implements java.io.Serializable {

	public final static EnumCallcenterUserLevel PLANT_REP 		= new EnumCallcenterUserLevel(0, "PlantGroup", "Plant Rep");
	public final static EnumCallcenterUserLevel PRODUCT_MANAGER = new EnumCallcenterUserLevel(1, "DepartmentManager", "Product Manager");
	public final static EnumCallcenterUserLevel DISPATCHER 		= new EnumCallcenterUserLevel(2, "Dispatch", "Dispatch");
	public final static EnumCallcenterUserLevel CSR		 		= new EnumCallcenterUserLevel(3, "CustomerService", "Customer Service Rep");
	public final static EnumCallcenterUserLevel STAFF	 		= new EnumCallcenterUserLevel(4, "Staff", "Staff");
	public final static EnumCallcenterUserLevel SUPERVISOR 		= new EnumCallcenterUserLevel(5, "Supervisor", "Supervisor");
	public final static EnumCallcenterUserLevel ADMINISTRATOR	= new EnumCallcenterUserLevel(6, "Administrators", "Administrator");
	public final static EnumCallcenterUserLevel CSR_HYBRID		= new EnumCallcenterUserLevel(7, "CustomerServiceH", "Customer Service Rep Hybrid");

	private int id;
	private String code;
	private String description;

	// Default no-arg constructor
	public EnumCallcenterUserLevel() {
		this.id = -1;
		this.code = null;
		this.description = null;
	}


	public EnumCallcenterUserLevel(int id, String code, String desc) {
		this.id = id;
		this.code = code;
		this.description = desc;
	}

	public int getId() { return this.id; }
	public String getCode() { return this.code; }
	public String getDescription() { return this.description; }

	public boolean equals(EnumCallcenterUserLevel level) {
		return this.id == level.getId();
	}

	public static EnumCallcenterUserLevel getUserLevel(int i) {
		if (i == 0) {
			return PLANT_REP;
		} else if (i == 1) {
			return PRODUCT_MANAGER;
		} else if (i == 2) {
			return DISPATCHER;
		} else if (i == 3) {
			return CSR;
		} else if (i == 4) {
			return STAFF;
		} else if (i == 5) {
			return SUPERVISOR;
		} else if (i == 6) {
			return ADMINISTRATOR;
		} else if (i == 7) {
			return CSR_HYBRID;
		} else {
			return null;
		}
	}

	public static EnumCallcenterUserLevel getUserLevel(String desc) {
		if ( "PlantGroup".equalsIgnoreCase(desc) ) {
			return PLANT_REP;
		} else if ( "DepartmentManager".equalsIgnoreCase(desc) ) {
			return PRODUCT_MANAGER;
		} else if ( "Dispatch".equalsIgnoreCase(desc) ) {
			return DISPATCHER;
		} else if ( "CustomerService".equalsIgnoreCase(desc) ) {
			return CSR;
		} else if ( "Staff".equalsIgnoreCase(desc) ) {
			return STAFF;
		} else if ( "Supervisor".equalsIgnoreCase(desc) ) {
			return SUPERVISOR;
		} else if ( "Administrators".equalsIgnoreCase(desc) ) {
			return ADMINISTRATOR;
		} else if ( "CustomerServiceH".equalsIgnoreCase(desc) ) {
			return CSR_HYBRID;
		} else {
			return null;
		}
	}

}
