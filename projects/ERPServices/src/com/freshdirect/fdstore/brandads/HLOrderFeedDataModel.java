package com.freshdirect.fdstore.brandads;

public class HLOrderFeedDataModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String pUserId;
	private String cUserId;
	private String orderId;
	private String prodctSku;
	private String sku;
	private String parentSku;
	private String productPrice;
	private String price;
	private String quantity;
	private String orderTotal;
	
	public String getProdctSku() {
		return prodctSku;
	}
	public void setProdctSku(String prodctSku) {
		this.prodctSku = prodctSku;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getpUserId() {
		return pUserId;
	}
	public void setpUserId(String pUserId) {
		this.pUserId = pUserId;
	}
	public String getcUserId() {
		return cUserId;
	}
	public void setcUserId(String cUserId) {
		this.cUserId = cUserId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getParentSku() {
		return parentSku;
	}
	public void setParentSku(String parentSku) {
		this.parentSku = parentSku;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}
	
	@Override
	public String toString() {
		return "HLOrderFeedDataModel [clientId=" + clientId
				+ ", pUserId=" + pUserId + ", orderId=" + orderId
				+ ", sku=" + sku + ", parentSku=" + parentSku
				+  ", price=" + price
				+ ", quantity=" + quantity + ", orderTotal=" + orderTotal+ "]";
	}
	
}
