package com.freshdirect.webapp.util.prodconf;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.smartstore.fdstore.RecommendationEventLoggerMockup;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

public class SmartStoreConfigurationStrategyTest extends FDCustomerManagerTestSupport {
	
    private XmlContentService service;

    RecommendationEventLoggerMockup eventLogger;
    

    ConfigurationStrategy strategy;
    // personalized strategy chain
    ConfigurationStrategy personalStrategy;
    


    public SmartStoreConfigurationStrategyTest(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();

		// setup CMS content database
		List list = new ArrayList();
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/smartstore/ConfigurationStrategyTest.xml");

		CmsManager.setInstance(new CmsManager(service, null));




		// set the context factory to the mockejb context factory
		Hashtable          env                = new Hashtable();
		FDStoreProperties.set("fdstore.initialContextFactory", "org.mockejb.jndi.MockContextFactory");

		env.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory() );

		MockContextFactory.setAsInitial();
		aspectSystem.add(new FDFactoryProductAspect());
		aspectSystem.add(new FDFactoryProductInfoAspect());		



		// setup configuration strategy chains
		//   non-personal chain (disregards order line configs)
		strategy = 
				new ConfiguredProductGroupConfigurationStrategy(
						new ConfiguredProductConfigurationStrategy(
								new AutoConfigurationStrategy() ) );


		// personal strategy chain - prefers order line configurations
		personalStrategy = TestSmartStoreConfigurationStrategy.getInstance();
	}
	
	public void tearDown() {
		MockContextFactory.revertSetAsInitial();
	}
	


	/**
	 * Test strategy on auto-configurable and not auto-configurable products
	 */
	public void testAutoConfigurationStrategy () {
		FDConfigurableI conf;

		// [1] not auto-configurable simple product
		conf = getConfiguration(findProduct("product_mea0004561"), false);
		assertNull(conf);

		// [2] auto-configurable simple product
		conf = getConfiguration(findProduct("product_mea0004561a"), false);
		assertNotNull(conf);
		assertEquals(1.0, conf.getQuantity(), 0);
	}


	/**
	 * Test strategy on configured product which can be auto-configured.
	 * Strategy should prefer the preset configuration.
	 */
	public void testConfiguredProductConfiguration() {
		// [3] configured product
		ProductModel prod = findProduct("configured_product_mea0004561a");
		assertNotNull(prod);
		
		// cheat - configured product must be added to cache in order 
		AbstractRecommendationService.addConfiguredProductToCache(prod);
		
		FDConfigurableI conf = getConfiguration( ((ConfiguredProduct)prod).getProduct(), false);
		assertNotNull(conf);
		assertEquals(12.0, conf.getQuantity(), 0);
		// System.out.println("CGG[1]: " + conf.getOptions());
	}
	

	/**
	 * Test strategy on group of configured products which can be auto-configured.
	 * Strategy should prefer the preset configuration.
	 */
	public void testConfiguredProductGroupConfiguration() {
		// [4] configured product group
		ProductModel prod = findProduct("configured_product_group_mea0004561");
		assertNotNull(prod);
		
		// cheat - configured product must be added to cache in order 
		AbstractRecommendationService.addConfiguredProductToCache(prod);

		FDConfigurableI conf = getConfiguration( ((ConfiguredProduct) ((ConfiguredProductGroup)prod).getProduct()).getProduct(), false );
		assertNotNull(conf);
		assertEquals(4.0, conf.getQuantity(), 0);
		// System.out.println("CGG[2]: " + conf.getOptions());

		assertNotSame(conf, ((ConfiguredProduct) ((ConfiguredProductGroup)prod).getProduct()).getConfiguration());
}


	/**
	 * Get a configured product which is also bought with different config.
	 * Strategy should prefer the config the orderline has.
	 */
	public void testPersonalConfiguration() {
		// [5] personal
		ProductModel prod = findProduct("configured_product_mea0004561a");
		assertNotNull(prod);
		
		// cheat - configured product must be added to cache in order 
		AbstractRecommendationService.addConfiguredProductToCache(prod);

		FDConfigurableI conf = getConfiguration( ((ConfiguredProduct)prod).getProduct(), true);
		assertNotNull(conf);
		assertEquals(1.0, conf.getQuantity(), 0);
		// System.out.println("CGG[pers]: " + conf.getOptions());
		assertNotSame(conf, ((ConfiguredProduct)prod ).getConfiguration());
	}





	protected FDConfigurableI getConfiguration(ProductModel prd, boolean personal) {
		ConfigurationContext cs = new ConfigurationContext();
		ProductImpression pi = (personal ?  personalStrategy : strategy).configure(prd, cs);

		return pi instanceof TransactionalProductImpression ? ((TransactionalProductImpression)pi).getConfiguration() : null;
	}
	
	/**
	 * @override
	 */
	protected String[] getAffectedTables() {
		return null;
	}

	/**
	 * @override
	 */
	protected String getSchema() {
		return null;
	}
    
    
	private ProductModel findProduct(String id) {
		return (ProductModel) ContentFactory.getInstance().getContentNode(id);
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
						                           "", inventoryCache,"",1.0,"ea",false,1);
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
						                           "", inventoryCache,"",1.0,"ea",false,1);
			}

			return productInfo;
		}
	}


	/**
	 * Extracted from YmalTest class
	 * 
	 * @author segabor
	 *
	 */
	public static class FDFactoryProductAspect implements Aspect {
		public static final AttributesI			EMPTY_ATTRIBUTES		= new AttributeCollection();

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
	    	return TestFDProduct.createTest(sku, version, "MEA0004561a".equalsIgnoreCase(sku));
	    }
	}
}




/**
 * Strategy Mock-up
 * 
 * @author segabor
 *
 */
class TestSmartStoreConfigurationStrategy extends SmartStoreConfigurationStrategy {
	String customerPK = "12345678";

	// List<FDCustomerProductListLineItem>
	List productList;
	
	public static ConfigurationStrategy getInstance() {
		if (instance == null) {
			setupCache();
			instance = new TestSmartStoreConfigurationStrategy(
					new ConfiguredProductGroupConfigurationStrategy(
							new ConfiguredProductConfigurationStrategy(
									new AutoConfigurationStrategy())));
		}

		// setup a list of bought items with personal configs
		List plist = new ArrayList();
		
		Map opts = new HashMap();
		opts.put("var1", "VariationOption1");
		opts.put("var2", "VariationOption1");
		opts.put("var3", "VariationOption1");
		
		FDConfiguration conf = new FDConfiguration(3.0, "SalesUnit1", opts );

		FDCustomerProductListLineItem item1 = new FDCustomerProductListLineItem("MEA0004561a", conf);
		plist.add(item1);

		((TestSmartStoreConfigurationStrategy)instance).setProductList(plist);

		return instance;
	}


	public void setProductList(List productList) {
		this.productList = productList;
	}


	public TestSmartStoreConfigurationStrategy(ConfigurationStrategy fallback) {
		super(fallback);
	}

	protected String getCustomerPK(ConfigurationContext context) {
		return customerPK;
	}

	// List<FDCustomerProductListLineItem>
	protected List getUserLineItems(String erpCustomerPK, List skuCodes) throws FDResourceException {
		return productList == null ? Collections.EMPTY_LIST : productList;
	}
}



/**
 * Product mock-up
 * 
 * @author segabor
 *
 */
class TestFDProduct extends FDProduct {
	private static final long serialVersionUID = 1L;

	public static final AttributesI			EMPTY_ATTRIBUTES		= new AttributeCollection();

	public static final ErpAffiliate ERP_AFFILIATE = new ErpAffiliate("FD", "FreshDirect", "FreshDirect", "ZT01", "ZBD1", Collections.EMPTY_MAP, Collections.EMPTY_SET );

	public TestFDProduct(String skuCode, int version, Date pricingDate,
			FDMaterial material, FDVariation[] variations,
			FDSalesUnit[] salesUnits, Pricing pricing, ArrayList nutrition) {
		super(skuCode, version, pricingDate, material, variations, salesUnits, pricing,
				nutrition);
	}

	
	public static TestFDProduct createTest(String sku, int version, boolean autoconfig) {
    	Date          now            = new Date();
    	FDMaterial    material       = new FDMaterial(
    			EMPTY_ATTRIBUTES,
    			"000000000123",
    			EnumATPRule.MATERIAL,
    			"", // Sales Unit characteristics
    			"", // 
    			EnumAlcoholicContent.NONE,
    			false, // taxable
    			false, // kosher
    			false, // platter
    			DayOfWeekSet.EMPTY
    	);
		FDVariation[] variations;
		FDSalesUnit[] salesUnits;

		if (autoconfig) {
			// create auto-configurable setting
			final int vopts=3;

			variations     = new FDVariation[vopts];

			for (int i=0; i<vopts; i++) {
				int p=i+1;
				
				// NOTE: auto-configurables must have only one option per variation
		    	FDVariationOption[] variationOptions = new FDVariationOption[1];
		    	variationOptions[0] = new FDVariationOption(EMPTY_ATTRIBUTES,
						"VariationOption1",
						"Test Option 1");

				variations[i] = new FDVariation(EMPTY_ATTRIBUTES, "var"+p, variationOptions);
			}


			salesUnits     = new FDSalesUnit[1];
			salesUnits[0] = new FDSalesUnit(EMPTY_ATTRIBUTES, "SalesUnit1",	"Test Sales Unit 1");
		} else {
			// default case
			variations     = new FDVariation[1];
			variations[0] = new FDVariation(new AttributeCollection(),
					                        "variation",
					                        null);
			
			// multiple sales units?
			salesUnits     = new FDSalesUnit[2];
			salesUnits[0] = new FDSalesUnit(EMPTY_ATTRIBUTES,
					"SalesUnit1",
					"Test Sales Unit 1");
			salesUnits[1] = new FDSalesUnit(EMPTY_ATTRIBUTES,
					"SalesUnit2",
					"Test Sales Unit 2");
		}

		// pricing
		MaterialPrice	mp[]			= {new MaterialPrice(1.0, "SalesUnit1")};
		CharacteristicValuePrice cvp[]	= {};
		SalesUnitRatio sur[]			= {};
		Pricing       pricing        = new Pricing(mp, cvp, sur);

		ArrayList     nutrition      = null;
		
		return new TestFDProduct(
				sku,
                version,
                now,
                material,
                variations,
                salesUnits,
                pricing,
                nutrition);
	}


	public ErpAffiliate getAffiliate() {
		return ERP_AFFILIATE;
	}

	public boolean isAlcohol() {
		return false;
	}

	public boolean isDeliveryPass() {
		return false;
	}
}
