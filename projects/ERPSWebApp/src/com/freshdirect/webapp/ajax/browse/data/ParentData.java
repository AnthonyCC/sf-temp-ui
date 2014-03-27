package com.freshdirect.webapp.ajax.browse.data;

public class ParentData extends BasicData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1493685216298783528L;
	
	private String parentId;

	public ParentData(String parentId, String id, String name) {
		super(id, name);
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
