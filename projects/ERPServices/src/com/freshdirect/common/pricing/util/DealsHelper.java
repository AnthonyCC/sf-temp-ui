package com.freshdirect.common.pricing.util;

import java.util.List;

import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.FDStoreProperties;

public class DealsHelper {

    public static boolean isItemOnSale(double sellingPrice, double promoPrice) {
		if (!isValidInput(promoPrice) || !isValidInput(sellingPrice) ||  sellingPrice<=promoPrice) {
			return false;
		}
		int p = getVariancePercentage(sellingPrice,promoPrice);
		return ( (FDStoreProperties.getDealsLowerLimit()<=p) && (FDStoreProperties.getDealsUpperLimit()>=p) );
	}
	
	public static boolean isShowBurstImage(double sellingPrice, double promoPrice) {
		if (!isValidInput(promoPrice) || !isValidInput(sellingPrice) ||  sellingPrice<=promoPrice) {
			return false;
		}
		int p = getVariancePercentage(sellingPrice,promoPrice);
		return ( (FDStoreProperties.getBurstsLowerLimit()<=p) && (FDStoreProperties.getBurstUpperLimit()>=p) );
	}
	
	/**
	 * Returns a -ve integer if either the basePrice or sellingPrice is zero or 
	 * if the sellingPrice is greater than or equal to basePrice.
	 * The formula used is (basePrice - sellingPrice) / basePrice x 100
	 * The percentage is rounded down to the nearest integer divisible by 5 or 2.
	 * Ex: 27.XX becomes 26, 25.XX becomes 25 etc.
	 * 
	 * @param basePrice 
	 * @param sellingPrice
	 * @return
	 */
	public static int getVariancePercentage(double sellingPrice, double promoPrice) {
        if (!isValidInput(promoPrice) || !isValidInput(sellingPrice) || sellingPrice <= promoPrice) {
			return -1;
        }
        return (int) ((sellingPrice - promoPrice) * 100.0 / sellingPrice + 0.2);
	}
	
	public static double determineBasePrice(double sellingPrice, double promoPrice) {
		return sellingPrice >= promoPrice ? sellingPrice : promoPrice;
	}

	private static boolean isValidInput(double input) {
		return input > 0.0;
	}

    public static int determineTieredDeal(double sellingPrice, List<ErpMaterialPrice> materialPrices) {
		int highest = 0;
        for (ErpMaterialPrice materialPrice : materialPrices) {
            highest = Math.max(getVariancePercentage(sellingPrice, materialPrice.getPrice()), highest);
		}
        return highest;
	}
	
	public static boolean isDealOutOfBounds(int price) {
		return price < FDStoreProperties.getDealsLowerLimit()
				|| price > FDStoreProperties.getDealsUpperLimit();
	}
}
