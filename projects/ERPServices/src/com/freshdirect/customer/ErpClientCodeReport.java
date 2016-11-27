package com.freshdirect.customer;

import java.util.Date;

public class ErpClientCodeReport extends ErpClientCode {
	private static final long serialVersionUID = -6748002134397469307L;

	private double unitPrice;
	
	private double taxRate;
	
	private Date deliveryDate;
	
	private String productDescription;
	
	private String orderId;

	public ErpClientCodeReport() {
		super();
	}

	public ErpClientCodeReport(ErpClientCode clientCode) {
		super(clientCode.getClientCode(), clientCode.getQuantity());
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
