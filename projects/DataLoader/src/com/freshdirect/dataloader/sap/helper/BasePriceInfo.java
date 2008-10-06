package com.freshdirect.dataloader.sap.helper;

public class BasePriceInfo implements java.io.Serializable {
	
	private String materialID;
	private double price;
	private String unit;
	
	public BasePriceInfo(String materialID, double price, String unit) {
		
		this.materialID = materialID;
		this.price = price;
		this.unit = unit;
	}
	

	public String getMaterialID() {
		return materialID;
	}
	public void setMaterialID(String materialID) {
		this.materialID = materialID;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public int hashCode() {
		
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((materialID == null) ? 0 : materialID.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BasePriceInfo other = (BasePriceInfo) obj;
		if (materialID == null) {
			if (other.materialID != null)
				return false;
		} else if (!materialID.equals(other.materialID))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}
	

}

