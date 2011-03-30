package com.freshdirect.delivery;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDayShift extends Enum{
	  public static final EnumDayShift DAY_SHIFT_AM = new EnumDayShift("AM","Morning");  
	  public static final EnumDayShift DAY_SHIFT_PM = new EnumDayShift("PM","Evening");
	  
	  private final String desc;
	  
	  private EnumDayShift(String id,String desc) {
	        super(id);
	        this.desc=desc;
	    }
	  
	  public static EnumDayShift getEnum(String id) {
	        return (EnumDayShift) getEnum(EnumDayShift.class, id);
	    }

	    public static Map getEnumMap() {
	        return getEnumMap(EnumDayShift.class);
	    }

	    public static List getEnumList() {
	        return getEnumList(EnumDayShift.class);
	    }

	    public static Iterator iterator() {
	        return iterator(EnumDayShift.class);
	    }
	    public String toString() {
			return this.getName();
		}
	    
		public String getDesc() {
			return this.desc;
		}
	
}
