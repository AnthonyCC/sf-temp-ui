package com.freshdirect.webapp.taglib.crm;

public interface CrmCustomerInfoI {
	
	public String getUserId();
	public void setUserId(String userId);
	
	public String getPassword();
	public void setPassword(String password);
	
	public String getPasswordHint();
	public void setPasswordHint(String passwordHint);
	
	public boolean isRecieveFdNews();
	public void setRecieveFdNews(boolean recieveNews);
	
	public boolean isTextOnlyEmail();
	public void setTextOnlyEmail(boolean textOnlyEmail);
	
	public boolean isReceiveOptinNewsletter();
	public void setReceiveOptinNewsletter(boolean receiveOptinNewsletter);

}
