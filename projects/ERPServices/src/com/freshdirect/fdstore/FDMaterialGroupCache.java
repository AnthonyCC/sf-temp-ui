package com.freshdirect.fdstore;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author ksriram
 *
 */
public class FDMaterialGroupCache {

	private static Category LOGGER = LoggerFactory.getInstance( FDMaterialGroupCache.class );
	private static Date lastExecuted = null;
	public static boolean warmUpComplete = false;
	private static Map<String, Map<String,FDGroup>> materialGroupsCache = new ConcurrentHashMap<String,Map<String,FDGroup>>();
	private static Map<String, List<String>> grpMaterialsLastModified = new HashMap<String, List<String>>();
	
	private static ExpiringReference<Map<String, List<String>>> grpMaterials = new ExpiringReference<Map<String, List<String>>>(FDStoreProperties.getRefreshSecsGroupScaleInfo() * 1000) {
		@Override
		protected Map<String, List<String>> load() {
			try {
				LOGGER.info("REFRESHING GROUP MAP FOR ANY CHANGES FROM LAST EXECUTED TIME: "+lastExecuted);
				Map<String, List<String>> latestGroups = new HashMap<String, List<String>>();
				Date currDate = new Date();					
				latestGroups = FDFactory.getModifiedOnlyGroups(lastExecuted);
				populateMaterialGroupsCache(grpMaterialsLastModified, latestGroups);
				lastExecuted = currDate;				
				grpMaterialsLastModified.putAll(latestGroups);
				LOGGER.info("REFRESHED GROUP MAP FOR ANY CHANGES: "+(null !=latestGroups?latestGroups.size():"0")+" groups changed");
				return latestGroups;
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}

		
	};
	
	private static void populateMaterialGroupsCache(Map<String, List<String>> grpMaterialsLastModified, Map<String, List<String>> latestGroups) {
		if(null !=latestGroups && !latestGroups.isEmpty()){
			for (Iterator<String> iterator = latestGroups.keySet().iterator(); iterator.hasNext();) {
				String grpId = iterator.next();
				List<String> materials = (null != grpMaterialsLastModified.get(grpId) && !grpMaterialsLastModified.get(grpId).isEmpty()) ?grpMaterialsLastModified.get(grpId) :latestGroups.get(grpId);
				populateMaterialGroupsCache(materials);
			}
		}
	}
	
	
	private static void populateMaterialGroupsCache(List<String> materials){
		if(null !=materials){
			for (Iterator<String> iterator = materials.iterator(); iterator.hasNext();) {
				String material = (String) iterator.next();
				try {
					LOGGER.info("Fetching groups for material: "+material);
					Map<String,FDGroup> groupsForMaterial = FDFactory.getGroupIdentityForMaterial(material);
					LOGGER.info("Fetched groups for material: "+material);
					materialGroupsCache.remove(material);
					if(null !=groupsForMaterial && !groupsForMaterial.isEmpty()){
						materialGroupsCache.put(material,groupsForMaterial);
					}
				} catch (FDResourceException e) {
					LOGGER.info("Excetpion  while fetching groups for the material: "+material,e);
				}
				
			}
		}
	}
	
	public static void loadMaterialGroups(){
		grpMaterials.get();//to get the cache refreshed if expired.
	}
	
	public static Map<String,FDGroup> getGroupsByMaterial(String material) {
		loadMaterialGroups();
		if(materialGroupsCache.containsKey(material)){
			return materialGroupsCache.get(material);
		}
		return null;
	}
}
