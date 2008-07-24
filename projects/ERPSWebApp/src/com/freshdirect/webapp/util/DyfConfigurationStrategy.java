package com.freshdirect.webapp.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;

/**
 * Configuration utility for DYF impressions.
 * 
 * The strategy uses an LRU cache, whose entries can be set as
 * fdstore.strategy.cache.entries in the fdstore.properties file (default 1000)
 * @author istvan
 *
 */
public class DyfConfigurationStrategy implements ConfigurationStrategy {
	
	private final static int MAX_CONFIGURATION_DURATION = 10 * 60 * 1000;
	
	private final static Category LOGGER = Category.getInstance(DyfConfigurationStrategy.class);
	
	/**
	 * Wrapper for a customer id and a content key.
	 */
	protected static class CustomerContentPair {
		private String erpCustomerId;
		private ContentKey contentKey;
		
		/**
		 * Constructor.
		 * @param erpCustomerId
		 * @param contentKey
		 */
		protected CustomerContentPair(String erpCustomerId, ContentKey contentKey) {
			this.erpCustomerId = erpCustomerId;
			this.contentKey = contentKey;
		}
		
		public boolean equals(Object o) {
			CustomerContentPair op = (CustomerContentPair)o;
			return  
				op.erpCustomerId.equals(erpCustomerId) && 
				op.contentKey.equals(contentKey);
		}
		
		public int hashCode() {
			return erpCustomerId.hashCode() ^ contentKey.hashCode();
		}
	}
	
	/**
	 * Wrapper for a sku code and a configuration.
	 */
	protected static class SkuConfigurationPair {
		private String skuCode;
		private FDConfigurableI configuration;
		private long timeRecorded;
		
		public static SkuConfigurationPair NULL = new SkuConfigurationPair(null,null);
		
		/**
		 * Constructor.
		 * @param skuCode
		 * @param configuration
		 */
		public SkuConfigurationPair(String skuCode, FDConfigurableI configuration) {
			this.skuCode = skuCode;
			this.configuration = configuration;
			this.timeRecorded = System.currentTimeMillis();
		}
		
		/**
		 * Get sku code.
		 * @return cached sku code or null 
		 */
		public String getSkuCode() {
			return skuCode;
		}
		
		/**
		 * Get configuration.
		 * @return cached configuration
		 */
		public FDConfigurableI getConfiguration() {
			return configuration;
		}
		
		/**
		 * Is configuration expired?
		 */
		public boolean expired() {
			long diff = System.currentTimeMillis() - timeRecorded;
			boolean exp = diff > MAX_CONFIGURATION_DURATION;
			if (exp) LOGGER.debug("Cached configuration for " + skuCode + " expired after " + (diff/1000) + " seconds");
			return exp;
		}
	}
	
	
	/**
	 * LRU cache.
	 * 
	 * {Customer,ConentKey} -> {Sku,Configuration} 
	 *
	 */
	protected static Map cache; 
	
	private static ConfigurationStrategy instance = null;
	
	public static ConfigurationStrategy getInstance() {
		if (instance == null) {
			final int maxEntries = FDStoreProperties.getMaxDyfStrategyCacheEntries();
			cache = new LinkedHashMap(3*maxEntries /2 + 1, 0.75f, true) {
				
				private static final long serialVersionUID = 4998658502428769840L;

				protected boolean removeEldestEntry(Map.Entry e) {
					return (size() > maxEntries);
				}		
			};
			instance = new DyfConfigurationStrategy();
		}
		return instance;
	}
	
	
	// make sku model from sku code
	private static SkuModel makeSkuModel(String skuCode) {
		return (SkuModel) ContentNodeModelUtil.constructModel(new ContentKey(FDContentTypes.SKU,skuCode),true);
	}
	
	/**
	 * Configure a recommended product from the user's shopping history.
	 * 
	 * These are the steps:
	 * <ul>
	 *    <li>Get all the sku codes for the product.</li>
	 *    <li>For each sku code, get all configurations</li>
	 *    <li>Sort the items by recency</li> TODO check with Neal
	 *    <li>Return the first {@link OrderLineUtil#cleanup(FDProductSelectionI) valid} configuration</li>
	 *    <li>If all fail, return a non-transactional {@link ProductImpression}
	 * </ul>
	 * 
	 * @param productModel product model
	 * @param context context with ERP customer id
	 * @return product impression
	 */
	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		
		// see if already stored
		CustomerContentPair key = new CustomerContentPair(context.getErpCustomerId(),productModel.getContentKey());
		SkuConfigurationPair storedConfiguration = (SkuConfigurationPair)cache.get(key);
        if (storedConfiguration != null && !storedConfiguration.expired()) {
        	if (storedConfiguration.getSkuCode() != null) {
        		return new TransactionalProductImpression(
        			productModel,
        			storedConfiguration.getSkuCode(),
        			storedConfiguration.getConfiguration());
        	} else {
        		return new ProductImpression(productModel);
        	}
        }
		
		try {
			// Get order details
			FDCustomerProductList details = FDListManager.getOrderDetails(context.getErpCustomerId(), productModel.getSkuCodes());
			
			// if no answer, return unconfigured impression
			if (details.getLineItems().size() == 0) {
				cache.put(key,SkuConfigurationPair.NULL);
				return new ProductImpression(productModel);
			}
			
			// sort items by recency
			Collections.sort(details.getLineItems(),
				new Comparator() {
		
					public int compare(Object o1, Object o2) {
						FDCustomerProductListLineItem item1 = (FDCustomerProductListLineItem)o1;
						FDCustomerProductListLineItem item2 = (FDCustomerProductListLineItem)o2;
						// flip sign, since the newer (larger date) the better
						return - item1.getLastPurchase().compareTo(item2.getLastPurchase());
					}
				}
			);
			
			// go through list and get first good one
			
			for(Iterator i = details.getLineItems().iterator(); i.hasNext();) {
				FDCustomerProductListLineItem item = (FDCustomerProductListLineItem)i.next();
				
				String selectedSkuCode = item.getSkuCode();
				SkuModel skuModel = makeSkuModel(selectedSkuCode); 
				
				// not available, skip
				if (skuModel.isUnavailable()) continue;
				FDConfigurableI configuration = item.getConfiguration();
				
				// no stored configuration and no auto-configuration, skip
				if (configuration == null && !productModel.isAutoconfigurable()) continue;
				
				
				// there was a stored configuration, lets see if ok
				if (configuration != null) {
					
					try {
						FDProductSelectionI selection = item.convertToSelection();
						OrderLineUtil.cleanup(selection);
						
						configuration = selection.getConfiguration();
						
						// see if sku has changed as result of cleanup
						if (!selection.getSku().getSkuCode().equals(selectedSkuCode)) {
							selectedSkuCode = selection.getSkuCode();
						}
						
					// any problems, skup
					} catch (FDInvalidConfigurationException e) {
						continue;
					} catch (FDSkuNotFoundException e) {
						continue;
					} catch (IllegalStateException e) {
						continue;
					}
				}
				
				// ok, we have a transactional impression
				
				FDConfigurableI conf = 
					configuration == null ? 
						productModel.getAutoconfiguration() : 
						new FDConfiguration(
							productModel.getQuantityMinimum(), // mask out any larger quantity
							configuration.getSalesUnit(),
							configuration.getOptions());
						
				LOGGER.debug("Storing configuration for " + context.getErpCustomerId() + ", sku = " + selectedSkuCode);
				cache.put(key, new SkuConfigurationPair(selectedSkuCode,conf));
				return new TransactionalProductImpression(
					productModel,selectedSkuCode,conf);
			}
			
			// we do not have a transactional impression, return a non-transactional
			cache.put(key,SkuConfigurationPair.NULL);
			return new ProductImpression(productModel);
			
		} catch (FDResourceException e) {
			// could not make query, return non-transactional
			// Do not cache though, may work next time :)
			return new ProductImpression(productModel);
		}
	}
}
