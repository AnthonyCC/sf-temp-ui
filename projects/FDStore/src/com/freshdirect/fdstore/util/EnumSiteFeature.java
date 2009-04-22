/*
 * Created on Aug 1, 2005
 */
package com.freshdirect.fdstore.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.ejb.DynamicSiteFeature;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;


public class EnumSiteFeature implements Serializable, Comparable {
	private static final long serialVersionUID = -2545205419352991876L;

	private final static String PROFILE_PREFIX = "siteFeature.";

    private final static Map staticEnum = Collections.synchronizedMap(new HashMap());

    private static Map dynamicEnum = Collections.synchronizedMap(new HashMap());
    
    private final static Latch loaded = new Latch(false);
    
    private final static Latch mocked = new Latch(false);
    
    /**
     * @deprecated
     */
	public final static EnumSiteFeature NEW_SEARCH = new EnumSiteFeature("newSearch");
	public final static EnumSiteFeature CCL = new EnumSiteFeature("CCL");
	public final static EnumSiteFeature DYF = new EnumSiteFeature("DYF", true, "Your Favorites");
	public final static EnumSiteFeature YMAL = new EnumSiteFeature("YMAL", true, "You Might Also Like");
	public final static EnumSiteFeature YMAL_YF = new EnumSiteFeature("YMAL_YF", true, "YMAL from Your Favorites");
	public final static EnumSiteFeature RATING = new EnumSiteFeature("RATING");
    public final static EnumSiteFeature FEATURED_ITEMS = new EnumSiteFeature("FEATURED_ITEMS", true, "Featured Items");
    public final static EnumSiteFeature FAVORITES = new EnumSiteFeature("FAVORITES", true, "FreshDirect Favorites");
    public final static EnumSiteFeature CART_N_TABS = new EnumSiteFeature("CART_N_TABS", true, "Cart & Tabs");
    public final static EnumSiteFeature SOYF = new EnumSiteFeature("SOYF", true, true, "Save on Your Favorites");
    public final static EnumSiteFeature SAVE_ON_FAVORITES = new EnumSiteFeature("SOFDF", true, true ,"Save on FreshDirect Favorites");
    public final static EnumSiteFeature SAVE_ON_YMAL = new EnumSiteFeature("SOYMAL", true, true ,"Save on You Might Also Like");


    private static class Latch implements Serializable {
		private static final long serialVersionUID = -5565193126379596331L;

		boolean value;

		public Latch(boolean value) {
			super();
			this.value = value;
		}
    	
    	public void set() {
    		value = true;
    	}
    	
    	public void reset() {
    		value = false;
    	}
    	
    	public boolean isSet() {
    		return value;
    	}
    	
    	public boolean isReset() {
    		return !value;
    	}
    }

	String name;
	
	/**
     * Is Smart Store feature?
     */
    boolean isSmartStore; 
    
    /**
     * Is Smart Savings feature?
     */
    boolean isSmartSavings;
    
    String title;
    
    String prez_title = null;
    
    String prez_desc = null;
    
	private EnumSiteFeature(String name) {
		this.name = name;
		this.isSmartStore = false;
		this.isSmartSavings = false;
		staticEnum.put(name, this);
	}

	private EnumSiteFeature(String name, boolean isSmartStore, String title) {
		this.name = name;
		this.isSmartStore = isSmartStore;
		this.isSmartSavings = false;
		this.title = title;
		staticEnum.put(name, this);
	}

	private EnumSiteFeature(String name, boolean isSmartStore, boolean isSmartSavings, String title) {
		this.name = name;
		this.isSmartStore = isSmartStore;
		this.isSmartSavings = isSmartSavings;
		this.title = title;
		staticEnum.put(name, this);
	}

	public EnumSiteFeature(DynamicSiteFeature sf) {
		this.name = sf.getName();
		this.isSmartStore = true;
		this.isSmartSavings = false;
		this.title = sf.getTitle();
		this.prez_title = sf.getPresentationTitle();
		this.prez_desc = sf.getPresentationDescription();
		dynamicEnum.put(name, this);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnumSiteFeature other = (EnumSiteFeature) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Object o) {
		EnumSiteFeature e = (EnumSiteFeature) o;
		if (title != null && e.title != null)
			return title.compareTo(e.title);
		return name.compareTo(e.name);
	}
	
	public String toString() {
		return "EnumSiteFeature[" + name + "]";
	}
	
	protected static void loadDynamicSiteFeatures() {
		synchronized (loaded) {
			if (loaded.isReset()) {
				if (mocked.isReset()) {
					Iterator it = SmartStoreServiceConfiguration.getInstance().loadSiteFeatures().iterator();
					while (it.hasNext()) {
						new EnumSiteFeature(((DynamicSiteFeature) it.next()));
					}
				}
				loaded.set();
			}
		}
	}

	public static void refresh() {
		synchronized (loaded) {
			dynamicEnum.clear();
			loaded.reset();
		}
	}
	

	public static EnumSiteFeature getEnum(String name) {
		loadDynamicSiteFeatures();
		EnumSiteFeature dynamic = (EnumSiteFeature) dynamicEnum.get(name);
		if (dynamic != null)
			return dynamic;
		return (EnumSiteFeature) staticEnum.get(name);
	}

	public static Map getEnumMap() {
		loadDynamicSiteFeatures();
		Map enums = new HashMap();
		synchronized (staticEnum.keySet()) {
			enums.putAll(staticEnum);
		}
		synchronized (dynamicEnum.keySet()) {
			enums.putAll(dynamicEnum);			
		}
		return enums;
	}

	public static List getEnumList() {
		return new ArrayList(getEnumMap().values());
	}

	public static Iterator iterator() {
		return getEnumMap().values().iterator();
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
	
	public String getName() {
		return name;
	}

	public String getPresentationTitle() {
		return prez_title;
	}

	public String getPresentationDescription() {
		return prez_desc;
	}

	public boolean isSmartStore() {
		return isSmartStore;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static List getSmartStoreEnumList() {
		List list = new ArrayList();
		
		Iterator it = iterator();
		while (it.hasNext()) {
			EnumSiteFeature sf = (EnumSiteFeature) it.next();
			if (sf.isSmartStore)
				list.add(sf);
		}
		
		return list;
	}

	public static List getSmartSavingsFeatureList() {
		List list = new ArrayList();
		
		Iterator it = iterator();
		while (it.hasNext()) {
			EnumSiteFeature sf = (EnumSiteFeature) it.next();
			if (sf.isSmartSavings)
				list.add(sf);
		}
		
		return list;
	}

	public static void mock() {
		mocked.set();
	}
}
