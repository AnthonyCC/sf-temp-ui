package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;

public class DataGeneratorCompilerTest extends TestCase {
    DataGeneratorCompiler comp;

    DataAccess            input;

    SessionInput          s = new SessionInput("ses1");

    protected void setUp() throws Exception {
        comp = new DataGeneratorCompiler();
        comp.addVariable("set", Expression.RET_SET);
        comp.addVariable("set2", Expression.RET_SET);
        comp.addVariable("content", Expression.RET_SET);
        comp.addVariable("content2", Expression.RET_SET);

        comp.addVariable("afact", Expression.RET_INT);
        comp.addVariable("bfact", Expression.RET_FLOAT);

        input = new DataAccess() {
            public List getDatasource(SessionInput input, String name) {
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

                for (int i = 0; i < variables.length; i++) {
                    String var = variables[i];
                    Number number = (Number) varMap.get(var);
                    if (number != null) {
                        result[i] = number.doubleValue();
                    }
                }

                return result;
            }

        };

    }

    public void testSimpleCompilers() throws CompileException {

        {
            DataGenerator dataGenerator = comp.createDataGenerator("test1", "set + set2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 4, collection.size());
            assertTrue("contains a1", collection.contains("a1"));
            assertTrue("contains a2", collection.contains("a2"));
            assertTrue("contains a3", collection.contains("a3"));
            assertTrue("contains b1", collection.contains("b1"));
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("test2", "set - set2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            assertTrue("contains a1", collection.contains("a1"));
            assertTrue("contains a2", collection.contains("a2"));
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("test3", "set * set2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
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
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 0, collection.size());
            assertEquals("no factor", 0, dataGenerator.getFactors().size());
        }

        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest2", "content:between(afact,2,3)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest3", "content:between(afact,2,3):between(afact,1,2)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 1, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));

            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest4", "content:between(afact,2,3) + content2");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 3, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            assertTrue("contains bbb", strings.contains("bbb"));

            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest5", "content:(between(afact,2,3)*between(afact,1,2))");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 1, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
        }
    }

    public void testAtLeastMost() throws CompileException {
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest6", "content:atLeast(afact,2)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a3", strings.contains("a3"));
            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
        {
            DataGenerator dataGenerator = comp.createDataGenerator("atest7", "content:atMost(afact,2)");
            assertNotNull("dataGenerator", dataGenerator);
            Collection collection = dataGenerator.generate(s, input);
            assertNotNull("result collection", collection);
            assertEquals("result collection size", 2, collection.size());
            Collection strings = TestUtils.convertToStringList(collection);
            assertTrue("contains a2", strings.contains("a2"));
            assertTrue("contains a1", strings.contains("a1"));
            assertEquals("afact factor", 1, dataGenerator.getFactors().size());
            assertTrue("afact factor", dataGenerator.getFactors().contains("afact"));
        }
    }
    
    public void testYmalRelated() throws CompileException {
        DataGenerator dataGenerator = comp.createDataGenerator("ymal1", "RecursiveNodes(currentProduct)");
        assertNotNull("dataGenerator", dataGenerator);
        Collection collection = dataGenerator.generate(s, input);
    }
    
    public void testYmalValidations() {
        try {
            DataGenerator dataGenerator = comp.createDataGenerator("ymalVal1", "RecursiveNodes(3)");
            fail("should fail, with invalid parameter type");
        } catch (CompileException e) {
            assertEquals("type error", CompileException.TYPE_ERROR, e.getCode());
            assertEquals("error message", "The 1. parameter type is integer instead of the expected node/set/string!", e.getMessage());
        }
    }
    


}
