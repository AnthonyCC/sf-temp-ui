package com.freshdirect.framework.event;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 *  Enumeration for cartline event sources, that is, what part of the site
 *  was a product put into the cart.
 */
public class EnumEventSource extends Enum {

	/**
	 *  The source of the event is not known.
	 */
	public static final EnumEventSource UNKNOWN = new EnumEventSource("Unknown");
	
	/**
	 *  The source of the event is the 'regular' site, looking at products, etc.
	 */
	public static final EnumEventSource BROWSE = new EnumEventSource("Browse");
	
	/**
	 *  The source of the event is watching recipes.
	 */
	public static final EnumEventSource RECIPE = new EnumEventSource("Recipe");
	
	/**
	 *  The source of the event is the quickshop area.
	 */
	public static final EnumEventSource QUICKSHOP = new EnumEventSource("Quickshop");
	
	/**
	 *  The source if the event is the TX YMAL.
	 */
	public static final EnumEventSource TXYMAL = new EnumEventSource("TxYmal");

	/**
	 *  The source of the event is a customer created list.
	 */
	public static final EnumEventSource CCL = new EnumEventSource("CCL");
	
	/**
	 * The source of the event is SmartStore related.
	 */
	public static final EnumEventSource SmartStore = new EnumEventSource("SS");

	public EnumEventSource(String name) {
		super(name);
	}

	public static EnumEventSource getEnum(String name) {
		return (EnumEventSource) getEnum(EnumEventSource.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumEventSource.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumEventSource.class);
	}

	public static Iterator iterator() {
		return iterator(EnumEventSource.class);
	}

	public String toString() {
		return this.getName();
	}

}
