package com.freshdirect.transadmin.datamanager.model;

import com.freshdirect.transadmin.web.model.ResourceList;

public class WorkTableModel {
	

	private String code;
	private String name;
	private String deliveryFee;
	
	
	private boolean isWorkTableOnly=false;
	private boolean isZoneOnly=false;
	private boolean isCommonInBoth=false;
	
	private String newZone;
	private String commonInboth;
	private String zoneTblOnly;
	
	public String getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}


	public String getZoneTblOnly() {
		return zoneTblOnly;
	}

	public void setZoneTblOnly(String zoneTblOnly) {
		this.zoneTblOnly = zoneTblOnly;
	}

	public String getCommonInboth() {
		return commonInboth;
	}

	public void setCommonInboth(String commonInboth) {
		this.commonInboth = commonInboth;
	}

	public String getNewZone() {
		return newZone;
	}

	public void setNewZone(String newZone) {
		this.newZone = newZone;
	}

	public boolean isWorkTableOnly() {
		return isWorkTableOnly;
	}

	public void setWorkTableOnly(boolean isWorkTableOnly) {
		this.isWorkTableOnly = isWorkTableOnly;
	}

	public boolean isZoneOnly() {
		return isZoneOnly;
	}

	public void setZoneOnly(boolean isZoneOnly) {
		this.isZoneOnly = isZoneOnly;
	}

	public boolean isCommonInBoth() {
		return isCommonInBoth;
	}

	public void setCommonInBoth(boolean isCommonInBoth) {
		this.isCommonInBoth = isCommonInBoth;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public WorkTableModel(){
		super();
	}
	
	public WorkTableModel(String code, String name, String deliveryfee) {
	
		this.code = code;
		this.name = name;
		this.deliveryFee=deliveryfee;
	}
	
	public WorkTableModel(String code, String name) {
		
		this.code = code;
		this.name = name;
	}
	
	
	public boolean equals(Object obj){
		if(obj instanceof WorkTableModel){
			WorkTableModel w=(WorkTableModel)obj;
		   
			if(this.code.trim().equalsIgnoreCase(w.getCode().trim())) return true;		 	
			
		}
		return false;
	}
	
	public int hashCode(){
		
		return 37*this.code.hashCode();
	}
	
	
	
	/*@Override
	public int compareTo(Object o) {
		if(o instanceof WorkTableModel){
			WorkTableModel w= (WorkTableModel)o;
			if(w.getCode().equals(this.code)){
				return 0;
			}
		}
		return 1;
	}
	*/
	
}
