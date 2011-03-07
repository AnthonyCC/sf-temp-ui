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
import com.freshdirect.fdstore.FDMaterial;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.ProductAutoconfigureTest.FDFactoryProductAspect;
import com.freshdirect.fdstore.content.ProductAutoconfigureTest.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.framework.util.DateUtil;

/**
 * Test case for the availability of Recipes in preview mode.
 */
public class RecipeAvailabilityPreviewTest extends FDCustomerManagerTestSupport {

	public RecipeAvailabilityPreviewTest(String name) {
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
		FDStoreProperties.set("fdstore.preview", "true");

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
		Recipe recipe = (Recipe) findRecipe("rec_available");
		assertNotNull(recipe);
	}

	/**
	 *  Look the isAvailable() call.
	 */
	public void testAvailable() {
		Recipe recipe;
		
		recipe = (Recipe) findRecipe("rec_available");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_unavailable");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_unavailable_ok");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_by_tomorrow_1st");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_by_tomorrow_not_1st");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_by_after_tomorrow");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_in_three_days");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
	}

	/**
	 *  Look the default variant's isAvailable() call.
	 */
	public void testVariantAvailable() {
		Recipe        recipe;
		RecipeVariant variant;
		
		recipe = (Recipe) findRecipe("rec_available");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_unavailable");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_unavailable_ok");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_by_tomorrow_1st");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_by_tomorrow_not_1st");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_by_after_tomorrow");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
		
		recipe = (Recipe) findRecipe("rec_in_three_days");
		assertNotNull(recipe);
		assertTrue(recipe.isAvailable());
		variant = recipe.getDefaultVariant();
		assertNotNull(variant);
		assertTrue(variant.isAvailable());
	}
	
	private ContentNodeModel findRecipe(String id) {
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
			FDProductInfo productInfo;

			if ("MEA0004561".equals(sku)) {
				// return this item as unavailable
				// this SKU is included in the ConfiguredProduct "ok"
				// a 0 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(now, 0));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"",null, dummyList, null,"");


			} else if ("MEA0004562".equals(sku)) {
				// return this item as available
				// this SKU is not included in the ConfiguredProduct "ok"
				// a 10000 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(now, 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"",null, dummyList, null,"");

			} else if ("MEA0004563".equals(sku)) {
				// return this item as available by tomorrow, but not today
				Date tomorrow  = DateUtil.addDays(now, 1);
				// a 10000 units available starting tomorrow
				inventoryEntries.add(new ErpInventoryEntryModel(tomorrow, 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"",null,dummyList,null,"");

			} else if ("MEA0004564".equals(sku)) {
				// return this item as available the day after tomorrow
				Date afterTomorrow = DateUtil.addDays(now, 2);
				// a 10000 units available starting the day after tomorrow
				inventoryEntries.add(new ErpInventoryEntryModel(afterTomorrow, 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));

	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"",null,dummyList,null,"");

			} else if ("MEA0004565".equals(sku)) {
				// return this item as available the day after tomorrow
				Date inThreeDays   = DateUtil.addDays(now, 3);
				// a 10000 units available starting the day after tomorrow
				inventoryEntries.add(new ErpInventoryEntryModel(inThreeDays, 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, -1, -1, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"",null,dummyList,null,"");

			} else if ("MEA0004566".equals(sku)) {
				// return a discontinued item
				// a 10000 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.DISCONTINUED, now,inventoryCache,"",null,dummyList,null,"");

			} else if ("MEA0004567".equals(sku)) {
				// return a temporarily unavailable item
				// a 10000 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.TEMP_UNAV, now,inventoryCache,"",null,dummyList,null,"");

			} else {
				// fallback: return any unknown item as unavailable
				// a 0 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
	    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
	    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
	    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
	            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.DISCONTINUED, now,inventoryCache,"",null,dummyList,null,"");
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
		    			                           nutrition,null);
	    	
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
