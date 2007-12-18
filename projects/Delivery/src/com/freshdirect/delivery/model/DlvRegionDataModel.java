/*
 * DlvRegionDataModel.java
 *
 * Created on January 18, 2002, 5:56 PM
 */

package com.freshdirect.delivery.model;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.collection.LocalObjectList;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateComparator;

public class DlvRegionDataModel extends ModelSupport{
	
	private Date startDate;
	private double deliveryCharges;
	private List zones = new LocalObjectList();
	private LocalObjectList templates = new LocalObjectList();

	/** Creates new DlvRegionDataModel */
    public DlvRegionDataModel() {
		super();
    }
	
	/**
	 * Constructor with all properties
	 */
	public DlvRegionDataModel(Date startDate) {
		super();
		this.startDate = startDate;		
	}
	
	public DlvRegionDataModel(PrimaryKey pk, Date startDate){
		super();
		this.setPK(pk);
		this.startDate = startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}
	
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}
	
	public List getZones() {
		return (List)this.zones;
	}
	public void setZones(List zones) {
		this.zones = new LocalObjectList(zones);
	}
	
	public List getTemplates(){
		return (List)this.templates.clone();
	}
	public void setTemplates(List templates){
		this.templates = new LocalObjectList(templates);
	}
	
	public double getDeliveryCharges(){
		return this.deliveryCharges;
	}
	public void setDeliveryCharges(double deliveryCharges){
		this.deliveryCharges = deliveryCharges;
	}
	public static class RegionDataComparator extends DateComparator {
		
		public RegionDataComparator(){
			super(PRECISION_DAY);
		}
		
		public int compare(Object obj1, Object obj2){
			DlvRegionDataModel rd1 = (DlvRegionDataModel)obj1;
			DlvRegionDataModel rd2 = (DlvRegionDataModel)obj2;
			
			return super.compare(DateComparator.PRECISION_DAY, rd1.getStartDate(), rd2.getStartDate());
		}
	}

}
