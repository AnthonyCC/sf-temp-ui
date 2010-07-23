package com.freshdirect.fdstore.promotion.management;

import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.framework.core.ModelSupport;

public class FDPromoContentModel extends ModelSupport {
	
	private String promotionId;
	private EnumDCPDContentType contentType;
	private String contentId;
	private boolean excluded;
	
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	public EnumDCPDContentType getContentType() {
		return contentType;
	}
	public void setContentType(EnumDCPDContentType contentType) {
		this.contentType = contentType;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public boolean isExcluded() {
		return excluded;
	}
	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}

}
