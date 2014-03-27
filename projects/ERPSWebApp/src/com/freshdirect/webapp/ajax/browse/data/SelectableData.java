package com.freshdirect.webapp.ajax.browse.data;


public class SelectableData extends BasicData {

	private static final long serialVersionUID = 2334738607850570805L;
	private boolean selected;
	
	public SelectableData() {
		super();
	}

	public SelectableData(String name){
		super(name);
	}

	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
