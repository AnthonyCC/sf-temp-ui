/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.fdstore.customer.ejb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

/**
 *  Enumeration of the different list types the customer might have.
 */
public class EnumCustomerListType extends Enum {

	/** The customer's shopping list. */
	public static final EnumCustomerListType SHOPPING_LIST = new EnumCustomerListType("AUTO");
	
	/** A recipe list. */
	public static final EnumCustomerListType RECIPE_LIST = new EnumCustomerListType("RECIPE");
	
	/** The customer's own list, created and maintained by the customer. */
	public static final EnumCustomerListType CC_LIST = new EnumCustomerListType("CCL");

	/**
	 *  Constructor.
	 *  
	 *  @param typeName the name of the list type
	 */
	private EnumCustomerListType(String typeName) {
		super(typeName);
	}

	/**
	 *  Return the list type enumerated object by specifying the list type
	 *  name.
	 *  
	 *  @param typeName the name of the custmer list's type
	 *  @return the enumerated object based on the specified name.
	 */
	public static EnumCustomerListType getEnum(String typeName) {
		return (EnumCustomerListType) getEnum(EnumCustomerListType.class, typeName);
	}

	/**
	 *  Return a map of customer list type enumerated objects, keyed by their
	 *  name.
	 *  
	 *  @return a map, containing the enumerated objects of customer list types,
	 *          with the list names as keys.
	 */
	public static Map getEnumMap() {
		return getEnumMap(EnumCustomerListType.class);
	}

	/**
	 *  Return a list of customer list type enumerated objects.
	 *  
	 *  @return a list of customer list type enumerated objects.
	 */
	public static List getEnumList() {
		return getEnumList(EnumCustomerListType.class);
	}

	/**
	 *  Return an iterator on the customer list type enumerated objects.
	 *  
	 *  @return an iterator on the customer list type enumerated objects.
	 */
	public static Iterator iterator() {
		return iterator(EnumCustomerListType.class);
	}

	/**
	 *  Return a string representation of this customer list type object.
	 *  
	 *  @return the name of the customer list type.
	 */
	public String toString() {
		return this.getName();
	}

}
