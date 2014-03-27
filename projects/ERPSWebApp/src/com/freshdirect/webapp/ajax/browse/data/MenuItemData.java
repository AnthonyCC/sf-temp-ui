package com.freshdirect.webapp.ajax.browse.data;


//TODO verify structure
public class MenuItemData extends SelectableData {

	private static final long serialVersionUID = -5000076157128296031L;
	private String urlParameter;
	private boolean active;
	private boolean special;
	
	public MenuItemData() {
		super();
	}
	
	public MenuItemData(String name) {
		super(name);
	}
	
	public String getUrlParameter() {
		return urlParameter;
	}
	public void setUrlParameter(String urlParameter) {
		this.urlParameter = urlParameter;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}	
	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public MenuItemData copy(){
		
		MenuItemData copy = new MenuItemData();
		
		copy.setActive(isActive());
		copy.setId(getId());
		copy.setName(getName());
		copy.setSelected(isSelected());
		copy.setUrlParameter(getUrlParameter());
		copy.setSpecial(isSpecial());
		
		return copy;
	}
	
	
}
