package com.freshdirect.fdstore.util;

import java.text.DecimalFormat;

import com.freshdirect.fdstore.FDSalesUnit;

/**
 * 
 * @author ksriram
 *
 */
public class UnitPriceUtil {
	
	private static String FORMAT_STR = "0.00";
	
	/**
	 * Method to format the unitPriceUOM from SAP, for display.
	 * @param unitPriceUOM
	 * @return
	 */
	public static String getFormattedUnitPriceUOM(String unitPriceUOM) {
		
		String formattedUnitPriceUOM = unitPriceUOM;
		if(null != unitPriceUOM && !"".equalsIgnoreCase(unitPriceUOM)){
			if("CT".equalsIgnoreCase(unitPriceUOM)){ // CT in SAP is Carton which will be displayed as ctn in website
				formattedUnitPriceUOM = "ctn";
			} else if("CN".equalsIgnoreCase(unitPriceUOM)){ // CN is count in SAP which will be displayed as ct in website
				formattedUnitPriceUOM = "ct";
			} else if("PT".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "pt";
			} else if("L".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "l";
			} else if("OZ".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "oz";
			} else if("QT".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "qt";
			} else if("LB".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "lb";
			} else if("FT2".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "sq ft";
			} else if("YD".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "yd";
			} else if ("SS1".equalsIgnoreCase(unitPriceUOM)){
			    formattedUnitPriceUOM = "Per Serving";
			}
			
		}
		return formattedUnitPriceUOM;
	}
	
	public static String getUnitPrice(FDSalesUnit su, double defaultPrice) {
		
		String unitPrice = null;
		if (su != null) {
			// validate unit price values
			final int n = su.getUnitPriceNumerator();
			final int d = su.getUnitPriceDenominator();
			if (n > 0 && n > 0) {
				final double p = (defaultPrice * n) / d;

				unitPrice = formatDecimalToString(p);
			}
		}
		return unitPrice;
	}
	
	public static String formatDecimalToString(double number) {
		DecimalFormat decimalFormat = new DecimalFormat( FORMAT_STR );
		String strNumber = decimalFormat.format(number);
		strNumber = strNumber.replaceAll(",", ".");

		return strNumber;
	}

}
