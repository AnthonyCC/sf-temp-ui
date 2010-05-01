package com.freshdirect.transadmin.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class ZoneWorkTableUtil {
	
	private static Map workTableMap = new HashMap();
	public static Map getActiveWorkTableAndRegionIds(){
		
//		Map refData = new HashMap();
		String expansionData=TransportationAdminProperties.getZoneExpansionTables();
		String[] worktableData=expansionData.split("\\,");
		for (int i = 0; i < worktableData.length; i++) {
			String workTable=worktableData[i].trim();
			String[] regionData = workTable.split("\\=");
			if(null != regionData && regionData.length>=2){
				workTableMap.put(regionData[0].trim(), regionData[1]);
			}
		}
		return workTableMap;
	}
	
	public static String getRegionId(String workTableName){
		if(null != workTableName)
			return (String)workTableMap.get(workTableName);
		else
			return null;
	}
	
}
