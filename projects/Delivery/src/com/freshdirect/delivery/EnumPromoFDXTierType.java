package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumPromoFDXTierType extends Enum implements Serializable {
	
	private static final long serialVersionUID = -4733656286254593374L;
	
	public final static EnumPromoFDXTierType NEXT_HOUR = new EnumPromoFDXTierType("TIER1", "Next Hour Only");

	public final static EnumPromoFDXTierType POST_NEXT_HOUR = new EnumPromoFDXTierType("TIER2", "Post Next Hour Only");
	
	public final static EnumPromoFDXTierType ALL = new EnumPromoFDXTierType("ALL", "All");
        
		private final String tierType;

		public EnumPromoFDXTierType(String name, String tierType) {
			super(name);
			this.tierType = tierType;
		}

		public String getDeliveryOption() {
			return this.tierType;
		}

		public static EnumPromoFDXTierType getEnum(String name) {
			return (EnumPromoFDXTierType) getEnum(EnumPromoFDXTierType.class, name);
		}

		public static Map getEnumMap() {
			return getEnumMap(EnumPromoFDXTierType.class);
		}

		public static List getEnumList() {
			return getEnumList(EnumPromoFDXTierType.class);
		}

		public static Iterator iterator() {
			return iterator(EnumPromoFDXTierType.class);
		}

		public String toString() {
			return this.getName();
		}
	
	
}