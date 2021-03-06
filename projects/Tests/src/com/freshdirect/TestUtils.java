package com.freshdirect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.search.ContentIndex;
import com.freshdirect.cms.search.SearchTestUtils;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerEntityBean;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.ejb.DlvManagerSessionBean;
import com.freshdirect.deliverypass.ejb.DlvPassManagerHome;
import com.freshdirect.deliverypass.ejb.DlvPassManagerSB;
import com.freshdirect.deliverypass.ejb.DlvPassManagerSessionBean;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.ejb.ErpInfoSessionBean;
import com.freshdirect.erp.ejb.ErpZoneInfoHome;
import com.freshdirect.erp.ejb.ErpZoneInfoSB;
import com.freshdirect.erp.ejb.ErpZoneInfoSessionBean;
import com.freshdirect.event.ejb.EventLoggerHome;
import com.freshdirect.event.ejb.EventLoggerSB;
import com.freshdirect.event.ejb.EventLoggerSessionBean;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport.MockErpCustomerHome;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.NullEventLogger;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEntityBean;
import com.freshdirect.fdstore.customer.ejb.FDCustomerHome;
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
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewHome;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewSB;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewSessionBean;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerSB;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerSessionBean;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceHome;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSB;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSessionBean;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoHome;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoSB;
import com.freshdirect.fdstore.zone.ejb.FDZoneInfoSessionBean;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.mail.ejb.MailerGatewaySessionBean;
import com.freshdirect.security.ticket.TicketServiceHome;
import com.freshdirect.security.ticket.TicketServiceSB;
import com.freshdirect.security.ticket.TicketServiceSessionBean;
import com.freshdirect.smartstore.ejb.DyfModelHome;
import com.freshdirect.smartstore.ejb.DyfModelSB;
import com.freshdirect.smartstore.ejb.DyfModelSessionBean;
import com.freshdirect.smartstore.ejb.OfflineRecommenderHome;
import com.freshdirect.smartstore.ejb.OfflineRecommenderSB;
import com.freshdirect.smartstore.ejb.OfflineRecommenderSessionBean;
import com.freshdirect.smartstore.ejb.ScoreFactorHome;
import com.freshdirect.smartstore.ejb.ScoreFactorSB;
import com.freshdirect.smartstore.ejb.ScoreFactorSessionBean;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationHome;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSB;
import com.freshdirect.smartstore.ejb.SmartStoreServiceConfigurationSessionBean;
import com.freshdirect.smartstore.ejb.VariantSelectionHome;
import com.freshdirect.smartstore.ejb.VariantSelectionSB;
import com.freshdirect.smartstore.ejb.VariantSelectionSessionBean;
import com.freshdirect.smartstore.fdstore.CohortSelector;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.smartstore.ProductGroupRecommenderTag;
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
                this.promotionEligibility = new FDPromotionEligibility();
            }

            @Override
            public FDCustomerModel getFDCustomer() throws FDResourceException {
                FDCustomerModel fdCust = new FDCustomerModel();
                fdCust.setProfile(new ProfileModel());
                return fdCust;
            }
        };
        user.setIdentity(new FDIdentity(erpCustomerId, fdCustomerId));
        // Set dummy pricing context for zone pricing.
        // user.setZipCode("11101");
        // user.setSelectedServiceType(EnumServiceType.HOME);
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

        SessionBeanDescriptor ssServiceDesc = new SessionBeanDescriptor("freshdirect.smartstore.SmartStoreServiceConfiguration",
                SmartStoreServiceConfigurationHome.class, SmartStoreServiceConfigurationSB.class, SmartStoreServiceConfigurationSessionBean.class);
        container.deploy(ssServiceDesc);

        SessionBeanDescriptor scoreFactServiceDesc = new SessionBeanDescriptor("freshdirect.smartstore.ScoreFactorHome", ScoreFactorHome.class,
                ScoreFactorSB.class, ScoreFactorSessionBean.class);
        container.deploy(scoreFactServiceDesc);

        SessionBeanDescriptor dlvAdminDesc = new SessionBeanDescriptor(FDStoreProperties.getDeliveryManagerHome(), DlvManagerHome.class, DlvManagerSB.class,
                DlvManagerSessionBean.class);
        container.deploy(dlvAdminDesc);

        // Added for zone pricing.
        SessionBeanDescriptor zoneInfoDesc = new SessionBeanDescriptor("freshdirect.erp.ZoneInfoManager", ErpZoneInfoHome.class, ErpZoneInfoSB.class,
                ErpZoneInfoSessionBean.class);
        container.deploy(zoneInfoDesc);

        container
                .deploy(new SessionBeanDescriptor("freshdirect.fdstore.ZoneInfoManager", FDZoneInfoHome.class, FDZoneInfoSB.class, FDZoneInfoSessionBean.class));

        // added for promotion manager
        container.deploy(new SessionBeanDescriptor("freshdirect.fdstore.PromotionManager", FDPromotionManagerHome.class, FDPromotionManagerSB.class,
                FDPromotionManagerSessionBean.class));

        container.deploy(new SessionBeanDescriptor("java:comp/env/ejb/DlvPassManager", DlvPassManagerHome.class, DlvPassManagerSB.class,
                DlvPassManagerSessionBean.class));

//        container.deploy(new SessionBeanDescriptor("java:comp/env/ejb/ErpInfo", ErpInfoHome.class, ErpInfoSB.class, ErpInfoSessionBean.class));
        container.deploy(new SessionBeanDescriptor("freshdirect.erp.Info", ErpInfoHome.class, ErpInfoSB.class, ErpInfoSessionBean.class));
        container.deploy(new SessionBeanDescriptor("freshdirect.fdstore.PromotionManagerNew", FDPromotionManagerNewHome.class, FDPromotionManagerNewSB.class, FDPromotionManagerNewSessionBean.class));

        // added for smart store
        container.deploy(new SessionBeanDescriptor("freshdirect.smartstore.VariantSelection", VariantSelectionHome.class, VariantSelectionSB.class,
                VariantSelectionSessionBean.class));
        // FDStoreProperties.getFDCustomerHome()
        container.deploy(new EntityBeanDescriptor(FDStoreProperties.getFDCustomerHome(), FDCustomerHome.class, FDCustomerEB.class, FDCustomerEntityBean.class));
        container.deploy(new SessionBeanDescriptor(TicketServiceHome.JNDI_HOME, TicketServiceHome.class, TicketServiceSB.class, TicketServiceSessionBean.class));

        // added for offline recommendation
        container.deploy(new SessionBeanDescriptor(OfflineRecommenderHome.JNDI_HOME, OfflineRecommenderHome.class, OfflineRecommenderSB.class,
                OfflineRecommenderSessionBean.class));

        container.deploy(
        	new SessionBeanDescriptor(
    			StandingOrdersServiceHome.JNDI_HOME, StandingOrdersServiceHome.class, StandingOrdersServiceSB.class,
    			StandingOrdersServiceSessionBean.class
        	)
        );

        return container;
    }

    public static Context createContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();

        // set the context factory to the mockejb context factory
        FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");
        ErpServicesProperties.set("erpservices.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

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

    public static ProductGroupRecommenderTag createFeaturedItemsTag(MockPageContext ctx, String contentKey) {
        ProductGroupRecommenderTag fit = new ProductGroupRecommenderTag() {
            @Override
            protected void collectRequestId(HttpServletRequest request, Recommendations recommendations, FDUserI user) {
                LOG.info("collectRequestId called " + recommendations);
            }
        };
        fit.setPageContext(ctx);
        fit.setId("recommendations");
        fit.setItemCount(5);
        fit.setNoShuffle(true);
        fit.setCurrentNode(ContentFactory.getInstance().getContentNode(contentKey));
        fit.setSiteFeature(EnumSiteFeature.FEATURED_ITEMS.getName());
        return fit;
    }

    /**
     * Convert a Collection<ContentNodeModel> to Set<String>, where the result
     * contains the ids of the nodes.
     * 
     * @param coll
     * @return
     */
    public static Set<String> convertToStringList(Collection<? extends ContentNodeModel> coll) {
        Set<String> set = new HashSet<String>();
        for (ContentNodeModel obj : coll) {
            set.add(obj.getContentKey().getId());
        }
        return set;
    }

    public static String getId(List<? extends ContentNodeModel> nodes, int pos) {
        return (nodes.get(pos)).getContentKey().getId();
    }

    @SuppressWarnings("unchecked")
	public static Set toSet(Object[] arr) {
        return new HashSet(Arrays.asList(arr));
    }

    public static XmlContentService initCmsManagerFromXmls(String xmlPath) {
        List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(), xmlPath);

        try {
            CmsManager.setInstance(new CmsManager(service, SearchTestUtils.createSearchService(new ArrayList<ContentIndex>(), SearchTestUtils.createTempDir(TestUtils.class.getCanonicalName(), (new Date()).toString()))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return service;
    }

    public static void initFDStoreProperties() {
        FDStoreProperties.set(FDStoreProperties.SMARTSTORE_CACHE_ONLINE_FACTORS, "false");
        FDStoreProperties.setLastRefresh(System.currentTimeMillis() + (1000 * 60 * 10));
    }

    public static void initCohortSelector() {
        Map<String, Integer> cohorts = new HashMap<String, Integer>();

        Integer FIFTY = new Integer(50);
        cohorts.put("A", FIFTY);
        cohorts.put("B", FIFTY);
        CohortSelector.setCohorts(cohorts);
        CohortSelector.setCohortNames(Arrays.asList(new String[] {"A", "B" } ));

    }

}
