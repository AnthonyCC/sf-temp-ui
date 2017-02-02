package com.freshdirect.webapp.taglib.productpromotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ejb.ProductPromotionInfoManager;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.logistics.delivery.model.DlvZoneModel;

public class ErpProductPromotionUtil {

	public static final String DEFAULT_ZONE = "0000100000";
	public static final ZoneInfo DEFAULT_ZONE_INFO = new ZoneInfo("0000100000","0001","01");
	public static List<ErpZoneMasterInfo> zoneList =  getAvailablePricingZones();
	/*public static Object getAvailableDepartments(){		
		try {
			return ProductPromotionManager.getAllDepartments();
		} catch (FDResourceException e) {
			return Collections.EMPTY_LIST;
		}		
	}*/
	
	public static Object getAvailableCmsDepartments() {		
		StoreModel store = ContentFactory.getInstance().getStore();
		CmsManager          manager     = CmsManager.getInstance();
		List departments = new ArrayList();
        if(store != null) {
     	   List<DepartmentModel> storeDepartments = store.getDepartments();
	       ContentKey contentKey = ContentKey.getContentKey(FDContentTypes.DEPARTMENT,"wgd");
	       if(storeDepartments != null) {
	      	   for(DepartmentModel storeDepartment : storeDepartments) {
	      		   if(storeDepartment.getContentKey() != null		        				   
	      				   && !storeDepartment.isHidden()
	      				   && !storeDepartment.isHideIphone()&& !storeDepartment.getContentKey().equals(contentKey)) {
	      			   ContentNodeI contentNode = manager.getContentNode(storeDepartment.getContentKey());
	      			   departments.add(contentNode);
	      		   }
	      	   }
	       }
        }
		/*CmsManager          manager     = CmsManager.getInstance();		
		Set<ContentKey> contentKeys =manager.getContentKeysByType(FDContentTypes.DEPARTMENT);
		Map<ContentKey, ContentNodeI> contentNodes =manager.getContentNodes(contentKeys);
		//return ProductPromotionManager.getAllDepartments();
		ContentKey contentKey = ContentKey.getContentKey(FDContentTypes.DEPARTMENT,"wgd");
		contentNodes.remove(contentKey);//Excluding 'what's good' department.
		contentKey = ContentKey.getContentKey(FDContentTypes.DEPARTMENT,"our_picks");
		contentNodes.remove(contentKey);*/
        Collections.sort(departments,new DepartmentIdComparator());
		return departments;
		
	}
	
	public static List<DlvZoneModel> getAvailableDeliveryZones(){
		try {
			return FDDeliveryManager.getInstance().getActiveZones();
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}
	}
	
	public static List<ErpZoneMasterInfo> getAvailablePricingZones(){
		try {
			List<ErpZoneMasterInfo> list =ProductPromotionInfoManager.getAllZoneInfoDetails();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				ErpZoneMasterInfo erpZoneMasterInfo = (ErpZoneMasterInfo) iterator.next();
				if(erpZoneMasterInfo.getSapId().equals("0000100001")||erpZoneMasterInfo.getSapId().equals("0000100002")){
					iterator.remove();//Removing the not required 'Corporate Default' and 'Residential Default' zones from the list.
				}				
			}
			return list;
		} catch (FDResourceException e) {
			return Collections.EMPTY_LIST;
		}	
	}
	
	private static class DepartmentIdComparator implements Comparator<ContentNodeI> {
		@Override
		public int compare(ContentNodeI str1, ContentNodeI str2) {
			return str1.getKey().getId().toLowerCase().compareTo(str2.getKey().getId().toLowerCase());
		}
	}
	
	public static String getZoneNameByZoneCode(String zoneCode){
		if(null !=zoneList){
			for (Iterator iterator = zoneList.iterator(); iterator.hasNext();) {
				ErpZoneMasterInfo zoneInfo = (ErpZoneMasterInfo) iterator.next();
				if(zoneInfo.getSapId().equals(zoneCode)){
					return zoneInfo.getDescription();
				}				
			}
		}
		return "";
	}
	
	public static String getDepartmentNameById(String deptId){
		CmsManager          manager     = CmsManager.getInstance();	
		ContentKey contentKey = ContentKey.getContentKey(FDContentTypes.DEPARTMENT,deptId);
		ContentNodeI contentNode = manager.getContentNode(contentKey);
		if(null != contentNode){
			return (String)contentNode.getAttribute("FULL_NAME").getValue();
		}
		return "";
	}
}
