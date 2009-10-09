package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.aspects.VendorPersonalRecommendationAspect;
import com.freshdirect.fdstore.aspects.VendorRecommendationAspect;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.RecommendationServiceTestBase;
import com.freshdirect.smartstore.impl.GlobalCompiler;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

public class VendorRecommendationTest extends RecommendationServiceTestBase {

    MockDataAccess da = new MockDataAccess() {
        
        @Override
        protected Map getVariables(String id) {
            return new HashMap();
        }
    };
    public void setUp() throws Exception {
        super.setUp();
        GlobalCompiler compiler = new GlobalCompiler();

        GlobalCompiler.setInstance(compiler);

        aspectSystem.add(new VendorRecommendationAspect() {
            public List<ContentKey> getRecommendation(String recommender, ContentKey contentKey) {
                List<ContentKey> result = new ArrayList<ContentKey>(3);
                if ("r1".equals(recommender)) {
                    for (int i = 0; i < 3; i++) {
                        result.add(new ContentKey(FDContentTypes.PRODUCT, "r1_" + contentKey.getId() + '_' + (i + 1)));
                    }
                }
                if ("r2".equals(recommender)) {
                    for (int i = 0; i < 3; i++) {
                        result.add(new ContentKey(FDContentTypes.PRODUCT, "p_" + (i + 1)));
                    }
                }
                return result;
            }
        });
        aspectSystem.add(new VendorPersonalRecommendationAspect() {
            public List<ContentKey> getRecommendation(String recommender, String customerId) {
                List<ContentKey> result = new ArrayList<ContentKey>(3);
                if ("r1".equals(recommender)) {
                    result.add(new ContentKey(FDContentTypes.PRODUCT, "r1_prod1_3"));
                }
                if ("123".equals(customerId)) {
                    result.add(new ContentKey(FDContentTypes.PRODUCT, "p_2"));
                }
                return result;
            }
        });
    }

    protected String getCmsXmlName() {
        return "classpath:/com/freshdirect/cms/fdstore/content/simple.xml";
    }

    private ScriptedRecommendationService build(String generator, String scoringFunction) throws CompileException {
        Variant variant = new Variant("srs", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("srs_variant", RecommendationServiceType.SCRIPTED));
        return new ScriptedRecommendationService(variant, RecommendationServiceFactory.configureSampler(variant.getServiceConfig(), new java.util.HashMap()),
                false, false, generator, scoringFunction);
    }

    public void testProductToProductRecommendation() throws CompileException {
        SessionInput s = new SessionInput("", null);
        s.setNoShuffle(true);

        ScriptedRecommendationService r1test = build("ProductRecommendation(\"r1\",\"prod1\")", null);
        List nodes = r1test.recommendNodes(s, da);
        assertNotNull("recommendation", nodes);
        assertEquals("3 recommendation", 3, nodes.size());
        for (int i = 0; i < 3; i++) {
            ContentNodeModel model = (ContentNodeModel) nodes.get(i);
            int x = i + 1;
            assertEquals("the " + x + ". node is 'r1_prod1_" + x + "'!", "r1_prod1_" + x, model.getContentKey().getId());
        }
        assertTrue("statement is cacheable", r1test.isCacheable());
    }
    
    public void testUserToProductRecommendation() throws CompileException {
        SessionInput s = new SessionInput("", null);
        s.setNoShuffle(true);

        ScriptedRecommendationService r1test = build("PersonalRecommendation(\"r1\",)", null);
        List nodes = r1test.recommendNodes(s, da);
        assertNotNull("recommendation", nodes);
        assertEquals("1 recommendation", 1, nodes.size());
        assertEquals("first node is ... ", "r1_prod1_3", ((ContentNodeModel)nodes.get(0)).getContentKey().getId());

        s = new SessionInput("123", null);
        s.setNoShuffle(true);

        nodes = r1test.recommendNodes(s, da);
        assertNotNull("recommendation", nodes);
        assertEquals("2 recommendation", 2, nodes.size());
        assertEquals("first node is ... ", "r1_prod1_3", ((ContentNodeModel)nodes.get(0)).getContentKey().getId());
        assertEquals("second node is ... ", "p_2", ((ContentNodeModel)nodes.get(1)).getContentKey().getId());

        assertTrue("statement is not cacheable", !r1test.isCacheable());
    }
    

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
