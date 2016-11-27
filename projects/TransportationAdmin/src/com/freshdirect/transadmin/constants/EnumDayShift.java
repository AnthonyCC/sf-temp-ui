package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDayShift  extends Enum {

	public static final EnumDayShift AM_SHIFT = new EnumDayShift("AM", "AM Shift");
	public static final EnumDayShift PM_SHIFT = new EnumDayShift("PM", "PM Shift");
	  
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
