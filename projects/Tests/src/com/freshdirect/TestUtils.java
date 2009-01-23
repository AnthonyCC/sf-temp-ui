package com.freshdirect;

import java.util.Hashtable;

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

import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerEntityBean;
import com.freshdirect.event.ejb.EventLoggerHome;
import com.freshdirect.event.ejb.EventLoggerSB;
import com.freshdirect.event.ejb.EventLoggerSessionBean;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.NullEventLogger;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport.MockErpCustomerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSessionBean;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDFactorySB;
import com.freshdirect.fdstore.ejb.FDFactorySessionBean;
import com.freshdirect.fdstore.lists.ejb.FDListManagerHome;
import com.freshdirect.fdstore.lists.ejb.FDListManagerSB;
import com.freshdirect.fdstore.lists.ejb.FDListManagerSessionBean;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.mail.ejb.MailerGatewaySessionBean;
import com.freshdirect.smartstore.ejb.DyfModelHome;
import com.freshdirect.smartstore.ejb.DyfModelSB;
import com.freshdirect.smartstore.ejb.DyfModelSessionBean;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSessionBean;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.mockrunner.mock.ejb.MockUserTransaction;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockPageContext;

public class TestUtils {

    public static MockPageContext createMockPageContext(FDUserI user) {
        MockPageContext ctx = new MockPageContext();
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        session.setAttribute(SessionName.USER, user);
        ctx.setServletRequest(request);
        return ctx;
    }

    public static FDUser createUser(String primaryKey, String erpCustomerId, String fdCustomerId) {
        FDUser user = new FDUser(new PrimaryKey(primaryKey));
        user.setIdentity(new FDIdentity(erpCustomerId, fdCustomerId));
        return user;
    }

    public static MockContainer createMockContainer(Context context) throws NamingException {
        MockContainer container = new MockContainer(context);

        SessionBeanDescriptor listManagerDesc = new SessionBeanDescriptor(FDListManagerHome.JNDI_HOME, FDListManagerHome.class, FDListManagerSB.class,
                FDListManagerSessionBean.class);
        container.deploy(listManagerDesc);

        SessionBeanDescriptor customerManagerDesc = new SessionBeanDescriptor(FDStoreProperties.getFDCustomerManagerHome(), FDCustomerManagerHome.class,
                FDCustomerManagerSB.class, FDCustomerManagerSessionBean.class);
        container.deploy(customerManagerDesc);

        SessionBeanDescriptor mailerGWDesc = new SessionBeanDescriptor("freshdirect.mail.MailerGateway", MailerGatewayHome.class, MailerGatewaySB.class,
                MailerGatewaySessionBean.class);
        container.deploy(mailerGWDesc);

        EntityBeanDescriptor erpCustDesc = new EntityBeanDescriptor(FDStoreProperties.getErpCustomerHome(), MockErpCustomerHome.class, ErpCustomerEB.class,
                ErpCustomerEntityBean.class);
        container.deploy(erpCustDesc);

        SessionBeanDescriptor fdFactoryDesc = new SessionBeanDescriptor(FDStoreProperties.getFDFactoryHome(), FDFactoryHome.class, FDFactorySB.class,
                FDFactorySessionBean.class);
        container.deploy(fdFactoryDesc);

        SessionBeanDescriptor fdEventLoggerDesc = new SessionBeanDescriptor("freshdirect.event.EventLogger", EventLoggerHome.class, EventLoggerSB.class,
                EventLoggerSessionBean.class);
        container.deploy(fdEventLoggerDesc);

        SessionBeanDescriptor dyfModelDesc = new SessionBeanDescriptor("freshdirect.smartstore.DyfModelHome", DyfModelHome.class, DyfModelSB.class,
                DyfModelSessionBean.class);
        container.deploy(dyfModelDesc);

        SessionBeanDescriptor ssServiceDesc = new SessionBeanDescriptor("freshdirect.smartstore.SmartStoreServiceConfiguration", SmartStoreServiceConfigurationHome.class, SmartStoreServiceConfigurationSB.class,
                SmartStoreServiceConfigurationSessionBean.class);
        container.deploy(ssServiceDesc);
        
        return container;
    }

    public static Context createContext() throws NamingException {
        Hashtable env = new Hashtable();

        // set the context factory to the mockejb context factory
        FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

        env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL());
        env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory());

        MockContextFactory.setAsInitial();
        MockContextFactory mockContextFactory = new MockContextFactory();
        return mockContextFactory.getInitialContext(env);
    }
    
    public static MockUserTransaction createTransaction(Context context) throws NamingException {
        MockUserTransaction mockTransaction = new MockUserTransaction();
        context.rebind("javax.transaction.UserTransaction", mockTransaction);
        return mockTransaction;
    }
        
    
    public static AspectSystem createAspectSystem() {
        /* Set up Aspect System to handle interceptors */
        AspectSystem aspectSystem = AspectSystemFactory.getAspectSystem();

        aspectSystem.add(new ClassPatternPointcut("com.freshdirect.fdstore"), new TransactionManager(TransactionPolicy.REQUIRED));
        aspectSystem.add(new NullEventLogger());
        return aspectSystem;
    }

    

}
