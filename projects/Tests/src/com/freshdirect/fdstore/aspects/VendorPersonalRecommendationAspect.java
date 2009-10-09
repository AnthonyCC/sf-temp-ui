package com.freshdirect.fdstore.aspects;

import java.util.List;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class VendorPersonalRecommendationAspect extends BaseAspect {

    public VendorPersonalRecommendationAspect() {
        super(new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getPersonalRecommendations\\(java.lang.String,java.lang.String\\)"));
    }

    public void intercept(InvocationContext ctx) throws Exception {
        Object[] objects = ctx.getParamVals();
        ctx.setReturnObject(getRecommendation((String) (objects[0]), (String) objects[1]));
    }

    public abstract List getRecommendation(String recommender, String customerId);

}
