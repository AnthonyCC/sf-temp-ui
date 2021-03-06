package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ModelSupport;

public class ErpGrpPriceModel extends ModelSupport {
		
	private int version;
	private String grpId;
	private String longDesc;
	private String shortDesc;
	private boolean isActive;
	private Set<ErpGrpPriceZoneModel> zoneModelList;
	private Set<String> matList;
	private List<String> skuList;
	
	public Set<String> getMatList() {
		return matList;
	}

	public void setMatList(Set<String> matList) {
		this.matList = matList;
	}
	
	public Set<ErpGrpPriceZoneModel> getZoneModelList() {
		return zoneModelList;
	}


	public void setZoneModelList(Set<ErpGrpPriceZoneModel> zoneModelList) {
		this.zoneModelList = zoneModelList;
	}

	public ErpGrpPriceModel()
	{
		
	}
	public  ErpGrpPriceModel(String grpId,String longDesc,String shortDesc,boolean isActive){		
		this.grpId=grpId;
		this.longDesc=longDesc;
		this.shortDesc=shortDesc;
		this.isActive=isActive;
	}

	
	public void setVersion(int version){
		this.version=version;
	}
	
	public int getVersion() {
		return version;
	}

	public String getGrpId() {
		return grpId;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
	public Collection<ErpGrpPriceZoneModel> getGrpZoneModel(ZoneInfo zone){
		
    	List<ErpGrpPriceZoneModel> zoneList=new ArrayList<ErpGrpPriceZoneModel>();
    		if(this.zoneModelList!=null && this.zoneModelList.size()>0){
    			Iterator<ErpGrpPriceZoneModel> iterator=this.zoneModelList.iterator();
    			while(iterator.hasNext()){
    				ErpGrpPriceZoneModel mat=(ErpGrpPriceZoneModel)iterator.next();
    				if(mat.getZone().equals(zone)){    	
    					zoneList.add(mat);
    					//break;
    				}    				    				
    			}
    		}	
    	return 	zoneList;	
		
	}
	

    public Collection<ErpGrpPriceZoneModel> getGrpPriceModel(ZoneInfo zone){
    			
    	Collection<ErpGrpPriceZoneModel> grpZoneModelList;
    	try{
    		grpZoneModelList=getGrpZoneModel(zone);
			
			if(grpZoneModelList == null || grpZoneModelList.size()==0) {
				//do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(zone.getPricingZoneId());
				grpZoneModelList = getGrpPriceModel(new ZoneInfo(zoneInfo.getParentZone().getSapId(),zone.getSalesOrg(),zone.getDistributionChanel()));
			}
			return grpZoneModelList;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the grp Price Model");
		}	
    }
	
    public List<ErpGrpPriceZoneModel> getOrderedZoneModelList(){
    	List<ErpGrpPriceZoneModel> orderedList = new ArrayList<ErpGrpPriceZoneModel>(this.zoneModelList);
		// sort by zone id ascending
		Collections.sort(orderedList, new Comparator<ErpGrpPriceZoneModel>() {
			public int compare(ErpGrpPriceZoneModel z1, ErpGrpPriceZoneModel z2) {
				return z1.getZone().compareTo(z2.getZone());
			}
		} );
		return orderedList;
    }
    
	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}
	
}
