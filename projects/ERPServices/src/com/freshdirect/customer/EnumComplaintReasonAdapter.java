/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

/**
 * Adapts department names to their equivalent value in the COMPLAINTCODE database
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumComplaintReasonAdapter {

	private String departmentFull 		= null;
	private String departmentAdapter	= null;

	public EnumComplaintReasonAdapter(String departmentFull, String departmentAdapter) {
		this.departmentFull = departmentFull;
		this.departmentAdapter = departmentAdapter;
	}

	public boolean equals(String s) {
		return this.departmentAdapter.equalsIgnoreCase(s);
	}

	public String getFullName() {
		return this.departmentFull;
	}

	public String getAdapterName() {
		return this.departmentAdapter;
	}

	public final static EnumComplaintReasonAdapter BAKERY		= new EnumComplaintReasonAdapter("Bakery", "bkry");
	public final static EnumComplaintReasonAdapter CHEESE		= new EnumComplaintReasonAdapter("Cheese", "ches");
	public final static EnumComplaintReasonAdapter COFFEE		= new EnumComplaintReasonAdapter("Coffee", "cofe");
	public final static EnumComplaintReasonAdapter DAIRY		= new EnumComplaintReasonAdapter("Dairy", "dair");
	public final static EnumComplaintReasonAdapter DELI			= new EnumComplaintReasonAdapter("Deli", "deli");
	public final static EnumComplaintReasonAdapter FROZEN		= new EnumComplaintReasonAdapter("Frozen", "frzn");
	public final static EnumComplaintReasonAdapter FRUIT		= new EnumComplaintReasonAdapter("Fruit", "frut");
	public final static EnumComplaintReasonAdapter GROCERY		= new EnumComplaintReasonAdapter("Grocery", "grcy");
	public final static EnumComplaintReasonAdapter KITCHEN		= new EnumComplaintReasonAdapter("Meals", "kchn");
	public final static EnumComplaintReasonAdapter MEAT			= new EnumComplaintReasonAdapter("Meat", "meat");
	public final static EnumComplaintReasonAdapter PASTA		= new EnumComplaintReasonAdapter("Pasta", "psta");
	public final static EnumComplaintReasonAdapter SEAFOOD		= new EnumComplaintReasonAdapter("Seafood", "sefd");
	public final static EnumComplaintReasonAdapter SPECIALTY	= new EnumComplaintReasonAdapter("Specialty", "spcl");
	public final static EnumComplaintReasonAdapter TEA			= new EnumComplaintReasonAdapter("Tea", "tea");
	public final static EnumComplaintReasonAdapter VEGETABLES	= new EnumComplaintReasonAdapter("Vegetables & Herbs", "vege");
	public final static EnumComplaintReasonAdapter CUSTOMER_SERVICE
																= new EnumComplaintReasonAdapter("customer service", "csrv");
	public final static EnumComplaintReasonAdapter TRANSPORTATION
																= new EnumComplaintReasonAdapter("transportation", "trns");
	public final static EnumComplaintReasonAdapter QUICK_REFUND	= new EnumComplaintReasonAdapter("quick refund", "rfnd");
}