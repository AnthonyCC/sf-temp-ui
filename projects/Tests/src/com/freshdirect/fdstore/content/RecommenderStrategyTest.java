package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.Context;

import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.event.RecommendationEventLogger;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.RecommendationEventLoggerMockup;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.smartstore.impl.FavoritesRecommendationService;

import junit.framework.TestCase;

public class RecommenderStrategyTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();

		XmlTypeService typeService = new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml");     

		XmlContentService service = new XmlContentService( typeService, new FlexContentHandler(), 
        		"classpath:/com/freshdirect/fdstore/content/RecommenderStrategyTest.xml" );

        CmsManager.setInstance(new CmsManager(service, null));

	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
    public void testRecommenderDefaultValues() {    	
    	
    	Recommender recommender = null;
		recommender = (Recommender) ContentFactory.getInstance().getContentNode("recommender_default");
		
    	RecommenderStrategy recommenderStrategy = null;
		recommenderStrategy = (RecommenderStrategy) ContentFactory.getInstance().getContentNode("recommender_strategy_default");
		
		assertNotNull("default recommender node is null", recommender );
		assertNotNull("default recommender strategy node is null", recommenderStrategy );
		assertNotNull("default recommender strategy node is null", recommender.getStrategy() );
		assertSame( "recommender strategy nodes are not the same", recommenderStrategy, recommender.getStrategy() );

		assertNotNull( recommender.getScope() );
		assertEquals( 0, recommender.getScope().size() );
		
		assertEquals( "wrong value for generator", "generator_expr", recommender.getStrategy().getGenerator() );		
		assertNull( "wrong default value for scoring", recommender.getStrategy().getScoring() );		
		assertEquals( "wrong default value for sampling", "deterministic", recommender.getStrategy().getSampling() );
		assertEquals( "wrong default value for top_n", 20, recommender.getStrategy().getTopN() );
		assertEquals( "wrong default value for top_percent", 20.0d, recommender.getStrategy().getTopPercent(), 0.001d );
		assertEquals( "wrong default value for exponent", 0.66d, recommender.getStrategy().getExponent(), 0.001d );		
		
    }

    public void testRecommenderExplicitValues() {    	
    	
    	Recommender recommender = null;
		recommender = (Recommender) ContentFactory.getInstance().getContentNode("recommender_full");
		
    	RecommenderStrategy recommenderStrategy = null;
		recommenderStrategy = (RecommenderStrategy) ContentFactory.getInstance().getContentNode("recommender_strategy_full");
		
		assertNotNull("recommender node is null", recommender );
		assertNotNull("recommender strategy node is null", recommenderStrategy );
		assertNotNull("recommender strategy node is null", recommender.getStrategy() );
		assertSame( "recommender strategy nodes are not the same", recommenderStrategy, recommender.getStrategy() );

		ProductModel model = null;
		model = (ProductModel) ContentFactory.getInstance().getContentNode("product_mea0004561");
		
		assertNotNull( recommender.getScope() );
		assertEquals( 1, recommender.getScope().size() );		
		assertNotNull( "product model is null", model );				
		assertEquals( "scope is not equal to expected product model", model, recommender.getScope().get(0) );		
		assertEquals( "wrong value for generator", "generator_expr", recommender.getStrategy().getGenerator() );		
		assertEquals( "wrong value for scoring", "scoring_expr", recommender.getStrategy().getScoring() );		
		assertEquals( "wrong value for sampling", "harmonic", recommender.getStrategy().getSampling() );
		assertEquals( "wrong value for top_n", 50, recommender.getStrategy().getTopN() );
		assertEquals( "wrong value for top_percent", 12.5d, recommender.getStrategy().getTopPercent(), 0.001d );
		assertEquals( "wrong value for exponent", 0.15d, recommender.getStrategy().getExponent(), 0.001d );		
		
    }

    
    public void testRecommenderMultiScope() {    	
    	
    	Recommender recommender = null;
		recommender = (Recommender) ContentFactory.getInstance().getContentNode("recommender_multiscope");
		
    	RecommenderStrategy recommenderStrategy = null;
		recommenderStrategy = (RecommenderStrategy) ContentFactory.getInstance().getContentNode("recommender_strategy_full");
		
		assertNotNull("recommender node is null", recommender );
		assertNotNull("recommender strategy node is null", recommenderStrategy );
		assertNotNull("recommender strategy node is null", recommender.getStrategy() );
		assertSame( "recommender strategy nodes are not the same", recommenderStrategy, recommender.getStrategy() );

		// ---
		
		ProductModel product = null;
		product = (ProductModel) ContentFactory.getInstance().getContentNode("product_mea0004561");
		assertNotNull( "product model is null", product );

		CategoryModel category = null;
		category = (CategoryModel) ContentFactory.getInstance().getContentNode("category_mea0004561");
		assertNotNull( "category model is null", category );
		
		ConfiguredProduct confProd = null;
		confProd = (ConfiguredProduct) ContentFactory.getInstance().getContentNode("configured_product_mea0004561a");
		assertNotNull( "configured product model is null", confProd );
		
		ConfiguredProductGroup confProdGrp = null;
		confProdGrp = (ConfiguredProductGroup) ContentFactory.getInstance().getContentNode("configured_product_group_mea0004561");
		assertNotNull( "configured product group model is null", confProdGrp );

		DepartmentModel department = null;
		department = (DepartmentModel) ContentFactory.getInstance().getContentNode("mea");
		assertNotNull( "department model is null", department );
		
		FavoriteList favList = null;
		favList = (FavoriteList) ContentFactory.getInstance().getContentNode("fd_favorites");
		assertNotNull( "favorite list model is null", favList );
		
		List scope = recommender.getScope();
		assertNotNull( scope );
		assertEquals( 6, recommender.getScope().size() );
		
		assertTrue( "product is missing from scope", scope.contains(product) );
		assertTrue( "category is missing from scope", scope.contains(category) );
		assertTrue( "configured product is missing from scope", scope.contains(confProd) );
		assertTrue( "configured product group is missing from scope", scope.contains(confProdGrp) );
		assertTrue( "department is missing from scope", scope.contains(department) );
		assertTrue( "favorite list is missing from scope", scope.contains(favList) );
		
    }
    
}
