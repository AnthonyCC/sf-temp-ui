package com.freshdirect.framework.mail;

import java.util.Collection;
import java.io.Serializable;

public interface EmailI extends Serializable {
	
	public String getRecipient();
	
	public EmailAddress getFromAddress();
	
	public String getSubject();
	
	public Collection<String> getCCList();
	
	public Collection<String> getBCCList();

}