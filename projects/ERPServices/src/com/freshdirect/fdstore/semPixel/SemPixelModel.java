package com.freshdirect.fdstore.semPixel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.core.ModelSupport;

public class SemPixelModel extends ModelSupport {

	private static final long serialVersionUID = 4667560976937506670L;
	
	private String name;
	private boolean enabled;
	private List<String> validReferers;
	private List<String> validZipCodes;
	private String mediaPath;
	private Date lastModifiedDate;
    private Map<String, String> params = new HashMap<String, String>();
	
	public SemPixelModel() {
        super();
        setName(null);
        setEnabled(false);
        setValidReferers(new ArrayList<String>());
        setValidZipCodes(new ArrayList<String>());
        setMediaPath("");
        setLastModifiedDate(new Date());
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setValidReferers(List<String> validReferers) {
		this.validReferers = validReferers;
	}
	
	public void addValidReferer(String validReferer) {
		this.validReferers.add(validReferer);
	}

	public List<String> getValidReferers() {
		return validReferers;
	}

	public void setValidZipCodes(List<String> validZipCodes) {
		this.validZipCodes = validZipCodes;
	}
	
	public void addValidZipCode(String validZipCode) {
		this.validZipCodes.add(validZipCode);
	}

	public List<String> getValidZipCodes() {
		return validZipCodes;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setLastModifiedDate(Date lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public Date getLastModifiedDate(){
		return this.lastModifiedDate;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParam(String key, String value) {
		params.put(key, value);
	}
	
	public String getParam(String key) {
		return params.get(key);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("SemPixelModel[");
			buf.append("\n\t").append(this.getName());
			buf.append("\n\t").append(this.isEnabled());
			buf.append("\n\t").append(this.getValidReferers());
			buf.append("\n\t").append(this.getValidZipCodes());
			buf.append("\n\t").append(this.getMediaPath());
			buf.append("\n\t").append(this.getLastModifiedDate());
			buf.append("\n\t").append(this.getParams());
		buf.append("\n]");
		return buf.toString();
	}
	
}