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
import com.freshdirect.framework.util.DateUtil;

/**
 * Test case for the availability of ConfiguredProducts.
 */
public class ConfiguredProductGroupAvailabilityTest extends FDCustomerManagerTestSupport {

	public ConfiguredProductGroupAvailabilityTest(String name) {
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
		ConfiguredProductGroup node = (ConfiguredProductGroup) findProductGroup("cpg_ok");
		assertNotNull(node);
	}

	/**
	 *  Test the isUnavailable() call.
	 *  
	 *  The test is centered around a ConfiguredProductGroup that has both
	 *  an available and an unavailable ConfiguredProduct.	
	 */
	public void testUnavailable() {
		ConfiguredProductGroup node;
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_ok");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_unavailable");
		assertNotNull(node);
		assertTrue(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_unavailable_ok");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_by_tomorrow_1st");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_by_tomorrow_not_1st");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_by_after_tomorrow");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_in_three_days");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_discontinued_ok");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_temp_unav_ok");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_after_horizon");
		assertNotNull(node);
		assertTrue(node.isUnavailable());		
	}

	/**
	 *  Test the getEarliestAvailability() call.
	 */
	public void testEarliestAvailability() {
		ConfiguredProductGroup node;
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_ok");
		assertNotNull(node);
		assertNotNull(node.getEarliestAvailability());
		
		node = (ConfiguredProductGroup) findProductGroup("cpg_unavailable");
		assertNotNull(node);
		assertNull(node.getEarliestAvailability());
		
		// this is a CPG that only has products available after the horizon
		node = (ConfiguredProductGroup) findProductGroup("cpg_after_horizon");
		assertNotNull(node);
		assertNull(node.getEarliestAvailability());
	}

	/**
	 *  Test to see which is the active / available product in the product group.
	 */
	public void testAvailableProduct() {
		ConfiguredProductGroup node;
		
		// in this CPG, the first product is called "ok", and is
		// available as well
		node = (ConfiguredProductGroup) findProductGroup("cpg_ok");
		assertNotNull(node);
		assertEquals("ok", node.getProduct().getContentName());

		// in this CPG, there is no available product
		node = (ConfiguredProductGroup) findProductGroup("cpg_unavailable");
		assertNotNull(node);
		assertNull(node.getProduct());

		// in this CPG, the first product is called "cp_unavailable",
		// but it is unavailable. the second product is called "sam_as_ok",
		// and is available as well - so this is what we'll get
		node = (ConfiguredProductGroup) findProductGroup("cpg_unavailable_ok");
		assertNotNull(node);
		assertEquals("same_as_ok", node.getProduct().getContentName());
		
		// in this CPG, the first product is called "cp_by_tomorrow",
		// and is availabe by tomorrow - so this is what we'll get
		node = (ConfiguredProductGroup) findProductGroup("cpg_by_tomorrow_1st");
		assertNotNull(node);
		assertEquals("cp_by_tomorrow", node.getProduct().getContentName());
		
		// in this CPG, the product available by tomorrow is called "cp_by_tomorrow",
		// but it is not the first in the CPG. all other available items are not
		// available by tomorrow, so we should still get "cp_by_tomorrow"
		node = (ConfiguredProductGroup) findProductGroup("cpg_by_tomorrow_not_1st");
		assertNotNull(node);
		assertEquals("cp_by_tomorrow", node.getProduct().getContentName());
		
		// in this CPG, the product available by after tomorrow is called "cp_by_after_tomorrow",
		// it is the only available product in the CPG, so that's what we'll get
		node = (ConfiguredProductGroup) findProductGroup("cpg_by_after_tomorrow");
		assertNotNull(node);
		assertEquals("cp_by_after_tomorrow", node.getProduct().getContentName());		
		
		// in this CPG, has an item available after tomorrow, but the first available
		// item is available in three days. thus this is the one we get.
		node = (ConfiguredProductGroup) findProductGroup("cpg_in_three_days");
		assertNotNull(node);
		assertEquals("cp_in_three_days", node.getProduct().getContentName());
		
		// this is a CPG, that has a discontinued item along available items
		node = (ConfiguredProductGroup) findProductGroup("cpg_discontinued_ok");
		assertNotNull(node);
		assertEquals("cp_by_after_tomorrow", node.getProduct().getContentName());
		
		// this is a CPG that has a temporarily unavailable item along with
		// available items
		node = (ConfiguredProductGroup) findProductGroup("cpg_temp_unav_ok");
		assertNotNull(node);
		assertEquals("cp_by_after_tomorrow", node.getProduct().getContentName());		
	}

	private ContentNodeModel findProductGroup(String id) {
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
			String[] materials = {"000000000000123"};
			TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
			List inventoryEntries = new ArrayList();
			FDProductInfo productInfo;

			if ("MEA0004561".equals(sku)) {
				// return this item as unavailable
				// this SKU is included in the ConfiguredProduct "ok"
				// a 0 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));

				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);

			} else if ("MEA0004562".equals(sku)) {
				// return this item as available
				// this SKU is not included in the ConfiguredProduct "ok"
				// a 10000 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));

				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else if ("MEA0004563".equals(sku)) {
				// return this item as available by tomorrow, but not today
				Date tomorrow  = DateUtil.addDays(now, 1);
				// a 10000 units available starting tomorrow
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(tomorrow), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
				
				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else if ("MEA0004564".equals(sku)) {
				// return this item as available the day after tomorrow
				Date afterTomorrow = DateUtil.addDays(now, 2);
				// a 10000 units available starting the day after tomorrow
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(afterTomorrow), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else if ("MEA0004565".equals(sku)) {
				// return this item as available the day after tomorrow
				Date inThreeDays   = DateUtil.addDays(now, 3);
				// a 10000 units available starting the day after tomorrow
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(inThreeDays), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.AVAILABLE,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else if ("MEA0004566".equals(sku)) {
				// return a discontinued item
				// a 10000 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));

				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.DISCONTINUED,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else if ("MEA0004567".equals(sku)) {
				// return a temporarily unavailable item
				// a 10000 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.TEMP_UNAV,
						                           now,
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else if ("MEA0004568".equals(sku)) {
				// return an item available after the horizon only
				// a 10000 units available starting after the horizon
				Date afterHorizon  = DateUtil.addDays(now, 10);
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(afterHorizon), 10000));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));
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
				// fallback: return any unknown item as unavailable
				// a 0 units available starting now
				inventoryEntries.add(new ErpInventoryEntryModel(DateUtil.truncate(now), 0));
				inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, inventoryEntries));

				productInfo    = new FDProductInfo(sku,
						                           1,
						                           1.0,
						                           "ea",
						                           materials,
						                           EnumATPRule.MATERIAL,
						                           EnumAvailabilityStatus.DISCONTINUED,
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
			FDVariation[] variations     = new FDVariation[0];
			FDSalesUnit[] salesUnits     = null;
			Pricing       pricing        = null;
			ArrayList     nutrition      = null;
			String rating=null;
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
		return null;
	}

	protected String getSchema() {
		return null;
	}

}
