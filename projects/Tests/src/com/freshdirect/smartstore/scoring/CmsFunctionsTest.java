package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.ScoreProvider;

public class CmsFunctionsTest extends TestCase {
    private final static Logger LOGGER = Logger.getLogger(CmsFunctionsTest.class);

    private DataGeneratorCompiler comp;
    
    private XmlContentService service;

    SessionInput          s;

    DataAccess da;
    
    protected void setUp() throws Exception {
        super.setUp();

        {
            List list = new ArrayList();
        
            list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
    
            CompositeTypeService typeService = new CompositeTypeService(list);
    
            service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");
            
            CmsManager.setInstance(new CmsManager(service, null));
        }
        
        comp = new DataGeneratorCompiler(ScoreProvider.ZONE_DEPENDENT_FACTORS_ARRAY);
        {   
            s = new SessionInput("ses1", null, null);
            s.setCurrentNode(ContentFactory.getInstance().getContentNode("cfncndy_ash_mcrrd"));
            
            List explicitList = new ArrayList(2);
            explicitList.add(ContentFactory.getInstance().getContentNode("dai_organi_2_milk_02"));
            explicitList.add(ContentFactory.getInstance().getContentNode("xxx_2"));
            s.setExplicitList(explicitList);
        }
        
        da = new DataAccess() {
            public List getDatasource(SessionInput input, String name) {
                LOGGER.info("getDatasource called with name: '"+name+"'");
                return Collections.EMPTY_LIST;
            }
            public double[] getVariables(String userId, PricingContext pricingContext, ContentNodeModel contentNode, String[] variables) {
                LOGGER.info("getVariables called with name: '"+variables+"'");
                return new double[variables.length];
            }
            
            @Override
            public boolean addPrioritizedNode(ContentNodeModel model) {
            	return false;
            }
            
            @Override
            public List<ContentNodeModel> getPrioritizedNodes() {
            	return Collections.EMPTY_LIST;
            }
        };
        
    }
    
    public void testBasicFunctions() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("cms_test1", "toList(currentProduct)");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains cfncndy_ash_mcrrd", strings.contains("cfncndy_ash_mcrrd"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("cms_test2", "toList(\"dai_orgval_laclfmilkhlf\")");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains dai_orgval_laclfmilkhlf", strings.contains("dai_orgval_laclfmilkhlf"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("cms_test3", "toList(\"dai_orgval_laclfmilkhlf\") + toList(currentProduct)");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains cfncndy_ash_mcrrd", strings.contains("cfncndy_ash_mcrrd"));
            assertTrue("contains dai_orgval_laclfmilkhlf", strings.contains("dai_orgval_laclfmilkhlf"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("cms_test4", "toList(\"dai_orgval_laclfmilkhlf\",currentProduct)");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains cfncndy_ash_mcrrd", strings.contains("cfncndy_ash_mcrrd"));
            assertTrue("contains dai_orgval_laclfmilkhlf", strings.contains("dai_orgval_laclfmilkhlf"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("cms_test5", "toList(\"dai_orgval_whlmilk_01\",currentProduct, explicitList)");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 4, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains cfncndy_ash_mcrrd", strings.contains("cfncndy_ash_mcrrd"));
            assertTrue("contains dai_orgval_laclfmilkhlf", strings.contains("dai_orgval_whlmilk_01"));
            assertTrue("contains dai_organi_2_milk_02", strings.contains("dai_organi_2_milk_02"));
            assertTrue("contains xxx_2", strings.contains("xxx_2"));
        }
    }
    
    public void testParentCategory() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("rel_test1", "toList(ParentCategory(currentProduct))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains gro_candy_blkch", strings.contains("gro_candy_blkch"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rel_test2", "toList(ParentCategory(\"dai_orgval_laclfmilkhlf\"))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains dai_milk_1per", strings.contains("dai_milk_1per"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rel_test3", "toList(ParentCategory(ParentCategory(currentProduct)))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains gro_choc_fine", strings.contains("gro_choc_fine"));
        }
    }

    public void testToplevelCategory() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("toplevel_1", "toList(TopLevelCategory(currentProduct))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains gro", strings.contains("gro_candy"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("toplevel_2", "toList(TopLevelCategory(\"dai_orgval_laclfmilkhlf\"))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains dai_milk_1per", strings.contains("dai_milk"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("toplevel_3", "toList(TopLevelCategory(ParentCategory(currentProduct)))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains gro_choc_fine", strings.contains("gro_candy"));
        }
    }

    public void testRecursiveNodes() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("rec_test_1", "RecursiveNodes(ParentCategory(\"xxx_1\"))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
            assertTrue("contains xxx_2", strings.contains("xxx_2"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_test_2", "RecursiveNodes(ParentCategory(\"xxx_1\"), \"dai_milk_1per\")");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 3, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
            assertTrue("contains xxx_2", strings.contains("xxx_2"));
            assertTrue("contains dai_organi_1_milk_01", strings.contains("dai_organi_1_milk_01"));
        }
    }

    public void testRecursiveNodesExcept() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_1", "RecursiveNodesExcept(ParentCategory(\"xxx_1\"))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
            assertTrue("contains xxx_2", strings.contains("xxx_2"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_2", "RecursiveNodesExcept(ParentCategory(\"xxx_1\"), \"xxx_2\")");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_3", "RecursiveNodesExcept(Department(\"xxx_1\"), TopLevelCategory(\"xxx_1\"))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_3", strings.contains("xxx_3"));
            assertTrue("contains xxx_4", strings.contains("xxx_4"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_4", "RecursiveNodesExcept(Department(\"xxx_1\"), TopLevelCategory(\"xxx_1\"), \"xxx_4\")");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_3", strings.contains("xxx_3"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_5", "RecursiveNodesExcept(Department(\"xxx_1\"), explicitList)");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 3, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
            assertTrue("contains xxx_3", strings.contains("xxx_3"));
            assertTrue("contains xxx_4", strings.contains("xxx_4"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_6", "RecursiveNodesExcept(toList(\"xx_cat_1\",\"xx_cat_2\"), explicitList)");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 3, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
            assertTrue("contains xxx_3", strings.contains("xxx_3"));
            assertTrue("contains xxx_4", strings.contains("xxx_4"));
        }
        {
            DataGenerator generator = comp.createDataGenerator("rec_ex_test_7", "RecursiveNodesExcept(toList(\"xx_cat_1\",\"xx_cat_2\"), explicitList, \"xxx_4\")");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_1", strings.contains("xxx_1"));
            assertTrue("contains xxx_3", strings.contains("xxx_3"));
        }
    }
    
    
    public void testRecursiveNodeOptimizer() throws CompileException {
        {
            comp.setOptimize(true);
            DataGenerator generator = comp.createDataGenerator("optim_1", "RecursiveNodes(\"xx_cat_1\") - toList(\"xxx_1\")");
            assertNotNull("generator", generator);
            assertEquals("optimization description", "RecursiveNodes('xx_cat_1') - toList('xxx_1') -> optimized to : RecursiveNodesExcept('xx_cat_1','xxx_1')", generator.toString());
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_2", strings.contains("xxx_2"));
        }
        {
            comp.setOptimize(false);
            DataGenerator generator = comp.createDataGenerator("optim_2", "RecursiveNodes(\"xx_cat_1\") - toList(\"xxx_1\")");
            assertNotNull("generator", generator);
            assertEquals("optimization description", "RecursiveNodes('xx_cat_1') - toList('xxx_1')", generator.toString());
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains xxx_2", strings.contains("xxx_2"));
        }
    }

    
    public void testExplicitList() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("expl_test_1", "explicitList");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 2, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains gro", strings.contains("dai_organi_2_milk_02"));
            assertTrue("contains gro", strings.contains("xxx_2"));
        }
    }

    public void testDepartmentFunction() throws CompileException {
        {
            DataGenerator generator = comp.createDataGenerator("dep_test1", "toList(Department(currentProduct))");
            List list = generator.generate(s, da);
            assertNotNull("list", list);
            assertEquals("list size", 1, list.size());
            Collection strings = TestUtils.convertToStringList(list);
            assertTrue("contains gro_candy_blkch", strings.contains("gro"));
        }
    }

    public void testTypeErrorHandling() {
        try {
            DataGenerator dataGenerator = comp.createDataGenerator("err_1", "toList(3)");
            fail("should fail, with invalid parameter type");
        } catch (CompileException e) {
            assertEquals("type error", CompileException.TYPE_ERROR, e.getCode());
            assertEquals("error message", "The 1. parameter type is integer instead of the expected node/set/string!", e.getMessage());
        }
        try {
            DataGenerator dataGenerator = comp.createDataGenerator("err_2", "toList(currentProduct, 3)");
            fail("should fail, with invalid parameter type");
        } catch (CompileException e) {
            assertEquals("type error", CompileException.TYPE_ERROR, e.getCode());
            assertEquals("error message", "The 2. parameter type is integer instead of the expected node/set/string!", e.getMessage());
        }
        
    }
    
    
}
