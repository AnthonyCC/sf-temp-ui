package com.freshdirect.storeapi.content;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class FilteringMenuItem implements Serializable {

	private static final long serialVersionUID = 264989063964263913L;

	private String name;
	private String filteringUrlValue;
	private int counter;
	private boolean isHidden;
	private boolean isSelected;
	private FilteringValue filter;
	private String info;
	private String year;
	private Date deliveryDate;

	public FilteringMenuItem() {

	}

	public FilteringMenuItem(String name, String url, int counter, FilteringValue filter) {
		this.name = name;
		this.filteringUrlValue = url;
		this.counter = counter;
		this.isHidden = false;
		this.isSelected = false;
		this.filter = filter;
	}

	public FilteringMenuItem clone(FilteringMenuItem source) {
		return new FilteringMenuItem(name, filteringUrlValue, counter, filter);
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

	public void setFilter(FilteringValue filter) {
		this.filter = filter;
	}

	public FilteringValue getFilter() {
		return filter;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	public boolean equals(Object obj) {
		FilteringMenuItem other = (FilteringMenuItem) obj;

		if (other.getFilteringUrlValue().equals(filteringUrlValue) && other.getName().equals(name)) {
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

	@Override
	public String toString() {
		return "[" + filter + "=" + filteringUrlValue + "]=>" + name + "(" + counter + ")";
	}

	public static final Comparator<FilteringMenuItem> COUNT_ORDER_REV = new Comparator<FilteringMenuItem>() {
		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			return o2.getCounter() - o1.getCounter();
		}
	};

	public static final Comparator<FilteringMenuItem> RATING_COMP = new Comparator<FilteringMenuItem>() {
		@Override
		public int compare(FilteringMenuItem o1, FilteringMenuItem o2) {
			int a = 0;
			try {
				a = Integer.parseInt(o1.getName());
			} catch (NumberFormatException e) {
			}
			int b = 0;
			try {
				b = Integer.parseInt(o2.getName());
			} catch (NumberFormatException e) {
			}
			return a > b ? -1 : a < b ? +1 : 0;
		}
	};

}
