package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *	Simple java bean for product config requests. 
 *	Class structure is representing the received JSON structure. 	
 * 
 * @author treer
 */
public class ProductConfigRequestData implements Serializable {
	
	private static final long	serialVersionUID	= -4196044006826787164L;
	
	/**
	 * required
	 */
	private String productId = null;
	
	/**
	 * optional
	 */
	private String categoryId = null;
	
	/**
	 * optional
	 */
	private String skuCode = null;
	
	private String salesUnit = null;
	
	private double quantity = 0.0;
	
	/**
	 * optional
	 */
	private Map<String,String> configuration = new HashMap<String, String>();

	
	public String getProductId() {
		return productId;
	}	
	public void setProductId( String productId ) {
		this.productId = productId;
	}	
	public String getCategoryId() {
		return categoryId;
	}	
	public void setCategoryId( String categoryId ) {
		this.categoryId = categoryId;
	}	
	public String getSkuCode() {
		return skuCode;
	}	
	public void setSkuCode( String skuCode ) {
		this.skuCode = skuCode;
	}	
	public Map<String, String> getConfiguration() {
		return configuration;
	}	
	public void setConfiguration( Map<String, String> configuration ) {
		this.configuration = configuration;
	}
	public String getSalesUnit() {
		return salesUnit;
	}
	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
}
