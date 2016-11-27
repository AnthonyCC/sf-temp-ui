package com.freshdirect.routing.constants;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDeliveryType extends Enum implements Serializable {
	
	private static final long serialVersionUID = -4733656286254593374L;
	
	public final static EnumDeliveryType HOME = new EnumDeliveryType("H", "Residential");

	public final static EnumDeliveryType CORPORATE = new EnumDeliveryType("C", "Corporate");
	
	public final static EnumDeliveryType PICKUP = new EnumDeliveryType("P", "Pick-up");
        
		private final String deliveryType;

		public EnumDeliveryType(String name, String deliveryType) {
			super(name);
			this.deliveryType = deliveryType;
		}

		public String getDeliveryType() {
			return this.deliveryType;
		}

		public static EnumDeliveryType getEnum(String name) {
			return (EnumDeliveryType) getEnum(EnumDeliveryType.class, name);
		}

		public static Map getEnumMap() {
			return getEnumMap(EnumDeliveryType.class);
		}

		public static List getEnumList() {
			return getEnumList(EnumDeliveryType.class);
		}

		public static Iterator iterator() {
			return iterator(EnumDeliveryType.class);
		}

		public String toString() {
			return this.getName();
		}
	
	
}