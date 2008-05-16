package com.freshdirect.webapp.util;

import com.freshdirect.customer.EnumSaleStatus;


public class OrderPermissionsImpl implements OrderPermissionsI {
	
	private final EnumSaleStatus status;
	private final String application;
	private final boolean makeGoodOrder;
	private final int creditIssued;
	
	public OrderPermissionsImpl(EnumSaleStatus status, String application, boolean makeGoodOrder, int creditIssued){
		this.status = status;
		this.application = application;
		this.makeGoodOrder = makeGoodOrder;
		this.creditIssued = creditIssued;
		
	}
	
	public boolean allowCancelOrder(){
		
		if ( EnumSaleStatus.SUBMITTED.equals(status) || EnumSaleStatus.AVS_EXCEPTION.equals(status) 
			|| EnumSaleStatus.AUTHORIZED.equals(status)  
			|| EnumSaleStatus.PENDING.equals(status) ) {
			 return true;
		}
		
        if ( (EnumSaleStatus.NOT_SUBMITTED.equals(status) || EnumSaleStatus.AUTHORIZATION_FAILED.equals(status) ) 
        	&& "CALLCENTER".equalsIgnoreCase(application) ) {
			return true;
		}
        
		return false;
	}
	
	public boolean allowComplaint(){
		
		if (EnumSaleStatus.ENROUTE.equals(status)
			|| EnumSaleStatus.REDELIVERY.equals(status)
			|| EnumSaleStatus.SETTLED.equals(status)
			|| EnumSaleStatus.PAYMENT_PENDING.equals(status)
			|| EnumSaleStatus.CAPTURE_PENDING.equals(status)
			|| EnumSaleStatus.SETTLED_RETURNED.equals(status)
			|| EnumSaleStatus.RETURNED.equals(status)) {
			
			if (this.creditIssued != 2 ) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean allowModifyOrder(){
		if(!this.makeGoodOrder){
			if ( EnumSaleStatus.SUBMITTED.equals(status) || EnumSaleStatus.AUTHORIZED.equals(status) 
					|| EnumSaleStatus.AVS_EXCEPTION.equals(status)) {
				return true;
			}
			if ( (EnumSaleStatus.NOT_SUBMITTED.equals(status) || EnumSaleStatus.AUTHORIZATION_FAILED.equals(status) ) 
					&& "CALLCENTER".equalsIgnoreCase(application) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean allowNewCharges(){
		
		if ( !EnumSaleStatus.ENROUTE.equals(status) && !EnumSaleStatus.REDELIVERY.equals(status) ) {
			return true;
		}
		
		return false;
	}
	
	public boolean allowResubmitOrder(){
		
		if ( EnumSaleStatus.NOT_SUBMITTED.equals(status) || EnumSaleStatus.MODIFIED.equals(status) 
			|| EnumSaleStatus.MODIFIED_CANCELED.equals(status) || EnumSaleStatus.NEW.equals(status)) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean allowReturnOrder(){
		
		if ( EnumSaleStatus.RETURNED.equals(status) ) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean hasPaymentException(){

		if( EnumSaleStatus.AUTHORIZATION_FAILED.equals(status) || EnumSaleStatus.CHARGEBACK.equals(status) 
			|| EnumSaleStatus.AVS_EXCEPTION.equals(status) ) {
			return true;
		}else{
			return false;
        }
	}
	
	public boolean isRefusedOrder() {
		if ( EnumSaleStatus.REFUSED_ORDER.equals(status) ) {
			return true;
		}
		
		return false;
	}
}