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

	/* util call for "USQ" value */
	public static String getWineAssociateId() {
		//TODO logic here for where this should be stored (like a prop)
		//return "USQ";
		//return "VIN";
		return FDStoreProperties.getWineAssid();
	}
}
