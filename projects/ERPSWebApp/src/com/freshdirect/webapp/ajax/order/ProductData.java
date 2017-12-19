package com.freshdirect.webapp.ajax.order;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;

public class ProductData implements Serializable{
	private static final long serialVersionUID = 6050392174425276731L;
	private String id;
	private String title;
	private List<SalesUnit> saleUnit;
	public ProductData(String id, String title, List<SalesUnit> saleUnit){
		this.id = id;
		this.title = title;
		this.saleUnit = saleUnit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public int hashCode() {
	    return (id == null? 0 : id.hashCode());
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof ProductData){
			ProductData toCompare = (ProductData) o;
		    return this.id.equals(toCompare.id);
		  }
		  return false;
	}
	public List<SalesUnit> getSaleUnit() {
		return saleUnit;
	}
	public void setSaleUnit(List<SalesUnit> saleUnit) {
		this.saleUnit = saleUnit;
	}
	
}

