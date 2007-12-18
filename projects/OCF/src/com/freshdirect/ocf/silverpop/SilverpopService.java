/**
 * @author ekracoff
 * Created on Jun 7, 2005*/

package com.freshdirect.ocf.silverpop;


public class SilverpopService implements SilverpopServiceI {
	private String url;
	private String ftp;
	private String username;
	private String password;
	private String basePath;
	private int timeout;
	private int importTimeout;
	
	private boolean printXml;
	private boolean allowExternal;
	
	private boolean appendTimeStamp;
	
	
	public String getFtp() {
		return ftp;
	}
	
	public void setFtp(String ftpUrl) {
		this.ftp = ftpUrl;
	}
	
	public boolean isAllowExternal() {
		return allowExternal;
	}
	
	public void setAllowExternal(boolean internalOnly) {
		this.allowExternal = internalOnly;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isPrintXml() {
		return printXml;
	}
	
	public void setPrintXml(boolean printXml) {
		this.printXml = printXml;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String silverpopUrl) {
		this.url = silverpopUrl;
	}
	
	public String getBasePath() {
		return basePath;
	}
	
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getImportTimeoutMins() {
		return importTimeout;
	}
	
	public void setImportTimeoutMins(int importTimeout) {
		this.importTimeout = importTimeout;
	}

	public boolean isAppendTimeStamp() {
		return appendTimeStamp;
	}

	public void setAppendTimeStamp(boolean appendTimeStamp) {
		this.appendTimeStamp = appendTimeStamp;
	}

}
