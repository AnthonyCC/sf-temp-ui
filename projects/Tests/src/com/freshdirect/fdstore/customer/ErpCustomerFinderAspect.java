/**
 * 
 */
package com.freshdirect.fdstore.customer;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.mockejb.GenericHome;
import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;

class ErpCustomerFinderAspect implements Aspect {

    ErpCustomerInfoModel customerInfo;

    public ErpCustomerFinderAspect(ErpCustomerInfoModel customerInfo) {
        this.customerInfo = customerInfo;
    }

    public void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
        this.customerInfo = customerInfo;
    }

    /**
     * Intercept findByName method.
     */
    public Pointcut getPointcut() {
        // Note that we are intecepting target method on the bean
        // as opposed to the interface method. Unlike in CMP case, we can do it
        // because BMP entities have defined finder methods.
        return new MethodPatternPointcut("MockErpCustomerEntityBean\\.ejbFindByPrimaryKey");
    }

    public void intercept(InvocationContext invocationContext) throws Exception {
        Object[] paramVals = invocationContext.getParamVals();

        // now create
        invocationContext.setReturnObject(create((PrimaryKey) paramVals[0]));
        // We don't need to proceed to the next interceptor since we're done
        // with the finder
    }

    /**
     * Creates Person entity using "genericCreate" method which creates an
     * instance of an entity without calling the actual "ejbCreate"
     */
    private ErpCustomerEB create(PrimaryKey pk) throws Exception {

        Context context = new InitialContext();
        GenericHome home = (GenericHome) context.lookup(FDStoreProperties.getErpCustomerHome());

        ErpCustomerEB erpCustomer = (ErpCustomerEB) home.genericCreate();
        erpCustomer.setCustomerInfo(customerInfo);
        return erpCustomer;
    }

}