package com.freshdirect.webapp.ajax.order;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;

public class ProductData implements Serializable {
	private static final long serialVersionUID = 6050392174425276731L;
	private String id;
	private String skuCode;
	private String title;
	private double quantity;
	private List<SalesUnit> saleUnit;

	public ProductData(QuickShopLineItem item) {
		if (item != null) {
			this.id = item.getProductId();
			this.skuCode = item.getSkuCode();
			this.title = item.getProductName();
			this.saleUnit = item.getSalesUnit();
			if (item.getQuantity() != null) {
				quantity = item.getQuantity().getQuantity();
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public List<SalesUnit> getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(List<SalesUnit> saleUnit) {
		this.saleUnit = saleUnit;
	}

	@Override
	public int hashCode() {
		return (id == null ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ProductData) {
			ProductData toCompare = (ProductData) o;
			return this.id.equals(toCompare.id);
		}
		return false;
	}

}
