package com.freshdirect.fdstore.util;

/**
 * 
 * @author ksriram
 *
 */
public class UnitPriceUtil {

	/**
	 * Method to format the unitPriceUOM from SAP, for display.
	 * @param unitPriceUOM
	 * @return
	 */
	public static String getFormattedUnitPriceUOM(String unitPriceUOM) {
		String formattedUnitPriceUOM = unitPriceUOM;
		if(null != unitPriceUOM && !"".equalsIgnoreCase(unitPriceUOM)){
			if("CT".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "ctn";
			}else if("CN".equalsIgnoreCase(unitPriceUOM)){
				formattedUnitPriceUOM = "ct";
			}
		}
		return formattedUnitPriceUOM;
	}

}
