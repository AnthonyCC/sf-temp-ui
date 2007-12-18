package com.freshdirect.delivery.model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.delivery.model.DlvRegionDataModel.RegionDataComparator;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.QuickDateFormat;

/**
 * DlvRegion entity bean implementation.
 *
 * @version    $Revision$
 * @author     $Author$
 *
 */
public class DlvRegion extends ModelSupport {
	
	private String name;
	
	private List regionDataList;
	
	public DlvRegion() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getRegionDataList() {
		return regionDataList;
	}

	public void setRegionDataList(List regionDataList) {
		java.util.Collections.sort(regionDataList, new RegionDataComparator());
		this.regionDataList = regionDataList;
	}
	
	public DlvRegionDataModel getCurrentRegionData(){
		return getCurrentRegionData(new GregorianCalendar().getTime());
	}
	
	public DlvRegionDataModel getCurrentRegionData(java.util.Date lookupDate){
		DlvRegionDataModel foundRegionData = null;
		QuickDateFormat qf = new QuickDateFormat(QuickDateFormat.FORMAT_SHORT_DATE);
		String param = qf.format(lookupDate);
		for(Iterator i = this.regionDataList.iterator(); i.hasNext();){
			DlvRegionDataModel regionData = (DlvRegionDataModel)i.next();
			if(qf.format(regionData.getStartDate()).compareTo(param) <= 0) {
				foundRegionData =regionData;
			}
		}		
		return foundRegionData;
	}
	
	public ModelI getModel() {
		DlvRegionDataModel regionData = getCurrentRegionData();
		DlvRegionModel model = null;
		if(regionData != null){
			model = new DlvRegionModel(this.getName(), regionData.getDeliveryCharges(), regionData.getZones());
			model.setRegionDataPk(regionData.getPK().getId());
		}else{
			model = new DlvRegionModel(this.getName(), 0.0, new ArrayList());
		}
		for(Iterator i = this.regionDataList.iterator(); i.hasNext(); ){
			DlvRegionDataModel region_Data = (DlvRegionDataModel)i.next();
			model.addVersion(region_Data.getStartDate());
		}
		model.setPK(new PrimaryKey(this.getId()));
		return model;
	}

	public ModelI getModel(java.util.Date effectiveDate){
		DlvRegionDataModel regionData = getCurrentRegionData(effectiveDate);
		DlvRegionModel model = null;
		if(regionData != null){
			model = new DlvRegionModel(this.getName(), regionData.getDeliveryCharges(), regionData.getZones());
			model.setRegionDataPk(regionData.getPK().getId());
		}else{
			model = new DlvRegionModel(this.getName(), 0.0, new ArrayList());
		}
		for(Iterator i = this.regionDataList.iterator(); i.hasNext(); ){
			DlvRegionDataModel region_Data = (DlvRegionDataModel)i.next();
			model.addVersion(region_Data.getStartDate());
		}
		model.setPK(new PrimaryKey(this.getId()));
		return model;
	}

}