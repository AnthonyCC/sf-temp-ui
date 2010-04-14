package com.freshdirect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.mockejb.EntityBeanDescriptor;
import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionManager;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.ClassPatternPointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerEntityBean;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.ejb.DlvManagerSessionBean;
import com.freshdirect.erp.ejb.ErpZoneInfoHome;
import com.freshdirect.erp.ejb.ErpZoneInfoSB;
import com.freshdirect.erp.ejb.ErpZoneInfoSessionBean;
import com.freshdirect.event.ejb.EventLoggerHome;
import com.freshdirect.event.ejb.EventLoggerSB;
import com.freshdirect.event.ejb.EventLoggerSessionBean;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.NullEventLogger;
import com.freshdirect.fdstore.customer.ProfileModel;
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
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerHome;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerSB;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerSessionBean;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.mail.ejb.MailerGatewaySessionBean;
import com.freshdirect.smartstore.ejb.DyfModelHome;
import com.freshdirect.smartstore.ejb.DyfModelSB;
import com.freshdirect.smartstore.ejb.DyfModelSessionBean;
import com.freshdirect.smartstore.ejb.ScoreFactorHome;
import com.freshdirect.smartstore.ejb.ScoreFactorSB;
import com.freshdirect.smartstore.ejb.ScoreFactorSessionBean;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSessionBean;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.smartstore.FeaturedItemsTag;
import com.mockrunner.mock.ejb.MockUserTransaction;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockPageContext;

public class TestUtils {

    final static Logger LOG = Logger.getLogger(TestUtils.class);
    
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
        FDUser user = new FDUser(new PrimaryKey(primaryKey)) {
			private static final long serialVersionUID = 426996618694995328L;

			@Override
            public void updateUserState() {
                LOG.info("No one can call updateUserState!");
                this.promotionEligibility=  new FDPromotionEligibility();
            }
            
            @Override
            public FDCustomerModel getFDCustomer() throws FDResourceException {
            	FDCustomerModel fdCust = new FDCustomerModel();
            	fdCust.setProfile(new ProfileModel());
            	return fdCust;
            }
        };
        user.setIdentity(new FDIdentity(erpCustomerId, fdCustomerId));
    	//Set dummy pricing context for zone pricing.
    	//user.setZipCode("11101");
    	//user.setSelectedServiceType(EnumServiceType.HOME);
        user.setDefaultPricingContext();
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

        
        SessionBeanDescriptor scoreFactServiceDesc = new SessionBeanDescriptor("freshdirect.smartstore.ScoreFactorHome", ScoreFactorHome.class, ScoreFactorSB.class,
                ScoreFactorSessionBean.class);
        container.deploy(scoreFactServiceDesc);

        SessionBeanDescriptor dlvAdminDesc = new SessionBeanDescriptor(FDStoreProperties.getDeliveryManagerHome(), DlvManagerHome.class, DlvManagerSB.class,
                DlvManagerSessionBean.class);
        container.deploy(dlvAdminDesc);
        
        //Added for zone pricing.
        SessionBeanDescriptor zoneInfoDesc = new SessionBeanDescriptor("freshdirect.erp.ZoneInfoManager", ErpZoneInfoHome.class, ErpZoneInfoSB.class,
                ErpZoneInfoSessionBean.class);
        container.deploy(zoneInfoDesc);
        
        // added for promotion manager
        container.deploy(new SessionBeanDescriptor("freshdirect.fdstore.PromotionManager", FDPromotionManagerHome.class, FDPromotionManagerSB.class, FDPromotionManagerSessionBean.class));
        

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

    public static FeaturedItemsTag createFeaturedItemsTag(MockPageContext ctx, String contentKey) {
        FeaturedItemsTag fit = new FeaturedItemsTag() {
            @Override
            protected void collectRequestId(HttpServletRequest request, Recommendations recommendations, FDUserI user) {
                LOG.info("collectRequestId called "+recommendations);
            }
        };
        fit.setPageContext(ctx);
        fit.setId("recommendations");
        fit.setItemCount(5);
        fit.setNoShuffle(true);
        fit.setCurrentNode(ContentFactory.getInstance().getContentNode(contentKey));
        return fit;
    }

    /**
     * Convert a Collection<ContentNodeModel> to Set<String>, where the result contains the ids of the nodes.
     * @param coll
     * @return
     */
    public static Set<String> convertToStringList(Collection coll) {
        Set<String> set = new HashSet<String>();
        for (Iterator iter = coll.iterator(); iter.hasNext();) {
            ContentNodeModel obj = (ContentNodeModel) iter.next();
            set.add(obj.getContentKey().getId());
        }
        return set;
    }
    
    
    public static String getId(List<ContentNodeModel> nodes, int pos) {
        return (nodes.get(pos)).getContentKey().getId();
    }
    
    
    public static XmlContentService initCmsManagerFromXmls(String xmlPath) {
        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(), xmlPath);

        CmsManager.setInstance(new CmsManager(service, null));
        
        return service;
    }
    
    public static void initFDStoreProperties() {
        FDStoreProperties.set(FDStoreProperties.SMARTSTORE_CACHE_ONLINE_FACTORS, "false");
        FDStoreProperties.setLastRefresh(System.currentTimeMillis()+ (1000*60*10));
    }
    

}
