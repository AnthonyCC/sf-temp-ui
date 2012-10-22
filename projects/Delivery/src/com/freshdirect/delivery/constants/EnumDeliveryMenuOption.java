package com.freshdirect.delivery.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDeliveryMenuOption  extends Enum {
	
	public static final EnumDeliveryMenuOption EARLY_DELIVERY = new EnumDeliveryMenuOption("EDR","early delivery request");

    public static final EnumDeliveryMenuOption DELIVERY_ACCESS = new EnumDeliveryMenuOption("DAR","delivery access");
    
    private final String desc;

	public EnumDeliveryMenuOption(String name, String desc) {
		super(name);
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}

	public static EnumDeliveryMenuOption getEnum(String name) {
		return (EnumDeliveryMenuOption) getEnum(EnumDeliveryMenuOption.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumDeliveryMenuOption.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumDeliveryMenuOption.class);
	}

	public static Iterator iterator() {
		return iterator(EnumDeliveryMenuOption.class);
	}

	public String toString() {
		return this.getDesc();
	}
	
	public static EnumDeliveryMenuOption getEnumByDesc(String desc) {
		
		for(Object _option :  EnumDeliveryMenuOption.getEnumList()){
			if(((EnumDeliveryMenuOption) _option).getDesc().equalsIgnoreCase(desc)){
				return (EnumDeliveryMenuOption) _option;
			}
		}
		return null;
	}
}
