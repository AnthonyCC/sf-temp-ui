package com.freshdirect.fdstore.customer;

import java.util.Hashtable;

import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.NamingException;

import org.mockejb.EntityBeanDescriptor;
import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionManager;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.ClassPatternPointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.TestUtils;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ErpCustomerEntityBean;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.event.ejb.EventLoggerHome;
import com.freshdirect.event.ejb.EventLoggerSB;
import com.freshdirect.event.ejb.EventLoggerSessionBean;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSessionBean;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDFactorySB;
import com.freshdirect.fdstore.ejb.FDFactorySessionBean;
import com.freshdirect.fdstore.lists.ejb.FDListManagerHome;
import com.freshdirect.fdstore.lists.ejb.FDListManagerSB;
import com.freshdirect.fdstore.lists.ejb.FDListManagerSessionBean;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.mail.ejb.MailerGatewaySessionBean;
import com.freshdirect.smartstore.ejb.DyfModelHome;
import com.freshdirect.smartstore.ejb.DyfModelSB;
import com.freshdirect.smartstore.ejb.DyfModelSessionBean;
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
