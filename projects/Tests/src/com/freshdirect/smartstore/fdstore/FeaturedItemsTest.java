/**
 * 
 */
package com.freshdirect.smartstore.fdstore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.jsp.JspException;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.impl.AllProductInCategoryRecommendationService;
import com.freshdirect.smartstore.impl.CandidateProductRecommendationService;
import com.freshdirect.smartstore.impl.FeaturedItemsRecommendationService;
import com.freshdirect.smartstore.impl.ManualOverrideRecommendationService;
import com.freshdirect.smartstore.impl.NullRecommendationService;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.impl.YourFavoritesInCategoryRecommendationService;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;
import com.freshdirect.smartstore.service.VariantRegistry;
import com.freshdirect.webapp.taglib.smartstore.ProductGroupRecommenderTag;
import com.freshdirect.webapp.util.FDEventUtil;
import com.mockrunner.mock.web.MockPageContext;

/**
 * @author zsombor
 * 
 */
public class FeaturedItemsTest extends RecommendationServiceTestBase {

    FeaturedItemsRecommendationService firs = null;
    AllProductInCategoryRecommendationService apicrs = null;
    CandidateProductRecommendationService cprs = null;
    ManualOverrideRecommendationService mors = null;
    ScriptedRecommendationService mors_s = null;
    ScriptedRecommendationService yfrs_s = null;
    ScriptedRecommendationService yfrs_s2 = null;

    private YourFavoritesInCategoryRecommendationService yfrs;

    
    public FeaturedItemsTest(String name) throws NamingException {
        super(name);
    } 
    
    RecommendationService getFeaturedItemsService() {
        if (firs == null) {
            firs = new FeaturedItemsRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS)), RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS), new java.util.HashMap()), false);
            firs.getVariant().setRecommender(firs);
            VariantRegistry.getInstance().addService(firs.getVariant());
        }
        return firs;
    }

    RecommendationService getDeterministicFeaturedItemsService() {
        RecommendationService ds = new FeaturedItemsRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS).set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "deterministic")),
                    RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("fi_config",
                    RecommendationServiceType.FEATURED_ITEMS).set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "deterministic"), new java.util.HashMap()), false);
        ds.getVariant().setRecommender(ds);
        VariantRegistry.getInstance().addService(ds.getVariant());
        return ds;
    }
    
    

    RecommendationService getAllProductInCategoryService() {
        if (apicrs == null) {
            apicrs = new AllProductInCategoryRecommendationService(new Variant("apc", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("apc_config",
                    RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY)),
                    RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("apc_config",
                    RecommendationServiceType.ALL_PRODUCT_IN_CATEGORY)
                    .set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "linear")
                    .set(RecommendationServiceFactory.CKEY_TOP_N, "18")
                    .set(RecommendationServiceFactory.CKEY_TOP_PERC, "23"), new java.util.HashMap()), false);
            apicrs.getVariant().setRecommender(apicrs);
            VariantRegistry.getInstance().addService(apicrs.getVariant());
        }
        return apicrs;
    }
    
    
    RecommendationService getCandidateListService() {
        if (cprs == null) {
            cprs = new CandidateProductRecommendationService(new Variant("cpc", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("cpc_config",
                RecommendationServiceType.CANDIDATE_LIST)),
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("cpc_config",
                RecommendationServiceType.CANDIDATE_LIST), new java.util.HashMap()), false);
            cprs.getVariant().setRecommender(cprs);
            VariantRegistry.getInstance().addService(cprs.getVariant());
        }
        return cprs;
    }
    
    

    RecommendationService getManualOverrideService() {
        if (mors == null) {
            mors = new ManualOverrideRecommendationService(new Variant("mos", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("mos_config",
                RecommendationServiceType.MANUAL_OVERRIDE)),
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("mos_config",
                RecommendationServiceType.MANUAL_OVERRIDE), new java.util.HashMap()), false);
            mors.getVariant().setRecommender(mors);
            VariantRegistry.getInstance().addService(mors.getVariant());
        }
        return mors;
    }

    RecommendationService getScriptedManualOverrideService() throws CompileException {
        if (mors_s == null) {
            mors_s = new ScriptedRecommendationService(new Variant("mos_s", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("mos_scripted_config",
                RecommendationServiceType.SCRIPTED)), 
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("mos_config",
                RecommendationServiceType.SCRIPTED), new java.util.HashMap()),
                false, "ManuallyOverriddenSlotsP(currentNode) + CandidateLists", "Popularity");
            mors_s.getVariant().setRecommender(mors_s);
            VariantRegistry.getInstance().addService(mors_s.getVariant());
        }
        return mors_s;
    }
    
    RecommendationService getYourFavoritesService() {
        if (yfrs == null) {
            yfrs = new YourFavoritesInCategoryRecommendationService(new Variant("yf_fi", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("yf_fi",
                RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS)),
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("yf_fi",
                RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS), new java.util.HashMap()), false);
            yfrs.getVariant().setRecommender(yfrs);
            VariantRegistry.getInstance().addService(yfrs.getVariant());
        }
        return yfrs;
    }
    
    RecommendationService getDeterministicYourFavoritesService() {
        RecommendationService yfrsd = new YourFavoritesInCategoryRecommendationService(new Variant("yf_fid", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("yf_fid",
                RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS).set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "deterministic")),
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("yf_fid",
                RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS).set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "deterministic"), new java.util.HashMap()), false);
        yfrsd.getVariant().setRecommender(yfrsd);
        VariantRegistry.getInstance().addService(yfrsd.getVariant());
        return yfrsd;
    }

    RecommendationService getScriptedYourFavoritesService() throws CompileException {
        if (yfrs_s == null) {
            yfrs_s = new ScriptedRecommendationService(new Variant("yf_fi_s", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("yf_fi_s_config",
                RecommendationServiceType.SCRIPTED)), 
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("mos_config",
                RecommendationServiceType.SCRIPTED), new java.util.HashMap()),
                false, "(Top(RecursiveNodes(currentNode),Frequency,1):atLeast(Frequency,1):prioritize()) + ManuallyOverriddenSlotsP(currentNode) + CandidateLists", "Popularity");
            yfrs_s.getVariant().setRecommender(yfrs_s);
            VariantRegistry.getInstance().addService(yfrs_s.getVariant());
        }
        return yfrs_s;
    }

    RecommendationService getScriptedYourFavoritesService2() throws CompileException {
        if (yfrs_s2 == null) {
            yfrs_s2 = new ScriptedRecommendationService(new Variant("yf_fi_s2", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("yf_fi_s_config2",
                RecommendationServiceType.SCRIPTED)), 
                RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("mos_config",
                RecommendationServiceType.SCRIPTED), new java.util.HashMap()),
                false, "Top(RecursiveNodes(currentNode),Frequency,1):atLeast(Frequency,1):prioritize() + ManuallyOverriddenSlotsP(currentNode) + CandidateLists", "Popularity");
            yfrs_s2.getVariant().setRecommender(yfrs_s2);
            VariantRegistry.getInstance().addService(yfrs_s2.getVariant());
        }
        return yfrs_s2;
    }
    
    
    RecommendationService getNullService() { 
        NullRecommendationService nil = new NullRecommendationService(new Variant("nil", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("nil", RecommendationServiceType.NIL)));
        nil.getVariant().setRecommender(nil);
        VariantRegistry.getInstance().addService(nil.getVariant());
		return nil;
    }

    public void testRecommendationService() {
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("spe_cooki_cooki");

        assertNotNull("spe_cooki_cooki category", category);

        SessionInput si = new SessionInput("12345", null, null);
        si.setCurrentNode(category);
        si.setNoShuffle(true);

        List nodes = getFeaturedItemsService().recommendNodes(si);
        assertNotNull("recommend nodes", nodes);
        assertEquals("recommended nodes size", 3, nodes.size());

        assertNode("0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
        assertNode("1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
        assertNode("2-spe_walkers_shortbre_02", nodes, 2, "spe_walkers_shortbre_02");
        
        nodes = getNullService().recommendNodes(si);
        assertNotNull("nul recommend nodes", nodes);
        assertEquals("nul recommended nodes size", 0, nodes.size());
    }

    public void testFeaturedItemsService() {
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getFeaturedItemsService().getVariant()));

        ProductGroupRecommenderTag fit = TestUtils.createFeaturedItemsTag(ctx, "spe_cooki_cooki");

        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("recommendations", recomm);
            assertEquals("3 recommendation", 3, recomm.getProducts().size());
            
            List nodes = recomm.getProducts();
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

    
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getDeterministicFeaturedItemsService().getVariant()));

        fit = TestUtils.createFeaturedItemsTag(ctx, "spe_cooki_cooki");
        fit.setNoShuffle(false);

        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("recommendations", recomm);
            assertEquals("3 recommendation", 3, recomm.getProducts().size());
            
            List nodes = recomm.getProducts();
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
    }

    public void testAllProductInCategoryService() {
        MockPageContext ctx;
        ProductGroupRecommenderTag fit;
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getAllProductInCategoryService().getVariant()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));

        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_cooki_cooki");
        
        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("all_p : recommendations", recomm);
            assertEquals("all_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            assertNode("all_p : 0-spe_walkers_shortbre_02", nodes, 0, "spe_walkers_shortbre_02");
            assertNode("all_p : 1-spe_madmoose_chc", nodes , 1, "spe_madmoose_chc");
            assertNode("all_p : 2-gro_chips_nabisco_04", nodes, 2, "gro_chips_nabisco_04");
            assertNode("all_p : 3-spe_moore_lemon", nodes, 3, "spe_moore_lemon");
            assertNode("all_p : 4-fro_veniero_biscotti", nodes, 4, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("all_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
    }

    public void testCandidateListService() {
        MockPageContext ctx;
        ProductGroupRecommenderTag fit;
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getCandidateListService().getVariant()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_cooki_cooki");
        
        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("can_p : recommendations", recomm);
            assertEquals("can_p : 1 recommendation", 1, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            assertNode("can_p : 0-fro_veniero_biscotti", nodes, 0, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("can_p : event log size", 1, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
    }

    public void testManualOverrideService() {
        MockPageContext ctx;
        ProductGroupRecommenderTag fit;
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getManualOverrideService().getVariant()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_cooki_cooki");
        
        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("man_p : recommendations", recomm);
            assertEquals("man_p : 2 recommendation", 2, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set:
            assertNode("man_p : 0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
            assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("man_p : event log size", 2, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        fit = TestUtils.createFeaturedItemsTag(ctx, "speci_xxx_yyy");
        
        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("speci 1 : recommendations", recomm);
            assertEquals("speci 1 : 1 recommendation", 1, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set:
            assertNode("man_p : 0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
            //assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("man_p : event log size", 1, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
        Set cart = new HashSet();
        cart.add(new ContentKey(FDContentTypes.PRODUCT, "spe_madmoose_chc"));
        fit.setShoppingCart(cart);

        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("speci 2 : recommendations", recomm);
            assertEquals("speci 2 : 1 recommendation", 1, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set:
            assertNode("speci 2 : 0-fro_veniero_biscotti", nodes, 0, "fro_veniero_biscotti");
            //assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("speci 2 : event log size", 1, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
    
    }

    public void testScriptedManualOverrideService() throws CompileException {
        MockPageContext ctx;
        ProductGroupRecommenderTag fit;
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS,
        		new SingleVariantSelector(getScriptedManualOverrideService().getVariant()));
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_cooki_cooki");
        
        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("man_p : recommendations", recomm);
            assertEquals("man_p : 2 recommendation", 2, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set:
            assertNode("man_p : 0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
            assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("man_p : event log size", 2, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        fit = TestUtils.createFeaturedItemsTag(ctx, "speci_xxx_yyy");
        
        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("speci 1 : recommendations", recomm);
            assertEquals("speci 1 : 1 recommendation", 1, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set:
            assertNode("man_p : 0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
            //assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("man_p : event log size", 1, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
        Set cart = new HashSet();
        cart.add(new ContentKey(FDContentTypes.PRODUCT, "spe_madmoose_chc"));
        fit.setShoppingCart(cart);

        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("speci 2 : recommendations", recomm);
            assertEquals("speci 2 : 1 recommendation", 1, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set:
            assertNode("speci 2 : 0-fro_veniero_biscotti", nodes, 0, "fro_veniero_biscotti");
            //assertNode("man_p : 1-fro_veniero_biscotti", nodes, 1, "fro_veniero_biscotti");

            FDEventUtil.flushImpressions();
            
            assertEquals("speci 2 : event log size", 1, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
        
    
    }
    
    public void testYourFavoritesInFi() {
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getYourFavoritesService().getVariant()));
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        ProductGroupRecommenderTag fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        
        RecommendationEventLoggerMockup eventLogger = getMockup();
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
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
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_enfamil_powder_m_02", nodes, 0, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 1 hab_pampers_crsrs_4", nodes, 1, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 2 gro_earths_oat_cereal", nodes, 2, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 3 hba_1ups_aloe_rf", nodes, 3, "hba_1ups_aloe_rf");
            assertNode("with user prefs : yf_p : 4 hba_svngen_dprs_3", nodes, 4, "hba_svngen_dprs_3");
//            assertNode("with user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
//            assertNode("with user prefs : yf_p : 2 hba_1ups_aloe_rf", nodes, 2, "hba_1ups_aloe_rf");
//            assertNode("with user prefs : yf_p : 3 hab_pampers_crsrs_4", nodes, 3, "hab_pampers_crsrs_4");
//            assertNode("with user prefs : yf_p : 4 hba_svngen_dprs_3", nodes, 4, "hba_svngen_dprs_3");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "user-with-favorite-prods2", "789"));
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_7gen_diaperlg", nodes, 0, "gro_7gen_diaperlg");
//            assertNode("with user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
//            assertNode("with user prefs : yf_p : 2 gro_enfamil_powder_m_02", nodes, 2, "gro_enfamil_powder_m_02");
//            assertNode("with user prefs : yf_p : 3 hba_1ups_aloe_rf", nodes, 3, "hba_1ups_aloe_rf");
//            assertNode("with user prefs : yf_p : 4 hab_pampers_crsrs_4", nodes, 4, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 1 hab_pampers_crsrs_4", nodes, 1, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 2 gro_earths_oat_cereal", nodes, 2, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 3 gro_enfamil_powder_m_02", nodes, 3, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 4 hba_1ups_aloe_rf", nodes, 4, "hba_1ups_aloe_rf");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        
        // Test the deterministic sampling strategy
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getDeterministicYourFavoritesService().getVariant()));
        
        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "user-with-favorite-prods2", "789"));
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        fit.setNoShuffle(true);
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_7gen_diaperlg", nodes, 0, "gro_7gen_diaperlg");
            assertNode("with user prefs : yf_p : 1 hab_pampers_crsrs_4", nodes, 1, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 2 gro_earths_oat_cereal", nodes, 2, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 3 gro_enfamil_powder_m_02", nodes, 3, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 4 hba_1ups_aloe_rf", nodes, 4, "hba_1ups_aloe_rf");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
    }

    public void testScriptedYourFavoritesInFi() throws CompileException {
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getScriptedYourFavoritesService().getVariant()));
        doScriptedYourFavoritesInFi();
    }

    public void testScriptedYourFavoritesInFi2() throws CompileException {
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getScriptedYourFavoritesService2().getVariant()));
        doScriptedYourFavoritesInFi();
    }
    
    private void doScriptedYourFavoritesInFi() throws CompileException {
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        
        ProductGroupRecommenderTag fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        
        RecommendationEventLoggerMockup eventLogger = getMockup();
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
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
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_enfamil_powder_m_02", nodes, 0, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 1 hab_pampers_crsrs_4", nodes, 1, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 2 gro_earths_oat_cereal", nodes, 2, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 3 hba_1ups_aloe_rf", nodes, 3, "hba_1ups_aloe_rf");
            assertNode("with user prefs : yf_p : 4 hba_svngen_dprs_3", nodes, 4, "hba_svngen_dprs_3");
//            assertNode("with user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
//            assertNode("with user prefs : yf_p : 2 hba_1ups_aloe_rf", nodes, 2, "hba_1ups_aloe_rf");
//            assertNode("with user prefs : yf_p : 3 hab_pampers_crsrs_4", nodes, 3, "hab_pampers_crsrs_4");
//            assertNode("with user prefs : yf_p : 4 hba_svngen_dprs_3", nodes, 4, "hba_svngen_dprs_3");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }

        ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "user-with-favorite-prods2", "789"));
        
        fit = TestUtils.createFeaturedItemsTag(ctx, "gro_baby");
        
        try {
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();
            FDEventUtil.flushImpressions();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("yf_p : recommendations", recomm);
            assertEquals("yf_p : 5 recommendation", 5, recomm.getProducts().size());
            List nodes = recomm.getProducts();
            // from the FEATURED_ITEMS set
            // gro_earths_oat_cereal, gro_enfamil_powder_m_02, hba_1ups_aloe_rf, hab_pampers_crsrs_4, hba_svngen_dprs_3
            assertNode("with user prefs - manual slot/featured : yf_p : 0 gro_7gen_diaperlg", nodes, 0, "gro_7gen_diaperlg");
//            assertNode("with user prefs : yf_p : 1 gro_earths_oat_cereal", nodes, 1, "gro_earths_oat_cereal");
//            assertNode("with user prefs : yf_p : 2 gro_enfamil_powder_m_02", nodes, 2, "gro_enfamil_powder_m_02");
//            assertNode("with user prefs : yf_p : 3 hba_1ups_aloe_rf", nodes, 3, "hba_1ups_aloe_rf");
//            assertNode("with user prefs : yf_p : 4 hab_pampers_crsrs_4", nodes, 4, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 1 hab_pampers_crsrs_4", nodes, 1, "hab_pampers_crsrs_4");
            assertNode("with user prefs : yf_p : 2 gro_earths_oat_cereal", nodes, 2, "gro_earths_oat_cereal");
            assertNode("with user prefs : yf_p : 3 gro_enfamil_powder_m_02", nodes, 3, "gro_enfamil_powder_m_02");
            assertNode("with user prefs : yf_p : 4 hba_1ups_aloe_rf", nodes, 4, "hba_1ups_aloe_rf");

            
            assertEquals("yf_p : event log size", 5, eventLogger.getCollectedEvents().size());

        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
    }

    
    public void testCartItemRemoval() {
        FDUser user = TestUtils.createUser("123", "456", "789");
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getFeaturedItemsService().getVariant()));

        ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode("spe_cooki_cooki");
        try {

            FDStoreRecommender recommender = FDStoreRecommender.getInstance();
            
            SessionInput si = new SessionInput(user);
            si.setCurrentNode(contentNode);
            si.setNoShuffle(true);
            si.setMaxRecommendations(5);
            Recommendations recomm = recommender.getRecommendations(EnumSiteFeature.FEATURED_ITEMS, user, si);

            {
                assertNotNull("default-recommendations", recomm);
                assertEquals("default-3 recommendation", 3, recomm.getProducts().size());
                
                List nodes = recomm.getProducts();
                assertNode("default-0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
                assertNode("default-1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
                assertNode("default-2-spe_walkers_shortbre_02", nodes, 2, "spe_walkers_shortbre_02");
            }
            
            // Test cart item removal 
            Set cartItems = new HashSet();
            cartItems.add(ContentKey.create(FDContentTypes.PRODUCT, "spe_moore_lemon"));
            si.setCartContents(cartItems);
            
            recomm = recommender.getRecommendations(EnumSiteFeature.FEATURED_ITEMS, user, si);
            {
                assertNotNull("exlusion-recommendations", recomm);
                assertEquals("exlusion-2 recommendation", 2, recomm.getProducts().size());
                
                List nodes = recomm.getProducts();
                assertNode("exlusion-0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
                assertNode("exlusion-1-spe_walkers_shortbre_02", nodes, 1, "spe_walkers_shortbre_02");
            }
            
            {
                // test for 'INCLUDE_CART_ITEMS'
                RecommendationServiceConfig fiConfig = new RecommendationServiceConfig("fi_config",
                        RecommendationServiceType.FEATURED_ITEMS).set(RecommendationServiceFactory.CKEY_INCLUDE_CART_ITEMS, "true");
                FeaturedItemsRecommendationService noRemovalService = new FeaturedItemsRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, 
                        fiConfig),
                        RecommendationServiceFactory.configureSampler(fiConfig, new java.util.HashMap()), true);
                noRemovalService.getVariant().setRecommender(noRemovalService);
                VariantRegistry.getInstance().addService(noRemovalService.getVariant());
                VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(noRemovalService.getVariant()));
            }

            recomm = recommender.getRecommendations(EnumSiteFeature.FEATURED_ITEMS, user, si);
            {
                assertNotNull("include-cart-items-recommendations", recomm);
                assertEquals("include-cart-items-3 recommendation", 3, recomm.getProducts().size());
                
                List nodes = recomm.getProducts();
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
        assertNotNull("service["+service.getClass()+"] has variant id", service.getVariant().getServiceConfig().getVariantId());
        assertNotNull("service["+service.getClass()+"] has config type", service.getVariant().getServiceConfig().getType());
        assertEquals("service["+service.getClass()+"] config type", type, service.getVariant().getServiceConfig().getType());
        
        List nodes = service.recommendNodes(input);
        assertNotNull("service["+service.getClass()+"] should return a non-null list of recommendations", nodes);
        assertEquals("service["+service.getClass()+"] should return a 0 number of recommendations", 0, nodes.size());
        
    }

}
