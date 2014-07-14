/*
 * WineUtils.java
 * 
 * Created on 2013.09.26_12.10.50.PM
 */
package com.freshdirect;

import com.freshdirect.fdstore.FDStoreProperties;

/**
 * 
 * @author batchley
 * @versions
 */
public class WineUtil {
	
	private static final String USQ_LONG_TEXT = "Union Square Wines & Spirits";
	private static final String USQ_ASS_TEXT = "UNION SQUARE WINE";
	private static final String FDW_LONG_TEXT = "FreshDirect Wines & Spirits"; //pretty text
	private static final String FDW_ASS_TEXT = "Fresh Direct Wines"; //"charge line" look-alike 
	
	/* util call for "USQ" value */
	public static String getWineAssociateId() {
		//TODO logic here for where this should be stored (like a prop)
		//return "USQ";
		//return "VIN";
		return FDStoreProperties.getWineAssid();
	}
	
	/* get correct long text based on prop */
	public static String getWineLongText() {
		if ("USQ".equalsIgnoreCase(getWineAssociateId())) {
			return getUsqLongText();
		} else if ("FDW".equalsIgnoreCase(getWineAssociateId())) {
			return getFdwLongText();
		}
		return "";
	}
	
	/* get correct associate text based on prop */
	public static String getWineAssText() {
		if ("USQ".equalsIgnoreCase(getWineAssociateId())) {
			return getUsqAssText();
		} else if ("FDW".equalsIgnoreCase(getWineAssociateId())) {
			return getFdwAssText();
		}
		return "";
	}
	
	public static String getUsqLongText() {
		return USQ_LONG_TEXT;
	}

	public static String getUsqAssText() {
		return USQ_ASS_TEXT;
	}

	public static String getFdwLongText() {
		return FDW_LONG_TEXT;
	}

	public static String getFdwAssText() {
		return FDW_ASS_TEXT;
	}

	public static String getWineLongTextByAssId(String assId) {
		if ("USQ".equalsIgnoreCase(assId)) {
			return getUsqAssText();
		} else if ("FDW".equalsIgnoreCase(assId)) {
			return getFdwAssText();
		}
		return "";
	}

	public static String getWineAssTextByAssId(String assId) {
		if ("USQ".equalsIgnoreCase(assId)) {
			return getUsqLongText();
		} else if ("FDW".equalsIgnoreCase(assId)) {
			return getFdwLongText();
		}
		return "";
	}

}
