package com.freshdirect.framework.mail;


import java.util.Date;

import com.freshdirect.framework.mail.EmailI;

public interface TEmailI extends EmailI {

	public String getEmailContent();
	
	public String getId();
	
	public String getOrderId();
	
	public String getCustomerId();
	
	public String getTemplateId();
	
	public String getEmailStatus();
	
	public String getEmailTransactionType();
	
	public String getEmailType();
	
	public String getProvider();
	
	public Date getCroModDate();
	
	public String getTargetProgId();
	
	public boolean isProductionReady();
	
	public String getOasQueryString();
	
}