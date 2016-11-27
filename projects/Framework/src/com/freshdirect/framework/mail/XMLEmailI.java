package com.freshdirect.framework.mail;

public interface XMLEmailI extends EmailI {
	
	public String getXslPath();
	
	public String getXML();
	
	public boolean isHtmlEmail();

}
