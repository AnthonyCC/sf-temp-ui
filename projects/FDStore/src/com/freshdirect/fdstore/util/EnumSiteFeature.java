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
import com.freshdirect.framework.util.Latch;
import com.freshdirect.smartstore.ejb.DynamicSiteFeature;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;


public class EnumSiteFeature implements Serializable, Comparable<EnumSiteFeature> {
	
	private static final long serialVersionUID = -2545205419352991876L;

	private final static String PROFILE_PREFIX = "siteFeature.";

    private final static Map<String, EnumSiteFeature> staticEnum = Collections.synchronizedMap(new HashMap<String, EnumSiteFeature>());

    private static Map<String, EnumSiteFeature> dynamicEnum = Collections.synchronizedMap(new HashMap<String, EnumSiteFeature>());
    
    private final static Latch loaded = new Latch(false);
    
    private final static Latch mocked = new Latch(false);
    
    /**
     * @deprecated
     */
	public final static EnumSiteFeature NEW_SEARCH = new EnumSiteFeature("newSearch");
	public final static EnumSiteFeature CCL = new EnumSiteFeature("CCL");
	public final static EnumSiteFeature DYF = new EnumSiteFeature("DYF", true, "Your Favorites");
	public final static EnumSiteFeature YMAL = new EnumSiteFeature("YMAL", true, "You Might Also Like");
	public final static EnumSiteFeature RATING = new EnumSiteFeature("RATING");
    public final static EnumSiteFeature FEATURED_ITEMS = new EnumSiteFeature("FEATURED_ITEMS", true, "Featured Items");
    public final static EnumSiteFeature FAVORITES = new EnumSiteFeature("FAVORITES", true, "FreshDirect Favorites");
    public final static EnumSiteFeature CART_N_TABS = new EnumSiteFeature("CART_N_TABS", true, "Cart & Tabs");
    public final static EnumSiteFeature SMART_CATEGORY = new EnumSiteFeature("SMART_CATEGORY", true, "Smart Category (virtual)");
    
    //gift cards
    public final static EnumSiteFeature GIFT_CARDS = new EnumSiteFeature("giftCards");
    //Zone Pricing
    public final static EnumSiteFeature ZONE_PRICING = new EnumSiteFeature("zonePricing");
    
    //Product Group Popular
    public final static EnumSiteFeature PROD_GRP_POPULAR = new EnumSiteFeature("PROD_GRP_POPULAR");
    //Product Group Your Favorites
    public final static EnumSiteFeature PROD_GRP_YF = new EnumSiteFeature("PROD_GRP_YF");

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

	public EnumSiteFeature(DynamicSiteFeature sf) {
		this.name = sf.getName();
		this.isSmartStore = true;
		this.title = sf.getTitle();
		this.prez_title = sf.getPresentationTitle();
		this.prez_desc = sf.getPresentationDescription();
		this.isSmartSavings = sf.isSmartSaving();
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

	public int compareTo(EnumSiteFeature e) {
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
					Iterator<DynamicSiteFeature> it = SmartStoreServiceConfiguration.getInstance().loadDynamicSiteFeatures().iterator();
					while (it.hasNext()) {
						new EnumSiteFeature( it.next() );
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
		EnumSiteFeature dynamic = dynamicEnum.get(name);
		if (dynamic != null)
			return dynamic;
		return staticEnum.get(name);
	}

	public static Map<String, EnumSiteFeature> getEnumMap() {
		loadDynamicSiteFeatures();
		Map<String, EnumSiteFeature> enums = new HashMap<String, EnumSiteFeature>();
		synchronized (staticEnum.keySet()) {
			enums.putAll(staticEnum);
		}
		synchronized (dynamicEnum.keySet()) {
			enums.putAll(dynamicEnum);			
		}
		return enums;
	}

	public static List<EnumSiteFeature> getEnumList() {
		return new ArrayList<EnumSiteFeature>(getEnumMap().values());
	}

	public static Iterator<EnumSiteFeature> iterator() {
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
	
	public boolean isSmartSavings() {
		return isSmartSavings;
	}
	
	public String getTitle() {
		return title;
	}
	
        public static List<EnumSiteFeature> getSmartStoreEnumList() {
            List<EnumSiteFeature> list = new ArrayList<EnumSiteFeature>();
    
            Iterator<EnumSiteFeature> it = iterator();
            while (it.hasNext()) {
                EnumSiteFeature sf = it.next();
                if (sf.isSmartStore) {
                    list.add(sf);
                }
            }
    
            return list;
        }

	public static List<EnumSiteFeature> getSmartSavingsFeatureList() {
		List<EnumSiteFeature> list = new ArrayList<EnumSiteFeature>();
		
		Iterator<EnumSiteFeature> it = iterator();
		while (it.hasNext()) {
			EnumSiteFeature sf = it.next();
			if (sf.isSmartSavings)
				list.add(sf);
		}
		
		return list;
	}

	public static void mock() {
		mocked.set();
	}
}
