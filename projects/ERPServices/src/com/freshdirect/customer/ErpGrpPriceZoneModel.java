package com.freshdirect.customer;

import java.io.Serializable;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.framework.core.ModelSupport;

public class ErpGrpPriceZoneModel extends ModelSupport implements Serializable{

	private ZoneInfo zone;
	public ZoneInfo getZone() {
		return zone;
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
	private String salesOrg;
	private String distChannel;
	
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
	
	public ErpGrpPriceZoneModel(ZoneInfo zone,double qty,String unitOfMeasure,double price){
		this.price=price;
		this.qty=qty;
		this.unitOfMeasure=unitOfMeasure;
		this.zone=zone;		
	}
	
	public ErpGrpPriceZoneModel(ZoneInfo zone,double qty,String unitOfMeasure,double price, String scaleUnit){
		this.price=price;
		this.qty=qty;
		this.unitOfMeasure=unitOfMeasure;
		this.zone=zone;		
		this.scaleUnit = scaleUnit;
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((distChannel == null) ? 0 : distChannel.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(qty);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((salesOrg == null) ? 0 : salesOrg.hashCode());
		result = prime * result
				+ ((scaleUnit == null) ? 0 : scaleUnit.hashCode());
		result = prime * result
				+ ((unitOfMeasure == null) ? 0 : unitOfMeasure.hashCode());
		result = prime * result + version;
		result = prime * result + ((zone == null) ? 0 : zone.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErpGrpPriceZoneModel other = (ErpGrpPriceZoneModel) obj;
		if (distChannel == null) {
			if (other.distChannel != null)
				return false;
		} else if (!distChannel.equals(other.distChannel))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (Double.doubleToLongBits(qty) != Double.doubleToLongBits(other.qty))
			return false;
		if (salesOrg == null) {
			if (other.salesOrg != null)
				return false;
		} else if (!salesOrg.equals(other.salesOrg))
			return false;
		if (scaleUnit == null) {
			if (other.scaleUnit != null)
				return false;
		} else if (!scaleUnit.equals(other.scaleUnit))
			return false;
		if (unitOfMeasure == null) {
			if (other.unitOfMeasure != null)
				return false;
		} else if (!unitOfMeasure.equals(other.unitOfMeasure))
			return false;
		if (version != other.version)
			return false;
		if (zone == null) {
			if (other.zone != null)
				return false;
		} else if (!zone.equals(other.zone))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ErpGrpPriceZoneModel [zone=" + zone + ", qty=" + qty
				+ ", unitOfMeasure=" + unitOfMeasure + ", price=" + price
				+ ", salesOrg=" + salesOrg + ", distChannel=" + distChannel
				+ ", version=" + version + ", scaleUnit=" + scaleUnit + "]";
	}
	
	
}
