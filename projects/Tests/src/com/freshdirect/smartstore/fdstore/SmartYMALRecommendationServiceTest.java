package com.freshdirect.smartstore.fdstore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.SmartYMALRecommendationService;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

public class SmartYMALRecommendationServiceTest extends RecommendationServiceTestBase {
    
    private RecommendationService firs;
    SessionInput input ;
    
    protected String getCmsXmlName() {
        return "classpath:/com/freshdirect/cms/fdstore/content/YmalSet.xml";
    }
    
    public void setUp() throws Exception {
        super.setUp();
        input = new SessionInput("12345", null, null);
        input.setNoShuffle(true);
        input.setMaxRecommendations(10);
    }

    
    protected void initAspects(AspectSystem aspectSystem) {
        aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("sku_1", 2.0).addAvailableSku("sku_2", 2.0).addAvailableSku("sku_3", 2.0)
                .addAvailableSku("sku_4", 2.0).addAvailableSku("sku_5", 2.0).addAvailableSku("sku_6", 2.0).addAvailableSku("sku_7", 2.0)
                .addAvailableSku("sku_8", 2.0).addAvailableSku("sku_9", 2.0).addAvailableSku("sku_10", 2.0)
        );
    }
    
    RecommendationService getSmartYmalService() {
        if (firs == null) {
            firs = new SmartYMALRecommendationService(new Variant("smart_ymal", EnumSiteFeature.YMAL, new RecommendationServiceConfig("smart_ymal_config",
                    RecommendationServiceType.SMART_YMAL)),
                    RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("smart_ymal_config",
                    RecommendationServiceType.SMART_YMAL), new java.util.HashMap()), false, false);
        }
        return firs;
    }
    
    
    public void testSmartYmalService() {
        ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNode("prod_1");
        
        input.setCurrentNode(product);
        input.setYmalSource(product);
        
        {        
            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 4, nodes.size());
            Set nodeNames = TestUtils.convertToStringList(nodes);
            contains(nodeNames, "prod_2");
            contains(nodeNames, "prod_3");
            contains(nodeNames, "prod_4");
            contains(nodeNames, "prod_5");
        }
        {
            // no ymal set
            product = (ProductModel) ContentFactory.getInstance().getContentNode("prod_2");
            input.setCurrentNode(product);
            input.setYmalSource(product);

            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 0, nodes.size());
        }
        {
            product = (ProductModel) ContentFactory.getInstance().getContentNode("prod_3");
            input.setCurrentNode(product);
            input.setYmalSource(product);

            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 2, nodes.size());
            Set nodeNames = TestUtils.convertToStringList(nodes);
            contains(nodeNames, "prod_4");
            contains(nodeNames, "prod_5");
        }
    }
    
    public void testFallbackToRelatedProducts() {
        ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNode("prod_5");
        input.setCurrentNode(product);
        input.setYmalSource(product);
        {        
            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 1, nodes.size());
            Set nodeNames = TestUtils.convertToStringList(nodes);
            contains(nodeNames, "prod_1");
        }
    }
    

    public void testOtherScript() {
        ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNode("prod_6");
        input.setCurrentNode(product);
        input.setYmalSource(product);
        {        
            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 5, nodes.size());
            Set nodeNames = TestUtils.convertToStringList(nodes);
            // ymal_4/ymals : <Product ref="prod_3"/>
            contains(nodeNames, "prod_3");
            // script recommender 
            contains(nodeNames, "prod_7");
            contains(nodeNames, "prod_8");
            contains(nodeNames, "prod_9");
            contains(nodeNames, "prod_10");
            
            atPos(nodes, 0, "prod_7");
            atPos(nodes, 1, "prod_8");
            atPos(nodes, 2, "prod_9");
            atPos(nodes, 3, "prod_10");
            // ymal_4/ymals : <Product ref="prod_3"/>
            atPos(nodes, 4, "prod_3");
        }
    }
    
    public void testRandomizedRecommender() {
        ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNode("prod_7");
        input.setCurrentNode(product);
        input.setYmalSource(product);
        
        {        
            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 5, nodes.size());
            Set nodeNames = TestUtils.convertToStringList(nodes);
            // ymal_4/ymals : <Product ref="prod_3"/>
            contains(nodeNames, "prod_4");
            // script recommender 
            contains(nodeNames, "prod_6");
            contains(nodeNames, "prod_8");
            contains(nodeNames, "prod_9");
            contains(nodeNames, "prod_10");
            
            atPos(nodes, 0, "prod_6");
            atPos(nodes, 1, "prod_8");
            atPos(nodes, 2, "prod_9");
            atPos(nodes, 3, "prod_10");
            atPos(nodes, 4, "prod_4");
            // ymal_4/ymals : <Product ref="prod_3"/>
        }
        
        
        // now, test with randomization
        input.setNoShuffle(false);
        Set firstNames = new HashSet();
        Set lastNames = new HashSet();
        
        for (int i=0;i<10;i++) {
            List nodes = getSmartYmalService().recommendNodes(input);
            assertNotNull("nodes", nodes);
            assertEquals("node size", 5, nodes.size());
            firstNames.add(getId(nodes, 0));
            lastNames.add(getId(nodes, 4));
        }
        assertTrue("first recommendation should be more then 1 different ID (ids:"+firstNames+")", firstNames.size()>1);
        assertEquals("5th recommendation should be more 1 ID (ids:"+lastNames+")", 1, lastNames.size());
    }
    
    
    private void contains(Set nodeNames, String name) {
        assertTrue("contains "+name + " ("+nodeNames+')', nodeNames.contains(name));
    }
    private void atPos(List nodes, int pos, String name) {
        assertEquals("node at "+pos+" is "+name, name, getId(nodes, pos));
    }

    private String getId(List nodes, int pos) {
        return ((ContentNodeModel) nodes.get(pos)).getContentKey().getId();
    }
    
    
}
