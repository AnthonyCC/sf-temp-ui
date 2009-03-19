package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.impl.GlobalCompiler;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;

public class GlobalCompilerTest extends TestCase {

    Variant variant;
    private DataAccess input;
    
    protected void setUp() throws Exception {
        input = new DataAccess() {
            public List getDatasource(SessionInput input,  String name) {
                List set = new ArrayList();
                if ("set".equals(name)) {
                    set.add("a1");
                    set.add("a2");
                    set.add("a3");

                    return set;
                }
                if ("set2".equals(name)) {
                    set.add("a3");
                    set.add("b1");
                    return set;
                }
                if ("content".equals(name)) {
                    set.add(new ProductModelImpl(new ContentKey(FDContentTypes.PRODUCT, "a1")));
                    set.add(new ProductModelImpl(new ContentKey(FDContentTypes.PRODUCT, "a2")));
                    set.add(new ProductModelImpl(new ContentKey(FDContentTypes.PRODUCT, "a3")));
                    return set;
                }
                if ("content2".equals(name)) {
                    set.add(new ProductModelImpl(new ContentKey(FDContentTypes.PRODUCT, "a2")));
                    set.add(new ProductModelImpl(new ContentKey(FDContentTypes.PRODUCT, "bbb")));
                    return set;
                }
                return null;
            }

            public Map getVariables(ContentNodeModel node) {
                String key = node.getContentKey().getId();
                Map mp = new HashMap();
                if ("a1".equals(key)) {
                    mp.put("afact", new Integer(1));
                    mp.put("bfact", new Double(3));
                }
                if ("a2".equals(key)) {
                    mp.put("afact", new Integer(2));
                    mp.put("bfact", new Double(2));
                }
                if ("a3".equals(key)) {
                    mp.put("afact", new Integer(3));
                    mp.put("bfact", new Double(0.5));
                }
                return mp;
            }
            
            public double[] getVariables(String userId, ContentNodeModel contentNode, String[] variables) {
                Map varMap = getVariables(contentNode);
                double[] result = new double[variables.length];
                
                for (int i=0;i<variables.length;i++) {
                    String var = variables[i];
                    Number number = (Number) varMap.get(var);
                    if (number!=null){ 
                        result[i] = number.doubleValue();
                    }
                }
                
                return result;
            }

        };
        
        GlobalCompiler gc = new GlobalCompiler();
        gc.getDataGeneratorCompiler().addVariable("content", Expression.RET_SET);
        gc.getDataGeneratorCompiler().addVariable("content2", Expression.RET_SET);
        gc.getDataGeneratorCompiler().addVariable("afact", Expression.RET_INT);
        gc.getDataGeneratorCompiler().addVariable("bfact", Expression.RET_FLOAT);
        gc.getScoringAlgorithmCompiler().addVariable("afact", Expression.RET_INT);
        gc.getScoringAlgorithmCompiler().addVariable("bfact", Expression.RET_FLOAT);

        GlobalCompiler.setInstance(gc);
        variant = new Variant("srs", EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("srs_variant", RecommendationServiceType.SCRIPTED));
    }
    
    public void testRecommenderService() throws CompileException {
        SessionInput s = new SessionInput("");
        s.setNoShuffle(true);
        
        {
            ScriptedRecommendationService srs = new ScriptedRecommendationService(variant, "content:(between(afact,2,3)*between(afact,1,2))", null);
            List collection = srs.recommendNodes(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 1, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));

            Collection factors = srs.collectFactors(new HashSet());
            assertEquals("factor number", 1, factors.size());
            assertTrue("afact factor needed", factors.contains("afact"));
        }
        {
            ScriptedRecommendationService srs = new ScriptedRecommendationService(variant, "content:between(afact,2,3)", null);
            List collection = srs.recommendNodes(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));

            Collection factors = srs.collectFactors(new HashSet());
            assertEquals("factor number", 1, factors.size());
            assertTrue("afact factor needed", factors.contains("afact"));

        }
        {
            ScriptedRecommendationService srs = new ScriptedRecommendationService(variant, "content", "afact");
            List collection = srs.recommendNodes(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 3, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a1", strings.contains("a1"));
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            
            assertEquals("1. elem:", "a3", ((ContentNodeModel)collection.get(0)).getContentKey().getId());
            assertEquals("2. elem:", "a2", ((ContentNodeModel)collection.get(1)).getContentKey().getId());
            assertEquals("3. elem:", "a1", ((ContentNodeModel)collection.get(2)).getContentKey().getId());

            Collection factors = srs.collectFactors(new HashSet());
            assertEquals("factor number", 1, factors.size());
            assertTrue("afact factor needed", factors.contains("afact"));
        }
        {
            ScriptedRecommendationService srs = new ScriptedRecommendationService(variant, "content", "bfact");
            List collection = srs.recommendNodes(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 3, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a1", strings.contains("a1"));
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            
            assertEquals("1. elem:", "a1", ((ContentNodeModel)collection.get(0)).getContentKey().getId());
            assertEquals("2. elem:", "a2", ((ContentNodeModel)collection.get(1)).getContentKey().getId());
            assertEquals("3. elem:", "a3", ((ContentNodeModel)collection.get(2)).getContentKey().getId());

            Collection factors = srs.collectFactors(new HashSet());
            assertEquals("factor number", 1, factors.size());
            assertTrue("bfact factor needed", factors.contains("bfact"));
        }
        {
            ScriptedRecommendationService srs = new ScriptedRecommendationService(variant, "content", "afact*bfact");
            List collection = srs.recommendNodes(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 3, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a1", strings.contains("a1"));
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            
            assertEquals("1. elem:", "a2", ((ContentNodeModel)collection.get(0)).getContentKey().getId());
            assertEquals("2. elem:", "a1", ((ContentNodeModel)collection.get(1)).getContentKey().getId());
            assertEquals("3. elem:", "a3", ((ContentNodeModel)collection.get(2)).getContentKey().getId());

            Collection factors = srs.collectFactors(new HashSet());
            assertEquals("factor number", 2, factors.size());
            assertTrue("afact factor needed", factors.contains("afact"));
            assertTrue("bfact factor needed", factors.contains("bfact"));
        }

    }

}
