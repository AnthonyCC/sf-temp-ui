package com.freshdirect.webapp.taglib.promotion;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetPromotionTag extends AbstractGetterTag {

	private String promotionId;

	public void setPromotionId(String promotionID) {
		this.promotionId = promotionID;
	}

	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		FDPromotionModel promotion = (FDPromotionModel) session.getAttribute(SessionName.EDIT_PROMOTION);
		/*
		 * Removed the check if the promotion requested for is the one in the session
		 * as this unnecessarily load the previous changes in the promotion details
		 * page even when the changes are not committed to DB.
		 * 
		 */
		if (promotion == null) {
			if (promotionId != null && !"".equals(promotionId)) {
				try{
					promotion = FDPromotionManager.loadPromotion(this.promotionId);
					session.setAttribute(SessionName.EDIT_PROMOTION, promotion);
				}catch(FDResourceException e){
					throw new JspException(e);
				}
			} else {
				promotion = new FDPromotionModel(new PrimaryKey());
			}
		}
		return promotion;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.fdstore.promotion.management.FDPromotionModel";
		}
	}
	
}