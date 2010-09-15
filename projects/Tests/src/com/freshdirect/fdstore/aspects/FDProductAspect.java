package com.freshdirect.fdstore.aspects;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class FDProductAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(FDProductAspect.class);

    Map<String, FDProduct> products = new HashMap<String, FDProduct>();
    
    public FDProductAspect() {
        super(new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProduct\\(java.lang.String,int\\)"));
    }


    @Override
    public final void intercept(InvocationContext ctx) throws Exception {
        String sku = (String) ctx.getParamVals()[0];
        int version = (Integer) ctx.getParamVals()[1];
        LOG.info("getProduct for "+sku);

        ctx.setReturnObject(getProduct(sku, version));
    }

    public void addProduct(FDProduct product) {
        products.put(product.getSkuCode(), product);
    }

    protected FDProduct getProduct(String sku, int version) throws RemoteException, FDSkuNotFoundException, FDResourceException {
        return products.get(sku);
    }
    
}
