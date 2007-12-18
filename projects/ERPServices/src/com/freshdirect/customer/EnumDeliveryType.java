/*
 * EnumDeliveryType.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  jmccarter
 * @version
 */
public class EnumDeliveryType implements java.io.Serializable {

	private final static Map DELIVERY_TYPE_MAP = new HashMap();

	public final static EnumDeliveryType HOME = new EnumDeliveryType(0, "H", "Home Delivery", "003");
	public final static EnumDeliveryType DEPOT = new EnumDeliveryType(1, "D", "Depot Delivery", "002");
	public final static EnumDeliveryType PICKUP = new EnumDeliveryType(2, "P", "Pickup", "002");
	public final static EnumDeliveryType CORPORATE = new EnumDeliveryType(3, "C", "Corporate Delivery", "004");

	private final int id;
	private final String code;
	private final String name;
	private final String deliveryModel;

	private EnumDeliveryType(int id, String code, String name, String deliveryModel) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.deliveryModel = deliveryModel;

		DELIVERY_TYPE_MAP.put(code, this);
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getDeliveryModel() {
		return this.deliveryModel;
	}

	public String toString() {
		return this.code;
	}

	public static EnumDeliveryType getDeliveryType(String code) {
		return (EnumDeliveryType) DELIVERY_TYPE_MAP.get(code);
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof EnumDeliveryType) {
			return this.id == ((EnumDeliveryType) o).id;
		}
		return false;
	}
}