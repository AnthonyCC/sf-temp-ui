package com.freshdirect.webapp.taglib.coremetrics;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.service.VariantRegistry;


public class CmMarketingLinkUtil {

	private static final String ATTR_DELIMITER = "-_-";
	public static final String OFFSITE_PARAMETER_NAME = "cm_mmc";
	public static final String ONSITE_PARAMETER_NAME = "cm_sp";
	
	public static String getAdd2AnyLink(String url){
		return StringUtil.addParameterToUrl(url, OFFSITE_PARAMETER_NAME, getOffsiteParameter("fd_social","social_share","social_share",StringUtil.encodeUrl(url)));
	}

	public static String getSocialLink(String url, String placement, String offsiteItem){
		return StringUtil.addParameterToUrl(url, OFFSITE_PARAMETER_NAME, getOffsiteParameter("fd_social","social_share", placement, offsiteItem));
	}

	
	public static String getOffsiteParameter(String vendor, String category, String placement, String item){
		return vendor + ATTR_DELIMITER + category + ATTR_DELIMITER + placement + ATTR_DELIMITER + item;
	}
	
	public static String getOnsiteParameter(String promotionType, String promotion, String linkLevel){
		return promotionType + ATTR_DELIMITER + promotion + ATTR_DELIMITER + linkLevel;
	}

	public static String getSmartStoreLink(String url, Recommendations recommendations){
		String variant = recommendations.getVariant().getId();
		String siteFeature = VariantRegistry.getInstance().getService(variant).getSiteFeature().getName();
		return StringUtil.addParameterToUrl(url, ONSITE_PARAMETER_NAME, getOnsiteParameter("smartstore",siteFeature,variant));
	}

}
