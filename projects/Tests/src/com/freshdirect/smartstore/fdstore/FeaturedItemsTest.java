/**
 * 
 */
package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.event.RecommendationEventLogger;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.aspects.ProductStatisticProviderAspect;
import com.freshdirect.fdstore.aspects.ProductStatisticUserProviderAspect;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.smartstore.impl.AllProductInCategoryRecommendationService;
import com.freshdirect.smartstore.impl.CandidateProductRecommendationService;
import com.freshdirect.smartstore.impl.FeaturedItemsRecommendationService;
import com.freshdirect.smartstore.impl.ManualOverrideRecommendationService;
import com.freshdirect.smartstore.impl.NullRecommendationService;
import com.freshdirect.smartstore.impl.YourFavoritesInCategoryRecommendationService;
import com.freshdirect.webapp.taglib.smartstore.FeaturedItemsTag;
import com.freshdirect.webapp.util.FDEventUtil;
import com.mockrunner.mock.web.MockPageContext;

/**
 * @author zsombor
 * 
 */
public class FeaturedItemsTest extends TestCase {

    private XmlContentService service;

    RecommendationEventLoggerMockup eventLogger;

    FeaturedItemsRecommendationService firs = null;
    AllProductInCategoryRecommendationService apicrs = null;
    CandidateProductRecommendationService cprs = null;
    ManualOverrideRecommendationService mors = null;

    private YourFavoritesInCategoryRecommendationService yfrs;

    public FeaturedItemsTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();

        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml");

        CmsManager.setInstance(new CmsManager(service, null));

        Context context = TestUtils.createContext();
        
        TestUtils.createTransaction(context);
        
        MockContainer mockContainer = TestUtils.createMockContainer(context);

        AspectSystem aspectSystem = TestUtils.createAspectSystem();

        aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("SPE0063144", 2.0).addAvailableSku("SPE0060510", 3.0).addAvailableSku("SPE0000468",
                4.0).addAvailableSku("GRO001792", 5.0).addAvailableSku("FRO0066635", 64.0)
                // YourFavorites in FI
                .addAvailableSku("HBA0063975", 100).addAvailableSku("HBA0072207", 110).addAvailableSku("GRO0065252").addAvailableSku("GRO0057899")
                .addAvailableSku("GRO001066").addAvailableSku("HBA0063975").addAvailableSku("HBA0072637").addAvailableSku("GRO001055")
             //   .addAvailableSku("FRO0060512", 32) // for spe_bruces_whtsvn:'Bruce's Seven Layer White Cake, Frozen'
                
        );
        
        aspectSystem.add(new ProductStatisticProviderAspect() {
            public Map getGlobalProductScores() {
                try {
                    Map map = new HashMap();
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "cfncndy_ash_mcrrd"), new Float(100));
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "dai_orgval_whlmilk_01"), new Float(90));
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "dai_organi_2_milk_02"), new Float(80));
                    
                    
                    String[] keys = StringUtils.split("gro_earths_oat_cereal, gro_enfamil_powder_m_02,hba_1ups_aloe_rf,hab_pampers_crsrs_4,hba_svngen_dprs_3",',');
                    for (int i=0;i<keys.length;i++) {
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, keys[i].trim()), new Float(200-i));
                    }
                    
                    return map;
                } catch (InvalidContentKeyException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        aspectSystem.add(new ProductStatisticUserProviderAspect() {
            public Map getUserProductScores(String userId) {
                try {
                    Map map = new HashMap();
                    if ("user-with-favorite-prods".equals(userId)) {
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, "gro_enfamil_powder_m_02"), new Float(10));
                    }
                    // gro_7gen_diaperlg
                    if ("user-with-favorite-prods2".equals(userId)) {
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, "gro_enfamil_powder_m_02"), new Float(10));
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, "gro_7gen_diaperlg"), new Float(20));
                    }
                    return map;
                } catch (InvalidContentKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        
        eventLogger = new RecommendationEventLoggerMockup();
        RecommendationEventLogger.setInstance(eventLogger);
    }


    RecommendationService getFeaturedItemsService() {
        if (firs == null) {
            firs = new FeaturedItemsRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS)));
        }
        return firs;
    }


    RecommendationService getAllProductInCategoryService() {
        if (apicrs == null) {
            apicrs = new AllProductInCategoryRecommendationService(new Variant("apc", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("apc_config",
                    RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY)));
        }
        return apicrs;
    }
    
    
    RecommendationService getCandidateListService() {
        if (cprs == null) {
            cprs = new CandidateProductRecommendationService(new Variant("cpc", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("cpc_config",
                RecommendationServiceType.CANDIDATE_LIST)));
        }
        return cprs;
    }
    
    

    RecommendationService getManualOverrideService() {
        if (mors == null) {
            mors = new ManualOverrideRecommendationService(new Variant("mos", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("mos_config",
                RecommendationServiceType.MANUAL_OVERRIDE)));
        }
        return mors;
    }

    RecommendationService getYourFavoritesService() {
        if (yfrs == null) {
            yfrs = new YourFavoritesInCategoryRecommendationService(new Variant("yf_fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("yf_fi",
                RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS)));
        }
        return yfrs;
    }
    
    RecommendationService getNullService() { 
        return new NullRecommendationService(new Variant("nil", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("nil", RecommendationServiceType.NIL)));
    }

    public void testRecommendationService() {
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("spe_cooki_cooki");

        assertNotNull("spe_cooki_cooki category", category);

        SessionInput si = new SessionInput("12345");
        si.setCurrentNode(category);

        List nodes = getFeaturedItemsService().recommendNodes(si);
        assertNotNull("recommend nodes", nodes);
        assertEquals("recommended nodes size", 6, nodes.size());

        assertNode("0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
        assertNode("1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
        assertNode("2-cake_choclayer", nodes, 2, "cake_choclayer");
        assertNode("3-spe_walkers_shortbre_02", nodes, 3, "spe_walkers_shortbre_02");
        assertNode("4-fro_up_mudck", nodes, 4, "fro_up_mudck");
        assertNode("5-fro_up_threberylg", nodes, 5, "fro_up_threberylg");
        
        nodes = getNullService().recommendNodes(si);
        assertNotNull("nul recommend nodes", nodes);
        assertEquals("nul recommended nodes size", 0, nodes.size());
    }

    public void testServiceTag() {
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));

        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getFeaturedItemsService()));

        FeaturedItemsTag fit = createTag(ctx, "spe_cooki_cooki");

        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("recommendations", recomm);
            assertEquals("3 recommendation", 3, recomm.getContentNodes().size());
            
            List nodes = recomm.getContentNodes();
            assertNode("0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
            assertNode("1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
            assertNode("2-spe_walkers_shortbre_02", nodes, 2, "spe_walkers_shortbre_02");

            FDEventUtil.flushImpressions();
            
            assertEquals("event log size", 3, eventLogger.getCollectedEvents().size());

            assertRecommendationEventsAggregate("0", "Product:spe_madmoose_chc", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(0));
            assertRecommendationEventsAggregate("1", "Product:spe_moore_lemon", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(1));
            assertRecommendationEventsAggregate("2", "Product:spe_walkers_shortbre_02", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(2));
        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getAllProductInCategoryService()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        fit = createTag(ctx, "gro_cooki_cooki");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("all_p : recommendations", recomm);
            assertEquals("all_p : 5 recommendation", 5, recomm.getContentNodes().size());
            List nodes = recomm.getContentNodes();
            assertNode("all_p : 0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
            assertNode("all_p : 1-gro_chips_nabisco_04", nodes, 1, "gro_chips_nabisco_04");
            assertNode("all_p : 2-spe_moore_lemon", nodes, 2, "spe_moore_lemon");
            assertNode("all_p : 3-fro_veniero_biscotti", nodes, 3, "fro_veniero_biscotti");
            assertNode("all_p : 4-spe_walkers_shortbre_02", nodes, 4, "spe_walkers_shortbre_02");

            FDEventUtil.flushImpressions();
            
            assertEquals("all_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getCandidateListService()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        fit = createTag(ctx, "gro_cooki_cooki");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("can_p : recommendations", recomm);
            assertEquals("can_p : 1 recommendation", 1, recomm.getContentNodes().size());
            List nodes = recomm.getContentNodes();
            assertNode("can_p : 0-fro_veniero_biscotti", nodes, 0, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("can_p : event log size", 1, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getManualOverrideService()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        fit = createTag(ctx, "gro_cooki_cooki");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("man_p : recommendations", recomm);
            assertEquals("man_p : 2 recommendation", 2, recomm.getContentNodes().size());
            List nodes = recomm.getContentNodes();
            // from the FEATURED_ITEMS set:
            assertNode("man_p : 0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
            assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("man_p : event log size", 2, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        

        
    }

    
    public void testYourFavoritesInFi() {
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getYourFavoritesService()));
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        FeaturedItemsTag fit = createTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getContentNodes().size());
            List nodes = recomm.getContentNodes();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("without user prefs - manual slot/featured : yf_p : 0 hab_pampers_crsrs_4", nodes, 0, "hab_pampers_crsrs_4");
            assertNode("without user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
            assertNode("without user prefs : yf_p : 2 gro_enfamil_powder_m_02", nodes, 2, "gro_enfamil_powder_m_02");
            assertNode("without user prefs : yf_p : 3 hba_1ups_aloe_rf", nodes, 3, "hba_1ups_aloe_rf");
            assertNode("without user prefs : yf_p : 4 hba_svngen_dprs_3", nodes, 4, "hba_svngen_dprs_3");

            FDEventUtil.flushImpressions();
            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "user-with-favorite-prods", "789"));
        
        fit = createTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getContentNodes().size());
            List nodes = recomm.getContentNodes();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_enfamil_powder_m_02", nodes, 0, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 2 hba_1ups_aloe_rf", nodes, 2, "hba_1ups_aloe_rf");
            assertNode("with user prefs : yf_p : 3 hab_pampers_crsrs_4", nodes, 3, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 4 hba_svngen_dprs_3", nodes, 4, "hba_svngen_dprs_3");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "user-with-favorite-prods2", "789"));
        
        fit = createTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getContentNodes().size());
            List nodes = recomm.getContentNodes();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_7gen_diaperlg", nodes, 0, "gro_7gen_diaperlg");
            assertNode("with user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 2 gro_enfamil_powder_m_02", nodes, 2, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 3 hba_1ups_aloe_rf", nodes, 3, "hba_1ups_aloe_rf");
            assertNode("with user prefs : yf_p : 4 hab_pampers_crsrs_4", nodes, 4, "hab_pampers_crsrs_4");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
        
    }
    public void testCartItemRemoval() {
        FDUser user = TestUtils.createUser("123", "456", "789");
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getFeaturedItemsService()));

        ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode("spe_cooki_cooki");
        try {

            Trigger trigger = new Trigger(EnumSiteFeature.FEATURED_ITEMS, 5);
            FDStoreRecommender recommender = FDStoreRecommender.getInstance();
            
            SessionInput si = new SessionInput(user);
            si.setCurrentNode(contentNode);
            Recommendations recomm = recommender.getRecommendations(trigger, user, si, null, new HashSet());

            {
                assertNotNull("default-recommendations", recomm);
                assertEquals("default-3 recommendation", 3, recomm.getContentNodes().size());
                
                List nodes = recomm.getContentNodes();
                assertNode("default-0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
                assertNode("default-1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
                assertNode("default-2-spe_walkers_shortbre_02", nodes, 2, "spe_walkers_shortbre_02");
            }
            
            // Test cart item removal 
            Set cartItems = new HashSet();
            cartItems.add(ContentKey.create(FDContentTypes.PRODUCT, "spe_moore_lemon"));
            
            recomm = recommender.getRecommendations(trigger, user, si, null, cartItems);
            {
                assertNotNull("exlusion-recommendations", recomm);
                assertEquals("exlusion-2 recommendation", 2, recomm.getContentNodes().size());
                
                List nodes = recomm.getContentNodes();
                assertNode("exlusion-0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
                assertNode("exlusion-1-spe_walkers_shortbre_02", nodes, 1, "spe_walkers_shortbre_02");
            }
            
            {
                // test for 'INCLUDE_CART_ITEMS'
                FeaturedItemsRecommendationService noRemovalService = new FeaturedItemsRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("fi_config",
                        RecommendationServiceType.FEATURED_ITEMS).set(AbstractRecommendationService.INCLUDE_CART_ITEMS, "true")));
                VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(noRemovalService));
            }

            recomm = recommender.getRecommendations(trigger, user, si, null, cartItems);
            {
                assertNotNull("include-cart-items-recommendations", recomm);
                assertEquals("include-cart-items-3 recommendation", 3, recomm.getContentNodes().size());
                
                List nodes = recomm.getContentNodes();
                assertNode("include-cart-items 0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
                assertNode("include-cart-items 1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
                assertNode("include-cart-items 2-spe_walkers_shortbre_02", nodes, 2, "spe_walkers_shortbre_02");
            }
            
        } catch (FDResourceException e) {
            e.printStackTrace();
            fail("FDResourceException " + e.getMessage());
        } catch (InvalidContentKeyException e) {
            e.printStackTrace();
            fail("FDResourceException " + e.getMessage());
        }
    }
    

    public void testTriviality() {
        SessionInput input = new SessionInput(TestUtils.createUser("test", "test123", "345"));
        input.setCartContents(new HashSet());
        
        testService(getFeaturedItemsService(), RecommendationServiceType.FEATURED_ITEMS, input);
        testService(getAllProductInCategoryService(), RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY, input);
        testService(getCandidateListService(), RecommendationServiceType.CANDIDATE_LIST, input);
        testService(getManualOverrideService(), RecommendationServiceType.MANUAL_OVERRIDE, input);
        testService(getNullService(), RecommendationServiceType.NIL, input);
    }

    private void testService(RecommendationService service, RecommendationServiceType type, SessionInput input) {
        assertNotNull("service["+service.getClass()+"] has a variant", service.getVariant());
        assertNotNull("service["+service.getClass()+"] has site feature", service.getVariant().getSiteFeature());
        assertEquals("service["+service.getClass()+"] site feature is FeaturedItems", EnumSiteFeature.FEATURED_ITEMS, service.getVariant().getSiteFeature());
        assertNotNull("service["+service.getClass()+"] has config", service.getVariant().getServiceConfig());
        assertNotNull("service["+service.getClass()+"] has config name", service.getVariant().getServiceConfig().getName());
        assertNotNull("service["+service.getClass()+"] has config type", service.getVariant().getServiceConfig().getType());
        assertEquals("service["+service.getClass()+"] config type", type, service.getVariant().getServiceConfig().getType());
        
        List nodes = service.recommendNodes(input);
        assertNotNull("service["+service.getClass()+"] should return a non-null list of recommendations", nodes);
        assertEquals("service["+service.getClass()+"] should return a 0 number of recommendations", 0, nodes.size());
        
    }

    private FeaturedItemsTag createTag(MockPageContext ctx, String contentKey) {
        FeaturedItemsTag fit = new FeaturedItemsTag();
        fit.setPageContext(ctx);
        fit.setId("recommendations");
        fit.setItemCount(5);
        fit.setNoShuffle(true);
        fit.setCurrentNode(ContentFactory.getInstance().getContentNode(contentKey));
        return fit;
    }

    private void assertRecommendationEventsAggregate(String label, String contentKey, RecommendationEventsAggregate event) {
        assertEquals(label + " content key", contentKey, event.getContentId());
        assertEquals(label + " variant id", "fi", event.getVariantId());
    }

    private void assertNode(String string, List nodes, int i, String key) {
        assertNotNull("not-null:" + string + '[' + i + ']', nodes.get(i));
        assertEquals("content-key:" + string + '[' + i + ']', key, ((ContentNodeModel) nodes.get(i)).getContentKey().getId());
    }
}
