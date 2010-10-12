package com.freshdirect.fdstore.aspects;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class FDProductAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(FDProductAspect.class);

    Map<String, FDProduct> products = new HashMap<String, FDProduct>();
    
    public FDProductAspect() {
        super(new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProduct\\(java.lang.String,int\\)"));
    }


    @Override
    public void intercept(InvocationContext ctx) throws Exception {
        String sku = (String) ctx.getParamVals()[0];
        LOG.info("getProduct for "+sku);

        ctx.setReturnObject(getFDProduct(sku));
    }

    public void addProduct(FDProduct product) {
        products.put(product.getSkuCode(), product);
    }

    protected FDProduct getFDProduct(String sku) {
        return products.get(sku);
    }
    
    

}
