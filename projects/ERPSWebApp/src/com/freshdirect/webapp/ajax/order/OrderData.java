package com.freshdirect.webapp.ajax.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderData implements Serializable{

	private static final long serialVersionUID = -4519095971094960664L;
	private List<ProductData> products;
	
	private String orderId;
	private Date orderDeliveryDate;
	
	public OrderData(String id, Date date, ProductData product) {
		orderId = id;
		orderDeliveryDate = date;
		products = new ArrayList<ProductData>();
		if(product != null){
			products.add(product);
		}
	}
	public List<ProductData> getProducts(){
		return products;
	}

	public void setProducts(List<ProductData> products) {
		this.products = products;
	}
	public void addProduct(ProductData p) {
		if (!getProducts().contains(p)) {
			getProducts().add(p);
		}
	}
	public Date getOrderDeliveryDate() {
		return orderDeliveryDate;
	}
	public void setOrderDeliveryDate(Date orderDate) {
		this.orderDeliveryDate = orderDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}

