package com.freshdirect.routing.constants;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumProfileList extends Enum implements Serializable {
	
	private static final long serialVersionUID = -4733656235254593374L;
	
	public final static EnumProfileList CHEFSTABLE = new EnumProfileList("CT", "Chefstable");
	
	public final static EnumProfileList NONCHEFSTABLE = new EnumProfileList("NCT", "Non-Chefstable");
        
	private final String profileName;

		public EnumProfileList(String name, String profileName) {
			super(name);
			this.profileName = profileName;
		}

		public String getProfileName() {
			return this.profileName;
		}

		public static EnumProfileList getEnum(String name) {
			return (EnumProfileList) getEnum(EnumProfileList.class, name);
		}

		public static Map getEnumMap() {
			return getEnumMap(EnumProfileList.class);
		}

		public static List getEnumList() {
			return getEnumList(EnumProfileList.class);
		}

		public static Iterator iterator() {
			return iterator(EnumProfileList.class);
		}

		public String toString() {
			return this.getName();
		}
	
	
}