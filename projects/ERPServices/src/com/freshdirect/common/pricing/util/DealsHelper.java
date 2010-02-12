package com.freshdirect.common.pricing.util;

import org.apache.log4j.Logger;

import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.FDStoreProperties;

public class DealsHelper {
	private static final Logger LOG = Logger.getLogger(DealsHelper.class);
	

	public static boolean isItemOnSale(double sellingPrice, double promoPrice) {
		if (!isValidInput(promoPrice) || !isValidInput(sellingPrice) ||  sellingPrice<=promoPrice) {
			return false;
		}

		int p = getVariancePercentage(sellingPrice,promoPrice);
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
	public static int getVariancePercentage(double sellingPrice, double promoPrice) {
		
		if(!isValidInput(promoPrice)||!isValidInput(sellingPrice)||sellingPrice<=promoPrice)
			return -1;
		int val = (int) ((sellingPrice - promoPrice) * 100.0 / sellingPrice + 0.2);
		if( ((val%5)==0)||((val%2)==0))
			return val;
		return val-1;
	}
	
	public static double determineBasePrice(double sellingPrice, double promoPrice) {
		
		return sellingPrice >= promoPrice ? sellingPrice : promoPrice;
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
		
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(0.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(0.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(5.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(100.0, 6.0));
		System.out.println(DealsHelper.isItemOnSale(100.0, 25.1));
		System.out.println(DealsHelper.isItemOnSale(100.0, 24.99));
		System.out.println(DealsHelper.isItemOnSale(100.0, 24.0));
		System.out.println(DealsHelper.isItemOnSale(100.0, 89.99));
		System.out.println(DealsHelper.isItemOnSale(10.0, 9.0));
		System.out.println(DealsHelper.isItemOnSale(10.0, 25.0));
		
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


	public static int determineTieredDeal(double sellingPrice, ErpMaterialPrice[] materialPrices) {
		int highest = 0;
		for (int i = 0; i < materialPrices.length; i++) {
			int deal = getVariancePercentage(sellingPrice, materialPrices[i].getPrice());
			if (deal > highest)
				highest = deal;
		}
		return Math.max(highest, 0);
	}
	
	public static boolean isDealOutOfBounds(int price) {
		return price < FDStoreProperties.getDealsLowerLimit()
				|| price > FDStoreProperties.getDealsUpperLimit();
	}
}
