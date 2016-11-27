package com.freshdirect.mobileapi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;

public class BannerService {

	private static final ConcurrentMap<String, BannerModel> ALL_BANNERS = new ConcurrentHashMap<String, BannerModel>();
	private static final ConcurrentMap<String, BannerModel> BANNERS_FOR_ENTITIES = new ConcurrentHashMap<String, BannerModel>();
			
	public static BannerModel bannerFor(String id) {
		if (ALL_BANNERS.size() == 0) {
			loadBanners();
		}
		return BANNERS_FOR_ENTITIES.get(id);
	}

	private static void loadBanners() {
		if (ALL_BANNERS.size() == 0) {
	        Set<ContentKey> bannerKeys = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
	        Map<String, BannerModel> banners = new HashMap<String, BannerModel>(bannerKeys.size() * 2);
	        Map<String, BannerModel> bannersForEntities = new HashMap<String, BannerModel>(bannerKeys.size() * 2); 
	        for (ContentKey key : bannerKeys) {
	            BannerModel banner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
                banners.put(banner.getContentName(), banner);
                final ContentNodeModel link = banner.getLink();
                if (link != null) {
                    bannersForEntities.put(link.getContentName(), banner);
                }
	        }
	        if (ALL_BANNERS.size() == 0) {
	        	ALL_BANNERS.putAll(banners);
	        }
	        if (BANNERS_FOR_ENTITIES.size() == 0) {
	        	BANNERS_FOR_ENTITIES.putAll(bannersForEntities);
	        }
		}
	}
}
