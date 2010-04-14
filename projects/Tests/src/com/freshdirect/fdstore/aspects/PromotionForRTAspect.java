package com.freshdirect.fdstore.aspects;

import java.sql.Timestamp;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.core.PrimaryKey;

public class PromotionForRTAspect extends BaseAspect {

    public PromotionForRTAspect() {
        super(new DebugMethodPatternPointCut("FDPromotionManagerSB\\.getPromotionForRT\\(java.lang.String\\)"));
    }

    
    public PromotionI getPromotionForRT(String promoCode) {
        return new Promotion(new PrimaryKey("non-existant"), EnumPromotionType.GIFT_CARD, promoCode, "promo name for "+promoCode, "description for "+promoCode, new Timestamp(System.currentTimeMillis() - 3600000));
    }
    
    @Override
    public void intercept(InvocationContext arg0) throws Exception {
        arg0.setReturnObject(getPromotionForRT((String) arg0.getParamVals()[0]));
    }

}
