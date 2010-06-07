package com.freshdirect.transadmin.util;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public final class EnumResourceSubType extends Enum {
    public static final EnumResourceSubType ROUTE_DRIVER = new EnumResourceSubType("001","Route Driver");
    public static final EnumResourceSubType HELPER = new EnumResourceSubType("002","Helper");
    public static final EnumResourceSubType RUNNER = new EnumResourceSubType("003","Runner");
    public static final EnumResourceSubType DEPOT_SHUTTLE_DRIVER = new EnumResourceSubType("004","Depot Shuttle Driver");
    public static final EnumResourceSubType DEPOT_DRIVER = new EnumResourceSubType("005","Depot Driver");
    public static final EnumResourceSubType PART_TIME_DRIVER = new EnumResourceSubType("006","Part Time Worker");
    public static final EnumResourceSubType YARD_WORKER = new EnumResourceSubType("007","Yard Worker");
    public static final EnumResourceSubType MOT = new EnumResourceSubType("008","MOT");
    public static final EnumResourceSubType VENDING = new EnumResourceSubType("009","Vending");
    public static final EnumResourceSubType PART_TIME_HELPER = new EnumResourceSubType("010","part Time Helper");
    public static final EnumResourceSubType PART_TIME_RUNNER = new EnumResourceSubType("011","Part Time Runner");
    public static final EnumResourceSubType MANAGER = new EnumResourceSubType("012","Manager");
  
    
    private final String desc;
    private EnumResourceSubType(String id,String desc) {
        super(id);
        this.desc=desc;
    }

    public static EnumResourceSubType getEnum(String id) {
        return (EnumResourceSubType) getEnum(EnumResourceSubType.class, id);
    }

    public static Map getEnumMap() {
        return getEnumMap(EnumResourceSubType.class);
    }

    public static List getEnumList() {
        return getEnumList(EnumResourceSubType.class);
    }

    public static Iterator iterator() {
        return iterator(EnumResourceSubType.class);
    }
    public String toString() {
		return this.getName();
	}
    
	public String getDesc() {
		return this.desc;
	}
	
	public static boolean isSchedule(EnumResourceSubType temp) {
		if (temp == MANAGER || temp == YARD_WORKER) {
			return false;
		}
		return true;
	}

	public static boolean isKronosFileGeneration(EnumResourceSubType temp) {
		if (temp == MANAGER || temp == YARD_WORKER) {
			return false;
		}
		return true;
	}

	public static boolean isUnassignedEmployees(EnumResourceSubType temp) {
		if (temp == MANAGER || temp == YARD_WORKER) {
			return false;
		}
		return true;
	}

	public static boolean isUnAvailable(EnumResourceSubType temp) {
		if (temp == MANAGER || temp == YARD_WORKER) {
			return false;
		}
		return true;
	}

	public static boolean ignorePunch(EnumResourceSubType temp) {
		if (temp == MANAGER || temp == VENDING) {
			return true;
		}			
		return false;
	}
}

