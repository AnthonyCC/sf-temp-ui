
package com.freshdirect.fdstore.atp;


public class FDStockAvailabilityInfo extends FDAvailabilityInfo {
	
	protected final double quantity;
	
	public  FDStockAvailabilityInfo(boolean available, double quantity){
		super(available);
		this.quantity = quantity;
	}
	
	
	public double getQuantity(){
		return this.quantity;
	}

	public String toString() {
		return "[FDStockAvailabilityInfo available: "+this.available+ " quantity: "+this.quantity+"]";
	}
}
