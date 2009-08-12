package com.freshdirect.cms.ui.model;

import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;


public class BulkEditModel extends ContentNodeModel {

	private static final long	serialVersionUID	= -676595176933252056L;

	protected BulkEditModel() {
		super();
	}
	
	public BulkEditModel(String type, String label, String key) {
		super(type, label, key);
		setFullName("");
		setGlanceName("");
		setNavName("");
	}
	
	public void setFullName(String fullname) {		
		set("fullname", fullname);
	}
	
	public void setNavName(String navname) {
		set("navname", navname);
	}
	
	public void setGlanceName(String glancename) {
		set("glancename", glancename);
	}
	
	public String getFullName() {
		return get("fullname");
	}
	
	public String getNavName() {
		return get("navname");
	}
	
	public String getGlanceName() {
		return get("glancename");
	}

}
