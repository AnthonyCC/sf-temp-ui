/**
 * 
 * WebHowToCookIt.java
 * Created Dec 12, 2002
 */
package com.freshdirect.fdstore.content.view;

/**
 *
 *  @author knadeem
 */
public class WebHowToCookIt {
	
	private String name;
	private String linkParams;
	
	public WebHowToCookIt(String name, String linkParams){
		this.name = name;
		this.linkParams = linkParams;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLinkParams(){
		return this.linkParams;
	}
	
	public void setLinkParams(String linkParams){
		this.linkParams = linkParams;
	}

}
