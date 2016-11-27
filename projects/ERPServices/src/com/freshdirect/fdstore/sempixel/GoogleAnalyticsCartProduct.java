package com.freshdirect.fdstore.sempixel;

import java.text.DecimalFormat;

/*
 *	Google Analytics Cart Product utility class
 *
 *	Used to merge back in all unique products (based on sku) based on cartLines
 *
 *	Creates an object for each unique product (store in a hashmap in the jsp, not in this class), to total out quantities and prices
 *	for each product during the cartLine iteration on the receipt page. 
 *
 *	This info is then sent to Google Analytics (in the jsp, not in this class).
 */
public class GoogleAnalyticsCartProduct {
	private String skuCode = "";
	private double qty = 0;
	private double total = 0;
	private String unitPrice = "0";
	private String productId = "";
	private String productCategoryId = "";
	protected final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("0.##");
	protected final static DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");
	
	public GoogleAnalyticsCartProduct() {
		super();
	}
	
	public GoogleAnalyticsCartProduct(String skuCode, double qty, double total) {
		super();
		this.skuCode = skuCode;
		this.setQty(qty);
		this.setTotal(total);
	}
	
	public String getSkuCode() {
		return this.skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public double getTotal() {
		return total;
	}
	
	public String getTotalFormatted() {
		return CURRENCY_FORMATTER.format(total);
	}
	
	public void addToTotal(double addTotal) {
		this.setTotal(this.getTotal() + addTotal);
	}

	public void setQty(double d) {
		this.qty = d;
	}

	public double getQty() {
		return qty;
	}
	
	public String getQtyFormatted() {
		return QUANTITY_FORMATTER.format(qty);
	}
	
	public void addToQty(double addQty) {
		this.setQty(this.getQty() + addQty);
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}
}