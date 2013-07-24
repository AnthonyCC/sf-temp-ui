package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDeliveryOption extends Enum implements Serializable {
	
	private static final long serialVersionUID = -4733656286254593374L;
	
	public final static EnumDeliveryOption SAMEDAY = new EnumDeliveryOption("S", "Same-Day Only");

	public final static EnumDeliveryOption REGULAR = new EnumDeliveryOption("R", "Regular");
	
	public final static EnumDeliveryOption ALL = new EnumDeliveryOption("A", "All");
	
	public final static EnumDeliveryOption SO = new EnumDeliveryOption("SO", "Standing-Order Only");
        
		private final String deliveryOption;

		public EnumDeliveryOption(String name, String deliveryOption) {
			super(name);
			this.deliveryOption = deliveryOption;
		}

		public String getDeliveryOption() {
			return this.deliveryOption;
		}

		public static EnumDeliveryOption getEnum(String name) {
			return (EnumDeliveryOption) getEnum(EnumDeliveryOption.class, name);
		}

		public static Map getEnumMap() {
			return getEnumMap(EnumDeliveryOption.class);
		}

		public static List getEnumList() {
			return getEnumList(EnumDeliveryOption.class);
		}

		public static Iterator iterator() {
			return iterator(EnumDeliveryOption.class);
		}

		public String toString() {
			return this.getName();
		}
	
	
}