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
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.smartstore.impl.FavoritesRecommendationService;

/**
 * Configuration utility for DYF impressions.
 * 
 * The strategy uses an LRU cache, whose entries can be set as
 * fdstore.strategy.cache.entries in the fdstore.properties file (default 1000)
 * @author istvan
 *
 */
public class FavoritesConfigurationStrategy implements ConfigurationStrategy {
	
	private static ConfigurationStrategy instance = null;
	
	public static ConfigurationStrategy getInstance() {
		if (instance == null) {
			instance = new FavoritesConfigurationStrategy();
		}
		return instance;
	}
	
	/**
	 * Configure a recommended product from the recommenders thread local (It's
	 * a HACK) !!! SHOULD BE REFACTORED !!!
	 * 
	 * @param productModel
	 *            product model
	 * @param context
	 *            context with ERP customer id
	 * @return product impression
	 */
	public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
		Map cfgProds = (Map) FavoritesRecommendationService.CFG_PRODS.get();
		ConfiguredProduct configuredProduct = (ConfiguredProduct) cfgProds.get(productModel.getContentKey().getId());
		if (configuredProduct != null) {
			SkuModel sku = configuredProduct.getPreferredSku() == null
					? configuredProduct.getDefaultSku()
					: configuredProduct.getPreferredSku();

			return sku == null
					? new ProductImpression(productModel)
					: new TransactionalProductImpression(
							configuredProduct.getProduct(),
							sku.getSkuCode(),
							configuredProduct.getConfiguration()
					);
			
		} else {
			return YmalConfigurationStrategy.plainProductConfigurationUtil.configure(productModel, context);
		}
	}
}
