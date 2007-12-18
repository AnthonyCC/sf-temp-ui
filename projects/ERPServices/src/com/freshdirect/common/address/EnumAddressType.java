package com.freshdirect.common.address;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;


/**@author ekracoff on Jun 29, 2004*/
public class EnumAddressType extends ValuedEnum {
	
	public final static EnumAddressType FIRM = new EnumAddressType("F", "Firm", "Commercial Addresses", 1);
	public final static EnumAddressType GENERAL_DELIVERY = new EnumAddressType("G", "General Delivery", "Contains the word \"General Delivery\" in the street name", 2);
	public final static EnumAddressType HIGHRISE = new EnumAddressType("H", "Highrise", "Residential Apartment Buildings", 3);
	public final static EnumAddressType PO_BOX = new EnumAddressType("P", "PO Box", "Post Office Box", 4);
	public final static EnumAddressType RURAL_ROUTE = new EnumAddressType("R", "Rural Route/Highway Contract", "Addresses like ROUTE 3 BOX 15", 5);
	public final static EnumAddressType STREET = new EnumAddressType("S", "Street", "Residential addresses not in a highrise building, ie no apartment number", 6);
	
	private String description;
	private String explanation;
	
	private EnumAddressType(String name, String description, String explanation, int value){
		super(name, value);
		this.description = description;
		this.explanation = explanation;
	}
	
	public static EnumAddressType getEnum(String type) {
		return (EnumAddressType) getEnum(EnumAddressType.class, type);
	}

	public static EnumAddressType getEnum(int id) {
		return (EnumAddressType) getEnum(EnumAddressType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumAddressType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumAddressType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumAddressType.class);
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getExplanation(){
		return this.explanation;
	}
}
