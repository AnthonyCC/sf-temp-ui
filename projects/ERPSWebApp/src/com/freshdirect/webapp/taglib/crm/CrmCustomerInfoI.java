package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.common.address.PhoneNumber;

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
	
	public PhoneNumber getMobileNumber();
	public void setMobileNumber(PhoneNumber mobileNumber);

	public boolean isDelNotification();	
	public void setDelNotification(boolean delNotification);

	public boolean isOffNotification();
	public void setOffNotification(boolean offNotification);

	public boolean isGoGreen();
	public void setGoGreen(boolean goGreen);

	public boolean isNoThanks();
	public void setNoThanks(boolean noThanks);

}
