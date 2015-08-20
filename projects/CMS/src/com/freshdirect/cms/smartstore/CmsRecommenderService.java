package com.freshdirect.cms.smartstore;

import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.ZoneInfo;

public interface CmsRecommenderService extends java.io.Serializable {
	List<String> recommendNodes(ContentKey recommenderId, ContentKey categoryId, ZoneInfo zoneInfo);
}
