package com.freshdirect.fdstore.content.browse.filter;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.NutritionValueEnum;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;

public abstract class NutritionInfoFilter extends AbstractProductItemFilter {
	private static final Logger LOGGER = LoggerFactory.getInstance( NutritionInfoFilter.class );

	protected String claimCode;
	
	/**
	 * Default constructor
	 * 
	 * @param model
	 */
	public NutritionInfoFilter(ProductFilterModel model, String parentId) {
		super(model, parentId);
		
		this.claimCode = model.getErpsyFlagCode();
	}

	public NutritionInfoFilter(ProductFilterModel model, String claimCode, String parentId) {
		super(model, parentId);
		
		this.claimCode = claimCode;
	}

	public NutritionInfoFilter(String id, String parentId, String claimCode, String name) { //'virtual' nutritionFilter for search page 
		super(id, parentId, name);
		this.claimCode = claimCode;
	}

	protected abstract ErpNutritionInfoType getType();
	
	protected Collection<? extends NutritionValueEnum> getValues(FDProduct prd) {
		return prd.getNutritionInfoList(getType());
	}
	
	@Override
	public boolean apply(FilteringProductItem productItem) {

		if (productItem == null || productItem.getProductModel() == null || claimCode == null || productItem.getProductModel().isUnavailable()) {
			return false;
		}

		try {
			FDProduct fdProd = productItem.getFdProduct();
			
			Collection<? extends NutritionValueEnum> values = getValues(fdProd);
			if(values==null){
				return invertChecker(false);
			}
			
			for (NutritionValueEnum claim : values) {
				if (claimCode.equalsIgnoreCase(claim.getCode())) {
					return invertChecker(true);
				}
			}
        } catch (FDResourceException e) {
            if (FDStoreProperties.getPreviewMode()) {
                LOGGER.debug("No fdProduct found for product " + productItem.getProductModel().getContentName());
            } else {
                LOGGER.error("Failed to obtain fdProduct for product " + productItem.getProductModel().getContentName(), e);
            }
            return false;
        }

		return invertChecker(false);
	}
}
