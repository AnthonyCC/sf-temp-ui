package com.freshdirect.cms.application.service.db;

public class TestRelationshipClass {
	private String message;
	private String id;
	
	public TestRelationshipClass() {}
	
	public TestRelationshipClass(String id,String message) {
		this.id = id;
		this.message = message;
	}
	
	public void setId(String a) {id = a;}
	public String getId() {return id;}
	public String getMessage() {return message;}
	public void setMessage(String message) {this.message = message;}
}
