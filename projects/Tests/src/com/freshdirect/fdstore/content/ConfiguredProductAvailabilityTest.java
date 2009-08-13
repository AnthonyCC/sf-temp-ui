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
 * Test case for the availability of ConfiguredProducts.
 */
public class ConfiguredProductAvailabilityTest extends FDCustomerManagerTestSupport {

	public ConfiguredProductAvailabilityTest(String name) {
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
		ConfiguredProduct node = (ConfiguredProduct) findProduct("ok");
		assertNotNull(node);
	}

	/**
	 *  Test the isUnavailable() call.
	 *  
	 *  The test is centered around a ConfiguredProduct, that ponts to a SKU
	 *  that is unavailable. But, the SKU's original product is in fact available,
	 *  as it contains another, available SKU. Despite this fact, the ConfiguredProduct
	 *  is unavailable.
	 */
	public void testUnavailable() {
		ConfiguredProduct node;
		
		node = (ConfiguredProduct) findProduct("ok");
		assertNotNull(node);
		assertFalse(node.isUnavailable());
		
		node = (ConfiguredProduct) findProduct("cp_unavailable");
		assertNotNull(node);
		assertTrue(node.isUnavailable());
	}

	/**
	 *  Test the getEarliestAvailability() call.
	 *  
	 *  The test is centered around a ConfiguredProduct, that ponts to a SKU
	 *  that is unavailable. But, the SKU's original product is in fact available,
	 *  as it contains another, available SKU. Despite this fact, the ConfiguredProduct
	 *  is unavailable, and has no earliest availability.
	 */
	public void testEarliestAvailability() {
		ConfiguredProduct node;
		
		node = (ConfiguredProduct) findProduct("ok");
		assertNotNull(node);
		assertNotNull(node.getEarliestAvailability());
		
		node = (ConfiguredProduct) findProduct("cp_unavailable");
		assertNotNull(node);
		assertNull(node.getEarliestAvailability());
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
			String [] materials = {"000000000000000123"};
			TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
			FDProductInfo productInfo;
			List inventoryEntries = new ArrayList();

			if ("MEA0004562".equals(sku)) {
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
						                           "", inventoryCache,"",1.0,"ea",false,-1,-1);
			} else {
				// return this item as unavailable
				// this SKU is included in the ConfiguredProduct "ok"
				inventoryEntries.add(new ErpInventoryEntryModel(now, 0));
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
