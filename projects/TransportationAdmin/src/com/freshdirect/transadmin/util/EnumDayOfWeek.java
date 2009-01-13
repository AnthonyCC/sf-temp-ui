package com.freshdirect.transadmin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumDayOfWeek  extends Enum {

	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_MON=new EnumDayOfWeek("1","Mon"); 
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_TUE=new EnumDayOfWeek("2","Tue"); 
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_WED=new EnumDayOfWeek("3","Wed"); 
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_THUR=new EnumDayOfWeek("4","Thu"); 
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_FRI=new EnumDayOfWeek("5","Fri");
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_SAT=new EnumDayOfWeek("6","Sat");
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_SUN=new EnumDayOfWeek("7","Sun");
	  public static final EnumDayOfWeek ENUM_DAYOFWEEK_ALL=new EnumDayOfWeek("0","All");
	  
	  private final String desc;
	  
	  private EnumDayOfWeek(String id,String desc) {
	        super(id);
	        this.desc=desc;
	    }
	  
	  public static EnumDayOfWeek getEnum(String id) {
	        return (EnumDayOfWeek) getEnum(EnumDayOfWeek.class, id);
	    }

	    public static Map getEnumMap() {
	        return getEnumMap(EnumDayOfWeek.class);
	    }

	    public static List getEnumList() {
	        return getEnumList(EnumDayOfWeek.class);
	    }

	    public static Iterator iterator() {
	        return iterator(EnumDayOfWeek.class);
	    }
	    public String toString() {
			return this.getName();
		}
	    
		public String getDesc() {
			return this.desc;
		}
	
}
