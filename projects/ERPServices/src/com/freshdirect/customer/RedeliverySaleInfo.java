/**
 * 
 * RedeliverySaleInfo.java
 * Created Dec 2, 2002
 */
package com.freshdirect.customer;

/**
 *
 *  @author knadeem
 */
import java.util.Date;

public class RedeliverySaleInfo extends BasicSaleInfo {
	
	private Date startTime;
	private Date endTime;
    private String truckNumber;
    private String stopSequence;
    private String customerName;
    private String deliveryAddress;
	
	
	public RedeliverySaleInfo(String saleId, String erpCustomerId, EnumSaleStatus status, String truckNumber, String stopSequence, String customerName, String deliveryAddress, Date startTime, Date endTime) {
		super(saleId,erpCustomerId, status);
        this.truckNumber = truckNumber;
        this.stopSequence = stopSequence;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}
	
	public Date getEndTime( ){
		return this.endTime;
	}
    
    public String getTruckNumber() {
        return this.truckNumber;
    }
    
    public String getStopSequence() {
        return this.stopSequence;
    }

    public String getCustomerName() {
        return this.customerName;
    }
    
    public String getDeliveryAddress() {
        return this.deliveryAddress;
    }
    
}
