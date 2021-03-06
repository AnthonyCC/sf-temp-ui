package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;

import junit.framework.TestCase;

import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.core.MockProductModel;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModelImpl;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;
import com.freshdirect.smartstore.fdstore.ScoreProvider;

public class DataGeneratorCompilerTest extends TestCase {
    DataGeneratorCompiler comp;

    MockDataAccess            input;

    SessionInput          s = new SessionInput("ses1", null, null, new FulfillmentContext());

    protected void setUp() throws Exception {
        {
            TestUtils.initCmsManagerFromXmls("classpath:/com/freshdirect/cms/fdstore/content/simple2.xml");
        }
        
        {
            Context context = TestUtils.createContext();
            
            TestUtils.createTransaction(context);
            
            MockContainer mockContainer = TestUtils.createMockContainer(context);

            AspectSystem aspectSystem = TestUtils.createAspectSystem();
            
            aspectSystem.add(new FDFactoryProductInfoAspect()
            		.addAvailableSku("a1sku", 2.0).addAvailableSku("a2sku", 3.0)
					.addAvailableSku("a3sku", 4.0).addAvailableSku("b1sku", 5.0)
					.addAvailableSku("bbbsku", 64.0)
            		.addAvailableSku("b1sku", 2.0).addAvailableSku("b2sku", 3.0)
					.addAvailableSku("b3sku", 4.0)
            );
        	
        }
        
        comp = new DataGeneratorCompiler(ScoreProvider.ZONE_DEPENDENT_FACTORS_ARRAY);
        comp.addVariable("set", Expression.RET_SET);
        comp.addVariable("set2", Expression.RET_SET);
        comp.addVariable("content", Expression.RET_SET);
        comp.addVariable("content2", Expression.RET_SET);
        comp.addVariable("content3", Expression.RET_SET);

        comp.addVariable("afact", Expression.RET_INT);
        comp.addVariable("bfact", Expression.RET_FLOAT);

        input = new MockDataAccess() {
            @SuppressWarnings("unchecked")
            public List fetchContentNodes(SessionInput input, String name) {
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
                    set.add(new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a1")));
                    set.add(new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a2")));
                    set.add(new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a3")));
                    return set;
                }
                if ("content2".equals(name)) {
                    set.add(new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a2")));
                    set.add(new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "bbb")));
                    return set;
                }
                if ("content3".equals(name)) {
                    BrandModel b = new BrandModel(ContentKey.getContentKey(FDContentTypes.BRAND, "brand")); 
                    BrandModel b2 = new BrandModel(ContentKey.getContentKey(FDContentTypes.BRAND, "brand2"));
                    
                    set.add(new MockProductModel(ContentKey.getContentKey(FDContentTypes.PRODUCT, "e1"))
                        .addSku(new SkuModel(ContentKey.getContentKey(FDContentTypes.SKU, "bela_01")))
                        .addSku(new SkuModel(ContentKey.getContentKey(FDContentTypes.SKU, "fru_01"))));
                    set.add(new MockProductModel(ContentKey.getContentKey(FDContentTypes.PRODUCT, "e2"))
                    .addSku(new SkuModel(ContentKey.getContentKey(FDContentTypes.SKU, "bela_02")))
                    .addSku(new SkuModel(ContentKey.getContentKey(FDContentTypes.SKU, "cucc"))).addBrand(b));
                    set.add(new MockProductModel(ContentKey.getContentKey(FDContentTypes.PRODUCT, "e3"))
                    .addSku(new SkuModel(ContentKey.getContentKey(FDContentTypes.SKU, "cucc_02")))
                    .addBrand(b).addBrand(b2));
                    
                    return set;
                }
                return null;
            }

            public Map getVariables(String key) {
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

        };
        ContentFactory.setInstance(new ContentFactory());
    }

    public void testSimpleCompilers() throws CompileException {

        {
            DataGenerator dataGenerator = comp.createDataGenerator("test1", "set + set2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            // a1, a2, a3, a3, b1
            assertEquals("result collection size", 5, collection.size());
            assertTrue("contains a1", collection.contains("a1"));
            assertTrue("contains a2", collection.contains("a2"));
            assertTrue("contains a3", collection.contains("a3"));
            assertTrue("contains b1", collection.contains("b1"));
            assertEquals("result list", "[a1, a2, a3, a3, b1]", collection.toString());
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("test2", "set - set2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            assertTrue("contains a1", collection.contains("a1"));
            assertTrue("contains a2", collection.contains("a2"));
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("test3", "set * set2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 1, collection.size());
            assertTrue("contains a3", collection.contains("a3"));
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }

    }

    public void testSetFilter() throws CompileException {
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest1", "content:(1-1)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 0, collection.size());
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }

        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest2", "content:between(afact,2,3)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Set<String> strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest3", "content:between(afact,2,3):between(afact,1,2)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 1, collection.size());
            Set<String> strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));

            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest4", "content:between(afact,2,3) + content2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            // a2, a3, a2, bbb
            assertEquals("result collection size", 4, collection.size());
            Set<String> strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            assertTrue("contains bbb", strings.contains("bbb"));

            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest5", "content:(between(afact,2,3)*between(afact,1,2))");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 1, collection.size());
            Set<String> strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
        }
    }

    public void testAtLeastMost() throws CompileException {
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest6", "content:atLeast(afact,2)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Set<String> strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest7", "content:atMost(afact,2)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Set<String> strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a1", strings.contains("a1"));
            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
    }
    
    public void testYmalRelated() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("ymal1", "RecursiveNodes(currentNode)");
        assertNotNull("dataGenerator", dataGenerator);
        dataGenerator.generate(s, input);
    }
    
    public void testYmalValidations() {
        try {
            comp.createDataGenerator("ymalVal1", "RecursiveNodes(3)");
            fail("should fail, with invalid parameter type");
        } catch (CompileException e) {
            assertEquals("type error", CompileException.TYPE_ERROR, e.getCode());
            assertEquals("error message", "The 1. parameter type is integer instead of the expected node/set/string!", e.getMessage());
        }
    }
    
    public void testManuallyOverriddenSlots() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("mo1", "ManuallyOverriddenSlots(currentNode)");
        assertNotNull("dataGenerator", dataGenerator);
        dataGenerator.generate(s, input);
        try {
        	dataGenerator = comp.createDataGenerator("mo2", "ManuallyOverriddenSlots(content)");
        	fail("should fail with invalid parameter type");
        } catch (CompileException e) {
        	e.printStackTrace();
            assertEquals("type error", CompileException.PARAMETER_ERROR, e.getCode());
            assertEquals("error message", "first parameter has unexpected type of set, but expected node or string", e.getMessage());
        }
    }
    
    public void testManuallyOverriddenSlotsP() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("mo2", "ManuallyOverriddenSlotsP(currentNode)");
        assertNotNull("dataGenerator", dataGenerator);
        dataGenerator.generate(s, input);
    }

    public void testPrioritize() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("prio1", "content:prioritize()");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
        assertNotNull("result collection", collection);
        assertEquals("result collection size", 0, collection.size());
        assertNotNull("result prioritized nodes", input.getPrioritizedNodes());
        assertEquals("result prioritized nodes size", 3, input.getPrioritizedNodes().size());
        assertTrue("contains a1", input.getPrioritizedNodes().contains(
        		new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a1"))));
        assertTrue("contains a2", input.getPrioritizedNodes().contains(
        		new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a2"))));
        assertTrue("contains a3", input.getPrioritizedNodes().contains(
        		new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a3"))));
    }

    public void testDeprioritize() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("deprio1", "content:deprioritize()");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        Collection<? extends ContentNodeModel> collection = dataGenerator.generate(s, input);
        assertNotNull("result collection", collection);
        assertEquals("result collection size", 0, collection.size());
        assertNotNull("result deprioritized nodes", input.getPosteriorNodes());
        assertEquals("result deprioritized nodes size", 3, input.getPosteriorNodes().size());
        assertTrue("contains a1", input.getPosteriorNodes().contains(
        		new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a1"))));
        assertTrue("contains a2", input.getPosteriorNodes().contains(
        		new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a2"))));
        assertTrue("contains a3", input.getPosteriorNodes().contains(
        		new ProductModelImpl(ContentKey.getContentKey(FDContentTypes.PRODUCT, "a3"))));
    }

    public void testRecurseRecursiveNodes() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("recRec", "RecursiveNodes(RecursiveNodes(RecursiveNodes(content)))");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        Collection<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
        assertNotNull("result not null", result);
    }
    
    public void testTopN() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("topN", "Top(content,afact,3)");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        Collection<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
        assertNotNull("result not null", result);
    }
    
    public void testPrioritize2() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("prio2", "content + content2:prioritize()");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        List<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
        assertNotNull("result not null", result);
        assertEquals("result length 3", 3, result.size());
        Set<String> resultNodes = TestUtils.convertToStringList(result);
        assertTrue("prio a1", resultNodes.contains("a1"));
        assertTrue("prio a2", resultNodes.contains("a2"));
        assertTrue("prio a3", resultNodes.contains("a3"));

        
        assertNotNull("result prioritized nodes", input.getPrioritizedNodes());
        assertEquals("result prioritized nodes size", 2, input.getPrioritizedNodes().size());
        Set<String> prioNodes = TestUtils.convertToStringList(input.getPrioritizedNodes());
        assertTrue("prio a2", prioNodes.contains("a2"));
        assertTrue("prio bbb", prioNodes.contains("bbb"));
    }
 
    
    public void testSkuFilter() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("skuFilter", "content3:matchSkuPrefix(\"fru\",\"bela\")");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        List<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
        assertNotNull("result not null", result);
        assertEquals("result length 2", 2, result.size());
        Set<String> resultNodes = TestUtils.convertToStringList(result);
        assertTrue("e1", resultNodes.contains("e1"));
        assertTrue("e2", resultNodes.contains("e2"));
        assertEquals("e1-1", "e1", TestUtils.getId(result, 0));
        assertEquals("e2-2", "e2", TestUtils.getId(result, 1));
    }
    
    public void testSkuFilter2() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("skuFilter2", "content3:matchSkuPrefix(\"cucc\") + content3:matchSkuPrefix(\"fru\")");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        List<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
        assertNotNull("result not null", result);
        assertEquals("result length 2", 3, result.size());
        Set<String> resultNodes = TestUtils.convertToStringList(result);
        assertTrue("e1", resultNodes.contains("e1"));
        assertTrue("e2", resultNodes.contains("e2"));
        assertTrue("e3", resultNodes.contains("e3"));
        assertEquals("0", "e2", TestUtils.getId(result, 0));
        assertEquals("1", "e3", TestUtils.getId(result, 1));
        assertEquals("2", "e1", TestUtils.getId(result, 2));
    }
    
    public void testMultipleCalls() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("skuFilter3", "content3:matchSkuPrefix(\"cucc\") + content3:matchSkuPrefix(\"fru\") + content3:matchSkuPrefix(\"cucc\")");
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        List<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
        assertNotNull("result not null", result);
        assertEquals("result length 5", 5, result.size());
        Set<String> resultNodes = TestUtils.convertToStringList(result);
        assertTrue("e1", resultNodes.contains("e1"));
        assertTrue("e2", resultNodes.contains("e2"));
        assertTrue("e3", resultNodes.contains("e3"));
        assertEquals("0", "e2", TestUtils.getId(result, 0));
        assertEquals("1", "e3", TestUtils.getId(result, 1));
        assertEquals("2", "e1", TestUtils.getId(result, 2));
        assertEquals("3", "e2", TestUtils.getId(result, 3));
        assertEquals("4", "e3", TestUtils.getId(result, 4));
    }
    
    public void testDuplicateRecursive() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("recCall", "RecursiveNodes(explicitList)");
        assertNotNull("dataGenerator", dataGenerator);

        DataGenerator dataGenerator2 = comp.createDataGenerator("recCall2", "RecursiveNodes(explicitList)");
        assertNotNull("dataGenerator", dataGenerator2);
    }
    
    @SuppressWarnings("unchecked")
    public void testBrandFilterCall() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("brandFilter", "content3:matchBrand(\"brand\")");
        System.out.println(dataGenerator + " ->\n" + dataGenerator.getGeneratedCode());
        assertNotNull("dataGenerator", dataGenerator);
        input.reset();
        {
            List<? extends ContentNodeModel> result = dataGenerator.generate(s, input);
            assertNotNull("result not null", result);
            assertEquals("result length 2", 2, result.size());
            Set<String> resultNodes = TestUtils.convertToStringList(result);
            assertTrue("e2", resultNodes.contains("e2"));
            assertTrue("e3", resultNodes.contains("e3"));
        }
        

        DataGenerator dataGenerator2 = comp.createDataGenerator("brandFilter2", "content3:matchBrand(explicitList)");
        assertNotNull("dataGenerator", dataGenerator2);
        input.reset();
        
        final List<? extends ContentNodeModel> asList = (List) Arrays.asList(new MockProductModel(ContentKey.getContentKey(FDContentTypes.PRODUCT, "ize")), new BrandModel(ContentKey.getContentKey(FDContentTypes.BRAND, "brand2")));
        s.setExplicitList(asList); 
        {
            List<? extends ContentNodeModel> result = dataGenerator2.generate(s, input);
            s.setExplicitList(null);
            assertNotNull("result not null", result);
            assertEquals("result length 1", 1, result.size());
            Set<String> resultNodes = TestUtils.convertToStringList(result);
            assertTrue("e3", resultNodes.contains("e3"));
        }

    }
    
    public void testCartContentsVariable() throws CompileException, InvalidContentKeyException {
    	DataGenerator generator = comp.createDataGenerator("cartContents1", "cartContents");
        assertNotNull("generator", generator);
        input.reset();
        HashSet<ContentKey> cartContents = new HashSet<ContentKey>();
        cartContents.add(ContentKey.create(ContentType.get("Product"), "a1"));
        cartContents.add(ContentKey.create(ContentType.get("Product"), "a2"));
        s.setCartContents(cartContents);
        List<? extends ContentNodeModel> result = generator.generate(s, input);
        assertNotNull("result not null", result);
        assertEquals("result length 2", 2, result.size());
        Set<String> resultNodes = TestUtils.convertToStringList(result);
        assertTrue("a1", resultNodes.contains("a1"));
        assertTrue("a2", resultNodes.contains("a2"));
        assertEquals("0", "a1", TestUtils.getId(result, 0));
        assertEquals("1", "a2", TestUtils.getId(result, 1));
    }

    public void testRecentItemsVariable() throws CompileException, InvalidContentKeyException {
    	DataGenerator generator = comp.createDataGenerator("recentItems1", "recentItems");
        assertNotNull("generator", generator);
        input.reset();
        HashSet<ContentKey> recentItems = new HashSet<ContentKey>();
        recentItems.add(ContentKey.create(ContentType.get("Product"), "a3"));
        s.setRecentItems(recentItems);
        List<? extends ContentNodeModel> result = generator.generate(s, input);
        assertNotNull("result not null", result);
        assertEquals("result length 2", 1, result.size());
        Set<String> resultNodes = TestUtils.convertToStringList(result);
        assertTrue("a3", resultNodes.contains("a3"));
        assertEquals("0", "a3", TestUtils.getId(result, 0));
    }

}
