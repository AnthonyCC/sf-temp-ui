package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPaymentechRecordType extends Enum {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final EnumPaymentechRecordType DFR_START 				= new EnumPaymentechRecordType("*DFRBEG", false);
	public static final EnumPaymentechRecordType FIN0010_HEADER			= new EnumPaymentechRecordType("HFIN0010", true);
	public static final EnumPaymentechRecordType FIN0010_DATA			= new EnumPaymentechRecordType("RFIN0010", false);
	public static final EnumPaymentechRecordType LNK010A_HEADER			= new EnumPaymentechRecordType("HLNK010A", true);
	public static final EnumPaymentechRecordType LNK010A_DATA			= new EnumPaymentechRecordType("RLNK010A", false);
	public static final EnumPaymentechRecordType ACT0033_HEADER			= new EnumPaymentechRecordType("HACT0033", true);
	public static final EnumPaymentechRecordType ACT0033_DATA			= new EnumPaymentechRecordType("RACT0033", false);
	public static final EnumPaymentechRecordType FIN0011_HEADER			= new EnumPaymentechRecordType("HFIN0011", true);
	public static final EnumPaymentechRecordType FIN0011_DATA			= new EnumPaymentechRecordType("RFIN0011", false);
	public static final EnumPaymentechRecordType ACT0010_HEADER			= new EnumPaymentechRecordType("HACT0010", true);
	public static final EnumPaymentechRecordType ACT0010_DATA			= new EnumPaymentechRecordType("RACT0010", false);
	public static final EnumPaymentechRecordType PDE0017_HEADER			= new EnumPaymentechRecordType("HPDE0017", true);
	public static final EnumPaymentechRecordType PDE0017_SUMMARY_DATA	= new EnumPaymentechRecordType("RPDE0017S", false);
	public static final EnumPaymentechRecordType PDE0017_DETAIL_DATA 	= new EnumPaymentechRecordType("RPDE0017D", false);
	public static final EnumPaymentechRecordType PDE0018_HEADER			= new EnumPaymentechRecordType("HPDE0018", true);
	public static final EnumPaymentechRecordType PDE0018_SUMMARY_DATA	= new EnumPaymentechRecordType("RPDE0018S", false);
	public static final EnumPaymentechRecordType PDE0018_DETAIL_DATA 	= new EnumPaymentechRecordType("RPDE0018D", false);
	public static final EnumPaymentechRecordType PDE0020_HAEDER 		= new EnumPaymentechRecordType("HPDE0020", true);
	public static final EnumPaymentechRecordType PDE0020_DATA 			= new EnumPaymentechRecordType("RPDE0020", false);
	public static final EnumPaymentechRecordType PDE0022_HEADER			= new EnumPaymentechRecordType("HPDE0022", true);
	public static final EnumPaymentechRecordType PDE0022_DETAIL_DATA 	= new EnumPaymentechRecordType("RPDE0022D", false);
	public static final EnumPaymentechRecordType DFR_END 				= new EnumPaymentechRecordType("*DFREND", false);
	
	private final boolean header;
	
	private EnumPaymentechRecordType(String name, boolean header) {
		super(name);
		this.header = header;
	}
	
	public static EnumPaymentechRecordType getEnum(String name) {
		return (EnumPaymentechRecordType) getEnum(EnumPaymentechRecordType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPaymentechRecordType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPaymentechRecordType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentechRecordType.class);
	}
	
	public boolean isHeader(){
		return this.header;
	}

}
