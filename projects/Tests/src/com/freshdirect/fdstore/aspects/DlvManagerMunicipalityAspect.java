package com.freshdirect.fdstore.aspects;

import java.util.Collections;
import java.util.List;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class DlvManagerMunicipalityAspect extends BaseAspect {

    List municipalityInfo;
    
    
    public DlvManagerMunicipalityAspect() {
        this(Collections.EMPTY_LIST);
    }
    
    public DlvManagerMunicipalityAspect(List municipalityInfo) {
        super(new DebugMethodPatternPointCut("DlvManagerSessionBean\\.getMunicipalityInfos\\(\\)"));
        this.municipalityInfo = municipalityInfo;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mockejb.interceptor.Interceptor#intercept(org.mockejb.interceptor
     * .InvocationContext)
     */
    public void intercept(InvocationContext ctx) throws Exception {
        ctx.setReturnObject(municipalityInfo);
    }


}
