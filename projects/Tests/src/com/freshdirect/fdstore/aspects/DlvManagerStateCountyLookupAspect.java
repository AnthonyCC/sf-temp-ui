package com.freshdirect.fdstore.aspects;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class DlvManagerStateCountyLookupAspect extends BaseAspect {

    public DlvManagerStateCountyLookupAspect() {
        super(new DebugMethodPatternPointCut("DlvManagerSessionBean\\.lookupStateCountyByZip\\(java.lang.String\\)"));
    }

    public void intercept(InvocationContext ctx) throws Exception {
        ctx.setReturnObject(lookupStateCountyByZip((String) ctx.getParamVals()[0]));
    }
    
    public StateCounty lookupStateCountyByZip(String zipcode) {
        StateCounty sc = new StateCounty("Wonderland", "Oz");
        return sc;
    }
    

}
