package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.framework.util.log.LoggerFactory;

public class NutritionComparator implements Comparator<FilteringProductItem> {

	private static final Logger LOGGER = LoggerFactory.getInstance( NutritionComparator.class ); 
	
	private ErpNutritionType.Type erpNutritionTypeType;
	
	public NutritionComparator(ErpNutritionType.Type erpNutritionTypeType){
		this.erpNutritionTypeType = erpNutritionTypeType;
	}
	
	@Override
	public int compare(FilteringProductItem productItem1, FilteringProductItem productItem2) {
		int compareValue;
		
		if (erpNutritionTypeType == null){
			LOGGER.error("erpNutritionTypeType is null, returning 0");
			compareValue = 0;

		} else {
		    boolean unavailable1 = productItem1.getProductModel().isUnavailable();
		    boolean unavailable2 = productItem2.getProductModel().isUnavailable();
		    if (unavailable1 && unavailable2) {
		        return 0;
		    } else if (unavailable1) {
		        return 1;
		    } else if (unavailable2) {
		        return -1;
		    }

			Double nutritionValue1 = getNutritionValue(productItem1);
			Double nutritionValue2 = getNutritionValue(productItem2);
			
			if (nutritionValue1==null) {
				if (nutritionValue2==null) {
					compareValue = 0;
				} else {
					compareValue = 1; //null value goes in the back
				}
			} else {
				if (nutritionValue2==null) {
					compareValue = -1;  //null value goes in the back
				} else {
					compareValue = nutritionValue1 > nutritionValue2 ? 1 : (nutritionValue1 < nutritionValue2 ? -1 : 0);
					if (erpNutritionTypeType.isGood()){
						compareValue *= -1;
					}
				}
			}
		}
		return compareValue;
	}

	private Double getNutritionValue(FilteringProductItem productItem){
		Double nutritionValue = null;
		try {
		    FDProduct product = productItem.getFdProduct();
			FDNutrition servingSizeFdNutrition = product.getNutritionItemByType(ErpNutritionType.getType(ErpNutritionType.SERVING_SIZE));
			if (servingSizeFdNutrition!=null && servingSizeFdNutrition.getValue() != 0){ //missing serving size will go to the bottom of the list
				FDNutrition fdNutrition = product.getNutritionItemByType(erpNutritionTypeType);
				
				if (fdNutrition!=null){
					nutritionValue = fdNutrition.getValue() * 1000;
					if (nutritionValue < 0) {
						nutritionValue = 0.001;
					}
				}
			}
		} catch (FDResourceException e) {
        	LOGGER.error("Failed to obtain fdProduct for product " + productItem.getProductModel().getContentName(), e);
        }
		return nutritionValue;
	}
}
