package com.freshdirect.transadmin.web.model;


public class Tooltip implements IToolTip,Comparable {
	
	Object value = null;
	String toolTip = null;
	
	public Tooltip(Object value, String tooltip) {
		this.value = value;
		this.toolTip = tooltip;
	}

	

	public Object getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



	public String getToolTip() {
		return toolTip;
	}



	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	
	public String toString() {
		if(value!=null)
			return getValue().toString();
		return "";
	}
	
	public int compareTo(Object o) {
		if(o instanceof Tooltip) {
			String _val=((Tooltip)o).getValue().toString();
			return ((String)value).compareTo(_val);
		}
		return 0;
		
	}
}
