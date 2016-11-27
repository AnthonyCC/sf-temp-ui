package com.freshdirect.fdstore;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum EnumEStoreId {
		
	FD("FreshDirect"), FDX("FDX");
	
	private static Category LOGGER = LoggerFactory.getInstance(EnumEStoreId.class);

	/**
	 * CMS Content ID
	 */
	private final String contentId;

	private EnumEStoreId(String contentKey) {
		this.contentId = contentKey;
	}
	
	/**
	 * @return the ID part of the corresponding CMS content key
	 * @see {@link FDContentTypes#STORE}
	 * @see {#link ContentKey}
	 */
	public String getContentId() {
		return contentId;
	}

	public static EnumEStoreId valueOfContentId(String contentId){
		if (contentId != null ){
			for (EnumEStoreId value : EnumEStoreId.values()){
				if (contentId.equals(value.getContentId())){
					return value;
				}
			}
		}		
		LOGGER.error("Cannot resolve EnumEStoreId with contentKey: " + contentId);
		return null;
	}
}
