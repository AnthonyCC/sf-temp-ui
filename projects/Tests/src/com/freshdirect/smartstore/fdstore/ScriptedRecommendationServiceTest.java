package com.freshdirect.smartstore.fdstore;

import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.freshdirect.TestUtils;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.aspects.ScoreFactorGlobalNameAspect;
import com.freshdirect.fdstore.aspects.ScoreFactorPersonalNameAspect;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.impl.GlobalCompiler;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;
import com.freshdirect.smartstore.service.VariantRegistry;
import com.freshdirect.webapp.taglib.smartstore.ProductGroupRecommenderTag;
import com.freshdirect.webapp.util.FDEventUtil;
import com.mockrunner.mock.web.MockPageContext;

public class ScriptedRecommendationServiceTest extends RecommendationServiceTestBase {


    public void setUp() throws Exception {
        super.setUp();
        aspectSystem.add(new ScoreFactorGlobalNameAspect(Collections.singleton("globalPopularity")));
        aspectSystem.add(new ScoreFactorPersonalNameAspect(Collections.EMPTY_SET));
        
        ScoreProvider.setInstance(new ScoreProvider() {
           public double[] getVariables(String userId, PricingContext pricingCtx, ContentNodeModel contentNode, String[] variables) {
               double[] result = new double[variables.length];
               for (int i=0;i<variables.length;i++) {
                   if ("globalPopularity".equals(variables[i])) {
                       result[i] = ProductStatisticsProvider.getInstance().getGlobalProductScore(contentNode.getContentKey());
                   } else {
                       
                       result[i] = 0;
                   }
               }
               return result;
            } 
        });
        
    }

    
    public void testScriptedRecService() throws CompileException {
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getScriptedRecommendationService("FeaturedItems").getVariant()));

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
    }

    public void testScriptedRecServiceWithFiltering() throws CompileException {
        GlobalCompiler gc = new GlobalCompiler();
        gc.addVariable("FeaturedItems", Expression.RET_SET);
        gc.addVariable("globalPopularity", Expression.RET_FLOAT);
        GlobalCompiler.setInstance(gc);
        
        
        MockPageContext ctx = TestUtils.createMockPageContext(TestUtils.createUser("123", "456", "789"));
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, new SingleVariantSelector(getScriptedRecommendationService("FeaturedItems:between(globalPopularity,0,50)").getVariant()));

        ProductGroupRecommenderTag fit = TestUtils.createFeaturedItemsTag(ctx, "spe_cooki_cooki");

        try {
            RecommendationEventLoggerMockup eventLogger = getMockup();
            eventLogger.getCollectedEvents().clear();
            
            fit.doStartTag();

            Recommendations recomm = (Recommendations) ctx.getAttribute("recommendations");
            assertNotNull("recommendations", recomm);
            assertEquals("3 recommendation", 1, recomm.getProducts().size());
            
            List nodes = recomm.getProducts();
            /*assertNode("0-spe_madmoose_chc", nodes , 0, "spe_madmoose_chc");
            assertNode("1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");*/
            assertNode("2-spe_walkers_shortbre_02", nodes, 0, "spe_walkers_shortbre_02");

            FDEventUtil.flushImpressions();
            
            assertEquals("event log size", 1, eventLogger.getCollectedEvents().size());

            /*assertRecommendationEventsAggregate("0", "Product:spe_madmoose_chc", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(0));
            assertRecommendationEventsAggregate("1", "Product:spe_moore_lemon", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(1));*/
            assertRecommendationEventsAggregate("0", "Product:spe_walkers_shortbre_02", (RecommendationEventsAggregate) eventLogger.getCollectedEvents().get(0));
        } catch (JspException e) {
            e.printStackTrace();
            fail("jsp exception" + e.getMessage());
        }
    }
    
    RecommendationService getScriptedRecommendationService(String generator) throws CompileException {
        RecommendationServiceConfig config = new RecommendationServiceConfig("yf_fi", RecommendationServiceType.YOUR_FAVORITES_IN_FEATURED_ITEMS);
        RecommendationService fi = new ScriptedRecommendationService(new Variant("fi", EnumSiteFeature.FEATURED_ITEMS, config),
                RecommendationServiceFactory.configureSampler(config, new java.util.HashMap()), false, generator);
        fi.getVariant().setRecommender(fi);
        VariantRegistry.getInstance().addService(fi.getVariant());
        return fi;
    }

}
