package com.freshdirect.fdstore;

import java.util.List;
import java.util.Set;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpZoneMasterInfo;

public class GroupScalePricing extends FDGroup {
		
	private static final long	serialVersionUID	= -3247305173630685836L;
	
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
	
	/*public GrpZonePriceModel getGrpZonePrice(ZoneInfo pricingZoneInfo) {
		try {//::FDX:: this does half the job.
			GrpZonePriceModel grpZpModel = this.grpZonePriceList.getGrpZonePrice(pricingZoneInfo);
			if(grpZpModel == null) {
				//do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(pricingZoneInfo.getPricingZoneId());
				if(zoneInfo != null && zoneInfo.getParentZone() != null){
					//This check has been added to make sure the following line does not
					//throw NPE when master default zone does not have a defined GS price.
					grpZpModel = getGrpZonePrice(new ZoneInfo(zoneInfo.getParentZone().getSapId(),pricingZoneInfo.getSalesOrg(),pricingZoneInfo.getDistributionChanel()));
				}
			}
			return grpZpModel;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the Group Zone Price Model");
		}
		
		
	}
	
	public GrpZonePriceModel _getGrpZonePrice(ZoneInfo pricingZone) {
		try {
			ZoneInfo _pricingZone=pricingZone;
			GrpZonePriceModel grpZpModel = this.grpZonePriceList.getGrpZonePrice(_pricingZone);
			while(grpZpModel == null) {
				//do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(_pricingZone.getPricingZoneId());
				if(zoneInfo != null && zoneInfo.getParentZone() != null){
					//This check has been added to make sure the following line does not
					//throw NPE when master default zone does not have a defined GS price.
					grpZpModel = this.grpZonePriceList.getGrpZonePrice(new ZoneInfo(zoneInfo.getParentZone().getSapId(),_pricingZone.getSalesOrg(),_pricingZone.getDistributionChanel()));
				} else if(zoneInfo != null && zoneInfo.getParentZone() == null) {
					
				} else {
					//::FDX:: do what?
				}
			}
			return grpZpModel;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the Group Zone Price Model");
		}
		
		
	}*/

	private GrpZonePriceModel _getZonePrice(ZoneInfo pricingZoneInfo) {//new
		try {
		
			GrpZonePriceModel zpModel = this.grpZonePriceList.getGrpZonePrice(pricingZoneInfo.hasParentZone()?new ZoneInfo(pricingZoneInfo.getPricingZoneId(),pricingZoneInfo.getSalesOrg(),pricingZoneInfo.getDistributionChanel()):pricingZoneInfo);
			if(zpModel == null) {
				//::FDX:do a item cascading to its parent until we find a price info.
				ErpZoneMasterInfo zoneInfo = FDCachedFactory.getZoneInfo(pricingZoneInfo.getPricingZoneId());
				if(zoneInfo.getParentZone()!=null)
					zpModel = _getZonePrice(new ZoneInfo(zoneInfo.getParentZone().getSapId(),pricingZoneInfo.getSalesOrg(),pricingZoneInfo.getDistributionChanel()));
			}
			return zpModel;
		}
		catch(FDResourceException fe){
			throw new FDRuntimeException(fe, "Unexcepted error happened while fetching the Zone Price Model");
		}
	}
	
	
	
	
	public GrpZonePriceModel getGrpZonePrice(ZoneInfo pricingZoneInfo) {//new
		ZoneInfo zone=pricingZoneInfo;
		GrpZonePriceModel zpModel=_getZonePrice(zone);
		//APPDEV-6291 - Group Scale Pricing(Cascade for Sales Org) 
		while(zpModel==null && zone.hasParentZone()) {
			zone=zone.getParentZone();
			zpModel=_getZonePrice(zone);
		}
		return zpModel;
	}
	public List<String> getSkuList() {
		return skuList;
	}
	
}
