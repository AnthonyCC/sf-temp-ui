package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.OncePerRequestDateCache;
import com.freshdirect.framework.util.DateUtil;

public class ConfiguredProductGroup extends ConfiguredProduct {

	private final List items = new ArrayList();
	
	public ConfiguredProductGroup(ContentKey key) {
		super(key);
	}

	//
	// configured product-specific methods  
	//


	/**
	 *  Get the available configured product.
	 *  The selection method is the following:
	 *  <ul>
	 *    <li>the first configured product is returned, if one is available
	 *        by tomorrow</li>
	 *    <li>othwise, the first generally available configured product is
	 *        returned</li>
	 *    <li>otherwise, null is returned</li>
	 *  </ul>
	 *  
	 *  @return the availabe configured product (see above)
	 */
	private ConfiguredProduct getAvailableConfiguredProduct() {
		List items = (List) getCMSNode().getAttributeValue("items");
		if (items == null || items.isEmpty()) {
			return null;
		}
		
		ContentFactory contentFactory = ContentFactory.getInstance();

		// loop through the products in the group, and see if anything is available tomorrow,
		// or what is the first product available otherwise
		ConfiguredProduct tomorrowProduct  = null;
		ConfiguredProduct availableProduct = null;
		Date now = OncePerRequestDateCache.getToday();
		if(now == null){
			now = DateUtil.truncate(new Date());
		} 
		
		for (Iterator it = items.iterator(); it.hasNext(); ) {
			
			ContentKey        key     = (ContentKey) it.next();
			ConfiguredProduct product = (ConfiguredProduct) contentFactory.getContentNode(key.getId());
			Date              avail   = product.getEarliestAvailability();
			
			if (avail == null) {
				continue;
			}
			
			if (tomorrowProduct == null && (!now.before(avail) || DateUtil.getDiffInDays(now, avail) <= 1)) {
				tomorrowProduct = product;
			}
			if (availableProduct == null) {
				availableProduct = product;
			}
			
			if (tomorrowProduct != null && availableProduct != null) {
				break;
			}
		}

		return tomorrowProduct != null
		       ? tomorrowProduct
		       : availableProduct;
	}
	
	/**
	 *  Returns the first configured product, regardless of the fact
	 *  that is available or not.
	 *  
	 *  @return the first configured product, or null if there are no products
	 *          inside this group
	 */
	private ConfiguredProduct getFirstConfiguredProduct() {
            List items = (List) getCMSNode().getAttributeValue("items");
            if (items == null || items.isEmpty()) {
                    return null;
            }
		
		ContentFactory contentFactory = ContentFactory.getInstance();

		Iterator          it      = items.iterator();
		ContentKey        key     = (ContentKey) it.next();
		ConfiguredProduct product = (ConfiguredProduct) contentFactory.getContentNode(key.getId());
		
		return product;
	}
	
	public FDConfigurableI getConfiguration() {
		ConfiguredProduct cp = getAvailableConfiguredProduct();
		return cp == null ? null : cp.getConfiguration();
	}

	/**
	 * @return the ProductModel associated with this configuration
	 */
	public ProductModel getProduct() {
		return getAvailableConfiguredProduct();
	}

	public String getFullName() {
		ConfiguredProduct product;
		
		if ((product = getAvailableConfiguredProduct()) != null) {
			return product.getFullName();
		}
		
		if ((product = getFirstConfiguredProduct()) != null) {
			return product.getFullName();
		}
		
		return getAttribute("name", getContentName());
	}
	
	public String getUnavailabilityMessage() {
		return getAttribute("unavailabilityMessage", "");
	}

	public boolean isRequired() {
		return getAttribute("required", false);
	}
	
	public List getItems() {
		ContentNodeModelUtil.refreshModels(this, "items", items, true);

		return new ArrayList(items);
	}
	
}
