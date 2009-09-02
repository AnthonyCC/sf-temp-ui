package com.freshdirect.fdstore.content;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;


import javax.naming.Context;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;

/**
 * Test case for YMALs and YMAL sets.
 */
public class YmalTest extends FDCustomerManagerTestSupport {

	public YmalTest(String name) {
		super(name);
	}

	ContentServiceI service;

	public void setUp() throws Exception {
		super.setUp();
		
		List list = new ArrayList();
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/Ymals.xml");

		CmsManager.setInstance(new CmsManager(service, null));
		
		Hashtable          env                = new Hashtable();
		// set the context factory to the mockejb context factory
		FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

		env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory() );
		
		MockContextFactory.setAsInitial();
		aspectSystem.add(new FDFactoryProductAspect());
		aspectSystem.add(new FDFactoryProductInfoAspect());		
	}
	
	public void tearDown() {
		MockContextFactory.revertSetAsInitial();
	}

	/**
	 *  Just a very little smoke test.
	 */
	public void testFirst() {
		ProductModel product = (ProductModel) findNode("product_mea0004561");
		assertNotNull(product);
		assertEquals("product mea0004561", product.getFullName());
		assertFalse(product.isUnavailable());
		
		ProductModel productUnav = (ProductModel) findNode("product_mea0004561_unavailable");
		assertNotNull(productUnav);
		assertEquals("product mea0004561 unavailable", productUnav.getFullName());
		assertTrue(productUnav.isUnavailable());
		
		ConfiguredProduct cf = (ConfiguredProduct) findNode("configured_product_mea0004561");
		assertNotNull(cf);
		assertFalse(cf.isUnavailable());
		
		Recipe recipe = (Recipe) findNode("recipe_mea0004561");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		YmalSet ymalSet = (YmalSet) findNode("ymal_set_simple");
		assertNotNull(ymalSet);
		assertTrue(ymalSet.isActive());
	}
	
	/**
	 *  A generic YMAL test, without YMAL sets.
	 *  This test will check if a product that contains items in its
	 *  RELATED_PRODUCTS list returns these items properly.
	 */
	public void testYmalSimple() {
		ProductModel product = (ProductModel) findNode("product_ymal_simple");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 3);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 3);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 3);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
	}

	/**
	 *  Test that unavailable YMAL products and recipes are removed from the
	 *  YMAL list.
	 */
	public void testYmalUnavailableRemoved() {
		ProductModel product = (ProductModel) findNode("product_ymal_some_unavailable");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 4);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004563"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004564"));
		assertEquals(relatedProducts.get(3), findNode("product_mea0004566"));

		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 6);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
		assertEquals(relatedCategories.get(3), findNode("category_mea0004564"));
		assertEquals(relatedCategories.get(4), findNode("category_mea0004565"));
		assertEquals(relatedCategories.get(5), findNode("category_mea0004566"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 4);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004563"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004564"));
		assertEquals(relatedRecipes.get(3), findNode("recipe_mea0004566"));
	}

	/**
	 *  Test to see that if a YMAL list conains the product itself as a YMAL,
	 *  it is removed.
	 */
	public void testYmalRemoveSelf() {
		ProductModel product = (ProductModel) findNode("product_ymal_self_ref");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 3);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 3);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 3);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
	}

	/**
	 *  Test to see that if a YMAL set conains the product itself as a YMAL,
	 *  it is removed.
	 */
	public void testYmalSetRemoveSelf() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_self_ref");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 3);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 3);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 3);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
	}

	/**
	 *  A test to see that each ymal list is at most 6 items long.
	 *  This test does not refer to YMAL sets.
	 */
	public void testCutAt6() {
		ProductModel product = (ProductModel) findNode("product_ymal_more_than_6");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 6);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		assertEquals(relatedProducts.get(3), findNode("product_mea0004564"));
		assertEquals(relatedProducts.get(4), findNode("product_mea0004565"));
		assertEquals(relatedProducts.get(5), findNode("product_mea0004566"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 6);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
		assertEquals(relatedCategories.get(3), findNode("category_mea0004564"));
		assertEquals(relatedCategories.get(4), findNode("category_mea0004565"));
		assertEquals(relatedCategories.get(5), findNode("category_mea0004566"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 6);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
		assertEquals(relatedRecipes.get(3), findNode("recipe_mea0004564"));
		assertEquals(relatedRecipes.get(4), findNode("recipe_mea0004565"));
		assertEquals(relatedRecipes.get(5), findNode("recipe_mea0004566"));
	}

	/**
	 *  Test to check YMAL sets, and that their active status is derived
	 *  correctly from the workflow status and the start and end dates.
	 */
	public void testYmalSetSimple() {
		YmalSet ymalSet;
		boolean caughtException;
		
		ymalSet = (YmalSet) findNode("ymal_set_simple");
		assertNotNull(ymalSet);
		assertEquals(EnumWorkflowStatus.ACTIVE, ymalSet.getWorkflowStatus());
		assertTrue(ymalSet.isActive());

		ymalSet = (YmalSet) findNode("ymal_set_pending");
		assertNotNull(ymalSet);
		assertEquals(EnumWorkflowStatus.PENDING_APPROVAL, ymalSet.getWorkflowStatus());
		assertFalse(ymalSet.isActive());

		ymalSet = (YmalSet) findNode("ymal_set_completed");
		assertNotNull(ymalSet);
		assertEquals(EnumWorkflowStatus.COMPLETED, ymalSet.getWorkflowStatus());
		assertFalse(ymalSet.isActive());
		
		ymalSet = (YmalSet) findNode("ymal_set_ends_in_past");
		assertNotNull(ymalSet);
		assertFalse(ymalSet.isActive());
		
		ymalSet = (YmalSet) findNode("ymal_set_starts_in_future");
		assertNotNull(ymalSet);
		assertFalse(ymalSet.isActive());
		
		caughtException = false;
		try {
			ymalSet = (YmalSet) findNode("ymal_set_ends_before_start");
			assertNotNull(ymalSet);
			assertFalse(ymalSet.isActive());
		} catch (IllegalArgumentException e) {
			caughtException = true;
		}
		assertTrue(caughtException);
	}
	
	/**
	 *  Test to see that that items for the active YMAL set are put after
	 *  the items from RELATED_PRODUCTS.
	 */
	public void testYmalSetConcatenation() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_simple");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 6);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		assertEquals(relatedProducts.get(3), findNode("product_mea0004564"));
		assertEquals(relatedProducts.get(4), findNode("product_mea0004565"));
		assertEquals(relatedProducts.get(5), findNode("product_mea0004566"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 6);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
		assertEquals(relatedCategories.get(3), findNode("category_mea0004564"));
		assertEquals(relatedCategories.get(4), findNode("category_mea0004565"));
		assertEquals(relatedCategories.get(5), findNode("category_mea0004566"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 6);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
		assertEquals(relatedRecipes.get(3), findNode("recipe_mea0004564"));
		assertEquals(relatedRecipes.get(4), findNode("recipe_mea0004565"));
		assertEquals(relatedRecipes.get(5), findNode("recipe_mea0004566"));
	}

	/**
	 *  Test to see that that items for the active YMAL set are put after
	 *  the items from RELATED_PRODUCTS, and that the final list is cut at
	 *  6 items.
	 */
	public void testYmalSetConcatenationCutAt6() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_more_than_6");
		assertNotNull(product);

		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 6);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		assertEquals(relatedProducts.get(3), findNode("product_mea0004564"));
		assertEquals(relatedProducts.get(4), findNode("product_mea0004565"));
		assertEquals(relatedProducts.get(5), findNode("product_mea0004566"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 6);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
		assertEquals(relatedCategories.get(3), findNode("category_mea0004564"));
		assertEquals(relatedCategories.get(4), findNode("category_mea0004565"));
		assertEquals(relatedCategories.get(5), findNode("category_mea0004566"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 6);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
		assertEquals(relatedRecipes.get(3), findNode("recipe_mea0004564"));
		assertEquals(relatedRecipes.get(4), findNode("recipe_mea0004565"));
		assertEquals(relatedRecipes.get(5), findNode("recipe_mea0004566"));
	}
	
	/**
	 *  Test to see that if products are both in RELATED_PRODUCTS and in
	 *  the active YMAL set, they are not shown twice.
	 */
	public void testOverlappingYmals() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_overlapping");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 3);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 3);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 3);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
	}

	/**
	 *  Test to see that if multiple YMAL sets are present, the first one
	 *  is taken into account.
	 */
	public void testYmalSetMultiple() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_multiple");
		assertNotNull(product);
		product.resetActiveYmalSetSession();
		YmalSet ys = product.getActiveYmalSet();
		assertNotNull(ys);
		System.out.println("ymalset:" + ys.getContentKey().getId());
		if ("ymal_set_4_to_6".equals(ys.getContentKey().getId())) {
                    List relatedProducts = product.getYmalProducts();
                    assertEquals(6, relatedProducts.size());
                    assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
                    assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
                    assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
                    assertEquals(relatedProducts.get(3), findNode("product_mea0004564"));
                    assertEquals(relatedProducts.get(4), findNode("product_mea0004565"));
                    assertEquals(relatedProducts.get(5), findNode("product_mea0004566"));
        
                    List relatedCategories = product.getYmalCategories();
                    assertEquals(6, relatedCategories.size());
                    assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
                    assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
                    assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
                    assertEquals(relatedCategories.get(3), findNode("category_mea0004564"));
                    assertEquals(relatedCategories.get(4), findNode("category_mea0004565"));
                    assertEquals(relatedCategories.get(5), findNode("category_mea0004566"));
        
                    List relatedRecipes = product.getYmalRecipes();
                    assertEquals(6, relatedRecipes.size());
                    assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
                    assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
                    assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
                    assertEquals(relatedRecipes.get(3), findNode("recipe_mea0004564"));
                    assertEquals(relatedRecipes.get(4), findNode("recipe_mea0004565"));
                    assertEquals(relatedRecipes.get(5), findNode("recipe_mea0004566"));
		} else if ("ymal_set_simple".equals(ys.getContentKey().getId())) {
                    List relatedProducts = product.getYmalProducts();
                    assertEquals(3, relatedProducts.size());
                    assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
                    assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
                    assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
        
                    List relatedCategories = product.getYmalCategories();
                    assertEquals(3, relatedCategories.size());
                    assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
                    assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
                    assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
        
                    List relatedRecipes = product.getYmalRecipes();
                    assertEquals(3, relatedRecipes.size());
                    assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
                    assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
                    assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
		} else {
		    fail("Unknown ymal set:"+ys.getContentKey().getId());
		}
	}
	
	/**
	 *  Test to see that inavtive YMAL sets are omitted.
	 */
	public void testYmalSetInactive() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_inactive");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 3);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 3);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 3);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
	}

	/**
	 *  Test to see that if multiple YMAL sets are present, the first active
	 *  is taken into account.
	 */
	public void testYmalSetMultipleInactive() {
		ProductModel product = (ProductModel) findNode("product_ymal_set_multiple_inactive");
		assertNotNull(product);
		
		List relatedProducts = product.getYmalProducts();
		assertTrue(relatedProducts.size() == 6);
		assertEquals(relatedProducts.get(0), findNode("product_mea0004561"));
		assertEquals(relatedProducts.get(1), findNode("product_mea0004562"));
		assertEquals(relatedProducts.get(2), findNode("product_mea0004563"));
		assertEquals(relatedProducts.get(3), findNode("product_mea0004564"));
		assertEquals(relatedProducts.get(4), findNode("product_mea0004565"));
		assertEquals(relatedProducts.get(5), findNode("product_mea0004566"));
		
		List relatedCategories = product.getYmalCategories();
		assertTrue(relatedCategories.size() == 6);
		assertEquals(relatedCategories.get(0), findNode("category_mea0004561"));
		assertEquals(relatedCategories.get(1), findNode("category_mea0004562"));
		assertEquals(relatedCategories.get(2), findNode("category_mea0004563"));
		assertEquals(relatedCategories.get(3), findNode("category_mea0004564"));
		assertEquals(relatedCategories.get(4), findNode("category_mea0004565"));
		assertEquals(relatedCategories.get(5), findNode("category_mea0004566"));

		List relatedRecipes = product.getYmalRecipes();
		assertTrue(relatedRecipes.size() == 6);
		assertEquals(relatedRecipes.get(0), findNode("recipe_mea0004561"));
		assertEquals(relatedRecipes.get(1), findNode("recipe_mea0004562"));
		assertEquals(relatedRecipes.get(2), findNode("recipe_mea0004563"));
		assertEquals(relatedRecipes.get(3), findNode("recipe_mea0004564"));
		assertEquals(relatedRecipes.get(4), findNode("recipe_mea0004565"));
		assertEquals(relatedRecipes.get(5), findNode("recipe_mea0004566"));
	}
	
	
	/**
	 *  Test methods defined in YmalSource interface by
	 *  calling them on a ConfiguredProduct instance
	 */
	public void testYmalSourceMethodsOnConfiguredProduct() {
		YmalSource cf = (YmalSource) findNode("configured_product_mea0004561");
		cf.resetActiveYmalSetSession();
		
		assertNotNull(cf);
		// assertFalse(cf.isUnavailable());
		assertTrue(cf instanceof ConfiguredProduct);

		// test getActiveYmalSet()
		YmalSet ys = cf.getActiveYmalSet();
		assertNotNull(ys);
		assertEquals(ys.getContentName(), "ymal_set_4_to_6");
		
		// test getYmalProducts()
		List ymalProducts = cf.getYmalProducts();

		assertNotNull(ymalProducts);
		assertTrue(ymalProducts.size() == 3);
		
		// test getYmalRecipes()
		List ymalRecipes = cf.getYmalRecipes();
		assertTrue(ymalRecipes.size() == 3);

		// test getYmalCategories()
		List ymalCategories = cf.getYmalCategories();
		assertTrue(ymalCategories.size() == 3);
	}

	
	/**
	 *  Test methods defined in YmalSource interface by
	 *  calling them on a Recipe instance
	 */
	public void testYmalSourceMethodsOnRecipe() {
		YmalSource cf = (YmalSource) findNode("recipe_mea0004567");
		cf.resetActiveYmalSetSession();
		assertNotNull(cf);
		// assertFalse(cf.isUnavailable());
		assertTrue(cf instanceof Recipe);

		// test getActiveYmalSet()
		YmalSet ys = cf.getActiveYmalSet();
		assertNotNull(ys);
		assertTrue("ymal_set_4_to_6".equals(ys.getContentName()) || "ymal_set_simple".equals(ys.getContentName()));
		
		// test getYmalProducts()
		List ymalProducts = cf.getYmalProducts();

		assertNotNull(ymalProducts);
		assertTrue(ymalProducts.size() == 3);
		
		// test getYmalRecipes()
		List ymalRecipes = cf.getYmalRecipes();
		assertTrue(ymalRecipes.size() == 3);

		// test getYmalCategories()
		List ymalCategories = cf.getYmalCategories();
		assertTrue(ymalCategories.size() == 3);
	}


	/**
	 *  Find any product specified by its id.
	 *  
	 *  @param id the id of the product to find.
	 *  @return the product by the specified id, if any.
	 */
	private ContentNodeModel findNode(String id) {
		return ContentFactory.getInstance().getContentNode(id);
	}

	public static class FDFactoryProductInfoAspect implements Aspect {

		public Pointcut getPointcut() {
			return new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProductInfo\\(java.lang.String\\)");
		}

		public void intercept(InvocationContext ctx) throws Exception {
			String sku = (String) ctx.getParamVals()[0];
			ctx.setReturnObject(getProductInfo(sku));
		}
	    
		/**
		 * Get current product information object for sku.
		 *
		 * @param sku SKU code
		 *
		 * @return FDProductInfo object
		 *
	 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
		 * @throws FDResourceException if an error occured using remote resources
		 */
		public FDProductInfo getProductInfo(String sku) throws RemoteException, FDSkuNotFoundException, FDResourceException {			
			Date now = new Date();
			String[] materials = {"000000000123"};
			TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
			List erpEntries = new ArrayList();
			FDProductInfo                productInfo;
			
			if (sku.endsWith("_unavailable")) {
				// return this item as unavailable
				// this SKU is included in the ConfiguredProduct "ok"
				// a 0 units available starting now
				erpEntries.add(new ErpInventoryEntryModel(now, 0));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else {
				// fallback: return all other items as available
				// a 1000 units available starting now
				erpEntries.add(new ErpInventoryEntryModel(now, 1000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			}

			return productInfo;
		}
	}

	public static class FDFactoryProductAspect implements Aspect {

		public Pointcut getPointcut() {
			return new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProduct\\(java.lang.String,int\\)");
		}

		public void intercept(InvocationContext ctx) throws Exception {
			String sku = (String) ctx.getParamVals()[0];
			Integer i = (Integer) ctx.getParamVals()[1];
			ctx.setReturnObject(getProduct(sku, i.intValue()));
		}
	
		/**
		 * Get product with specified version. 
		 *
		 * @param sku SKU code
		 * @param version requested version
		 *
		 * @return FDProduct object
		 *
		 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
		 */
	    public FDProduct getProduct(String sku, int version) throws RemoteException, FDSkuNotFoundException, FDResourceException {
	    	
	    	Date          now            = new Date();
	    	FDMaterial    material       = null;
			FDVariation[] variations     = new FDVariation[1];
			variations[0] = new FDVariation(new AttributeCollection(),
					                        "variation",
					                        null);
			FDSalesUnit[] salesUnits     = null;
			Pricing       pricing        = null;
			ArrayList     nutrition      = null;
			
	    	FDProduct     product  = new FDProduct(sku,
		    			                           version,
		    			                           now,
		    			                           material,
		    			                           variations,
		    			                           salesUnits,
		    			                           pricing,
		    			                           nutrition);
	    	
	    	return product;
	    }
		
	}

	protected String[] getAffectedTables() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
