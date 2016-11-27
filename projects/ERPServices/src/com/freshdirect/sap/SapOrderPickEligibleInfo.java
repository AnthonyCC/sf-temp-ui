package com.freshdirect.sap;

import java.io.Serializable;
import java.util.Date;

public class SapOrderPickEligibleInfo implements Serializable {

	public SapOrderPickEligibleInfo(Date deliveryDate, String sapSalesOrder,
			String webSalesOrder) {
		super();
		this.deliveryDate = deliveryDate;
		this.sapSalesOrder = sapSalesOrder;
		this.webSalesOrder = webSalesOrder;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3647117568838616718L;
	
	private Date deliveryDate;
	private String sapSalesOrder;
	private String webSalesOrder;
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getSapSalesOrder() {
		return sapSalesOrder;
	}
	public void setSapSalesOrder(String sapSalesOrder) {
		this.sapSalesOrder = sapSalesOrder;
	}
	public String getWebSalesOrder() {
		return webSalesOrder;
	}
	public void setWebSalesOrder(String webSalesOrder) {
		this.webSalesOrder = webSalesOrder;
	}
	@Override
	public String toString() {
		return "SapOrderPickEligibleInfo [deliveryDate=" + deliveryDate
				+ ", sapSalesOrder=" + sapSalesOrder + ", webSalesOrder="
				+ webSalesOrder + "]";
	}
}
