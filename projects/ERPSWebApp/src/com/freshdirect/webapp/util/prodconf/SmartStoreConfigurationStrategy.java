package com.freshdirect.webapp.util.prodconf;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.ProductSkuImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

/**
 * Configuration utility for DYF impressions.
 * 
 * The strategy uses an LRU cache, whose entries can be set as
 * fdstore.strategy.cache.entries in the fdstore.properties file (default 1000)
 * @author istvan
 *
 */
public class SmartStoreConfigurationStrategy extends FallbackConfigurationStrategy {
	
	private final static int MAX_CONFIGURATION_DURATION = 10 * 60 * 1000;
	
	private final static Logger LOGGER = LoggerFactory.getInstance(SmartStoreConfigurationStrategy.class);
	
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
	protected static Map<CustomerContentPair, SkuConfigurationPair> cache; 
	
	protected static void setupCache() {
		final int maxEntries = FDStoreProperties.getMaxDyfStrategyCacheEntries();
		cache = new LinkedHashMap<CustomerContentPair, SkuConfigurationPair>(3 * maxEntries / 2 + 1, 0.75f, true) {
			
			private static final long serialVersionUID = 4998658502428769840L;

			protected boolean removeEldestEntry(Map.Entry<CustomerContentPair, SkuConfigurationPair> e) {
				return (size() > maxEntries);
			}		
		};
	}



	protected static ConfigurationStrategy instance = null;

	public static ConfigurationStrategy getInstance() {
		if (instance == null) {
			setupCache();
			instance = new SmartStoreConfigurationStrategy(
					new ConfiguredProductGroupConfigurationStrategy(
					new ConfiguredProductConfigurationStrategy(
					new AutoConfigurationStrategy())));
		}
		return instance;
	}


	public SmartStoreConfigurationStrategy(ConfigurationStrategy fallback) {
		super(fallback);
	}

	/**
	 * Configure a recommended product from the user's shopping history.
	 * 
	 * These are the steps:
	 * <ul>
	 *    <li>Get all the sku codes for the product.</li>
	 *    <li>For each sku code, get all configurations</li>
	 *    <li>Sort the items by recency</li>
	 *    <li>Return the first {@link OrderLineUtil#cleanup(FDProductSelectionI) valid} configuration</li>
	 *    <li>If all fail, return a non-transactional {@link ProductImpression}
	 * </ul>
	 * 
	 * @param productModel product model
	 * @param context context with ERP customer id
	 * @return product impression
	 */
	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		String erpCustomerPK = getCustomerPK(context);

		// if no appropriate user in the context automatically fall back
		if (erpCustomerPK == null) {
			return super.configure(productModel, context);
		}

		// see if already stored

		CustomerContentPair key = new CustomerContentPair(
				erpCustomerPK,
				productModel.getContentKey());

		SkuConfigurationPair storedConfiguration = cache.get(key);
                if (storedConfiguration != null && !storedConfiguration.expired()) {
                    if (storedConfiguration.getSkuCode() != null) {
                        if (storedConfiguration.getConfiguration() != null) {
                            // there is a configuration ...
                            return new TransactionalProductImpression(productModel, storedConfiguration.getSkuCode(), 
                                    storedConfiguration.getConfiguration());
                        } else {
                            // no configuration present, return a ProductSkuImpression
                            return new ProductSkuImpression(productModel, storedConfiguration.getSkuCode());
                        }
                    } else {
                        return super.configure(productModel, context);
                    }
                }
		
		try {
			// Get order details
			// List<FDCustomerProductListLineItem>
			List<FDCustomerProductListLineItem> lineItems = getUserLineItems(erpCustomerPK, productModel.getSkuCodes());

			// fallback if no answer
			if (lineItems.size() == 0) {
				cache.put(key, SkuConfigurationPair.NULL);
				return super.configure(productModel, context);
			}
			
			// sort items by recency
			Collections.sort(lineItems,
				new Comparator<FDCustomerListItem>() {
					public int compare(FDCustomerListItem item1, FDCustomerListItem item2) {
						// flip sign, since the newer (larger date) the better
						return - item1.getLastPurchase().compareTo(item2.getLastPurchase());
					}
				}
			);

			// go through list and get first good one
			for(FDCustomerProductListLineItem item : lineItems) {
				
				String selectedSkuCode = item.getSkuCode();
				SkuModel skuModel = (SkuModel) ContentFactory.getInstance().getContentNode(selectedSkuCode); 
				
				// not available, skip
				if (skuModel.isUnavailable())
					continue;

				FDConfigurableI configuration = item.getConfiguration();
				// no stored configuration, so it's an unconfigurable product model
				if (configuration == null) {
				    cache.put(key, new SkuConfigurationPair(selectedSkuCode, null));
				    return new ProductSkuImpression(productModel, selectedSkuCode);
				}
				
				// there was a stored configuration, lets see if ok
				try {
					FDProductSelectionI selection = item.convertToSelection();
					OrderLineUtil.cleanup(selection);
					
					configuration = selection.getConfiguration();

					if (configuration == null)
						continue;
					
					// see if sku has changed as result of cleanup
					if (!selection.getSku().getSkuCode().equals(selectedSkuCode)) {
						selectedSkuCode = selection.getSkuCode();
					}
				// any problems, skip
				} catch (FDInvalidConfigurationException e) {
					continue;
				} catch (FDSkuNotFoundException e) {
					continue;
				} catch (IllegalStateException e) {
					continue;
				}
				
				// ok, we have a transactional impression
				// mask out any larger quantity
				FDConfigurableI conf = new FDConfiguration(productModel.getQuantityMinimum(),
							configuration.getSalesUnit(), configuration.getOptions());
						
				LOGGER.debug("Storing configuration for "
						+ erpCustomerPK
						+ ", sku = " + selectedSkuCode);
				cache.put(key, new SkuConfigurationPair(selectedSkuCode, conf));
				LOGGER.debug("configuring using smart store configurer + "
						+ productModel.getContentKey().getId());
				return new TransactionalProductImpression(productModel, selectedSkuCode, conf);
			}
			
			// we do not have a transactional impression, do a fallback
			cache.put(key, SkuConfigurationPair.NULL);
			return super.configure(productModel, context);
			
		} catch (FDResourceException e) {
			// could not make query, do a fallback
			// Do not cache though, may work next time :)
			return super.configure(productModel, context);
		}
	}


	/**
	 * Returns the ERP PK of the customer in context
	 * 
	 * @param context
	 * @return Customer's ERP primary key or null if not available
	 */
	protected String getCustomerPK(ConfigurationContext context) {
		try {
			return context.getFDUser().getIdentity().getErpCustomerPK();
		} catch(Exception exc) {
			// this is to handle NPE while dereferencing
		}
		return null;
	}

	

	
	/**
	 * @param erpCustomerPK
	 * @param skuCodes
	 * 
	 * @return List<FDCustomerProductListLineItem>
	 */
	@SuppressWarnings("unchecked")
        protected List<FDCustomerProductListLineItem> getUserLineItems(String erpCustomerPK, List<String> skuCodes) throws FDResourceException {
            FDCustomerProductList details = FDListManager.getOrderDetails(erpCustomerPK, skuCodes);
            // FDCustomerProductList contains only FDCustomerProductListLineItem-s 
            return (List<FDCustomerProductListLineItem>) (List) details.getLineItems();
        }
}
