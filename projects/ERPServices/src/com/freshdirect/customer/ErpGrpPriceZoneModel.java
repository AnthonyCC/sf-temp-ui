package com.freshdirect.customer;

import java.io.Serializable;

import com.freshdirect.framework.core.ModelSupport;

public class ErpGrpPriceZoneModel extends ModelSupport implements Serializable{

	private String zoneId;
	public String getZoneId() {
		return zoneId;
	}
	public double getQty() {
		return qty;
	}
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public double getPrice() {
		return price;
	}
	private double qty;
	private String unitOfMeasure;
	private double price;
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version){
		this.version=version;
	}
	
	private int version;
	
	private String scaleUnit;
	public String getScaleUnit() {
		return scaleUnit;
	}
	public void setScaleUnit(String scaleUnit) {
		this.scaleUnit = scaleUnit;
	}
	
	public ErpGrpPriceZoneModel(String zoneId,double qty,String unitOfMeasure,double price){
		this.price=price;
		this.qty=qty;
		this.unitOfMeasure=unitOfMeasure;
		this.zoneId=zoneId;		
	}
	
	public ErpGrpPriceZoneModel(String zoneId,double qty,String unitOfMeasure,double price, String scaleUnit){
		this.price=price;
		this.qty=qty;
		this.unitOfMeasure=unitOfMeasure;
		this.zoneId=zoneId;		
		this.scaleUnit = scaleUnit;
	}
	
	public boolean equals(Object o){
		if(o instanceof ErpGrpPriceZoneModel){
			ErpGrpPriceZoneModel model=(ErpGrpPriceZoneModel)o;
			if(this.zoneId.equalsIgnoreCase(model.getZoneId()) && this.qty==model.getQty()){
				return true;
			}			
		}				
		return false;
	}
	
	public int hashCode(){
		return 37+zoneId.hashCode()+(int)this.qty;
	}
	
}
