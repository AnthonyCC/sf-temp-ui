package com.freshdirect.cms.application.service.db;

import java.util.HashSet;
import java.util.Set;

public class TestClass {
	public TestClass() {}
	
	public TestClass(String id, String a,int b, String message) {
		this.id   = id;
		theString = a;
		theInt    = b;
		
		theOtherClass = new TestRelationshipClass(id,message);
		
		theSet.add(new TestRelationshipClass(id + "1",message+"1"));
		theSet.add(new TestRelationshipClass(id + "2",message+"2"));
		theSet.add(new TestRelationshipClass(id + "3",message+"3"));
	}
	
	private String id;
	private String theString;
	private int theInt;
	private TestRelationshipClass theOtherClass;
	

	public void setId(String a) {id = a;}
	public String getId() {return id;}
	public void setTheString(String a) {theString = a;}
	public String getTheString() {return theString;}
	public void setTheInt(int a) {theInt = a;}
	public int getTheInt() {return theInt;}

	public TestRelationshipClass getTheOtherClass() {return theOtherClass;}
	public void setTheOtherClass(TestRelationshipClass theOtherClass) {this.theOtherClass = theOtherClass;}
	
	public Set theSet = new HashSet();
	public Set getTheSet() {return theSet;}
	public void setTheSet(Set theSet) {this.theSet = theSet;}
};
