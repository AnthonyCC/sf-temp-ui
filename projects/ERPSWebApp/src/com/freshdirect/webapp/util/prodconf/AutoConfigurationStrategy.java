package com.freshdirect.webapp.util.prodconf;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public class AutoConfigurationStrategy extends SimpleConfigurationStrategy {
	private static Logger LOGGER = LoggerFactory.getInstance(AutoConfigurationStrategy.class);

	public ProductImpression configure(ProductModel productModel,
			ConfigurationContext context) {
		FDConfigurableI configuration = productModel.getAutoconfiguration();
		if (configuration != null) {
			String sku = productModel.getDefaultSkuCode();
					
			if (sku != null) {
				return new TransactionalProductImpression(productModel,
						sku, configuration);
			} else
				return super.configure(productModel, context);
		} else 
			return super.configure(productModel, context);
	}
}
