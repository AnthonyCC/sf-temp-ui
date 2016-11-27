package com.freshdirect.transadmin.service.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.EquipmentType;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.transadmin.dao.AssetManagerDaoI;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetActivity;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetType;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.util.TransAdminCacheManager;

public class AssetManagerImpl  
		extends BaseManagerImpl implements AssetManagerI {
	
	private final static Category LOGGER = LoggerFactory.getInstance(AssetManagerImpl.class);
	
	private AssetManagerDaoI assetManagerDao = null;
	
	public AssetManagerDaoI getAssetManagerDao() {
		return assetManagerDao;
	}

	public void setAssetManagerDao(AssetManagerDaoI assetManagerDao) {
		this.assetManagerDao = assetManagerDao;
	}

	protected BaseManagerDaoI getBaseManageDao() {
		return getAssetManagerDao();
	}

	public Collection getAssetTypes() {

		return getAssetManagerDao().getAssetTypes();
	}

	public Collection getAssetAttributeTypes() {

		return getAssetManagerDao().getAssetAttributeTypes();
	}
	
	public Collection getAssets(String assetType, String atrName, String atrValue) {
		Collection assets = getAssetManagerDao().getAssets(assetType, atrName, atrValue);
		if(assets != null && assets.size() > 0) {
			Collections.sort((List) assets, new AlphaNumericComparator());
		}
		return assets;
	}
	
	public Collection getActiveAssets(String assetType) {

		return getAssetManagerDao().getActiveAssets(assetType);
	}
	
	public Asset getAsset(String assetId) {

		return getAssetManagerDao().getAsset(assetId);
	}
	
	public void saveAsset(Asset asset)  {
		getAssetManagerDao().saveAsset(asset);
	}

	public AssetTemplate getAssetTemplate(String assetTemplateId){
		return getAssetManagerDao().getAssetTemplate(assetTemplateId);
	}

	public void saveAssetTemplate(AssetTemplate assetTemplate) {
		getAssetManagerDao().saveAssetTemplate(assetTemplate);
	}

	public Collection getAssetTemplates(String assetType){
		return getAssetManagerDao().getAssetTemplates(assetType);
	}

	public AssetType getAssetType(String assetType){
		return getAssetManagerDao().getAssetType(assetType);
	}

	public Collection getAssetAttributeTypes(String attributeCode, String assetType){
		return getAssetManagerDao().getAssetAttributeTypes(attributeCode,assetType);
	}
	
	public Collection getAssetAttributeTypes(String assetType){
		return getAssetManagerDao().getAssetAttributeTypes(assetType);
	}
	
	public Asset getAssetByAssetNumber(String assetNo){
		return getAssetManagerDao().getAssetByAssetNumber(assetNo);
	}
	
	public Collection getAsset(String assetNo, String assetType) {
		return getAssetManagerDao().getAsset(assetNo, assetType);
	}
	
	
	public class AlphaNumericComparator implements Comparator {
		/**
		  * The compare method that compares the alphanumeric strings
		  */
		public int compare(Object Obj1, Object Obj2) {
			Asset a1 = (Asset) Obj1;
			Asset a2 = (Asset) Obj2;
			String objStr1 = a1.getAssetNo();
			String objStr2 = a2.getAssetNo();

			if (objStr2 == null || objStr1 == null) {
				return 0;
			}

			int objStrlength1 = objStr1.length();
			int objStrlength2 = objStr2.length();

			int index1 = 0;
			int index2 = 0;

			while (index1 < objStrlength1 && index2 < objStrlength2) {
				char ch1 = objStr1.charAt(index1);
				char ch2 = objStr2.charAt(index2);

				char[] space1 = new char[objStrlength1];
				char[] space2 = new char[objStrlength2];

				int loc1 = 0;
				int loc2 = 0;

				do {
					space1[loc1++] = ch1;
					index1++;

					if (index1 < objStrlength1) {
						ch1 = objStr1.charAt(index1);
					} else {
						break;
					}
				} while (Character.isDigit(ch1) == Character.isDigit(space1[0]));

				do {
					space2[loc2++] = ch2;
					index2++;

					if (index2 < objStrlength2) {
						ch2 = objStr2.charAt(index2);
					} else {
						break;
					}
				} while (Character.isDigit(ch2) == Character.isDigit(space2[0]));

				String str1 = new String(space1);
				String str2 = new String(space2);

				int result;

				if (Character.isDigit(space1[0])
						&& Character.isDigit(space2[0])) {
					Long firstNumberToCompare = new Long(
							Long.parseLong(str1.trim()));
					Long secondNumberToCompare = new Long(
							Long.parseLong(str2.trim()));
					result = firstNumberToCompare
							.compareTo(secondNumberToCompare);
				} else {
					result = str1.compareToIgnoreCase(str2);
				}

				if (result != 0) {
					return result;
				}
			}
			return objStrlength1 - objStrlength2;
	    } 
	}
	
	@Override
	public Map<String, List<EquipmentType>> loadEquipmentTypes() {
		
		RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
		List<IRegionModel> regions = new RoutingInfoServiceProxy().getRegions();
		Map<String, List<EquipmentType>> equipmentTypes = new HashMap<String, List<EquipmentType>>();
		for(IRegionModel region : regions)
		{
			List<EquipmentType> e = proxy.getEquipmentTypes(region.getName());
			equipmentTypes.put(region.getName(), e);
		}
		return equipmentTypes;
	}

	@Override
	public List<EquipmentType> getEquipmentTypes(String region) {
		return  TransAdminCacheManager.getInstance().getEquipmentTypes(this, region);
		
	}
	
	public Asset getAssetByBarcode(String barcode) {
		return getAssetManagerDao().getAssetByBarcode(barcode);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, List<AssetActivity>> getScannedAssets(Date assetScanDate) {
		
		Map<String, List<AssetActivity>> assetMapping = new HashMap<String, List<AssetActivity>>();
		
		Collection assets = getAssetManagerDao().getScannedAssets(assetScanDate);
		
		if(assets != null) {
			Iterator<AssetActivity> assetItr = assets.iterator();
			while(assetItr.hasNext()) {
				AssetActivity asset = assetItr.next();
				if(!assetMapping.containsKey(asset.getEmployeeId())){
					assetMapping.put(asset.getEmployeeId(), new ArrayList<AssetActivity>());
				}
				List<AssetActivity> employeeScannedAssets = assetMapping.get(asset.getEmployeeId());
				boolean foundScannedAsset = false;
				if(employeeScannedAssets != null && employeeScannedAssets.size() > 0) {
					for(Iterator<AssetActivity> itr = employeeScannedAssets.iterator();itr.hasNext();)  
			        { 
						AssetActivity _resAsset = itr.next();
						if(asset.getAssetNo().equals(_resAsset.getAssetNo()) 
								&& asset.getScannedTime().after(_resAsset.getScannedTime())) {							
							foundScannedAsset = true;
							itr.remove();						
							assetMapping.get(asset.getEmployeeId()).add(asset);
						}
			        }
					if(!foundScannedAsset) {
						assetMapping.get(asset.getEmployeeId()).add(asset);
					}
				} else {
					assetMapping.get(asset.getEmployeeId()).add(asset);
				}
			}
		}
		
		return assetMapping;
	}
}
