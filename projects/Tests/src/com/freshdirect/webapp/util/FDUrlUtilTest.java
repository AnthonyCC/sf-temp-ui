package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.util.prodconf.AutoConfigurationStrategy;

import junit.framework.TestCase;

/**
 * Test case for url generating - {@link FDURLUtil} class
 * @author treer
 *
 */

public class FDUrlUtilTest extends TestCase {

    XmlContentService service;
    
	protected void setUp() throws Exception {
		super.setUp();		
		
        List list = new ArrayList();
        list.add( new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml") );
        CompositeTypeService typeService = new CompositeTypeService( list ); 
        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml");
        CmsManager.setInstance( new CmsManager(service, null) );        
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testProductURI () {
		
		// Dutch Country Stroehmann Honey White Bread
		String productId = "gro_stroe_hnywhite";
		String categoryId = "gro_baker_white";
		String deptId = "bak";
		String impId = "123566";
		String trackingCode = "ymal";
		String trackingCodeEx = "deal";
		String variantId = "std-ymal";
		int rank = 3;
		
		ProductModel product = null;
		ConfiguredProduct confProduct = null;
		Variant variant = null; 
		
		try { 
			
			ContentKey contentKey = ContentKey.create( FDContentTypes.PRODUCT, productId );
			assertNotNull("contentKey is null", contentKey);
			
			product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey( contentKey );
			assertNotNull("product node is null", product);
			
			ContentKey confContentKey = ContentKey.create( FDContentTypes.PRODUCT, "vrst_rbeyerst" );
			assertNotNull("confContentKey is null", contentKey);
			confProduct = (ConfiguredProduct) ContentFactory.getInstance().getContentNodeByKey( confContentKey );
			assertNotNull("configured product node is null", product);			
			
			variant = new Variant(variantId, EnumSiteFeature.YMAL, new RecommendationServiceConfig(variantId,RecommendationServiceType.CLASSIC_YMAL) );
			assertNotNull("variant is null", variant);
			
		} catch (InvalidContentKeyException e) {
			e.printStackTrace();
			fail( "invalid content key" );
		}
				
		
		String u1  = FDURLUtil.getProductURI( product, trackingCode );
		String u2  = FDURLUtil.getProductURI( product, trackingCode, variantId );
		String u3a = FDURLUtil.getProductURI( product, trackingCode, trackingCodeEx, rank );
		String u3b = FDURLUtil.getProductURI( product, (String)null, (String)null, rank );
		String u4  = FDURLUtil.getProductURI( product, variantId, trackingCode, trackingCodeEx, rank );
		String u5  = FDURLUtil.getProductURI( product, variant );
		String u6  = FDURLUtil.getProductURI( product, variant, trackingCodeEx, rank );
                String u9  = FDURLUtil.getProductURI( product, variant.getId(), trackingCode, trackingCodeEx, rank, impId );
		
		String u7  = FDURLUtil.getCategoryURI(categoryId, trackingCode);
		String u8  = FDURLUtil.getDepartmentURI(deptId, trackingCode);
		
		Image img = FDURLUtil.getProductImage( product );
		assertNotNull( "image object is null" , img );
		
//		ConfigurationContext confContext = new ConfigurationContext();
//		confContext.setFDUser( new FDUser() );
//
//		FDConfiguration config = new FDConfiguration( 10.0, "db", new HashMap() );
//		//ProductImpression prodImpr = new AutoConfigurationStrategy().configure(confProduct, confContext);
//		
//		FDURLUtil.getConfiguredProductURI( confProduct, trackingCode, config );
		
		final String SEP = "&amp;"; //FDURLUtil.URL_PARAM_SEP;
		
		assertEquals("url generating error", u1,  "/product.jsp?catId="+categoryId+SEP+	"trk="+trackingCode+SEP+	"productId="+productId );		
		assertEquals("url generating error", u2,  "/product.jsp?catId="+categoryId+SEP+	"trk="+trackingCode+SEP+	"productId="+productId+SEP+	"variant="+variantId+SEP+	"fdsc.source=SS" );
		assertEquals("url generating error", u3a, "/product.jsp?catId="+categoryId+SEP+	"trk="+trackingCode+SEP+	"productId="+productId+SEP+	"trkd="+trackingCodeEx+SEP+	"rank="+rank );
		assertEquals("url generating error", u3b, "/product.jsp?catId="+categoryId+SEP+	"trk=srch"+SEP+				"productId="+productId+SEP+	"rank="+rank );
		assertEquals("url generating error", u4,  "/product.jsp?catId="+categoryId+SEP+	"productId="+productId+SEP+	"variant="+variantId+SEP+	"trk="+trackingCode+SEP+	"trkd="+trackingCodeEx+SEP+	"rank="+rank );		
		assertEquals("url generating error", u5,  "/product.jsp?catId="+categoryId+SEP+	"trk="+trackingCode+SEP+	"productId="+productId+SEP+	"variant="+variantId+SEP+	"fdsc.source=SS" );
		assertEquals("url generating error", u6,  "/product.jsp?catId="+categoryId+SEP+	"productId="+productId+SEP+	"variant="+variantId+SEP+	"trk="+trackingCode+SEP+	"trkd="+trackingCodeEx+SEP+	"rank="+rank );		
		
		assertEquals("url generating error", u7,  "/category.jsp?catId="+categoryId+SEP+"trk="+trackingCode );	
		assertEquals("url generating error", u8,  "/department.jsp?deptId="+deptId+SEP+	"trk="+trackingCode );	
		
                assertEquals("url generating error", u9,  "/product.jsp?catId="+categoryId+SEP+ "productId="+productId+SEP+     "variant="+variantId+SEP+       "trk="+trackingCode+SEP+        "trkd="+trackingCodeEx+SEP+     "rank="+rank+SEP  + "impId="+impId );         
		
	}
	
//	public static String getConfiguredProductURI(ConfiguredProduct productNode, String trackingCode, FDConfigurableI config) {
	
//	public static String getCategoryURI(HttpServletRequest request, ProductModel productNode) {
//	...


	
}
