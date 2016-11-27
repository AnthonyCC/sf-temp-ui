package com.freshdirect.fdstore.aspects;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class ModifiedPromosAspect extends BaseAspect {

    public ModifiedPromosAspect() {
        super(new DebugMethodPatternPointCut("FDPromotionManagerSB\\.getModifiedOnlyPromos\\(java.util.Date\\)"));
    }

    
    @Override
    public void intercept(InvocationContext arg0) throws Exception {
        arg0.setReturnObject(getModifiedOnlyPromos((Date) arg0.getParamVals()[0])); 

    }


    protected List getModifiedOnlyPromos(Date date) {
        return Collections.EMPTY_LIST;
    }

}
