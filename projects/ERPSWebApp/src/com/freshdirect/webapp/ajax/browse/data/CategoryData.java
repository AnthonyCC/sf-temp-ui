package com.freshdirect.webapp.ajax.browse.data;

import org.apache.commons.lang.StringEscapeUtils;


public class CategoryData extends BasicData {

	private static final long serialVersionUID = 6420471260944576323L;
	private String image;
	private String globalNavPostNameImage;
	private String globalNavShortName;
	private boolean available = true;
	
	public CategoryData(String image, String id, String name, String globalNavPostNameImage) {
		super(id, name);
		this.image = image;
		this.globalNavPostNameImage = globalNavPostNameImage;
		this.globalNavShortName = StringEscapeUtils.unescapeHtml(name).length() > 21 ? StringEscapeUtils.escapeHtml(StringEscapeUtils.unescapeHtml(name).substring(0, 18)) + "..." : name;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getGlobalNavPostNameImage() {
		return globalNavPostNameImage;
	}

	public void setGlobalNavPostNameImage(String globalNavPostNameImage) {
		this.globalNavPostNameImage = globalNavPostNameImage;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getGlobalNavShortName() {
		return globalNavShortName;
	}

	public void setGlobalNavShortName(String globalNavShortName) {
		this.globalNavShortName = globalNavShortName;
	}
	
}
