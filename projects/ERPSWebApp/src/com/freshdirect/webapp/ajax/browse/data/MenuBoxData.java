package com.freshdirect.webapp.ajax.browse.data;

import java.util.List;

import com.freshdirect.storeapi.content.ProductFilterGroupType;

public class MenuBoxData extends BasicData {
	
	public enum MenuBoxDisplayType{
		SIMPLE, POPUP, CENTER_POPUP;
	}
	
	public enum MenuBoxSelectionType{
		SINGLE, MULTI, LINK;
	}
	
	public enum MenuBoxType{
		SUPERDEPARTMENT(false),
		DEPARTMENT(false),
		CATEGORY(false),
		SUB_CATEGORY(true),
		SUB_SUB_CATEGORY(true),
		FILTER(true);
		
		private boolean hasFilterEffect;
		
		MenuBoxType(boolean hasFilterEffect){
			this.hasFilterEffect = hasFilterEffect;
		}

		public boolean isHasFilterEffect() {
			return hasFilterEffect;
		}

		public void setHasFilterEffect(boolean hasFilterEffect) {
			this.hasFilterEffect = hasFilterEffect;
		}
		
	}

	private static final long serialVersionUID = 8636429466943993604L;
	private List<MenuItemData> items;
	private MenuBoxDisplayType displayType;
	private MenuBoxSelectionType selectionType;
	private MenuBoxType boxType;
	private String selectedLabel;
	private Integer position=Integer.MAX_VALUE;
	private boolean multiGroupBox;
	private boolean brandFilter;
	
	
	/**
	 * all=true should be set for user selection
	 */
	private boolean shouldSetAll = false;

	public List<MenuItemData> getItems() {
		return items;
	}
	public void setItems(List<MenuItemData> items) {
		this.items = items;
	}
	public void addItems(List<MenuItemData> items) {
		if (this.items == null) {
			this.items = items;
		} else {
			this.items.addAll(items);
		}
	}
	public MenuBoxDisplayType getDisplayType() {
		return displayType;
	}
	public void setDisplayType(MenuBoxDisplayType displayType) {
		this.displayType = displayType;
	}
	public MenuBoxSelectionType getSelectionType() {
		return selectionType;
	}
	public void setSelectionType(MenuBoxSelectionType selectionType) {
		this.selectionType = selectionType;
	}
	public MenuBoxType getBoxType() {
		return boxType;
	}
	public void setBoxType(MenuBoxType boxType) {
		this.boxType = boxType;
	}
	public String getSelectedLabel() {
		return selectedLabel;
	}
	public void setSelectedLabel(String selectedLabel) {
		this.selectedLabel = selectedLabel;
	}
	public boolean isShouldSetAll() {
		return shouldSetAll;
	}
	public void setShouldSetAll(boolean shouldSetAll) {
		this.shouldSetAll = shouldSetAll;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public boolean isMultiGroupBox() {
		return multiGroupBox;
	}
	public void setMultiGroupBox(boolean multiGroupBox) {
		this.multiGroupBox = multiGroupBox;
	}
	public boolean isBrandFilter() {
		return brandFilter;
	}
	public void setBrandFilter(boolean brandFilter) {
		this.brandFilter = brandFilter;
	}
	
}
