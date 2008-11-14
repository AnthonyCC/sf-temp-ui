package com.freshdirect.fdstore.customer;

import java.util.Hashtable;

import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.mockejb.EntityBeanDescriptor;
import org.mockejb.GenericHome;
import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionManager;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.ClassPatternPointcut;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;
import org.mockejb.interceptor.Pointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerEntityBean;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.event.ejb.EventLoggerHome;
import com.freshdirect.event.ejb.EventLoggerSB;
import com.freshdirect.event.ejb.EventLoggerSessionBean;
import com.freshdirect.fdstore.FDStoreProperties;
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
import com.mockrunner.mock.ejb.MockUserTransaction;

/**
 * Supporting base class for more advanced EJB testcases using a mock EJB container.
 */
public abstract class FDCustomerManagerTestSupport extends DbTestCaseSupport {

	public FDCustomerManagerTestSupport(String name) {
		super(name);
	}

	ContentServiceI service;
	protected Context context;
	protected MockContainer container;
	protected AspectSystem aspectSystem;
	
	MockUserTransaction mockTransaction;
	private ErpCustomerInfoModel customerInfo;

	public void setUp() throws Exception {
		
		/* Set up Aspect System to handle interceptors */
		aspectSystem = AspectSystemFactory.getAspectSystem();
						
		Hashtable env = new Hashtable();

		// set the context factory to the mockejb context factory
		FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

		env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory() );
		
		MockContextFactory.setAsInitial();
		MockContextFactory mockContextFactory = new MockContextFactory();		
		context = mockContextFactory.getInitialContext(env);
		dbUnitSetUp(context);
		dbUnitRegisterPool(context);

		container = new MockContainer(context);

		SessionBeanDescriptor listManagerDesc = new SessionBeanDescriptor(FDListManagerHome.JNDI_HOME, FDListManagerHome.class, FDListManagerSB.class, FDListManagerSessionBean.class);
		container.deploy(listManagerDesc);

		SessionBeanDescriptor customerManagerDesc = new SessionBeanDescriptor(FDStoreProperties.getFDCustomerManagerHome(), FDCustomerManagerHome.class, FDCustomerManagerSB.class, FDCustomerManagerSessionBean.class);
		container.deploy(customerManagerDesc);

		SessionBeanDescriptor mailerGWDesc = new SessionBeanDescriptor("freshdirect.mail.MailerGateway", MailerGatewayHome.class, MailerGatewaySB.class, MailerGatewaySessionBean.class);
		container.deploy(mailerGWDesc);
				
		EntityBeanDescriptor erpCustDesc = new EntityBeanDescriptor(FDStoreProperties.getErpCustomerHome(), MockErpCustomerHome.class, ErpCustomerEB.class, ErpCustomerEntityBean.class);
		container.deploy(erpCustDesc);
		
		SessionBeanDescriptor fdFactoryDesc = new SessionBeanDescriptor(FDStoreProperties.getFDFactoryHome(), FDFactoryHome.class, FDFactorySB.class, FDFactorySessionBean.class);
		container.deploy(fdFactoryDesc);

		SessionBeanDescriptor fdEventLoggerDesc = new SessionBeanDescriptor("freshdirect.event.EventLogger", EventLoggerHome.class, EventLoggerSB.class, EventLoggerSessionBean.class);
		container.deploy(fdEventLoggerDesc);

                SessionBeanDescriptor dyfModelDesc = new SessionBeanDescriptor("freshdirect.smartstore.DyfModelHome", DyfModelHome.class, DyfModelSB.class, DyfModelSessionBean.class);
                container.deploy(dyfModelDesc);

		
		mockTransaction = new MockUserTransaction(); 
		context.rebind("javax.transaction.UserTransaction", mockTransaction);

		aspectSystem.add( new ClassPatternPointcut("com.freshdirect.fdstore"), 
	            new TransactionManager( TransactionPolicy.REQUIRED ));	
		
		aspectSystem.add(new ErpCustomerFinderAspect());
		aspectSystem.add(new NullEventLogger());
	}
	
	
	public void tearDown() throws Exception {
		dbUnitTearDown(null);
		MockContextFactory.revertSetAsInitial();
	}

	   class ErpCustomerFinderAspect implements Aspect {

		/**
		 * Intercept findByName method. 
		 */
		public Pointcut getPointcut() {
			// Note that we are intecepting target method on the bean 
			// as opposed to the interface method. Unlike in CMP case, we can do it 
			// because BMP entities have defined finder methods. 
			return new MethodPatternPointcut("MockErpCustomerEntityBean\\.ejbFindByPrimaryKey");
		}

		public void intercept(InvocationContext invocationContext)
				throws Exception {
			Object[] paramVals = invocationContext.getParamVals();

			// now create 
			invocationContext.setReturnObject(create((PrimaryKey) paramVals[0]));
			// We don't need to proceed to the next interceptor since we're done with the finder
		}

		/**
		 * Creates Person entity using "genericCreate" method which creates an
		 * instance of an entity without calling the actual "ejbCreate" 
		 */
		private ErpCustomerEB create(PrimaryKey pk)
				throws Exception {

			Context context = new InitialContext();
			GenericHome home = (GenericHome) context.lookup(FDStoreProperties.getErpCustomerHome());

			ErpCustomerEB erpCustomer = (ErpCustomerEB) home.genericCreate();
			erpCustomer.setCustomerInfo(customerInfo);
			return erpCustomer;
		}

	}
	   
	   protected void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
		   this.customerInfo = customerInfo;
	   }

	   public static interface MockErpCustomerHome extends ErpCustomerHome, EJBLocalHome {
	   }
	   
	   public static class NullEventLogger implements Aspect {

		public Pointcut getPointcut() {
			return new DebugMethodPatternPointCut("EventLoggerSessionBean\\.log");
		}

		public void intercept(InvocationContext ctx) throws Exception {
			// Simply intercept the call and do nothing
		}
		   
	   }
}

