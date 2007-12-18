package com.freshdirect.fdstore.content;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

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
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ConfiguredProductGroupAvailabilityTest.FDFactoryProductAspect;
import com.freshdirect.fdstore.content.ConfiguredProductGroupAvailabilityTest.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;

/**
 * Test case for the availability of ConfiguredProducts.
 */
public class ProductAutoconfigureTest extends FDCustomerManagerTestSupport {

	public ProductAutoconfigureTest(String name) {
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
				"classpath:/com/freshdirect/cms/fdstore/ConfiguredProducts.xml");

		CmsManager.setInstance(new CmsManager(service, null));
		
		Context            context            = null;
		Hashtable          env                = new Hashtable();
		MockContextFactory mockContextFactory = new MockContextFactory();

		// set the context factory to the mockejb context factory
		FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

		env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory() );
		
		MockContextFactory.setAsInitial();
		context = mockContextFactory.getInitialContext(env);

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
		ProductModelImpl node = (ProductModelImpl) findProduct("product_autoconfigurable");
		assertNotNull(node);
	}

	/**
	 *  Test to see if a product with multiple SKUs can not be auto-configured.
	 */
	public void testMultipleSku() {
		ProductModelImpl product = (ProductModelImpl)
										findProduct("product_multi_sku");
		assertNotNull(product);
		assertFalse(product.isAutoconfigurable());
		assertNull(product.getAutoconfiguration());
	}

	/**
	 *  Test to see if a product with multiple variation options
	 *  can not be auto-configured.
	 */
	public void testMultipleVariationOptions() {
		ProductModelImpl product = (ProductModelImpl)
								findProduct("product_multi_variation_options");
		assertNotNull(product);
		assertFalse(product.isAutoconfigurable());
		assertNull(product.getAutoconfiguration());
	}

	/**
	 *  Test to see if a product with multiple sales units
	 *  can not be auto-configured.
	 */
	public void testMultipleSalesUnits() {
		ProductModelImpl product = (ProductModelImpl)
								findProduct("product_multi_sales_units");
		assertNotNull(product);
		assertFalse(product.isAutoconfigurable());
		assertNull(product.getAutoconfiguration());
	}

	/**
	 *  Test to see if a product with a single sales unit and no variation
	 *  options can be auto-confifgured.
	 */
	public void testAutoconfigurable() {
		ProductModelImpl product = (ProductModelImpl)
								findProduct("product_autoconfigurable");
		assertNotNull(product);
		assertTrue(product.isAutoconfigurable());
		
		FDConfigurableI configuration = product.getAutoconfiguration();
		assertNotNull(configuration);
		assertEquals(configuration.getQuantity(), 1.0, 0.001);
		assertEquals(configuration.getSalesUnit(), "SalesUnit1");
		assertTrue(configuration.getOptions().isEmpty());
	}

	/**
	 *  Test to see if a product with a single sales unit and single variation
	 *  options can be auto-confifgured.
	 */
	public void testAutoconfigurableVariationOptions() {
		ProductModelImpl product = (ProductModelImpl)
								findProduct("product_autoconfigurable_variation_options");
		assertNotNull(product);
		assertTrue(product.isAutoconfigurable());

		FDConfigurableI configuration = product.getAutoconfiguration();
		Map             options       = configuration.getOptions();
		assertNotNull(configuration);
		assertEquals(configuration.getQuantity(), 1.0, 0.001);
		assertEquals(configuration.getSalesUnit(), "SalesUnit1");
		assertFalse(options.isEmpty());
		assertTrue(options.containsKey("variation1"));
		assertEquals(options.get("variation1"), "VariationOption1");
		assertTrue(options.containsKey("variation2"));
		assertEquals(options.get("variation2"), "VariationOption2");
	}

	
	private ContentNodeModel findProduct(String id) {
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
			List inventoryEntries = new ArrayList();
			TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
			FDProductInfo                productInfo;

			// return this item as available
			// this SKU is not included in the ConfiguredProduct "ok"
			// a 10000 units available starting now
			inventoryEntries.add(new ErpInventoryEntryModel(now, 10000));
			inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));

			
			productInfo    = new FDProductInfo(sku,
					                           1,
					                           1.0,
					                           "ea",
					                           materials,
					                           EnumATPRule.MATERIAL,
					                           EnumAvailabilityStatus.AVAILABLE,
					                           now,
					                           "", inventoryCache);

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
	    	Date          			now;
	    	FDMaterial    			material;
			FDVariation[] 			variations;
			FDVariationOption[]		variationOptions;
			FDSalesUnit[] 			salesUnits;
			Pricing       			pricing;
			ArrayList     			nutrition;
			Map           			nutritionInfo;
	    	FDProduct     			product;

	    	if ("FRU0004715".equals(sku)) {
		    	// this is a product with multiple variation options
		    	now            = new Date();
		    	material       = null;
		    	
		    	variationOptions = new FDVariationOption[2];
		    	variationOptions[0] = new FDVariationOption(null,
						"VariationOption1",
						"test variation option 1");
		    	variationOptions[1] = new FDVariationOption(null,
						"VariationOption2",
						"test variation option 2");
				variations     = new FDVariation[1];
				variations[0] = new FDVariation(new AttributeCollection(),
						                        "variation",
						                        variationOptions);
				
				salesUnits     = new FDSalesUnit[1];
				salesUnits[0] = new FDSalesUnit(null,
												"SalesUnit1",
												"test sales unit 1");
				pricing        = null;
				nutrition      = null;
				nutritionInfo  = null;
				
	    	} else if ("FRU0005131".equals(sku)) {
		    	// this is a product with multiple sales units
		    	now            = new Date();
		    	material       = null;
				variations     = new FDVariation[1];
				variations[0] = new FDVariation(new AttributeCollection(),
						                        "variation",
						                        null);
				
				salesUnits     = new FDSalesUnit[2];
				salesUnits[0] = new FDSalesUnit(null,
						"SalesUnit1",
						"test sales unit 1");
				salesUnits[1] = new FDSalesUnit(null,
						"SalesUnit2",
						"test sales unit 2");
				pricing        = null;
				nutrition      = null;
				nutritionInfo  = null;
				
	    	} else if ("FRU0005133".equals(sku)) {
		    	// this is a product which can be auto-configured, and
	    		// has no variation options
		    	now            = new Date();
		    	material       = null;
				variations     = new FDVariation[0];
				salesUnits     = new FDSalesUnit[1];
				salesUnits[0] = new FDSalesUnit(null,
						"SalesUnit1",
						"test sales unit 1");
				pricing        = null;
				nutrition      = null;
				nutritionInfo  = null;
				
	    	} else if ("VEG0010983".equals(sku)) {
		    	// this is a product which can be auto-configured, and
	    		// has single variation options
		    	now            = new Date();
		    	material       = null;
		    	
				variations     = new FDVariation[2];
		    	variationOptions = new FDVariationOption[1];
		    	variationOptions[0] = new FDVariationOption(null,
						"VariationOption1",
						"test variation option 1");
				variations[0] = new FDVariation(new AttributeCollection(),
						                        "variation1",
						                        variationOptions);
		    	variationOptions = new FDVariationOption[1];
		    	variationOptions[0] = new FDVariationOption(null,
						"VariationOption2",
						"test variation option 2");
				variations[1] = new FDVariation(new AttributeCollection(),
						                        "variation2",
						                        variationOptions);
				
				salesUnits     = new FDSalesUnit[1];
				salesUnits[0] = new FDSalesUnit(null,
						"SalesUnit1",
						"test sales unit 1");
				pricing        = null;
				nutrition      = null;
				nutritionInfo  = null;
				
	    	} else {
		    	now            = new Date();
		    	material       = null;
				variations     = new FDVariation[1];
				variations[0] = new FDVariation(new AttributeCollection(),
						                        "variation",
						                        null);
				salesUnits     = null;
				pricing        = null;
				nutrition      = null;
				nutritionInfo  = null;
	    	}

	    	product  = new FDProduct(sku,
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
