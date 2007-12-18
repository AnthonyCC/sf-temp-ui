/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

/** 
 * an interface that defines constants common to all classes that deal with SAP exports
 *
 * @version $Revision$
 * @author $Author$
 */
public interface SAPConstants {

	/*
	 * Definitions of field names used in SAP export files
	 * See the documents in
	 * ERP Services/Technical Specs/Batch Loads in the VSS repository
	 * for their definitions
	 */
	public static final String MATERIAL_NUMBER = "Material";
	public static final String MATERIAL_DESCRIPTION = "Material Description";
	public static final String SKU = "SKU";
	public static final String SALES_ORGANIZATION = "Sales Organization";
	public static final String DISTRIBUTION_CHANNEL = "Distribution Channel";
	public static final String BASE_UNIT = "Base Unit";
	public static final String SALES_UNIT = "Sales Unit";
	public static final String MATERIAL_TYPE = "Material Type";
	public static final String MATERIAL_TYPE_DESC = "Material Type Description";
	public static final String MATERIAL_GROUP = "Material Group";
	public static final String CONFIGURABLE_ITEM = "Configurable Item";
	public static final String CROSS_CHAIN_SALES_STATUS = "Cross Chain Sales Status";
	public static final String SALES_STATUS_DATE = "Sales Status Date";
	public static final String DIST_CHAIN_SPEC_STATUS = "DChain Spec Status";
	public static final String DIST_CHAIN_SPEC_STATUS_DESC = "DChain Spec Status Description";
	public static final String DIST_CHAIN_SPEC_STATUS_DATE = "DChain Spec Status Date";
	public static final String AVAILABILITY_CHECK = "Availability Check";
	public static final String PLANNED_DELIVERY_TIME = "Planned Delivery Time";
	public static final String INHOUSE_PRODUCTION_TIME = "Inhouse Production Time";
	public static final String DELETION_FLAG = "Deletion Flag";
	public static final String PLANT = "Plant";
	public static final String CONDITION_SCALE_QUANTITY = "Condition Scale Quantity";
	public static final String CONDITION_SCALE_UOM = "Condition Scale UOM";
	public static final String PRICE = "Price";
	public static final String RATE_UNIT = "Rate Unit";
	public static final String CONDITION_UNIT = "Condition Unit";
	public static final String CONDITION_TYPE = "Condition Type";
	public static final String VALIDITY_END_DATE = "Validity End Date";
	public static final String VALIDITY_START_DATE = "Validity Start Date";
	public static final String CONDITION_RECORD_NUMBER = "Condition Record Number";
	public static final String CONFIGURATION_PROFILE_NAME = "Configuration Profile Name";
	public static final String CLASS = "Class";
	public static final String CHARACTERISTIC_NAME = "Characteristic Name";
	public static final String CHARACTERISTIC_VALUE = "Characteristic Value";
	public static final String CHARACTERISTIC_VALUE_DESC = "Characteristic Value Description";
	public static final String DEFAULT_VALUE = "Default Value";
	public static final String DENOMINATOR = "Denominator";
	public static final String NUMERATOR = "Numerator";
	public static final String ALTERNATIVE_UOM = "Alternative UOM";
	public static final String MEASUREMENT_UNIT_TEXT = "Measurement Unit Text";
	public static final String DEPARTMENT = "Department";
	public static final String DEPARTMENT_DESC = "Department Description";
	public static final String ATP_RULE_1 = "ATP Rule 1";
	public static final String ATP_RULE_2 = "ATP Rule 2";
	public static final String ATP_RULE_3 = "ATP Rule 3";
	public static final String ATP_RULE_4 = "ATP Rule 4";
	public static final String ATP_RULE_5 = "ATP Rule 5";
	public static final String ATP_RULE_6 = "ATP Rule 6";
	public static final String ATP_RULE_7 = "ATP Rule 7";
	public static final String UPC = "UPC";
	public static final String TAXABLE = "Taxable";
	public static final String KOSHER_PRODUCTION = "Kosher Production";
	public static final String PLATTER = "Platter";
	public static final String DAY_INDICATOR = "Day-specific Indicator";

	//
	// some constants for properties of materials that aren't well defined in SAP
	//
	public static final java.util.Date THE_FUTURE = new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime();
	// January, 1, 3000

	//
	// constants for invoice feed parsing
	//

	public static final String TYPE_INDICATOR = "Type indicator";
	public static final String SALES_ORDER_NUMBER = "Sales order number";
	public static final String WEB_ORDER_NUMBER = "Web order number";
	public static final String INVOICE_NUMBER = "Invoice number";

	//
	// constants for invoice header
	//

	public static final String INVOICE_TOTAL_BEFORE_CREDIT = "Invoice total before applying credit";
	public static final String INVOICE_TAX = "Invoice tax";
	public static final String INVOICE_BOTTLE_DEPOSIT = "Invoice bottle deposit";
	public static final String INVOICE_SUB_TOTAL = "Invoice sub total";
	public static final String CREDIT_AMOUNT = "Credit amount";
	public static final String INVOICE_TOTAL_AFTER_CREDIT = "Invoice total after credit";
	public static final String ORDER_STATUS = "Order status";
	public static final String BILLING_TYPE = "Billing type";
	public static final String STOP_SEQUENCE = "Stop Sequence";
	public static final String TRUCK_NUMBER = "Truck Number";
	public static final String NUMBER_REGULAR_CARTONS = "Total Number of Regular Cartons";
	public static final String NUMBER_FREEZER_CARTONS = "Number of Freezer Cartons";
	public static final String NUMBER_ALCOHOL_CARTONS = "Number of Cartons Containing alcohol";

	//
	// constants for invoice line item
	//

	public static final String INVOICE_LINE_NUMBER = "Invoice line number";
	//public static final String MATERIAL_NUMBER		    = "Material number";
	public static final String INVOICE_LINE_AMOUNT = "Invoice line amount";
	public static final String INVOICE_LINE_TAX = "Invoice line tax";
	public static final String INVOICE_LINE_BOTTLE_DEPOSIT = "Invoice line bottle deposit";
	public static final String UNIT_PRICE = "Unit price";
	public static final String CUSTOMIZATION_PRICE = "Customization price";
	public static final String SALES_UNIT_OF_MEASURE = "Sales unit of measure";
	public static final String ORDER_QUANTITY = "Order quantity";
	public static final String ACTUAL_SHIPPED_QUANTITY = "Actual shipped quantity";
	public static final String TOTAL_SHIPPED_QUANTITY = "Total shipped quantity";
	public static final String GROSS_WEIGHT = "Gross weight";
	public static final String WEIGHT_UNIT = "Weight unit";
	public static final String ORDER_LINE_STATUS = "Line status";
	public static final String ACTUAL_COST = "Actual Cost";
	//credit memo fields
	public static final String WEB_REFERENCE_NUMBER = "Web reference number";
	public static final String CREDIT_MEMO_NUMBER = "Credit memo number";
	//public static final String CREDIT_AMOUNT		    = "Credit amount";
}
