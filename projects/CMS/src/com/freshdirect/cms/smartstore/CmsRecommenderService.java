package com.freshdirect.cms.smartstore;

import java.util.List;

public interface CmsRecommenderService extends java.io.Serializable {
	List<String> recommendNodes(String recommenderId, String categoryId, String zoneId);
}
