package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumCreditEmailType extends Enum implements java.io.Serializable {

	private final String description;
	private final String htmlXSLFilename;
	private final String plainTextXSLFilename;

	public final static EnumCreditEmailType DEFAULT_EMAIL = new EnumCreditEmailType(
		"DFLT",
		"Default",
		"h_credit_confirm_v1.xsl",
		"x_credit_confirm_v1.xsl");
	public final static EnumCreditEmailType DAMAGED_ITEM_EMAIL = new EnumCreditEmailType(
		"PDMG",
		"Damaged item",
		"h_credit_damaged_item.xsl",
		"x_credit_damaged_item.xsl");
	public final static EnumCreditEmailType MISSING_ITEM_EMAIL = new EnumCreditEmailType(
		"PMIS",
		"Missing item",
		"h_credit_missing_item.xsl",
		"x_credit_missing_item.xsl");
	public final static EnumCreditEmailType BELOW_PAR_SERVICE_EMAIL = new EnumCreditEmailType(
		"PQLT",
		"Product quality",
		"h_credit_product_quality.xsl",
		"x_credit_product_quality.xsl");
	public final static EnumCreditEmailType MULTIPLE_DAMAGED_ITEMS_EMAIL = new EnumCreditEmailType(
		"MDMG",
		"Multiple damaged item",
		"h_credit_multiple_damage.xsl",
		"x_credit_multiple_damage.xsl");
	public final static EnumCreditEmailType EGG_DAMAGED_EMAIL = new EnumCreditEmailType(
		"EDMG",
		"Eggs damaged",
		"h_credit_egg_damage.xsl",
		"x_credit_egg_damage.xsl");
	public final static EnumCreditEmailType APOLOGY_LATE_DELIVERY = new EnumCreditEmailType(
		"ALDL",
		"Late delivery",
		"h_late_delivery.xsl",
		"x_late_delivery.xsl");
	public final static EnumCreditEmailType CREDIT_LATE_DELIVERY = new EnumCreditEmailType(
		"CLDL",
		"Late delivery credit",
		"h_credit_late_delivery.xsl",
		"x_credit_late_delivery.xsl");

	private EnumCreditEmailType(String code, String desc, String htmlXslFileName, String plainTextXslFileName) {
		super(code);
		this.description = desc;
		this.htmlXSLFilename = htmlXslFileName;
		this.plainTextXSLFilename = plainTextXslFileName;
	}

	public static EnumCreditEmailType getEnum(String name) {
		return (EnumCreditEmailType) getEnum(EnumCreditEmailType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCreditEmailType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCreditEmailType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCreditEmailType.class);
	}

	public String getDescription() {
		return this.description;
	}

	public String getHtmlXSLFilename() {
		return this.htmlXSLFilename;
	}

	public String getPlainTextXSLFilename() {
		return this.plainTextXSLFilename;
	}

	public String toString() {
		return "[EnumCreditEmailType name: "
			+ getName()
			+ " Html XSL filename: "
			+ htmlXSLFilename
			+ " Plain Text XSL filename: "
			+ plainTextXSLFilename
			+ "]";
	}

}