package com.freshdirect.fdstore.content.browse.filter;

import org.apache.log4j.Logger;

import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;

public class NutritionFilter extends AbstractRangeFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance( NutritionFilter.class ); 

	private ErpNutritionType.Type nutritionType;

	public NutritionFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
	
		final String nutritionCode = model.getNutritionCode();

		this.nutritionType = ErpNutritionType.getType(nutritionCode);

		if (this.nutritionType == null) {
			LOGGER.warn("Invalid nutrition type " + nutritionCode + " filter will fail");
		}
	}

	@Override
	public boolean apply(FilteringProductItem productItem) throws FDResourceException {
		if (productItem == null || productItem.getProductModel() == null || nutritionType == null || productItem.getProductModel().isUnavailable()) {
			return false;
		}
		
		FDProduct fdPrd = productItem.getFdProduct();
		
		final String displayName = nutritionType.getDisplayName();
		for (FDNutrition nutrition : fdPrd.getNutrition()) {
			if (displayName.equalsIgnoreCase( nutrition.getName() )) {
				return invertChecker(isWithinRange( nutrition.getValue() ));
			}
		}

		return invertChecker(false);
	}

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.ERPS;
	}

}
