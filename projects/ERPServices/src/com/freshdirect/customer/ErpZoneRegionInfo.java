package com.freshdirect.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;

public class ErpZoneRegionInfo extends ModelSupport implements Serializable{

	public ErpZoneRegionInfo(String sapId,String desc){
		this.description=desc;
		this.sapId=sapId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public List getZoneRegionZipList() {
		return zoneRegionZipList;
	}

	public void setZoneRegionZipList(List zoneRegionZipList) {
		this.zoneRegionZipList = zoneRegionZipList;
	}
	
    public void addZoneRegionZipModel(ErpZoneRegionZipInfo zipModel){
    	if(this.zoneRegionZipList==null) this.zoneRegionZipList=new ArrayList();
    	this.zoneRegionZipList.add(zipModel);
    }
	
	
	private String description;	
	private int version;	
	private String sapId;
	private List zoneRegionZipList=null;
	
}
