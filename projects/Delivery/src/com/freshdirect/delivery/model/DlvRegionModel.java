/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.delivery.model;

import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.collection.*;

/**
 * DlvRegion model object.
 *
 * @version    $Revision$
 * @author     $Author$
 */
public class DlvRegionModel extends ModelSupport {
	
	/** name of this model */
	private String name;
	private double deliveryCharges;
	private String regionDataPk;
	private List versions = new ArrayList();
	
	/**
	 *
	 */
	private LocalObjectList zones = new LocalObjectList();
	
	/** Creates new DlvRegionModel */
	public DlvRegionModel() {
		super();
	}
	
	public DlvRegionModel(PrimaryKey pk, String name){
		super();
		this.setPK(pk);
		this.name = name;
	}
	
	/**
	 * Constructor with all properties
	 *
	 *@param String name
	 */
	public DlvRegionModel(String name, double deliveryCharges, List zones){
		this.setName(name);
		this.deliveryCharges = deliveryCharges;
		this.zones = new LocalObjectList(zones);
	}
	
	/**
	 * get Name for this model
	 *
	 * @return String name
	 * @throws RemoteException if there is any problem in accesing remotely
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * set Name for this model
	 *
	 * @param String name
	 * @throws RemoteException if there is any problem in accesing remotely
	 */
	public void setName(String name){
		this.name = name;
	}
	
	public double getDeliveryCharges(){
		return this.deliveryCharges;
	}
	
	public void setDeliveryCharges(double deliveryCharges){
		this.deliveryCharges = deliveryCharges;
	}
	
	public String getRegionDataPk(){
		return this.regionDataPk;
	}
	
	public void setRegionDataPk(String regionDataPk){
		this.regionDataPk = regionDataPk;
	}
	
	public DlvZoneModel getZone(String zoneCode){
		DlvZoneModel foundZone = null;
		for(Iterator i = this.zones.iterator(); i.hasNext(); ){
			DlvZoneModel zone = (DlvZoneModel)i.next();
			if(zone.getZoneCode().equals(zoneCode)){
				foundZone = zone;
				break;
			}
		}
		return foundZone;
	}
	
	public List getZones() {
		Collections.sort(this.zones, DlvZoneModel.COMPARATOR_ZONE_ZONECODE);
		return (List)this.zones.clone();
	}
	
	public List getZonesForPlan(String planId){
		
		List matchingZones = new ArrayList();
		for(int i = 0, size = zones.size(); i < size; i++){
			DlvZoneModel zone = (DlvZoneModel)zones.get(i);
			if(planId.equalsIgnoreCase(zone.getPlanId())){
				matchingZones.add(zone);
			}
		}
		return matchingZones;
	}
	
	public void setZones(List zones) {
		this.zones = new LocalObjectList(zones);
	}
	
	public void addVersion(Date date){
		this.versions.add(date);
	}
	
	public List getVersions(){
		return this.versions;
	}
}