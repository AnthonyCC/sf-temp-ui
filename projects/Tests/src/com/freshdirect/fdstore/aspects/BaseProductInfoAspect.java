package com.freshdirect.fdstore.aspects;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class BaseProductInfoAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(BaseProductInfoAspect.class);
        
    public BaseProductInfoAspect() {
        super(new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProductInfo\\(java.lang.String\\)"));
    }

    @Override
    public final void intercept(InvocationContext ctx) throws Exception {
        String sku = (String) ctx.getParamVals()[0];
        LOG.info("getProductInfo for "+sku);
        ctx.setReturnObject(getProductInfo(sku));
    }
    

    
    /**
     * Get current product information object for sku.
     *
     * @param sku SKU code
     *
     * @return FDProductInfo object
     *
     * @throws FDSkuNotFoundException if the SKU was not found in ERP services
     * @throws FDResourceException if an error occured using remote resources
     */
    public abstract FDProductInfo getProductInfo(String sku) throws RemoteException, FDSkuNotFoundException, FDResourceException;                   


}
