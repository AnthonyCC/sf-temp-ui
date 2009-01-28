package com.freshdirect.webapp.util;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;

/**
 * Get a {@link ConfigurationStrategy} for the given {@link EnumSiteFeature site feature}.
 * 
 * @author istvan
 *
 */
public class ConfigurationStrategyFactory {

	// Map<String,ConfigurationUtil>
	static private Map utils = new HashMap();
	
	// simply wraps the product model
	static private ConfigurationStrategy defaultUtil = new ConfigurationStrategy() {

		public ProductImpression configure(ProductModel productModel, ConfigurationContext context) {
			return new ProductImpression(productModel);
		}
		
	};

	// establish site feature -> configuration util mappings
	private static void instantiate() {
		utils.put(EnumSiteFeature.DYF, DyfConfigurationStrategy.getInstance());
		utils.put(EnumSiteFeature.YMAL, new YmalConfigurationStrategy());
		utils.put(EnumSiteFeature.FAVORITES, new FavoritesConfigurationStrategy());
	}
	
	/**
	 * Get configuration util for site feature.
	 * @param feature site feature
	 * @return the matching configuration util (if found) or a default one that will only wrap the product
	 */
	synchronized static public ConfigurationStrategy getConfigurationStrategy(EnumSiteFeature feature) {
		if (utils.size() == 0) instantiate();
		ConfigurationStrategy requestedUtil = (ConfigurationStrategy)utils.get(feature);
		return requestedUtil == null ? defaultUtil : requestedUtil;
	}
}
