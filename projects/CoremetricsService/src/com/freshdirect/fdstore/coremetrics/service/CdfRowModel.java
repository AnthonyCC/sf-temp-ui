package com.freshdirect.fdstore.coremetrics.service;

import java.io.Serializable;

public class CdfRowModel implements Serializable {
	private static final long serialVersionUID = -7000720031923667576L;
	public static final String DELIMITER = ",";
	public static final String CAT_NAME_QUOTE = "\"";
	private String categoryId;
	private String categoryName;
	private String parentCategoryId;

	private String clientId;
	
	public CdfRowModel(String clientId, String categoryId, String categoryName, String parentCategoryId) {		
		this.clientId = clientId;
		this.categoryId = categoryId == null ? "" : categoryId;
		this.categoryName = categoryName == null ? "" : categoryName;
		this.parentCategoryId = parentCategoryId == null ? "" : parentCategoryId;
	}

	public void prefixCategoryId(String prefix) {
		if (prefix != null) {
			categoryId = prefix + "_" + categoryId;
			if (parentCategoryId != null && parentCategoryId.length() > 0)
				parentCategoryId = prefix + "_" + parentCategoryId;
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(clientId).append(DELIMITER);
		sb.append(categoryId.toUpperCase()).append(DELIMITER);
		sb.append(CAT_NAME_QUOTE).append(categoryName.toUpperCase()).append(CAT_NAME_QUOTE).append(DELIMITER);
		sb.append(parentCategoryId.toUpperCase());
		return sb.toString();
	}
}
