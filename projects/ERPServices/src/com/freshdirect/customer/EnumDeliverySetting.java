/*
 * EnumDeliverySetting.java
 *
 * Created on March 21, 2002, 10:38 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  rfalck
 * @version
 */
public class EnumDeliverySetting implements java.io.Serializable {

    public final static EnumDeliverySetting NONE			= new EnumDeliverySetting(0, "none", "No Delivery Setting");
    public final static EnumDeliverySetting DOORMAN		= new EnumDeliverySetting(1, "doorman", "Deliver order to doorman");
    public final static EnumDeliverySetting NEIGHBOR		= new EnumDeliverySetting(2, "neighbor", "Deliver order to neighbor");

    private EnumDeliverySetting(int id, String deliveryCode, String name) {
        this.id = id;
        this.deliveryCode = deliveryCode;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getDeliveryCode() {
		return this.deliveryCode;
	}

    public String getName() {
        return this.name;
    }

	public static EnumDeliverySetting getDeliverySetting(String code) {
		if ( "none".equalsIgnoreCase(code) ) {
			return NONE;
		} else if ( "doorman".equalsIgnoreCase(code) ) {
			return DOORMAN;
		} else if ( "neighbor".equalsIgnoreCase(code) ) {
			return NEIGHBOR;
		} else
			return null;
	}

	public static EnumDeliverySetting getDeliverySetting(int id) {
		if ( id == 0 ) {
			return NONE;
		} else if ( id == 1 ) {
			return DOORMAN;
		} else if ( id == 2 ) {
			return NEIGHBOR;
		} else
			return null;
	}

	public boolean equals(Object type) {
		
		if(!(type instanceof EnumDeliverySetting)){
			return false;
		}
		
		return this.id == ((EnumDeliverySetting)type).getId();
	}
	
	public String toString(){
		return this.name;
	}

    private final int id;
    private final String deliveryCode;
    private final String name;

} // class EnumDeliverySetting

