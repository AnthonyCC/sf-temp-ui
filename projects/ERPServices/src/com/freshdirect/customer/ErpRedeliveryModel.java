/**
 * 
 * ErpRedeliveryModel.java
 * Created Nov 19, 2002
 */
package com.freshdirect.customer;

/**
 *
 *  @author knadeem
 */
public class ErpRedeliveryModel extends ErpTransactionModel {
	
	private ErpDeliveryInfoModel deliveryInfo;
	
	public ErpRedeliveryModel(){
		super(EnumTransactionType.REDELIVERY);
		deliveryInfo = new ErpDeliveryInfoModel();
	}
	
	public ErpDeliveryInfoModel getDeliveryInfo() { 
		return deliveryInfo; 
	}

    public void setDeliveryInfo(ErpDeliveryInfoModel deliveryInfo) { 
    	this.deliveryInfo = deliveryInfo; 
    }
    
    public double getAmount() {
        return 0.0;
    }

}
