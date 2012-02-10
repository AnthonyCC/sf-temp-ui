package com.freshdirect.transadmin.web.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.Neighbourhood;
import com.freshdirect.transadmin.model.NeighbourhoodZipcode;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.SpatialBoundary;
import com.freshdirect.transadmin.web.util.ZoneWorkTableUtil;

public class GeographyProviderController extends JsonRpcController  implements IGeographyProvider {
	
	private RestrictionManagerI restrictionManagerService;
	
	private DomainManagerI domainManagerService;
	
	private ZoneManagerI zoneManagerService;
	
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
	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
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
		        } else if(_tmpCode.startsWith("NH_")) {
		        	Map<String, NeighbourhoodZipcode> zipInfo = zoneManagerService.getNeighbourhoodZipCodeInfo(_tmpCode.substring(3, _tmpCode.length()));
		        	if(zipInfo != null && zipInfo.size() > 0){
		        		boundary = getRestrictionManagerService().getNeighbourhoodBoundary(_tmpCode.substring(3, _tmpCode.length()));
		        	}
		        } else{
		        	boundary = getRestrictionManagerService().getZoneBoundary(_tmpCode);
		        	boundary.setZone(true);
		        }
		        if(boundary != null) 
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
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			domainManagerService.updateDisassociatedTimeslots();
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
		try{
			groupsList = (List<DeliveryGroup>) domainManagerService.getDeliveryGroups();
		}catch(Exception ex){
			ex.printStackTrace();			
		}
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
	
	@SuppressWarnings("unchecked")
	public List<Neighbourhood> getNeighbourhood(){
		List<Neighbourhood> result = new ArrayList<Neighbourhood>();
		Collection neighbourhoodDataLst = domainManagerService.getNeighbourhood();
		for (Object o : neighbourhoodDataLst){
			result.add((Neighbourhood) o);
		}		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public boolean addNeighbourhood(String[][] _neighbourhoodData){
		
		Set<Neighbourhood> newNeighbourhoods = new HashSet<Neighbourhood>();	
		Map<String, Neighbourhood> previousNeighbourhoods = new HashMap<String, Neighbourhood>();
		
		Collection neighbourhoodDataLst = domainManagerService.getNeighbourhood();
		for (Object o : neighbourhoodDataLst){
			Neighbourhood _nHood = (Neighbourhood) o;
			previousNeighbourhoods.put(_nHood.getName(), _nHood);
		}
		try{
			for(int i=0;i < _neighbourhoodData.length;i++) {
				Neighbourhood _neighbourhoodModel = new Neighbourhood(_neighbourhoodData[i][0], _neighbourhoodData[i][1], _neighbourhoodData[i][2]);
				if(!previousNeighbourhoods.containsKey(_neighbourhoodModel.getName()))
					newNeighbourhoods.add(_neighbourhoodModel);
				else
					previousNeighbourhoods.remove(_neighbourhoodModel.getName());
					newNeighbourhoods.add(_neighbourhoodModel);
			}
			if(previousNeighbourhoods != null && previousNeighbourhoods.size() > 0)
				domainManagerService.removeEntity(previousNeighbourhoods.values());
			if(newNeighbourhoods.size() > 0) {				
				domainManagerService.saveEntityList(newNeighbourhoods);
			}
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

 }
