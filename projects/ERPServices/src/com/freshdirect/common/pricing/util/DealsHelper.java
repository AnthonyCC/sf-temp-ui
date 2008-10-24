package com.freshdirect.common.pricing.util;

import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDStoreProperties;

public class DealsHelper {
	
	
	public static boolean isDeal(ErpProductInfoModel product ) {
		
		return isDeal(product.getSkuCode(),product.getBasePrice(),product.getDefaultPrice(),product.getBasePriceUnit(),product.getDefaultPriceUnit());
	}
	
	public static boolean isDeal(String sku, double basePrice, double sellingPrice, String baseUnit, String sellingUnit) {
		
		if( !isValidInput(sku,4)||!isValidInput(baseUnit)||!isValidInput(sellingUnit)||
		    !isValidInput(basePrice)||!isValidInput(sellingPrice)||
		    !baseUnit.equals(sellingUnit) || sellingPrice>=basePrice  
		  )
			return false;
		int percentage=0;
		if(isEligibleSku(sku)) {
			percentage=getVariancePercentage(basePrice,sellingPrice);
			if((FDStoreProperties.getDealsLowerLimit()<=percentage)&& (FDStoreProperties.getDealsUpperLimit()>=percentage))
				return true;	
		}
		return false;
	}
	
	
	/**
	 * Returns a -ve integer if either the basePrice or sellingPrice is zero or 
	 * if the sellingPrice is greater than or equal to basePrice.
	 * The formula used is (basePrice – sellingPrice) ÷basePrice x 100
	 * The percentage is rounded down to the nearest integer divisble by 5 or 2.
	 * Ex: 27.XX becomes 26, 25.XX becomes 25 etc.
	 * 
	 * @param basePrice
	 * @param sellingPrice
	 * @return
	 */
	public static int getVariancePercentage(double basePrice, double sellingPrice) {
		
		if(!isValidInput(basePrice)||!isValidInput(sellingPrice)||sellingPrice>=basePrice)
			return -1;
		String value=String.valueOf(((basePrice-sellingPrice)/basePrice)*100);
		int val=Integer.parseInt(value.substring(0,value.indexOf(".")));
		if( ((val%5)==0)||((val%2)==0))
			return val;
		return val-1;
	}
	
	private static boolean isValidInput(String input,int length) {
		
		if(isValidInput(input)&&input.length()>=length) 
			return true;
		return false;
	}
	private static boolean isValidInput(String input) {
		
		if(input==null ||"".equals(input.trim()))
			return false;
		return true;
	}
	
	private static boolean isValidInput(double input) {
		
		if(input<=0.0)
			return false;
		return true;
	}
	private static boolean isEligibleSku(String sku) {
		
		String skuPrefixes=FDStoreProperties.getDealsSkuPrefixes();
		if ("".equals(skuPrefixes.trim()))
			return false;
		if("ALL".equalsIgnoreCase(skuPrefixes))
			return true;
		if(skuPrefixes.toUpperCase().indexOf(sku.substring(0, 3).toUpperCase())!=-1) 
			return true;
		
		return false;
	}
	
	
	
	public static void main(String[] args) {
		
		System.out.println(DealsHelper.isDeal(null, 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal(" ", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GR", 0.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 0.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal(null, 5.0, 6.0, "EA", "PB"));
		System.out.println(DealsHelper.isDeal("GRO000110", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("NNN000110", 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal(null, 5.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 100.0, 6.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 100.0, 25.1, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 100.0, 24.99, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 100.0, 24.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 100.0, 89.99, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 10.0, 9.0, "EA", "EA"));
		System.out.println(DealsHelper.isDeal("GRO000110", 10.0, 25.0, "EA", "EA"));
		
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

}


