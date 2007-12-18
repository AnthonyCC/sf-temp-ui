package com.freshdirect.fdstore.promotion;

import java.io.Serializable;

public interface PromotionApplicatorI extends Serializable {

	public boolean apply(String promotionCode, PromotionContextI context);

}
