package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumAssetElectricFleetMetric  extends Enum {

	public static final EnumAssetElectricFleetMetric ELECTRIC_DRIVE = new EnumAssetElectricFleetMetric("EDF", "Electric Drive Flag");
	public static final EnumAssetElectricFleetMetric ELECTRIC_REEFER = new EnumAssetElectricFleetMetric("ERF", "Electric Reefer Flag");
	  
	  private final String desc;
	  
	  private EnumAssetElectricFleetMetric(String id,String desc) {
	        super(id);
	        this.desc=desc;
	    }
	  
	  public static EnumAssetElectricFleetMetric getEnum(String id) {
	        return (EnumAssetElectricFleetMetric) getEnum(EnumAssetElectricFleetMetric.class, id);
	    }

	    public static Map getEnumMap() {
	        return getEnumMap(EnumAssetElectricFleetMetric.class);
	    }

	    public static List getEnumList() {
	        return getEnumList(EnumAssetElectricFleetMetric.class);
	    }

	    public static Iterator iterator() {
	        return iterator(EnumAssetElectricFleetMetric.class);
	    }
	    public String toString() {
			return this.getName();
		}
	    
		public String getDesc() {
			return this.desc;
		}
	
}
