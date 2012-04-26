package com.freshdirect.fdstore.content;

public class FilteringMenuItem {
	
	private String name;
	private String filteringUrlValue;
	private int counter;
	private boolean isHidden;
	private boolean isSelected;
	private EnumFilteringValue filter;
	
	public FilteringMenuItem(){
		
	}
	
	public FilteringMenuItem(String name, String url, int counter, EnumFilteringValue filter) {
		this.name = name;
		this.filteringUrlValue = url;
		this.counter = counter;
		this.isHidden = false;
		this.isSelected = false;
		this.filter = filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilteringUrlValue() {
		return filteringUrlValue;
	}

	public void setFilteringUrlValue(String filteringUrlValue) {
		this.filteringUrlValue = filteringUrlValue;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public boolean isHidden() {
		return isHidden;
	}
	
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	public void hide() {
		this.isHidden = true;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public void setFilter(EnumFilteringValue filter) {
		this.filter = filter;
	}
	
	public EnumFilteringValue getFilter() {
		return filter;
	}
	
	@Override
	public boolean equals(Object obj) {
		FilteringMenuItem other= (FilteringMenuItem) obj;
		
		if(other.getFilteringUrlValue().equals(filteringUrlValue) && other.getName().equals(name)){
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((filteringUrlValue == null) ? 0 : filteringUrlValue.hashCode());
		return result;
	}
	
}
