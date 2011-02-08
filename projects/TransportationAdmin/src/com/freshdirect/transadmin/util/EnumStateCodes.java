package com.freshdirect.transadmin.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumStateCodes  extends Enum {

	  
	  public static final EnumStateCodes ENUM_STATE_NY = new EnumStateCodes("NY","New York, NY");  
	  public static final EnumStateCodes ENUM_STATE_NJ = new EnumStateCodes("NJ","New Jersey, NJ");
	  public static final EnumStateCodes ENUM_STATE_CT = new EnumStateCodes("CT","Connecticut, CT"); 
	  
	  private final String desc;
	  
	  private EnumStateCodes(String id,String desc) {
	        super(id);
	        this.desc=desc;
	    }
	  
	  public static EnumStateCodes getEnum(String id) {
	        return (EnumStateCodes) getEnum(EnumStateCodes.class, id);
	    }

	    public static Map getEnumMap() {
	        return getEnumMap(EnumStateCodes.class);
	    }

	    public static List getEnumList() {
	        return getEnumList(EnumStateCodes.class);
	    }

	    public static Iterator iterator() {
	        return iterator(EnumStateCodes.class);
	    }
	    public String toString() {
			return this.getName();
		}
	    
		public String getDesc() {
			return this.desc;
		}
	
}
