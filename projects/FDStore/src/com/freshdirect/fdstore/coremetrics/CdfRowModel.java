package com.freshdirect.fdstore.coremetrics;

import com.freshdirect.fdstore.FDStoreProperties;

public class CdfRowModel {
	public static final String DELIMITER = ",";
	public static final String CAT_NAME_QUOTE = "\"";
	private String categoryId;
	private String categoryName;
	private String parentCategoryId;
	
	public CdfRowModel(String categoryId, String categoryName, String parentCategoryId) {
		this.categoryId = categoryId == null ? "" : categoryId;
		this.categoryName = categoryName == null ? "" : categoryName;
		this.parentCategoryId = parentCategoryId == null ? "" : parentCategoryId;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(FDStoreProperties.getCoremetricsClientId()).append(DELIMITER);
		sb.append(categoryId.toUpperCase()).append(DELIMITER);
		sb.append(CAT_NAME_QUOTE).append(categoryName.toUpperCase()).append(CAT_NAME_QUOTE).append(DELIMITER);
		sb.append(parentCategoryId.toUpperCase());
		return sb.toString();
	}
}
