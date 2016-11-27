package com.freshdirect.fdstore.aspects;

import java.util.List;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class VendorRecommendationAspect extends BaseAspect {

    public VendorRecommendationAspect() {
        super(new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getProductRecommendations\\(java.lang.String,com.freshdirect.cms.ContentKey\\)"));
    }


    public void intercept(InvocationContext ctx) throws Exception {
        Object[] objects = ctx.getParamVals();
        ctx.setReturnObject(getRecommendation((String) (objects[0]), (ContentKey) objects[1]));
    }

    public abstract List getRecommendation(String recommender, ContentKey contentKey);

}
