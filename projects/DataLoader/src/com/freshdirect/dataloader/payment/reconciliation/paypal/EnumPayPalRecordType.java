package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;

public class EnumPayPalRecordType extends Enum {
	
	public static final EnumPayPalRecordType REPORT_HEADER			= new EnumPayPalRecordType("RH", true, false, false, false);
	public static final EnumPayPalRecordType REPORT_FOOTER			= new EnumPayPalRecordType("RF", false, true, false, false);
	public static final EnumPayPalRecordType FILE_HEADER			= new EnumPayPalRecordType("FH", true, false, false, false);
	public static final EnumPayPalRecordType FILE_FOOTER			= new EnumPayPalRecordType("FF", false, true, false, false);
	public static final EnumPayPalRecordType SECTION_HEADER			= new EnumPayPalRecordType("SH", true, false, false, false);
	public static final EnumPayPalRecordType SECTION_FOOTER			= new EnumPayPalRecordType("SF", false, true, false, false);
	public static final EnumPayPalRecordType COLUMN_HEADER			= new EnumPayPalRecordType("CH", true, false, false, false);
	public static final EnumPayPalRecordType REPORT_COUNT			= new EnumPayPalRecordType("RC", false, false, false, true);
	public static final EnumPayPalRecordType SECTION_COUNT			= new EnumPayPalRecordType("SC", false, false, false, true);
	public static final EnumPayPalRecordType SECTION_BODY			= new EnumPayPalRecordType("SB", false, false, true, false);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5638733914483611097L;
	boolean header;
	boolean footer;
	boolean body;
	boolean count;
	
	public EnumPayPalRecordType(String name, boolean header, boolean footer, boolean body, boolean count) {
		super(name);
		this.header = header;
		this.footer = footer;
		this.body   = body;
		this.count  = count;
	}

	
	public static EnumPayPalRecordType getEnum(String name) {
		return (EnumPayPalRecordType) getEnum(EnumPayPalRecordType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPayPalRecordType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPayPalRecordType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentechRecordType.class);
	}
	
	public boolean isHeader(){
		return this.header;
	}
	
	public boolean isFooter(){
		return this.footer;
	}
	
	public boolean isBody(){
		return this.body;
	}
}
