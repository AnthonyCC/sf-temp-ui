package com.freshdirect.fdstore;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public enum EnumEStoreId {
		
	FD("FreshDirect"), FDX("FDX");
	
	private static Category LOGGER = LoggerFactory.getInstance(EnumEStoreId.class);
	private String contentKey;

	private EnumEStoreId(String contentKey) {
		this.contentKey = contentKey;
	}
	
	public String getContentKey() {
		return contentKey;
	}

	static EnumEStoreId valueOfContentKey(String contentKey){
		if (contentKey != null ){
			for (EnumEStoreId value : EnumEStoreId.values()){
				if (contentKey.equals(value.getContentKey())){
					return value;
				}
			}
		}		
		LOGGER.error("Cannot resolve EnumEStoreId with contentKey: " + contentKey);
		return null;
	}
}
