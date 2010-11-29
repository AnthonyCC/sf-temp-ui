package com.freshdirect.webapp.taglib.promotion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetWSPromotionTag extends AbstractGetterTag {

	@Override
	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		List<WSPromotionInfo> infos = FDPromotionNewManager.getWSPromotionInfos();
		return infos;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
	
}
