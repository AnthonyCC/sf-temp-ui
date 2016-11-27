package com.freshdirect.webapp.taglib.promotion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetPromotionNewTag extends AbstractGetterTag {

	private String promotionId;

	public void setPromotionId(String promotionID) {
		this.promotionId = promotionID;
	}
	@Override
	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		FDPromotionNewModel promotion = new FDPromotionNewModel();
		if(null != promotionId && !"".equals(promotionId.trim())){
			promotion = FDPromotionNewManager.loadPromotion(promotionId);
			promotion.setAuditChanges(FDPromotionNewManager.loadPromoAuditChanges(promotion.getId()));
		}		
		return promotion;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.fdstore.promotion.management.FDPromotionNewModel";
		}
	}
	
}
