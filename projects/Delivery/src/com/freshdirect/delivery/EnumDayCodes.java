/*
 * EnumDayCodes.java
 *
 * Created on November 9, 2001, 7:47 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
public class EnumDayCodes implements java.io.Serializable {

	public final static EnumDayCodes MONDAY = new EnumDayCodes(0, 2, "Monday");
    public final static EnumDayCodes TUESDAY = new EnumDayCodes(1, 3, "Tuesday");
	public final static EnumDayCodes WEDNESDAY = new EnumDayCodes(2, 4, "Wednesday");
	public final static EnumDayCodes THURSDAY = new EnumDayCodes(3, 5, "Thursday");
	public final static EnumDayCodes FRIDAY = new EnumDayCodes(4, 6, "Friday");
	public final static EnumDayCodes SATURDAY = new EnumDayCodes(5, 7, "Saturday");
	public final static EnumDayCodes SUNDAY = new EnumDayCodes(6, 1, "Sunday");

    protected final int id;
    private final int code;
    private final String name;
    /** Creates new Class */
    private EnumDayCodes(int id, int code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}
    
    public int getCode(){
		return this.code;
    }
    
    public String getName(){
		return this.name;
    }
	
	public static int getCode(String name){
		if(MONDAY.getName().equalsIgnoreCase(name)){
			return MONDAY.getCode();
		}else if(TUESDAY.getName().equalsIgnoreCase(name)){
			return TUESDAY.getCode();
		}else if(WEDNESDAY.getName().equalsIgnoreCase(name)){
			return WEDNESDAY.getCode();
		}else if(THURSDAY.getName().equalsIgnoreCase(name)){
			return THURSDAY.getCode();
		}else if(FRIDAY.getName().equalsIgnoreCase(name)){
			return FRIDAY.getCode();
		}else if(SATURDAY.getName().equalsIgnoreCase(name)){
			return SATURDAY.getCode();
		}else if(SUNDAY.getName().equalsIgnoreCase(name)){
			return SUNDAY.getCode();
		}else{
			return 0;
		}
	}
	
	public static String getName(int code){
		switch(code){
			case 1 :
				return SUNDAY.getName();
			case 2 :
				return MONDAY.getName();
			case 3 :
				return TUESDAY.getName();
			case 4 :
				return WEDNESDAY.getName();
			case 5 :
				return THURSDAY.getName();
			case 6 :
				return FRIDAY.getName();
			case 7 :
				return SATURDAY.getName();
			default : return "";
		}
	}
    
    public boolean equals(Object o) {
	if (o instanceof EnumReservationStatus) {
	    return this.id == ((EnumReservationStatus)o).id;
	}
	return false;
    }

}
