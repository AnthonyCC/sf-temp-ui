/*
 * 
 * BasicSaleInfo.java
 * Date: Sep 13, 2002 Time: 7:02:03 PM
 */
package com.freshdirect.customer;

/**
 * 
 * @author knadeem
 */
import java.io.Serializable;

public class BasicSaleInfo implements Serializable {
	
	private final String saleId;
	private final EnumSaleStatus status;
	private final String erpCustomerId;
	public BasicSaleInfo (String saleId, String erpCustomerId, EnumSaleStatus status){
		this.saleId = saleId;
		this.status = status;
		this.erpCustomerId = erpCustomerId;
	}
	
	public String getSaleId(){
		return this.saleId;
	}
	
	public EnumSaleStatus getStatus(){
		return this.status;
	}

	public String getErpCustomerId(){
		return this.erpCustomerId;
	}
}
