package com.freshdirect.fdstore.aspects;

import java.util.Collections;
import java.util.List;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class FDPromotionManagerAspect extends BaseAspect {
    
    public FDPromotionManagerAspect() {
        super(new DebugMethodPatternPointCut("FDPromotionManagerSB\\.getAllAutomtaticPromotions\\(\\)"));
    }

    
    public List getAllAutomaticPromotions() {
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public void intercept(InvocationContext arg0) throws Exception {
        arg0.setReturnObject(getAllAutomaticPromotions());
    }

}
