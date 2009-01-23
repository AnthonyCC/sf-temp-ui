/*
 * Created on Aug 1, 2005
 */
package com.freshdirect.fdstore.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;


public class EnumSiteFeature extends Enum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1944504955176270287L;

	private final static String PROFILE_PREFIX = "siteFeature.";

	public final static EnumSiteFeature NEW_SEARCH = new EnumSiteFeature("newSearch");
	public final static EnumSiteFeature CCL = new EnumSiteFeature("CCL");
	public final static EnumSiteFeature DYF = new EnumSiteFeature("DYF");
	public final static EnumSiteFeature YMAL = new EnumSiteFeature("YMAL");
	public final static EnumSiteFeature RATING = new EnumSiteFeature("RATING");
    public final static EnumSiteFeature FEATURED_ITEMS = new EnumSiteFeature("FEATURED_ITEMS");
    public final static EnumSiteFeature FAVORITES = new EnumSiteFeature("FAVORITES");

	protected EnumSiteFeature(String name) {
		super(name);
	}

	public static EnumSiteFeature getEnum(String name) {
		return (EnumSiteFeature) getEnum(EnumSiteFeature.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumSiteFeature.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumSiteFeature.class);
	}

	public static Iterator iterator() {
		return iterator(EnumSiteFeature.class);
	}
	
	public boolean isEnabled(FDUserI user) {
		if (user == null || user.getIdentity() == null) {
			return false;
		}

		try {
			String value = user.getFDCustomer().getProfile().getAttribute(PROFILE_PREFIX + getName());

			if (value == null) {
				return false;
			}

			return Boolean.valueOf(value).booleanValue();
		} catch (FDResourceException e) {
			return false;
		}		
	}
	
	public String getAttributeKey() {
		return PROFILE_PREFIX + getName();
	}
}
