package com.freshdirect.webapp.ajax.quickshop.data;

import java.io.Serializable;

public class QuickShopSorterValues implements Serializable {
	
	private static final long	serialVersionUID	= -5736297639029178465L;
	
	private String sortId;
	private String name;
	private boolean isOrderAsc;
	private boolean selected;	
	
	public QuickShopSorterValues(String sortId, String name, boolean isOrderAsc, boolean selected) {
		super();
		this.sortId = sortId;
		this.name = name;
		this.isOrderAsc = isOrderAsc;
		this.selected = selected;
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOrderAsc() {
		return isOrderAsc;
	}
	public void setOrderAsc(boolean isOrderAsc) {
		this.isOrderAsc = isOrderAsc;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
