package com.freshdirect.storeapi.content;

import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.CmsLegacy;

@CmsLegacy
public class ContentUtil {

	private static final Logger LOGGER = LoggerFactory.getInstance(ContentUtil.class);

    //Origin : [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
	public static boolean isAvailableByContext(ProductModel product) {

		if(product != null) {
			List<ProductFilterI> productFilters = ProductFilterFactory.getInstance().getDefaultFilters();
			try {
				for (ProductFilterI fi : productFilters) {
					if(!fi.apply(product)) {
						return false;
					}
				}
			} catch (FDResourceException fdre) {
				LOGGER.error("isAvailableByContext FDResourceException :", fdre);
			}
		}
		return true;
	}
}
