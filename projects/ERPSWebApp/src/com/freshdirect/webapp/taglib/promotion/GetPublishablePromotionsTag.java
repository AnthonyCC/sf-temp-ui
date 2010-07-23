package com.freshdirect.webapp.taglib.promotion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;

public class GetPublishablePromotionsTag extends AbstractPromotionGetterTag {
	private static final long serialVersionUID = 9182589305181000638L;

	@Override
	protected List<PromoNewRow> getResult() throws Exception {
		List<FDPromotionNewModel> promotions = FDPromotionNewManager.loadPublishablePromotions();
		
		// Convert promos to promo rows
		List<PromoNewRow> promoRows = toRows(promotions);

		// And sort them
		sortRows(promoRows, (HttpServletRequest)pageContext.getRequest());
		
		return promoRows;
	}
}
