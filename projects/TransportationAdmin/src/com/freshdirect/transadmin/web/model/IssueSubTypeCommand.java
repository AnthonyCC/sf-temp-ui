package com.freshdirect.transadmin.web.model;

import com.freshdirect.transadmin.model.IssueSubType;

public class IssueSubTypeCommand implements java.io.Serializable{
	
	private String id;
	private String name;
	private String desc;
	
	public IssueSubTypeCommand(IssueSubType src) {
		
		this.id  = src.getIssueSubTypeId()!= null ? src.getIssueSubTypeId(): "";
		this.name = src.getIssueSubTypeName()!=null
								    ? src.getIssueSubTypeName():"";
		this.desc = src.getIssueSubTypeDescription() != null 
									? src.getIssueSubTypeDescription() : "";
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
