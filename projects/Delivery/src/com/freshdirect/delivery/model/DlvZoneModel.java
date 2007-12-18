/*
 * DlvZoneModel.java
 *
 * Created on August 28, 2001, 1:08 PM
 */

package com.freshdirect.delivery.model;

/**
 *
 * @author  knadeem
 * @version
 */
import java.util.Comparator;

import com.freshdirect.framework.core.*;

public class DlvZoneModel extends ModelSupport {
	
	public final static Comparator COMPARATOR_ZONE_ZONECODE = new Comparator() {
		public int compare(Object o1, Object o2) {
			DlvZoneModel z1 = (DlvZoneModel)o1;
			DlvZoneModel z2 = (DlvZoneModel)o2;
			return z1.getZoneCode().compareTo( z2.getZoneCode() );
		}
	};
	
	/** name for this zone */
	private String name;
	private String planId;
	private boolean ctActive;
	private int ctReleaseTime;
	
	private DlvZoneDescriptor zoneDescriptor;
	
	/** Creates new DlvZoneModel */
	public DlvZoneModel() {
		super();
	}
	
	/**
	 * Constructor with all properties
	 */
	public DlvZoneModel(PrimaryKey pk, String name, String planId, boolean ctActive, int ctReleaseTime, DlvZoneDescriptor zoneDescriptor){
		super();
		this.setPK(pk);
		this.name = name;
		this.planId = planId;
		this.ctActive = ctActive;
		this.ctReleaseTime = ctReleaseTime;
		
		this.zoneDescriptor = zoneDescriptor;
	}

	/**
	 * get name for this zone
	 *
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set name for this zone
	 *
	 * @param String name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String getZoneCode(){
		return this.zoneDescriptor.getZoneCode();
	}
	
	
	public String getDisplayName(){
		return this.zoneDescriptor.getZoneCode() +" - " + name;
	}
	
	public String toString(){
		return this.zoneDescriptor.getZoneCode() +" - " + name;
	}
	
	public String getPlanId(){
		return this.planId;
	}
	public void setPlanId(String planId){
		this.planId = planId;
	}

	public boolean isCtActive() {
		return ctActive;
	}

	public void setCtActive(boolean ctActive) {
		this.ctActive = ctActive;
	}

	public int getCtReleaseTime() {
		return ctReleaseTime;
	}

	public void setCtReleaseTime(int ctReleaseTime) {
		this.ctReleaseTime = ctReleaseTime;
	}
	
	public DlvZoneDescriptor getZoneDescriptor() {
		return zoneDescriptor;
	}
	
	public void setZoneDescriptor(DlvZoneDescriptor zoneDescriptor) {
		this.zoneDescriptor = zoneDescriptor;
	}
	
	/*
	public boolean isUnattended() {
		System.out.println(" ===> Reading for " + zoneCode + ": "  + unattended);
		return unattended;
	}
	
	public void setUnattended(boolean unattended) {
		System.out.println(" ===> Setting to " + unattended + " in zone " + zoneCode);
		this.unattended = unattended;
	}
	*/

}