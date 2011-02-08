package com.freshdirect.transadmin.web.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.DlvScenarioZones;
import com.freshdirect.transadmin.model.ScenarioZonesId;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.ScenarioZoneCommand;
import com.freshdirect.transadmin.web.model.SpatialBoundary;
import com.freshdirect.transadmin.web.util.ZoneWorkTableUtil;

public class GeographyProviderController extends JsonRpcController  implements IGeographyProvider {
	
	private RestrictionManagerI restrictionManagerService;
	
	private DomainManagerI domainManagerService;
	
	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}
	
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	

	public SpatialBoundary getGeoRestrictionBoundary(String code) {
		// TODO Auto-generated method stub
		return getRestrictionManagerService().getGeoRestrictionBoundary(code);
	}
	
	public SpatialBoundary getZoneBoundary(String code) {
		// TODO Auto-generated method stub
		return getRestrictionManagerService().getZoneBoundary(code);
	}
	
	public List getBoundaries(String code) {
		List result = new ArrayList<SpatialBoundary>();
		SpatialBoundary boundary = null;
		if (code != null && code.trim().length() > 0) {
			String[] splitCodes = code.split(",");	
			for (String _tmpCode : splitCodes) {
		        if(_tmpCode.startsWith("$_")) {
		        	boundary = getRestrictionManagerService().getGeoRestrictionBoundary(_tmpCode.substring(2, _tmpCode.length()));		        	
		        } else{
		        	boundary = getRestrictionManagerService().getZoneBoundary(_tmpCode);
		        	boundary.setZone(true);
		        }
		        result.add(boundary);
			}

		}
		return result;
	}
	
	public boolean doZoneExpansion(String worktable, String zone[][], String deliveryFee, String expansionType) {
		
		String regionId=ZoneWorkTableUtil.getRegionId(worktable);
		try{
			if(regionId!=null && !"".equals(regionId)){
				domainManagerService.doZoneExpansion(worktable, zone, regionId, deliveryFee, expansionType);
			}else{
				return false;
			}
			//saveMessage(request, getMessage("app.actionmessage.153", new Object[]{}));
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean generateTimeslots(String zone[][],String worktable) {
		String regionId = ZoneWorkTableUtil.getRegionId(worktable);
		try{
			domainManagerService.rollbackTimeslots(zone);
			
			boolean temp=false;
			List query_list = domainManagerService.getStartDateForRegion(regionId);
			if(query_list!=null && !query_list.isEmpty()){ 
				temp=true;
			}
			if(temp){
				domainManagerService.updateStartDate(regionId);
				domainManagerService.makeDevLive(regionId);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean doGeoRestriction(String zone[][]) {
		
		try{
			domainManagerService.doGeoRestriction(zone);
			//saveMessage(request, getMessage("app.actionmessage.153", new Object[]{}));
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryGroup> getDeliveryGroups(){
		
		List<DeliveryGroup> groupsList = new ArrayList<DeliveryGroup>();
		groupsList = (List<DeliveryGroup>) domainManagerService.getDeliveryGroups();
					
		return groupsList;	
	}
	
	public boolean doDeliveryGroup(String[][] zone) {

		List<DeliveryGroup> newGroupList = new ArrayList<DeliveryGroup>();
		List<DeliveryGroup> removeGroupList = new ArrayList<DeliveryGroup>();
		List<DeliveryGroup> currentGroupList = new ArrayList<DeliveryGroup>();
		currentGroupList = this.getDeliveryGroups();
		try {
			for (int i = 0; i < zone.length; i++) {

				DeliveryGroup deliveryGroup = new DeliveryGroup();
				deliveryGroup
						.setGroupName(TransStringUtil.isEmpty(zone[i][0]) ? ""
								: zone[i][0]);
				boolean isMatching = false;
				for (Iterator<DeliveryGroup> itr = currentGroupList.iterator(); itr
						.hasNext();) {
					DeliveryGroup _deliveryGroup = itr.next();
					if (_deliveryGroup.getGroupName().equals(
							deliveryGroup.getGroupName())) {
						isMatching = true;
						break;
					}
				}
				if (!isMatching)// new Group list
					newGroupList.add(deliveryGroup);

			}

			for (Iterator<DeliveryGroup> itr = currentGroupList.iterator(); itr
					.hasNext();) {
				DeliveryGroup deliveryGroup = itr.next();
				boolean isMatching = false;
				for (int i = 0; i < zone.length; i++) {
					DeliveryGroup _deliveryGroup = new DeliveryGroup();
					_deliveryGroup.setGroupName(TransStringUtil
							.isEmpty(zone[i][0]) ? "" : zone[i][0]);

					if (_deliveryGroup.getGroupName().equals(
							deliveryGroup.getGroupName())) {
						isMatching = true;
						break;
					}
				}
				if (!isMatching)// remove Group list
					removeGroupList.add(deliveryGroup);
			}
			if (removeGroupList.size() > 0) {
				domainManagerService.removeEntity(removeGroupList);
			}
			if (newGroupList.size() > 0) {
				domainManagerService.saveEntityList(newGroupList);
			}
		} catch (Exception exp) {
			return false;
		}
		return true;
	}

 }
