package com.freshdirect.fdstore.customer;

import javax.ejb.EJBLocalHome;
import javax.naming.Context;

import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.TestUtils;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.fdstore.FDCachedFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;

/**
 * Supporting base class for more advanced EJB testcases using a mock EJB
 * container.
 */
public abstract class FDCustomerManagerTestSupport extends DbTestCaseSupport {

    public FDCustomerManagerTestSupport(String name) {
        super(name);
    }

    ContentServiceI                 service;
    protected Context               context;
    protected MockContainer         container;
    protected AspectSystem          aspectSystem;

    MockUserTransaction             mockTransaction;
    private ErpCustomerFinderAspect customerInfoAspect;

    public void setUp() throws Exception {

        context = TestUtils.createContext();
        dbUnitSetUp(context);
        dbUnitRegisterPool(context);

        container = TestUtils.createMockContainer(context);

        mockTransaction = TestUtils.createTransaction(context);

        aspectSystem = TestUtils.createAspectSystem();

        aspectSystem.add(customerInfoAspect = new ErpCustomerFinderAspect(null));
        FDCachedFactory.mockInstances();
    }
    
    

    public void tearDown() throws Exception {
        dbUnitTearDown(null);
        MockContextFactory.revertSetAsInitial();
    }

    protected void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
        this.customerInfoAspect.setCustomerInfo(customerInfo);
    }

    public static interface MockErpCustomerHome extends ErpCustomerHome, EJBLocalHome {
    }
}
