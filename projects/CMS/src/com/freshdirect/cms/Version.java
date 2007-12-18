/**
 * @author ekracoff
 * Created on Jan 18, 2005*/

package com.freshdirect.cms;

/**
 * Record of a content revision.
 * 
 * @TODO refactor to be in same packages as {@link com.freshdirect.cms.application.service.WebDavService}
 */
public class Version {

	private final String versionName;
	private final String userId;
	private final String lastModified;
	private final String url;

	public Version(String versionName, String userId, String lastModified, String url) {
		this.versionName = versionName;
		this.userId = userId;
		this.lastModified = lastModified;
		this.url = url;
	}

	public String getLastModified() {
		return lastModified;
	}

	public String getUserId() {
		return userId;
	}

	public String getVersionName() {
		return versionName;
	}

	public String getUrl() {
		return url;
	}

	/*	private Map properties = new HashMap();
	 
	 public List getPropertyNames(){
	 List names = new ArrayList();
	 for(Iterator i = properties.keySet().iterator(); i.hasNext();){
	 String name = (String) i.next();
	 names.add(name);
	 }
	 return names;
	 }
	 
	 public Map getProperties(){
	 return this.properties;
	 }
	 
	 public String getValue(String name){
	 return (String)properties.get(name);
	 }
	 
	 public void addProperty(String name, String value){
	 properties.put(name, value);
	 }*/

}
