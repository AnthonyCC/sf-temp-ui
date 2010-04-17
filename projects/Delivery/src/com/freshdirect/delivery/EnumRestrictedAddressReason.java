package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumRestrictedAddressReason  implements Serializable {
	
	private static final long	serialVersionUID	= -8891749505635801945L;

	private final static Map<String,EnumRestrictedAddressReason> STATUSCODE_MAP = new HashMap<String,EnumRestrictedAddressReason>();
	
	public static final EnumRestrictedAddressReason NONE 		= new EnumRestrictedAddressReason(1, "N", "No Restrictions");
	public static final EnumRestrictedAddressReason ALCOHOL 	= new EnumRestrictedAddressReason(2, "A", "No Alcohol Delivery");
	public static final EnumRestrictedAddressReason FRAUD		= new EnumRestrictedAddressReason(3, "F", "Restricted due to fraud");
	public static final EnumRestrictedAddressReason COMMERCIAL 	= new EnumRestrictedAddressReason(4, "C", "Commercial Address Restriction");
	
	private final int 		id;
	private final String 	code;
	private final String 	description;
	
	public EnumRestrictedAddressReason(int id, String code, String description){
		this.id = id;
		this.code = code;
		this.description = description;
		
		STATUSCODE_MAP.put( this.code, this );
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public static EnumRestrictedAddressReason getRestrictionReason(String code){
		return (EnumRestrictedAddressReason) STATUSCODE_MAP.get( code.toUpperCase() );
	}

	public boolean equals(Object o) {
		if (o instanceof EnumRestrictedAddressReason) {
			return this.id == ((EnumRestrictedAddressReason)o).id;
		}
		return false;
	}
	
	public static List<EnumRestrictedAddressReason> getEnumList() {
		List<EnumRestrictedAddressReason> eList = new ArrayList<EnumRestrictedAddressReason>();
		eList.add( NONE );
		eList.add( ALCOHOL );
		eList.add( FRAUD );
		eList.add( COMMERCIAL );
		return eList;
	}

	public String toString() {
		return "EnumRestrictedAddressReason : " + this.code;
	}

}
