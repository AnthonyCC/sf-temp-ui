package com.freshdirect.fdstore;

import java.util.List;
import java.util.Set;

import com.freshdirect.customer.ErpZoneMasterInfo;

public class GroupScalePricing extends FDGroup {
		
	private String longDesc;
	private String shortDesc;
	private boolean isActive;
	private GrpZonePriceListing grpZonePriceList;
	private Set<String> matList;
	private List<String> skuList;
	
	public Set<String> getMatList() {
		return matList;
	}

	public  GroupScalePricing(String grpId,int version, String longDesc,String shortDesc,boolean isActive, GrpZonePriceListing grpZonePriceListing, Set<String> matList, List<String> skuList){		
		super(grpId, version);
		this.longDesc=longDesc;
		this.shortDesc=shortDesc;
		this.isActive=isActive;
		this.grpZonePriceList = grpZonePriceListing;
		this.matList = matList;
		this.skuList =  skuList;
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
	
	public GrpZonePriceListing getGrpZonePriceList() {
		return grpZonePriceList;
	}
	
	public GrpZonePriceModel getGrpZonePrice(String pZoneId) {
		try {
			GrpZonePriceModel grpZpModel = this.grpZonePriceList.getGrpZonePrice(pZoneId);
			if(grpZpModel == null) {
				//do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(pZoneId);
				if(zoneInfo != null && zoneInfo.getParentZone() != null){
					//This check has been added to make sure the following line does not
					//throw NPE when master default zone does not have a defined GS price.
					grpZpModel = getGrpZonePrice(zoneInfo.getParentZone().getSapId());
				}
			}
			return grpZpModel;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the Group Zone Price Model");
		}
	}

	public List<String> getSkuList() {
		return skuList;
	}
	
}
