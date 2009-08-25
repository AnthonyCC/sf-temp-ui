package com.freshdirect.common.pricing.util;

import org.apache.log4j.Logger;

import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.FDStoreProperties;

public class DealsHelper {
	private static final Logger LOG = Logger.getLogger(DealsHelper.class);
	
	public static boolean hasWasPrice(String sku, double basePrice, double sellingPrice, String baseUnit, String sellingUnit) {
		if (
			!isValidInput(sku,4) || !isValidInput(baseUnit) || !isValidInput(sellingUnit) ||
			!isValidInput(basePrice) || !isValidInput(sellingPrice) ||
			!baseUnit.equals(sellingUnit) || sellingPrice>=basePrice  
		) {
			return false;
		}


		int p = getVariancePercentage(basePrice,sellingPrice);
		return ( (FDStoreProperties.getDealsLowerLimit()<=p) && (FDStoreProperties.getDealsUpperLimit()>=p) );
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
	public static int getVariancePercentage(double basePrice, double sellingPrice) {
		
		if(!isValidInput(basePrice)||!isValidInput(sellingPrice)||sellingPrice>=basePrice)
			return -1;
		int val = (int) ((basePrice - sellingPrice) * 100.0 / basePrice + 0.2);
		if( ((val%5)==0)||((val%2)==0))
			return val;
		return val-1;
	}
	
	public static double determineBasePrice(double basePrice, double sellingPrice) {
		
		return basePrice >= sellingPrice ? basePrice : sellingPrice;
	}

	private static boolean isValidInput(String input,int length) {
		return isValidInput(input) && input.length()>=length;
	}

	private static boolean isValidInput(String input) {
		return !(input==null ||"".equals(input.trim()));
	}
	
	private static boolean isValidInput(double input) {
		return input > 0.0;
	}

	
	
	
	public static void main(String[] args) {
		
		System.out.println(DealsHelper.hasWasPrice(null, 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice(" ", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GR", 0.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 0.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice(null, 5.0, 6.0, "EA", "PB"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("NNN000110", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice(null, 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 100.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 100.0, 25.1, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 100.0, 24.99, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 100.0, 24.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 100.0, 89.99, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 10.0, 9.0, "EA", "EA"));
		System.out.println(DealsHelper.hasWasPrice("GRO000110", 10.0, 25.0, "EA", "EA"));
		
		/*System.out.println(DealsHelper.getVariancePercentage(6.0, 5.0));
		System.out.println(DealsHelper.getVariancePercentage(7.0, 5.0));
		System.out.println(DealsHelper.getVariancePercentage(8.0, 5.0));
		System.out.println(DealsHelper.getVariancePercentage(6.5, 5.0));
		System.out.println(DealsHelper.getVariancePercentage(6.7, 5.0));
		System.out.println(DealsHelper.getVariancePercentage(10.0, 5.0));
		System.out.println(DealsHelper.getVariancePercentage(10.0, 9.99));
		System.out.println(DealsHelper.getVariancePercentage(0.0, 9.99));
		System.out.println(DealsHelper.getVariancePercentage(9.0, 0.0));
		System.out.println(DealsHelper.getVariancePercentage(0.0, 0.0));
		System.out.println(DealsHelper.getVariancePercentage(0.01, 100.0));
		System.out.println(DealsHelper.getVariancePercentage(100.01, 0.001));
		*/
	}


	public static int determineTieredDeal(double basePrice, String basePriceUnit,
			double defaultPrice, String defaultPriceUnit, ErpMaterialPrice[] materialPrices) {
		double base = basePrice >= defaultPrice ? basePrice : defaultPrice;
		String baseUnit = basePrice >= defaultPrice && basePriceUnit != null ? basePriceUnit: defaultPriceUnit;
		int highest = 0;
		for (int i = 0; i < materialPrices.length; i++) {
			if (!baseUnit.equals(materialPrices[i].getUnit())) {
				LOG.warn("OOPS! material price with price unit different from the base price unit! Check DB!!!");
				continue;
			}
			int deal = getVariancePercentage(base, materialPrices[i].getPrice());
			if (deal > highest)
				highest = deal;
		}
		return highest > 0 ? highest : -1;
	}
	
	public static boolean isDealOutOfBounds(int price) {
		return price < FDStoreProperties.getDealsLowerLimit()
				|| price > FDStoreProperties.getDealsUpperLimit();
	}
}
