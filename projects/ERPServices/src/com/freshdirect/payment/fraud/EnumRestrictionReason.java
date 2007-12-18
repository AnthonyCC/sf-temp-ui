package com.freshdirect.payment.fraud;

import org.apache.commons.lang.enum.ValuedEnum;
import java.util.*;

public class EnumRestrictionReason extends ValuedEnum {
    public final static EnumRestrictionReason ACCOUNT_FRAUD				= new EnumRestrictionReason("AFR", "Account Fraud", 0);
    public final static EnumRestrictionReason CLOSED_ACCOUNT			= new EnumRestrictionReason("CLA", "Closed Account", 1);
    public final static EnumRestrictionReason COMPROMISED_ACCOUNT		= new EnumRestrictionReason("CMPA", "Account has been Compromised", 2);
    public final static EnumRestrictionReason FROZEN_ACCOUNT			= new EnumRestrictionReason("FRA", "Frozen Account", 3);
    public final static EnumRestrictionReason GENERAL_ACCOUNT_PROBLEM	= new EnumRestrictionReason("GAP", "General Account Problem", 4);
    public final static EnumRestrictionReason INSUFFICIENT_FUNDS		= new EnumRestrictionReason("ISF", "Insufficient Funds", 5);
    public final static EnumRestrictionReason INVALID_ABA_ROUTE_NUMBER	= new EnumRestrictionReason("IRN", "Invalid ABA Route Number", 6);
	public final static EnumRestrictionReason INVALID_ACCOUNT_NUMBER	= new EnumRestrictionReason("IAN", "Invalid Account Number", 7);
    public final static EnumRestrictionReason SUSPECT_ACCOUNT			= new EnumRestrictionReason("SPTA", "Account is Suspect", 8);
    public final static EnumRestrictionReason CUSTOMER_DECEASED			= new EnumRestrictionReason("CDSD", "Customer Deceased", 9);
    public final static EnumRestrictionReason BENEFICIARY_DECEASED		= new EnumRestrictionReason("BDSD", "Beneficiary Deceased", 10);
    public final static EnumRestrictionReason INVALID_ACCOUNT_TYPE		= new EnumRestrictionReason("IAT",  "Invalid Account Type", 11);

	private String description;
	
	private EnumRestrictionReason(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumRestrictionReason getEnum(String code) {
		return (EnumRestrictionReason) getEnum(EnumRestrictionReason.class, code);
	}

	public static EnumRestrictionReason getEnum(int id) {
		return (EnumRestrictionReason) getEnum(EnumRestrictionReason.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumRestrictionReason.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumRestrictionReason.class);
	}

	public static Iterator iterator() {
		return iterator(EnumRestrictionReason.class);
	}
	
	public String getDescription(){
		return this.description;
	}

}
