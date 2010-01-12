package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.cms.core.MockContentNodeModel;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.dsl.Expression;

public class CachingCompilerTest extends TestCase {

    DataGeneratorCompiler  compiler;
    private SessionInput   input;
    private SessionInput   input2;
    private MockDataAccess dataAccess;

    public CachingCompilerTest() {
    }

    public CachingCompilerTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        compiler = new DataGeneratorCompiler();
        compiler.addVariable("afact", Expression.RET_FLOAT);
        compiler.addVariable("globalfact", Expression.RET_FLOAT);
        compiler.setCaching(true);
        compiler.setGlobalVariables(Collections.singleton("globalfact"));

        input = new SessionInput("12345", null);
        input.setNoShuffle(true);
        input.setCurrentNode(new MockContentNodeModel(FDContentTypes.PRODUCT, "prod1"));
        {
            List explicitList = new ArrayList();
            explicitList.add(new MockContentNodeModel(FDContentTypes.PRODUCT, "exp1"));
            explicitList.add(new MockContentNodeModel(FDContentTypes.PRODUCT, "exp2"));
            input.setExplicitList(explicitList);
        }

        input2 = new SessionInput("12345", null);
        input2.setNoShuffle(true);
        input2.setCurrentNode(new MockContentNodeModel(FDContentTypes.PRODUCT, "prod1"));
        {
            List explicitList = new ArrayList();
            explicitList.add(new MockContentNodeModel(FDContentTypes.PRODUCT, "exp2"));
            explicitList.add(new MockContentNodeModel(FDContentTypes.PRODUCT, "exp4"));
            input2.setExplicitList(explicitList);
        }

        dataAccess = new MockDataAccess() {
            protected Map getVariables(String id) {
                Map result = new HashMap();
                if ("exp1".equals(id)) {
                    result.put("globalfact", new Double(3));
                    result.put("afact", new Double(10));
                }
                if ("exp2".equals(id)) {
                    result.put("globalfact", new Double(0.5));
                    result.put("afact", new Double(7));
                }
                if ("exp4".equals(id)) {
                    result.put("globalfact", new Double(5));
                    result.put("afact", new Double(10));
                }
                return result;
            }
        };
    }

    public void testBasicDecisions() throws CompileException {
        {
            DataGenerator dataGenerator = compiler.createDataGenerator("cache1", "RecursiveNodes(\"department\"):atLeast(globalfact,1)");
            assertTrue("caching enabled", dataGenerator instanceof CachingDataGenerator);

            CachingDataGenerator cdg = (CachingDataGenerator) dataGenerator;
            String cacheKey = cdg.getKey(input);
            assertNotNull("cache key is not null", cacheKey);
            assertEquals("cache key is ", "RecursiveNodes('department'):atLeast(globalfact,1)", cacheKey);
        }
        {
            DataGenerator dataGenerator = compiler.createDataGenerator("cache2", "RecursiveNodes(explicitList):atLeast(globalfact,1)");
            assertTrue("caching enabled", dataGenerator instanceof CachingDataGenerator);

            CachingDataGenerator cdg = (CachingDataGenerator) dataGenerator;
            String cacheKey = cdg.getKey(input);
            assertNotNull("cache key is not null", cacheKey);
            assertEquals("cache key is ", "RecursiveNodes(explicitList):atLeast(globalfact,1)$[exp1,exp2]", cacheKey);

            String cacheKey2 = cdg.getKey(input2);
            assertNotNull("cache key is not null", cacheKey2);
            assertEquals("cache key is ", "RecursiveNodes(explicitList):atLeast(globalfact,1)$[exp2,exp4]", cacheKey2);
        }
        {
            DataGenerator dataGenerator = compiler.createDataGenerator("cache3", "RecursiveNodes(\"department\"):atLeast(afact,1)");
            assertTrue("caching NOT enabled", !(dataGenerator instanceof CachingDataGenerator));
        }
    }

    public void testCachingDataGenerator() throws CompileException, InterruptedException {
        DataGenerator dataGenerator = compiler.createDataGenerator("cache4", "explicitList:atLeast(globalfact,1)");
        assertTrue("caching enabled", dataGenerator instanceof CachingDataGenerator);
        ((CachingDataGenerator)dataGenerator).setCacheEnabled(true);

        List firstLiveResult = dataGenerator.generate(input, dataAccess);
        assertNotNull("result", firstLiveResult);
        assertEquals("result size 1", 1, firstLiveResult.size());
        assertEquals("one node", "[Mock[ContentKey[Product:exp1]]]", firstLiveResult.toString());

        List secondLiveResult = dataGenerator.generate(input2, dataAccess);
        assertNotNull("result", secondLiveResult);
        assertEquals("result size 1", 1, secondLiveResult.size());
        assertEquals("one node", "[Mock[ContentKey[Product:exp4]]]", secondLiveResult.toString());
        
        Thread.sleep(1000);

        List firstResultFromCache = CachingDataGenerator.peekIntoCache("explicitList:atLeast(globalfact,1)$[exp1,exp2]");
        List secondResultFromCache = CachingDataGenerator.peekIntoCache("explicitList:atLeast(globalfact,1)$[exp2,exp4]");

        assertEquals("first result get into the cache", firstLiveResult, firstResultFromCache);
        assertEquals("second result get into the cache", secondLiveResult, secondResultFromCache);

        List cachedLiveResult = dataGenerator.generate(input, dataAccess);
        assertEquals("first result get into the cache", firstLiveResult, cachedLiveResult);
    }
}
