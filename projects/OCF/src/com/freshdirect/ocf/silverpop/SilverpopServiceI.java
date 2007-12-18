/**
 * @author ekracoff
 * Created on Jul 5, 2005*/

package com.freshdirect.ocf.silverpop;

public interface SilverpopServiceI {
	public String getFtp();

	public void setFtp(String ftpUrl);

	public boolean isAllowExternal();

	public void setAllowExternal(boolean allowExternal);

	public String getPassword();

	public void setPassword(String password);

	public boolean isPrintXml();

	public void setPrintXml(boolean printXml);

	public String getUrl();

	public void setUrl(String silverpopUrl);

	public String getBasePath();

	public void setBasePath(String basePath);

	public int getTimeout();

	public void setTimeout(int timeout);

	public String getUsername();

	public void setUsername(String username);
	
	public int getImportTimeoutMins();
	
	public void setImportTimeoutMins(int timeout);
	
	public boolean isAppendTimeStamp();

	public void setAppendTimeStamp(boolean appendTimeStamp); 

}